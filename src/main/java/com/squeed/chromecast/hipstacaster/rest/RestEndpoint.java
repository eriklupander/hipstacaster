package com.squeed.chromecast.hipstacaster.rest;

import com.flickr4java.flickr.galleries.Gallery;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.squeed.chromecast.hipstacaster.controller.FlickrController;
import com.squeed.chromecast.hipstacaster.dto.PhotoDTO;
import com.squeed.chromecast.hipstacaster.dto.TinyPhotoDTO;
import com.squeed.chromecast.hipstacaster.exception.HipstaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: Erik
 * Date: 2013-10-03
 * Time: 08:28
 *
 * REST endpoint for the aggregate flickr / hipstacaster service.
 */
@Path("/")
@Component
public class RestEndpoint {

    @Autowired
    FlickrController flickrController;

    @GET
    @Path("/albums/popular")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPopularAlbums() throws HipstaException {
        List<Gallery> l = flickrController.getPopularAlbums();
        return Response.ok(l).build();
    }

    @GET
    @Path("/albums/{albumId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAlbum(@PathParam("albumId") String albumId) throws HipstaException {
        Gallery g = flickrController.getAlbum(albumId);
        return Response.ok(g).build();
    }

    @GET
    @Path("/albums/{albumId}/photos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPhotosOfAlbum(@PathParam("albumId") String albumId) throws HipstaException {
        PhotoList<Photo> photoList = flickrController.getPhotosOfAlbum(albumId);
        List<TinyPhotoDTO> l = new ArrayList<>();
        for(Photo p : photoList) {
            l.add(new TinyPhotoDTO(p));
        }
        return Response.ok(l).build();
    }

    @GET
    @Path("/albums/{albumId}/photos/{photoId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPhotoOfAlbum(@PathParam("albumId") String albumId, @PathParam("photoId") String photoId) throws HipstaException {
        TinyPhotoDTO photo = flickrController.getPhoto(photoId);
        return Response.ok(photo).build();
    }

    @GET
    @Path("/photos/{photoId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPhoto(@PathParam("photoId") String photoId) throws HipstaException {
        TinyPhotoDTO photo = flickrController.getPhoto(photoId);
        return Response.ok(photo).build();
    }
}
