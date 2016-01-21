package com.baxter.githubexplore.network;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;

/**
 * Created by tbaxter on 1/20/16.
 */
public class GitHubClient {

    public static final String API_URL = "https://api.github.com";

    private GitHubClient() { }

    public static GitHubApi createGitHubClient(final String githubToken) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create())
                .baseUrl(API_URL);

        if (!TextUtils.isEmpty(githubToken)) {

            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Request newReq = request.newBuilder()
                            .addHeader("Authorization", String.format("token %s", githubToken))
                            .build();
                    return chain.proceed(newReq);
                }
            }).build();

            builder.client(client);
        }

        return builder.build().create(GitHubApi.class);
    }
}