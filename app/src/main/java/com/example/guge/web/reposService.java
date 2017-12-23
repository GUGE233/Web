package com.example.guge.web;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by GUGE on 2017/12/24.
 */

public interface reposService {
    @GET("/users/{user}/repos")
    Observable<List<repos>> getUser(@Path("user") String user);
}
