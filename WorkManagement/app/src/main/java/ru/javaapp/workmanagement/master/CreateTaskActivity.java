package ru.javaapp.workmanagement.master;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.javaapp.workmanagement.R;
import ru.javaapp.workmanagement.fragments.DatePickerFragmentAfter;
import ru.javaapp.workmanagement.fragments.DatePickerFragmentBefore;
import ru.javaapp.workmanagement.fragments.TimePickerFragmentAfter;
import ru.javaapp.workmanagement.fragments.TimePickerFragmentBefore;

public class CreateTaskActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText countPlan, comment;
    Button send_btn;
    TextView tvDateBefore, tvDateAfter, tvTimeBefore, tvTimeAfter;
    Spinner whomSpinner, whatSpinner, whereSpinner;
    ArrayAdapter whomAdapter, whatAdapter, whereAdapter;
    HttpURLConnection urlConnection;
    int code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        toolbarInitialize();
        componentsInitialize();
        setListeners();
    }

    // inin all components
    private void componentsInitialize(){
        whomSpinner = (Spinner) findViewById(R.id.whomSpinner);
        whatSpinner = (Spinner) findViewById(R.id.whatSpinner);
        whereSpinner = (Spinner) findViewById(R.id.whereSpinner);
        countPlan = (EditText) findViewById(R.id.howManyEt);
        comment = (EditText) findViewById(R.id.cta_comment);

        whomSpinner.setAdapter(fillWhomSpinner());
        whomSpinner.setFocusable(true);
        whatSpinner.setAdapter(fillWhatSpinner());
        whatSpinner.setFocusable(true);
        whereSpinner.setAdapter(fillWhereSpinner());
        whereSpinner.setFocusable(true);

        tvDateBefore = (TextView) findViewById(R.id.tv_input_dateBefore);
        tvDateBefore.setText(getCurrentDate());
        tvTimeBefore = (TextView) findViewById(R.id.tv_input_timeBefore);
        tvTimeBefore.setText(getCurrentTime());
        tvDateAfter = (TextView) findViewById(R.id.tv_input_dateAfter);
        tvDateAfter.setText(getCurrentDate());
        tvTimeAfter = (TextView) findViewById(R.id.tv_input_timeAfter);
        tvTimeAfter.setText(getCurrentTime());

        send_btn = (Button) findViewById(R.id.btn_send);
    }
    // get current date for dates textview
    private  String getCurrentDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }
    // get current time for times textview
    private String getCurrentTime(){
        Calendar c = Calendar.getInstance();
        StringBuilder timeBuilder = new StringBuilder();
        timeBuilder.append(c.get(Calendar.HOUR_OF_DAY)).append(":");
        timeBuilder.append(c.get(Calendar.MINUTE));
        String formattedTime = timeBuilder.toString();
        return formattedTime;
    }

    // fill adapters for our spinners
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

    //init toolbar
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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    // all listeners
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

    // check for filled all fields
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

    // send task on server
    private class InsertData extends AsyncTask<String, Void, String> {
        int workerId = whomSpinner.getSelectedItemPosition() + 1;
        int whatId = whatSpinner.getSelectedItemPosition() + 1;
        int whereId = whereSpinner.getSelectedItemPosition() + 1;
        String plan = countPlan.getText().toString();
        String commentEdit = comment.getText().toString();
        String timeBefore = tvTimeBefore.getText().toString();
        String timeAfter = tvTimeAfter.getText().toString();
        String dateBefore = tvDateBefore.getText().toString();
        String dateAfter = tvDateAfter.getText().toString();

        @Override
        protected String doInBackground(String... urls) {
            String result = null;
            InputStream is = null;

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
            pairs.add(new BasicNameValuePair("comment", commentEdit));

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

    public void onBackPressed() {
        Log.d("My", "OnBackPressed");
        try {
            finish();
        }
        catch (Exception e){
        }
    }

}
