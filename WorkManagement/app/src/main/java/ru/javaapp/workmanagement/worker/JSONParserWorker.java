package ru.javaapp.workmanagement.worker;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by User on 13.10.2015.
 */
public class JSONParserWorker {

    private String urlGetTasks = "";
    final int CONN_WAIT_TIME = 8000;
    final int CONN_DATA_WAIT_TIME = 7000;
    private String jsonResult;
    private JSONObject jsonObject;

    public JSONObject makeHttpRequest(String url) {

        try {
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, CONN_WAIT_TIME);
            HttpConnectionParams.setSoTimeout(httpParams, CONN_DATA_WAIT_TIME);

            DefaultHttpClient postClient = new DefaultHttpClient(httpParams);

            HttpClient httpclient = postClient;
            HttpPost httppost = new HttpPost(url);
            HttpResponse response = httpclient.execute(httppost);
            jsonResult = inputStreamToString(
                    response.getEntity().getContent()).toString();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            jsonObject = null;
            jsonObject = new JSONObject(jsonResult);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    private StringBuilder inputStreamToString(InputStream is) {
        String rLine = "";
        StringBuilder answer = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        try {
            while ((rLine = rd.readLine()) != null) {
                answer.append(rLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answer;
    }
}
