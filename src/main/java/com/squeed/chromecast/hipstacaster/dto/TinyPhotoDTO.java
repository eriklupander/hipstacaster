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
    private String description;
    private String ownerName;

    private String thumbnailUrl;
    private String fullsizeUrl;
    private String squareUrl;

    private Integer xSize;
    private Integer ySize;

    public TinyPhotoDTO() {}

    public TinyPhotoDTO(Photo photo) {
        this.title = photo.getTitle();
        this.description = photo.getDescription();
        this.url = photo.getUrl();
        this.id = photo.getId();
        this.ownerName = photo.getOwner().getRealName() != null ? photo.getOwner().getRealName() : photo.getOwner().getUsername();

        if(photo.getSmallSize() != null) {
            this.setThumbnailUrl(photo.getThumbnailSize().getSource());
            resolveLargestSize(photo);
            this.setSquareUrl(photo.getSquareSize().getSource());
        }
    }

    private void resolveLargestSize(Photo photo) {
        if(photo.getLarge1600Size() != null) {
            this.setFullsizeUrl(photo.getLarge1600Size().getSource());
            this.xSize = photo.getLarge1600Size().getWidth();
            this.ySize = photo.getLarge1600Size().getHeight();
        }
        else if(photo.getLargeSize() != null) {
            this.setFullsizeUrl(photo.getLargeSize().getSource());
            this.xSize = photo.getLargeSize().getWidth();
            this.ySize = photo.getLargeSize().getHeight();
        }
        else if(photo.getLarge2048Size() != null) {
            this.setFullsizeUrl(photo.getLarge2048Size().getSource());
            this.xSize = photo.getLarge2048Size().getWidth();
            this.ySize = photo.getLarge2048Size().getHeight();
        }
        else if(photo.getMedium800Size() != null) {
            this.setFullsizeUrl(photo.getMedium800Size().getSource());
            this.xSize = photo.getMedium800Size().getWidth();
            this.ySize = photo.getMedium800Size().getHeight();
        }
        else if(photo.getMediumSize() != null) {
            this.setFullsizeUrl(photo.getMediumSize().getSource());
            this.xSize = photo.getMediumSize().getWidth();
            this.ySize = photo.getMediumSize().getHeight();
        }
        else {
            this.setFullsizeUrl(photo.getOriginalSize().getSource());
            this.xSize = photo.getOriginalSize().getWidth();
            this.ySize = photo.getOriginalSize().getHeight();
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

    public Integer getxSize() {
        return xSize;
    }

    public void setxSize(Integer xSize) {
        this.xSize = xSize;
    }

    public Integer getySize() {
        return ySize;
    }

    public void setySize(Integer ySize) {
        this.ySize = ySize;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
