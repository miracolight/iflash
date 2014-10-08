package com.codepath.apps.basictwitter.fragments;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by qingdi on 10/4/14.
 */
public class MentionsTimelineFragment extends TweetsListFragment{

    public void populateTimeline() {
        Map params = null;
        if (maxId != null) {
            params = new HashMap<String, String>();
            params.put("max_id", maxId.toString());
        }

        populateTimeline("getMentionsTimeline", params);
    }
}
