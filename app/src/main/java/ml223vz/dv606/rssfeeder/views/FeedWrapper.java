package ml223vz.dv606.rssfeeder.views;

import android.view.View;
import android.widget.TextView;

import ml223vz.dv606.rssfeeder.R;

public class FeedWrapper {
    private View mBaseView;
    private TextView mTitle = null;
    private TextView mSubTitle = null;
    private TextView mTextBody = null;

    FeedWrapper(View base){
        mBaseView = base;
    }

    public TextView getTitle() {
        if(mTitle == null){
            mTitle = (TextView) mBaseView.findViewById(R.id.view_post_title);
        }
        return mTitle;
    }

    public TextView getSubTitle() {
        if(mSubTitle == null){
            mSubTitle = (TextView) mBaseView.findViewById(R.id.view_post_subtitle);
        }
        return mSubTitle;
    }

    public TextView getTextBody() {
        if(mTextBody == null){
            mTextBody = (TextView) mBaseView.findViewById(R.id.view_post_body);
        }
        return mTextBody;
    }


}