package com.jbs.evecompanion;


import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class OAuthCallback extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth_callback);

        Intent intent = getIntent();
        Uri data = intent.getData();

        if(data != null){
            new AddChar(OAuthCallback.this).execute(data.getQueryParameter("code"));
        }
    }
}


