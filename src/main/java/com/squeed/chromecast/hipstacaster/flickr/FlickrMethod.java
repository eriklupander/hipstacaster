package com.squeed.chromecast.hipstacaster.flickr;

/**
 * Created with IntelliJ IDEA.
 * User: Erik
 * Date: 2013-10-13
 * Time: 12:30
 * To change this template use File | Settings | File Templates.
 */
public enum FlickrMethod {
    PHOTO_SEARCH("flickr.photos.search"), PHOTO_GETINFO("flickr.photos.getInfo");

    private String method;

    FlickrMethod(String method) {
        this.method = method;
    }

    public String method() {
        return this.method;
    }
}
