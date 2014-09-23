package com.codepath.gridimagesearch.adapters;

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

import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.models.ImageItem;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by qingdi on 9/19/14.
 */
public class ImageResultsAdapter extends ArrayAdapter<ImageItem> {

    public ImageResultsAdapter(Context context, List<ImageItem> images) {
        super(context, R.layout.item_image_result, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageItem item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result, parent, false);
        }

        ViewHolder viewHolder = (ViewHolder)convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        viewHolder.tvTitle.setText(Html.fromHtml(item.title));
        viewHolder.ivImage.setImageResource(0);
        Picasso.with(getContext()).load(item.thumbUrl).into(viewHolder.ivImage);

        return convertView;
    }

    /**
     * Cache of the children views for a list item.
     */
    public static class ViewHolder {
        public final ImageView ivImage;
        public final TextView tvTitle;

        public ViewHolder(View view) {
            ivImage = (ImageView)view.findViewById(R.id.ivImage);
            tvTitle = (TextView)view.findViewById(R.id.tvTitle);
        }
    }
}
