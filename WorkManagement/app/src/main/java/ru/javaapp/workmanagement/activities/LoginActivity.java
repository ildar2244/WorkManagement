package ru.javaapp.workmanagement.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import ru.javaapp.workmanagement.WorkerMainActivity;
import ru.javaapp.workmanagement.R;
import ru.javaapp.workmanagement.jsons.JSONAuthorize;
import ru.javaapp.workmanagement.jsons.JSONSelectTasksByWorker;
import ru.javaapp.workmanagement.master.MasterMainActivity;

public class LoginActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Spinner spinnerUsers;
    private ArrayAdapter<String> spinnerAdapter = null;
    private EditText inputLogin;
    private EditText inputPassword;
    private Button buttonEnter;
    private String usersType;
    private boolean isAuthorize;
    private String role;
    private String name, surname, sessionKey;
    String urlWorker = "http://autocomponent.motorcum.ru/select_worker_auth.php";
    String urlMaster = "http://autocomponent.motorcum.ru/update_currentCount_by_task.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        if(isAuthorize){
            startActivity(new Intent(LoginActivity.this, WorkerMainActivity.class));
            finish();
        }
        else {
            toolbarInitialize(); // init toolbar
            componentsInitialize(); //init components in activity
        }
    }

    /**
     * initialize toolbar
     */
    private void toolbarInitialize() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            toolbar.setTitle("Авторизация");
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

    /**
     * Initialize all components in UI
     */
    private void componentsInitialize() {

        spinnerUsers = (Spinner) findViewById(R.id.spinner_user);
        inputLogin = (EditText) findViewById(R.id.et_login);
        inputPassword = (EditText) findViewById(R.id.et_password);
        buttonEnter = (Button) findViewById(R.id.button_enter);

        spinnerUsers.setAdapter(selectSpinner());
        spinnerUsers.setFocusable(true);
        spinnerUsers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                usersType = spinnerUsers.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        buttonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usersType.equals("Работник")) {
                    role = "Работник";
                    JSONAuthorize jsonAuthorize = new JSONAuthorize(getApplicationContext(), role,
                            inputLogin.getText().toString(), inputPassword.getText().toString(), urlWorker);
                }
                if (usersType.equals("Руководитель")) {
                    role = "Руководитель";
                    Intent masterIntent = new Intent(LoginActivity.this, MasterMainActivity.class);
                    startActivity(masterIntent);
                    finish();
                }
            }
        });
    }

    /**
     * Adapter spinner select
     * @return
     */
    private ArrayAdapter selectSpinner(){
        String[] cities = getResources().getStringArray(R.array.users_type);
        spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, cities);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return spinnerAdapter;
    }

    /**
     * Action for the "Back"
     */
    @Override
    public void onBackPressed() {
        Log.d("My", "On Back Pressed");
        super.onBackPressed();
        try {
            startActivity(new Intent(LoginActivity.this, WorkerMainActivity.class));
            finish();
        }
        catch (Exception e) {}
    }
}
