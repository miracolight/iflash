package com.codepath.apps.basictwitter.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.adapters.TweetArrayAdapter;
import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.TwitterClient;
import com.codepath.apps.basictwitter.fragments.ComposeDialogFragment;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends FragmentActivity implements ComposeDialogFragment.ComposeDialogListener{


    private FragmentManager fm = getSupportFragmentManager();

    private TwitterClient client;
    private List<Tweet>         tweets;
    private ArrayAdapter<Tweet> aTweets;
    private ListView            lvTweets;
    private Long                maxId = null;
    private User                loginUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        client = TwitterApplication.getRestClient();
        populateTimeline();
        getLoginUser();
        lvTweets = (ListView)findViewById(R.id.lvTweets);
        tweets = new ArrayList<Tweet>();
        aTweets = new TweetArrayAdapter(this, tweets);
        lvTweets.setAdapter(aTweets);

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                populateTimeline();
            }
        });

    }


    public void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                ArrayList<Tweet> tweets = Tweet.fromJSONArray(jsonArray);
                aTweets.addAll(tweets);
                maxId = tweets.get(tweets.size()-1).getId();
            }

            @Override
            public void onFailure(Throwable e, String s) {
                Log.d("debug", e.toString());
                Log.d("debug", s.toString());
            }
        }, maxId);
    }

    public void getLoginUser() {
        client.getLoginUser(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject jsonObj) {
                loginUser = User.fromJSON(jsonObj);
            }

            @Override
            public void onFailure(Throwable e, String s) {
                Log.d("debug", e.toString());
                Log.d("debug", s.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_compose) {
            ComposeDialogFragment composeFragment = new ComposeDialogFragment();
            // Show DialogFragment
            Bundle args = new Bundle();
            args.putParcelable("loginUser", loginUser);
            composeFragment.setArguments(args);
            composeFragment.show(fm, "Compose new Tweet Fragment");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onFinishComposeDialog(String t) {
        client.postTweet(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject jsonObj) {
                Tweet tweet = Tweet.fromJSON(jsonObj);
                aTweets.insert(tweet, 0);
            }

            @Override
            public void onFailure(Throwable e, String s) {
                Log.d("debug", e.toString());
                Log.d("debug", s.toString());
            }
        }, t);
    }
}
