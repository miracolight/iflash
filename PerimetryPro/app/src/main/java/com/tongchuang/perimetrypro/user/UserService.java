package com.tongchuang.perimetrypro.user;

/**
 * Created by qingdi on 8/5/16.
 */
public interface UserService {

    public void getUserInfo(String barCode, UserServiceResponseHandler responseHandler);
}
