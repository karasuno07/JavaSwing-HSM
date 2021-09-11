package entities;

public class PhieuNhapCT {
	private String MaPhieuNhap;
	private String MaHang;
	private String TenHang;
	private int SoLuong;
	private long DonGia;
	private long ThanhTien;
	
	
	
	public PhieuNhapCT(String maPhieuNhap, String maHang, String tenHang, int soLuong, long donGia, long thanhTien) {
		MaPhieuNhap = maPhieuNhap;
		MaHang = maHang;
		TenHang = tenHang;
		SoLuong = soLuong;
		DonGia = donGia;
		ThanhTien = thanhTien;
	}
	public String getMaPhieuNhap() {
		return MaPhieuNhap;
	}
	public void setMaPhieuNhap(String maPhieuNhap) {
		MaPhieuNhap = maPhieuNhap;
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
	public int getSoLuong() {
		return SoLuong;
	}
	public void setSoLuong(int soLuong) {
		SoLuong = soLuong;
	}
	public long getDonGia() {
		return DonGia;
	}
	public void setDonGia(long donGia) {
		DonGia = donGia;
	}
	public long getThanhTien() {
		return ThanhTien;
	}
	public void setThanhTien(long thanhTien) {
		ThanhTien = thanhTien;
	}
	@Override
	public String toString() {
		return "PhieuNhapCT [MaPhieuNhap=" + MaPhieuNhap + ", MaHang=" + MaHang + ", TenHang=" + TenHang + ", SoLuong="
				+ SoLuong + ", DonGia=" + DonGia + ", ThanhTien=" + ThanhTien + "]";
	}
	
	
}
