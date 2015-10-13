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
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.javaapp.workmanagement.MainActivity;
import ru.javaapp.workmanagement.R;
import ru.javaapp.workmanagement.worker.JSONParserWorker;

public class TaskListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabHost tabHost;
    private String urlGetTasks = "http://autocomponent.motorcum.ru/get_tasks_by_worker.php";

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        toolbarInitialize(); // init toolbar
        componentsInitialize(); //init components in activity
        new JsonReadByWorker().execute();
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
        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tapSpec = tabHost.newTabSpec("tabOne");
        tapSpec.setContent(R.id.tab1);
        tapSpec.setIndicator("ONE");
        tabHost.addTab(tapSpec);

        tapSpec = tabHost.newTabSpec("tabTwo");
        tapSpec.setContent(R.id.tab2);
        tapSpec.setIndicator("TWO");
        tabHost.addTab(tapSpec);

        tapSpec = tabHost.newTabSpec("tabThree");
        tapSpec.setContent(R.id.tab3);
        tapSpec.setIndicator("THREE");
        tabHost.addTab(tapSpec);

        textView = (TextView) findViewById(R.id.tv_json);
    }

    /**
     * Action for the "Back"
     */
    @Override
    public void onBackPressed() {
        Log.d("My", "On Back Pressed");
        super.onBackPressed();
        try {
            startActivity(new Intent(TaskListActivity.this, MainActivity.class));
            finish();
        } catch (Exception e) {}
    }

    /**
     * We get the Tasks by Worker.
     * Start AsyncTask to DB for getting JSON
     */
    public class JsonReadByWorker extends AsyncTask<String, String, JSONObject> {

        JSONObject object;

        @Override
        protected JSONObject doInBackground(String... params) {

            try {
                JSONParserWorker parserWorker = new JSONParserWorker();
                object = parserWorker.makeHttpRequest(urlGetTasks);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return object;
        }

        protected void onPostExecute(JSONObject json) {
            try {
                if(json != null) {
                    ListDrawer(json);
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(TaskListActivity.this,  R.style.AlertDialogStyle);
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
            } catch (JSONException e) {
                Toast.makeText(TaskListActivity.this, "Ошибка", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    public void ListDrawer(JSONObject json) throws JSONException {
        JSONArray jsonArray = null;
        String resultText = "";

        jsonArray = json.getJSONArray("allTasks_by_worker");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String nameMaster = jsonObject.getString("name");
            String nameWorker = jsonObject.getString("nameWorker");
            String nameWhat = jsonObject.getString("nameWhat");
            String namePlace = jsonObject.getString("namePlace");
            String countPlan = jsonObject.getString("count_plan");
            String countCurrent = jsonObject.getString("count_current");
            String timeStart = jsonObject.getString("time_start");
            String timeFinish = jsonObject.getString("time_finish");
            String dateStart = jsonObject.getString("date_start");
            String dateFinish = jsonObject.getString("date_finish");
            String comment = jsonObject.getString("comment");
            resultText += nameMaster + " " + nameWorker + " " + nameWhat + " " + namePlace + " " + countPlan + " " + countCurrent + " " + timeStart + " " + timeFinish + " " + dateStart + " " + dateFinish + " " + comment;

        }
        textView.setText(resultText);
    }
}
