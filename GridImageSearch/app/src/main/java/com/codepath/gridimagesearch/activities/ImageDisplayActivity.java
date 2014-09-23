package com.codepath.gridimagesearch.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ShareActionProvider;

import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.models.ImageItem;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ImageDisplayActivity extends Activity {

    private ImageView ivImageResult;
    private ShareActionProvider miShareAction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
      //  getActionBar().hide();
        ImageItem image = (ImageItem)getIntent().getSerializableExtra("result");
        ivImageResult = (ImageView) findViewById(R.id.ivImageResult);



        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);

        if (metrics.widthPixels < metrics.heightPixels) {
            int imgY = (int)(metrics.widthPixels*((double)image.height/image.width));
            ivImageResult.getLayoutParams().height = imgY;
            ivImageResult.getLayoutParams().width = metrics.widthPixels;
        } else {
            int imgX = (int)(metrics.heightPixels*((double)image.width/image.height));
            ivImageResult.getLayoutParams().height = metrics.heightPixels;
            ivImageResult.getLayoutParams().width = imgX;
        }

        Picasso.with(this).load(image.fullUrl).into(ivImageResult, new Callback() {
                @Override
                public void onSuccess() {
                    // Setup share intent now that image has loaded
                    setupShareIntent();
                }

                @Override
                public void onError() {
                    // ...
                }
            });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image_display, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        // Fetch reference to the share action provider
        miShareAction = (ShareActionProvider) item.getActionProvider();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }



    public void setupShareIntent() {
        // Fetch Bitmap Uri locally
        Uri bmpUri = getLocalBitmapUri(ivImageResult); // see previous remote images section
        // Create share intent as described above
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.setType("image/*");
        // Attach share event to the menu item provider
        miShareAction.setShareIntent(shareIntent);
    }

    public Uri getLocalBitmapUri(ImageView imageView) {

        Drawable mDrawable = ivImageResult.getDrawable();
        Bitmap mBitmap = ((BitmapDrawable)mDrawable).getBitmap();

        String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                mBitmap, "Image Description", null);

        Uri uri = Uri.parse(path);
        return uri;
    }

}
