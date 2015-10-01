package ru.javaapp.workmanagement.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ru.javaapp.workmanagement.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDrawer extends Fragment {

    private TextView userName;
    private Button exitButton;

    public FragmentDrawer() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating View layout
        final View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        userName = (TextView) layout.findViewById(R.id.tv_user_name);
        exitButton = (Button) layout.findViewById(R.id.button_exit);
        return layout;
    }
}
