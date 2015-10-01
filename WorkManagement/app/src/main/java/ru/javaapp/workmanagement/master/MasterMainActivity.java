package ru.javaapp.workmanagement.master;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import ru.javaapp.workmanagement.R;

public class MasterMainActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    Button btn_createTask, btn_tracking, btn_reports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_main);

        toolbarInitialize();
        componentinitialize();
    }

    private void toolbarInitialize() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void componentinitialize(){
        btn_createTask = (Button) findViewById(R.id.btn_addTask);
        btn_tracking = (Button) findViewById(R.id.btn_tracking);
        btn_reports = (Button) findViewById(R.id.btn_reports);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_addTask:
                Intent createTaskIntent = new Intent(getApplicationContext(), CreateTaskActivity.class);
                startActivity(createTaskIntent);
                break;
            case R.id.btn_tracking:
                Intent trackingIntent = new Intent(getApplicationContext(), TrackingActivity.class);
                startActivity(trackingIntent);
                break;
            case R.id.btn_reports:
                Intent reportsActivity = new Intent(getApplicationContext(), ReportsActivity.class);
                startActivity(reportsActivity);
                break;
        }
    }
}
