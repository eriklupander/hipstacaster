package com.squeed.chromecast.hipstacaster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.MediaRouteActionProvider;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.cast.ApplicationChannel;
import com.google.cast.ApplicationMetadata;
import com.google.cast.ApplicationSession;
import com.google.cast.CastContext;
import com.google.cast.CastDevice;
import com.google.cast.MediaRouteAdapter;
import com.google.cast.MediaRouteHelper;
import com.google.cast.MediaRouteStateChangeListener;
import com.google.cast.SessionError;
import com.squeed.chromecast.hipstacaster.dto.Photo;
import com.squeed.chromecast.hipstacaster.grid.GridViewAdapter;
import com.squeed.chromecast.hipstacaster.grid.ImageItem;
import com.squeed.chromecast.hipstacaster.img.Callback;
import com.squeed.chromecast.hipstacaster.img.DrawableManager;
import com.squeed.chromecast.hipstacaster.rest.LoadImageListTask;

/**
 * Demonstrative Activity for the HipstaCaster Android client. Somewhat based on the Android demo tic-tac-toe game from 
 * https://github.com/googlecast/cast-android-tictactoe
 * 
 * @author Erik
 *
 */
public class HipstaActivity extends ActionBarActivity implements MediaRouteAdapter {

    private static final String DEFAULT_SEARCH_TAG = "sarek";
	private static final String TAG = HipstaActivity.class.getSimpleName();
    private static final com.google.cast.Logger sLog = new com.google.cast.Logger(TAG, true);
    private static final String APP_NAME = "fc91668a-cf4b-4a18-9611-f2c120d0bf07_1";
    private static final String PROTOCOL = "com.squeed.chromecast.hipstacaster";
    
    static final int IMAGES_PER_FETCH = 20;

    private ApplicationSession mSession;
    private SessionListener mSessionListener;
    private CustomHipstaCasterStream mMessageStream;

    private GridView gridView;
    private GridViewAdapter customGridAdapter;
    
    private TextView mInfoView;
    private ProgressBar spinner;
    
    private CastContext mCastContext;
    private CastDevice mSelectedDevice;
    private MediaRouter mMediaRouter;
    private MediaRouteSelector mMediaRouteSelector;
    private MediaRouter.Callback mMediaRouterCallback;
    
    
    private ArrayList<ImageItem> photoSet;
    private DrawableManager drawableManager;
    
    private boolean allowLoading = false;
    private boolean isLoadingMore = false;
    private int index = 0;
    /** Set when an image is clicked, can be use to supply start index of slideshow */
    private int offset = 0;
    private int pageOffset = 0;
    
    private int loadedInCurrentBatch = 0;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.main);
        
        ArrayList<String> protocols = new ArrayList<String>();
        protocols.add(PROTOCOL);
        
        mInfoView = (TextView) findViewById(R.id.status);
        mInfoView.setText("Loading...");
        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
              int lastInScreen = firstVisibleItem + visibleItemCount;
              if (totalItemCount > 0 && (lastInScreen == totalItemCount) && !isLoadingMore && !(pageOffset*IMAGES_PER_FETCH > gridView.getCount()) && allowLoading)  {
            	  isLoadingMore = true;
                  // FETCH THE NEXT BATCH OF FEEDS
            	  SharedPreferences preferences = 
          	        	PreferenceManager.getDefaultSharedPreferences(HipstaActivity.this);
                  new LoadImageListTask(HipstaActivity.this, ++pageOffset, IMAGES_PER_FETCH).execute(preferences.getString("tagsPref", DEFAULT_SEARCH_TAG));            	  
              }
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}        	
        });
        spinner = (ProgressBar) findViewById(R.id.myspinner);
        spinner.setVisibility(View.GONE);
        photoSet = new ArrayList<ImageItem>();
        customGridAdapter = new GridViewAdapter(this, R.layout.row_grid, photoSet);
        gridView.setAdapter(customGridAdapter);
        
        drawableManager = new DrawableManager();
        
        SharedPreferences preferences = 
	        	PreferenceManager.getDefaultSharedPreferences(HipstaActivity.this);
        
        // This flipped check is just for the first load, to make sure the 
        if(!allowLoading) {
        	allowLoading = false;
        	isLoadingMore = true;
        	new LoadImageListTask(this, pageOffset, IMAGES_PER_FETCH).execute(preferences.getString("tagsPref", DEFAULT_SEARCH_TAG));
        }

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
     * Typically invoked 
     * @param list
     */
    public void onPhotoListLoaded(List<Photo> list) {
    	
    	loadedInCurrentBatch = 0;
    	
    	mInfoView.setText("Loaded " + list.size() + " images definitions from Flickr");
        for(Photo p : list) {
        	photoSet.add(new ImageItem(drawableManager.drawableToBitmap(getResources().getDrawable(R.drawable.user_placeholder)), p.getOwnerName(), p.getFullsizeUrl(), p.getTitle(), p.getDescription()));
        }
	
    
        index = pageOffset * IMAGES_PER_FETCH;
        for(Photo p : list) {
    		drawableManager.fetchDrawableOnThread(p.getSquareUrl(), index, new Callback() {

    			/**
    			 * 
    			 */
				@Override
				public void updateGridView(final Bitmap bitmap, final int position) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							ImageItem itemAtPosition = (ImageItem) gridView.getItemAtPosition(position);
			                itemAtPosition.setImage(bitmap);	                            
			                gridView.invalidateViews();
			                loadedInCurrentBatch++;
			                if(loadedInCurrentBatch >= IMAGES_PER_FETCH) {
			                	allowLoading = true;
			                	loadedInCurrentBatch = 0;
			                }
						}
						
					});					
				}        
    		});
            index++;
        }
        gridView.setAlpha(1.0f);
    	
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
        
        MenuItem settingsMenuItem = menu.findItem(R.id.action_settings);
        settingsMenuItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent settingsActivity = new Intent(getBaseContext(),
                        Preferences.class);
				startActivity(settingsActivity);
				return false;
			}
		});

        MenuItem refreshMenuItem = menu.findItem(R.id.action_refresh);
        refreshMenuItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				if(gridView != null) {
					((GridViewAdapter) gridView.getAdapter()).clear();
					gridView.setAlpha(0.2f);
					SharedPreferences preferences = 
				        	PreferenceManager.getDefaultSharedPreferences(HipstaActivity.this);
					pageOffset = 0;
					
					new LoadImageListTask(HipstaActivity.this, pageOffset, IMAGES_PER_FETCH).execute(preferences.getString("tagsPref", DEFAULT_SEARCH_TAG));
				}
				return false;
			}
		});    
        
        MenuItem slideShowMenuItem = menu.findItem(R.id.action_slideshow);
        slideShowMenuItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				if(mSession != null && mSession.hasStarted() && photoSet != null && photoSet.size() > 0) {
					sendPhotoSet();
					Toast.makeText(HipstaActivity.this, "Starting slideshow on ChromeCast!", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(HipstaActivity.this, "Please connect to cast device first.", Toast.LENGTH_SHORT).show();
				}
				return false;
			}
        });
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
        Log.i(TAG, "ENTER - onPause");
       // finish();
    }

    /**
     * 
     */
    @Override
    protected void onStop() {
    	Log.i(TAG, "ENTER - onStop");
        //endSession();
        //mMediaRouter.removeCallback(mMediaRouterCallback);
        super.onStop();
    }

    /**
     * Ends any existing application session with a Chromecast device.
     */
    private void endSession() {
        if ((mSession != null) && (mSession.hasStarted())) {
            try {
                if (mSession.hasChannel()) {
                	// TODO perhaps notify receiever app?
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
    	Log.i(TAG, "ENTER - onDestroy");
    	
    	// These two has been moved from onStop. We don't want slideshow or view photo on Chromecast to stop unless Hipstacaster app closes for real.
    	endSession();
    	mMediaRouter.removeCallback(mMediaRouterCallback);
    	
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
    	Log.i(TAG, "ENTER - onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }




    private void setSelectedDevice(CastDevice device) {
    	
        mSelectedDevice = device;

        if (mSelectedDevice != null) {
        	Log.i(TAG, "ENTER -  setSelectedDevice: " + device.toString());
        	mInfoView.setText("Starting session");
            mSession = new ApplicationSession(mCastContext, mSelectedDevice);
            mSession.setListener(mSessionListener);

            try {
                mSession.startSession(APP_NAME);
                mInfoView.setText("Session started!");
            } catch (IOException e) {
                Log.e(TAG, "Failed to open a session", e);
                mInfoView.setText("Failed to open a session");
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
        	Log.i(TAG, "SessionListener.onStarted");

            ApplicationChannel channel = mSession.getChannel();
            if (channel == null) {
                Log.w(TAG, "onStarted: channel is null");
                return;
            }
            channel.attachMessageStream(mMessageStream);
            mInfoView.setText("Session started");
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
     * An extension of the MessageStream with some local details.
     */
    private class CustomHipstaCasterStream extends HipstaCasterMessageStream {

        /**
         * Displays an error dialog.
         */
        @Override
        protected void onError(String errorMessage) {
        	buildAlertDialog("Error", errorMessage);
        }

        /**
         * Displays a message that the slideshow has ended.
         */
		@Override
		protected void onSlideShowEnded() {
			buildAlertDialog("Message from ChromeCast", "Slideshow has ended");
		}

		@Override
		protected void onCurrentSlideShowImageMessage(String message) {
			mInfoView.setText(message);
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
            Log.i(TAG, "onRouteSelected: " + route);
            HipstaActivity.this.onRouteSelected(route);
        }

        @Override
        public void onRouteUnselected(MediaRouter router, android.support.v7.media.MediaRouter.RouteInfo route) {
        	Log.i(TAG, "onRouteUnselected: " + route);
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

	public void openPhoto(String title, String url, String ownerName, String description) {
		if(isCastSessionActive()) {
			mMessageStream.openPhotoOnChromecast(title, url, ownerName, description);
		}		
	}
	
	public void sendPhotoSet() {
		if(isCastSessionActive()) {
			mMessageStream.sendPhotoSetToChromecast(photoSet, offset);
		}
	}
	
	private boolean isCastSessionActive() {
		return mSession != null && mSession.hasChannel() && mSession.hasStarted();
	}
	

	public void showSpinner() {
		isLoadingMore = true;
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				spinner.setVisibility(View.VISIBLE);
			}			
		});
	}	

	public void hideSpinner() {
		isLoadingMore = false;
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				spinner.setVisibility(View.GONE);
			}			
		});
		
	}
	
	public void setOffset(int offset) {
		this.offset = offset;
	}
}
