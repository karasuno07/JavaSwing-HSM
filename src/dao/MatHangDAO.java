package dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import entities.MatHang;
import utils.JDBCHelper;

public class MatHangDAO implements DAO<MatHang> {

	String INSERT_MATHANG = "INSERT INTO MatHang(MaHang, TenHang, MaDanhMuc, DonViTinh, NSX, Hinh) VALUES (?,?,?,?,?,?)";
	String INSERT_KHOHANG = "INSERT INTO KhoHang(MaHang, SoLuong, GiaNhap, GiaBan) VALUES (?,?,?,?)";
	String UPDATE_MATHANG = "UPDATE MatHang SET MaHang=?, TenHang=?, MaDanhMuc=?, DonViTinh=?, NSX=?, Hinh=? WHERE MaHang=?";
	String UPDATE_KHOHANG = "UPDATE KhoHang SET SoLuong=?, GiaBan=? WHERE MaHang=?";
	String DELETE = "DELETE FROM MatHang WHERE MaHang=?";
	String SELECT_ALL = "SELECT * FROM MatHang JOIN DanhMuc ON DanhMuc.MaDanhMuc = MatHang.MaDanhMuc JOIN KhoHang ON KhoHang.MaHang = MatHang.MaHang";

	@Override
	public boolean insert(MatHang entity) {
		try {
			JDBCHelper.update(INSERT_MATHANG, entity.getMaHang(), entity.getTenHang(), entity.getMaDanhMuc(),
					entity.getDonViTinh(), entity.getNSX(), entity.getHinh());
			JDBCHelper.update(INSERT_KHOHANG, entity.getMaHang(), entity.getSoLuong(), entity.getGiaNhap(),
					entity.getGiaBan());
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	@Override
	public boolean update(MatHang old, MatHang entity) {
		try {
			JDBCHelper.update(UPDATE_MATHANG, entity.getMaHang(), entity.getTenHang(), entity.getMaDanhMuc(),
					entity.getDonViTinh(), entity.getNSX(), entity.getHinh(), old.getMaHang());
			JDBCHelper.update(UPDATE_KHOHANG, entity.getSoLuong(), entity.getGiaBan(), old.getMaHang());
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	@Override
	public boolean delete(MatHang entity) {
		try {
			JDBCHelper.update(DELETE, entity.getMaHang());
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	@Override
	public List<MatHang> selectAll() {
		List<MatHang> list = selectBySQL(SELECT_ALL);
		return list;
	}

	@Override
	public List<MatHang> select(String SQL, Object key) {
		List<MatHang> list = selectBySQL(SQL, key);
		return list;
	}

	@Override
	public List<MatHang> selectBySQL(String sql, Object... args) {
		List<MatHang> list = new ArrayList<>();
		try {
			ResultSet rs = JDBCHelper.query(sql, args);
			while (rs.next()) {
				String MaHang = rs.getString("MaHang");
				String TenHang = rs.getString("TenHang");
				int MaDanhMuc = rs.getInt("MaDanhMuc");
				String TenDanhMuc = rs.getString("TenDanhMuc");
				String DonViTinh = rs.getString("DonViTinh");
				int SoLuong = rs.getInt("SoLuong");
				long GiaNhap = rs.getLong("GiaNhap");
				long GiaBan = rs.getLong("GiaBan");
				String NSX = rs.getString("NSX");
				String Hinh = rs.getString("Hinh");
				String TinhTrangHang = rs.getString("TinhTrangHang");
				MatHang mh = new MatHang(MaHang, TenHang, MaDanhMuc, TenDanhMuc, DonViTinh, SoLuong, GiaNhap, GiaBan, NSX, Hinh, TinhTrangHang);
				list.add(mh);
			}
			rs.getStatement().getConnection().close();
			return list;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

}
