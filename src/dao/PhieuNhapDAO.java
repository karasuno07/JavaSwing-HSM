package dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import entities.PhieuNhap;
import entities.PhieuNhapCT;
import utils.JDBCHelper;

public class PhieuNhapDAO implements DAO<PhieuNhap> {

	String INSERT = "INSERT INTO PhieuNhap(MaPhieuNhap, MaNV, MaNCC, NgayNhap, TongTien, GhiChu) VALUES (?,?,?,?,?,?)";
	String INSERT_CT = "INSERT INTO PhieuNhapChiTiet(MaPhieuNhap, MaHang, SoLuongNhap, GiaNhap, ThanhTien) VALUES (?,?,?,?,?)";
	String SELECT_ALL = "SELECT * FROM PhieuNhap";
	
	@Override
	public boolean insert(PhieuNhap entity) {
		try {
			JDBCHelper.update(INSERT, entity.getMaPhieu(), entity.getMaNV(), entity.getMaNCC(), entity.getNgayNhap(), entity.getTongTien(), entity.getGhiChu());
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	public boolean insertDetails(PhieuNhapCT entity) {
		try {
			JDBCHelper.update(INSERT_CT, entity.getMaPhieuNhap(), entity.getMaHang(), entity.getSoLuong(), entity.getDonGia(), entity.getThanhTien());
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	@Override
	public boolean update(PhieuNhap oldEntity, PhieuNhap newEntity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(PhieuNhap entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<PhieuNhap> selectAll() {
		return selectBySQL(SELECT_ALL);
	}

	@Override
	public List<PhieuNhap> select(String SQL, Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PhieuNhap> selectBySQL(String sql, Object... args) {
		List<PhieuNhap> list = new ArrayList<>();
		try {
			ResultSet rs = JDBCHelper.query(sql, args);
			while (rs.next()) {
				String MaPhieu = rs.getString(1);
				String MaNV = rs.getString(2);
				int MaNCC = rs.getInt(3);
				Date NgayNhap = rs.getDate(4);
				long TongTien = rs.getLong(5);
				String GhiChu = rs.getString(6);
				PhieuNhap pn = new PhieuNhap(MaPhieu, MaNV, MaNCC, NgayNhap, TongTien, GhiChu);
				list.add(pn);
			}
			rs.getStatement().getConnection().close();
			return list;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public static void fillDSPN(JTable table, String MaHang) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0);
		try {
			String call = "{call sp_GetDSPN (?)}";
			ResultSet rs = JDBCHelper.query(call, MaHang);
			int i = 0;
			while (rs.next()) {
				Date date = rs.getDate(1);
				String id = rs.getString(2);
				int SL = rs.getInt(3);
				String GhiChu = rs.getString(4);
				Object[] row = { ++i, date, id, SL, GhiChu };
				model.addRow(row);
			}
			rs.getStatement().getConnection().close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RuntimeException();
		}
	}

}
