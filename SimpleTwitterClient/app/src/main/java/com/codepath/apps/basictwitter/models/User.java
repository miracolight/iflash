package com.codepath.apps.basictwitter.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

/**
 * Created by qingdi on 9/23/14.
 */

@Table(name = "Users")
public class User extends Model implements Parcelable{
    public static final String ARG_PARAM = "User";

    @Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long    uid;

    @Column(name = "name")
    private String  name;

    @Column(name = "screenname")
    private String  screenName;

    @Column(name = "profileimageurl")
    private String  profileImageUrl;

    @Column(name = "description")
    private String  description;

    @Column(name = "followerscount")
    private int     followersCount;

    @Column(name = "friendscount")
    private int     friendsCount;

    @Column(name = "statusescount")
    private int     statusesCount;

    public User() {
        super();
    }

    public User(long uid, String name, String screenName, String profileImageUrl, String description,
                int followersCount, int friendsCount, int statusesCount) {
        this.uid = uid;
        this.name = name;
        this.screenName = screenName;
        this.profileImageUrl = profileImageUrl;
        this.description = description;
        this.followersCount = followersCount;
        this.friendsCount = friendsCount;
        this.statusesCount = statusesCount;
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
            user.statusesCount = jsonObject.getInt("statuses_count");
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

    public int getStatusesCount() {
        return statusesCount;
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
        out.writeInt(statusesCount);
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
        statusesCount = in.readInt();
    }

    public static List<User> getByUid(long userId) {
        // This is how you execute a query
        From s =  new Select()
                .from(User.class)
                .where("uid = ?", Long.toString(userId));
        return s.execute();
    }

    public static List<User> getAll() {
        // This is how you execute a query
        return new Select()
                .from(User.class)
                .execute();
    }
}
