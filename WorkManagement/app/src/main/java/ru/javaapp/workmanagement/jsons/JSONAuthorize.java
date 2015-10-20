package ru.javaapp.workmanagement.jsons;

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
 * Created by User on 20.10.2015.
 */
public class JSONAuthorize {
    HttpURLConnection urlConnection;
    StringBuilder result = new StringBuilder();
    URL urlGet;
    private JSONObject jsonObject;
    String role;
    String url = "";

    public JSONAuthorize(String role){
        this.role = role;
    }

    public JSONObject makeHttpRequest() {

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
