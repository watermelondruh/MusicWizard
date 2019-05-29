package com.hfad.musicwizard.SpotifyAPI;

import retrofit.Callback;
import retrofit.RetrofitError;

public abstract class SpotifyCallback<T> implements Callback<T> {
    public abstract void failure(SpotifyError error);

    @Override
    public void failure(RetrofitError error) {
        failure(SpotifyError.fromRetrofitError(error));
    }
}
