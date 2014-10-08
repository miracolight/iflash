package com.codepath.apps.basictwitter.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.activities.ProfileActivity;
import com.codepath.apps.basictwitter.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import com.codepath.apps.basictwitter.models.Tweet;

/**
 * Created by qingdi on 9/23/14.
 */
public class TweetArrayAdapter extends ArrayAdapter<Tweet> {
    public TweetArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, R.layout.tweet_item, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Tweet item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tweet_item, parent, false);
        }

        ViewHolder viewHolder = (ViewHolder)convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }


        viewHolder.ivProfileImage.setImageResource(0);
        viewHolder.ivProfileImage.setImageBitmap(null);
        viewHolder.ivMedia.setImageResource(0);
        viewHolder.ivMedia.setImageBitmap(null);
        try {
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(item.getUser().getProfileImageUrl(), viewHolder.ivProfileImage);
            if (item.getMediaUrl() != null) {
                imageLoader.displayImage(item.getMediaUrl(), viewHolder.ivMedia);
            }
        } catch (Exception e) {

        }


        viewHolder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), ProfileActivity.class);
                i.putExtra(User.ARG_PARAM, item.getUser());
                getContext().startActivity(i);
            }
        });
        viewHolder.tvUserScreenName.setText(item.getUser().getScreenName());
        viewHolder.tvUserName.setText(item.getUser().getName());
        viewHolder.tvBody.setText(item.getBody());
        viewHolder.tvRelCreatedAt.setText(item.getRelativeCreatedAt());
        return convertView;
    }

    /**
     * Cache of the children views for a list item.
     */
    public static class ViewHolder {
        public final ImageView  ivProfileImage;
        public final TextView   tvUserScreenName;
        public final TextView   tvUserName;
        public final TextView   tvBody;
        public final TextView   tvRelCreatedAt;
        public final ImageView  ivMedia;

        public ViewHolder(View view) {
            ivProfileImage = (ImageView)view.findViewById(R.id.ivProfileImage);
            tvUserScreenName = (TextView)view.findViewById(R.id.tvUserScreenName);
            tvUserName = (TextView)view.findViewById(R.id.tvUserName);
            tvBody = (TextView)view.findViewById(R.id.tvBody);
            tvRelCreatedAt = (TextView)view.findViewById(R.id.tvRelCreatedAt);
            ivMedia = (ImageView)view.findViewById(R.id.ivMedia);
        }
    }
}
