package com.codepath.instagramviewer.instagramviewer;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PhotosActivity extends Activity {
    public static final String CLIENT_ID="caa2d15e2de442809782a23e68a1a5f9";
    private List<InstagramPhoto> photos;
    private InstagramPhotosAdapter  aPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

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
                JSONArray photosJSON = null;
                try{
                    photos.clear();

                    photosJSON = response.getJSONArray("data");
                    for (int i = 0; i < photosJSON.length(); i++) {
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        if (!photoJSON.isNull("caption")) {
                            photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        }
                        photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");

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
