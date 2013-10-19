package com.squeed.chromecast.hipstacaster.grid;



public class ImageItem {
	
    private String title;
    private String url;
    private String ownerName;
    private String description;

    public ImageItem(String title, String url, String ownerName, String description) {
        super();
        this.title = title;
        this.url = url;
        this.ownerName = ownerName;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
