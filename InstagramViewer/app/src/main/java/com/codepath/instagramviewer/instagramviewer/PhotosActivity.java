package com.codepath.instagramviewer.instagramviewer;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.codepath.instagramviewer.instagramviewer.object.InstagramComment;
import com.codepath.instagramviewer.instagramviewer.object.InstagramPhoto;
import com.codepath.instagramviewer.instagramviewer.object.InstagramUser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;


public class PhotosActivity extends Activity {
    public static final String CLIENT_ID="caa2d15e2de442809782a23e68a1a5f9";
    private List<InstagramPhoto> photos;
    private InstagramPhotosAdapter  aPhotos;

    private PullToRefreshLayout mPullToRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);


        // Now find the PullToRefreshLayout to setup
        mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.ptr_layout);

        // Now setup the PullToRefreshLayout
        ActionBarPullToRefresh.from(this)
                // Mark All Children as pullable
                .allChildrenArePullable()
                        // Set a OnRefreshListener
                .listener(new OnRefreshListener() {
                    @Override
                    public void onRefreshStarted(View view) {
                        fetchPopularPhotos();
                        mPullToRefreshLayout.setRefreshComplete();
                    }
                })
                // Finally commit the setup to our PullToRefreshLayout
                .setup(mPullToRefreshLayout);

        fetchPopularPhotos();
    }

    private void fetchPopularPhotos() {
        photos = new ArrayList<InstagramPhoto>();
        aPhotos = new InstagramPhotosAdapter(this, photos);
        ListView lvPhotos = (ListView)findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(aPhotos);

        // setup popular url end point
        String popularUrl = "https://api.instagram.com/v1/media/popular?client_id="+CLIENT_ID;
        // create the network client
        AsyncHttpClient client = new AsyncHttpClient();
        // trigger the network request
        client.get(popularUrl, new JsonHttpResponseHandler() {
            // define the success and failure callbacks
            // handle the successful response (popular photos JSON)

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // fired once the successful response comes back
                // data=>[x]=>”images”=>”standard_resolution”=>”url”
                // data=>[x]=>”images”=>”standard_resolution”=>”height”
                // data=>[x]=>”user”=>”username”
                // data=>[x]=>”caption”=>”text”
                // data=>[x]=>”likes”=>”count”
                // data=>[x]=>"comments"=>[y]=>"text"
                // data=>[x]=>"comments"=>[y]=>"from"=>"username"
                JSONArray photosJSON = null;
                try{
                    photos.clear();

                    photosJSON = response.getJSONArray("data");
                    for (int i = 0; i < photosJSON.length(); i++) {
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        InstagramPhoto photo = new InstagramPhoto();

                        String userName = photoJSON.getJSONObject("user").getString("username");
                        String profileImgUrl = photoJSON.getJSONObject("user").getString("profile_picture");
                        photo.user = new InstagramUser(userName, profileImgUrl);

                        if (!photoJSON.isNull("caption")) {
                            photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        }
                        photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imageWidth = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("width");
                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
                        photo.creationDate = photoJSON.getLong("created_time");

                        if (!photoJSON.isNull("comments")) {
                            JSONArray commentsJSON = photoJSON.getJSONObject("comments").getJSONArray("data");
                            JSONObject commentJSON = commentsJSON.getJSONObject(0);
                            InstagramComment comment = new InstagramComment();
                            comment.text = commentJSON.getString("text");
                            comment.userName = commentJSON.getJSONObject("from").getString("username");
                            photo.lastComment = comment;
                        }
                        photos.add(photo);
                    }

                    aPhotos.notifyDataSetChanged();
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.photos, menu);
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
