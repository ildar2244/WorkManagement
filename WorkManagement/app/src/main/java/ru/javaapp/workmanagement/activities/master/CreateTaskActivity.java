package ru.javaapp.workmanagement.activities.master;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import ru.javaapp.workmanagement.R;
import ru.javaapp.workmanagement.fragments.DatePickerFragmentAfter;
import ru.javaapp.workmanagement.fragments.DatePickerFragmentBefore;
import ru.javaapp.workmanagement.fragments.TimePickerFragmentAfter;
import ru.javaapp.workmanagement.fragments.TimePickerFragmentBefore;
import ru.javaapp.workmanagement.workDB.Transmission;

public class CreateTaskActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText countPlan, comment;
    Button send_btn;
    TextView tvDateBefore, tvDateAfter, tvTimeBefore, tvTimeAfter;
    Spinner whomSpinner, whatSpinner, whereSpinner;
    ArrayAdapter whomAdapter, whatAdapter, whereAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        toolbarInitialize();
        componentsInitialize();
        setListeners();
    }

    // inin all components
    private void componentsInitialize(){
        whomSpinner = (Spinner) findViewById(R.id.whomSpinner);
        whatSpinner = (Spinner) findViewById(R.id.whatSpinner);
        whereSpinner = (Spinner) findViewById(R.id.whereSpinner);
        countPlan = (EditText) findViewById(R.id.howManyEt);
        comment = (EditText) findViewById(R.id.cta_comment);

        whomSpinner.setAdapter(fillWhomSpinner());
        whomSpinner.setFocusable(true);
        whatSpinner.setAdapter(fillWhatSpinner());
        whatSpinner.setFocusable(true);
        whereSpinner.setAdapter(fillWhereSpinner());
        whereSpinner.setFocusable(true);

        tvDateBefore = (TextView) findViewById(R.id.tv_input_dateBefore);
        tvTimeBefore = (TextView) findViewById(R.id.tv_input_timeBefore);
        tvDateAfter = (TextView) findViewById(R.id.tv_input_dateAfter);
        tvTimeAfter = (TextView) findViewById(R.id.tv_input_timeAfter);

        send_btn = (Button) findViewById(R.id.btn_send);
    }

    // fill adapters for our spinners
    private ArrayAdapter fillWhomSpinner(){
        String[] whom = getResources().getStringArray(R.array.workers);
        whomAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, whom);
        whomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return whomAdapter;
    }
    private ArrayAdapter fillWhatSpinner(){
        String[] what = getResources().getStringArray(R.array.whatDo);
        whatAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, what);
        whatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return whatAdapter;
    }
    private ArrayAdapter fillWhereSpinner(){
        String[] where = getResources().getStringArray(R.array.whereDo);
        whereAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, where);
        whereAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return whereAdapter;
    }

    //init toolbar
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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    // all listeners
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
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkFillAllFields()) {
                    int workerId = whomSpinner.getSelectedItemPosition() + 1;
                    int whatId = whatSpinner.getSelectedItemPosition() + 1;
                    int whereId = whereSpinner.getSelectedItemPosition() + 1;
                    String plan = countPlan.getText().toString();
                    String commentEdit = comment.getText().toString();
                    String timeBefore = tvTimeBefore.getText().toString();
                    String timeAfter = tvTimeAfter.getText().toString();
                    String dateBefore = tvDateBefore.getText().toString();
                    String dateAfter = tvDateAfter.getText().toString();
                    Transmission responce = new Transmission();
                    responce.CreateTask(1, workerId, whatId, whereId, plan, commentEdit, timeBefore,
                            timeAfter, dateBefore, dateAfter, getApplicationContext());
                    finish();
                }
                else{
                    return;
                }
            }
        });
    }

    // check for filled all fields
    boolean checkFillAllFields(){
        if(
                countPlan.getText().toString().trim().length() == 0 ||
                        tvDateAfter.getText().toString().trim().length() == 0 ||
                        tvDateBefore.getText().toString().trim().length() == 0 ||
                        tvTimeAfter.getText().toString().trim().length() == 0 ||
                        tvTimeBefore.getText().toString().trim().length() == 0 )
        {
            return false;
        }
        else{
            return true;
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
