package dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import entities.NhaCungCap;
import utils.JDBCHelper;

public class NhaCungCapDAO implements DAO<NhaCungCap> {
	
	String INSERT = "INSERT INTO NhaCungCap(TenNCC, SDT, DiaChi, NguoiLienHe, GhiChu) VALUES (?,?,?,?,?)";
	String UPDATE = "UPDATE NhaCungCap SET TenNCC=?, SDT=?, DiaChi=?, NguoiLienHe=?, GhiChu=? WHERE MaNCC=?";
	String DELETE = "DELETE FROM NhaCungCap WHERE MaNCC=?";
	String SELECT_ALL = "SELECT * FROM NhaCungCap";
	
	@Override
	public boolean insert(NhaCungCap entity) {
		try {
			JDBCHelper.update(INSERT, entity.getTenNCC(), entity.getSDT(), entity.getDiaChi(), entity.getNguoiLienHe(), entity.getGhiChu());
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	@Override
	public boolean update(NhaCungCap old, NhaCungCap entity) {
		try {
			JDBCHelper.update(UPDATE, entity.getTenNCC(), entity.getSDT(), entity.getDiaChi(), entity.getNguoiLienHe(), entity.getGhiChu(), old.getMaNCC());
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	@Override
	public boolean delete(NhaCungCap entity) {
		try {
			JDBCHelper.update(DELETE, entity.getMaNCC());
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	@Override
	public List<NhaCungCap> selectAll() {
		List<NhaCungCap> list = selectBySQL(SELECT_ALL);
		return list;
	}

	@Override
	public List<NhaCungCap> select(String SQL, Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NhaCungCap> selectBySQL(String sql, Object... args) {
		List<NhaCungCap> list = new ArrayList<>();
		try {
			ResultSet rs = JDBCHelper.query(sql, args);
			while (rs.next()) {
				int MaNCC = rs.getInt(1);
				String TenNCC = rs.getString(2);
				String SDT = rs.getString(3);
				String DiaChi = rs.getString(4);
				String NLH = rs.getString(5);
				String GhiChu = rs.getString(6);
				NhaCungCap ncc = new NhaCungCap(MaNCC, TenNCC, SDT, DiaChi, NLH, GhiChu);
				list.add(ncc);
			}
			rs.getStatement().getConnection().close();
			return list;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

}
