package android.project.hospital.model;

public class Specialize {
	public String MaChuyenMon;
	public String TenChuyenMon;
	public Specialize(String maChuyenMon, String tenChuyenMon) {
		super();
		MaChuyenMon = maChuyenMon;
		TenChuyenMon = tenChuyenMon;
	}
	public Specialize() {
		super();
	}
	public String getMaChuyenMon() {
		return MaChuyenMon;
	}
	public void setMaChuyenMon(String maChuyenMon) {
		MaChuyenMon = maChuyenMon;
	}
	public String getTenChuyenMon() {
		return TenChuyenMon;
	}
	public void setTenChuyenMon(String tenChuyenMon) {
		TenChuyenMon = tenChuyenMon;
	}
	@Override
	  public String toString() {
	    return TenChuyenMon; 
	}
	
}
