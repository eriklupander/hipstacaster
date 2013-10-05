package com.squeed.chromecast.hipstacaster;

/*
 * Copyright (C) 2013 Google Inc. All Rights Reserved. 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

import android.util.Log;
import com.google.cast.MessageStream;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * An abstract class which encapsulates control for sending and receiving messages to/from the receiver app
 */
public abstract class HipstaCasterMessageStream extends MessageStream {
	private static final String TAG = HipstaCasterMessageStream.class.getSimpleName();

	private static final String GAME_NAMESPACE = "com.squeed.chromecast.hipstacaster";

	// Receivable event types
	private static final String KEY_EVENT = "event";
	private static final String KEY_ERROR = "error";
	private static final String KEY_MESSAGE = "message";

	// Commands

	/**
	 * Constructs a new GameMessageStream with GAME_NAMESPACE as the namespace
	 * used by the superclass.
	 */
	protected HipstaCasterMessageStream() {
		super(GAME_NAMESPACE);
	}

	protected abstract void onError(String errorMessage);
	protected abstract void start();
	protected abstract void end();

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
			} else {
				Log.w(TAG, "Unknown message: " + message);
			}
		} catch (JSONException e) {
			Log.w(TAG, "Message doesn't contain an expected key.", e);
		}
	}

	
}
