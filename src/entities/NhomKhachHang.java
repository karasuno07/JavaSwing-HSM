package entities;

public class NhomKhachHang {

	private int MaNhomKH;
	private String TenNhomKH;

	public NhomKhachHang(String tenNhomKH) {
		TenNhomKH = tenNhomKH;
	}

	public NhomKhachHang(int maNhomKH, String tenNhomKH) {
		MaNhomKH = maNhomKH;
		TenNhomKH = tenNhomKH;
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


	@Override
	public String toString() {
		return "NhomKhachHang [MaNhomKH=" + MaNhomKH + ", TenNhomKH=" + TenNhomKH + "]";
	}

	

}
