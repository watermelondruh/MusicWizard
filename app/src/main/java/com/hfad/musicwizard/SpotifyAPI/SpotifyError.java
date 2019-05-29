package com.hfad.musicwizard.SpotifyAPI;

import kaaes.spotify.webapi.android.models.*;
import retrofit.RetrofitError;

public class SpotifyError extends Exception {

    private final RetrofitError mRetrofitError;
    private final ErrorDetails mErrorDetails;

    public static SpotifyError fromRetrofitError(RetrofitError error) {
        ErrorResponse errorResponse = null;

        try {
            errorResponse = (ErrorResponse) error.getBodyAs(ErrorResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (errorResponse != null && errorResponse.error != null) {
            String message = errorResponse.error.status + " " + errorResponse.error.message;
            return new SpotifyError(error, errorResponse.error, message);
        } else {
            return new SpotifyError(error);
        }
    }

    public SpotifyError(RetrofitError retrofitError, ErrorDetails errorDetails, String message) {
        super(message, retrofitError);
        mRetrofitError = retrofitError;
        mErrorDetails = errorDetails;
    }

    public SpotifyError(RetrofitError retrofitError) {
        super(retrofitError);
        mRetrofitError = retrofitError;
        mErrorDetails = null;
    }

    /**
     * @return the original {@link retrofit.RetrofitError} that was returned for this request.
     */
    public RetrofitError getRetrofitError() {
        return mRetrofitError;
    }

    /**
     * @return true if there are {@link kaaes.spotify.webapi.android.models.ErrorDetails}
     * associated with this error. False otherwise.
     */
    public boolean hasErrorDetails() {
        return mErrorDetails != null;
    }

    /**
     * @return Details returned from the Web API associated with this error if present.
     */
    public ErrorDetails getErrorDetails() {
        return mErrorDetails;
    }
}