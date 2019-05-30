package com.hfad.musicwizard.SpotifyAPI;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.android.MainThreadExecutor;

public class SpotifyAPI {

    private static final String BASE_URL = "https://api.spotify.com/v1";
    private SpotifyService spotifyService;
    private String token;

    private class WebAPIAuthenticator implements RequestInterceptor {
        @Override
        public void intercept(RequestInterceptor.RequestFacade request) {
            if (token != null) {
                request.addHeader("Authorization", "Bearer " + token);
            }
        }
    }

    public SpotifyAPI() {
        Executor httpExecutor = Executors.newSingleThreadExecutor();
        MainThreadExecutor callbackExecutor = new MainThreadExecutor();
        spotifyService = execute(httpExecutor, callbackExecutor);
    }

    private SpotifyService execute(Executor httpExecutor, Executor callbackExecutor) {
        RestAdapter restAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.BASIC)
                .setExecutors(httpExecutor, callbackExecutor)
                .setEndpoint(BASE_URL)
                .setRequestInterceptor(new WebAPIAuthenticator())
                .build();

        return restAdapter.create(SpotifyService.class);
    }

    public void setToken(String token) {
        this.token = token;
    }

    public SpotifyService getService() {
        return spotifyService;
    }

}
