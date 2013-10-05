package com.squeed.chromecast.hipstacaster.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.galleries.GalleriesInterface;
import com.flickr4java.flickr.galleries.Gallery;
import com.flickr4java.flickr.people.User;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.SearchParameters;
import com.flickr4java.flickr.photos.Size;
import com.squeed.chromecast.hipstacaster.dto.TinyPhotoDTO;
import com.squeed.chromecast.hipstacaster.exception.HipstaException;

/**
 * Created with IntelliJ IDEA.
 * User: Erik
 * Date: 2013-10-03
 * Time: 09:11
 * To change this template use File | Settings | File Templates.
 */
@Component
public class FlickrController {

    Logger log = LoggerFactory.getLogger(FlickrController.class);

    private String apiKey = "xxx";
    private String sharedSecret = "yyy";

    private Flickr f;

    private PhotosInterface photoInterface;
    private GalleriesInterface galleriesInterface;

    private static final HashSet<String> extras = new HashSet<>();
    {
        extras.add("url_t");
        extras.add("url_s");
        extras.add("url_m");
        extras.add("url_o");
        extras.add("url_sq");
        extras.add("owner_name");
    }

    @PostConstruct
    public void init() {

        Transport transport = new REST();
        f = new Flickr(apiKey, sharedSecret, transport);

        photoInterface = f.getPhotosInterface();
        galleriesInterface = f.getGalleriesInterface();
    }


    public List<TinyPhotoDTO> search(String tags) throws HipstaException {
        List<TinyPhotoDTO> list = new ArrayList<>();
        SearchParameters params = new SearchParameters();
        params.setTags(toArray(tags));
        params.setExtras(extras);

        try {
            long start = System.currentTimeMillis();
            PhotoList<Photo> searchResult = photoInterface.search(params, 20, 0);
            log.info("Reading 20 photos over interface took {} ms", (System.currentTimeMillis() - start));
            start = System.currentTimeMillis();
            toTinyPhotoDTOs(list, searchResult);
            log.info("Reading 20 photosizes over interface took {} ms", (System.currentTimeMillis() - start));
            return list;
        } catch (FlickrException e) {
            throw new HipstaException(e.getMessage());
        }
    }

    private String[] toArray(String tags) {
        if(tags.contains(",")) {
            return tags.split(",");
        }
        return new String[]{tags};
    }

    public List<Gallery> getPopularAlbums() throws HipstaException {
        List<Gallery> galleries = new ArrayList<>();

        SearchParameters params = new SearchParameters();

        int added = 0;

        try {
            PhotoList<Photo> photoList = photoInterface.searchInterestingness(params, 50, 0);

            for(int a = 0; a < photoList.size() && added < 10; a++) {
                Photo photo = photoList.get(a);
                User owner = photo.getOwner();
                List<Gallery> galleryList = galleriesInterface.getList(owner.getId(), 1, 0);
                if(galleryList.size() > 0) {
                    galleries.add(galleryList.get(0));
                    added++;
                }
            }
        } catch (FlickrException e) {
            throw new HipstaException(e.getMessage());
        }

        return galleries;
    }

    public Gallery getAlbum(String albumId) throws HipstaException {
        try {
            return galleriesInterface.getInfo(albumId);
        } catch (FlickrException e) {
            throw new HipstaException(e.getMessage());
        }
    }

    public List<TinyPhotoDTO> getPhotosOfAlbum(String albumId) throws HipstaException {
        List<TinyPhotoDTO> list = new ArrayList<>();
        try {
            PhotoList<Photo> photos = galleriesInterface.getPhotos(albumId, new HashSet<String>(), 100, 0);
            toTinyPhotoDTOs(list, photos);

            return list;
        } catch (FlickrException e) {
            throw new HipstaException(e.getMessage());
        }
    }

    private void toTinyPhotoDTOs(List<TinyPhotoDTO> list, PhotoList<Photo> photos) throws FlickrException {
        for(int a = 0; a < photos.size(); a++) {
            TinyPhotoDTO dto = new TinyPhotoDTO(photos.get(a));

            list.add(dto);
        }
    }

    public TinyPhotoDTO getPhoto(String photoId) throws HipstaException {
        try {
            Photo photo = photoInterface.getPhoto(photoId);
            TinyPhotoDTO dto = new TinyPhotoDTO(photo);
            fetchAndApplySizes(photoId, dto);
            return dto;
        } catch (FlickrException e) {
            throw new HipstaException(e.getMessage());
        }
    }

    private void fetchAndApplySizes(String photoId, TinyPhotoDTO dto) throws FlickrException {
        Collection<Size> sizes = photoInterface.getSizes(photoId);

        for(Size size : sizes) {
            if(size.getLabel() == Size.THUMB) {
                dto.setThumbnailUrl(size.getSource());
            }
            if(size.getLabel() == Size.SQUARE) {
                dto.setSquareUrl(size.getSource());
            }
            if(size.getLabel() == Size.LARGE) {
                dto.setFullsizeUrl(size.getSource());
            }
        }
    }
}
