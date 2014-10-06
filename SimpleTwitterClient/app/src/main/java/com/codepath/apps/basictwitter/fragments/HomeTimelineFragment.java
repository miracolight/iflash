package com.codepath.apps.basictwitter.fragments;

import android.util.Log;
import android.widget.ProgressBar;

import com.codepath.apps.basictwitter.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by qingdi on 10/4/14.
 */
public class HomeTimelineFragment extends TweetsListFragment{

    public void populateTimeline() {
        RequestParams params = null;
        if (maxId != null) {
            params = new RequestParams();
            params.put("max_id", maxId.toString());
        }
        populateTimeline("getHomeTimeline", params);
    }
}
