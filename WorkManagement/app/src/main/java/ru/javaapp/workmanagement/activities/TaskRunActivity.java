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
import android.widget.EditText;
import android.widget.ImageButton;
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

/**
 * Created by User on 15.10.2015.
 */
public class TaskRunActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageButton btnMinus, btnPlus;
    Button btnFinish;
    TextView tvCurrentAdd,tvCountToGo, tvTimeToGo, tvWhatDo, tvWhereDo, tvComment;
    EditText etSpeedCount;
    Task taskGet; // Наше задание
    int myTemp; // Текущий темп инкремента
    int currentCount; // Текущее количество штук
    int currentCountToGo; // Сколько осталось выполнить штук
    int code; // Код возврата из сервера
    int getCountToGo; // План на задание
    HttpURLConnection urlConnection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_run);

        toolbarInitialize(); // init toolbar
        takeFieldsFromPreviousActivity(); // get data from Intent
        componentsInitialize(); //init components in activity
        setListeners(); // set all listeners
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

    private void takeFieldsFromPreviousActivity() {
        taskGet = (Task) getIntent().getSerializableExtra("taskObj"); // Получаем наше задание
    }

    private void componentsInitialize() {
        try{
            getSupportActionBar().setTitle("№ " + Integer.toString(taskGet.getIdTask()));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        btnMinus = (ImageButton) findViewById(R.id.btn_minus);
        btnPlus = (ImageButton) findViewById(R.id.btn_plus);
        tvCurrentAdd = (TextView) findViewById(R.id.tv_currentAdd);
        tvCountToGo = (TextView) findViewById(R.id.tv_CountToGo);
        tvTimeToGo = (TextView) findViewById(R.id.tv_TimeToGo);
        tvWhatDo = (TextView) findViewById(R.id.tv_what);
        tvWhereDo = (TextView) findViewById(R.id.tv_where);
        tvComment = (TextView) findViewById(R.id.tv_comment);
        etSpeedCount = (EditText) findViewById(R.id.et_speedCount);

        int getCountCurrentTask = taskGet.getCountCurrentTask(); // Текущее количество штук
        getCountToGo = taskGet.getCountPlanTask(); // Наш план
        int count = getCountToGo - getCountCurrentTask;
        tvCurrentAdd.setText(Integer.toString(getCountCurrentTask));
        tvCountToGo.setText(Integer.toString(count));
        tvWhatDo.setText(taskGet.getWhatName());
        tvWhereDo.setText(taskGet.getPlaceName());
        tvComment.setText(taskGet.getCommentTask());
        currentCount = getCountCurrentTask;
    }

    private boolean checkFillField(){
        if(!etSpeedCount.getText().toString().equals(""))
            return true;
        else
            return false;
    }

    private void setListeners() {
        /*
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        */

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkFillField()){
                    String text = methodMinus();
                    if(text == null){
                        return;
                    }
                    else{
                        tvCurrentAdd.setText(text);
                        tvCountToGo.setText(Integer.toString(currentCountToGo));
                    }

                }
                else{
                    Toast.makeText(TaskRunActivity.this, "Заполните свою скорость", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkFillField()){
                    String text = methodPlus();
                    if(text == null){
                        return;
                    }
                    else{
                        tvCurrentAdd.setText(text);
                        tvCountToGo.setText(Integer.toString(currentCountToGo));
                    }
                }
                else{
                    Toast.makeText(TaskRunActivity.this, "Заполните свою скорость", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private String methodPlus() {
        myTemp = Integer.parseInt(etSpeedCount.getText().toString());
        currentCount = Integer.parseInt(tvCurrentAdd.getText().toString());
        currentCount = currentCount + myTemp;
        if(currentCount > getCountToGo) {
            currentCount = currentCount - myTemp;
            Toast.makeText(TaskRunActivity.this, "Введите другую скорость", Toast.LENGTH_SHORT).show();
            return null;
        }
        else {
            currentCountToGo = Integer.parseInt(tvCountToGo.getText().toString()) - myTemp;
            return Integer.toString(currentCount);
        }
    }

    private String methodMinus() {
        myTemp = Integer.parseInt(etSpeedCount.getText().toString());
        currentCount = Integer.parseInt(tvCurrentAdd.getText().toString());
        currentCount = currentCount - myTemp;
        if(currentCount < 0) {
            currentCount = currentCount + myTemp;
            Toast.makeText(TaskRunActivity.this, "Введите другую скорость", Toast.LENGTH_SHORT).show();
            return null;
        }
        currentCountToGo = Integer.parseInt(tvCountToGo.getText().toString()) + myTemp;
        return Integer.toString(currentCount);

    }

    @Override
    public void onBackPressed() {
        Log.d("My", "On Back Pressed");
        super.onBackPressed();
        try {
            UpdateData task1 = new UpdateData();
            task1.execute(new String[]{"http://autocomponent.motorcum.ru/update_currentCount_by_task.php"});
            startActivity(new Intent(TaskRunActivity.this, TaskListActivity.class));
            finish();
        }
        catch (Exception e){
            e.printStackTrace();
            return;
        }
    }

    // send task on server
    private class UpdateData extends AsyncTask<String, Void, String> {
        int taskId = taskGet.getIdTask();
        int currentcount = currentCount;

        @Override
        protected String doInBackground(String... urls) {
            String result = null;
            InputStream is = null;

            ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("id", Integer.toString(taskId)));
            pairs.add(new BasicNameValuePair("currentCount", Integer.toString(currentcount)));

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
                AlertDialog.Builder builder = new AlertDialog.Builder(TaskRunActivity.this, R.style.AlertDialogStyle);
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
            } else {
                try {
                    JSONObject json_data = new JSONObject(result);
                    code = json_data.getInt("code");
                } catch (JSONException e) {
                    Toast.makeText(getBaseContext(), R.string.error_sending_data_to_Db,
                            Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                if (code == 1) {
                    Toast.makeText(getBaseContext(), "Текущие данные отправлены на сервер.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "Что то пошло не так.",
                            Toast.LENGTH_LONG).show();
                    return;
                }
            }

        }

        private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for (NameValuePair pair : params) {
                if (first)
                    first = false;
                else
                    result.append("&");
                result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
            }
            return result.toString();
        }
    }

}
