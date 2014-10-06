package com.codepath.apps.basictwitter.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.fragments.ProfileFragment;
import com.codepath.apps.basictwitter.fragments.TweetsListFragment;
import com.codepath.apps.basictwitter.fragments.UserTimelineFragment;
import com.codepath.apps.basictwitter.models.User;

public class ProfileActivity extends FragmentActivity {

    private User    user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (savedInstanceState == null) {
            user = getIntent().getParcelableExtra("User");
            getActionBar().setTitle("@"+user.getScreenName());

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ProfileFragment profileFragment = ProfileFragment.newInstance(user);
            ft.replace(R.id.flProfileHeader, profileFragment);

            UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(user);
            ft.replace(R.id.flUserTimeline, userTimelineFragment);
            ft.commit();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
