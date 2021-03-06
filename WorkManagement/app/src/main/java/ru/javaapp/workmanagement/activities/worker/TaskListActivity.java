package ru.javaapp.workmanagement.activities.worker;

import android.app.ProgressDialog;
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

import ru.javaapp.workmanagement.Helper;
import ru.javaapp.workmanagement.R;
import ru.javaapp.workmanagement.activities.auth.LoginActivity;
import ru.javaapp.workmanagement.adapters.RVAdaptersTasks;
import ru.javaapp.workmanagement.dao.Task;
import ru.javaapp.workmanagement.list.DividerItemDecoration;
import ru.javaapp.workmanagement.list.RecyclerItemClickListener;
import ru.javaapp.workmanagement.workDB.Transmission;

public class TaskListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabHost tabHost;
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
        setListeners(); // rv.items select listener
        try {
            new JsonReadByWorker().execute(); // start AsyncTask and get JSON from DB
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            finish();
        } catch (Exception e) {}
    }

    /**
     * We get the Tasks by Worker.
     * Start AsyncTask to DB for getting JSON
     */
    public class JsonReadByWorker extends AsyncTask<String, String, JSONObject> {

        JSONObject object;

        ProgressDialog dialog = new ProgressDialog(TaskListActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle("Обработка данных");
            dialog.setMessage("Пожалуйста, подождите...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Отмена", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    cancel(true);
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            if(Helper.isConnected(getApplicationContext())) {
                Transmission responce = new Transmission();
                object = responce.getTasksForWorker(LoginActivity.sessionKey, getApplicationContext());
                return object;
            }
            else
            {
                return null;
            }
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
                    builder.setMessage(R.string.error_connection_wifi);
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // Кнопка ОК
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss(); // Отпускает диалоговое окно
                        }
                    });
                    builder.show();
                    dialog.dismiss();
                }
            } catch (JSONException e) {
                Toast.makeText(TaskListActivity.this, "Ошибка", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            dialog.dismiss();
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
            int statusId = jsonObject.getInt("id_status");
            statusList.add(statusId);
            task.setIdTask(jsonObject.getInt("id"));
            task.setIdStatus(statusId);
            task.setMasterName(jsonObject.getString("name"));
            task.setWhatName(jsonObject.getString("nameWhat"));
            task.setPlaceName(jsonObject.getString("namePlace"));
            task.setCountPlanTask(jsonObject.getInt("count_plan"));
            task.setCountCurrentTask(jsonObject.getInt("count_current"));
            task.setTimeStart(jsonObject.getString("time_start").substring(0, 5));
            task.setTimeFinish(jsonObject.getString("time_finish").substring(0, 5));
            task.setDateStart(Helper.parseDate(jsonObject.getString("date_start")));
            task.setDateFinish(Helper.parseDate(jsonObject.getString("date_finish")));
            task.setCommentTask(jsonObject.getString("comment"));

            // Create Task and add in list
            if (statusId == 1 || statusId == 2) {
                taskOneTwo.add(task);
            }
            if (statusId == 3) {
                taskThree.add(task);
            }

        }

        // Creating 2 adapters and 2 recyclerviews for two Tabs
        if (!taskOneTwo.isEmpty()) {
            adaptersTasksOneTwo = new RVAdaptersTasks(getApplicationContext(), taskOneTwo, statusList);
            LinearLayoutManager llm1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            rvTasksOne.setAdapter(adaptersTasksOneTwo);
            rvTasksOne.setLayoutManager(llm1);
            rvTasksOne.setVisibility(View.VISIBLE);
        }
        if (!taskThree.isEmpty()) {
            adaptersTasksThree = new RVAdaptersTasks(getApplicationContext(), taskThree, statusList);
            LinearLayoutManager llm2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            rvTasksTwo.setAdapter(adaptersTasksThree);
            rvTasksTwo.setLayoutManager(llm2);
            rvTasksTwo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.sync) {
            new JsonReadByWorker().execute(); // update RVs - get new data
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Action by clicking on items RV
     */
    private void setListeners() {
        rvTasksOne.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Task task = taskOneTwo.get(position);
                        int statusId = taskOneTwo.get(position).getIdStatus();
                        if(statusId == 1) {
                            Intent intentTba = new Intent(TaskListActivity.this, TaskBeginActivity.class);
                            intentTba.putExtra("taskObj", task);
                            startActivity(intentTba);
                        }
                        if(statusId == 2){
                            Intent intentTba = new Intent(TaskListActivity.this, TaskRunActivity.class);
                            intentTba.putExtra("taskObj", task);
                            startActivity(intentTba);
                            finish();
                        }
                    }
                })
        );
    }
}
