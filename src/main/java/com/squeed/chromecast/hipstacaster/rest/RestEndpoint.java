package com.squeed.chromecast.hipstacaster.rest;

import com.squeed.chromecast.hipstacaster.controller.FlickrController;
import com.squeed.chromecast.hipstacaster.dto.PhotoDTO;
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

    /**
     * Search for photos having the specified tag(s). Uses {@link FlickrController} internally, but could
     * theoretically query more photo services in the distant future.
     *
     * @param tags
     *      One or more comma-separated tags to search for.
     * @param page
     *      Page offset, e.g. 0 is first page.
     * @param perPage
     *      Number of items to return
     * @return
     *      A List of {@TinyPhotoDTO}s. These will contain direct URLs to thumbnail and fullsize versions of the image,
     *      title, owner etc.
     * @throws HipstaException
     */
    @GET
    @Path("/photos/search/{tags}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(@PathParam("tags") String tags, @QueryParam("page") Integer page, @QueryParam("perPage") Integer perPage) throws HipstaException {
        List<PhotoDTO> l = flickrController.search(tags, page, perPage);
        return Response.ok(l).build();
    }

    /**
     * Returns data about a single photo identified by the supplied id.
     *
     * @param photoId
     * @return
     * @throws HipstaException
     */
    @GET
    @Path("/photos/{photoId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPhoto(@PathParam("photoId") String photoId) throws HipstaException {
        PhotoDTO photo = flickrController.getPhoto(photoId);
        return Response.ok(photo).build();
    }
}
