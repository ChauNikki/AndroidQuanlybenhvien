package android.project.hospital.model;

public class Prescription {

	Integer MaDungThuoc;
	Integer MaKhamBenh;
	Integer Soluong;
	Integer Soluongdung;
	String LieuDung;
	String TaiKhoan;
	String NhacNho;
	String Thuoc;
	public Prescription() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Prescription(Integer maDungThuoc, Integer maKhamBenh,
			Integer soluong, Integer soluongdung, String lieuDung,
			String taiKhoan, String nhacNho, String thuoc) {
		super();
		MaDungThuoc = maDungThuoc;
		MaKhamBenh = maKhamBenh;
		Soluong = soluong;
		Soluongdung = soluongdung;
		LieuDung = lieuDung;
		TaiKhoan = taiKhoan;
		NhacNho = nhacNho;
		Thuoc = thuoc;
	}
	public Integer getMaDungThuoc() {
		return MaDungThuoc;
	}
	public void setMaDungThuoc(Integer maDungThuoc) {
		MaDungThuoc = maDungThuoc;
	}
	public Integer getMaKhamBenh() {
		return MaKhamBenh;
	}
	public void setMaKhamBenh(Integer maKhamBenh) {
		MaKhamBenh = maKhamBenh;
	}
	public Integer getSoluong() {
		return Soluong;
	}
	public void setSoluong(Integer soluong) {
		Soluong = soluong;
	}
	public Integer getSoluongdung() {
		return Soluongdung;
	}
	public void setSoluongdung(Integer soluongdung) {
		Soluongdung = soluongdung;
	}
	public String getLieuDung() {
		return LieuDung;
	}
	public void setLieuDung(String lieuDung) {
		LieuDung = lieuDung;
	}
	public String getTaiKhoan() {
		return TaiKhoan;
	}
	public void setTaiKhoan(String taiKhoan) {
		TaiKhoan = taiKhoan;
	}
	public String getNhacNho() {
		return NhacNho;
	}
	public void setNhacNho(String nhacNho) {
		NhacNho = nhacNho;
	}
	public String getThuoc() {
		return Thuoc;
	}
	public void setThuoc(String thuoc) {
		Thuoc = thuoc;
	}
}
