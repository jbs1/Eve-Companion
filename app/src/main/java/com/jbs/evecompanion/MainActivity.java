package com.jbs.evecompanion;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONException;


public class MainActivity extends AppCompatActivity {
    static DBhelper myDB;
    static String oauth_id = "ef19b0edc7464dd1aa1336d1e59ed6e2";
    static String oauth_sec = "QdoIBWm7cUAngPJIGMeDdEezUNi6TFekFQBqNQR4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDB=new DBhelper(this);
    }

    @Override
    public void onResume() {
        super.onResume();



        Log.i("eve_logged_chars",MainActivity.myDB.getTableAsString("char_table"));


    }

    /**Calls eve login on butt click**/
    public void login_to_eve(View view){
        Uri uri = Uri.parse("https://login.eveonline.com/oauth/authorize/?response_type=code" +
                "&redirect_uri=x-eve-oauth%3A%2F%2Fcallback" +
                "&client_id=" + MainActivity.oauth_id +
                "&scope=esi-skills.read_skillqueue.v1%20esi-skills.read_skills.v1" +
                "&state=login_eve_first");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
