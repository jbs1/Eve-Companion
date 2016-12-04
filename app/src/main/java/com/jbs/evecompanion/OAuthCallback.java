package com.jbs.evecompanion;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class OAuthCallback extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth_callback);

        Intent intent = getIntent();
        Uri data = intent.getData();



        if(data != null){
            try {
                JSONObject newchar=new AddChar(OAuthCallback.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,data.getQueryParameter("code")).get();
                TextView tv1 = (TextView)findViewById(R.id.textView2);
                tv1.setText("Login Successful for "+newchar.getString("CharacterName")+
                        "("+newchar.getString("CharacterID")+")\n"+
                        "Waiting for database confirmation!"
                );
            } catch (JSONException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}


