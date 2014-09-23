package com.codepath.gridimagesearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingdi on 9/19/14.
 */
public class ImageItem implements Serializable{

    private static final long serialVersionUID = 4448235746079175763L;

    public String   fullUrl;
    public String   thumbUrl;
    public String   title;
    public int      width;
    public int      height;

    public ImageItem(JSONObject imageJSON) {
        // results=>[x]=>tbUrl
        // results=>[x]=>title
        // results=>[x]=>url
        // results=>[x]=>width
        try {
            this.fullUrl = imageJSON.getString("url");
            this.thumbUrl = imageJSON.getString("tbUrl");
            this.title = imageJSON.getString("title");
            this.width = imageJSON.getInt("width");
            this.height = imageJSON.getInt("height");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static List<ImageItem> fromJSONArray(JSONArray array) {
        List<ImageItem> results = new ArrayList<ImageItem>();
        for (int i = 0; i < array.length(); i++) {
            try {
                results.add(new ImageItem(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return results;
    }
}
