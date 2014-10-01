package com.codepath.apps.basictwitter.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;


public class ComposeDialogFragment extends DialogFragment {
    private User            loginUser;
    private EditText        etTweet;
    private String          tweetContent;
    private ImageView       ivProfileImage;
    private TextView        tvUserName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loginUser = getArguments().getParcelable("loginUser");

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.fragment_compose, container);
        setViews(view);
        return view;
    }

    private void setViews(View rootView) {
        ivProfileImage = (ImageView)rootView.findViewById(R.id.ivProfileImage);
        ivProfileImage.setImageResource(0);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(loginUser.getProfileImageUrl(), ivProfileImage);

        tvUserName = (TextView)rootView.findViewById(R.id.tvUserName);
        tvUserName.setText(loginUser.getScreenName());

        etTweet = (EditText)rootView.findViewById(R.id.etTweet);


        Button saveButton = (Button)rootView.findViewById(R.id.btnSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tweetContent = etTweet.getText().toString();
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


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        ComposeDialogListener activity = (ComposeDialogListener) getActivity();
        activity.onFinishComposeDialog(tweetContent);
        this.dismiss();
    }

    public interface ComposeDialogListener {
        void onFinishComposeDialog(String t);
    }
}
