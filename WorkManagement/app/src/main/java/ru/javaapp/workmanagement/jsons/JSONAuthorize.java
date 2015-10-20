package ru.javaapp.workmanagement.jsons;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import ru.javaapp.workmanagement.R;
import ru.javaapp.workmanagement.WorkerMainActivity;

/**
 * Created by User on 20.10.2015.
 */
public class JSONAuthorize{
    HttpURLConnection urlConnection;
    StringBuilder result = new StringBuilder();
    String login, password;
    URL urlGet;

    public JSONAuthorize(String login, String password){
        this.login = login;
        this.password = password;
    }

    public String makeHttpRequest(String url) {

        String result = null;
        InputStream is = null;

        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("login", login));
        pairs.add(new BasicNameValuePair("password", password));

        try {
            urlGet = new URL(url);
            urlConnection = (HttpURLConnection) urlGet.openConnection();
            urlConnection.setRequestMethod("POST"); // set POST request? because we are send parameters
            urlConnection.setDoInput(true); // use Get request
            urlConnection.setDoOutput(true); // use POST request
            OutputStream os = urlConnection.getOutputStream(); // get output parameters
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(pairs)); // write our pairs
            writer.flush();
            writer.close();
            os.close();
            urlConnection.connect(); // connect with our server

            is = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return result;
    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(NameValuePair pair : params){
            if(first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }
        return  result.toString();
    }
}
