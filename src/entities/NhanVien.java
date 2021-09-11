package entities;

public class NhanVien {
	
	private String MaNV;
	private String TenNV;
	private String MatKhau;
	private boolean GioiTinh;
	private String DiaChi;
	private String SDT;
	private boolean Role;
	private String Hinh;

	public NhanVien() {
	}

	public NhanVien(String maNV, String tenNV, String matKhau, boolean gioiTinh, String diaChi, String sDT, 
			boolean role, String hinh) {
		MaNV = maNV;
		TenNV = tenNV;
		MatKhau = matKhau;
		GioiTinh = gioiTinh;
		SDT = sDT;
		DiaChi = diaChi;
		Role = role;
		Hinh = hinh;
	}

	public String getMaNV() {
		return MaNV;
	}

	public void setMaNV(String maNV) {
		MaNV = maNV;
	}

	public String getTenNV() {
		return TenNV;
	}

	public void setTenNV(String tenNV) {
		TenNV = tenNV;
	}

	public String getMatKhau() {
		return MatKhau;
	}

	public void setMatKhau(String matKhau) {
		MatKhau = matKhau;
	}

	public boolean isGioiTinh() {
		return GioiTinh;
	}

	public void setGioiTinh(boolean gioiTinh) {
		GioiTinh = gioiTinh;
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

	public boolean isRole() {
		return Role;
	}

	public void setRole(boolean role) {
		Role = role;
	}

	public String getHinh() {
		return Hinh;
	}

	public void setHinh(String hinh) {
		Hinh = hinh;
	}

	@Override
	public String toString() {
		return "NhanVien [MaNV=" + MaNV + ", TenNV=" + TenNV + ", MatKhau=" + MatKhau + ", GioiTinh=" + GioiTinh
				+ ", DiaChi=" + DiaChi + ", SDT=" + SDT + ", Role=" + Role + ", Hinh=" + Hinh + "]";
	}
	
	
	
}
