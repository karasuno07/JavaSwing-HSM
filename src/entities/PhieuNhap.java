package entities;

import java.util.ArrayList;
import java.util.Date;

public class PhieuNhap {

	private String MaPhieu;
	private String MaNV;
	private int MaNCC;
	private Date NgayNhap;
	private ArrayList<PhieuNhapCT> list = new ArrayList<>();
	private long TongTien;
	private String GhiChu;

	public PhieuNhap(String maPhieu, String maNV, int maNCC, Date ngayNhap, long tongTien, String ghiChu) {
		MaPhieu = maPhieu;
		MaNV = maNV;
		MaNCC = maNCC;
		NgayNhap = ngayNhap;
		TongTien = tongTien;
		GhiChu = ghiChu;
	}

	public String getMaPhieu() {
		return MaPhieu;
	}

	public void setMaPhieu(String maPhieu) {
		MaPhieu = maPhieu;
	}

	public String getMaNV() {
		return MaNV;
	}

	public void setMaNV(String maNV) {
		MaNV = maNV;
	}

	public int getMaNCC() {
		return MaNCC;
	}

	public void setMaNCC(int maNCC) {
		MaNCC = maNCC;
	}

	public Date getNgayNhap() {
		return NgayNhap;
	}

	public void setNgayNhap(Date ngayNhap) {
		NgayNhap = ngayNhap;
	}

	public ArrayList<PhieuNhapCT> getList() {
		return list;
	}

	public void setList(ArrayList<PhieuNhapCT> list) {
		this.list = list;
	}

	public long getTongTien() {
		return TongTien;
	}

	public void setTongTien(long tongTien) {
		TongTien = tongTien;
	}

	public String getGhiChu() {
		return GhiChu;
	}

	public void setGhiChu(String ghiChu) {
		GhiChu = ghiChu;
	}

	@Override
	public String toString() {
		return "PhieuNhap [MaPhieu=" + MaPhieu + ", MaNV=" + MaNV + ", MaNCC=" + MaNCC + ", NgayNhap=" + NgayNhap
				+ ", TongTien=" + TongTien + ", GhiChu=" + GhiChu + "]";
	}

}
