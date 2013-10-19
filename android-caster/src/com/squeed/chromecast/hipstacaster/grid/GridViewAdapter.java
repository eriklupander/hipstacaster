package com.squeed.chromecast.hipstacaster.grid;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squeed.chromecast.hipstacaster.HipstaActivity;
import com.squeed.chromecast.hipstacaster.R;

/**
 * Custom GridViewAdapter, derived from something found on StackOverflow.
 * 
 * Currently uses a fugly workaround for storing the relevant data directly on the ViewHolder. Should be replaced.
 * 
 * @author Erik
 */
public class GridViewAdapter extends ArrayAdapter<ImageItem> {
    private Context context;
    private int layoutResourceId;
   
    public GridViewAdapter(Context context, int layoutResourceId,
                           ArrayList<ImageItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
    }    
  
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            row.setOnClickListener(new GridViewOnClickListener());
           
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.text);
            holder.image = (ImageView) row.findViewById(R.id.image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        ImageItem item = (ImageItem) getItem(position);
        holder.imageTitle.setText(item.getTitle());
        holder.image.setImageBitmap(item.getImage());
        holder.title = item.getTitle();
        holder.ownerName = item.getOwnerName();
        holder.fullsizeUrl = item.getUrl();
        holder.description = item.getDescription();
        holder.position = position;
        return row;
    }

    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
        String title;
        String ownerName;
        String fullsizeUrl;
        String description;
        int position;
    }
    
    private class GridViewOnClickListener implements OnClickListener {
    	
		@Override
		public void onClick(View v) {
			
			ViewHolder tag = (ViewHolder) v.getTag();
			((HipstaActivity) context).setOffset(tag.position);
			((HipstaActivity) context).openPhoto(tag.title, tag.fullsizeUrl, tag.ownerName, tag.description);					
		}
    }    
}
