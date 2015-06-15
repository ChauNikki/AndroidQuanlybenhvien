package android.project.hospital;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.project.hospital.model.Account;
import android.project.hospital.model.Hospital;
import android.project.hospital.model.SessionManager;
import android.project.hospital.model.WebService;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ProfileFragment extends Fragment {
	static boolean errored = false;
	String username;
	private static EditText date;
	Button submit = null;
	Button logout = null;
	EditText fullname = null;
	EditText phonenumber = null;
	EditText address = null;
	Spinner sex = null;
	String arr[] = { "Nam", "Nữ" };
	private static int day;
	private static int month;
	private static int year;

	static AlertDialog alertDialog = null;
	static AlertDialog errorDialog = null;

	SessionManager session;
	Hospital hospital;
	private static Account account;
	AsyncCallWS updateAccount;

	public ProfileFragment() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_profile, container,
				false);
		return rootView;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		session = new SessionManager(getActivity().getApplicationContext());
		hospital = new Hospital(getActivity().getApplicationContext());
		errorDialog = new AlertDialog.Builder(getActivity()).create();
		alertDialog = new AlertDialog.Builder(getActivity()).create();
		alertDialog.setTitle("Lỗi...");
		alertDialog.setMessage("Ngày sinh không hợp lệ");
		alertDialog.setButton("Đồng ý", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();
				showTruitonDatePickerDialog(getView());
			}
		});

		username = session.getUserDetails();
		account = hospital.getAccount(username);

		sex = (Spinner) getActivity().findViewById(R.id.spSex);
		date = (EditText) getActivity().findViewById(R.id.txtBirthday);
		submit = (Button) getActivity().findViewById(R.id.btnChangeProfile);
		logout = (Button) getActivity().findViewById(R.id.btnLogout);
		fullname = (EditText) getActivity().findViewById(R.id.txtFullname);
		phonenumber = (EditText) getActivity()
				.findViewById(R.id.txtPhonenumber);
		address = (EditText) getActivity().findViewById(R.id.txtAddress);

		if ("true".equals(account.getStatus())) {
			fullname.setText(account.getFullname());
			address.setText(account.getAddress());
			if (account.getSex().equals("Nam")) {
				sex.setSelection(0);
			} else
				sex.setSelection(1);
			date.setText(account.getBirthday());

			String[] c = account.getBirthday().split("/");
			Log.e("test ngày tháng", c[0] + c[1] + c[2]);
			year = Integer.parseInt(c[2]);
			month = Integer.parseInt(c[1]) - 1;
			day = Integer.parseInt(c[1]);

			phonenumber.setText(account.getPhonenumber());

		} else {
			setDialog("Thông báo", "Tài khoản chưa cập nhật thông tin cá nhân");
			Calendar c = Calendar.getInstance();
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);
			// GET CURRENT DATE
			date.setText(day + "/" + (month + 1) + "/" + year);
		}
		date.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showTruitonDatePickerDialog(v);
			}
		});

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, arr);

		adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

		sex.setAdapter(adapter);

		logout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder alertDialogs = new AlertDialog.Builder(
						getActivity());
				alertDialogs.setTitle("Cảnh báo !!!!");
				alertDialogs.setMessage("Bạn có muốn đăng xuất");
				alertDialogs.setPositiveButton("Đồng ý",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								session.logoutUser();
								Intent intent = new Intent(getActivity(),
										MainActivity.class);
								startActivity(intent);
							}

						});
				alertDialogs.setNegativeButton("Hủy bỏ",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								dialog.cancel();
							}
						});
				alertDialogs.show();

			}

		});
		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				AlertDialog.Builder alertDialogs = new AlertDialog.Builder(
						getActivity());
				alertDialogs.setTitle("Cảnh báo !!!!");
				alertDialogs.setMessage("Bạn có muốn cập nhật tài khoản");
				//alertDialogs.setIcon(R.drawable.ic_launcher);
				alertDialogs.setPositiveButton("Đồng ý",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (fullname.getText().length() > 1
										&& phonenumber.getText().length() > 1
										&& address.getText().length() > 1) {
									if (phonenumber.getText().length() > 9
											&& phonenumber.getText().length() < 12) {
										Calendar daychoose = Calendar
												.getInstance();
										daychoose.set(year, month, day);
										Calendar c = Calendar.getInstance();
										year = c.get(Calendar.YEAR);
										month = c.get(Calendar.MONTH);
										day = c.get(Calendar.DAY_OF_MONTH);
										if (c.compareTo(daychoose) < 0) {
											year = daychoose.get(Calendar.YEAR);
											month = daychoose
													.get(Calendar.MONTH);
											day = daychoose
													.get(Calendar.DAY_OF_MONTH);
											alertDialog.show();
										} else {
											updateAccount = new AsyncCallWS();
											// Call execute
											updateAccount.execute();
										}

									} else {
										Toast.makeText(getActivity(),
												"Số điện thoại không đúng",
												Toast.LENGTH_LONG).show();
									}
								} else
									Toast.makeText(
											getActivity(),
											"Các trường giá trị không được bỏ trống",
											Toast.LENGTH_LONG).show();
							}
						});
				alertDialogs.setNegativeButton("Hủy bỏ",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								dialog.cancel();
							}
						});
				alertDialogs.show();
			}
		});

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (updateAccount != null
				&& updateAccount.getStatus() != AsyncTask.Status.FINISHED) {
			updateAccount.onCancelled();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if (updateAccount != null
				&& updateAccount.getStatus() != AsyncTask.Status.FINISHED) {
			updateAccount.onCancelled();
		}
	}

	public void showTruitonDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
	}

	public static class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker view, int years, int months, int days) {
			Calendar c = Calendar.getInstance();
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);

			Calendar daychoose = Calendar.getInstance();
			daychoose.set(years, months, days);

			if (c.compareTo(daychoose) < 0) {
				alertDialog.show();
			} else {

				date.setText(days + "/" + (months + 1) + "/" + years);
				year = years;
				month = months;
				day = days;
				return;
			}
		}
	}

	private class AsyncCallWS extends AsyncTask<String, String, String> {

		Boolean check = false;
		private boolean running = true;

		@Override
		protected void onCancelled() {
			running = false;
		}

		@Override
		protected String doInBackground(String... params) {
			if (running) {

				check = WebService.updateProfile(username, fullname.getText()
						.toString(), day + "", month + 1 + "", year + "", sex
						.getSelectedItem().toString(), phonenumber.getText()
						.toString(), address.getText().toString(),
						"UpdateProfile");

			}
			return null;

		}

		@Override
		protected void onPreExecute() {
			setEnable(false);
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);
			setEnable(true);
			if (!errored) {
				if (check) {
					Toast.makeText(getActivity(),
							"Cập nhật thông tin thành công", Toast.LENGTH_LONG)
							.show();
					account = new Account();
					account.setAddress(address.getText().toString());
					account.setBirthday(day + "/" + (month + 1) + "/" + year);
					account.setFullname(fullname.getText().toString());
					account.setPhonenumber(phonenumber.getText().toString());
					account.setSex(sex.getSelectedItem().toString());
					account.setUsername(username);
					hospital.updateAccount(account);

				} else {
					setDialog("Lỗi...", "Cập nhật thông tin thất bại!!!");
				}

			} else {

				setDialog("Lỗi...", "Không thể kết nối với server!!!");
			}
			errored = false;
		}

		@Override
		protected void onProgressUpdate(String... values) {
		}

	}

	private void setEnable(Boolean bool) {
		date.setEnabled(bool);
		submit.setEnabled(bool);
		fullname.setEnabled(bool);
		phonenumber.setEnabled(bool);
		address.setEnabled(bool);
		;
		sex.setEnabled(bool);
	}

	@SuppressWarnings("deprecation")
	private static void setDialog(String title, String mess) {
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
