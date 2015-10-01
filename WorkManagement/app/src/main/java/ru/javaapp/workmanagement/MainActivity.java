package ru.javaapp.workmanagement;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;

import ru.javaapp.workmanagement.fragments.FragmentDrawer;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FragmentDrawer drawerFragment;
    private EditText inputLogin;
    private EditText inputPassword;
    private Button buttonEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbarInitialize(); // init toolbar
        setNavigationDrawer(); // set navigation drawer
    }

    private void setNavigationDrawer() {
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
    }
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
}
