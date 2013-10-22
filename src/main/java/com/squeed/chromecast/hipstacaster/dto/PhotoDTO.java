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
public class PhotoDTO implements Serializable {
    private String id;
    private String url;
    private String title;
    private String description;
    private String ownerName;

    private String fullsizeUrl;
    private String squareUrl;

    public PhotoDTO() {}

    public PhotoDTO(Photo photo) {
        this.title = photo.getTitle();
        this.description = photo.getDescription();
        this.url = photo.getUrl();
        this.id = photo.getId();
        this.ownerName = photo.getOwner().getRealName() != null ? photo.getOwner().getRealName() : photo.getOwner().getUsername();

        if(photo.getSmallSize() != null) {
            resolveLargestSize(photo);
            this.setSquareUrl(photo.getSquareSize().getSource());
        }
    }

    private void resolveLargestSize(Photo photo) {
        if(photo.getLarge1600Size() != null) {
            this.setFullsizeUrl(photo.getLarge1600Size().getSource());
        }
        else if(photo.getLargeSize() != null) {
            this.setFullsizeUrl(photo.getLargeSize().getSource());
        }
        else if(photo.getLarge2048Size() != null) {
            this.setFullsizeUrl(photo.getLarge2048Size().getSource());
        }
        else if(photo.getMedium800Size() != null) {
            this.setFullsizeUrl(photo.getMedium800Size().getSource());
        }
        else if(photo.getMediumSize() != null) {
            this.setFullsizeUrl(photo.getMediumSize().getSource());
        }
        else {
            this.setFullsizeUrl(photo.getOriginalSize().getSource());
        }
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
