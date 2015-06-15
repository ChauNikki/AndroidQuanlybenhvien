package android.project.hospital;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.project.hospital.model.Specialize;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

public class DoctorFragment extends Fragment {

	final String URL = "http://hospitalserver.somee.com/bvAndroidWebService.asmx?WSDL";
	ArrayList<String> arrBacsi = new ArrayList<String>();
	ArrayList<Specialize> arrChuyenMon = new ArrayList<Specialize>();
	ArrayAdapter<String> adapter = null;
	ArrayAdapter<Specialize> spinner = null;
	ListView dsBacsi = null;
	Spinner chuyenMon = null;

	ProgressBar progressBar = null;
	private static int total = 0;
	private AsyncCallWS getBacsi = null;
	private ChuyenMonWS getChuyenMon = null;

	static AlertDialog alertDialog = null;

	public DoctorFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_doctor, container,
				false);

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		getActivity().setProgressBarIndeterminateVisibility(true);
		getActivity().setProgressBarVisibility(true);

		alertDialog = new AlertDialog.Builder(getActivity()).create();
		progressBar = (ProgressBar) getActivity().findViewById(
				R.id.activity_bar);
		progressBar.setVisibility(View.GONE);

		// khai bao listview bác sĩ
		dsBacsi = (ListView) getActivity().findViewById(R.id.lvDoctor);
		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, arrBacsi);
		dsBacsi.setAdapter(adapter);
		dsBacsi.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Fragment fragment = new AboutFragment();
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.frame_container, fragment).commit();
			}

		});
		// khởi tạo giá trị ban đầu cho spriner chuyên môn
		Specialize sp = new Specialize("0", "Chọn chuyên môn");
		arrChuyenMon.add(sp);
		chuyenMon = (Spinner) getActivity().findViewById(R.id.spChuyenMon);
		spinner = new ArrayAdapter<Specialize>(getActivity(),
				android.R.layout.simple_list_item_1, arrChuyenMon);
		spinner.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

		chuyenMon.setAdapter(spinner);

		chuyenMon.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {

				if (position > 0) {
					if (getBacsi != null
							&& getBacsi.getStatus() != AsyncTask.Status.FINISHED) {
						getBacsi.onCancelled();
					}

					Specialize sp = spinner.getItem(position);

					// gọi AsyncTask lấy danh sách bác sĩ từ webservice
					getBacsi = new AsyncCallWS(getActivity());
					getBacsi.execute(sp.getMaChuyenMon());
				} else {
					adapter.clear();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		// gọi AsyncTask lấy danh sách chuyên môn từ webservice
		getChuyenMon = new ChuyenMonWS();
		getChuyenMon.execute();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (getChuyenMon != null
				&& getChuyenMon.getStatus() != AsyncTask.Status.FINISHED) {
			getChuyenMon.onCancelled();
		}
		if (getBacsi != null
				&& getBacsi.getStatus() != AsyncTask.Status.FINISHED) {
			getBacsi.onCancelled();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if (getChuyenMon != null
				&& getChuyenMon.getStatus() != AsyncTask.Status.FINISHED) {
			getChuyenMon.onCancelled();
		}
		if (getBacsi != null
				&& getBacsi.getStatus() != AsyncTask.Status.FINISHED) {
			getBacsi.onCancelled();
		}
	}

	private class AsyncCallWS extends
			AsyncTask<String, String, ArrayList<String>> {

		private boolean running = true;
		private boolean error = false;

		public AsyncCallWS(Activity activitys) {

		}

		@Override
		protected void onCancelled() {
			running = false;
		}

		@Override
		protected ArrayList<String> doInBackground(String... params) {
			ArrayList<String> array = new ArrayList<String>();
			String cm = params[0];
			try {
				final String NAMESPACE = "http://tempuri.org/";

				final String METHOD_NAME = "ListBasiByCM";
				final String SOAP_ACTION = NAMESPACE + METHOD_NAME;

				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

				PropertyInfo chuyenMonPI = new PropertyInfo();

				chuyenMonPI.setName("maChuyenmon");
				chuyenMonPI.setValue(cm);
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

				SoapObject soapArray = (SoapObject) envelope.getResponse();

				if (total == 0)
					total = soapArray.getPropertyCount();

				for (int i = 0; i < soapArray.getPropertyCount(); i++) {

					if (!running)
						break;

					SoapObject soapItem = (SoapObject) soapArray.getProperty(i);

					String chuyenmon = soapItem.getProperty("Hoten").toString();
					array.add(chuyenmon);
					SystemClock.sleep(100);
					publishProgress(chuyenmon);
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
			adapter.clear();
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			if (running) {
				adapter.add(values[0]);
				adapter.notifyDataSetChanged();

				if (total > 0) {
					progressBar.setProgress(adapter.getCount() * 100 / total);
				}
			}
		}

		@Override
		protected void onPostExecute(ArrayList<String> result) {

			super.onPostExecute(result);
			total = 0;
			if (running) {
				if (error)
					setDialog("Lỗi....", "Không thể kết nối với sever");
				else {
					if (result.size() < 1)
						setDialog("Thông báo", "Không tìm thấy bác sĩ");
				}
				progressBar.setProgress(100);
				progressBar.setVisibility(View.GONE);
			}
		}

	}

	// AsyncTask lấy danh sách chuyên môn từ webservice
	private class ChuyenMonWS extends
			AsyncTask<Specialize, Specialize, ArrayList<Specialize>> {

		private boolean running = true;

		@Override
		protected void onCancelled() {
			running = false;
		}

		@Override
		protected ArrayList<Specialize> doInBackground(Specialize... params) {
			ArrayList<Specialize> array = new ArrayList<Specialize>();

			try {
				final String NAMESPACE = "http://tempuri.org/";

				final String METHOD_NAME = "ListChuyenMon";
				final String SOAP_ACTION = NAMESPACE + METHOD_NAME;

				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);

				MarshalFloat marshal = new MarshalFloat();
				marshal.register(envelope);

				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.call(SOAP_ACTION, envelope);

				SoapObject soapArray = (SoapObject) envelope.getResponse();

				if (total == 0)
					total = soapArray.getPropertyCount();

				for (int i = 0; i < soapArray.getPropertyCount(); i++) {

					if (!running)
						break;

					SoapObject soapItem = (SoapObject) soapArray.getProperty(i);
					String MaCm = soapItem.getProperty("MaCM").toString();
					String TenCm = soapItem.getProperty("TenCM").toString();
					Specialize sp = new Specialize(MaCm, TenCm);
					array.add(sp);
					SystemClock.sleep(100);
					publishProgress(sp);
				}
			} catch (Exception e) {
			}
			return array;
		}

		@Override
		protected void onPreExecute() {
			progressBar.setProgress(5);
			progressBar.setVisibility(View.VISIBLE);
			chuyenMon.setEnabled(false);
		}

		@Override
		protected void onProgressUpdate(Specialize... values) {
			super.onProgressUpdate(values);
			if (running) {
				spinner.add(values[0]);
				spinner.notifyDataSetChanged();

				if (total > 0) {
					progressBar.setProgress(spinner.getCount() * 100 / total);

				} else {
					setDialog("Lỗi....", "Không thể kết nối với sever");
				}
			}
		}

		@Override
		protected void onPostExecute(ArrayList<Specialize> result) {
			super.onPostExecute(result);

			total = 0;
			if (running) {
				chuyenMon.setEnabled(true);
				if (result.size() < 1)
					setDialog("Lỗi....", "Không thể kết nối với server");
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
}
