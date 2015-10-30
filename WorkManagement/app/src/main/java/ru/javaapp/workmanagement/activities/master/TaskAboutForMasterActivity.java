package ru.javaapp.workmanagement.activities.master;

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

/**
 * Created by User on 27.10.2015.
 */
public class TaskAboutForMasterActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tvTimeStart, tvTimeFinish, tvDateStart, tvDateFinish;
    private TextView tvWhatName, tvPlaceName, tvComment;
    private TextView tvCountPlan, tvCountCurrent, tvDiffCount, tvBrak, tvDowntime;
    private int diffCount, taskId;
    private Task taskGetAbout;
    private Button btnStopInfo, btnBrakInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_about_for_master);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbarInitialize(); // init toolbar
        takeFieldsFromPreviousActivity(); // get data from Intent
        componentsInitialize(); //init components in activity
        setListeners();
    }

    private void setListeners() {
        btnBrakInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskAboutForMasterActivity.this, BrakInfoActivity.class);
                intent.putExtra("taskId", taskId);
                startActivity(intent);
            }
        });
        btnStopInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskAboutForMasterActivity.this, StopInfoActivity.class);
                intent.putExtra("taskId", taskId);
                startActivity(intent);
            }
        });
    }

    private void takeFieldsFromPreviousActivity() {
        taskGetAbout = (Task) getIntent().getSerializableExtra("taskObj");
        taskId = taskGetAbout.getIdTask();
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
     * Initialize all components in UI
     */
    private void componentsInitialize() {
        tvTimeStart = (TextView) findViewById(R.id.taafm_timestart);
        tvTimeFinish = (TextView) findViewById(R.id.taafm_timefinish);
        tvDateStart = (TextView) findViewById(R.id.taafm_datestart);
        tvDateFinish = (TextView) findViewById(R.id.taafm_datefinish);
        tvWhatName = (TextView) findViewById(R.id.taafm_whatname);
        tvPlaceName = (TextView) findViewById(R.id.taafm_placename);
        tvCountPlan = (TextView) findViewById(R.id.taafm_countplan);
        tvCountCurrent = (TextView) findViewById(R.id.taafm_countcurrent);
        tvDiffCount = (TextView) findViewById(R.id.taafm_countdifference);
        tvBrak = (TextView) findViewById(R.id.taafm_brak);
        tvDowntime = (TextView) findViewById(R.id.taafm_downtime);
        tvComment = (TextView) findViewById(R.id.taafm_comment);
        btnStopInfo = (Button) findViewById(R.id.btn_stopInfo);
        btnBrakInfo = (Button) findViewById(R.id.btn_brakInfo);

        // set data to UI-elements
        try {
            getSupportActionBar().setTitle("№ " + Integer.toString(taskGetAbout.getIdTask()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvTimeStart.setText(taskGetAbout.getTimeStart());
        tvTimeFinish.setText(taskGetAbout.getTimeFinish());
        tvDateStart.setText(taskGetAbout.getDateStart());
        tvDateFinish.setText(taskGetAbout.getDateFinish());
        tvWhatName.setText(taskGetAbout.getWhatName());
        tvPlaceName.setText(taskGetAbout.getPlaceName());
        tvCountPlan.setText(Integer.toString(taskGetAbout.getCountPlanTask()));
        tvCountCurrent.setText(Integer.toString(taskGetAbout.getCountCurrentTask()));
        tvComment.setText(taskGetAbout.getCommentTask());

        diffCount = taskGetAbout.getCountPlanTask() - taskGetAbout.getCountCurrentTask();
        tvDiffCount.setText(Integer.toString(diffCount));
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
}
