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

import com.google.cast.ApplicationChannel;
import com.google.cast.ApplicationMetadata;
import com.google.cast.ApplicationSession;
import com.google.cast.CastContext;
import com.google.cast.CastDevice;
import com.google.cast.Logger;
import com.google.cast.MediaRouteAdapter;
import com.google.cast.MediaRouteHelper;
import com.google.cast.MediaRouteStateChangeListener;
import com.google.cast.SessionError;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.MediaRouteActionProvider;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.support.v7.media.MediaRouter.RouteInfo;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;

/**
 * An activity which both presents a UI on the first screen and casts the TicTacToe game board to
 * the selected Cast device and its attached second screen.
 */
public class GameActivity extends Activity implements MediaRouteAdapter {
    private static final String TAG = GameActivity.class.getSimpleName();
    private static final Logger sLog = new Logger(TAG, true);
    private static final String APP_NAME = "HipstaCaster";

    private ApplicationSession mSession;
    private SessionListener mSessionListener;
    private CustomHipstaCasterStream mMessageStream;

   
    private TextView mInfoView;

    private CastContext mCastContext;
    private CastDevice mSelectedDevice;
    private MediaRouter mMediaRouter;
    private MediaRouteSelector mMediaRouteSelector;
    private MediaRouter.Callback mMediaRouterCallback;

    /**
     * Called when the activity is first created. Initializes the game with necessary listeners
     * for player interaction, and creates a new message stream.
     */
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.game);

      
        mInfoView = (TextView) findViewById(R.id.info_turn);
        mPlayerNameView = (TextView) findViewById(R.id.player_name);

        mSessionListener = new SessionListener();
        mMessageStream = new CustomHipstaCasterStream();

        mCastContext = new CastContext(getApplicationContext());
        MediaRouteHelper.registerMinimalMediaRouteProvider(mCastContext, this);
        mMediaRouter = MediaRouter.getInstance(getApplicationContext());
        mMediaRouteSelector = MediaRouteHelper.buildMediaRouteSelector(
                MediaRouteHelper.CATEGORY_CAST, APP_NAME, null);
        mMediaRouterCallback = new MediaRouterCallback();
    }

    /**
     * Called when the options menu is first created.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem mediaRouteMenuItem = menu.findItem(R.id.media_route_menu_item);
        MediaRouteActionProvider mediaRouteActionProvider =
                (MediaRouteActionProvider) MenuItemCompat.getActionProvider(mediaRouteMenuItem);
        mediaRouteActionProvider.setRouteSelector(mMediaRouteSelector);
        return true;
    }

    /**
     * Called on application start. Using the previously selected Cast device, attempts to begin a
     * session using the application name TicTacToe.
     */
    @Override
    protected void onStart() {
        super.onStart();
        mMediaRouter.addCallback(mMediaRouteSelector, mMediaRouterCallback,
                MediaRouter.CALLBACK_FLAG_PERFORM_ACTIVE_SCAN);
    }

    /**
     * Removes the activity from memory when the activity is paused.
     */
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    /**
     * Attempts to end the current game session when the activity stops.
     */
    @Override
    protected void onStop() {
        endSession();
        mMediaRouter.removeCallback(mMediaRouterCallback);
        super.onStop();
    }

    /**
     * Ends any existing application session with a Chromecast device.
     */
    private void endSession() {
        if ((mSession != null) && (mSession.hasStarted())) {
            try {
                if (mSession.hasChannel()) {
                    mMessageStream.end();
                }
                mSession.endSession();
            } catch (IOException e) {
                Log.e(TAG, "Failed to end the session.", e);
            } catch (IllegalStateException e) {
                Log.e(TAG, "Unable to end session.", e);
            } finally {
                mSession = null;
            }
        }
    }

    /**
     * Unregisters the media route provider and disposes the CastContext.
     */
    @Override
    public void onDestroy() {
        MediaRouteHelper.unregisterMediaRouteProvider(mCastContext);
        mCastContext.dispose();
        mCastContext = null;
        super.onDestroy();
    }

    /**
     * Returns the screen configuration to portrait mode whenever changed.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

   

    
    private void setSelectedDevice(CastDevice device) {
        mSelectedDevice = device;

        if (mSelectedDevice != null) {
            mSession = new ApplicationSession(mCastContext, mSelectedDevice);
            mSession.setListener(mSessionListener);

            try {
                mSession.startSession(APP_NAME);
            } catch (IOException e) {
                Log.e(TAG, "Failed to open a session", e);
            }
        } else {
            endSession();
            mInfoView.setText("Select Device");
        }
    }

    /**
     * Called when a user selects a route.
     */
    private void onRouteSelected(RouteInfo route) {
        sLog.d("onRouteSelected: %s", route.getName());
        MediaRouteHelper.requestCastDeviceForRoute(route);
    }

    /**
     * Called when a user unselects a route.
     */
    private void onRouteUnselected(RouteInfo route) {
        sLog.d("onRouteUnselected: %s", route.getName());
        setSelectedDevice(null);
    }

   

    /**
     * A class which listens to session start events. On detection, it attaches the message
     * stream.
     */
    private class SessionListener implements ApplicationSession.Listener {
        @Override
        public void onSessionStarted(ApplicationMetadata appMetadata) {
            sLog.d("SessionListener.onStarted");

            mInfoView.setText("Waiting...");
            ApplicationChannel channel = mSession.getChannel();
            if (channel == null) {
                Log.w(TAG, "onStarted: channel is null");
                return;
            }
            channel.attachMessageStream(mMessageStream);
            //mGameMessageStream.join("MyName");
            // TODO Here maybe call the stream somehow.
        }

        @Override
        public void onSessionStartFailed(SessionError error) {
            sLog.d("SessionListener.onStartFailed: %s", error);
        }

        @Override
        public void onSessionEnded(SessionError error) {
            sLog.d("SessionListener.onEnded: %s", error);
        }
    }

    /**
     * An extension of the GameMessageStream specifically for the TicTacToe game.
     */
    private class CustomHipstaCasterStream extends HipstaCasterMessageStream {


        /**
         * Clears the game board upon a game error being detected, and displays an error dialog.
         */
        @Override
        protected void onError(String errorMessage) {
               
            mInfoView.setText(errorMessage);
           

            new AlertDialog.Builder(GameActivity.this)
                    .setTitle("Error")
                    .setMessage(errorMessage)
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    .create()
                    .show();
        }
        
        @Override
        protected void start() {
        	buildAlertDialog("Title: Start", "Starting...");
		}

		
		@Override
        protected void end() {
			buildAlertDialog("Title: End", "Ending...");
		}
    }
    
    private void buildAlertDialog(String title, String msg) {
		new AlertDialog.Builder(GameActivity.this)
        .setTitle(title)
        .setMessage(msg)
        .setCancelable(false)
        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        })
        .create()
        .show();
	}


    /**
     * An extension of the MediaRoute.Callback specifically for the TicTacToe game.
     */
    private class MediaRouterCallback extends MediaRouter.Callback {
        @Override
        public void onRouteSelected(MediaRouter router, RouteInfo route) {
            sLog.d("onRouteSelected: %s", route);
            GameActivity.this.onRouteSelected(route);
        }

        @Override
        public void onRouteUnselected(MediaRouter router, RouteInfo route) {
            sLog.d("onRouteUnselected: %s", route);
            GameActivity.this.onRouteUnselected(route);
        }
    }

    /* MediaRouteAdapter implementation */

    @Override
    public void onDeviceAvailable(CastDevice device, String routeId,
            MediaRouteStateChangeListener listener) {
        sLog.d("onDeviceAvailable: %s (route %s)", device, routeId);
        setSelectedDevice(device);
    }

    @Override
    public void onSetVolume(double volume) {
    }

    @Override
    public void onUpdateVolume(double delta) {
    }
}

