package ru.javaapp.workmanagement.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import ru.javaapp.workmanagement.MainActivity;
import ru.javaapp.workmanagement.R;

/**
 * Created by User on 15.10.2015.
 */
public class TaskRunActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageButton btnMinus, btnPlus;
    TextView tvCurrentAdd,tvCountToGo, tvTimeToGo, tvWhatDo, tvWhereDo, tvComment;
    EditText etSpeedCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_run);

        toolbarInitialize(); // init toolbar
        componentsInitialize(); //init components in activity
        setListeners(); // set all listeners
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

    private void componentsInitialize() {
        btnMinus = (ImageButton) findViewById(R.id.btn_minus);
        btnPlus = (ImageButton) findViewById(R.id.btn_plus);
        tvCurrentAdd = (TextView) findViewById(R.id.tv_currentAdd);
        tvCountToGo = (TextView) findViewById(R.id.tv_CountToGo);
        tvTimeToGo = (TextView) findViewById(R.id.tv_TimeToGo);
        tvWhatDo = (TextView) findViewById(R.id.tv_what);
        tvWhereDo = (TextView) findViewById(R.id.tv_where);
        tvComment = (TextView) findViewById(R.id.tv_comment);
        etSpeedCount = (EditText) findViewById(R.id.et_speedCount);
    }

    private void setListeners() {
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Log.d("My", "On Back Pressed");
        super.onBackPressed();
        try {
            finish();
        } catch (Exception e) {}
    }

}
