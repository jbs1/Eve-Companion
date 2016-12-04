package com.jbs.evecompanion;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


class AccessToken extends AsyncTask<Void, Void, JSONObject> {
    private JSONObject charobj;

    AccessToken(JSONObject cid){
        charobj = cid;
        try {
            Log.i("eve_",cid.toString(2));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected JSONObject doInBackground(Void... params) {

        try {
            String url = "https://login.eveonline.com/oauth/token";
            String charset = "UTF-8";
            String grant_type = "refresh_token";
            //encode id / secret
            String enc_auth = Base64.encodeToString((MainActivity.oauth_id+":"+MainActivity.oauth_sec).getBytes(), Base64.NO_WRAP);


            //create body
            String query = null;
            try {
                query = String.format("grant_type=%s&refresh_token=%s",
                        URLEncoder.encode(grant_type, charset),
                        URLEncoder.encode(charobj.getString("refresh"), charset));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            //build connection
            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Charset", charset);
            con.setRequestProperty("Authorization","Basic "+enc_auth);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=" + charset);

            //print request headers
            /**for (Map.Entry<String, List<String>> req_prop : con.getRequestProperties().entrySet()) {
                Log.i("eve_request_header",req_prop.getKey() + "=" + req_prop.getValue());
            }**/

            //write body
            con.setDoOutput(true);
            try (OutputStream output = con.getOutputStream()) {
                if (query != null) {
                    output.write(query.getBytes(charset));
                }
            }

            //check response headers
            /**for (Map.Entry<String, List<String>> header : con.getHeaderFields().entrySet()) {
                Log.i("eve_response_header",header.getKey() + "=" + header.getValue());
            }**/


            //check response body
            String result;
            StringBuilder sb = new StringBuilder();
            InputStream is;


            is = new BufferedInputStream(con.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            result = sb.toString();

            return new JSONObject(result);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }


}