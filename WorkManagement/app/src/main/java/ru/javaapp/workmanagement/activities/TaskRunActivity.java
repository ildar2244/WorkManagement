package ru.javaapp.workmanagement.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import ru.javaapp.workmanagement.R;
import ru.javaapp.workmanagement.dao.Task;

/**
 * Created by User on 15.10.2015.
 */
public class TaskRunActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageButton btnMinus, btnPlus;
    TextView tvCurrentAdd,tvCountToGo, tvTimeToGo, tvWhatDo, tvWhereDo, tvComment;
    EditText etSpeedCount;
    Task taskGet;
    int currentSpeed, currentCount, currentCountToGo;
    int countToGo; // План вн азадание


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
        taskGet = (Task) getIntent().getSerializableExtra("taskObj");
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

        tvCountToGo.setText(Integer.toString(taskGet.getCountPlanTask()));
        tvWhatDo.setText(taskGet.getWhatName());
        tvWhereDo.setText(taskGet.getPlaceName());
        tvComment.setText(taskGet.getCommentTask());
        countToGo =Integer.parseInt(tvCountToGo.getText().toString());
    }

    private boolean checkFillField(){
        if(!etSpeedCount.getText().toString().equals(""))
            return true;
        else
            return false;
    }

    private void setListeners() {
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkFillField()){
                    String text = methodMinus();
                    if(text == null){
                        return;
                    }
                    else{
                        tvCurrentAdd.setText(text);
                        tvCountToGo.setText(Integer.toString(currentCountToGo));
                    }

                }
                else{
                    Toast.makeText(TaskRunActivity.this, "Заполните свою скорость", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkFillField()){
                    String text = methodPlus();
                    if(text == null){
                        return;
                    }
                    else{
                        tvCurrentAdd.setText(text);
                        tvCountToGo.setText(Integer.toString(currentCountToGo));
                    }
                }
                else{
                    Toast.makeText(TaskRunActivity.this, "Заполните свою скорость", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private String methodPlus() {
        currentSpeed = Integer.parseInt(etSpeedCount.getText().toString());
        currentCount = Integer.parseInt(tvCurrentAdd.getText().toString());
        currentCount = currentCount + currentSpeed;
        if(currentCount > countToGo) {
            currentCount = currentCount - currentSpeed;
            Toast.makeText(TaskRunActivity.this, "Введите другую скорость", Toast.LENGTH_SHORT).show();
            return null;
        }
        else {
            currentCountToGo = Integer.parseInt(tvCountToGo.getText().toString()) - currentSpeed;
            return Integer.toString(currentCount);
        }
    }

    private String methodMinus() {
        currentSpeed = Integer.parseInt(etSpeedCount.getText().toString());
        currentCount = Integer.parseInt(tvCurrentAdd.getText().toString());
        currentCount = currentCount - currentSpeed;
        if(currentCount < 0) {
            currentCount = currentCount + currentSpeed;
            Toast.makeText(TaskRunActivity.this, "Введите другую скорость", Toast.LENGTH_SHORT).show();
            return null;
        }
        currentCountToGo = Integer.parseInt(tvCountToGo.getText().toString()) + currentSpeed;
        return Integer.toString(currentCount);

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
