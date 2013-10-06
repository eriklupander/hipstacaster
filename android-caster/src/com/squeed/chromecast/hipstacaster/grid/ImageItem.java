package com.squeed.chromecast.hipstacaster.grid;

import android.graphics.Bitmap;


public class ImageItem {
    private Bitmap image;
    private String title;
    private String url;

    public ImageItem(Bitmap image, String title, String url) {
        super();
        this.image = image;
        this.title = title;
        this.url = url;
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
}
