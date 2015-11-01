package ru.javaapp.workmanagement.activities.master;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import ru.javaapp.workmanagement.R;
import ru.javaapp.workmanagement.activities.reports.ReportsMainActivity;
import ru.javaapp.workmanagement.fragments.FragmentDrawer;

public class MasterMainActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    private FragmentDrawer drawerFragment;
    Button btn_createTask, btn_tracking, btn_reports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_main);

        toolbarInitialize();
        setNavigationDrawer(); // set navigation drawer
        componentinitialize();
        setListeners();
    }

    /**
     * initialize navigation drawer fragment
     */
    private void setNavigationDrawer() {
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
    }

    private void setListeners(){
        btn_createTask.setOnClickListener(this);
        btn_tracking.setOnClickListener(this);
        btn_reports.setOnClickListener(this);
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
                Intent reportsActivity = new Intent(getApplicationContext(), ReportsMainActivity.class);
                startActivity(reportsActivity);
                break;
        }
    }

    public void onBackPressed() {
        Log.d("My", "OnBackPressed");
        try {
            finish();
        }
        catch (Exception e){
        }
    }
}
