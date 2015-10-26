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

import ru.javaapp.workmanagement.WorkerMainActivity;
import ru.javaapp.workmanagement.R;
import ru.javaapp.workmanagement.jsons.Transmission;
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
    private String role, name;
    public static String sessionKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        componentsInitialize(); //init components in activity
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
                    if (!checkFields()) {
                        new AuthorizeAsyncTask().execute();
                    }
                    else{
                        return;
                    }
                }

                if (usersType.equals("Руководитель")) {
                    role = "Руководитель";
                    if (!checkFields()) {
                        new AuthorizeAsyncTask().execute();
                    }
                    else{
                        return;
                    }
                }
            }
        });
    }

    private boolean checkFields(){
        if(inputLogin.getText().toString().trim().length() == 0 ||
                inputPassword.getText().toString().trim().length() == 0){
            Toast.makeText(getApplicationContext(), "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show();
            inputPassword.getText().clear();
            inputLogin.getText().clear();

            return true;
        }
        else{
            return false;
        }
    }

    private class AuthorizeAsyncTask extends AsyncTask<Void, Void, String> {

        String login = inputLogin.getText().toString();
        String password = inputPassword.getText().toString();

        @Override
        protected String doInBackground(Void... urls) {
            Transmission responce = new Transmission();
            return responce.DoAuthorize(login, password, role);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject json_data = new JSONObject(result);
                name = json_data.getString("name");
                sessionKey = json_data.getString("sessionKey");
                if(!name.equals("")) {
                    isAuthorize = true;
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), R.string.no_find_worker, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            if (isAuthorize) {
                if(role.equals("Работник")) {
                    startActivity(new Intent(getApplicationContext(), WorkerMainActivity.class));
                    finish();
                }
                else{
                    startActivity(new Intent(getApplicationContext(), MasterMainActivity.class));
                    finish();
                }

            }
            else{
                return;
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
