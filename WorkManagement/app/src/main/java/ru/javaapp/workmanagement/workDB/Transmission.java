package ru.javaapp.workmanagement.workDB;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
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
import ru.javaapp.workmanagement.Helper;
import ru.javaapp.workmanagement.R;
import ru.javaapp.workmanagement.interfaces.ITransmission;

/**
 * Created by User on 26.10.2015.
 */
public class Transmission implements ITransmission {
    private final String urlTaskStatus = "http://autocomponent.motorcum.ru/update_statusId_by_task.php";
    private final String urlCountAndStatus = "http://autocomponent.motorcum.ru/update_count_and_status.php";
    private final String urlCurrentCount = "http://autocomponent.motorcum.ru/update_currentCount_by_task.php";
    private final String urlTasksForWorker = "http://autocomponent.motorcum.ru/get_tasks_by_worker.php";
    private final String urlTasksForMaster = "http://autocomponent.motorcum.ru/get_tasks_for_master.php";
    private final String urlAuthWorker = "http://autocomponent.motorcum.ru/select_worker_auth.php";
    private final String urlAuthMaster = "http://autocomponent.motorcum.ru/select_master_auth.php";
    private final String urlCreateTask = "http://autocomponent.motorcum.ru/insert_task.php";
    private final String urlDefect = "http://autocomponent.motorcum.ru/update_defect.php";
    private final String urlStop = "http://autocomponent.motorcum.ru/update_downtime.php";
    String login, password;
    HttpURLConnection urlConnection;
    StringBuilder result = new StringBuilder();
    URL urlRequest;
    JSONObject jsonObject;
    Context context;
    int code;
    ArrayList<NameValuePair> pairs;

    // Запрос на добавления задания
    @Override
    public void CreateTask(int masterId, int workerId, int whatId, int whereId, String countPlan,
                           String commentEdit, String timeBefore,
                           String timeAfter, String dateBefore, String dateAfter, Context context){
        this.context = context;
        pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("masterId", Integer.toString(masterId)));
        pairs.add(new BasicNameValuePair("workerId", Integer.toString(workerId)));
        pairs.add(new BasicNameValuePair("whatId", Integer.toString(whatId)));
        pairs.add(new BasicNameValuePair("placeId", Integer.toString(whereId)));
        pairs.add(new BasicNameValuePair("countPlan", countPlan));
        pairs.add(new BasicNameValuePair("timeStart", timeBefore));
        pairs.add(new BasicNameValuePair("timeFinish", timeAfter));
        pairs.add(new BasicNameValuePair("dateStart", dateBefore));
        pairs.add(new BasicNameValuePair("dateFinish", dateAfter));
        pairs.add(new BasicNameValuePair("comment", commentEdit));
        new JSONSAsyncTask().execute(urlCreateTask);
    }


    // Запрос на обновление статуса (прочитано, выполнено)
    @Override
    public void UpdateTaskStatus(int taskId, int statusId, Context context){
        this.context = context;
        pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("id", Integer.toString(taskId)));
        pairs.add(new BasicNameValuePair("statusId", Integer.toString(statusId)));
        new JSONSAsyncTask().execute(urlTaskStatus);
    }

    // Запрос на обновление статуса текущего ко-ва и статуса при завершении задания
    @Override
    public void UpdateCountAndStatus(int taskId, int currentCount, int statusId, Context context){
        this.context = context;
        pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("id", Integer.toString(taskId)));
        pairs.add(new BasicNameValuePair("currentCount", Integer.toString(currentCount)));
        pairs.add(new BasicNameValuePair("statusId", Integer.toString(statusId)));
        new JSONSAsyncTask().execute(urlCountAndStatus);
    }

    // Запрос на обновление текущего кол-ва при переходе назад
    @Override
    public void UpdateCurrentCount(int taskId, int currentCount, Context context){
        this.context = context;
        pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("id", Integer.toString(taskId)));
        pairs.add(new BasicNameValuePair("currentCount", Integer.toString(currentCount)));
        new JSONSAsyncTask().execute(urlCurrentCount);
    }

    @Override
    public void UpdateDefect(int taskId, int defectId, int defectCount, Context context) {
        this.context = context;
        pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("id", Integer.toString(taskId)));
        pairs.add(new BasicNameValuePair("defectId", Integer.toString(defectId)));
        pairs.add(new BasicNameValuePair("defectCount", Integer.toString(defectCount)));
        new JSONSAsyncTask().execute(urlDefect);
    }

    @Override
    public void UpdateDownTime(int taskId, int stopId, Context context) {
        this.context = context;
        pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("id", Integer.toString(taskId)));
        pairs.add(new BasicNameValuePair("stopId", Integer.toString(stopId)));
        new JSONSAsyncTask().execute(urlStop);
    }


    // Запрос на авторизацию
    @Override
    public String DoAuthorize(String login, String password, String role){
        this.login = login;
        this.password = password;
        if(role.equals("Работник")) {
            return makeHttpRequestForAuth(urlAuthWorker);
        }
        else{
            return makeHttpRequestForAuth(urlAuthMaster);
        }
    }

    // Запрос на получение задач в системе Работник
    public JSONObject getTasksForWorker(String sessionKey, Context context){
        this.context = context;
        pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("sessionKey", sessionKey));
        return makeHttpRequestForWorker(urlTasksForWorker);
    }

    // Запрос на получение задач в системе Руководитель
    public JSONObject getTasksForMaster(){
        return makeHttpRequestForMaster(urlTasksForMaster);
    }

    // Выполнение запроса авторизации
    private String makeHttpRequestForAuth(String url) {

        String result = null;
        InputStream is = null;

        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("login", login));
        pairs.add(new BasicNameValuePair("password", password));

        try {
            urlRequest = new URL(url);
            urlConnection = (HttpURLConnection) urlRequest.openConnection();
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

    // Выполнение щапроса списка задач на странице мастера
    private JSONObject makeHttpRequestForMaster(String url){
        String result = null;
        InputStream is = null;
        try {
            urlRequest = new URL(url);
            urlConnection = (HttpURLConnection) urlRequest.openConnection(); // open connection
            urlConnection.setRequestMethod("GET"); // set GET request? because we are send parameters
            urlConnection.setDoInput(true); // use Get request
            urlConnection.connect(); // connect with our server

            is = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            result = sb.toString();
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

    // Отправка запросов и получение задач для Работника
    private JSONObject makeHttpRequestForWorker(String url) {

        String result = null;
        InputStream is = null;
        try {
            urlRequest = new URL(url);
            urlConnection = (HttpURLConnection) urlRequest.openConnection(); // open connection
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

    // Класс выполнения запросов в фоне
    private class JSONSAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {
            String result = null;
            InputStream is = null;

            if(Helper.isConnected(context)) {
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
            else{
                result = null;
                return result;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject json_data = new JSONObject(result);
                code = json_data.getInt("code");
            } catch (JSONException e) {
                Toast.makeText(context, R.string.error_sending_data_to_Db,
                        Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            if (code == 1) {
                Toast.makeText(context, "Данные отправлены на сервер.",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Что то пошло не так.",
                        Toast.LENGTH_LONG).show();
                return;
            }

        }
    }
}

