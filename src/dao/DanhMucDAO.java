package dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import entities.DanhMuc;
import utils.JDBCHelper;

public class DanhMucDAO implements DAO<DanhMuc> {
	
	String SELECT_ALL = "SELECT * FROM DanhMuc";
	String INSERT = "INSERT INTO DanhMuc(TenDanhMuc) VALUES (?)";
	String UPDATE = "UPDATE DanhMuc SET TenDanhMuc=? WHERE MaDanhMuc=?";
	String DELETE = "DELETE FROM DanhMuc WHERE MaDanhMuc=?";
	
	@Override
	public boolean insert(DanhMuc entity) {
		try {
			JDBCHelper.update(INSERT, entity.getTenDanhMuc());
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	@Override
	public boolean update(DanhMuc old, DanhMuc entity) {
		try {
			JDBCHelper.update(UPDATE, entity.getTenDanhMuc(), old.getMaDanhMuc());
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	@Override
	public boolean delete(DanhMuc entity) {
		try {
			JDBCHelper.update(DELETE, entity.getMaDanhMuc());
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	@Override
	public List<DanhMuc> selectAll() {
		List<DanhMuc> list = selectBySQL(SELECT_ALL);
		return list;
	}

	@Override
	public List<DanhMuc> select(String SQL, Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DanhMuc> selectBySQL(String sql, Object... args) {
		List<DanhMuc> list = new ArrayList<>();
		try {
			ResultSet rs = JDBCHelper.query(sql, args);
			while (rs.next()) {
				int MaDanhMuc = rs.getInt("MaDanhMuc");
				String TenDanhMuc = rs.getString("TenDanhMuc");
				DanhMuc dm = new DanhMuc(MaDanhMuc, TenDanhMuc);
				list.add(dm);
			}
			rs.getStatement().getConnection().close();
			return list;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	} 

}
