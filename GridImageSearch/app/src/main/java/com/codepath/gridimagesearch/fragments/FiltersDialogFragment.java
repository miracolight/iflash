package com.codepath.gridimagesearch.fragments;

import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.gridimagesearch.Helper.SpinnerHelper;
import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.models.AdvancedFilter;


public class FiltersDialogFragment extends DialogFragment {

    private AdvancedFilter  advancedFilter;
    private Spinner         spImageSize;
    private Spinner         spImageType;
    private Spinner         spColorFilter;
    private EditText        etSiteFilter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        advancedFilter = (AdvancedFilter)getArguments().getSerializable("filter");

        getDialog().setTitle(title);

        View view = inflater.inflate(R.layout.fragment_search_filters, container);
        setViews(view);
        return view;
    }

    private void setViews(View rootView) {
        spImageSize = (Spinner)rootView.findViewById(R.id.spImageType);
        spImageType = (Spinner)rootView.findViewById(R.id.spImageType);
        spColorFilter = (Spinner)rootView.findViewById(R.id.spColorFilter);
        etSiteFilter = (EditText)rootView.findViewById(R.id.etSiteFilter);

        if (advancedFilter != null) {
            SpinnerHelper.setSpinnerToValue(spImageSize, advancedFilter.imageSize);
            SpinnerHelper.setSpinnerToValue(spImageType, advancedFilter.imageType);
            SpinnerHelper.setSpinnerToValue(spColorFilter, advancedFilter.colorFilter);
            etSiteFilter.setText(advancedFilter.siteFilter);
        }


        Button saveButton = (Button)rootView.findViewById(R.id.btnSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveFilter(v);
                getDialog().dismiss();
            }
        });

        Button cancelButton = (Button)rootView.findViewById(R.id.btnCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
    }

    private void saveFilter(View v) {
        if (advancedFilter == null) {
            advancedFilter = new AdvancedFilter();
        }
        advancedFilter.imageSize = spImageSize.getSelectedItem().toString();
        advancedFilter.imageType = spImageType.getSelectedItem().toString();
        advancedFilter.colorFilter = spColorFilter.getSelectedItem().toString();
        advancedFilter.siteFilter = etSiteFilter.getText().toString();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        FilterDialogListener activity = (FilterDialogListener) getActivity();
        activity.onFinishFilterDialog(advancedFilter);
        this.dismiss();
    }

    public interface FilterDialogListener {
        void onFinishFilterDialog(AdvancedFilter filter);
    }
}
