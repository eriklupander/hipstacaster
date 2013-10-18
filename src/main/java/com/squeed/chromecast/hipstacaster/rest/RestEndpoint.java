package com.squeed.chromecast.hipstacaster.rest;

import com.flickr4java.flickr.galleries.Gallery;
import com.squeed.chromecast.hipstacaster.controller.FlickrController;
import com.squeed.chromecast.hipstacaster.dto.TinyPhotoDTO;
import com.squeed.chromecast.hipstacaster.exception.HipstaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    @Path("/photos/search/{tags}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(@PathParam("tags") String tags, @QueryParam("page") Integer page, @QueryParam("perPage") Integer perPage) throws HipstaException {
        List<TinyPhotoDTO> l = flickrController.search(tags, page);
        return Response.ok(l).build();
    }

    @GET
    @Path("/photos/{photoId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPhoto(@PathParam("photoId") String photoId) throws HipstaException {
        TinyPhotoDTO photo = flickrController.getPhoto(photoId);
        return Response.ok(photo).build();
    }
}
