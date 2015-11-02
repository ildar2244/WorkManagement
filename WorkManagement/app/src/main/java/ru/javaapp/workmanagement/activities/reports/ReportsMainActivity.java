package ru.javaapp.workmanagement.activities.reports;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import java.util.Arrays;
import java.util.List;
import ru.javaapp.workmanagement.R;
import ru.javaapp.workmanagement.adapters.RVProductsAdapter;
import ru.javaapp.workmanagement.list.DividerItemDecoration;
import ru.javaapp.workmanagement.list.RecyclerItemClickListener;

/**
 * Created by User on 01.11.2015.
 */
public class ReportsMainActivity extends AppCompatActivity {

    RecyclerView rvProducts;
    Toolbar toolbar;
    RVProductsAdapter productsAdapter;
    List<String> productsList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_main);

        toolbarInitialize();
        componentsInitialize();
        setListeners();
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

        String[] products = getResources().getStringArray(R.array.productsName);
        productsList = Arrays.asList(products);

        productsAdapter = new RVProductsAdapter(getApplicationContext(), productsList);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvProducts.setAdapter(productsAdapter);
        rvProducts.setLayoutManager(llm);

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
}
