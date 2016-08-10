package com.tongchuang.perimetrypro.user;

/**
 * Created by qingdi on 8/9/16.
 */
public interface UserServiceResponseHandler {
    public void onFailure();

    public void onSuccess(UserInfo userInfo);
}
