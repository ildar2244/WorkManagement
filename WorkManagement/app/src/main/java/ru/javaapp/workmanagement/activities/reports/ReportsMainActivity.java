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
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.javaapp.workmanagement.Helper;
import ru.javaapp.workmanagement.R;
import ru.javaapp.workmanagement.adapters.RVProductsAdapter;
import ru.javaapp.workmanagement.dao.Complects;
import ru.javaapp.workmanagement.fragments.DatePickerFragment;
import ru.javaapp.workmanagement.list.DividerItemDecoration;
import ru.javaapp.workmanagement.workDB.Transmission;

/**
 * Created by User on 01.11.2015.
 */
public class ReportsMainActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabHost tabHost;
    RecyclerView rvProducts;
    RecyclerView rvPeriods;
    RVProductsAdapter productsAdapter;
    RVProductsAdapter periodsAdapter;
    List<Complects> productList;
    List<Complects> periodsList;
    TextView tvOt, tvDo;
    Button postDates;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_main);

        toolbarInitialize();
        componentsInitialize();
        setListeners();
        new JsonReportAsyncTask().execute();
    }

    private void setListeners() {
        tvOt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment fragmentBefore = new DatePickerFragment(R.id.rma_tv_ot);
                fragmentBefore.show(getSupportFragmentManager(), "before");

            }
        });
        tvDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment fragmentAfter = new DatePickerFragment(R.id.rma_tv_do);
                fragmentAfter.show(getSupportFragmentManager(), "after");
            }
        });
        postDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkFillAllFields()) {
                    new JsonPeriodsAsyncTask().execute();
                }
            }
        });
    }

    // check for filled fields by select dates
    boolean checkFillAllFields(){
        if(
                tvOt.getText().toString().trim().length() == 0 ||
                        tvDo.getText().toString().trim().length() == 0 )
        {
            return false;
        }
        else{
            return true;
        }
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

    private void componentsInitialize() {
        tabHost = (TabHost) findViewById(R.id.tabHost3);
        tabHost.setup();
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tabOne");
        tabSpec.setContent(R.id.linearLayout4);
        tabSpec.setIndicator("Сегодня");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tabTwo");
        tabSpec.setContent(R.id.linearLayout5);
        tabSpec.setIndicator("Период");
        tabHost.addTab(tabSpec);

        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        tvOt = (TextView) findViewById(R.id.rma_tv_ot);
        tvOt.setText(date);
        tvDo = (TextView) findViewById(R.id.rma_tv_do);
        tvDo.setText(date);
        postDates = (Button) findViewById(R.id.rma_btn);

        // Список за сегодня
        rvProducts = (RecyclerView) findViewById(R.id.rv_products);
        rvProducts.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        // Список за период
        rvPeriods = (RecyclerView) findViewById(R.id.rv_periods);
        rvPeriods.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    /**
     * Получение в фоне отчета за СЕГОДНЯ
     */
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
                jsonAllProduct = productReport.getReportProductsToday(Helper.getCurrentDate());
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
        jsonArray = jsonObject.getJSONArray("allProductsToday");

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

    /**
     * Запрос в фоне на получение отчета за ПЕРИОД
     */
    public class JsonPeriodsAsyncTask extends AsyncTask<String, String, JSONObject> {
        JSONObject jsonPeriods;
        String dateBefore;
        String dateAfter;
        ProgressDialog dialog = new ProgressDialog(ReportsMainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dateBefore = tvOt.getText().toString();
            dateAfter = tvDo.getText().toString();
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
                Transmission reportPeriod = new Transmission();
                jsonPeriods = reportPeriod.getReportProductPeriods(dateBefore, dateAfter);
                return jsonPeriods;
            } else {
                return null;
            }
        }

        protected void onPostExecute(JSONObject json) {
            try {
                if(json != null) {
                    ListReportPeriods(json);
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
     * Разбираем json-файл и создаем отчет в списке по ПЕРИОДУ
     * @param object
     * @throws Exception
     */
    public void ListReportPeriods(JSONObject object) throws JSONException {
        JSONArray array = null;
        periodsList = new ArrayList<Complects>();
        array = object.getJSONArray("allProductPeriods");

        for (int i = 0; i < array.length(); i++) {
            Complects complect = new Complects();
            JSONObject jsonAll = array.getJSONObject(i);
            complect.setName(jsonAll.getString("nameWhat"));
            complect.setCount(jsonAll.getInt("SUM(Task_table.count_current)"));
            periodsList.add(complect);
        }

        periodsAdapter = new RVProductsAdapter(getApplicationContext(), periodsList);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvPeriods.setAdapter(periodsAdapter);
        rvPeriods.setLayoutManager(llm);
    }
}
