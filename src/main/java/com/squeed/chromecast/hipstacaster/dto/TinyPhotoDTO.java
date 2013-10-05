package com.squeed.chromecast.hipstacaster.dto;

import com.flickr4java.flickr.photos.Photo;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Erik
 * Date: 2013-10-03
 * Time: 14:34
 * To change this template use File | Settings | File Templates.
 */
public class TinyPhotoDTO implements Serializable {
    private String id;
    private String url;
    private String title;
    private String ownerName;

    private String smallsizeUrl;
    private String thumbnailUrl;
    private String fullsizeUrl;
    private String squareUrl;

    public TinyPhotoDTO() {}

    public TinyPhotoDTO(Photo photo) {
        this.title = photo.getTitle();
        this.url = photo.getUrl();
        this.id = photo.getId();
        this.ownerName = photo.getOwner().getRealName() != null ? photo.getOwner().getRealName() : photo.getOwner().getUsername();

        if(photo.getSmallSize() != null) {
            this.setThumbnailUrl(photo.getThumbnailSize().getSource());
            this.setSmallsizeUrl(photo.getSmallSize().getSource());
            this.setFullsizeUrl(photo.getOriginalSize().getSource());
            this.setSquareUrl(photo.getSquareSize().getSource());
        }
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getSmallsizeUrl() {
        return smallsizeUrl;
    }

    public void setSmallsizeUrl(String smallsizeUrl) {
        this.smallsizeUrl = smallsizeUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getFullsizeUrl() {
        return fullsizeUrl;
    }

    public void setFullsizeUrl(String fullsizeUrl) {
        this.fullsizeUrl = fullsizeUrl;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getSquareUrl() {
        return squareUrl;
    }

    public void setSquareUrl(String squareUrl) {
        this.squareUrl = squareUrl;
    }
}
