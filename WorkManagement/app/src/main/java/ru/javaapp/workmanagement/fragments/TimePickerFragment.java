package ru.javaapp.workmanagement.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by User on 01.10.2015.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    int tvTimeR;

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);


        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public TimePickerFragment(int time) {
        this.tvTimeR = time;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView tvInputtimeBefore = (TextView) getActivity().findViewById(tvTimeR);
        String time = pad(hourOfDay) + ":" + pad(minute);
        tvInputtimeBefore.setText(time);

    }

    private String pad(int value) {
        if (value < 10) {
            return "0"+value;
        }
        return ""+value;
    }
}
