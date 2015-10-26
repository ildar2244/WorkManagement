package ru.javaapp.workmanagement.master;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import ru.javaapp.workmanagement.R;
import ru.javaapp.workmanagement.adapters.RVAdaptersTasksForMaster;
import ru.javaapp.workmanagement.dao.Task;
import ru.javaapp.workmanagement.jsons.Transmission;
import ru.javaapp.workmanagement.list.DividerItemDecoration;

public class TrackingActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabHost tabHost;
    private String urlGetTasks = "http://autocomponent.motorcum.ru/get_tasks_for_master.php";
    private RecyclerView rvTasksCurrent;
    private RecyclerView rvTasksFinish;
    private RVAdaptersTasksForMaster adaptersTasksCurrent;
    private RVAdaptersTasksForMaster adaptersTasksFinish;
    private List<Task> taskListCurrent;
    private List<Task> taskListFinish;
    List statusList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        toolbarInitialize(); // init toolbar
        componentsInitialize(); //init components in activity
        new JsonReadTasksForMaster().execute(); // start AsyncTask and get JSON from DB
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
        TabHost.TabSpec tapSpec = tabHost.newTabSpec("Текущие");
        tapSpec.setContent(R.id.tabCurrent);
        tapSpec.setIndicator("Текущие");
        tabHost.addTab(tapSpec);

        tapSpec = tabHost.newTabSpec("Выполнено");
        tapSpec.setContent(R.id.tabFinish);
        tapSpec.setIndicator("Выполнено");
        tabHost.addTab(tapSpec);

        rvTasksCurrent = (RecyclerView) findViewById(R.id.rv_tasks_current);
        rvTasksCurrent.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        rvTasksFinish = (RecyclerView) findViewById(R.id.rv_tasks_finish);
        rvTasksFinish.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

    }

    /**
     * Action for the "Back"
     */
    @Override
    public void onBackPressed() {
        Log.d("My", "On Back Pressed");
        super.onBackPressed();
        try {
            finish();
        } catch (Exception e) {}
    }

    /**
     * We get the Tasks by Worker.
     * Start AsyncTask to DB for getting JSON
     */
    public class JsonReadTasksForMaster extends AsyncTask<String, String, JSONObject> {

        JSONObject object;

        @Override
        protected JSONObject doInBackground(String... params) {

            try {
                Transmission responce = new Transmission();
                object = responce.getTasksForMaster();
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(TrackingActivity.this,  R.style.AlertDialogStyle);
                    builder.setCancelable(false);
                    builder.setTitle("Ошибка");
                    builder.setMessage("Нет текущих заданий.");
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // Кнопка ОК
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss(); // Отпускает диалоговое окно
                        }
                    });
                    builder.show();
                }
            } catch (JSONException e) {
                Toast.makeText(TrackingActivity.this, "Ошибка", Toast.LENGTH_SHORT).show();
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
        taskListCurrent = new ArrayList<Task>();
        taskListFinish = new ArrayList<Task>();
        statusList = new ArrayList();

        jsonArray = json.getJSONArray("allTasks_for_master");

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
            task.setIdPerformer(nameWorker);
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
                taskListCurrent.add(task);
            }
            if (statusId == 3) {
                taskListFinish.add(task);
            }

        }

        // Creating 2 adapters and 2 recyclerviews for two Tabs
        adaptersTasksCurrent = new RVAdaptersTasksForMaster(getApplicationContext(), taskListCurrent, statusList);
        adaptersTasksFinish = new RVAdaptersTasksForMaster(getApplicationContext(), taskListFinish, statusList);
        LinearLayoutManager llm1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager llm2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvTasksCurrent.setAdapter(adaptersTasksCurrent);
        rvTasksCurrent.setLayoutManager(llm1);
        rvTasksFinish.setAdapter(adaptersTasksFinish);
        rvTasksFinish.setLayoutManager(llm2);

    }

}
