package dao;

import java.util.List;

import entities.HoaDon;
import utils.JDBCHelper;

public class HoaDonDAO implements DAO<HoaDon> {
	
	String Insert_SQL = "INSERT INTO HoaDon(MaHD, MaNV, NgayLapHD, MaKH, PhuThu, ChietKhau, TongTien, GhiChu) VALUES (?,?,?,?,?,?,?,?)";
	String SELECT_ALL = "SELECT *  FROM HoaDon";

	
	@Override
	public boolean insert(HoaDon e) {
		try {
			JDBCHelper.update(Insert_SQL, e.getMaHD(), e.getMaNV(), e.getNgayLapHD(), e.getMaKH(), e.getPhuThu(), e.getChietKhau(), e.getTongTien(), e.getGhiChu());
			return true;
		} catch (Exception ex) {
			System.out.println(ex);
			return false;
		}
		
	}

	public void insertCTDH(Object obj) {
		
	}

	@Override
	public boolean update(HoaDon oldEntity, HoaDon newEntity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(HoaDon entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<HoaDon> selectAll() {
		List<HoaDon> list = selectBySQL(SELECT_ALL);
		return list;
	}

	@Override
	public List<HoaDon> select(String SQL, Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HoaDon> selectBySQL(String sql, Object... args) {
		// TODO Auto-generated method stub
		return null;
	}

}
