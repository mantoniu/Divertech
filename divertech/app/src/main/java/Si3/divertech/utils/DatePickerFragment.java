package Si3.divertech.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.util.Locale;

import Si3.divertech.R;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    protected FragmentManager test;
    protected boolean time;
    private final View view;
    private final DateListener listener;

    public DatePickerFragment(FragmentManager test, boolean time, View view, DateListener listener) {
        this.test = test;
        this.time = time;
        this.view = view;
        this.listener = listener;
    }

    public DatePickerFragment(FragmentManager test, boolean time, View view) {
        this(test, time, view, null);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker.
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it.
        return new DatePickerDialog(requireContext(), this, year, month, day);
        //return new DatePickerDialog(requireContext(),R.style.ThemeOverlay_App_DatePicker, this, year, month, day);
    }

    public void onDateSet(android.widget.DatePicker view, int year, int month, int day) {
        // Do something with the date the user picks.
        if (time) {
            TimePickerFragment timeFragment = new TimePickerFragment(year, month, day, listener);
            timeFragment.show(this.test, "timePicker");
        } else {
            TextView textCalendar = this.view.findViewById(R.id.add_calendar);
            textCalendar.setText(String.format(Locale.FRANCE, "%02d/%02d/%04d", day, month, year));
            this.view.findViewById(R.id.add_calendar).setVisibility(View.VISIBLE);
            textCalendar.setTextColor(getResources().getColor(R.color.black, view.getContext().getTheme()));
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone((ConstraintLayout) this.view.findViewById(R.id.calendar_constraint_layout));
            constraintSet.connect(R.id.date, ConstraintSet.TOP, R.id.calendar_constraint_layout, ConstraintSet.TOP, 40);
            constraintSet.connect(R.id.date, ConstraintSet.BOTTOM, R.id.add_calendar, ConstraintSet.TOP);
            constraintSet.applyTo(this.view.findViewById(R.id.calendar_constraint_layout));
        }
    }


    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        private final int year;
        private final int month;
        private final int day;
        private final DateListener listener;

        public TimePickerFragment(int year, int month, int day, DateListener listener) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.listener = listener;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker.
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of DatePickerDialog and return it.
            return new TimePickerDialog(requireContext(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
            //return new DatePickerDialog(requireContext(),R.style.ThemeOverlay_App_DatePicker, this, year, month, day);
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            listener.onDateChoose(day, month + 1, year, hourOfDay, minute);
        }
    }
}
