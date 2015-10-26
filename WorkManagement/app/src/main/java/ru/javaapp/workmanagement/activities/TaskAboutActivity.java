package ru.javaapp.workmanagement.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import ru.javaapp.workmanagement.R;
import ru.javaapp.workmanagement.dao.Task;

public class TaskAboutActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView taaTimeStart, taaTimeFinish, taaDateStart, taaDateFinish;
    private TextView taaWhatName, taaPlaceName, taaComment;
    private TextView taaCountPlan, taaCountCurrent, taaDiffCount;
    private int diffCount;
    private Task taskGetAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbarInitialize(); // init toolbar
        takeFieldsFromPreviousActivity(); // get data from Intent
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
        taaTimeStart = (TextView) findViewById(R.id.taa_timestart);
        taaTimeFinish = (TextView) findViewById(R.id.taa_timefinish);
        taaDateStart = (TextView) findViewById(R.id.taa_datestart);
        taaDateFinish = (TextView) findViewById(R.id.taa_datefinish);
        taaWhatName = (TextView) findViewById(R.id.taa_whatname);
        taaPlaceName = (TextView) findViewById(R.id.taa_placename);
        taaCountPlan = (TextView) findViewById(R.id.taa_countplan);
        taaCountCurrent = (TextView) findViewById(R.id.taa_countcurrent);
        taaDiffCount = (TextView) findViewById(R.id.taa_countdifference);
        taaComment = (TextView) findViewById(R.id.taa_comment);

        // set data to UI-elements
        try {
            getSupportActionBar().setTitle("№ " + Integer.toString(taskGetAbout.getIdTask()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        taaTimeStart.setText(taskGetAbout.getTimeStart());
        taaTimeFinish.setText(taskGetAbout.getTimeFinish());
        taaDateStart.setText(taskGetAbout.getDateStart());
        taaDateFinish.setText(taskGetAbout.getDateFinish());
        taaWhatName.setText(taskGetAbout.getWhatName());
        taaPlaceName.setText(taskGetAbout.getPlaceName());
        taaCountPlan.setText(taskGetAbout.getCountPlanTask());
        taaCountCurrent.setText(taskGetAbout.getCountCurrentTask());

        diffCount = taskGetAbout.getCountPlanTask() - taskGetAbout.getCountCurrentTask();
        taaDiffCount.setText(Integer.toString(diffCount));

        taaComment.setText(taskGetAbout.getCommentTask());
    }

    /**
     * Get data from previous activity
     */
    private void takeFieldsFromPreviousActivity() {
        taskGetAbout = (Task) getIntent().getSerializableExtra("taskAbout");
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
