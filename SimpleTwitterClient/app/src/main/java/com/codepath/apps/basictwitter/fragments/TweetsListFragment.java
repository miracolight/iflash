package com.codepath.apps.basictwitter.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.TwitterClient;
import com.codepath.apps.basictwitter.adapters.TweetArrayAdapter;
import com.codepath.apps.basictwitter.helpers.InternetHelper;
import com.codepath.apps.basictwitter.listeners.EndlessScrollListener;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by qingdi on 10/3/14.
 */
public abstract class TweetsListFragment extends Fragment{

    private     List<Tweet>     tweets;
    private     ListView        lvTweets;
    protected   ProgressBar     pb;
    private     SwipeRefreshLayout swipeContainer;

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

        swipeContainer = (SwipeRefreshLayout)v.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                aTweets.clear();
                maxId = null;
                populateTimeline();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

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

    protected void populateTimeline(String method, Map<String, String> params) {
        pb.setVisibility(ProgressBar.VISIBLE);
        if (InternetHelper.isNetworkAvailable(getActivity())) {
            try {
                RequestParams rp = null;
                if (params != null) {
                    rp = new RequestParams();
                    for (Map.Entry<String, String> me : params.entrySet()) {
                        rp.put(me.getKey(), me.getValue());
                    }
                }

                Method m = client.getClass().getMethod(method, AsyncHttpResponseHandler.class, RequestParams.class);
                final int tweetType = Tweet.getTweetType(method);
                m.invoke(client, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        ArrayList<Tweet> tweets = Tweet.fromJSONArray(jsonArray);
                        if (tweets.size()>0) {
                            aTweets.addAll(tweets);
                            for (Tweet t : tweets) {
                                t.setType(tweetType);
                                saveToDb(t);
                            }
                            maxId = tweets.get(tweets.size() - 1).getTid()-1;
                        }

                        pb.setVisibility(ProgressBar.INVISIBLE);
                        swipeContainer.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Throwable e, String s) {
                        Log.d("debug", e.toString());
                        Log.d("debug", s.toString());
                        pb.setVisibility(ProgressBar.INVISIBLE);
                    }
                }, rp);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            aTweets.clear();
            aTweets.addAll(Tweet.getAll(Tweet.getTweetType(method), params == null ? null : params.get("user_id")));
            pb.setVisibility(ProgressBar.INVISIBLE);
            swipeContainer.setRefreshing(false);
        }

    }

    private void saveToDb(Tweet t) {
        List<User> u = User.getByUid(t.getUser().getUid());
        if (u != null && u.size() >0) {
            t.setUser(u.get(0));
        } else {
            t.getUser().save();
        }
        t.save();
    }

}
