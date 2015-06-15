package android.project.hospital.adapter;


import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.project.hospital.R;
import android.project.hospital.model.Prescription;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

@SuppressLint("ViewHolder")
public class PrescriptionAdapter extends ArrayAdapter<Prescription> {

	private Activity context = null;
	private ArrayList<Prescription> data = null;
	int layoutId;

	public PrescriptionAdapter(
			Activity context,int layoutId, ArrayList<Prescription> data) {
		super(context, layoutId, data);
		this.context = context;
		this.data = data;
		this.layoutId = layoutId;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = context.getLayoutInflater();
		convertView = inflater.inflate(layoutId, null);
		if (data.size() > 0 && position >= 0) {
			Prescription pre = data.get(position);
			TextView thuoc = (TextView) convertView.findViewById(R.id.thuoc);
			TextView Lieuluong = (TextView) convertView.findViewById(R.id.Lieuluong);
			TextView Lieudung = (TextView) convertView.findViewById(R.id.Lieudung);
			TextView Soluong = (TextView) convertView.findViewById(R.id.soluong);
			thuoc.setText(pre.getThuoc());
			Lieuluong.setText("Liều dùng: "+pre.getSoluongdung().toString());
			Lieudung.setText(pre.getLieuDung());
			Soluong.setText("Số lượng: "+pre.getSoluong().toString());
		}
		return convertView;
	}

	public ArrayList<Prescription> getData() {
		return data;
	}

	public void setData(ArrayList<Prescription> data) {
		Log.e("test",data.get(0).getLieuDung());
		this.data = data;
	}
	public void updateDataSetChanged(ArrayList<Prescription> data) {
		this.data.clear();
		this.data.addAll(data);
		this.notifyDataSetChanged();
	}
	
}