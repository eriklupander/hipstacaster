package com.squeed.chromecast.hipstacaster;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Preferences extends PreferenceActivity {
	
	String tagsPreference;
	 
	private void getPrefs() {
	                // Get the xml/preferences.xml preferences
	                SharedPreferences prefs = PreferenceManager
	                                .getDefaultSharedPreferences(getBaseContext());
	               
	                tagsPreference = prefs.getString("tagsPref", "");
	        }
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            getListView().setBackgroundResource(R.drawable.background_01p);       
            
            getPrefs();
    }
}
