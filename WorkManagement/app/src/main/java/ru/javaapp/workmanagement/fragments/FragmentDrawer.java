package ru.javaapp.workmanagement.fragments;


import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ru.javaapp.workmanagement.R;
import ru.javaapp.workmanagement.activities.LoginActivity;
import ru.javaapp.workmanagement.activities.TaskListActivity;
import ru.javaapp.workmanagement.master.MasterMainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDrawer extends Fragment {


    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View containerView;
    private Button exitButton, authButton;

    public FragmentDrawer() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating View layout
        final View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        authButton = (Button) layout.findViewById(R.id.button_auth);
        exitButton = (Button) layout.findViewById(R.id.button_exit);

        clickListener(); // action to clicking buttons
        return layout;
    }

    private void clickListener() {
        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                startActivity(loginIntent);
                mDrawerLayout.closeDrawer(containerView);
            }
        });
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }
}
