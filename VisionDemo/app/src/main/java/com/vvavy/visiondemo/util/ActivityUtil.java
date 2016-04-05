package com.vvavy.visiondemo.util;

import android.app.ActionBar;
import android.app.Activity;
import android.view.View;

/**
 * Created by qingdi on 3/10/16.
 */
public class ActivityUtil {
    public static void hideStatusBar(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        ActionBar actionBar = activity.getActionBar();
        actionBar.hide();
    }
}
