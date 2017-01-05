package com.zebra.rondaprf;

/**
 * Created by Fabio on 25/09/16.
 */


    import android.app.TimePickerDialog;
    import android.app.Dialog;
    import android.os.Bundle;
    import android.support.v4.app.DialogFragment;
    import android.widget.TimePicker;
    import android.widget.EditText;//TextView;
    import android.text.format.DateFormat;

    import java.util.Calendar;
    import java.util.Locale;

/**
     * Created by Fabio on 25/09/16.
     */
    public class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            int mHour = c.get(Calendar.HOUR_OF_DAY);
            int mMinute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, mHour, mMinute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        // Launch Time Picker Dialog
        public void onTimeSet(TimePicker view, int mHour, int mMinute) {
            EditText mHoras = (EditText) getActivity().findViewById(R.id.editText_time);
            mHoras.setText("");
            mHoras.setText(String.format(Locale.US,"%02d",view.getCurrentHour()) + ":" + String.format(Locale.US,"%02d",view.getCurrentMinute()) );

        }
    }
