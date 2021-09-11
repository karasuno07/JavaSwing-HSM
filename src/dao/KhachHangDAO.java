package dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import entities.KhachHang;
import utils.JDBCHelper;

public class KhachHangDAO implements DAO<KhachHang> {

	String INSERT = "INSERT INTO KhachHang(TenKH, GioiTinh, MaNhomKH, DiaChi, NgaySinh, Email, SDT) VALUES(?,?,?,?,?,?,?)";
	String UPDATE = "UPDATE KhachHang SET  TenKH=?, GioiTinh=?, MaNhomKH=?, DiaChi=?, NgaySinh=?, Email=?, SDT=? WHERE MaKH=?";
	String DELETE = "DELETE FROM KhachHang WHERE MaKH=?";
	String SELECT_ALL = "SELECT * FROM KhachHang JOIN NhomKhachHang ON KhachHang.MaNhomKH = NhomKhachHang.MaNhomKH";

	@Override
	public boolean insert(KhachHang entity) {
		try {
			JDBCHelper.update(INSERT, entity.getTenKH(), entity.isGioiTinh(), entity.getMaNhomKH(),
					entity.getDiaChi(), entity.getNgaySinh(), entity.getEmail(), entity.getSDT());
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	@Override
	public boolean update(KhachHang old, KhachHang entity) {
		try {
			JDBCHelper.update(UPDATE, entity.getTenKH(), entity.isGioiTinh(), entity.getMaNhomKH(),
					entity.getDiaChi(), entity.getNgaySinh(), entity.getEmail(), entity.getSDT(), old.getMaKH());
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	@Override
	public boolean delete(KhachHang entity) {
		try {
			JDBCHelper.update(DELETE, entity.getMaKH());
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	@Override
	public List<KhachHang> selectAll() {
		List<KhachHang> list = selectBySQL(SELECT_ALL);
		return list;
	}

	@Override
	public List<KhachHang> select(String SQL, Object key) {
		List<KhachHang> list = null;
		return list;
	}

	@Override
	public List<KhachHang> selectBySQL(String sql, Object... args) {
		List<KhachHang> list = new ArrayList<>();
		try {
			ResultSet rs = JDBCHelper.query(sql, args);
			while (rs.next()) {
				int MaKH = rs.getInt("MaKH");
				String TenKH = rs.getString("TenKH");
				boolean GioiTinh = rs.getBoolean("GioiTinh");
				int MaNhomKH = rs.getInt("MaNhomKH");
				String TenNhomKH = rs.getString("TenNhomKH");
				String DiaChi = rs.getString("DiaChi");
				Date NgaySinh = rs.getDate("NgaySinh");
				String Email = rs.getString("Email");
				String SDT = rs.getString("SDT");
				KhachHang kh = new KhachHang(MaKH, TenKH, GioiTinh, MaNhomKH, TenNhomKH, DiaChi, NgaySinh, Email, SDT);
				list.add(kh);
			}
			rs.getStatement().getConnection().close();
			return list;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

}
