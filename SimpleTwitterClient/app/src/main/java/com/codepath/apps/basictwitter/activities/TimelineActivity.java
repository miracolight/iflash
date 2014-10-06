package com.codepath.apps.basictwitter.activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.adapters.TweetArrayAdapter;
import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.TwitterClient;
import com.codepath.apps.basictwitter.fragments.ComposeDialogFragment;
import com.codepath.apps.basictwitter.fragments.HomeTimelineFragment;
import com.codepath.apps.basictwitter.fragments.MentionsTimelineFragment;
import com.codepath.apps.basictwitter.fragments.TweetsListFragment;
import com.codepath.apps.basictwitter.listeners.EndlessScrollListener;
import com.codepath.apps.basictwitter.listeners.FragmentTabListener;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends FragmentActivity
        implements ComposeDialogFragment.ComposeDialogListener{

    private     TwitterClient   client;
    private     FragmentManager     fm = getSupportFragmentManager();
    private     User                loginUser = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApplication.getRestClient();
        getLoginUser();
        setupTabs();

    }

    private void setupTabs() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);

        ActionBar.Tab tab1 = actionBar
                .newTab()
                .setText("Home")
                .setIcon(R.drawable.ic_action_home)
                .setTag("HomeTimelineFragment")
                .setTabListener(
                        new FragmentTabListener<HomeTimelineFragment>(R.id.flContainer, this, "HomeTimelineFragment",
                                HomeTimelineFragment.class));

        actionBar.addTab(tab1);
        actionBar.selectTab(tab1);

        ActionBar.Tab tab2 = actionBar
                .newTab()
                .setText("Mentions")
                .setIcon(R.drawable.ic_action_mention)
                .setTag("MentionsTimelineFragment")
                .setTabListener(
                        new FragmentTabListener<MentionsTimelineFragment>(R.id.flContainer, this, "MentionsTimelineFragment",
                                MentionsTimelineFragment.class)
                );

        actionBar.addTab(tab2);
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

    public void onComposeTweet(MenuItem mi) {
        ComposeDialogFragment composeFragment = new ComposeDialogFragment();
        // Show DialogFragment
        Bundle args = new Bundle();
        args.putParcelable("loginUser", loginUser);
        composeFragment.setArguments(args);
        composeFragment.show(fm, "Compose new Tweet Fragment");
    }

    public void onUserProfile(MenuItem mi) {
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra(User.ARG_PARAM, loginUser);
        startActivity(i);
    }

    @Override
    public void onFinishComposeDialog(String t) {
        client.postTweet(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject jsonObj) {
                Tweet tweet = Tweet.fromJSON(jsonObj);
                HomeTimelineFragment f = (HomeTimelineFragment)fm.findFragmentByTag("HomeTimelineFragment");
                f.addNewTweet(tweet);
            }

            @Override
            public void onFailure(Throwable e, String s) {
                Log.d("debug", e.toString());
                Log.d("debug", s.toString());
            }
        }, t);
    }

}
