package com.baxter.githubexplore.network;

import com.baxter.githubexplore.models.Repository;
import com.baxter.githubexplore.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by tbaxter on 1/20/16.
 */
public interface GitHubApi {

    /**
     * See https://developer.github.com/v3/users/
     */
    @GET("/users/{user}")
    Call<User> getUser(@Path("user") String user);

    /**
     * See https://developer.github.com/v3/repos/
     */
    @GET("users/{user}/repos")
    Call<List<Repository>> getRepos(@Path("user") String user);

}