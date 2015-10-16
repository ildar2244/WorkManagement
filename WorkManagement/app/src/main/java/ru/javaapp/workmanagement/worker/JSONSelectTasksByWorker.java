package ru.javaapp.workmanagement.worker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by User on 13.10.2015.
 */
public class JSONSelectTasksByWorker {

    HttpURLConnection urlConnection;
    StringBuilder result = new StringBuilder();
    URL urlGet;
    private JSONObject jsonObject;

    public JSONObject makeHttpRequest(String url) {

        try {
            urlGet = new URL(url);
            urlConnection = (HttpURLConnection) urlGet.openConnection();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            jsonObject = new JSONObject(result.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }

        return jsonObject;
    }
}
