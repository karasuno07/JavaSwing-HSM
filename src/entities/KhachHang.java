package entities;

import java.util.Date;

public class KhachHang {
	
	private int MaKH;
	private String TenKH;
	private boolean GioiTinh;
	private int MaNhomKH;
	private String TenNhomKH;
	private String DiaChi;
	private Date NgaySinh;
	private String Email;
	private String SDT;
	
	public KhachHang(int maKH, String tenKH, boolean gioiTinh, int maNhomKH, String tenNhomKH, String diaChi,
			Date ngaySinh, String email, String sDT) {
		MaKH = maKH;
		TenKH = tenKH;
		GioiTinh = gioiTinh;
		MaNhomKH = maNhomKH;
		TenNhomKH = tenNhomKH;
		DiaChi = diaChi;
		NgaySinh = ngaySinh;
		Email = email;
		SDT = sDT;
	}

	public KhachHang(String tenKH, boolean gioiTinh, int maNhomKH, String tenNhomKH, String diaChi,
			Date ngaySinh, String email, String sDT) {
		TenKH = tenKH;
		GioiTinh = gioiTinh;
		MaNhomKH = maNhomKH;
		TenNhomKH = tenNhomKH;
		DiaChi = diaChi;
		NgaySinh = ngaySinh;
		Email = email;
		SDT = sDT;
	}
	
	
	
	public int getMaKH() {
		return MaKH;
	}

	public void setMaKH(int maKH) {
		MaKH = maKH;
	}

	public String getTenKH() {
		return TenKH;
	}

	public void setTenKH(String tenKH) {
		TenKH = tenKH;
	}

	public boolean isGioiTinh() {
		return GioiTinh;
	}

	public void setGioiTinh(boolean gioiTinh) {
		GioiTinh = gioiTinh;
	}

	public int getMaNhomKH() {
		return MaNhomKH;
	}

	public void setMaNhomKH(int maNhomKH) {
		MaNhomKH = maNhomKH;
	}

	public String getTenNhomKH() {
		return TenNhomKH;
	}

	public void setTenNhomKH(String tenNhomKH) {
		TenNhomKH = tenNhomKH;
	}

	public String getDiaChi() {
		return DiaChi;
	}

	public void setDiaChi(String diaChi) {
		DiaChi = diaChi;
	}

	public Date getNgaySinh() {
		return NgaySinh;
	}

	public void setNgaySinh(Date ngaySinh) {
		NgaySinh = ngaySinh;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getSDT() {
		return SDT;
	}

	public void setSDT(String sDT) {
		SDT = sDT;
	}

	@Override
	public String toString() {
		return "KhachHang [MaKH=" + MaKH + ", TenKH=" + TenKH + ", GioiTinh=" + GioiTinh + ", MaNhomKH=" + MaNhomKH
				+ ", TenNhomKH=" + TenNhomKH + ", DiaChi=" + DiaChi + ", NgaySinh=" + NgaySinh + ", Email=" + Email
				+ ", SDT=" + SDT + "]";
	}
}
