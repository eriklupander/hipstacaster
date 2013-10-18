package com.squeed.chromecast.hipstacaster.img;

import android.graphics.Bitmap;

/**
 * Defines a callback to run after a (new) bitmap is available to render into a position on the gridView.
 * @author Erik
 *
 */
public interface Callback {
	
	void updateGridView(Bitmap bitmap, int position);

}
