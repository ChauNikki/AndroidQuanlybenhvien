package android.project.hospital.model;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class Account implements KvmSerializable {

	public String username;
	public String password;
	public String fullname;
	public String birthday;
	public String sex;
	public String phonenumber;
	public String address;
	public String status;

	public Account(String username, String password, String fullname,
			String birthday, String sex, String phonenumber, String address) {
		super();
		this.username = username;
		this.password = password;
		this.fullname = fullname;
		this.birthday = birthday;
		this.sex = sex;
		this.phonenumber = phonenumber;
		this.address = address;
	}

	public Account() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object getProperty(int arg0) {
		// TODO Auto-generated method stub
		switch (arg0) {
		case 0:
			return username;
		case 1:
			return password;
		case 2:
			return fullname;
		case 3:
			return birthday;
		case 4:
			return sex;
		case 5:
			return phonenumber;
		case 6:
			return address;
		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 7;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
		// TODO Auto-generated method stub
		switch (index) {
		case 0:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "TaiKhoan";
			break;
		case 1:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Pass";
			break;
		case 2:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "HoTen";
			break;
		case 3:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "NgaySinhBN";
			break;
		case 4:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "GioiTinhBN";
			break;
		case 5:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "SDT";
			break;
		case 6:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Diachi";
			break;
		default:
			break;
		}
	}

	@Override
	public void setProperty(int index, Object value) {
		// TODO Auto-generated method stub
		switch (index) {
		case 0:
			username = value.toString();
			break;
		case 1:
			password = value.toString();
			break;
		case 2:
			fullname = value.toString();
			break;
		case 3:
			birthday = value.toString();
			break;
		case 4:
			sex = value.toString();
			break;
		case 5:
			phonenumber = value.toString();
			break;
		case 6:
			address = value.toString();
			break;
		default:
			break;
		}

	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
