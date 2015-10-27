package ru.javaapp.workmanagement.activities.master;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import ru.javaapp.workmanagement.list.RecyclerItemClickListener;
import ru.javaapp.workmanagement.workDB.Transmission;
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
        setListeners();
        new JsonReadTasksForMaster().execute(); // start AsyncTask and get JSON from DB
    }

    private void setListeners() {
        rvTasksCurrent.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position){
                Task task = taskListCurrent.get(position);
                Intent intentTaskAbout = new Intent(TrackingActivity.this, TaskAboutForMasterActivity.class);
                intentTaskAbout.putExtra("taskObj", task);
                startActivity(intentTaskAbout);
            }
        }));
        rvTasksFinish.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Task task = taskListFinish.get(position);
                Intent intentTaskAbout = new Intent(TrackingActivity.this, TaskAboutForMasterActivity.class);
                intentTaskAbout.putExtra("taskObj", task);
                startActivity(intentTaskAbout);
            }
        }));
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
            task.setTimeStart(timeStart);
            task.setTimeFinish(timeFinish);
            task.setDateStart(dateStart);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tracking, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.sync) {
            new JsonReadTasksForMaster().execute(); // update RVs - get new data
        }
        return super.onOptionsItemSelected(item);
    }

}
