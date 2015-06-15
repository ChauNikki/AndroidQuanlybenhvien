package android.project.hospital.model;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SessionManager {

	Hospital hospital;

	Context _context;

	private static String USER_NAME = "";

	// User name (make variable public to access from outside)
	public static final String KEY_NAME = "username";

	// Email address (make variable public to access from outside)
	public static final String KEY_PASS = "password";

	// Constructor

	public SessionManager(Context context) {
		this._context = context;
		hospital = new Hospital(context);
	}

	/**
	 * Create login session
	 * */
	public void createLoginSession(String name, String email) {

	}

	/**
	 * Create active
	 * */
	public boolean login(String username) {
		if (hospital.login(username) > 0) {
			USER_NAME = username;
			return true;
		}
		return false;
	}

	/**
	 * Get stored session data
	 * */
	public String getUserDetails() {
		return USER_NAME;
	}

	/**
	 * Clear session details
	 * */
	public void logoutUser() {
		// Clearing all data from Shared Preferences
		deleteRemindPrescription();
		deleteRemindAppointment();
		hospital.logout(USER_NAME);
	}

	private void deleteRemindPrescription() {
		ArrayList<Prescription> arrayPrescription = hospital
				.getPrescription(USER_NAME);
		int lieudung = 1;
		for (int i = 1; i < arrayPrescription.size(); i++) {
			int a = Integer.parseInt(arrayPrescription.get(0).getLieuDung()
					.split(" ")[1]);
			if (a > lieudung)
				lieudung = a;
		}
		for (int i = 0; i < lieudung; i++) {

			Calendar calendar = Calendar.getInstance();
			int hour = (6 * (i + 1));
			calendar.set(Calendar.HOUR_OF_DAY, hour);

			Log.e("test", "time REMIND " + calendar.getTime() + "/" + lieudung
					+ "/" + calendar.get(Calendar.HOUR_OF_DAY));

			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			int am_pm = (hour < 12) ? 0 : 1;
			calendar.set(Calendar.AM_PM, am_pm);

			Intent myIntent = new Intent(_context, MyReceiver.class);
			Bundle bundle = new Bundle();

			bundle.putString("Title", "Thông Báo");
			bundle.putString("Mess", "Đã tới giờ uống thuốc");
			bundle.putInt("Code", i);
			bundle.putInt("Fragment", 2);
			myIntent.putExtras(bundle);

			PendingIntent pendingIntent = PendingIntent.getBroadcast(_context,
					i, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			AlarmManager alarmManager = (AlarmManager) _context
					.getSystemService(Context.ALARM_SERVICE);

			alarmManager.cancel(pendingIntent);
		}
	}

	private void deleteRemindAppointment() {
		Appointment appointment = hospital.getAppointment(USER_NAME);

		if (appointment.isRemind()) {
			String[] t = appointment.getTime().split(":");
			int hour = Integer.parseInt(t[0]);
			int minute = Integer.parseInt(t[1]);
			Calendar calendar = Calendar.getInstance();
			String[] d = appointment.getNgayKB().split("/");

			int year = Integer.parseInt(d[2]);
			int month = Integer.parseInt(d[1]) - 1;
			int day = Integer.parseInt(d[0]);
			calendar.set(year, month, day);

			calendar.set(Calendar.HOUR_OF_DAY, hour);
			calendar.set(Calendar.MINUTE, minute);
			calendar.set(Calendar.SECOND, 0);
			int am_pm = (hour < 12) ? 0 : 1;
			calendar.set(Calendar.AM_PM, am_pm);
			Intent myIntent = new Intent(_context, MyReceiver.class);
			Bundle bundle = new Bundle();

			bundle.putString("Title", "Thông Báo");
			bundle.putString("Mess", "Hôm nay bạn có lịch khám");
			bundle.putInt("Code", 0);
			bundle.putInt("Fragment", 3);
			myIntent.putExtras(bundle);

			PendingIntent pendingIntent = PendingIntent.getBroadcast(_context,
					0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			AlarmManager alarmManager = (AlarmManager) _context
					.getSystemService(Context.ALARM_SERVICE);

			alarmManager.cancel(pendingIntent);
		}
	}

	/**
	 * Quick check for login
	 * **/
	// Get Login State
	public boolean isLoggedIn() {
		USER_NAME = hospital.checkLogin();
		if (hospital.checkLogin().length() > 0)
			return true;
		else
			return false;
	}
}