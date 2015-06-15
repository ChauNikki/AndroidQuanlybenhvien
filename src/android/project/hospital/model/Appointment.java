package android.project.hospital.model;

public class Appointment {

	int MaKhamBenh;
	String TaiKhoan;
	String NgayKB;
	String TrieuChung;
	String TrangThai;
	String Ca;
	String Time;
	boolean remind;
	
	public Appointment() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Appointment(int maKhamBenh, String taiKhoan, String ngayKB,
			String trieuChung, String trangThai, String ca) {
		super();
		MaKhamBenh = maKhamBenh;
		TaiKhoan = taiKhoan;
		NgayKB = ngayKB;
		TrieuChung = trieuChung;
		TrangThai = trangThai;
		Ca = ca;
	}
	public int getMaKhamBenh() {
		return MaKhamBenh;
	}
	public void setMaKhamBenh(int i) {
		MaKhamBenh = i;
	}
	public String getTaiKhoan() {
		return TaiKhoan;
	}
	public void setTaiKhoan(String taiKhoan) {
		TaiKhoan = taiKhoan;
	}
	public String getNgayKB() {
		return NgayKB;
	}
	public void setNgayKB(String ngayKB) {
		NgayKB = ngayKB;
	}
	public String getTrieuChung() {
		return TrieuChung;
	}
	public void setTrieuChung(String trieuChung) {
		TrieuChung = trieuChung;
	}
	public String getTrangThai() {
		return TrangThai;
	}
	public void setTrangThai(String trangThai) {
		TrangThai = trangThai;
	}
	public String getCa() {
		return Ca;
	}
	public void setCa(String ca) {
		Ca = ca;
	}
	public boolean isRemind() {
		return remind;
	}
	public void setRemind(boolean remind) {
		this.remind = remind;
	}
	public String getTime() {
		return Time;
	}
	public void setTime(String time) {
		Time = time;
	}
}
