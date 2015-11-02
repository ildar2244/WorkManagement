package ru.javaapp.workmanagement.activities.reports;

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
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.javaapp.workmanagement.Helper;
import ru.javaapp.workmanagement.R;
import ru.javaapp.workmanagement.adapters.RVProductsAdapter;
import ru.javaapp.workmanagement.dao.Complects;
import ru.javaapp.workmanagement.list.DividerItemDecoration;
import ru.javaapp.workmanagement.list.RecyclerItemClickListener;
import ru.javaapp.workmanagement.workDB.Transmission;

/**
 * Created by User on 01.11.2015.
 */
public class ReportsMainActivity extends AppCompatActivity {

    RecyclerView rvProducts;
    Toolbar toolbar;
    RVProductsAdapter productsAdapter;
    List<Complects> productList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_main);

        toolbarInitialize();
        componentsInitialize();
        //setListeners();
        new JsonReportAsyncTask().execute();
    }

    private void setListeners() {
        rvProducts.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        int productId = position + 1;
                        Intent intent = new Intent(ReportsMainActivity.this, ComplectsForProductsActivity.class);
                        intent.putExtra("productId", productId);
                        startActivity(intent);
                    }
                })
        );
    }

    private void componentsInitialize() {
        rvProducts = (RecyclerView) findViewById(R.id.rv_products);
        rvProducts.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
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

    public class JsonReportAsyncTask extends AsyncTask<String, String, JSONObject> {

        JSONObject jsonAllProduct;
        ProgressDialog dialog = new ProgressDialog(ReportsMainActivity.this);

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

            if (Helper.isConnected(getApplicationContext())) {
                Transmission productReport = new Transmission();
                jsonAllProduct = productReport.getReportAllProduct();
                return jsonAllProduct;
            } else {
                return null;
            }
        }

        protected void onPostExecute(JSONObject json) {
            try {
                if(json != null) {
                    ListReportProduct(json);
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReportsMainActivity.this,  R.style.AlertDialogStyle);
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
                Toast.makeText(ReportsMainActivity.this, "Ошибка", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            dialog.dismiss();
        }
    }

    /**
     * Tasks elements getting from JSONObject to String and create RecyclerView
     * @param jsonObject
     * @throws JSONException
     */
    public void ListReportProduct(JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = null;
        productList = new ArrayList<Complects>();
        jsonArray = jsonObject.getJSONArray("allProducts");

        for (int i = 0; i < jsonArray.length(); i++) {
            Complects complect = new Complects();
            JSONObject jsonAll = jsonArray.getJSONObject(i);
            complect.setName(jsonAll.getString("nameWhat"));
            complect.setCount(jsonAll.getInt("SUM(Task_table.count_current)"));
            productList.add(complect);
        }

        productsAdapter = new RVProductsAdapter(getApplicationContext(), productList);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvProducts.setAdapter(productsAdapter);
        rvProducts.setLayoutManager(llm);
    }
}
