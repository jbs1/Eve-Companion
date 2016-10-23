package com.jbs.evecompanion;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class oauth_callback extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth_callback);

        Intent intent = getIntent();
        Uri data = intent.getData();
        Log.i("eve_callback_response", data.getQueryParameter("code"));
        Log.i("eve_callback_response", data.getQueryParameter("state"));


        String enc_auth = Base64.encodeToString("9d7dcbb0380f450ea0d2b435b60f4c15:ssEAFgv5PfEs29bluxs9N3milKgC7j6saILCtMPw".getBytes(), Base64.DEFAULT);
        Log.i("eve_callback_base64",enc_auth);

        String urlParameters  = "grant_type=authorization_code&code="+data.getQueryParameter("code");
        byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
        int    postDataLength = postData.length;


        URL url = null;
        try {
            url = new URL("https://login.eveonline.com/oauth/token");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn= null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        conn.setDoOutput( true );
        try {
            conn.setRequestMethod( "POST" );
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        conn.setRequestProperty( "Authorization", enc_auth);
        conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty( "charset", "utf-8");
        conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
        conn.setUseCaches( false );
        try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
            wr.write( postData );
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
