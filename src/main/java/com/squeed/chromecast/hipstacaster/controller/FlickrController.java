package com.squeed.chromecast.hipstacaster.controller;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.galleries.GalleriesInterface;
import com.flickr4java.flickr.galleries.Gallery;
import com.flickr4java.flickr.people.User;
import com.flickr4java.flickr.photos.*;
import com.flickr4java.flickr.stats.StatsInterface;
import com.squeed.chromecast.hipstacaster.dto.TinyPhotoDTO;
import com.squeed.chromecast.hipstacaster.exception.HipstaException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Erik
 * Date: 2013-10-03
 * Time: 09:11
 * To change this template use File | Settings | File Templates.
 */
@Component
public class FlickrController {

    private String apiKey = "yourkey";
    private String sharedSecret = "yoursecret";

    private Flickr f;

    private PhotosInterface photoInterface;
    private GalleriesInterface galleriesInterface;

    @PostConstruct
    public void init() {

        Transport transport = new REST();
        f = new Flickr(apiKey, sharedSecret, transport);

        photoInterface = f.getPhotosInterface();
        galleriesInterface = f.getGalleriesInterface();
    }

    public List<Gallery> getPopularAlbums() throws HipstaException {
        List<Gallery> galleries = new ArrayList<>();

        SearchParameters params = new SearchParameters();

        try {
            PhotoList<Photo> photoList = photoInterface.searchInterestingness(params, 10, 0);

            for(Photo photo : photoList) {
                User owner = photo.getOwner();
                List<Gallery> galleryList = galleriesInterface.getList(owner.getId(), 1, 0);
                if(galleryList.size() > 0) {
                    galleries.add(galleryList.get(0));
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

    public PhotoList<Photo> getPhotosOfAlbum(String albumId) throws HipstaException {
        try {
            return galleriesInterface.getPhotos(albumId, new HashSet<String>(), 100, 0);
        } catch (FlickrException e) {
            throw new HipstaException(e.getMessage());
        }
    }

    public TinyPhotoDTO getPhoto(String photoId) throws HipstaException {
        try {
            Photo photo = photoInterface.getPhoto(photoId);
            TinyPhotoDTO dto = new TinyPhotoDTO(photo);
            Collection<Size> sizes = photoInterface.getSizes(photoId);

            for(Size size : sizes) {
                if(size.getLabel() == Size.THUMB) {
                    dto.setThumbnailUrl(size.getSource());
                }
                if(size.getLabel() == Size.LARGE) {
                    dto.setFullsizeUrl(size.getSource());
                }
            }
            return dto;
        } catch (FlickrException e) {
            throw new HipstaException(e.getMessage());
        }
    }
}
