package entities;

public class DanhMuc {
	private int MaDanhMuc;
	private String TenDanhMuc;

	public DanhMuc(int id, String name) {
		MaDanhMuc = id;
		TenDanhMuc = name;
	}
	
	
	public DanhMuc(String name) {
		TenDanhMuc = name;
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

	@Override
	public String toString() {
		return "DanhMuc [MaDanhMuc=" + MaDanhMuc + ", TenDanhMuc=" + TenDanhMuc + "]";
	}

}
