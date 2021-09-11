package entities;

public class NhaCungCap {

	private int MaNCC;
	private String TenNCC;
	private String SDT;
	private String DiaChi;
	private String NguoiLienHe;
	private String GhiChu;

	public NhaCungCap(int maNCC, String tenNCC, String sDT, String diaChi, String nguoiLienHe, String ghiChu) {
		MaNCC = maNCC;
		TenNCC = tenNCC;
		SDT = sDT;
		DiaChi = diaChi;
		NguoiLienHe = nguoiLienHe;
		GhiChu = ghiChu;
	}

	public NhaCungCap(String tenNCC, String sDT, String diaChi, String nguoiLienHe, String ghiChu) {
		TenNCC = tenNCC;
		SDT = sDT;
		DiaChi = diaChi;
		NguoiLienHe = nguoiLienHe;
		GhiChu = ghiChu;
	}



	public int getMaNCC() {
		return MaNCC;
	}

	public void setMaNCC(int maNCC) {
		MaNCC = maNCC;
	}

	public String getTenNCC() {
		return TenNCC;
	}

	public void setTenNCC(String tenNCC) {
		TenNCC = tenNCC;
	}

	public String getSDT() {
		return SDT;
	}

	public void setSDT(String sDT) {
		SDT = sDT;
	}

	public String getDiaChi() {
		return DiaChi;
	}

	public void setDiaChi(String diaChi) {
		DiaChi = diaChi;
	}

	public String getNguoiLienHe() {
		return NguoiLienHe;
	}

	public void setNguoiLienHe(String nguoiLienHe) {
		NguoiLienHe = nguoiLienHe;
	}

	public String getGhiChu() {
		return GhiChu;
	}

	public void setGhiChu(String ghiChu) {
		GhiChu = ghiChu;
	}

	@Override
	public String toString() {
		return "NhaCungCap [MaNCC=" + MaNCC + ", TenNCC=" + TenNCC + ", SDT=" + SDT + ", DiaChi=" + DiaChi
				+ ", NguoiLienHe=" + NguoiLienHe + ", GhiChu=" + GhiChu + "]";
	}

}
