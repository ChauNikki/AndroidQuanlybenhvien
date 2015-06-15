package android.project.hospital.model;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

public class WebService {

	private static String NAMESPACE = "http://tempuri.org/";

	private static String URL = "http://hospitalserver.somee.com/bvAndroidWebService.asmx?WSDL";

	private static String SOAP_ACTION = "http://tempuri.org/";

	public static boolean invokeCheckAppointmentWS(int id, String webMethName) {
		boolean Status = false;

		SoapObject request = new SoapObject(NAMESPACE, webMethName);

		PropertyInfo idPI = new PropertyInfo();

		idPI.setName("Id");

		idPI.setValue(id);

		idPI.setType(int.class);

		request.addProperty(idPI);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);

		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

		try {

			androidHttpTransport.call(SOAP_ACTION + webMethName, envelope);

			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

			Status = Boolean.parseBoolean(response.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return Status;
	}

	public static Appointment invokeGetAppointmentWS(String username,
			String webMethName) {
		Appointment appointment = new Appointment();
		SoapObject request = new SoapObject(NAMESPACE, webMethName);

		PropertyInfo idPI = new PropertyInfo();

		idPI.setName("username");

		idPI.setValue(username);

		idPI.setType(String.class);

		request.addProperty(idPI);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);

		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

		try {

			androidHttpTransport.call(SOAP_ACTION + webMethName, envelope);

			SoapObject soapItem = (SoapObject) envelope.getResponse();

			Log.e("test", "vào servide" + soapItem);
			int MaKhamBenh = Integer.parseInt(soapItem
					.getProperty("MaKhamBenh").toString());
			String TaiKhoan = soapItem.getProperty("TaiKhoan").toString();
			String NgayKB = soapItem.getProperty("NgayKB").toString();
			String Ca = soapItem.getProperty("Ca").toString();
			String TrieuChung = soapItem.getProperty("TrieuChung").toString();

			String[] d = NgayKB.split("T");

			String[]day = d[0].split("-");

			appointment.setMaKhamBenh(MaKhamBenh);
			appointment.setTaiKhoan(TaiKhoan);
			appointment.setNgayKB(day[2]+"/"+day[1]+"/"+day[0]);
			Log.e("test","check day"+appointment.getNgayKB());
			appointment.setCa(Ca);
			appointment.setTrieuChung(TrieuChung);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return appointment;
	}

	public static boolean invokeDeleteAppointmentWS(int id, String webMethName) {
		boolean Status = false;

		SoapObject request = new SoapObject(NAMESPACE, webMethName);

		PropertyInfo idPI = new PropertyInfo();

		idPI.setName("Id");

		idPI.setValue(id);

		idPI.setType(int.class);

		request.addProperty(idPI);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);

		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

		try {

			androidHttpTransport.call(SOAP_ACTION + webMethName, envelope);

			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

			Status = Boolean.parseBoolean(response.toString());

		} catch (Exception e) {
			e.printStackTrace();
			Log.e("test", e.toString());
		}

		return Status;
	}

	public static int invokeAppointmentWS(String user, int day, int month,
			int year, String symptom, String ca, String webMethName) {
		int maKb = 0;
		// Create request
		SoapObject request = new SoapObject(NAMESPACE, webMethName);
		// Property which holds input parameters
		PropertyInfo userPI = new PropertyInfo();
		PropertyInfo datePI = new PropertyInfo();
		PropertyInfo monthPI = new PropertyInfo();
		PropertyInfo yearPI = new PropertyInfo();
		PropertyInfo symptomPI = new PropertyInfo();
		PropertyInfo CaPI = new PropertyInfo();

		// Set Username
		datePI.setName("ngay");
		// Set Value
		datePI.setValue(day);
		// Set dataType
		datePI.setType(String.class);
		// Add the property to request object
		request.addProperty(datePI);

		// Set Username
		monthPI.setName("thang");
		// Set Value
		monthPI.setValue(month);
		// Set dataType
		monthPI.setType(String.class);
		// Add the property to request object
		request.addProperty(monthPI);

		// Set Username
		yearPI.setName("nam");
		// Set Value
		yearPI.setValue(year);
		// Set dataType
		yearPI.setType(String.class);
		// Add the property to request object
		request.addProperty(yearPI);

		// Set Username
		symptomPI.setName("trieuChung");
		// Set Value
		symptomPI.setValue(symptom);
		// Set dataType
		symptomPI.setType(String.class);
		// Add the property to request object
		request.addProperty(symptomPI);

		// Set Username
		userPI.setName("userName");
		// Set Value
		userPI.setValue(user);
		// Set dataType
		userPI.setType(String.class);
		// Add the property to request object
		request.addProperty(userPI);

		CaPI.setName("Ca");
		// Set dataType
		CaPI.setValue(ca);
		// Set dataType
		CaPI.setType(String.class);
		// Add the property to request object
		request.addProperty(CaPI);
		// Create envelope
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		// Set output SOAP object
		envelope.setOutputSoapObject(request);
		// Create HTTP call object
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

		try {

			androidHttpTransport.call(SOAP_ACTION + webMethName, envelope);

			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
			maKb = Integer.parseInt(response.toString());

		} catch (Exception e) {

			e.printStackTrace();
		}

		return maKb;
	}

	public static int invokeLoginWS(String userName, String passWord,
			String webMethName) {

		// Create request
		SoapObject request = new SoapObject(NAMESPACE, webMethName);

		// Property which holds input parameters
		PropertyInfo unamePI = new PropertyInfo();
		PropertyInfo passPI = new PropertyInfo();
		// Set Username
		unamePI.setName("tk");
		// Set Value
		unamePI.setValue(userName);
		// Set dataType
		unamePI.setType(String.class);
		// Add the property to request object
		request.addProperty(unamePI);
		// Set Password
		passPI.setName("pass");
		// Set dataType
		passPI.setValue(encryptPass(passWord));
		// Set dataType
		passPI.setType(String.class);
		// Add the property to request object
		request.addProperty(passPI);
		// Create envelope
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		// Set output SOAP object
		envelope.setOutputSoapObject(request);
		// Create HTTP call object
		Log.e("test server", request.toString());
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

		try {
			// Invoke web service
			androidHttpTransport.call(SOAP_ACTION + webMethName, envelope);
			Log.e("test server", request.toString());
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

			if (Boolean.parseBoolean(response.toString()))
				return 1;
			else
				return 0;

		} catch (Exception e) {
			// Assign Error Status true in static variable 'errored'
			// CheckDNLoginActivity.errored = true;

			Log.e("error", e.toString() + "");
			return -1;
		}
		// Return booleam to calling object

	}

	public static boolean invokeAppointmentWS(String userName, String passWord,
			String webMethName) {
		boolean loginStatus = false;
		// Create request
		SoapObject request = new SoapObject(NAMESPACE, webMethName);
		// Property which holds input parameters
		PropertyInfo unamePI = new PropertyInfo();
		PropertyInfo passPI = new PropertyInfo();
		// Set Username
		unamePI.setName("tk");
		// Set Value
		unamePI.setValue(userName);
		// Set dataType
		unamePI.setType(String.class);
		// Add the property to request object
		request.addProperty(unamePI);
		// Set Password
		passPI.setName("pass");
		// Set dataType
		passPI.setValue(passWord);
		// Set dataType
		passPI.setType(String.class);
		// Add the property to request object
		request.addProperty(passPI);
		// Create envelope
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		// Set output SOAP object
		envelope.setOutputSoapObject(request);
		// Create HTTP call object
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

		try {
			// Invoke web service
			androidHttpTransport.call(SOAP_ACTION + webMethName, envelope);
			// Get the response
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
			// Assign it to boolean variable variable
			Log.e("test", response.toString());
			loginStatus = Boolean.parseBoolean(response.toString());

		} catch (Exception e) {
			// Assign Error Status true in static variable 'errored'
			// CheckDNLoginActivity.errored = true;
			e.printStackTrace();
		}
		// Return booleam to calling object
		return loginStatus;
	}

	public static int registerWS(String userName, String passWord,
			String webMethName) {
		int register = -1;
		// Create request
		SoapObject request = new SoapObject(NAMESPACE, webMethName);
		SoapObject req = new SoapObject(NAMESPACE, "checkUsername");
		// Property which holds input parameters
		PropertyInfo unamePI = new PropertyInfo();
		PropertyInfo passPI = new PropertyInfo();
		// Set Username
		unamePI.setName("tk");
		// Set Value
		unamePI.setValue(userName);
		// Set dataType
		unamePI.setType(String.class);
		// Add the property to request object
		request.addProperty(unamePI);
		req.addProperty(unamePI);
		// Set Password
		passPI.setName("pass");
		// Set dataType
		passPI.setValue(encryptPass(passWord));
		// Set dataType
		passPI.setType(String.class);
		// Add the property to request object
		request.addProperty(passPI);
		// Create envelope
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		// Set output SOAP object
		envelope.setOutputSoapObject(req);
		// Create HTTP call object
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

		try {

			// Invoke web service
			androidHttpTransport.call(SOAP_ACTION + "checkUsername", envelope);
			// Get the response
			SoapPrimitive rsp = (SoapPrimitive) envelope.getResponse();

			Boolean check = Boolean.parseBoolean(rsp.toString());

			if (check)
				return 0;
			else {
				envelope.setOutputSoapObject(request);

				// Invoke web service
				androidHttpTransport.call(SOAP_ACTION + webMethName, envelope);
				// Get the response
				SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

				check = Boolean.parseBoolean(response.toString());
				if (check == true)
					return 1;
				else
					return -1;
			}

		} catch (Exception e) {
			// Assign Error Status true in static variable 'errored'
			// CheckDNLoginActivity.errored = true;
			e.printStackTrace();
			Log.e("test server", request.toString() + "/" + e.toString());
		}
		// Return booleam to calling object
		return register;
	}

	public static Boolean updateProfileWS(String username, String fullname,
			String birthday, String sex, String phonenumber, String address,
			String webMethName) {
		Boolean check = false;
		// Create request
		SoapObject request = new SoapObject(NAMESPACE, webMethName);
		// tạo đối tượng SoapObject với tên benhnhan như parameter trong service
		// description
		SoapObject newBenhnhan = new SoapObject(NAMESPACE, "benhnhan");
		// truyền giá trị cho các đối số (properties) như service desctiption
		newBenhnhan.addProperty("TaiKhoan", username);
		newBenhnhan.addProperty("SoTheBHYT", 123456);
		newBenhnhan.addProperty("HoTen", fullname);
		newBenhnhan.addProperty("NgaySinhBN", birthday);
		newBenhnhan.addProperty("GioiTinhBN", sex);
		newBenhnhan.addProperty("Diachi", address);

		request.addSoapObject(newBenhnhan);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);

		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

		try {
			androidHttpTransport.call(SOAP_ACTION + webMethName, envelope);
			// androidHttpTransport.call(SOAP_ACTION, envelope);
			SoapPrimitive soapPrimitive = (SoapPrimitive) envelope
					.getResponse();
			// kiểm tra hàm thành công hay thất bại
			check = Boolean.parseBoolean(soapPrimitive.toString());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return check;
	}

	public static Boolean updateProfile(String username, String fullname,
			String day, String month, String year, String sex,
			String phonenumber, String address, String webMethName) {
		Boolean check = false;
		// Create request
		SoapObject request = new SoapObject(NAMESPACE, webMethName);
		// Property which holds input parameters
		PropertyInfo unamePI = new PropertyInfo();
		PropertyInfo fullnamePI = new PropertyInfo();
		PropertyInfo dayPI = new PropertyInfo();
		PropertyInfo monthPI = new PropertyInfo();
		PropertyInfo yearPI = new PropertyInfo();
		PropertyInfo sexPI = new PropertyInfo();
		PropertyInfo phonenumberPI = new PropertyInfo();
		PropertyInfo addressPI = new PropertyInfo();
		// Set Username
		unamePI.setName("username");
		// Set Value
		unamePI.setValue(username);
		// Set dataType
		unamePI.setType(String.class);
		// Add the property to request object
		request.addProperty(unamePI);

		// Set fullname
		fullnamePI.setName("fullname");
		fullnamePI.setValue(fullname);
		fullnamePI.setType(String.class);
		request.addProperty(fullnamePI);

		// Set birthday
		dayPI.setName("ngay");
		dayPI.setValue(day);
		dayPI.setType(String.class);
		request.addProperty(dayPI);

		monthPI.setName("thang");
		monthPI.setValue(month);
		monthPI.setType(String.class);
		request.addProperty(monthPI);

		yearPI.setName("nam");
		yearPI.setValue(year);
		yearPI.setType(String.class);
		request.addProperty(yearPI);

		// Set sex
		sexPI.setName("sex");
		sexPI.setValue(sex);
		sexPI.setType(String.class);
		request.addProperty(sexPI);

		// Set phonenumber
		phonenumberPI.setName("phonenumber");
		phonenumberPI.setValue(phonenumber);
		phonenumberPI.setType(String.class);
		request.addProperty(phonenumberPI);

		// Set address
		addressPI.setName("address");
		addressPI.setValue(address);
		addressPI.setType(String.class);
		request.addProperty(addressPI);

		// Create envelope
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		// Set output SOAP object
		envelope.setOutputSoapObject(request);
		// Create HTTP call object
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

		try {
			// Invoke web service
			androidHttpTransport.call(SOAP_ACTION + webMethName, envelope);

			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

			check = Boolean.parseBoolean(response.toString());

			Log.e("test", response.toString());

		} catch (Exception e) {
			// Assign Error Status true in static variable 'errored'
			// CheckDNLoginActivity.errored = true;
			e.printStackTrace();
		}
		// Return booleam to calling object
		return check;
	}

	public static String encryptPass(String pass) {

		String passEncrypt;
		pass = pass + "HrefDTkwg";

		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException ex) {
		}
		md5.update(pass.getBytes());
		BigInteger dis = new BigInteger(1, md5.digest());
		passEncrypt = dis.toString();
		return passEncrypt;

	}
}
