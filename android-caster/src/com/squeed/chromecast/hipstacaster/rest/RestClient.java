package com.squeed.chromecast.hipstacaster.rest;

import android.util.Log;
import com.squeed.chromecast.hipstacaster.dto.Photo;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Erik
 * Date: 2013-10-05
 * Time: 22:34
 * To change this template use File | Settings | File Templates.
 */
public class RestClient {

    private String baseUrl = "http://192.168.1.128:8080/hipstacaster/rest";

    public List<Photo> search(String tags) {
    	ArrayList<Photo> l = new ArrayList<Photo>();
        HttpContext localContext = new BasicHttpContext();
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(baseUrl + "/photos/search/" + tags);
        //get.setHeader("Content-type", "application/json");
        //get.setHeader("Accepts", "application/json");
        try {
            HttpResponse response = client.execute(get,localContext);

            String responseText = IOUtils.toString(response.getEntity().getContent());

            JSONArray jsonArray = new JSONArray(responseText);

            for(int a = 0; a < jsonArray.length(); a++) {
                JSONObject o = (JSONObject) jsonArray.get(a);
                Photo p = new Photo(o.getString("ownerName"), o.getString("fullsizeUrl"), o.getString("squareUrl"));
                l.add(p);
            }

            return l;
        } catch (JSONException e) {
              Log.e("REST", e.getMessage());
        } catch (IOException e) {
            Log.e("REST", e.getMessage());
        }
        return l;
    }
}
