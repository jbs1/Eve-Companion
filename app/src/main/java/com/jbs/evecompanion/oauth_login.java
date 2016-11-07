package com.jbs.evecompanion;


import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class oauth_login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth_login);

        Intent intent = getIntent();
        Uri data = intent.getData();

        if(data != null){
            new add_char_to_database(oauth_login.this).execute(data.getQueryParameter("code"));
        }
    }
}


