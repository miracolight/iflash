package com.codepath.apps.basictwitter.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by qingdi on 9/23/14.
 */
public class User implements Parcelable{
    public static final String ARG_PARAM = "User";

    private String  name;
    private long    uid;
    private String  screenName;
    private String  profileImageUrl;
    private String  description;
    private int     followersCount;
    private int     friendsCount;

    public User() {

    }

    public static User fromJSON(JSONObject jsonObject) {
        User user = new User();
        try {
            user.name = jsonObject.getString("name");
            user.uid = jsonObject.getLong("id");
            user.screenName = jsonObject.getString("screen_name");
            user.profileImageUrl = jsonObject.getString("profile_image_url");
            user.description = jsonObject.getString("description");
            user.followersCount = jsonObject.getInt("followers_count");
            user.friendsCount = jsonObject.getInt("friends_count");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return user;
    }

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getDescription() {
        return description;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeLong(uid);
        out.writeString(name);
        out.writeString(screenName);
        out.writeString(profileImageUrl);
        out.writeString(description);
        out.writeInt(followersCount);
        out.writeInt(friendsCount);
    }

    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private User(Parcel in) {
        uid = in.readLong();
        name = in.readString();
        screenName = in.readString();
        profileImageUrl = in.readString();
        description = in.readString();
        followersCount = in.readInt();
        friendsCount = in.readInt();
    }
}
