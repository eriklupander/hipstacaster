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

    private String thumbnailUrl;
    private String fullsizeUrl;
    private String squareUrl;

    private String nextPhotoId;
    private String prevPhotoId;

    public TinyPhotoDTO() {}

    public TinyPhotoDTO(Photo photo) {
        this.title = photo.getTitle();
        this.url = photo.getUrl();
        this.id = photo.getId();
        this.ownerName = photo.getOwner().getRealName() != null ? photo.getOwner().getRealName() : photo.getOwner().getUsername();
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
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

    public String getNextPhotoId() {
        return nextPhotoId;
    }

    public void setNextPhotoId(String nextPhotoId) {
        this.nextPhotoId = nextPhotoId;
    }

    public String getPrevPhotoId() {
        return prevPhotoId;
    }

    public void setPrevPhotoId(String prevPhotoId) {
        this.prevPhotoId = prevPhotoId;
    }

    public String getSquareUrl() {
        return squareUrl;
    }

    public void setSquareUrl(String squareUrl) {
        this.squareUrl = squareUrl;
    }
}
