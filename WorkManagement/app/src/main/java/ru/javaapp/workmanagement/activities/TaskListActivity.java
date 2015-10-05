package ru.javaapp.workmanagement.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;

import ru.javaapp.workmanagement.MainActivity;
import ru.javaapp.workmanagement.R;

public class TaskListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        toolbarInitialize(); // init toolbar
        componentsInitialize(); //init components in activity
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
        tapSpec.setIndicator("ONE");
        tabHost.addTab(tapSpec);

        tapSpec = tabHost.newTabSpec("tabTwo");
        tapSpec.setContent(R.id.tab2);
        tapSpec.setIndicator("TWO");
        tabHost.addTab(tapSpec);

        tapSpec = tabHost.newTabSpec("tabThree");
        tapSpec.setContent(R.id.tab3);
        tapSpec.setIndicator("THREE");
        tabHost.addTab(tapSpec);
    }

    /**
     * Action for the "Back"
     */
    @Override
    public void onBackPressed() {
        Log.d("My", "On Back Pressed");
        super.onBackPressed();
        try {
            startActivity(new Intent(TaskListActivity.this, MainActivity.class));
            finish();
        } catch (Exception e) {}
    }
}
