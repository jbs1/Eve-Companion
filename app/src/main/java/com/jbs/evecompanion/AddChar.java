package com.jbs.evecompanion;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

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

import javax.net.ssl.HttpsURLConnection;


class AddChar extends AsyncTask<String, Void, JSONObject> {
    private Context ct;

    AddChar(Context context){
        ct = context;

    }

    protected JSONObject doInBackground(String... code) {



        try {
            String url = "https://login.eveonline.com/oauth/token";
            String charset = "UTF-8";
            String grant_type = "authorization_code";
            //encode id / secret
            String enc_auth = Base64.encodeToString((MainActivity.oauth_id+":"+MainActivity.oauth_sec).getBytes(), Base64.NO_WRAP);

            //create body
            String query = null;
            try {
                query = String.format("grant_type=%s&code=%s",
                        URLEncoder.encode(grant_type, charset),
                        URLEncoder.encode(code[0], charset));
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

            JSONObject json_result= new JSONObject(result);




            //get char id and name
            URL verify_obj = new URL("https://login.eveonline.com/oauth/verify");
            HttpsURLConnection verify_con = (HttpsURLConnection) verify_obj.openConnection();
            verify_con.setRequestMethod("GET");
            verify_con.setRequestProperty("Authorization","Bearer "+json_result.getString("access_token"));


            //get answer body

            String ver_res;
            StringBuilder ver_sb = new StringBuilder();
            InputStream ver_is;


            ver_is = new BufferedInputStream(verify_con.getInputStream());
            BufferedReader ver_br = new BufferedReader(new InputStreamReader(ver_is));
            String ver_inputLine;
            while ((ver_inputLine = ver_br.readLine()) != null) {
                ver_sb.append(ver_inputLine);
            }
            ver_res = ver_sb.toString();

            JSONObject verify_json= new JSONObject(ver_res);
            json_result.put("CharacterID",verify_json.getInt("CharacterID"));
            json_result.put("CharacterName",verify_json.getString("CharacterName"));

            return json_result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    protected void onPostExecute(JSONObject response) {
        if(response==null){
            return;
        }

        String access_token;
        //String token_type;
        int expires_in;
        String refresh_token;
        int char_id;
        String char_name;
        long valid_until;

        try {
            access_token=response.getString("access_token");
            //token_type=response.getString("token_type");
            expires_in=response.getInt("expires_in");
            refresh_token=response.getString("refresh_token");
            char_id=response.getInt("CharacterID");
            char_name=response.getString("CharacterName");


            valid_until=System.currentTimeMillis()+(expires_in-10)*1000;

            //Log.i("eve_answer_json",response.toString(4));


            if(MainActivity.myDB.insert_char(access_token,refresh_token,valid_until,char_id,char_name)){
                //Log.i("eve_chars", "SUCCESS: Added char "+char_name+"("+char_id+") to database!");
                Toast.makeText(this.ct, "SUCCESS: Added char "+char_name+"("+char_id+") to database!", Toast.LENGTH_LONG).show();
            } else {
                //Log.w("eve_chars", "ERROR: Could NOT add "+char_name+"("+char_id+") to database!");
                Toast.makeText(this.ct, "ERROR: Could NOT add "+char_name+"("+char_id+") to database!", Toast.LENGTH_LONG).show();
            }





        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}