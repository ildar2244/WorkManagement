package ru.javaapp.workmanagement.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import ru.javaapp.workmanagement.dao.Task;

public class TaskBeginActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tbaTimeStart, tbaTimeFinish, tbaDateStart, tbaDateFinish;
    private TextView tbaWhatName, tbaPlaceName, tbaComment;
    private TextView tbaCountPlan, tbaCountCurrent, tbaDiffCount, tbaDiffTime;
    private Button tbaButtonTake;
    private int diffCount, diffTime, statusId, code;
    private Task taskGet;
    HttpURLConnection urlConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_begin);

        toolbarInitialize(); // init toolbar
        takeFieldsFromPreviousActivity(); // get data from Intent
        componentsInitialize(); //init components in activity
        setListeners(); // button select listener
    }

    /**
     * initialize toolbar
     */
    private void toolbarInitialize() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        // Click Listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * Initialize all components in UI
     */
    private void componentsInitialize() {

        tbaTimeStart = (TextView) findViewById(R.id.tba_timestart);
        tbaTimeFinish = (TextView) findViewById(R.id.tba_timefinish);
        tbaDateStart = (TextView) findViewById(R.id.tba_datestart);
        tbaDateFinish = (TextView) findViewById(R.id.tba_datefinish);
        tbaWhatName = (TextView) findViewById(R.id.tba_whatname);
        tbaPlaceName = (TextView) findViewById(R.id.tba_placename);
        tbaCountPlan = (TextView) findViewById(R.id.tba_countplan);
        tbaCountCurrent = (TextView) findViewById(R.id.tba_countcurrent);
        tbaDiffCount = (TextView) findViewById(R.id.tba_countdifference);
        tbaDiffTime = (TextView) findViewById(R.id.tba_timedifference);
        tbaComment = (TextView) findViewById(R.id.tba_comment);
        tbaButtonTake = (Button) findViewById(R.id.tba_btnbegin);

        tbaTimeStart.setText(taskGet.getTimeStart());
        tbaTimeFinish.setText(taskGet.getTimeFinish());
        tbaDateStart.setText(taskGet.getDateStart());
        tbaDateFinish.setText(taskGet.getDateFinish());
        tbaWhatName.setText(taskGet.getWhatName());
        tbaPlaceName.setText(taskGet.getPlaceName());
        tbaCountPlan.setText(Integer.toString(taskGet.getCountPlanTask()));
        tbaCountCurrent.setText(Integer.toString(taskGet.getCountCurrentTask()));

        diffCount = taskGet.getCountPlanTask() - taskGet.getCountCurrentTask();
        tbaDiffCount.setText(Integer.toString(diffCount));

        tbaComment.setText(taskGet.getCommentTask());
    }

    /**
     * Get data from previous activity
     */
    private void takeFieldsFromPreviousActivity() {
        taskGet = (Task) getIntent().getSerializableExtra("taskObj");
    }

    /**
     * Action for the "Back"
     */
    @Override
    public void onBackPressed() {
        Log.d("My", "On Back Pressed");
        super.onBackPressed();
        /*try {
            startActivity(new Intent(TaskListActivity.this, MainActivity.class));
            finish();
        } catch (Exception e) {}*/
    }

    /**
     * Action by clicking on button
     */
    private void setListeners() {
        tbaButtonTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusId = 2;
                try {
                    UpdateData task1 = new UpdateData();
                    task1.execute(new String[]{"http://autocomponent.motorcum.ru/update_statusId_by_task.php"});
                }
                catch (Exception e){
                    e.printStackTrace();
                    return;
                }
                Intent intentTba = new Intent(TaskBeginActivity.this, TaskRunActivity.class);
                intentTba.putExtra("taskObj", taskGet);
                startActivity(intentTba);
            }
        });
    }

    // send task on server
    private class UpdateData extends AsyncTask<String, Void, String> {
        int taskId = taskGet.getIdTask();
        int statusid = statusId;

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
                    return  null;
                }
                finally {
                    if(urlConnection != null){
                        urlConnection.disconnect();
                    }
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result == null){
                AlertDialog.Builder builder = new AlertDialog.Builder(TaskBeginActivity.this,  R.style.AlertDialogStyle);
                builder.setCancelable(false);
                builder.setTitle("Ошибка");
                builder.setMessage("Нет соединения с интернетом.");
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // Кнопка ОК
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Отпускает диалоговое окно
                    }
                });
                builder.show();
            }
            else{
                try {
                    JSONObject json_data = new JSONObject(result);
                    code=json_data.getInt("code");
                } catch (JSONException e) {
                    Toast.makeText(getBaseContext(), R.string.error_sending_data_to_Db,
                            Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                if(code==1)
                {
                    Toast.makeText(getBaseContext(), "С заданием ознакомлен.",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getBaseContext(), "Что то пошло не так.",
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

}
