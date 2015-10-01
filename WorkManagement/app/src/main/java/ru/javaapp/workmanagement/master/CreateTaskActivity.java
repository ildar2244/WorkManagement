package ru.javaapp.workmanagement.master;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import ru.javaapp.workmanagement.R;
import ru.javaapp.workmanagement.fragments.DatePickerFragmentBefore;
import ru.javaapp.workmanagement.fragments.TimePickerFragmentBefore;

public class CreateTaskActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView tvDateBefore, tvDateAfter, tvTimeBefore, tvTimeAfter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        toolbarInitialize();
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


    public void onClickDateBefore(View v) {
        DialogFragment newFragment = new DatePickerFragmentBefore();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    /**
     * Call TimePicker and setting time
     * @param v
     */
    public void onClickTimeBefore(View v) {
        DialogFragment newFragment = new TimePickerFragmentBefore();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

}
