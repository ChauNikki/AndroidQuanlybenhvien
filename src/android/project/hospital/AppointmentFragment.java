package android.project.hospital;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.project.hospital.model.Account;
import android.project.hospital.model.Appointment;
import android.project.hospital.model.Hospital;
import android.project.hospital.model.MyReceiver;
import android.project.hospital.model.SessionManager;
import android.project.hospital.model.WebService;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AppointmentFragment extends Fragment {
	static boolean errored = false;
	private static EditText date;
	private static TextView Id;
	Button submit = null;
	Button delete = null;
	EditText symptom = null;
	Spinner shift = null;
	CheckBox remind = null;
	String arr[] = { "Sáng", "Chiều" };
	private static int day;
	private static int month;
	private static int year;
	private static int hour;
	private static int minute;
	private static Appointment appointment;
	static AlertDialog alertDialog = null;
	static AlertDialog errorDialog = null;
	AsyncCallCheckWS check;
	AsyncCallDeleteWS deleteApp;
	AsyncCallWS updateApp;
	SessionManager session;
	Hospital hospital;
	Account account;
	Calendar calendar;
	AlarmManager alarmManager;
	PendingIntent pendingIntent;

	public AppointmentFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_appointment,
				container, false);
		return rootView;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		getActivity().setProgressBarIndeterminateVisibility(true);
		getActivity().setProgressBarVisibility(true);

		session = new SessionManager(getActivity().getApplicationContext());
		hospital = new Hospital(getActivity().getApplicationContext());

		account = hospital.getAccount(session.getUserDetails());

		errorDialog = new AlertDialog.Builder(getActivity()).create();
		alertDialog = new AlertDialog.Builder(getActivity()).create();
		alertDialog.setTitle("Lỗi....");
		alertDialog
				.setMessage("Ngày đăng ký phải lớn hơn ngày hiện tại và không quá 2 tuần");
		alertDialog.setButton("Đồng Ý", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();
				showTruitonDatePickerDialog(getView());
			}
		});

		date = (EditText) getActivity().findViewById(R.id.txtDateAppointment);
		Id = (TextView) getActivity().findViewById(R.id.id);
		submit = (Button) getActivity().findViewById(R.id.btnSubmit);
		delete = (Button) getActivity().findViewById(R.id.btnDelete);
		symptom = (EditText) getActivity().findViewById(R.id.txtSymptom);
		shift = (Spinner) getActivity().findViewById(R.id.spShift);
		remind = (CheckBox) getActivity().findViewById(R.id.chkRemind);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, arr);

		adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

		shift.setAdapter(adapter);

		check = new AsyncCallCheckWS();
		check.execute(); // check mã khám bệnh
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (check != null && check.getStatus() != AsyncTask.Status.FINISHED) {
			check.onCancelled();
		}
		if (deleteApp != null
				&& deleteApp.getStatus() != AsyncTask.Status.FINISHED) {
			deleteApp.onCancelled();
		}
		if (updateApp != null
				&& updateApp.getStatus() != AsyncTask.Status.FINISHED) {
			updateApp.onCancelled();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if (check != null && check.getStatus() != AsyncTask.Status.FINISHED) {
			check.onCancelled();
		}
		if (deleteApp != null
				&& deleteApp.getStatus() != AsyncTask.Status.FINISHED) {
			deleteApp.onCancelled();
		}
		if (updateApp != null
				&& updateApp.getStatus() != AsyncTask.Status.FINISHED) {
			updateApp.onCancelled();
		}
	}

	/*
	 * Sự kiện hiện thị dialog ngày tháng
	 */
	public void showTruitonDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
	}

	public class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		@SuppressWarnings("deprecation")
		public void onDateSet(DatePicker view, int years, int months, int days) {
			Calendar c = Calendar.getInstance();
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);

			Calendar daychoose = Calendar.getInstance();
			daychoose.set(years, months, days);

			if (c.compareTo(daychoose) > 0)
				alertDialog.show();
			else {
				c.add(Calendar.DATE, 14);
				if (c.compareTo(daychoose) < 0)
					alertDialog.show();
				else {
					if (daychoose.get(Calendar.DAY_OF_WEEK) > 1) {
						date.setText(days + "/" + (months + 1) + "/" + years);
						year = years;
						month = months;
						day = days;
						return;
					} else {
						alertDialog = new AlertDialog.Builder(getActivity())
								.create();
						alertDialog.setTitle("Lỗi....");
						alertDialog
								.setMessage("Chủ nhật bệnh viện không làm việc");
						alertDialog.setButton("Đồng Ý",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										alertDialog.dismiss();
										showTruitonDatePickerDialog(getView());
									}
								});
						alertDialog.show();
					}
				}
			}
		}
	}

	public void showTruitonTimePickerDialog(View v) {
		DialogFragment newFragment = new TimePickerFragment();
		newFragment.show(getFragmentManager(), "timePicker");
	}

	public class TimePickerFragment extends DialogFragment implements
			TimePickerDialog.OnTimeSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			return new TimePickerDialog(getActivity(), this, hour, minute,
					DateFormat.is24HourFormat(getActivity()));
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
			hour = hourOfDay;
			minute = minutes;
			if (appointment.getMaKhamBenh() > 0) {
				Log.e("test", "vào set time");
				setAlarm();
			}
		}
	}

	private class AsyncCallWS extends AsyncTask<String, String, String> {

		private boolean error = false;
		private boolean running = true;

		@Override
		protected String doInBackground(String... params) {
			try {
				appointment = new Appointment();
				appointment.setMaKhamBenh(WebService.invokeAppointmentWS(
						account.getUsername(), day, (month + 1), year, symptom
								.getText().toString(), shift.getSelectedItem()
								.toString(), "DangkyKhambenh"));
			} catch (Exception e) {
				error = true;
			}
			return null;

		}

		@Override
		protected void onCancelled() {
			running = false;
		}

		@Override
		protected void onPreExecute() {
			setEnable(false);
		}

		@SuppressLint("CommitPrefEdits")
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			setEnable(true);
			if (running) {
				if (!error) {
					if (appointment.getMaKhamBenh() > 0) {

						appointment.setCa(shift.getSelectedItem().toString());
						appointment.setTaiKhoan(account.getUsername());
						appointment.setNgayKB(date.getText().toString());
						appointment.setTrieuChung(symptom.getText().toString());
						Log.e("check time check", remind.isChecked() + "");
						appointment.setRemind(remind.isChecked());
						appointment.setTime(hour + ":" + minute);

						hospital.insertAppointment(appointment);
						setAlarm();
						getActivity().recreate();
					} else {
						setDialog("Lỗi....", "Đăng ký thất bại!!!!");
					}

				} else {
					setDialog("Lỗi....", "Không thể kết nối với sever");
				}
			}
		}

		@Override
		protected void onProgressUpdate(String... values) {

		}

	}

	private class AsyncCallCheckWS extends AsyncTask<Integer, String, Integer> {

		private boolean running = true;
		private boolean check = false;

		@Override
		protected void onCancelled() {
			running = false;
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			if (!running)
				return null;

			appointment = hospital.getAppointment(session.getUserDetails());

			if (appointment.getMaKhamBenh() > 0) {
				if (WebService.invokeCheckAppointmentWS(
						appointment.getMaKhamBenh(), "KiemTraKhambenh"))
					check = true;
			} else {
				
				appointment = WebService.invokeGetAppointmentWS(
						session.getUserDetails(), "LayKhambenh");
				hospital.insertAppointment(appointment);
			}
			return null;

		}

		@Override
		protected void onPreExecute() {
			setEnable(false);
		}

		@Override
		protected void onPostExecute(Integer result) {

			super.onPostExecute(result);
			setEnable(true);
			if (running) {

				if (!check && appointment.getMaKhamBenh() > 0) {

					Id.setText("Mã số khám bệnh là "
							+ appointment.getMaKhamBenh());
					symptom.setText(appointment.getTrieuChung());
					date.setText(appointment.getNgayKB());
					String[] d = appointment.getNgayKB().split("/");
					if (appointment.isRemind()) {
						String[] t = appointment.getTime().split(":");
						hour = Integer.parseInt(t[0]);
						minute = Integer.parseInt(t[1]);
					} else {
						final Calendar c = Calendar.getInstance();
						hour = c.get(Calendar.HOUR_OF_DAY);
						minute = c.get(Calendar.MINUTE);
					}
					year = Integer.parseInt(d[2]);
					month = Integer.parseInt(d[1]) - 1;
					day = Integer.parseInt(d[0]);
					if ("Sáng".equals(appointment.getCa()))
						shift.setSelection(0);
					else
						shift.setSelection(1);

					remind.setChecked(appointment.isRemind());
					remind.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							if (isChecked) {
								showTruitonTimePickerDialog(buttonView);
							} else {
								setAlarm();
							}
						}
					});

					symptom.setFocusable(false);
					shift.setEnabled(false);
					submit.setEnabled(false);

					submit.setBackgroundResource(R.drawable.button_default_dis);

					delete.setEnabled(true);
					delete.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							AlertDialog.Builder alertDialog = new AlertDialog.Builder(
									getActivity());
							alertDialog
									.setMessage("Bạn có muốn hủy khám bệnh không?");
							alertDialog.setIcon(R.drawable.ic_launcher);
							alertDialog.setPositiveButton("Đồng ý",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											deleteApp = new AsyncCallDeleteWS();
											// Call execute
											deleteApp.execute();

										}

									});
							alertDialog.setNegativeButton("Hủy bỏ",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {

											dialog.cancel();
										}
									});
							alertDialog.show();
						}
					});
				} else {
					if (check)
						hospital.deleteAppointment(appointment.getMaKhamBenh());

					final Calendar c = Calendar.getInstance();
					year = c.get(Calendar.YEAR);
					month = c.get(Calendar.MONTH);
					day = c.get(Calendar.DAY_OF_MONTH);
					hour = c.get(Calendar.HOUR_OF_DAY);
					minute = c.get(Calendar.MINUTE);

					date.setText(day + "/" + (month + 1) + "/" + year);

					date.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							showTruitonDatePickerDialog(v);
						}
					});
					symptom.setText("");

					remind.setChecked(false);

					remind.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							if (isChecked) {
								showTruitonTimePickerDialog(buttonView);
							}

						}
					});

					delete.setEnabled(false);
					delete.setBackgroundResource(R.drawable.button_default_dis);
					submit.setEnabled(true);
					submit.setOnClickListener(new View.OnClickListener() {
						@SuppressWarnings("deprecation")
						@Override
						public void onClick(View v) {
							Calendar daychoose = Calendar.getInstance();
							daychoose.set(year, month, day);

							if (daychoose.get(Calendar.DAY_OF_WEEK) < 2) {
								alertDialog = new AlertDialog.Builder(
										getActivity()).create();
								alertDialog.setTitle("Lỗi....");
								alertDialog
										.setMessage("Chủ nhật bệnh viện không làm việc");
								alertDialog.setButton("Đồng Ý",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												alertDialog.dismiss();
												showTruitonDatePickerDialog(getView());
											}
										});
								alertDialog.show();
							} else {
								if (symptom.getText().length() < 1) {
									AlertDialog.Builder alertDialog = new AlertDialog.Builder(
											getActivity());
									alertDialog.setTitle("Cảnh báo !!!!");
									alertDialog
											.setMessage("Bạn chưa nhập triệu chứng!!! Bạn có muốn tiếp tục");
									alertDialog.setIcon(R.drawable.ic_launcher);
									alertDialog
											.setPositiveButton(
													"Đồng ý",
													new DialogInterface.OnClickListener() {

														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															updateApp = new AsyncCallWS();
															// Call execute
															updateApp.execute();
														}

													});
									alertDialog
											.setNegativeButton(
													"Hủy bỏ",
													new DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface dialog,
																int which) {
															symptom.requestFocus();
															return;
														}
													});
									alertDialog.show();
								} else {
									AsyncCallWS task = new AsyncCallWS();
									// Call execute
									task.execute();
								}

							}
						}
					});
				}
			}
		}

	}

	private class AsyncCallDeleteWS extends AsyncTask<String, String, String> {

		private boolean running = true;
		private boolean error = false;
		private boolean check = false;

		@Override
		protected String doInBackground(String... params) {

			try {
				if (!running)
					return null;
				if (appointment.getMaKhamBenh() > 0) {
					if (WebService.invokeDeleteAppointmentWS(
							appointment.getMaKhamBenh(), "XoaKhambenh"))
						check = true;
				}
			} catch (Exception e) {
				error = true;
			}
			return null;
		}

		@Override
		protected void onCancelled() {
			running = false;
		}

		@Override
		protected void onPreExecute() {
			setEnable(false);
		}

		@SuppressLint("CommitPrefEdits")
		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);
			setEnable(true);
			if (running) {
				if (!error) {
					if (check) {
						hospital.deleteAppointment(appointment.getMaKhamBenh());
						getActivity().recreate();
						Toast.makeText(getActivity(), "Hủy thành công",
								Toast.LENGTH_LONG).show();
					} else {
						setDialog("Lỗi....", "Hủy thất bại!!!!");

					}

				} else {
					setDialog("Lỗi....", "Không thể kết nối với server");
				}
			}

		}

		@Override
		protected void onProgressUpdate(String... values) {

		}

	}

	private void setAlarm() {
		calendar = Calendar.getInstance();
		calendar.set(year, month, day);

		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		int am_pm = (hour < 12) ? 0 : 1;
		calendar.set(Calendar.AM_PM, am_pm);
		Intent myIntent = new Intent(getActivity(), MyReceiver.class);
		Bundle bundle = new Bundle();

		bundle.putString("Title", "Thông Báo");
		bundle.putString("Mess", "Hôm nay bạn có lịch khám");
		bundle.putInt("Code", 0);
		bundle.putInt("Fragment", 3);
		myIntent.putExtras(bundle);

		pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, myIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager = (AlarmManager) getActivity().getSystemService(
				Context.ALARM_SERVICE);
		if (remind.isChecked()) {
			if (appointment.getMaKhamBenh() > 0)
				hospital.updateAppointment(hour + ":" + minute,
						appointment.getMaKhamBenh());
			alarmManager.cancel(pendingIntent);
			alarmManager.set(AlarmManager.RTC_WAKEUP,
					calendar.getTimeInMillis(), pendingIntent);

		} else {
			hospital.updateAppointment("", appointment.getMaKhamBenh());

			alarmManager.cancel(pendingIntent);
		}
	}

	private void setEnable(Boolean bool) {
		if (bool) {
			if (appointment.getMaKhamBenh() > 0) {
				submit.setEnabled(false);
				delete.setEnabled(true);
			} else {
				submit.setEnabled(true);
				delete.setEnabled(false);
			}
		} else {
			submit.setEnabled(bool);
			delete.setEnabled(bool);
		}
		date.setEnabled(bool);
		symptom.setEnabled(bool);
		shift.setEnabled(bool);
	}

	@SuppressWarnings("deprecation")
	private void setDialog(String title, String mess) {
		errorDialog.setTitle(title);
		errorDialog.setMessage(mess);
		errorDialog.setButton("Đồng Ý", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				errorDialog.dismiss();
			}
		});
		errorDialog.show();
	}
}
