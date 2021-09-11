package entities;

import java.util.Date;

public class HoaDon {
	
	private String MaHD;
	private String MaNV;
	private Date NgayLapHD;
	private int MaKH;
	private long PhuThu = 0;
	private long ChietKhau = 0;
	private long TongTien;
	private String GhiChu = "";
	
	
	
	public HoaDon(String maHD, String maNV, Date ngayLapHD, int maKH, long chietKhau, long tongTien) {
		MaHD = maHD;
		MaNV = maNV;
		NgayLapHD = ngayLapHD;
		MaKH = maKH;
		ChietKhau = chietKhau;
		TongTien = tongTien;
	}

	public HoaDon(String maHD, String maNV, Date ngayLapHD, int maKH, long phuThu, long chietKhau, long tongTien,
			String ghiChu) {
		MaHD = maHD;
		MaNV = maNV;
		NgayLapHD = ngayLapHD;
		MaKH = maKH;
		PhuThu = phuThu;
		ChietKhau = chietKhau;
		TongTien = tongTien;
		GhiChu = ghiChu;
	}
	
	

	public String getMaHD() {
		return MaHD;
	}

	public void setMaHD(String maHD) {
		MaHD = maHD;
	}

	public String getMaNV() {
		return MaNV;
	}

	public void setMaNV(String maNV) {
		MaNV = maNV;
	}

	public Date getNgayLapHD() {
		return NgayLapHD;
	}

	public void setNgayLapHD(Date ngayLapHD) {
		NgayLapHD = ngayLapHD;
	}

	public int getMaKH() {
		return MaKH;
	}

	public void setMaKH(int maKH) {
		MaKH = maKH;
	}

	public long getPhuThu() {
		return PhuThu;
	}

	public void setPhuThu(long phuThu) {
		PhuThu = phuThu;
	}

	public long getChietKhau() {
		return ChietKhau;
	}

	public void setChietKhau(long chietKhau) {
		ChietKhau = chietKhau;
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
		return "HoaDon [MaHD=" + MaHD + ", MaNV=" + MaNV + ", NgayLapHD=" + NgayLapHD + ", MaKH=" + MaKH + ", PhuThu="
				+ PhuThu + ", ChietKhau=" + ChietKhau + ", TongTien=" + TongTien + ", GhiChu=" + GhiChu + "]";
	}

	
	
	
}
