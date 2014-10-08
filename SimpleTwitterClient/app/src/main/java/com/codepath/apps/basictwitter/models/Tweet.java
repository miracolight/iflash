package com.codepath.apps.basictwitter.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.codepath.apps.basictwitter.helpers.DateHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingdi on 9/23/14.
 */
@Table(name = "Tweets")
public class Tweet extends Model{
    @Column(name = "tid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long    tid;

    @Column(name = "body")
    private String  body;

    @Column(name = "createdat")
    private String  createdAt;

    @Column(name = "relativecreatedat")
    private String  relativeCreatedAt;

    @Column(name = "User")
    private User    user;

    @Column(name = "type")
    private int    type;

    @Column(name = "mediaurl")
    private String mediaUrl;

    public Tweet() {
        super();
    }

    public Tweet(long id, String body, String createdAt, String relativeCreatedAt, User user, int type, String mediaUrl) {
        super();
        this.tid = id;
        this.body = body;
        this.createdAt = createdAt;
        this.relativeCreatedAt = relativeCreatedAt;
        this.user = user;
        this.type = type;
        this.mediaUrl = mediaUrl;
    }

    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.getString("text");
            tweet.tid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.relativeCreatedAt = DateHelper.getRelativeTimeAgo(tweet.createdAt);
            if(!jsonObject.isNull("extended_entities")) {
                tweet.mediaUrl = jsonObject.getJSONObject("extended_entities").getJSONArray("media").getJSONObject(0).getString("media_url");
            }

            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> results = new ArrayList<Tweet>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                results.add(Tweet.fromJSON(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    public String getBody() {
        return body;
    }

    public long getTid() {
        return tid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getRelativeCreatedAt() {
        return relativeCreatedAt;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static List<Tweet> getAll(int tweetType, String userId) {
        // This is how you execute a query
        From s =  new Select()
                .from(Tweet.class)
                .where("type = ?", tweetType);
        if (userId != null) {
            s = s.where("User = ?", userId);
        }
        return s.execute();
    }

    public static List<Tweet> getAll() {
        // This is how you execute a query
        From s =  new Select()
                .from(Tweet.class);
        return s.execute();
    }

    public static int getTweetType(String queryMethod) {
        if ("getHomeTimeline".equals(queryMethod)){
            return 0;
        } else if ("getMentionsTimeline".equals(queryMethod)){
            return 1;
        } else if ("getUserTimeline".equals(queryMethod)){
            return 2;
        }
        return -1;
    }
}
