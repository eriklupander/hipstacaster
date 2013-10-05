package com.squeed.chromecast.hipstacaster;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.MediaRouteActionProvider;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import com.google.cast.*;
import com.squeed.chromecast.hipstacaster.dto.Photo;
import com.squeed.chromecast.hipstacaster.grid.GridViewAdapter;
import com.squeed.chromecast.hipstacaster.grid.ImageItem;
import com.squeed.chromecast.hipstacaster.img.DrawableManager;
import com.squeed.chromecast.hipstacaster.rest.RestClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HipstaActivity extends Activity implements MediaRouteAdapter {

    private static final String TAG = HipstaActivity.class.getSimpleName();
    private static final com.google.cast.Logger sLog = new com.google.cast.Logger(TAG, true);
    private static final String APP_NAME = "HipstaCaster";

    private ApplicationSession mSession;
    private SessionListener mSessionListener;
    private CustomHipstaCasterStream mMessageStream;


    private GridView gridView;
    private GridViewAdapter customGridAdapter;



    private CastContext mCastContext;
    private CastDevice mSelectedDevice;
    private MediaRouter mMediaRouter;
    private MediaRouteSelector mMediaRouteSelector;
    private MediaRouter.Callback mMediaRouterCallback;

    private RestClient restClient;
    private DrawableManager drawableManager;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.main);

        restClient = new RestClient();
        drawableManager = new DrawableManager();

        gridView = (GridView) findViewById(R.id.gridView);
        customGridAdapter = new GridViewAdapter(this, R.layout.row_grid, getData());
        gridView.setAdapter(customGridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(HipstaActivity.this, position + "#Selected",
                        Toast.LENGTH_SHORT).show();
            }

        });



        mSessionListener = new SessionListener();
        mMessageStream = new CustomHipstaCasterStream();

        mCastContext = new CastContext(getApplicationContext());
        MediaRouteHelper.registerMinimalMediaRouteProvider(mCastContext, this);
        mMediaRouter = MediaRouter.getInstance(getApplicationContext());
        mMediaRouteSelector = MediaRouteHelper.buildMediaRouteSelector(
                MediaRouteHelper.CATEGORY_CAST, APP_NAME, null);
        mMediaRouterCallback = new MediaRouterCallback();
    }

    private ArrayList getData() {
        final ArrayList imageItems = new ArrayList();

        List<Photo> searchResult = restClient.search("sarek,narvik");
        for(Photo p : searchResult) {
            Drawable drawable = drawableManager.fetchDrawable(p.getSquareUrl());
            imageItems.add(new ImageItem(drawableToBitmap(drawable), p.getOwnerName()));
        }

        return imageItems;
    }

    private Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    /**
     * Called when the options menu is first created.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
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
        }
    }

    /**
     * Called when a user selects a route.
     */
    private void onRouteSelected(android.support.v7.media.MediaRouter.RouteInfo route) {
        sLog.d("onRouteSelected: %s", route.getName());
        MediaRouteHelper.requestCastDeviceForRoute(route);
    }

    /**
     * Called when a user unselects a route.
     */
    private void onRouteUnselected(android.support.v7.media.MediaRouter.RouteInfo route) {
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

            new AlertDialog.Builder(HipstaActivity.this)
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
        new AlertDialog.Builder(HipstaActivity.this)
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
        public void onRouteSelected(MediaRouter router, android.support.v7.media.MediaRouter.RouteInfo route) {
            sLog.d("onRouteSelected: %s", route);
            HipstaActivity.this.onRouteSelected(route);
        }

        @Override
        public void onRouteUnselected(MediaRouter router, android.support.v7.media.MediaRouter.RouteInfo route) {
            sLog.d("onRouteUnselected: %s", route);
            HipstaActivity.this.onRouteUnselected(route);
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
