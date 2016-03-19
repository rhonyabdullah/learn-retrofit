package com.labsgn.learn.retrofit.utils;

import com.labsgn.learn.retrofit.model.GitModel;

import retrofit2.Call;
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
    Call<GitModel> getUser(@Path("user") String username);

}
