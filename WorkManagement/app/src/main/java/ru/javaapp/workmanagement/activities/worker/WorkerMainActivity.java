package ru.javaapp.workmanagement.activities.worker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import ru.javaapp.workmanagement.R;
import ru.javaapp.workmanagement.fragments.FragmentDrawer;

public class WorkerMainActivity extends AppCompatActivity implements View.OnClickListener {

    // v.1.1
    private Toolbar toolbar;
    private FragmentDrawer drawerFragment;
    private ImageButton btnMyTasks, btnKaidzen, btnTRM, btnKanban, btn5C, btnSmed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_main);

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

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    /**
     * Initialize all components in UI
     */
    private void componentsInitialize() {
        btnMyTasks = (ImageButton) findViewById(R.id.main_iv_task);
        btnKaidzen = (ImageButton) findViewById(R.id.main_iv_kaydzen);
        btn5C = (ImageButton) findViewById(R.id.main_iv_5c);
        btnKanban = (ImageButton) findViewById(R.id.main_iv_kanban);
        btnSmed = (ImageButton) findViewById(R.id.main_iv_smed);
        btnTRM = (ImageButton) findViewById(R.id.main_iv_trm);

        btnMyTasks.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main_iv_task:
                startActivity(new Intent(getApplicationContext(), TaskListActivity.class));
                break;
        }
    }
}
