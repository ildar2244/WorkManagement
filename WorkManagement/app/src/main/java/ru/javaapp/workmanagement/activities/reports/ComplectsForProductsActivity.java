package ru.javaapp.workmanagement.activities.reports;

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
import java.util.Arrays;
import java.util.List;

import ru.javaapp.workmanagement.Helper;
import ru.javaapp.workmanagement.R;
import ru.javaapp.workmanagement.activities.auth.LoginActivity;
import ru.javaapp.workmanagement.adapters.RVAdaptersTasks;
import ru.javaapp.workmanagement.adapters.RVComplectAdapter;
import ru.javaapp.workmanagement.dao.Complects;
import ru.javaapp.workmanagement.dao.Task;
import ru.javaapp.workmanagement.list.DividerItemDecoration;
import ru.javaapp.workmanagement.list.RecyclerItemClickListener;
import ru.javaapp.workmanagement.workDB.Transmission;

/**
 * Created by User on 01.11.2015.
 */
public class ComplectsForProductsActivity extends AppCompatActivity {
    RecyclerView rvComplects;
    Toolbar toolbar;
    RVComplectAdapter complectsAdapter;
    List<Complects> complectsList;
    int productId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complects_for_products);

        toolbarInitialize();
        componentsInitialize();
        setListeners();
        new JsonReadByWorker().execute();
    }

    private void setListeners() {
        rvComplects.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }
                })
        );
    }

    private void componentsInitialize() {
        rvComplects = (RecyclerView) findViewById(R.id.rv_complects);
        rvComplects.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        productId = getIntent().getIntExtra("productId", 0);
    }

    private void toolbarInitialize() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try{
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

    public class JsonReadByWorker extends AsyncTask<String, String, JSONObject> {

        JSONObject object;
        ProgressDialog dialog = new ProgressDialog(ComplectsForProductsActivity.this);

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
                object = responce.getComplectsForProduct(productId);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(ComplectsForProductsActivity.this,  R.style.AlertDialogStyle);
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
                Toast.makeText(ComplectsForProductsActivity.this, "Ошибка", Toast.LENGTH_SHORT).show();
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
        complectsList = new ArrayList<Complects>();
        jsonArray = json.getJSONArray("allComplects");

        for (int i = 0; i < jsonArray.length(); i++) {
            Complects complect = new Complects();
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            complect.setName(jsonObject.getString("nameWhat"));
            complect.setCount(0);
            complectsList.add(complect);
        }

        complectsAdapter = new RVComplectAdapter(getApplicationContext(), complectsList);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvComplects.setAdapter(complectsAdapter);
        rvComplects.setLayoutManager(llm);

    }
}
