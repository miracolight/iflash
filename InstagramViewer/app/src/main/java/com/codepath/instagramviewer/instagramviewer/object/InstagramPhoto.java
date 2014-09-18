package com.codepath.instagramviewer.instagramviewer.object;

/**
 * Created by qingdi on 9/13/14.
 */
public class InstagramPhoto {
    public InstagramUser    user;
    public String           caption;
    public String           imageUrl;
    public int              imageWidth;
    public int              imageHeight;
    public int              likesCount;
    public long             creationDate;

    public InstagramComment lastComment;

}
