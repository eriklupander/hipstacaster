package com.squeed.chromecast.hipstacaster.rest;

import java.util.List;

import android.os.AsyncTask;

import com.squeed.chromecast.hipstacaster.HipstaActivity;
import com.squeed.chromecast.hipstacaster.dto.Photo;

/**
 * Created with IntelliJ IDEA.
 * User: Erik
 * Date: 2013-10-06
 * Time: 22:04
 * To change this template use File | Settings | File Templates.
 */
public class LoadImageListTask extends AsyncTask<String, Void, List<Photo>> {

    private final HipstaActivity activity;
    private Exception exception;
    private RestClient restClient = new RestClient();

    public LoadImageListTask(HipstaActivity activity) {
        this.activity = activity;
    }

    protected List<Photo> doInBackground(String... tags) {
        try {
            return restClient.search(tags[0]);
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    protected void onPostExecute(List<Photo> list) {
        activity.onPhotoListLoaded(list);
    }
}