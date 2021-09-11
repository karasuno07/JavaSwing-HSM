package entities;

import java.util.Date;

public class KhuyenMai {

	private int MaKM;
	private String TenKM;
	private Date NgayBatDau;
	private Date NgayKetThuc;
	private String ChietKhau;
	private String GhiChu;
	
	
	
	public KhuyenMai(String tenKM, Date ngayBatDau, Date ngayKetThuc, String chietKhau, String ghiChu) {
		TenKM = tenKM;
		NgayBatDau = ngayBatDau;
		NgayKetThuc = ngayKetThuc;
		ChietKhau = chietKhau;
		GhiChu = ghiChu;
	}

	public KhuyenMai(int maKM, String tenKM, Date ngayBatDau, Date ngayKetThuc, String chietKhau, String ghiChu) {
		MaKM = maKM;
		TenKM = tenKM;
		NgayBatDau = ngayBatDau;
		NgayKetThuc = ngayKetThuc;
		ChietKhau = chietKhau;
		GhiChu = ghiChu;
	}

	public int getMaKM() {
		return MaKM;
	}

	public void setMaKM(int maKM) {
		MaKM = maKM;
	}

	public String getTenKM() {
		return TenKM;
	}

	public void setTenKM(String tenKM) {
		TenKM = tenKM;
	}

	public Date getNgayBatDau() {
		return NgayBatDau;
	}

	public void setNgayBatDau(Date ngayBatDau) {
		NgayBatDau = ngayBatDau;
	}

	public Date getNgayKetThuc() {
		return NgayKetThuc;
	}

	public void setNgayKetThuc(Date ngayKetThuc) {
		NgayKetThuc = ngayKetThuc;
	}

	public String getChietKhau() {
		return ChietKhau;
	}

	public void setChietKhau(String chietKhau) {
		ChietKhau = chietKhau;
	}

	public String getGhiChu() {
		return GhiChu;
	}

	public void setGhiChu(String ghiChu) {
		GhiChu = ghiChu;
	}

	@Override
	public String toString() {
		return "KhuyenMai [MaKM=" + MaKM + ", TenKM=" + TenKM + ", NgayBatDau=" + NgayBatDau + ", NgayKetThuc="
				+ NgayKetThuc + ", ChietKhau=" + ChietKhau + ", GhiChu=" + GhiChu + "]";
	}

}
