package dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import entities.NhanVien;
import utils.JDBCHelper;

public class NhanVienDAO implements DAO<NhanVien> {
	String INSERT = "INSERT INTO NhanVien(MaNV, TenNV, MatKhau, GioiTinh, DiaChi, SDT, VaiTro, Hinh) VALUES (?,?,?,?,?,?,?,?)";
	String UPDATE = "UPDATE NhanVien SET MaNV=?, TenNV=?, MatKhau=?, GioiTinh=?, DiaChi=?, SDT=?, VaiTro=?, Hinh=? WHERE MaNV=?";
	String DELETE = "DELETE FROM NhanVien WHERE MaNV=?";
	String SELECT_ALL = "SELECT * FROM NhanVien";
	String SELECT = "SELECT * FROM NhanVien WHERE MaNV=?";
	String CHANGE_PASSWORD = "UPDATE NhanVien SET MatKhau=? WHERE MaNV=?";

	@Override
	public boolean insert(NhanVien nv) {
		try {
			JDBCHelper.update(INSERT, nv.getMaNV(), nv.getTenNV(), nv.getMatKhau(), nv.isGioiTinh(), nv.getDiaChi(),
					nv.getSDT(), nv.isRole(), nv.getHinh());
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	@Override
	public boolean update(NhanVien nvc, NhanVien nv) {
		try {
			JDBCHelper.update(UPDATE, nv.getMaNV(), nv.getTenNV(), nv.getMatKhau(), nv.isGioiTinh(), nv.getDiaChi(),
					nv.getSDT(), nv.isRole(), nv.getHinh(), nvc.getMaNV());
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	@Override
	public boolean delete(NhanVien nv) {
		try {
			JDBCHelper.update(DELETE, nv.getMaNV());
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}
	
	public boolean DoiMatKhau(NhanVien nv, String newPassword) {
		try {
			JDBCHelper.update(CHANGE_PASSWORD, newPassword, nv.getMaNV());
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	@Override
	public List<NhanVien> selectAll() {
		List<NhanVien> list = selectBySQL(SELECT_ALL);
		return list;
	}

	@Override
	public List<NhanVien> select(String SQL, Object key) {
		List<NhanVien> list = selectBySQL(SELECT, key);
		return list;
	}

	@Override
	public List<NhanVien> selectBySQL(String sql, Object... args) {
		List<NhanVien> list = new ArrayList<>();
		try {
			ResultSet rs = JDBCHelper.query(sql, args);
			while (rs.next()) {
				String MaNV = rs.getString(1);
				String TenNV = rs.getString(2);
				String MatKhau = rs.getString(3);
				boolean GioiTinh = rs.getBoolean(4);
				String DiaChi = rs.getString(5);
				String SDT = rs.getString(6);
				boolean Role = rs.getBoolean(7);
				String Hinh = rs.getString(8);
				NhanVien nv = new NhanVien(MaNV, TenNV, MatKhau, GioiTinh, DiaChi, SDT, Role, Hinh);
				list.add(nv);
			}
			rs.getStatement().getConnection().close();
			return list;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

}
