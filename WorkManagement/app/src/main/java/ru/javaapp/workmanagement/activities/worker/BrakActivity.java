package ru.javaapp.workmanagement.activities.worker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import ru.javaapp.workmanagement.R;
import ru.javaapp.workmanagement.workDB.Transmission;

/**
 * Created by User on 27.10.2015.
 */
public class BrakActivity extends AppCompatActivity {

    private Toolbar toolbar;
    Button btnOk, btnCancel;
    ListView lvBrak;
    EditText etBrakCount;
    int defectId;
    int taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brak);

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
        btnOk = (Button) findViewById(R.id.btn_brak_ok);
        btnCancel = (Button) findViewById(R.id.btn_brak_cancel);
        lvBrak = (ListView) findViewById(R.id.lvBrak);
        etBrakCount = (EditText) findViewById(R.id.et_brakCount);
        ArrayAdapter adapter = new ArrayAdapter(BrakActivity.this, android.R.layout.simple_list_item_single_choice, getResources().getStringArray(R.array.brak_cause));
        lvBrak.setAdapter(adapter);
    }

    private boolean checkFields(){
        if(etBrakCount.getText().toString().trim().length() != 0 ||
                lvBrak.isSelected()){
            return true;
        }
        else{
            Toast.makeText(getApplicationContext(), "Введите число", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void setListeners() {
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkFields()){
                    if(taskId != 0) {
                        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                        String time = new SimpleDateFormat("HH:mm:ss").format(new Date(System.currentTimeMillis()));
                        Transmission transmission = new Transmission();
                        transmission.UpdateDefect(taskId, defectId, Integer.parseInt(etBrakCount.getText().toString()), getApplicationContext(), date, time);
                        finish();
                    }
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        lvBrak.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                defectId = position + 1;
            }
        });
    }
}
