package ru.javaapp.workmanagement.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.javaapp.workmanagement.MainActivity;
import ru.javaapp.workmanagement.R;
import ru.javaapp.workmanagement.adapters.RVAdaptersTasks;
import ru.javaapp.workmanagement.dao.Task;
import ru.javaapp.workmanagement.list.DividerItemDecoration;
import ru.javaapp.workmanagement.worker.JSONParserWorker;

public class TaskListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabHost tabHost;
    private String urlGetTasks = "http://autocomponent.motorcum.ru/get_tasks_by_worker.php";
    private RecyclerView rvTasksOne;
    private RecyclerView rvTasksTwo;
    private RVAdaptersTasks adaptersTasksOneTwo;
    private RVAdaptersTasks adaptersTasksThree;
    private List<Task> taskOneTwo;
    private List<Task> taskThree;
    List statusList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        toolbarInitialize(); // init toolbar
        componentsInitialize(); //init components in activity
        new JsonReadByWorker().execute(); // start AsyncTask and get JSON from DB
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
        tapSpec.setIndicator("Текущие");
        tabHost.addTab(tapSpec);

        tapSpec = tabHost.newTabSpec("tabTwo");
        tapSpec.setContent(R.id.tab2);
        tapSpec.setIndicator("Выполнено");
        tabHost.addTab(tapSpec);

        rvTasksOne = (RecyclerView) findViewById(R.id.rv_tasks_one);
        rvTasksOne.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        rvTasksTwo = (RecyclerView) findViewById(R.id.rv_tasks_two);
        rvTasksTwo.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

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

    /**
     * Tasks elements getting from JSONObject to String and create RecyclerView
     * @param json
     * @throws JSONException
     */
    public void ListDrawer(JSONObject json) throws JSONException {
        JSONArray jsonArray = null;
        taskOneTwo = new ArrayList<Task>();
        taskThree = new ArrayList<Task>();
        statusList = new ArrayList();

        jsonArray = json.getJSONArray("allTasks_by_worker");

        for (int i = 0; i < jsonArray.length(); i++) {
            Task task = new Task();

            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int id = jsonObject.getInt("id");
            String nameMaster = jsonObject.getString("name");
            String nameWorker = jsonObject.getString("nameWorker");
            String nameWhat = jsonObject.getString("nameWhat");
            String namePlace = jsonObject.getString("namePlace");
            int statusId = jsonObject.getInt("id_status");
            statusList.add(statusId);
            int countPlan = jsonObject.getInt("count_plan");
            int countCurrent = jsonObject.getInt("count_current");
            String timeStart = jsonObject.getString("time_start");
            String timeFinish = jsonObject.getString("time_finish");
            String dateStart = jsonObject.getString("date_start");
            String dateFinish = jsonObject.getString("date_finish");
            String comment = jsonObject.getString("comment");

            task.setIdTask(id);
            task.setIdStatus(statusId);
            task.setMasterName(nameMaster);
            task.setWhatName(nameWhat);
            task.setPlaceName(namePlace);
            task.setCountPlanTask(countPlan);
            task.setCountCurrentTask(countCurrent);
            task.setTimeFinish(timeFinish);
            task.setDateFinish(dateFinish);
            task.setCommentTask(comment);

            // Create Task and add in list
            if (statusId == 1 || statusId == 2) {
                taskOneTwo.add(task);
            }
            if (statusId == 3) {
                taskThree.add(task);
            }

        }

        // Creating 2 adapters and 2 recyclerviews for two Tabs
        adaptersTasksOneTwo = new RVAdaptersTasks(getApplicationContext(), taskOneTwo, statusList);
        adaptersTasksThree = new RVAdaptersTasks(getApplicationContext(), taskThree, statusList);
        LinearLayoutManager llm1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager llm2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvTasksOne.setAdapter(adaptersTasksOneTwo);
        rvTasksOne.setLayoutManager(llm1);
        rvTasksTwo.setAdapter(adaptersTasksThree);
        rvTasksTwo.setLayoutManager(llm2);

    }
}
