package ru.javaapp.workmanagement.activities.worker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import ru.javaapp.workmanagement.R;

/**
 * Created by User on 27.10.2015.
 */
public class StopActivity extends AppCompatActivity {

    private Toolbar toolbar;
    Button btnOk, btnCancel;
    CheckBox chEquipment, chTools, chHarvesting, chOther;

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
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        chEquipment = (CheckBox) findViewById(R.id.chbx_equipment);
        chTools = (CheckBox) findViewById(R.id.chbx_tools);
        chHarvesting = (CheckBox) findViewById(R.id.chbx_harvesting);
        chOther = (CheckBox) findViewById(R.id.chbx_other);
    }

    private void setListeners() {
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Ожидайте в следующей версии", Toast.LENGTH_SHORT).show();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
