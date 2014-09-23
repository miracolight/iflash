package com.codepath.gridimagesearch.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.gridimagesearch.Helper.InternetHelper;
import com.etsy.android.grid.StaggeredGridView;
import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.adapters.ImageResultsAdapter;
import com.codepath.gridimagesearch.fragments.FiltersDialogFragment;
import com.codepath.gridimagesearch.models.AdvancedFilter;
import com.codepath.gridimagesearch.models.ImageItem;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends FragmentActivity implements FiltersDialogFragment.FilterDialogListener {

    public static final int SEARCH_PAGE_SIZE = 8;
    private FragmentManager fm = getSupportFragmentManager();


    private SearchView          searchView;
    private String              queryString;

    private StaggeredGridView   gvResults;
    private List<ImageItem>     imageResults;

    private ImageResultsAdapter aImageResultsAdapter;

    private AdvancedFilter      advancedFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setViews();
        imageResults = new ArrayList<ImageItem>();
        aImageResultsAdapter = new ImageResultsAdapter(this, imageResults);
        gvResults.setAdapter(aImageResultsAdapter);
    }

    @Override
    public View onCreateView(String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    private void setViews() {
        gvResults = (StaggeredGridView)findViewById(R.id.gvResults);
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);
                ImageItem item = imageResults.get(position);
                i.putExtra("result", item);
                startActivity(i);
            }
        });

        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                doImageSearch(page * SEARCH_PAGE_SIZE);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queryString = query;
                doImageSearch(0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    public void onImageSearch(View v) {
        doImageSearch(0);
    }


    private void doImageSearch(int offset) {

        if (!InternetHelper.isNetworkAvailable(this)) {
            Toast.makeText(SearchActivity.this, "No internet connection. ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (queryString == null || queryString.isEmpty()) {
            return;
        }

        String query = URLEncoder.encode(queryString);
        final int startOffset = offset;

        // https://ajax.googleapis.com/ajax/services/search/images?q=android&v=1.0&rsz=8
        String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?q="+query+"&v=1.0&rsz=8"
                +"&start="+offset
                +(advancedFilter==null?"":advancedFilter.getFilterString());
        // create the network client
        AsyncHttpClient client = new AsyncHttpClient();
        // trigger the network request
        client.get(searchUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // fired once the successful response comes back

                try{
                    JSONArray imageResultsJSON = response.getJSONObject("responseData").getJSONArray("results");
                    if (startOffset == 0) {
                        imageResults.clear();
                    }

                    aImageResultsAdapter.addAll(ImageItem.fromJSONArray(imageResultsJSON));
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_config_filters) {
            FiltersDialogFragment filtersFragment = new FiltersDialogFragment();
            // Show DialogFragment
            Bundle args = new Bundle();
            args.putString("title", "Advanced Filters");
            args.putSerializable("filter", advancedFilter);
            filtersFragment.setArguments(args);
            filtersFragment.show(fm, "Advanced Filters Dialog Fragment");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFinishFilterDialog(AdvancedFilter filter) {
        advancedFilter = filter;
        doImageSearch(0);
    }
}
