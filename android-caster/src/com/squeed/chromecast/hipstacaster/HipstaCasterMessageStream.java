package com.squeed.chromecast.hipstacaster;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.cast.MessageStream;
import com.squeed.chromecast.hipstacaster.grid.ImageItem;

/**
 * An abstract class which encapsulates control for sending and receiving messages to/from the receiver app.
 * 
 * Derived from the tic-tac-tie example at: https://github.com/googlecast/cast-android-tictactoe
 */
public abstract class HipstaCasterMessageStream extends MessageStream {
	private static final String TAG = HipstaCasterMessageStream.class.getSimpleName();

	private static final String HIPSTACASTER_NAMESPACE = "com.squeed.chromecast.hipstacaster";

	// Receivable event types
	private static final String KEY_EVENT = "event";
	private static final String KEY_ERROR = "error";
	private static final String KEY_MESSAGE = "message";
	
	private static final String KEY_COMMAND = "command";
	
	private static final String KEY_TEXT = "text";
	private static final String KEY_SLIDESHOW_ENDED = "slideshow_ended";
	private static final String KEY_SLIDESHOW_CURRENT_IMAGE_MSG = "slideshow_current_image";


	/**
	 * Constructs a new HipstaCasterMessageStream with HIPSTACASTER_NAMESPACE as the namespace
	 * used by the superclass.
	 */
	protected HipstaCasterMessageStream() {
		super(HIPSTACASTER_NAMESPACE);
	}

	protected abstract void onSlideShowEnded();
	protected abstract void onCurrentSlideShowImageMessage(String message);
	protected abstract void onError(String errorMessage);
	
	/**
	 * Sends image data for a single image to the cast device
	 * @param title
	 * 		Title of the photo.
	 * @param url
	 * 		The URL from where to load the actual image.
	 * @param ownerName
	 * 		Real name or username of the owner of the photo, as provided by the 3rd party photo service. (E.g. flickr)
	 * @param description
	 * 		Textual description of the photo. If null, the receiver app should handle that.
	 */
	public final void openPhotoOnChromecast(String title, String url, String ownerName, String description) {
        try {
            Log.d(TAG, "openPhotoOnChromecast: " + url);
            JSONObject payload = new JSONObject();
            payload.put(KEY_COMMAND, "viewphoto");
            payload.put("fullsizeUrl", url);
            payload.put("ownerName", ownerName);
            payload.put("title", title);
            payload.put("description", description);
           
            sendMessage(payload);
        } catch (JSONException e) {
            Log.e(TAG, "Cannot parse or serialize data for openPhotoOnChromecast", e);
        } catch (IOException e) {
            Log.e(TAG, "Unable to send openPhotoOnChromecast message", e);
        } catch (IllegalStateException e) {
            Log.e(TAG, "Message Stream is not attached", e);
        }
    }
	
	/**
	 * Sends a full set of data for images to the cast device.
	 * @param photoSet
	 * 		Photo data.
	 * @param offset
	 * 		Supply a starting offset for the cast device to use for starting the slideshow at some none 0 index.
	 */
	public final void sendPhotoSetToChromecast(ArrayList<ImageItem> photoSet, int offset) {
		try {
            Log.d(TAG, "sendPhotoSetToChromecast");
            JSONArray array = new JSONArray();
            for(ImageItem imageItem : photoSet) {
            	JSONObject payload = new JSONObject();
            	
                payload.put("fullsizeUrl", imageItem.getUrl());
                payload.put("ownerName", imageItem.getOwnerName());
                payload.put("title", imageItem.getTitle());
                payload.put("description", imageItem.getDescription());
                array.put(payload);
            }
            
            JSONObject obj = new JSONObject();
            obj.put(KEY_COMMAND, "slideshow");
            obj.put("photoSet", array);
            obj.put("offset", offset);
           
            sendMessage(obj);
        } catch (JSONException e) {
            Log.e(TAG, "Cannot parse or serialize data for sendPhotoSetToChromecast", e);
        } catch (IOException e) {
            Log.e(TAG, "Unable to send a sendPhotoSetToChromecast message", e);
        } catch (IllegalStateException e) {
            Log.e(TAG, "Message Stream is not attached", e);
        }
	}

	@Override
	public void onMessageReceived(JSONObject message) {
		try {
			Log.d(TAG, "onMessageReceived: " + message);
			if (message.has(KEY_EVENT)) {
				String event = message.getString(KEY_EVENT);
				if (KEY_ERROR.equals(event)) {
					Log.d(TAG, "ERROR");
					try {
						String errorMessage = message.getString(KEY_MESSAGE);
						onError(errorMessage);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				else if (KEY_SLIDESHOW_ENDED.equals(event)) {
					onSlideShowEnded();
				}
				else if (KEY_SLIDESHOW_CURRENT_IMAGE_MSG.equals(event)) {
					onCurrentSlideShowImageMessage(message.getString(KEY_TEXT));
				}
			} else {
				Log.w(TAG, "Unknown message: " + message);
			}
		} catch (JSONException e) {
			Log.w(TAG, "Message doesn't contain an expected key.", e);
		}
	}	
}
