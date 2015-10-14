package ru.javaapp.workmanagement.master;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import ru.javaapp.workmanagement.R;

public class TrackingActivity extends AppCompatActivity {

    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        text = (TextView)findViewById(R.id.text);

    }

}
