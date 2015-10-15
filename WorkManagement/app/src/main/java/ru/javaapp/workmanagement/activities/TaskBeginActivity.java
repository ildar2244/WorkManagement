package ru.javaapp.workmanagement.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.javaapp.workmanagement.R;
import ru.javaapp.workmanagement.dao.Task;

public class TaskBeginActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tbaTimeStart, tbaTimeFinish, tbaDateStart, tbaDateFinish;
    private TextView tbaWhatName, tbaPlaceName, tbaComment;
    private TextView tbaCountPlan, tbaCountCurrent, tbaDiffCount, tbaDiffTime;
    private Button tbaButtonTake;
    private int diffCount, diffTime;
    private Task taskGet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_begin);

        toolbarInitialize(); // init toolbar
        takeFieldsFromPreviousActivity(); // get data from Intent
        componentsInitialize(); //init components in activity
        setListeners(); // button select listener
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

        tbaTimeStart = (TextView) findViewById(R.id.tba_timestart);
        tbaTimeFinish = (TextView) findViewById(R.id.tba_timefinish);
        tbaDateStart = (TextView) findViewById(R.id.tba_datestart);
        tbaDateFinish = (TextView) findViewById(R.id.tba_datefinish);
        tbaWhatName = (TextView) findViewById(R.id.tba_whatname);
        tbaPlaceName = (TextView) findViewById(R.id.tba_placename);
        tbaCountPlan = (TextView) findViewById(R.id.tba_countplan);
        tbaCountCurrent = (TextView) findViewById(R.id.tba_countcurrent);
        tbaDiffCount = (TextView) findViewById(R.id.tba_countdifference);
        tbaDiffTime = (TextView) findViewById(R.id.tba_timedifference);
        tbaComment = (TextView) findViewById(R.id.tba_comment);
        tbaButtonTake = (Button) findViewById(R.id.tba_btnbegin);

        // set data to UI-elements
        getSupportActionBar().setTitle("№ " + Integer.toString(taskGet.getIdTask()));
        tbaTimeStart.setText(taskGet.getTimeStart());
        tbaTimeFinish.setText(taskGet.getTimeFinish());
        tbaDateStart.setText(taskGet.getDateStart());
        tbaDateFinish.setText(taskGet.getDateFinish());
        tbaWhatName.setText(taskGet.getWhatName());
        tbaPlaceName.setText(taskGet.getPlaceName());
        tbaCountPlan.setText(Integer.toString(taskGet.getCountPlanTask()));
        tbaCountCurrent.setText(Integer.toString(taskGet.getCountCurrentTask()));

        diffCount = taskGet.getCountPlanTask() - taskGet.getCountCurrentTask();
        tbaDiffCount.setText(Integer.toString(diffCount));

        tbaComment.setText(taskGet.getCommentTask());
    }

    /**
     * Get data from previous activity
     */
    private void takeFieldsFromPreviousActivity() {
        taskGet = (Task) getIntent().getSerializableExtra("taskObj");
    }

    /**
     * Action for the "Back"
     */
    @Override
    public void onBackPressed() {
        Log.d("My", "On Back Pressed");
        super.onBackPressed();
        /*try {
            startActivity(new Intent(TaskListActivity.this, MainActivity.class));
            finish();
        } catch (Exception e) {}*/
    }

    /**
     * Action by clicking on button
     */
    private void setListeners() {
        tbaButtonTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TaskBeginActivity.this, TaskRunActivity.class));
            }
        });
    }

}
