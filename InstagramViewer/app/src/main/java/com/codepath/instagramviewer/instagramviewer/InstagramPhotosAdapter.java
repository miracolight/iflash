package com.codepath.instagramviewer.instagramviewer;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.instagramviewer.instagramviewer.object.InstagramPhoto;
import com.pkmmte.view.CircularImageView;
//import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by qingdi on 9/13/14.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
   public static final String BLUE_HTML_TXT = "<font color=\"#1c6094\">TEXT</font> ";

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

        String formattedUserName = BLUE_HTML_TXT.replace("TEXT", photo.user.userName);
        viewHolder.tvUsername.setText(Html.fromHtml(formattedUserName));
        viewHolder.imgAvatar.setImageResource(0);
        Picasso.with(getContext()).load(photo.user.profileImgUrl).into(viewHolder.imgAvatar);

        viewHolder.tvCaption.setText(photo.caption);

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);

        int imgY = (int)(metrics.widthPixels*((double)photo.imageHeight/photo.imageWidth));
        viewHolder.imgPhoto.getLayoutParams().height = imgY;
        viewHolder.imgPhoto.getLayoutParams().width = metrics.widthPixels;
        viewHolder.imgPhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.imageUrl).into(viewHolder.imgPhoto);


        viewHolder.tvPhotoDate.setText(DateUtils.getRelativeTimeSpanString(photo.creationDate * 1000));

        DecimalFormat formatter = new DecimalFormat("#,###");
        viewHolder.tvLikes.setText(formatter.format(photo.likesCount));

        if (photo.lastComment != null) {
            viewHolder.tvLastComment.setText(Html.fromHtml(BLUE_HTML_TXT.replace("TEXT", photo.lastComment.userName)+"  "+photo.lastComment.text));
        }

        return convertView;
    }

    /**
     * Cache of the children views for a list item.
     */
    public static class ViewHolder {
        public final ImageView imgPhoto;
        public final TextView tvPhotoDate;
        public final TextView tvCaption;
        public final TextView tvLikes;
        public final TextView tvUsername;
        public final TextView tvLastComment;
        public final ImageView imgAvatar;


        public ViewHolder(View view) {
            tvUsername = (TextView)view.findViewById(R.id.tvUserName);
            imgAvatar = (ImageView)view.findViewById(R.id.imgAvatar);
            imgPhoto = (ImageView)view.findViewById(R.id.imgPhoto);
            tvCaption = (TextView)view.findViewById(R.id.tvCaption);
            tvPhotoDate = (TextView)view.findViewById(R.id.tvPhotoDate);
            tvLikes = (TextView)view.findViewById(R.id.tvLikes);
            tvLastComment = (TextView)view.findViewById(R.id.tvLastComment);
        }
    }
}
