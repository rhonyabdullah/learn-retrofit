package com.labsgn.learn.retrofit.utils;

import com.labsgn.learn.retrofit.model.GitModel;

import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by rhony on 17/03/16.
 */
public interface GitApi {

    /**
     * user : String yang diambil dari textField
     */
    @GET("/users/{user}")
    void getUserDetail(@Path("user") String user, Callback<GitModel> response);
}
