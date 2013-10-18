package com.squeed.chromecast.hipstacaster.grid;

import android.graphics.Bitmap;


public class ImageItem {
    private Bitmap image;
    private String title;
    private String url;
    private String ownerName;
    private String description;

    public ImageItem(Bitmap image, String title, String url, String ownerName, String description) {
        super();
        this.image = image;
        this.title = title;
        this.url = url;
        this.ownerName = ownerName;
        this.description = description;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
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
