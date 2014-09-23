package com.codepath.gridimagesearch.Helper;

import android.widget.Spinner;
import android.widget.SpinnerAdapter;

/**
 * Created by qingdi on 9/22/14.
 */
public class SpinnerHelper {
    public static void setSpinnerToValue(Spinner spinner, String value) {
        int index = 0;
        SpinnerAdapter adapter = spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(value)) {
                index = i;
                break; // terminate loop
            }
        }
        spinner.setSelection(index);
    }

}
