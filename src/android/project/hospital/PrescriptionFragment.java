package android.project.hospital;

import java.util.ArrayList;
import java.util.Calendar;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.project.hospital.adapter.PrescriptionAdapter;
import android.project.hospital.model.Hospital;
import android.project.hospital.model.MyReceiver;
import android.project.hospital.model.Prescription;
import android.project.hospital.model.SessionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class PrescriptionFragment extends Fragment {
	final String URL = "http://hospitalserver.somee.com/bvAndroidWebService.asmx?WSDL";
	ListView lvDonThuoc = null;
	CheckBox remind = null;
	ArrayList<Prescription> arrayPrescription = new ArrayList<Prescription>();

	PrescriptionAdapter adapter = null;

	AsyncCallWS check;
	ProgressBar progressBar = null;
	SessionManager session;
	Hospital hospital;
	static AlertDialog alertDialog = null;
	private static int total = 0;
	Calendar calendar;
	AlarmManager alarmManager;
	PendingIntent pendingIntent;

	public PrescriptionFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_prescription,
				container, false);

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		session = new SessionManager(getActivity().getApplicationContext());
		hospital = new Hospital(getActivity().getApplicationContext());
		remind = (CheckBox) getActivity().findViewById(R.id.chkRemind);
		remind.setChecked(false);

		remind.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				setAlarm();
			}
		});
		alertDialog = new AlertDialog.Builder(getActivity()).create();
		progressBar = (ProgressBar) getActivity().findViewById(
				R.id.activity_bar);
		progressBar.setVisibility(View.GONE);
		lvDonThuoc = (ListView) getActivity().findViewById(R.id.lvThuoc);
		
		arrayPrescription = hospital.getPrescription(session
				.getUserDetails());

		adapter = new PrescriptionAdapter(getActivity(),
				R.layout.listview_prescription, arrayPrescription);
		lvDonThuoc.setAdapter(adapter);
		
		
		if (arrayPrescription.size() < 1)
			setDialog("Thông báo ", "Bạn không có đơn thuốc");
		check = new AsyncCallWS();
		check.execute();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (check != null && check.getStatus() != AsyncTask.Status.FINISHED) {
			check.onCancelled();
		}

	}

	@Override
	public void onStop() {
		super.onStop();
		if (check != null && check.getStatus() != AsyncTask.Status.FINISHED) {
			check.onCancelled();
		}
	}

	private class AsyncCallWS extends
			AsyncTask<Prescription, Prescription, ArrayList<Prescription>> {

		private boolean running = true;

		private boolean error = false;

		@Override
		protected void onCancelled() {
			running = false;
		}

		@Override
		protected ArrayList<Prescription> doInBackground(Prescription... params) {
			ArrayList<Prescription> array = new ArrayList<Prescription>();
			try {
				final String NAMESPACE = "http://tempuri.org/";

				final String METHOD_NAME = "Laydonthuoc";
				final String SOAP_ACTION = NAMESPACE + METHOD_NAME;

				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

				PropertyInfo chuyenMonPI = new PropertyInfo();

				chuyenMonPI.setName("userName");
				chuyenMonPI.setValue(session.getUserDetails());
				chuyenMonPI.setType(int.class);
				request.addProperty(chuyenMonPI);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);

				MarshalFloat marshal = new MarshalFloat();
				marshal.register(envelope);

				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.call(SOAP_ACTION, envelope);
				SoapObject soapArray;
				try {
					soapArray = (SoapObject) envelope.getResponse();
				} catch (Exception e) {
					return array;
				}

				if (total == 0)
					total = soapArray.getPropertyCount();

				for (int i = 0; i < soapArray.getPropertyCount(); i++) {

					if (!running)
						break;

					SoapObject soapItem = (SoapObject) soapArray.getProperty(i);

					Prescription donthuoc = new Prescription();

					donthuoc.setMaDungThuoc(Integer.parseInt(soapItem
							.getProperty("MaDungThuoc").toString()));

					donthuoc.setMaKhamBenh(Integer.parseInt(soapItem
							.getProperty("MaKhamBenh").toString()));

					donthuoc.setLieuDung(soapItem.getProperty("LieuDung")
							.toString());

					donthuoc.setThuoc(soapItem.getProperty("Thuoc").toString());

					donthuoc.setSoluong(Integer.parseInt(soapItem.getProperty(
							"Soluong").toString()));

					donthuoc.setSoluongdung(Integer.parseInt(soapItem
							.getProperty("Soluongdung").toString()));

					donthuoc.setTaiKhoan(session.getUserDetails());

					array.add(donthuoc);
					
					if (!hospital.checkPrescription(donthuoc.getMaDungThuoc()))
						hospital.insertPrescription(donthuoc);
					
					publishProgress(donthuoc);
				}
			} catch (Exception e) {
				error = true;
			}
			return array;
		}

		@Override
		protected void onPreExecute() {
			progressBar.setProgress(5);
			progressBar.setVisibility(View.VISIBLE);
			remind.setEnabled(false);
			arrayPrescription = new ArrayList<Prescription>();
		}

		@Override
		protected void onProgressUpdate(Prescription... values) {
			super.onProgressUpdate(values);
			if (running) {
				arrayPrescription.add(values[0]);
				if (total > 0) {
					progressBar.setProgress(adapter.getCount() * 100 / total);
				}
				adapter.notifyDataSetChanged();
			}
		}

		@Override
		protected void onPostExecute(ArrayList<Prescription> result) {

			super.onPostExecute(result);
			if (running) {
				if (error) {
					setDialog("Lỗi...",
							"Không thể cập nhật đơn thuốc kiểm tra kết nối mạng");
				} else {
					if (result.size() < 1) {
						arrayPrescription = hospital.getPrescription(session
								.getUserDetails());
						if (arrayPrescription.size() < 1)
							setDialog("Thông báo ", "Bạn không có đơn thuốc mới");
						else
							adapter.notifyDataSetChanged();
					} else {
						hospital.deletePrescription(result.get(0)
								.getMaKhamBenh());
						remind.setEnabled(true);
					}
				}
				progressBar.setProgress(100);
				progressBar.setVisibility(View.GONE);
			}
		}

	}

	@SuppressWarnings("deprecation")
	private void setDialog(String title, String mess) {
		alertDialog.setTitle(title);
		alertDialog.setMessage(mess);
		alertDialog.setButton("Đồng Ý", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();
			}
		});
		alertDialog.show();
	}

	private void setAlarm() {
		int lieudung = 1;
		for (int i = 1; i < arrayPrescription.size(); i++) {
			int a = Integer.parseInt(arrayPrescription.get(0).getLieuDung()
					.split(" ")[1]);
			if (a > lieudung)
				lieudung = a;
		}
		for (int i = 0; i < lieudung; i++) {

			calendar = Calendar.getInstance();
			int hour = (6 * (i + 1));
			calendar.set(Calendar.HOUR_OF_DAY, hour);

			Log.e("test", "time REMIND " + calendar.getTime() + "/" + lieudung
					+ "/" + calendar.get(Calendar.HOUR_OF_DAY));

			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			int am_pm = (hour < 12) ? 0 : 1;
			calendar.set(Calendar.AM_PM, am_pm);

			Intent myIntent = new Intent(getActivity(), MyReceiver.class);
			Bundle bundle = new Bundle();

			bundle.putString("Title", "Thông Báo");
			bundle.putString("Mess", "Đã tới giờ uống thuốc");
			bundle.putInt("Code", i);
			bundle.putInt("Fragment", 2);
			myIntent.putExtras(bundle);

			pendingIntent = PendingIntent.getBroadcast(getActivity(), i,
					myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			alarmManager = (AlarmManager) getActivity().getSystemService(
					Context.ALARM_SERVICE);
			if (remind.isChecked()) {

				alarmManager.cancel(pendingIntent);
				alarmManager.set(AlarmManager.RTC_WAKEUP,
						calendar.getTimeInMillis(), pendingIntent);

			} else {
				alarmManager.cancel(pendingIntent);
			}
		}
	}
}
