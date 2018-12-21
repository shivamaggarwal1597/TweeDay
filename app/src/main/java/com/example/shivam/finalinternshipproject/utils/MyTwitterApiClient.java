package com.example.shivam.finalinternshipproject.utils;

import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;

/**
 * Created by shivam on 21/12/17.
 */

public class MyTwitterApiClient extends TwitterApiClient {
    public MyTwitterApiClient(TwitterSession session) {
        super(session);

    }
    public FollowingHandlesListService getCustomService() {
        return getService(FollowingHandlesListService.class);
    }

}