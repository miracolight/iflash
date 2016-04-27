package com.vvavy.visiondemo.adapter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.vvavy.visiondemo.R;
import com.vvavy.visiondemo.database.VisionDBSQLiteHelper;
import com.vvavy.visiondemo.database.entity.PerimetryTest;


/**
 * Created by qingdi on 4/15/16.
 */

public class ResultListAdapter extends ArrayAdapter<HashMap<String,Object>> {

    public ResultListAdapter(Context context, List<HashMap<String,Object>> objects) {
        super(context, -1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        View v;
        if(convertView == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.result_list_item, null);
            vh = new ViewHolder();
            vh.mTime = (TextView) v.findViewById(R.id.exam_time);
            vh.mUploaded = (ImageView)v.findViewById(R.id.imgUpload);
            vh.mDeviceId = (TextView)v.findViewById(R.id.device_id);
            v.setTag(vh);
        } else {
            v = convertView;
            vh = (ViewHolder) convertView.getTag();
        }

        HashMap<String, Object> map = getItem(position);
        vh.mTime.setText((String)map.get(PerimetryTest.KEY_TEST_DATE));
        vh.mDeviceId.setText((String)map.get(PerimetryTest.KEY_TEST_DEVICE_ID));
        if ("Y".equals((String)map.get(PerimetryTest.KEY_UPLOADED))) {
            vh.mUploaded.setImageResource(R.drawable.checked);
        } else {
            vh.mUploaded.setImageResource(R.drawable.unchecked);
        }

        return v;
    }

    private static class ViewHolder {
        public TextView mTime;
        public TextView mDeviceId;
        public ImageView mUploaded;
    }

}
