package com.squeed.chromecast.hipstacaster.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;

import com.squeed.chromecast.hipstacaster.dto.PhotoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.SearchParameters;
import com.flickr4java.flickr.photos.Size;
import com.squeed.chromecast.hipstacaster.exception.HipstaException;

/**
 * Interfaces our own REST endpoint with Flickr API:s.
 */
@Component
public class FlickrController {

    Logger log = LoggerFactory.getLogger(FlickrController.class);

    private String apiKey = "abc";
    private String sharedSecret = "def";

    private Flickr f;

    private PhotosInterface photoInterface;

    private static final HashSet<String> extras = new HashSet<>();
    {
        extras.add("url_l");     // large url
        extras.add("url_m");     // medium url
        extras.add("url_o");     // original url
        extras.add("url_sq");    // square url
        extras.add("owner_name");
        extras.add("description");
    }

    @PostConstruct
    public void init() {

        Transport transport = new REST();
        f = new Flickr(apiKey, sharedSecret, transport);

        photoInterface = f.getPhotosInterface();
    }


    public List<PhotoDTO> search(String tags, Integer page, Integer perPage) throws HipstaException {
        List<PhotoDTO> list = new ArrayList<>();
        SearchParameters params = new SearchParameters();
        params.setTags(toArray(tags));
        params.setExtras(extras);

        try {
            long start = System.currentTimeMillis();
            PhotoList<Photo> searchResult = photoInterface.search(params, perPage, page);
            log.info("Reading 20 photos over interface took {} ms", (System.currentTimeMillis() - start));
            toTinyPhotoDTOs(list, searchResult);
            return list;
        } catch (FlickrException e) {
            throw new HipstaException(e.getMessage());
        }
    }

    public PhotoDTO getPhoto(String photoId) throws HipstaException {
        try {
            Photo photo = photoInterface.getInfo(photoId, null);
            PhotoDTO dto = new PhotoDTO(photo);
            fetchAndApplySizes(photoId, dto);
            return dto;
        } catch (FlickrException e) {
            throw new HipstaException(e.getMessage());
        }
    }

    private String[] toArray(String tags) {
        if(tags == null || tags.trim().length() == 0) {
            throw new IllegalArgumentException("Cannot convert an empty tags string into an array.");
        }
        if(tags.contains(",")) {
            return tags.split(",");
        }
        return new String[]{tags};
    }

    private void toTinyPhotoDTOs(List<PhotoDTO> list, PhotoList<Photo> photos) throws FlickrException {
        for(int a = 0; a < photos.size(); a++) {
            list.add(new PhotoDTO(photos.get(a)));
        }
    }

    private void fetchAndApplySizes(String photoId, PhotoDTO dto) throws FlickrException {
        Collection<Size> sizes = photoInterface.getSizes(photoId);

        for(Size size : sizes) {
            if(size.getLabel() == Size.SQUARE) {
                dto.setSquareUrl(size.getSource());
            }
            if(size.getLabel() == Size.MEDIUM_800) {
                dto.setFullsizeUrl(size.getSource());
            }
        }
    }
}
