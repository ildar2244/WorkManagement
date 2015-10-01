package ru.javaapp.workmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;
import android.widget.Spinner;

import ru.javaapp.workmanagement.activities.TaskListActivity;
import ru.javaapp.workmanagement.fragments.FragmentDrawer;
import ru.javaapp.workmanagement.master.MasterMainActivity;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FragmentDrawer drawerFragment;
    private Spinner spinnerUsers;
    private ArrayAdapter<String> spinnerAdapter = null;
    private EditText inputLogin;
    private EditText inputPassword;
    private Button buttonEnter;
    private String usersType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbarInitialize(); // init toolbar
        setNavigationDrawer(); // set navigation drawer
        componentsInitialize(); //init components in activity
    }

    /**
     * initialize navigation drawer fragment
     */
    private void setNavigationDrawer() {
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
    }

    /**
     * initialize toolbar
     */
    private void toolbarInitialize() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        catch (Exception e){
            e.printStackTrace();
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
                    Intent workerIntent = new Intent(MainActivity.this, TaskListActivity.class);
                    startActivity(workerIntent);
                }
                if (usersType.equals("Руководитель")) {
                    Intent masterIntent = new Intent(MainActivity.this, MasterMainActivity.class);
                    startActivity(masterIntent);
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
}
