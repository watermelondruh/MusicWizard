package com.hfad.musicwizard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "22cf013cdf864bd7b350e83083465460";
    private static final String REDIRECT_URI = "testschema://callback";

    private static final String ACCESS_TOKEN_NAME = "webapi.credentials.access_token";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String ACCESS_TOKEN_EXPIRES_AT = "expires_at";

    private static final int REQUEST_CODE = 1337;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context appContext = this.getApplicationContext();
        SharedPreferences sharedPreferences = appContext.getSharedPreferences(ACCESS_TOKEN_NAME, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(ACCESS_TOKEN, null);
        long expiresAt = sharedPreferences.getLong(ACCESS_TOKEN_EXPIRES_AT,0L);

        if (token == null || expiresAt < System.currentTimeMillis()) {
            token = null;
        }
        if (token == null)
            setContentView(R.layout.activity_login);
        else
            startMainActivity(token);
    }

    public void onLoginButtonClicked(View view) {
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            switch (response.getType()) {
                case TOKEN:
                    Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
                    Log.d("LoginActivity", "Success!");

                    Context appContext = this.getApplicationContext();
                    SharedPreferences sharedPreferences = appContext.getSharedPreferences(ACCESS_TOKEN_NAME, Context.MODE_PRIVATE);
                    String token = response.getAccessToken();
                    long now = System.currentTimeMillis();
                    long expiresAt = now + TimeUnit.SECONDS.toMillis(response.getExpiresIn());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(ACCESS_TOKEN, token);
                    editor.putLong(ACCESS_TOKEN_EXPIRES_AT, expiresAt);
                    editor.apply();

                    startMainActivity(response.getAccessToken());
                    break;

                case ERROR:
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                    Log.d("LoginActivity", "Auth error: " + response.getError());
                    break;

                default:
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                    Log.d("LoginActivity", "Auth error: " + response.getError());
            }
        }
    }
    private void startMainActivity(String token) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_TOKEN, token);
        startActivity(intent);
        finish();
    }
}