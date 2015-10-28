package ru.javaapp.workmanagement.activities.worker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import ru.javaapp.workmanagement.R;
import ru.javaapp.workmanagement.list.DividerItemDecoration;
import ru.javaapp.workmanagement.workDB.Transmission;

/**
 * Created by User on 27.10.2015.
 */
public class StopActivity extends AppCompatActivity {

    private Toolbar toolbar;
    Button btnOk, btnCancel;
    ListView lvStop;
    int taskId, stopId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop);

        toolbarInitialize(); // init toolbar
        componentsInitialize(); //init components in activity
        setListeners(); // set all listeners

    }
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
        taskId = getIntent().getIntExtra("taskId", 0);
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        lvStop = (ListView) findViewById(R.id.lvStop);
        ArrayAdapter adapter = new ArrayAdapter(StopActivity.this, android.R.layout.simple_list_item_single_choice, getResources().getStringArray(R.array.stop_cause));
        lvStop.setAdapter(adapter);
    }

    private void setListeners() {
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(taskId != 0) {
                    Transmission transmission = new Transmission();
                    transmission.UpdateDownTime(taskId, stopId, getApplicationContext());
                    finish();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        lvStop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                stopId = position + 1;
            }
        });
    }

}
