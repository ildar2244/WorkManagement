package ru.javaapp.workmanagement.master;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import ru.javaapp.workmanagement.MainActivity;
import ru.javaapp.workmanagement.R;
import ru.javaapp.workmanagement.fragments.DatePickerFragmentAfter;
import ru.javaapp.workmanagement.fragments.DatePickerFragmentBefore;
import ru.javaapp.workmanagement.fragments.TimePickerFragmentAfter;
import ru.javaapp.workmanagement.fragments.TimePickerFragmentBefore;

public class CreateTaskActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView tvDateBefore, tvDateAfter, tvTimeBefore, tvTimeAfter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        toolbarInitialize();
        componentsInitialize();
        setListeners();
    }

    private void componentsInitialize(){
        tvDateBefore = (TextView) findViewById(R.id.tv_input_dateBefore);
        tvTimeBefore = (TextView) findViewById(R.id.tv_input_timeBefore);
        tvDateAfter = (TextView) findViewById(R.id.tv_input_dateAfter);
        tvTimeAfter = (TextView) findViewById(R.id.tv_input_timeAfter);
    }

    private void toolbarInitialize() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
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

    private void setListeners(){
        tvDateBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragmentBefore();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
        tvTimeBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new TimePickerFragmentBefore();
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });
        tvDateAfter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragmentAfter();
                newFragment.show(getSupportFragmentManager(), "dateAfterPicker");
            }
        });
        tvTimeAfter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new TimePickerFragmentAfter();
                newFragment.show(getSupportFragmentManager(), "timeAfterPicker");
            }
        });
    }

    public void onBackPressed() {
        Log.d("My", "OnBackPressed");
        try {
            startActivity(new Intent(CreateTaskActivity.this, MainActivity.class));
            finish();
        }
        catch (Exception e){
        }
    }

}
