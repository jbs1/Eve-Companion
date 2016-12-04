package com.jbs.evecompanion;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;

import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class CharView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_char_view);

        Intent intent = getIntent();

        int char_id = intent.getIntExtra("char_id", 0);

        TextView char_id_view = (TextView) findViewById(R.id.view_char_id);
        char_id_view.setText(String.format(Locale.getDefault(),"cid: %1$d",char_id));


        GetRequest at = new GetRequest(this,"https://esi.tech.ccp.is/latest/characters/"+Integer.toString(char_id)+"/skillqueue/","[{\"name\":123,\"value\":123}]");
        at.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,char_id);
        try {
            Log.i("eve_response_body",at.get().toString(2));
        } catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        /**try {
            Log.i("eve_response_body",at.get().toString(2));
        } catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }**/
    }
}
