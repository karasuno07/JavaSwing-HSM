package dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import entities.NhomKhachHang;
import utils.JDBCHelper;

public class NhomKhachHangDAO implements DAO<NhomKhachHang> {
	
	String INSERT = "INSERT INTO NhomKhachHang(TenNhomKH) VALUES (?)";
	String UPDATE = "UPDATE NhomKhachHang SET TenNhomKH=? WHERE MaNhomKH=?";
	String DELETE = "DELETE NhomKhachHang WHERE MaNhomKH=?";
	String SELECT_ALL = "SELECT * FROM NhomKhachHang";
	
	@Override
	public boolean insert(NhomKhachHang entity) {
		try {
			JDBCHelper.update(INSERT, entity.getTenNhomKH());
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	@Override
	public boolean update(NhomKhachHang old, NhomKhachHang entity) {
		try {
			JDBCHelper.update(UPDATE, entity.getTenNhomKH(), old.getMaNhomKH());
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}


	@Override
	public boolean delete(NhomKhachHang entity) {
		try {
			JDBCHelper.update(DELETE, entity.getMaNhomKH());
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}


	@Override
	public List<NhomKhachHang> selectAll() {
		List<NhomKhachHang> list = selectBySQL(SELECT_ALL);
		return list;
	}

	@Override
	public List<NhomKhachHang> select(String SQL, Object key) {
		List<NhomKhachHang> list = selectBySQL(SQL, key);
		return list;
	}

	@Override
	public List<NhomKhachHang> selectBySQL(String sql, Object... args) {
		List<NhomKhachHang> list = new ArrayList<>();
		try {
			ResultSet rs = JDBCHelper.query(sql, args);
			while (rs.next()) {
				int MaNhomKH = rs.getInt("MaNhomKH");
				String TenNhomKH = rs.getString("TenNhomKH");
				NhomKhachHang nkh = new NhomKhachHang(MaNhomKH, TenNhomKH);
				list.add(nkh);
			}
			rs.getStatement().getConnection().close();
			return list;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

}
