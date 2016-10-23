package com.jbs.evecompanion;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class oauth_login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth_login);
    }

    public void login_to_eve(View view){
        Uri uri = Uri.parse("https://login.eveonline.com/oauth/authorize/?response_type=code" +
                "&redirect_uri=x-eve-oauth%3A%2F%2Fcallback" +
                "&client_id=9d7dcbb0380f450ea0d2b435b60f4c15" +
                "&scope=characterAccountRead%20characterWalletRead" +
                "&state=login_eve_first");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
