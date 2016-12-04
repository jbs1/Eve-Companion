package com.jbs.evecompanion;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
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



class GetRequest extends AsyncTask<Integer, Void, JSONObject> {
    private Context ct;
    private JSONArray url_p;
    private String url_t;

    GetRequest(Context context, String target_url, JSONArray url_params){
        ct = context;
        url_p = url_params;
        url_t = target_url;
    }

    GetRequest(CharView context, String target_url, String url_params) {
        ct = context;
        try {
            url_p = new JSONArray(url_params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        url_t = target_url;
    }

    protected JSONObject doInBackground(Integer... charids) {
        try {
            //String url = "https://login.eveonline.com/oauth/token";
            String charset = "UTF-8";
            String access_t = MainActivity.myDB.access_token(charids[0]);



            URL obj;
            //create url parameters
            if(url_p.length()>0) {
                StringBuilder par_string = new StringBuilder();
                par_string.append("?");
                for (int i = 0; i < url_p.length(); i++) {
                    par_string.append(URLEncoder.encode(url_p.getJSONObject(i).getString("name"), charset));
                    par_string.append("=");
                    par_string.append(URLEncoder.encode(url_p.getJSONObject(i).getString("value"), charset));
                    if (i < url_p.length() - 1) {
                        par_string.append("&");
                    }
                }

                obj = new URL(url_t+par_string.toString());
            } else {
                obj = new URL(url_t);
            }


            //build connection

            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Accept-Charset", charset);
            con.setRequestProperty("Authorization","Bearer " + access_t);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=" + charset);

            //print request headers
            /**for (Map.Entry<String, List<String>> req_prop : con.getRequestProperties().entrySet()) {
                Log.i("eve_request_header",req_prop.getKey() + "=" + req_prop.getValue());
            }**/

            con.connect();

            //write body
            /**con.setDoOutput(true);
            try (OutputStream output = con.getOutputStream()) {
                if (query != null) {
                    output.write(query.getBytes(charset));
                }
            }**/

            //check response headers
            /**for (Map.Entry<String, List<String>> header : con.getHeaderFields().entrySet()) {
                Log.i("eve_response_header",header.getKey() + "=" + header.getValue());
            }**/


            //check response body
            String result;
            StringBuilder sb = new StringBuilder();
            InputStream is;


            is = new BufferedInputStream(con.getInputStream());
            //is = new BufferedInputStream(con.getErrorStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            result = sb.toString();

            //PROBLEM: returned string is not allways an object, but sometimes an array and then the return type is wrong

            //Log.i("eve_response_body",result);
            //JSONObject res = new JSONObject(result);
            String vla = "[\n" +
                    "  {\n" +
                    "    \"level_end_sp\": 90510,\n" +
                    "    \"level_start_sp\": 16000,\n" +
                    "    \"skill_id\": 16069,\n" +
                    "    \"finished_level\": 4,\n" +
                    "    \"training_start_sp\": 17018,\n" +
                    "    \"start_date\": \"2016-12-03T17:47:21Z\",\n" +
                    "    \"finish_date\": \"2016-12-05T10:37:05Z\",\n" +
                    "    \"queue_position\": 0\n" +
                    "  }\n" +
                    "]";
            JSONArray res = new JSONArray(vla);
            Log.i("eve_response_body",res.toString(2));
            return new JSONObject("{}");
            //return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    protected void onPostExecute(JSONObject response) {
        if(response==null){
            return;
        }
    }
}