package com.jbs.evecompanion;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.System.in;


public class MainActivity extends AppCompatActivity {
    static DBhelper myDB;
    static String oauth_id = "ef19b0edc7464dd1aa1336d1e59ed6e2";
    static String oauth_sec = "QdoIBWm7cUAngPJIGMeDdEezUNi6TFekFQBqNQR4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDB=new DBhelper(this);
        //myDB.access_token(93588930);
    }

    @Override
    public void onResume() {
        super.onResume();
        LinearLayout ll= (LinearLayout) findViewById(R.id.char_layout);
        if(( ll).getChildCount() > 0)
            ( ll).removeAllViews();

        JSONArray chars=myDB.get_all_chars();
        TextView[] tv= new TextView[chars.length()];

        View.OnClickListener char_listener= new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), CharView.class);
                intent.putExtra("char_id", (int) v.getTag());
                //Log.i("eve_click_row",v.getTag().toString());

                startActivity(intent);

            }
        };


        for (int i = 0; i < chars.length(); i++) {
            try {
                tv[i] = new TextView(this);
                JSONObject c = (JSONObject) chars.get(i);
                tv[i].setText(c.getString("name")+"\n"+c.getString("id"));
                tv[i].setTag(c.getInt("id"));
                tv[i].setOnClickListener(char_listener);
                ll.addView(tv[i]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Log.i("eve_logged_chars",MainActivity.myDB.getTableAsString("char_table"));


    }

    /**Calls eve login on button click**/
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
