package ru.javaapp.workmanagement.activities.worker;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import ru.javaapp.workmanagement.R;
import ru.javaapp.workmanagement.dao.Task;
import ru.javaapp.workmanagement.workDB.Transmission;

/**
 * Created by User on 15.10.2015.
 */
public class TaskRunActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageButton btnMinus, btnPlus;
    Button btnFinish, btnAbout, btnSos, btnBrak, btnStop;
    TextView tvCurrentAdd,tvCountToGo;
    EditText etSpeedCount;
    Task taskGet; // Наше задание
    int myTemp; // Текущий темп инкремента
    int currentCount; // Текущее количество штук
    int currentCountToGo; // Сколько осталось выполнить штук
    int getCountToGo; // План на задание

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_run);

        toolbarInitialize(); // init toolbar
        takeFieldsFromPreviousActivity(); // get data from Intent
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

    private void takeFieldsFromPreviousActivity() {
        taskGet = (Task) getIntent().getSerializableExtra("taskObj"); // Получаем наше задание
    }

    private void componentsInitialize() {
        try{
            getSupportActionBar().setTitle("№ " + Integer.toString(taskGet.getIdTask()));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        btnFinish = (Button) findViewById(R.id.btn_finish);
        btnMinus = (ImageButton) findViewById(R.id.btn_minus);
        btnPlus = (ImageButton) findViewById(R.id.btn_plus);
        btnAbout = (Button) findViewById(R.id.btn_about);
        btnSos = (Button) findViewById(R.id.btn_sos);
        btnStop = (Button) findViewById(R.id.btn_stop);
        btnBrak = (Button) findViewById(R.id.btn_brak);
        tvCurrentAdd = (TextView) findViewById(R.id.tv_currentAdd);
        tvCountToGo = (TextView) findViewById(R.id.tv_CountToGo);
        etSpeedCount = (EditText) findViewById(R.id.et_speedCount);

        int getCountCurrentTask = taskGet.getCountCurrentTask(); // Текущее количество штук
        getCountToGo = taskGet.getCountPlanTask(); // Наш план
        int count = getCountToGo - getCountCurrentTask;
        tvCurrentAdd.setText(Integer.toString(getCountCurrentTask));
        tvCountToGo.setText(Integer.toString(count));
        currentCount = getCountCurrentTask;
    }

    // проверка полей на заполненность
    private boolean checkFillField(){
        if(!etSpeedCount.getText().toString().equals(""))
            return true;
        else
            return false;
    }

    private void setListeners() {
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishTask();
            }
        });
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doMinus();
            }
        });
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doPlus();
            }
        });
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTaa = new Intent(TaskRunActivity.this, TaskAboutActivity.class);
                intentTaa.putExtra("taskAbout", taskGet);
                startActivity(intentTaa);
            }
        });

        btnSos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(new Intent(TaskRunActivity.this, SosActivity.class));    }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startActivity(new Intent(TaskRunActivity.this, StopActivity.class));    }
        });
        btnBrak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startActivity(new Intent(TaskRunActivity.this, BrakActivity.class));    }
        });
    }

    // алгоритм при плюсе
    private String mathPlus() {
        myTemp = Integer.parseInt(etSpeedCount.getText().toString());
        currentCount = Integer.parseInt(tvCurrentAdd.getText().toString());
        currentCount = currentCount + myTemp;
        currentCountToGo = Integer.parseInt(tvCountToGo.getText().toString()) - myTemp;
        return Integer.toString(currentCount);

    }

    // алгоритм при минусе
    private String mathMinus() {
        myTemp = Integer.parseInt(etSpeedCount.getText().toString());
        currentCount = Integer.parseInt(tvCurrentAdd.getText().toString());
        currentCount = currentCount - myTemp;
        if(currentCount < 0) {
            currentCount = currentCount + myTemp;
            Toast.makeText(TaskRunActivity.this, "Введите цифру в поле ДОБАВИТЬ", Toast.LENGTH_LONG).show();
            return null;
        }
        currentCountToGo = Integer.parseInt(tvCountToGo.getText().toString()) + myTemp;
        return Integer.toString(currentCount);

    }

    @Override
    public void onBackPressed() {
        Log.d("My", "On Back Pressed");
        try {
            backTaskList();
        }
        catch (Exception e){
            e.printStackTrace();
            return;
        }
        super.onBackPressed();
    }

    // возврат в список всех заданий
    private void backTaskList(){
        int taskId = taskGet.getIdTask();
        int currentcount = currentCount;
        try{
            Transmission responce = new Transmission();
            responce.UpdateCurrentCount(taskId, currentcount, getApplicationContext());
            startActivity(new Intent(TaskRunActivity.this, TaskListActivity.class));
            finish();
        }
        catch (Exception e){
            Log.d("My", "On Back Pressed " + e);
            e.printStackTrace();
            return;
        }

    }

    // завершение задания
    private void finishTask(){

        final AlertDialog.Builder quitDialog = new AlertDialog.Builder(
                TaskRunActivity.this);
        quitDialog.setTitle(getString(R.string.finish_task));

        quitDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int taskId = taskGet.getIdTask();
                int statusid = 3;
                int currentcount = currentCount;

                try {
                    Transmission responce = new Transmission();
                    responce.UpdateCountAndStatus(taskId, currentcount, statusid, getApplicationContext());
                    startActivity(new Intent(TaskRunActivity.this, TaskListActivity.class));
                    finish();
                }
                catch (Exception e){
                    e.printStackTrace();
                    return;
                }
            }
        });

        quitDialog.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        quitDialog.show();

    }

    // делать минус
    private void doMinus(){
        if(checkFillField()){
            String text = mathMinus();
            if(text == null){
                return;
            }
            else{
                tvCurrentAdd.setText(text);
                tvCountToGo.setText(Integer.toString(currentCountToGo));
            }

        }
        else{
            Toast.makeText(TaskRunActivity.this, "Введите цифру в поле ДОБАВИТЬ", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    // делать плюс
    private void doPlus(){
        if(checkFillField()){
            String text = mathPlus();
            if(text == null){
                return;
            }
            else{
                tvCurrentAdd.setText(text);
                tvCountToGo.setText(Integer.toString(currentCountToGo));
            }
        }
        else{
            Toast.makeText(TaskRunActivity.this, "Введите цифру в поле ДОБАВИТЬ", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
