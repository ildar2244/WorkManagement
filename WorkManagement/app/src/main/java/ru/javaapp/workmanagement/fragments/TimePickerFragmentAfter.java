package ru.javaapp.workmanagement.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import ru.javaapp.workmanagement.R;

/**
 * Created by User on 05.10.2015.
 */
public class TimePickerFragmentAfter extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);


        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView tvInputtimeBefore = (TextView) getActivity().findViewById(R.id.tv_input_timeAfter);
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
