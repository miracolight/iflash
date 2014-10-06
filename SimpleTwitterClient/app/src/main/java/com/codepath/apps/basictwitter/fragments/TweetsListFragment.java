package com.codepath.apps.basictwitter.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.TwitterClient;
import com.codepath.apps.basictwitter.adapters.TweetArrayAdapter;
import com.codepath.apps.basictwitter.listeners.EndlessScrollListener;
import com.codepath.apps.basictwitter.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingdi on 10/3/14.
 */
public abstract class TweetsListFragment extends Fragment{

    private     List<Tweet>     tweets;
    private     ListView        lvTweets;
    protected   ProgressBar     pb;

    protected   ArrayAdapter<Tweet> aTweets;
    protected   TwitterClient   client;
    protected   Long            maxId = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<Tweet>();
        aTweets = new TweetArrayAdapter(getActivity(), tweets);

        client = TwitterApplication.getRestClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        lvTweets = (ListView)v.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(aTweets);
        pb = (ProgressBar)v.findViewById(R.id.progressBar);

        populateTimeline();
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                populateTimeline();
            }
        });
        return v;
    }


    public void addNewTweet(Tweet tweet) {
        aTweets.insert(tweet, 0);
    }

    public abstract void populateTimeline();

    protected void populateTimeline(String method, RequestParams params) {
        pb.setVisibility(ProgressBar.VISIBLE);
        try {
            Method m = client.getClass().getMethod(method, AsyncHttpResponseHandler.class, RequestParams.class);
            m.invoke(client, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONArray jsonArray) {
                    ArrayList<Tweet> tweets = Tweet.fromJSONArray(jsonArray);
                    if (tweets.size()>0) {
                        aTweets.addAll(tweets);
                        maxId = tweets.get(tweets.size() - 1).getId()-1;
                    }
                }

                @Override
                public void onFailure(Throwable e, String s) {
                    Log.d("debug", e.toString());
                    Log.d("debug", s.toString());
                }
            }, params);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        pb.setVisibility(ProgressBar.INVISIBLE);
    }

    /*
    // Store the listener (activity) that will have events fired once the fragment is attached
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ProgressBarListener) {
            progressBarListener = (ProgressBarListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement ProgressBarListener");
        }
    }

    public interface ProgressBarListener {
        public void showProgressBar();
        public void hideProgressBar();
    }
    */
}
