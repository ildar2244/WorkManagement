package ru.javaapp.workmanagement.master;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.UUID;

import ru.javaapp.workmanagement.MainActivity;
import ru.javaapp.workmanagement.R;
import ru.javaapp.workmanagement.fragments.DatePickerFragmentAfter;
import ru.javaapp.workmanagement.fragments.DatePickerFragmentBefore;
import ru.javaapp.workmanagement.fragments.TimePickerFragmentAfter;
import ru.javaapp.workmanagement.fragments.TimePickerFragmentBefore;

public class CreateTaskActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText countPlan;
    Button send_btn;
    TextView tvDateBefore, tvDateAfter, tvTimeBefore, tvTimeAfter;
    Spinner whomSpinner, whatSpinner, whereSpinner;
    ArrayAdapter whomAdapter, whatAdapter, whereAdapter;
    int code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        toolbarInitialize();
        componentsInitialize();
        setListeners();
    }

    private void componentsInitialize(){
        whomSpinner = (Spinner) findViewById(R.id.whomSpinner);
        whatSpinner = (Spinner) findViewById(R.id.whatSpinner);
        whereSpinner = (Spinner) findViewById(R.id.whereSpinner);
        countPlan = (EditText) findViewById(R.id.howManyEt);

        whomSpinner.setAdapter(fillWhomSpinner());
        whomSpinner.setFocusable(true);
        whatSpinner.setAdapter(fillWhatSpinner());
        whatSpinner.setFocusable(true);
        whereSpinner.setAdapter(fillWhereSpinner());
        whereSpinner.setFocusable(true);

        tvDateBefore = (TextView) findViewById(R.id.tv_input_dateBefore);
        tvTimeBefore = (TextView) findViewById(R.id.tv_input_timeBefore);
        tvDateAfter = (TextView) findViewById(R.id.tv_input_dateAfter);
        tvTimeAfter = (TextView) findViewById(R.id.tv_input_timeAfter);

        send_btn = (Button) findViewById(R.id.btn_send);
    }

    private ArrayAdapter fillWhomSpinner(){
        String[] whom = getResources().getStringArray(R.array.workers);
        whomAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, whom);
        whomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return whomAdapter;
    }
    private ArrayAdapter fillWhatSpinner(){
        String[] what = getResources().getStringArray(R.array.whatDo);
        whatAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, what);
        whatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return whatAdapter;
    }
    private ArrayAdapter fillWhereSpinner(){
        String[] where = getResources().getStringArray(R.array.whereDo);
        whereAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, where);
        whereAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return whereAdapter;
    }

    private void toolbarInitialize() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void setListeners(){
        tvDateBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragmentBefore();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
        tvTimeBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new TimePickerFragmentBefore();
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });
        tvDateAfter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragmentAfter();
                newFragment.show(getSupportFragmentManager(), "dateAfterPicker");
            }
        });
        tvTimeAfter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new TimePickerFragmentAfter();
                newFragment.show(getSupportFragmentManager(), "timeAfterPicker");
            }
        });
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkFillAllFields()) {
                    InsertData task1 = new InsertData();
                    task1.execute(new String[]{"http://autocomponent.motorcum.ru/insert_task.php"});
                }
                else{
                    return;
                }
            }
        });
    }

    boolean checkFillAllFields(){
        if(
                countPlan.getText().toString().trim().length() == 0 ||
                        tvDateAfter.getText().toString().trim().length() == 0 ||
                        tvDateBefore.getText().toString().trim().length() == 0 ||
                        tvTimeAfter.getText().toString().trim().length() == 0 ||
                        tvTimeBefore.getText().toString().trim().length() == 0 )
        {
            return false;
        }
        else{
            return true;
        }
    }

    private class InsertData extends AsyncTask<String, Void, String> {
        int workerId = whomSpinner.getSelectedItemPosition() + 1;
        int whatId = whatSpinner.getSelectedItemPosition() + 1;
        int whereId = whereSpinner.getSelectedItemPosition() + 1;
        String plan = countPlan.getText().toString();
        String timeBefore = tvTimeBefore.getText().toString();
        String timeAfter = tvTimeAfter.getText().toString();
        String dateBefore = tvDateBefore.getText().toString();
        String dateAfter = tvDateAfter.getText().toString();

        @Override
        protected String doInBackground(String... urls) {
            String result = null;
            InputStream is = null;
            final int CONN_WAIT_TIME = 8000;
            final int CONN_DATA_WAIT_TIME = 7000;

            for (String url : urls) {
                try {
                    ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
                    pairs.add(new BasicNameValuePair("masterId", "1"));
                    pairs.add(new BasicNameValuePair("workerId", Integer.toString(workerId)));
                    pairs.add(new BasicNameValuePair("whatId", Integer.toString(whatId)));
                    pairs.add(new BasicNameValuePair("placeId", Integer.toString(whereId)));
                    pairs.add(new BasicNameValuePair("countPlan", plan));
                    pairs.add(new BasicNameValuePair("timeStart", timeBefore));
                    pairs.add(new BasicNameValuePair("timeFinish", timeAfter));
                    pairs.add(new BasicNameValuePair("dateStart", dateBefore));
                    pairs.add(new BasicNameValuePair("dateFinish", dateAfter));
                    pairs.add(new BasicNameValuePair("comment", "Никаких комментариев"));

                    HttpParams httpParams = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(httpParams, CONN_WAIT_TIME);
                    HttpConnectionParams.setSoTimeout(httpParams, CONN_DATA_WAIT_TIME);

                    DefaultHttpClient postClient = new DefaultHttpClient(httpParams);
                    HttpClient client = postClient;
                    HttpPost post = new HttpPost(url);
                    post.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
                    HttpResponse responce = client.execute(post);
                    HttpEntity entity = responce.getEntity();
                    is = entity.getContent();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result == null){
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateTaskActivity.this,  R.style.AlertDialogStyle);
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
                    Toast.makeText(getBaseContext(), R.string.task_sending_succesful,
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    Toast.makeText(getBaseContext(), R.string.task_sending_wrong,
                            Toast.LENGTH_LONG).show();
                    return;
                }
            }

        }
    }

    public void onBackPressed() {
        Log.d("My", "OnBackPressed");
        try {
            startActivity(new Intent(CreateTaskActivity.this, MainActivity.class));
            finish();
        }
        catch (Exception e){
        }
    }

}
