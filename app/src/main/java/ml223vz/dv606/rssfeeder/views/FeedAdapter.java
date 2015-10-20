package ml223vz.dv606.rssfeeder.views;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import ml223vz.dv606.rssfeeder.PostData;
import ml223vz.dv606.rssfeeder.R;

/**
 * TODO: When you change the way you handle the feed, make sure you do it in here too
 */
public class FeedAdapter extends ArrayAdapter<PostData> {
    private static Context mContext;
    private static ArrayList<PostData> mFeed;

    public FeedAdapter(Context context, ArrayList<PostData> feed){
        super(context, R.layout.row_feed, feed);
        mContext = context;
        mFeed = feed;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        FeedWrapper wrapper = null;
        if(row == null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(R.layout.row_feed, parent, false);
            wrapper = new FeedWrapper(row);
            row.setTag(wrapper);
        } else {
            wrapper = (FeedWrapper) row.getTag();
        }

        PostData postData = mFeed.get(position);

        wrapper.getTitle().setText(postData.getTitle());
        wrapper.getSubTitle().setText(postData.getLink().toString());
        wrapper.getTextBody().setText(postData.getDescription());

        return row;
    }
}
