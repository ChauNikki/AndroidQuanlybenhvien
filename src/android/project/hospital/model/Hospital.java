package android.project.hospital.model;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Hospital extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 10;
	private static final String DATABASE_NAME = "Hospital";

	// Table Names
	private static final String TABLE_ACCOUNT = "account";
	private static final String TABLE_APPOINTMENT = "appointment";
	private static final String TABLE_PRESCRIPTION = "prescription";
	// Column Account
	private static final String KEY_USERNAME = "TaiKhoan";
	private static final String KEY_PASSWORD = "Pass";
	private static final String KEY_FULLNAME = "HoTen";
	private static final String KEY_BIRTHDAY = "NgaySinhBN";
	private static final String KEY_SEX = "GioiTinhBN";
	private static final String KEY_PHONE = "SDT";
	private static final String KEY_ADDRESS = "Diachi";
	// private static final String KEY_STATUS = "TrangThai";
	private static final String KEY_ISLOGIN = "Islogin";

	// Column Appointment
	private static final String KEY_APPOINTMENT = "MaKhamBenh";
	// private static final String KEY_USERNAME="TaiKhoan";
	private static final String KEY_DATEAPPOINTMENT = "NgayKB";
	private static final String KEY_SHIFT = "Ca";
	private static final String KEY_SYMPTOM = "TrieuChung";
	private static final String KEY_STATUS = "TrangThai";
	private static final String KEY_REMIND = "NhacNho";
	private static final String KEY_TIMER = "ThoiGian";

	// Column Prescription
	private static final String KEY_PRESCRIPTON = "MaDungThuoc";
	// private static final String KEY_APPOINTMENT="MaKhamBenh";
	private static final String KEY_QUALITY = "Soluong";
	private static final String KEY_QUALITYUSE = "Soluongdung";
	private static final String KEY_QUALITY_USE = "LieuDung";
	private static final String KEY_PRESCRIPTON_NAME = "Thuoc";

	// Table Create Statements
	// ACCOUNT table create statement
	private static final String CREATE_TABLE_ACCOUNT = "CREATE TABLE "
			+ TABLE_ACCOUNT + "(" + KEY_USERNAME + " TEXT PRIMARY KEY,"
			+ KEY_PASSWORD + " TEXT," + KEY_FULLNAME + " TEXT," + KEY_BIRTHDAY
			+ " TEXT," + KEY_SEX + " TEXT," + KEY_PHONE + " TEXT,"
			+ KEY_ADDRESS + " TEXT," + KEY_STATUS + " TEXT," + KEY_ISLOGIN
			+ " TEXT" + ")";

	// APPOINTMENT table create statement
	private static final String CREATE_TABLE_APPOINTMENT = "CREATE TABLE "
			+ TABLE_APPOINTMENT + "(" + KEY_APPOINTMENT
			+ " INTEGER PRIMARY KEY," + KEY_USERNAME + " TEXT,"
			+ KEY_DATEAPPOINTMENT + " TEXT," + KEY_SHIFT + " TEXT,"
			+ KEY_SYMPTOM + " TEXT," + KEY_REMIND + " TEXT," + KEY_TIMER
			+ " TEXT," + KEY_STATUS + " TEXT" + ")";

	// PRESCRIPTION table create statement
	private static final String CREATE_TABLE_PRESCRIPTION = "CREATE TABLE "
			+ TABLE_PRESCRIPTION + "(" + KEY_PRESCRIPTON
			+ " INTEGER PRIMARY KEY," + KEY_APPOINTMENT + " TEXT,"
			+ KEY_USERNAME + " TEXT," + KEY_QUALITY + " TEXT," + KEY_QUALITYUSE
			+ " TEXT," + KEY_QUALITY_USE + " TEXT," + KEY_PRESCRIPTON_NAME
			+ " TEXT" + ")";

	public Hospital(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_TABLE_ACCOUNT);
		db.execSQL(CREATE_TABLE_APPOINTMENT);
		db.execSQL(CREATE_TABLE_PRESCRIPTION);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPOINTMENT);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRESCRIPTION);

		// create new tables
		onCreate(db);
	}

	public float login(String username) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		float todo_id;
		if (checkAccount(username)) {

			values.put(KEY_ISLOGIN, "true");
			todo_id = db.update(TABLE_ACCOUNT, values, KEY_USERNAME + " = '"
					+ username + "'", null);
		} else {

			values.put(KEY_USERNAME, username);
			values.put(KEY_ISLOGIN, "true");

			// insert row
			todo_id = db.insert(TABLE_ACCOUNT, null, values);
		}
		Log.e("test login", todo_id + "");
		return todo_id;
	}

	public float logout(String username) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		float todo_id;

		values.put(KEY_ISLOGIN, "false");
		todo_id = db.update(TABLE_ACCOUNT, values, KEY_USERNAME + " = '"
				+ username + "'", null);
		return todo_id;
	}

	public float updateAccount(Account account) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		float todo_id;

		values.put(KEY_ISLOGIN, "true");
		values.put(KEY_STATUS, "true");
		values.put(KEY_ADDRESS, account.getAddress());
		values.put(KEY_PHONE, account.getPhonenumber());
		values.put(KEY_SEX, account.getSex());
		values.put(KEY_BIRTHDAY, account.getBirthday());
		values.put(KEY_FULLNAME, account.getFullname());

		todo_id = db.update(TABLE_ACCOUNT, values, KEY_USERNAME + " = '"
				+ account.getUsername() + "'", null);

		return todo_id;
	}

	public boolean checkAccount(String username) {
		boolean account = false;
		String selectQuery = "SELECT " + KEY_ISLOGIN + " FROM " + TABLE_ACCOUNT
				+ " WHERE " + KEY_USERNAME + " = '" + username + "'";
		Cursor cursor = null;

		SQLiteDatabase db = this.getReadableDatabase();
		try {

			cursor = db.rawQuery(selectQuery, null);

			if (cursor.getCount() > 0) {
				account = true;
			}

		} finally {

			cursor.close();
		}

		return account;
	}

	public String checkLogin() {
		String account = "";
		String selectQuery = "SELECT " + KEY_USERNAME + " FROM "
				+ TABLE_ACCOUNT + " WHERE " + KEY_ISLOGIN + " = 'true'";
		Cursor cursor = null;

		SQLiteDatabase db = this.getReadableDatabase();
		try {

			cursor = db.rawQuery(selectQuery, null);

			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				account = cursor.getString(cursor.getColumnIndex(KEY_USERNAME));
			}

		} finally {

			cursor.close();
		}
		return account;
	}

	public Account getAccount(String username) {
		Account account = new Account();
		String selectQuery = "SELECT * FROM " + TABLE_ACCOUNT + " WHERE "
				+ KEY_USERNAME + " = '" + username + "'";
		Cursor cursor = null;

		SQLiteDatabase db = this.getReadableDatabase();
		try {

			cursor = db.rawQuery(selectQuery, null);

			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				account.setUsername(cursor.getString(cursor
						.getColumnIndex(KEY_USERNAME)));
				account.setStatus(cursor.getString(cursor
						.getColumnIndex(KEY_STATUS)));
				account.setAddress(cursor.getString(cursor
						.getColumnIndex(KEY_ADDRESS)));
				account.setPhonenumber(cursor.getString(cursor
						.getColumnIndex(KEY_PHONE)));
				account.setSex(cursor.getString(cursor.getColumnIndex(KEY_SEX)));
				account.setBirthday(cursor.getString(cursor
						.getColumnIndex(KEY_BIRTHDAY)));
				account.setFullname(cursor.getString(cursor
						.getColumnIndex(KEY_FULLNAME)));
			}

		} finally {

			cursor.close();
		}
		return account;
	}

	public Appointment getAppointment(String username) {
		Appointment appointment = new Appointment();
		String selectQuery = "SELECT * FROM " + TABLE_APPOINTMENT + " WHERE "
				+ KEY_USERNAME + " = '" + username + "'";
		Cursor cursor = null;

		SQLiteDatabase db = this.getReadableDatabase();
		try {

			cursor = db.rawQuery(selectQuery, null);

			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				appointment.setMaKhamBenh(cursor.getInt(cursor
						.getColumnIndex(KEY_APPOINTMENT)));
				appointment.setTaiKhoan(cursor.getString(cursor
						.getColumnIndex(KEY_USERNAME)));
				appointment.setNgayKB(cursor.getString(cursor
						.getColumnIndex(KEY_DATEAPPOINTMENT)));
				appointment.setCa(cursor.getString(cursor
						.getColumnIndex(KEY_SHIFT)));
				appointment.setTrieuChung(cursor.getString(cursor
						.getColumnIndex(KEY_SYMPTOM)));
				appointment.setTime(cursor.getString(cursor
						.getColumnIndex(KEY_TIMER)));
				Log.e("test get remind",cursor.getString(cursor
						.getColumnIndex(KEY_REMIND)));
				appointment.setRemind("1".equals(cursor.getString(cursor
						.getColumnIndex(KEY_REMIND))));
			}

		} finally {

			cursor.close();
		}
		return appointment;
	}

	public float insertAppointment(Appointment appointment) {

		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		float todo_id;

		values.put(KEY_APPOINTMENT, appointment.getMaKhamBenh());
		values.put(KEY_USERNAME, appointment.getTaiKhoan());
		values.put(KEY_DATEAPPOINTMENT, appointment.getNgayKB());
		values.put(KEY_SHIFT, appointment.getCa());
		values.put(KEY_SYMPTOM, appointment.getTrieuChung());
		values.put(KEY_REMIND, appointment.isRemind());
		values.put(KEY_TIMER, appointment.getTime());

		// insert row
		todo_id = db.insert(TABLE_APPOINTMENT, null, values);

		Log.e("test inserAppointment", todo_id + "");
		return todo_id;
	}

	public float updateAppointment(String timer, int appointmentID) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		float todo_id;

		values.put(KEY_TIMER, timer);
		todo_id = db.update(TABLE_APPOINTMENT, values, KEY_APPOINTMENT + " = '"
				+ appointmentID + "'", null);

		Log.e("test login", todo_id + "");
		return todo_id;
	}

	public float deleteAppointment(int appointmentID) {

		SQLiteDatabase db = this.getReadableDatabase();
		float todo_id = 0;

		try {

			todo_id = db.delete(TABLE_APPOINTMENT, KEY_APPOINTMENT + " = '"
					+ appointmentID + "'", null);
			Log.e("test xoa", todo_id + "");
		} finally {

		}
		return todo_id;
	}

	public boolean checkPrescription(int prescription) {
		boolean check = false;
		String selectQuery = "SELECT " + KEY_PRESCRIPTON_NAME + " FROM "
				+ TABLE_PRESCRIPTION + " WHERE " + KEY_PRESCRIPTON + " = '"
				+ prescription + "'";
		Cursor cursor = null;

		SQLiteDatabase db = this.getReadableDatabase();
		try {

			cursor = db.rawQuery(selectQuery, null);

			if (cursor.getCount() > 0) {
				check = true;
			}

		} finally {

			cursor.close();
		}
		return check;
	}

	public float insertPrescription(Prescription prescription) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		float todo_id;

		values.put(KEY_PRESCRIPTON, prescription.getMaDungThuoc());
		values.put(KEY_USERNAME, prescription.getTaiKhoan());
		values.put(KEY_APPOINTMENT, prescription.getMaKhamBenh());
		values.put(KEY_QUALITY, prescription.getSoluong());
		values.put(KEY_QUALITY_USE, prescription.getLieuDung());
		values.put(KEY_QUALITYUSE, prescription.getSoluongdung());
		values.put(KEY_PRESCRIPTON_NAME, prescription.getThuoc());

		// insert row
		todo_id = db.insert(TABLE_PRESCRIPTION, null, values);

		Log.e("test inserAppointment", todo_id + "");
		return todo_id;
	}

	public float deletePrescription(int appointmentID) {

		SQLiteDatabase db = this.getReadableDatabase();
		float todo_id = 0;

		try {

			todo_id = db.delete(TABLE_PRESCRIPTION, KEY_APPOINTMENT + " != '"
					+ appointmentID + "'", null);
			Log.e("test xoa", todo_id + "");
		} finally {

		}
		return todo_id;
	}

	public ArrayList<Prescription> getPrescription(String username) {
		ArrayList<Prescription> arrayList = new ArrayList<Prescription>();

		String selectQuery = "SELECT * FROM " + TABLE_PRESCRIPTION + " WHERE "
				+ KEY_USERNAME + " = '" + username + "'";
		Cursor cursor = null;

		SQLiteDatabase db = this.getReadableDatabase();
		try {

			cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					Prescription pre = new Prescription();
					pre.setMaDungThuoc(cursor.getInt(cursor
							.getColumnIndex(KEY_PRESCRIPTON)));
					pre.setMaDungThuoc(cursor.getInt(cursor
							.getColumnIndex(KEY_APPOINTMENT)));
					pre.setSoluong(cursor.getInt(cursor
							.getColumnIndex(KEY_QUALITY)));
					pre.setSoluongdung(cursor.getInt(cursor
							.getColumnIndex(KEY_QUALITYUSE)));
					pre.setLieuDung(cursor.getString(cursor
							.getColumnIndex(KEY_QUALITY_USE)));
					pre.setThuoc(cursor.getString(cursor
							.getColumnIndex(KEY_PRESCRIPTON_NAME)));
					arrayList.add(pre);
				} while (cursor.moveToNext());
			}

		} finally {

			cursor.close();
		}
		return arrayList;
	}
}
