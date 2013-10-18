package com.squeed.chromecast.hipstacaster.dto;

/**
 * Created with IntelliJ IDEA.
 * User: Erik
 * Date: 2013-10-05
 * Time: 22:48
 * To change this template use File | Settings | File Templates.
 */
public class Photo {

    private String ownerName;
    private String fullsizeUrl;
    private String squareUrl;
    private String title;
    private String description;

    public Photo() {}

    public Photo(String ownerName, String fullsizeUrl, String squareUrl, String title, String description) {
        this.ownerName = ownerName;
        this.fullsizeUrl = fullsizeUrl;
        this.squareUrl = squareUrl;
        this.title = title;
        this.description = description;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getFullsizeUrl() {
        return fullsizeUrl;
    }

    public void setFullsizeUrl(String fullsizeUrl) {
        this.fullsizeUrl = fullsizeUrl;
    }

    public String getSquareUrl() {
        return squareUrl;
    }

    public void setSquareUrl(String squareUrl) {
        this.squareUrl = squareUrl;
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
