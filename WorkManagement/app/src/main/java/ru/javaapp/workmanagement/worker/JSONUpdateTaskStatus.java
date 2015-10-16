package ru.javaapp.workmanagement.worker;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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

/**
 * Created by User on 16.10.2015.
 */
public class JSONUpdateTaskStatus extends AsyncTask<String, Void, String> {
    int taskId, statusid;
    Context context;
    HttpURLConnection urlConnection;
    int code;

    public JSONUpdateTaskStatus(int taskId, int statusId, Context context) {
        this.taskId = taskId;
        this.statusid = statusId;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... urls) {
        String result = null;
        InputStream is = null;

        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("id", Integer.toString(taskId)));
        pairs.add(new BasicNameValuePair("statusId", Integer.toString(statusid)));

        for (String url : urls) {
            try {
                URL urli = new URL(url); // our url
                urlConnection = (HttpURLConnection) urli.openConnection(); // open connection
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
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result == null) {
            Toast.makeText(context, "Нет соединения с интернетом", Toast.LENGTH_SHORT).show();
        } else {
            try {
                JSONObject json_data = new JSONObject(result);
                code = json_data.getInt("code");
            } catch (JSONException e) {
                Toast.makeText(context, R.string.error_sending_data_to_Db,
                        Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            if (code == 1) {
                Toast.makeText(context, "С заданием ознакомлен.",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Что то пошло не так.",
                        Toast.LENGTH_LONG).show();
                return;
            }
        }

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
