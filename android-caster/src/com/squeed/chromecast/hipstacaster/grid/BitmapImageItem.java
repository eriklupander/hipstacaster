package com.squeed.chromecast.hipstacaster.grid;

import android.graphics.Bitmap;

public class BitmapImageItem extends ImageItem {

	private Bitmap image;

	public BitmapImageItem(Bitmap image, String title, String url,
			String ownerName, String description) {
		super(title, url, ownerName, description);
		this.image = image;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}
	
	

}
