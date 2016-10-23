package com.jbs.evecompanion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**Call login activity on button press**/
    public void oauth_login(View view) {
        Intent login=new Intent(this, oauth_login.class);
        startActivity(login);

    }
}
