package ru.javaapp.workmanagement.activities.auth;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import ru.javaapp.workmanagement.Helper;
import ru.javaapp.workmanagement.R;
import ru.javaapp.workmanagement.activities.master.MasterMainActivity;
import ru.javaapp.workmanagement.activities.worker.WorkerMainActivity;
import ru.javaapp.workmanagement.workDB.Transmission;

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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

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

        inputPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                inputPassword.requestLayout();
                LoginActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
                return false;
            }
        });

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

        ProgressDialog dialog = new ProgressDialog(LoginActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle("Обработка данных");
            dialog.setMessage("Пожалуйста, подождите...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Отмена", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    cancel(true);
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... urls) {
            if(Helper.isConnected(getApplicationContext())) {
                Transmission responce = new Transmission();
                return responce.DoAuthorize(login, password, role);
            }
            else
            {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if(result == null){
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this,  R.style.AlertDialogStyle);
                builder.setCancelable(false);
                builder.setTitle("Ошибка");
                builder.setMessage("Нет соединения с интернетом.");
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // Кнопка ОК
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Отпускает диалоговое окно
                    }
                });
                builder.show();
                dialog.dismiss();
                return;
            }
            else {
                try {
                    JSONObject json_data = new JSONObject(result);
                    name = json_data.getString("name");
                    sessionKey = json_data.getString("sessionKey");
                    if (!name.equals("")) {
                        isAuthorize = true;
                    }
                } catch (JSONException e) {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), R.string.no_find_worker, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                if (isAuthorize) {
                    if (role.equals("Работник")) {
                        startActivity(new Intent(getApplicationContext(), WorkerMainActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(getApplicationContext(), MasterMainActivity.class));
                        finish();
                    }

                } else {
                    return;
                }
            }

            dialog.dismiss();
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
