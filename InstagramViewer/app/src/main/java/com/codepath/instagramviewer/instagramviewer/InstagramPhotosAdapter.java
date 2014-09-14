package com.codepath.instagramviewer.instagramviewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by qingdi on 9/13/14.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {

    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> photos) {
        super(context, R.layout.item_photo, photos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InstagramPhoto photo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }

        ViewHolder viewHolder = (ViewHolder)convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        viewHolder.tvUsername.setText(photo.username);
        viewHolder.tvCaption.setText(photo.caption);
        viewHolder.imgPhoto.getLayoutParams().height = photo.imageHeight;
        viewHolder.imgPhoto.setImageResource(0);

        Picasso.with(getContext()).load(photo.imageUrl).into(viewHolder.imgPhoto);

        return convertView;
    }

    /**
     * Cache of the children views for a list item.
     */
    public static class ViewHolder {
        public final ImageView imgPhoto;
        public final TextView tvUsername;
        public final TextView tvCaption;

        public ViewHolder(View view) {
            imgPhoto = (ImageView)view.findViewById(R.id.imgPhoto);
            tvCaption = (TextView)view.findViewById(R.id.tvCaption);
            tvUsername = (TextView)view.findViewById(R.id.tvUserName);
        }
    }
}
