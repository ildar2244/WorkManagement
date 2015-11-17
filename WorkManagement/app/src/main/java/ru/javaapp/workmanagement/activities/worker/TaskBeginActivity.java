package ru.javaapp.workmanagement.activities.worker;

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
import ru.javaapp.workmanagement.workDB.Transmission;

public class TaskBeginActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tbaTimeStart, tbaTimeFinish, tbaDateStart, tbaDateFinish;
    private TextView tbaWhatName, tbaPlaceName, tbaComment;
    private TextView tbaCountPlan, tbaCountCurrent, tbaDiffCount;
    private Button tbaButtonTake;
    private int diffCount;
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
        tbaComment = (TextView) findViewById(R.id.tba_comment);
        tbaButtonTake = (Button) findViewById(R.id.tba_btnbegin);

        // set data to UI-elements
        try {
            getSupportActionBar().setTitle("№ " + Integer.toString(taskGet.getIdTask()));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        try {
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Action by clicking on button
     */
    private void setListeners() {
        tbaButtonTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int taskId = taskGet.getIdTask();
                int statusid = 2;
                try {
                    Transmission responce = new Transmission();
                    responce.UpdateTaskStatus(taskId, statusid, getApplicationContext());
                    finish();
                }
                catch (Exception e){
                    e.printStackTrace();
                    return;
                }
                Intent intentTba = new Intent(TaskBeginActivity.this, TaskRunActivity.class);
                intentTba.putExtra("taskObj", taskGet);
                startActivity(intentTba);
                finish();
            }
        });
    }

}
