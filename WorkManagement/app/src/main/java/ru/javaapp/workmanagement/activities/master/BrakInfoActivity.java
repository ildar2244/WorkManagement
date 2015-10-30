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
import ru.javaapp.workmanagement.adapters.RVBrakInfoMaster;
import ru.javaapp.workmanagement.dao.Defect;
import ru.javaapp.workmanagement.list.DividerItemDecoration;
import ru.javaapp.workmanagement.workDB.Transmission;

/**
 * Created by User on 30.10.2015.
 */
public class BrakInfoActivity extends AppCompatActivity {

    private RecyclerView rvBrakInfo;
    Toolbar toolbar;
    private RVBrakInfoMaster brakInfoAdapter;
    List<Defect> brakList;
    int taskId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brak_info_for_master);
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
        rvBrakInfo = (RecyclerView) findViewById(R.id.rv_brakInfo);
        rvBrakInfo.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    public class JsonReadByWorker extends AsyncTask<String, String, JSONObject> {

        JSONObject object;
        ProgressDialog dialog = new ProgressDialog(BrakInfoActivity.this);

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
                object = responce.getBrakForMaster(taskId);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(BrakInfoActivity.this,  R.style.AlertDialogStyle);
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
                    dialog.dismiss();
                }
            } catch (JSONException e) {
                Toast.makeText(BrakInfoActivity.this, "Ошибка", Toast.LENGTH_SHORT).show();
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
        jsonArray = json.getJSONArray("allBrak");
        brakList = new ArrayList<Defect>();

        for (int i = 0; i < jsonArray.length(); i++) {
            Defect defect = new Defect();

            JSONObject jsonObject = jsonArray.getJSONObject(i);
            defect.setNameDefect(jsonObject.getString("cause_name"));
            defect.setQuantumDefect(jsonObject.getInt("defect_count"));
            String date = jsonObject.getString("defect_date").replace("-", ".");
            String time = jsonObject.getString("defect_time").substring(0, 5);
            defect.setDate(date);
            defect.setTime(time);

            brakList.add(defect);
        }

        brakInfoAdapter = new RVBrakInfoMaster(getApplicationContext(), brakList);
        LinearLayoutManager llm1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvBrakInfo.setAdapter(brakInfoAdapter);
        rvBrakInfo.setLayoutManager(llm1);

    }
}
