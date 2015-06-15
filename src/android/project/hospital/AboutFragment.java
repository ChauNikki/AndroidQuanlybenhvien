package android.project.hospital;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.project.hospital.model.MyReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Toast;

public class AboutFragment extends Fragment {
	CalendarView calendar;

	public AboutFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_about, container,
				false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		getActivity().setProgressBarIndeterminateVisibility(true);
		getActivity().setProgressBarVisibility(true);
		initializeCalendar();
	}

	public void initializeCalendar() {
		calendar = (CalendarView) getActivity().findViewById(R.id.calendar);

		// sets whether to show the week number.
		calendar.setShowWeekNumber(false);

		Toast.makeText(getActivity().getApplicationContext(), "",
				Toast.LENGTH_LONG).show();
		// sets the first day of week according to Calendar.
		// here we set Monday as the first day of the Calendar
		calendar.setFirstDayOfWeek(2);

		// The background color for the selected week.
		// calendar.setSelectedWeekBackgroundColor(getResources().getColor(
		// R.color.green));

		// sets the color for the dates of an unfocused month.
		calendar.setUnfocusedMonthDateColor(getResources().getColor(
				R.color.transparent));

		// sets the color for the separator line between weeks.
		calendar.setWeekSeparatorLineColor(getResources().getColor(
				R.color.transparent));

		// sets the color for the vertical bar shown at the beginning and at the
		// end of the selected date.
		// sets the listener to be notified upon selected date change.
		calendar.setOnDateChangeListener(new OnDateChangeListener() {
			// show the selected date as a toast
			@Override
			public void onSelectedDayChange(CalendarView view, int year,
					int month, int day) {

//				Toast.makeText(getActivity().getApplicationContext(),
//						day + "/" + month + "/" + year, Toast.LENGTH_LONG)
//						.show();
				showNotification();
			}
		});
	}

	public void showNotification() {

		Calendar calendar = Calendar.getInstance();
		Toast.makeText(getActivity().getApplicationContext(),
				calendar.getTime()+"", Toast.LENGTH_SHORT)
				.show();
		//calendar.set(Calendar.HOUR_OF_DAY, 22);
		calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE)-1);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.AM_PM, Calendar.PM);

		Toast.makeText(getActivity().getApplicationContext(),
				calendar.getTime()+"", Toast.LENGTH_SHORT)
				.show();
		Intent myIntent = new Intent(getActivity(), MyReceiver.class);
		PendingIntent  pendingIntent = PendingIntent.getBroadcast(getActivity(), 0,
				myIntent, 0);
		Log.e("test intent",myIntent.getData()+"");
		getActivity().getApplicationContext();
		AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
				pendingIntent);
		//alarmManager.cancel(pendingIntent);
	}

}
