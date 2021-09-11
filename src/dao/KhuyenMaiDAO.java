package dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import entities.KhuyenMai;
import entities.NhomKhachHang;
import utils.JDBCHelper;

public class KhuyenMaiDAO implements DAO<KhuyenMai> {
	
	String SELECT_ALL = "SELECT * FROM KhuyenMai";
	String INSERT = "INSERT INTO KhuyenMai(TenKM, NgayBatDau, NgayKetThuc, ChietKhau, GhiChu) VALUES (?,?,?,?,?)";
	String INSERT_DETAILS = "INSERT INTO ChiTietKhuyenMai(MaNhomKH, MaKM) VALUES (?,?)";
	String UPDATE = "UPDATE KhuyenMai SET TenKM=?, NgayBatDau=?, NgayKetThuc=?, ChietKhau=?, GhiChu=? WHERE MaKM=?";	String DELETE = "DELETE FROM KhuyenMai WHERE MaKM=?";
	String DELETE_DETAILS = "DELETE FROM ChitietKhuyenMai WHERE MaKM=?";

	@Override
	public boolean insert(KhuyenMai entity) {
		try {
			JDBCHelper.update(INSERT, entity.getTenKM(), entity.getNgayBatDau(), entity.getNgayKetThuc(), entity.getChietKhau(), entity.getGhiChu());
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}
	
	public boolean insertDetails(List<NhomKhachHang> list, Object MaKM) {
		try {
			for (NhomKhachHang nhom : list) {
				System.out.println("MaKM: " + MaKM);
				JDBCHelper.update(INSERT_DETAILS, nhom.getMaNhomKH(), MaKM);
			}
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	@Override
	public boolean update(KhuyenMai old, KhuyenMai entity) {
		try {
			JDBCHelper.update(UPDATE, entity.getTenKM(), entity.getNgayBatDau(), entity.getNgayKetThuc(), entity.getChietKhau(), entity.getGhiChu(), old.getMaKM());
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}
	

	@Override
	public boolean delete(KhuyenMai entity) {
		try {
			JDBCHelper.update(DELETE, entity.getMaKM());
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}
	
	public boolean deleteDetails(Object MaKM) {
		try {
			JDBCHelper.update(DELETE_DETAILS, MaKM);
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	@Override
	public List<KhuyenMai> selectAll() {
		List<KhuyenMai> list = selectBySQL(SELECT_ALL);
		return list;
	}

	@Override
	public List<KhuyenMai> select(String SQL, Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<KhuyenMai> selectBySQL(String sql, Object... args) {
		List<KhuyenMai> list = new ArrayList<>();
		try {
			ResultSet rs = JDBCHelper.query(sql, args);
			while (rs.next()) {
				int MaKM = rs.getInt(1);
				String TenKM = rs.getString(2);
				Date NgayBatDau = rs.getDate(3);
				Date NgayKetThuc = rs.getDate(4);
				String ChietKhau = rs.getString(5);
				String GhiChu = rs.getString(6);
				KhuyenMai km = new KhuyenMai(MaKM, TenKM, NgayBatDau, NgayKetThuc, ChietKhau, GhiChu);
				list.add(km);
			}
			rs.getStatement().getConnection().close();
			return list;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

}
