package com.jbs.evecompanion;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


public class oauth_callback extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth_callback);

        Intent intent = getIntent();
        Uri data = intent.getData();
        Log.i("eve_callback_code", data.getQueryParameter("code"));
        Log.i("eve_callback_state", data.getQueryParameter("state"));



        /**String urlParameters  = "grant_type=authorization_code&code="+data.getQueryParameter("code");
        byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
        int    postDataLength = postData.length;
        /**
        conn.setRequestProperty( "Authorization", enc_auth);
        conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty( "charset", "utf-8")
        url = new URL("https://login.eveonline.com/oauth/token");**/
/**
        String url = "https://login.eveonline.com/oauth/token";
        String charset = "UTF-8";

        String grant_type = "authorization_code";
        String enc_auth = Base64.encodeToString("9d7dcbb0380f450ea0d2b435b60f4c15:ssEAFgv5PfEs29bluxs9N3milKgC7j6saILCtMPw".getBytes(), Base64.DEFAULT);

        Log.i("eve_callback_base64",enc_auth);

        String query = null;
        try {
            query = String.format("grant_type=%s&code=%s",
                    URLEncoder.encode(grant_type, charset),
                    URLEncoder.encode(data.getQueryParameter("code"), charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        URLConnection connection = null;
        try {
            connection = new URL(url).openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.setDoOutput(true); // Triggers POST.
        connection.setRequestProperty("Accept-Charset", charset);
        connection.setRequestProperty( "Authorization", enc_auth);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);


        try (OutputStream output = connection.getOutputStream()) {
            output.write(query.getBytes(charset));
        } catch (IOException e) {
            e.printStackTrace();
        }**/


/**
        String enc_auth = Base64.encodeToString("9d7dcbb0380f450ea0d2b435b60f4c15:ssEAFgv5PfEs29bluxs9N3milKgC7j6saILCtMPw".getBytes(), Base64.DEFAULT);

        Log.i("eve_enc_auth",enc_auth);


        String charset = "UTF-8";
        String grant_type = "authorization_code";

        String query = null;
        try {
            query = String.format("grant_type=%s&code=%s",
                    URLEncoder.encode(grant_type, charset),
                    URLEncoder.encode(data.getQueryParameter("code"), charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.i("eve_token_query",query);
**/



        //int status = httpConnection.getResponseCode();
        new post_get_access_token().execute(data.getQueryParameter("code"));


    }
}


class post_get_access_token extends AsyncTask<String, Void, String> {
    private Exception exception;
    protected String doInBackground(String... code) {
        try {
            String url = "https://login.eveonline.com/oauth/token";
            String charset = "UTF-8";
            String grant_type = "authorization_code";
            //encode id / secret
            String enc_auth = Base64.encodeToString("9d7dcbb0380f450ea0d2b435b60f4c15:ssEAFgv5PfEs29bluxs9N3milKgC7j6saILCtMPw".getBytes(), Base64.NO_WRAP);
            Log.i("eve_auth_string_check",enc_auth);

            //create body
            String query = null;
            try {
                query = String.format("grant_type=%s&code=%s",
                        URLEncoder.encode(grant_type, charset),
                        URLEncoder.encode(code[0], charset));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.i("eve_token_query",query);

            //build connection
            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Charset", charset);
            con.setRequestProperty("Authorization","Basic "+enc_auth);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=" + charset);

            //print request headers
            for (Map.Entry<String, List<String>> req_prop : con.getRequestProperties().entrySet()) {
                Log.i("eve_request_header",req_prop.getKey() + "=" + req_prop.getValue());
            }

            //write body
            con.setDoOutput(true);
            try (OutputStream output = con.getOutputStream()) {
                output.write(query.getBytes(charset));
            }


            //check response headers
            for (Map.Entry<String, List<String>> header : con.getHeaderFields().entrySet()) {
                Log.i("eve_response_header",header.getKey() + "=" + header.getValue());
            }

            //check response message
            Log.i("eve_resp_msg",con.getResponseMessage());


            //check response body
            String result = null;
            StringBuffer sb = new StringBuffer();
            InputStream is = null;


                is = new BufferedInputStream(con.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String inputLine = "";
                while ((inputLine = br.readLine()) != null) {
                    sb.append(inputLine);
                }
                result = sb.toString();

            Log.i("eve_resp_body","----------------------NOW-------------------");
            Log.i("eve_resp_body",result);



        } catch (Exception e) {
            this.exception = e;
            return null;
        }
        return "bla";
    }

    protected void onPostExecute(String... response) {
        // TODO: check this.exception
        // TODO: do something with the feed
    }


}