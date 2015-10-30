package ru.javaapp.workmanagement.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

import ru.javaapp.workmanagement.R;

/**
 * Created by User on 01.10.2015.
 */
public class DatePickerFragmentBefore extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        TextView tvInputDateBefore = (TextView) getActivity().findViewById(R.id.tv_input_dateBefore);
        String date = year + "-" + plug(monthOfYear + 1) + "-" + plug(dayOfMonth);

        tvInputDateBefore.setText(date);
    }
    private String plug(int value) {
        if (value < 10) {
            return "0"+value;
        }
        return ""+value;
    }

}
