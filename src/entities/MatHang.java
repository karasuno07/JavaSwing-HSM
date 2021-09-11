package entities;

public class MatHang {

	private String MaHang;
	private String TenHang;
	private int MaDanhMuc;
	private String TenDanhMuc;
	private String DonViTinh;
	private int SoLuong;
	private long GiaNhap;
	private long GiaBan;
	private String NSX;
	private String Hinh;
	private String TinhTrangHang;

	public MatHang(String maHang, String tenHang, int maDanhMuc, String tenDanhMuc, String donViTinh, int soLuong,
			long giaNhap, long giaBan, String nSX, String hinh) {
		MaHang = maHang;
		TenHang = tenHang;
		MaDanhMuc = maDanhMuc;
		TenDanhMuc = tenDanhMuc;
		DonViTinh = donViTinh;
		SoLuong = soLuong;
		GiaNhap = giaNhap;
		GiaBan = giaBan;
		NSX = nSX;
		Hinh = hinh;
	}

	public MatHang(String maHang, String tenHang, int maDanhMuc, String tenDanhMuc, String donViTinh, int soLuong,
			long giaNhap, long giaBan, String nSX, String hinh, String tinhTrangHang) {
		MaHang = maHang;
		TenHang = tenHang;
		MaDanhMuc = maDanhMuc;
		TenDanhMuc = tenDanhMuc;
		DonViTinh = donViTinh;
		SoLuong = soLuong;
		GiaNhap = giaNhap;
		GiaBan = giaBan;
		NSX = nSX;
		Hinh = hinh;
		TinhTrangHang = tinhTrangHang;
	}

	public MatHang(String maHang, String tenHang, int soLuong, long giaNhap, long giaBan, String tinhTrangHang) {
		MaHang = maHang;
		TenHang = tenHang;
		SoLuong = soLuong;
		GiaNhap = giaNhap;
		GiaBan = giaBan;
		TinhTrangHang = tinhTrangHang;
	}

	public String getMaHang() {
		return MaHang;
	}

	public void setMaHang(String maHang) {
		MaHang = maHang;
	}

	public String getTenHang() {
		return TenHang;
	}

	public void setTenHang(String tenHang) {
		TenHang = tenHang;
	}

	public int getMaDanhMuc() {
		return MaDanhMuc;
	}

	public void setMaDanhMuc(int maDanhMuc) {
		MaDanhMuc = maDanhMuc;
	}

	public String getTenDanhMuc() {
		return TenDanhMuc;
	}

	public void setTenDanhMuc(String tenDanhMuc) {
		TenDanhMuc = tenDanhMuc;
	}

	public String getDonViTinh() {
		return DonViTinh;
	}

	public void setDonViTinh(String donViTinh) {
		DonViTinh = donViTinh;
	}

	public int getSoLuong() {
		return SoLuong;
	}

	public void setSoLuong(int soLuong) {
		SoLuong = soLuong;
	}

	public long getGiaNhap() {
		return GiaNhap;
	}

	public void setGiaNhap(long giaNhap) {
		GiaNhap = giaNhap;
	}

	public long getGiaBan() {
		return GiaBan;
	}

	public void setGiaBan(long giaBan) {
		GiaBan = giaBan;
	}

	public String getNSX() {
		return NSX;
	}

	public void setNSX(String nSX) {
		NSX = nSX;
	}

	public String getHinh() {
		return Hinh;
	}

	public void setHinh(String hinh) {
		Hinh = hinh;
	}

	public String getTinhTrangHang() {
		return TinhTrangHang;
	}

	public void setTinhTrangHang(String tinhTrangHang) {
		TinhTrangHang = tinhTrangHang;
	}

	@Override
	public String toString() {
		return "MatHang [MaHang=" + MaHang + ", TenHang=" + TenHang + ", MaDanhMuc=" + MaDanhMuc + ", TenDanhMuc="
				+ TenDanhMuc + ", DonViTinh=" + DonViTinh + ", SoLuong=" + SoLuong + ", GiaNhap=" + GiaNhap
				+ ", GiaBan=" + GiaBan + ", NSX=" + NSX + ", Hinh=" + Hinh + ", TinhTrangHang=" + TinhTrangHang + "]";
	}

}
