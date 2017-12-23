package com.example.guge.web;


import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by GUGE on 2017/12/22.
 */

public interface GithubService {
    @GET("/users/{user}")
    Observable<Information> getUser(@Path("user") String user);
}
