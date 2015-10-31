package ru.javaapp.workmanagement.activities.master;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.javaapp.workmanagement.Helper;
import ru.javaapp.workmanagement.R;
import ru.javaapp.workmanagement.adapters.RVStopInfoMaster;
import ru.javaapp.workmanagement.dao.Downtime;
import ru.javaapp.workmanagement.list.DividerItemDecoration;
import ru.javaapp.workmanagement.workDB.Transmission;

/**
 * Created by User on 30.10.2015.
 */
public class StopInfoActivity extends AppCompatActivity {

    private RecyclerView rvStopInfo;
    Toolbar toolbar;
    private RVStopInfoMaster stopInfoAdapter;
    List<Downtime> stopList;
    int taskId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_info_for_master);
        toolbarInitialize(); // init toolbar
        taskId = getIntent().getIntExtra("taskId", 0);
        componentsInitialize(); //init components in activity
        new JsonReadByWorker().execute();
    }

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

    private void componentsInitialize() {
        rvStopInfo = (RecyclerView) findViewById(R.id.rv_stopInfo);
        rvStopInfo.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    public class JsonReadByWorker extends AsyncTask<String, String, JSONObject> {

        JSONObject object;
        ProgressDialog dialog = new ProgressDialog(StopInfoActivity.this);

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
                object = responce.getStopForMaster(taskId);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(StopInfoActivity.this,  R.style.AlertDialogStyle);
                    builder.setCancelable(false);
                    builder.setTitle(getString(R.string.without_stop));
                    builder.setMessage(getString(R.string.no_stop_on_manufactering));
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // Кнопка ОК
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss(); // Отпускает диалоговое окно
                            finish();
                        }
                    });
                    builder.show();
                    dialog.dismiss();
                }
            } catch (JSONException e) {
                Toast.makeText(StopInfoActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
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
        jsonArray = json.getJSONArray("allStop");
        stopList = new ArrayList<Downtime>();

        for (int i = 0; i < jsonArray.length(); i++) {
            Downtime stop = new Downtime();

            JSONObject jsonObject = jsonArray.getJSONObject(i);
            stop.setNameDowntime(jsonObject.getString("downtime_name"));
            String date = jsonObject.getString("downtime_date");
            String time = jsonObject.getString("downtime_time").substring(0, 5);
            stop.setDate(Helper.parseDate(date));
            stop.setTime(time);

            stopList.add(stop);
        }

        stopInfoAdapter = new RVStopInfoMaster(getApplicationContext(), stopList);
        LinearLayoutManager llm1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvStopInfo.setAdapter(stopInfoAdapter);
        rvStopInfo.setLayoutManager(llm1);

    }

}
