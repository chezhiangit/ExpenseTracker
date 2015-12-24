package paramtech.com.exptracker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by 324590 on 12/1/2015.
 */
public class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    Calendar c;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
         c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {
        TextView tv = (TextView)getActivity().findViewById(R.id.headerDate);

        Calendar now = new GregorianCalendar(year,month,day);
        DateFormat df = new SimpleDateFormat("MMM yyyy");
        String date = df.format(now.getTime());

        tv.setText(date);
    }

//    @Override
//    public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {
//        TextView tv = (TextView)getActivity().findViewById(R.id.headerDate);
//
//        Calendar now = new GregorianCalendar(i,i1,i2);
//        DateFormat df = new SimpleDateFormat("MMM yyyy");
//        String date = df.format(now.getTime());
//
//        tv.setText(date);
//    }
}
