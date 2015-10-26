package ru.javaapp.workmanagement.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import ru.javaapp.workmanagement.R;
import ru.javaapp.workmanagement.WorkerMainActivity;
import ru.javaapp.workmanagement.jsons.JSONAuthorize;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        if(isAuthorize){
            startActivity(new Intent(LoginActivity.this, WorkerMainActivity.class));
            finish();
        }
        else {
            componentsInitialize(); //init components in activity
        }
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
                    Intent workerIntent = new Intent(LoginActivity.this, WorkerMainActivity.class);
                    startActivity(workerIntent);
                    finish();
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

    public class JsonAuthorize extends AsyncTask<String, String, JSONObject> {

        JSONObject object;

        @Override
        protected JSONObject doInBackground(String... params) {

            try {
                JSONAuthorize auth = new JSONAuthorize(role);
                object =  auth.makeHttpRequest();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return object;
        }

        protected void onPostExecute(JSONObject json) {
            try {
                JSONObject json_data = json;
                if(role.equals("Работник")){
                    name = json_data.getString("name");
                    surname = json_data.getString("surname");
                    sessionKey = json_data.getString("sessionKey");
                }
                else {
                    name = json_data.getString("name");
                    sessionKey = json_data.getString("sessionKey");
                }
                if(name.equals("")){
                    Toast.makeText(getApplicationContext(), " ", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Имя: " + name + " " + surname, Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
}
