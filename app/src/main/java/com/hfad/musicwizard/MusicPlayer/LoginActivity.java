package com.hfad.musicwizard.MusicPlayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hfad.musicwizard.R;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "22cf013cdf864bd7b350e83083465460";
    private static final String REDIRECT_URI = "testschema://callback";

    private static final int REQUEST_CODE = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String token = CredentialsHandler.getToken(this);
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

        // check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            switch (response.getType()) {
                // response was successful and contains auth token
                case TOKEN:
                    // handle successful response
                    Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
                    Log.d("LoginActivity", "Success!");
                    CredentialsHandler.setToken(this, response.getAccessToken(), response.getExpiresIn(), TimeUnit.SECONDS);
                    startMainActivity(response.getAccessToken());
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                    Log.d("LoginActivity", "Auth error: " + response.getError());
                    break;

                // Most likely auth flow was cancelled
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
