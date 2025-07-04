package com.example.tareasoauthapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.ResponseTypeValues;

import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.ResponseTypeValues;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationException;

public class MainActivity extends AppCompatActivity {

    private static final int RC_AUTH = 100;
    private AuthorizationService authService;
    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AuthorizationServiceConfiguration serviceConfig =
                new AuthorizationServiceConfiguration(
                        Uri.parse("https://accounts.google.com/o/oauth2/v2/auth"),
                        Uri.parse("https://oauth2.googleapis.com/token")
                );

        AuthorizationRequest request = new AuthorizationRequest.Builder(
                serviceConfig,
                "TU_CLIENT_ID", // ← Reemplázalo con tu verdadero client ID de Google
                ResponseTypeValues.CODE,
                Uri.parse("com.example.tareasoauthapp:/callback")
        ).setScopes("openid", "profile", "email").build();

        authService = new AuthorizationService(this);
        Intent authIntent = authService.getAuthorizationRequestIntent(request);
        startActivityForResult(authIntent, RC_AUTH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_AUTH) {
            AuthorizationResponse resp = AuthorizationResponse.fromIntent(data);
            AuthorizationException ex = AuthorizationException.fromIntent(data);

            if (resp != null) {
                authService.performTokenRequest(
                        resp.createTokenExchangeRequest(),
                        (response, exception) -> {
                            if (response != null) {
                                accessToken = response.idToken;
                                Intent i = new Intent(this, TareasActivity.class);
                                i.putExtra("token", accessToken);
                                startActivity(i);
                            }
                        });
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
