package dao;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeriesDataItem;

import entities.MatHang;
import utils.JDBCHelper;

public class ThongKeDAO {

	public static HashMap<String, String> ThongKeDS_Summary(Date from, Date to) {
		String sql = "SELECT SUM(TongTien) AS TienBanHang, COUNT(*) AS SoDonHang FROM HoaDon WHERE NgayLapHD BETWEEN ? AND ?";
		HashMap<String, String> map = new HashMap<>();
		try {
			ResultSet rs = JDBCHelper.query(sql, from, to);
			if (rs.next()) {
				map.put("TienBanHang", String.valueOf(rs.getLong(1)));
				map.put("SoDonHang", String.valueOf(rs.getInt(2)));
			}
			rs.getStatement().getConnection().close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return map;
	}

	public static HashMap<String, String> ThongKeHH_Summary(Date from, Date to, String MaDM) {
		String call = "{call sp_ThongKeHH_summary (?,?,?)}";
		HashMap<String, String> map = new HashMap<>();
		try {
			ResultSet rs = JDBCHelper.query(call, from, to, MaDM);
			if (rs.next()) {
				map.put("TongHangBan", String.valueOf(rs.getInt(1)));
				map.put("TongDoanhSo", String.valueOf(rs.getLong(2)));
			}
			rs.getStatement().getConnection().close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return map;
	}

	public static HashMap<String, String> ThongKeLN_Summary(Date from, Date to, String MaDM) {
		String call = "{call sp_ThongKeLN_summary (?,?,?)}";
		HashMap<String, String> map = new HashMap<>();
		try {
			ResultSet rs = JDBCHelper.query(call, from, to, MaDM);
			if (rs.next()) {
				map.put("DoanhSo", String.valueOf(rs.getInt(1)));
				map.put("TienVon", String.valueOf(rs.getLong(2)));
				map.put("TienLai", String.valueOf(rs.getLong(3)));
			}
			rs.getStatement().getConnection().close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return map;
	}

	public static void ThongKeDS_byTime(JTable table, Date from, Date to) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0);
		try {
			String call = "{call sp_ThongKeDS_byTime (?,?)}";
			ResultSet rs = JDBCHelper.query(call, from, to);
			while (rs.next()) {
				String DonHang = rs.getString("DonHang");
				String NgayBan = rs.getString("NgayBan");
				String ThuNgan = rs.getString("TenNV");
				int SoLuong = rs.getInt("SoLuong");
				Long DoanhSo = rs.getLong("DoanhSo");
				Object[] row = { DonHang, NgayBan, ThuNgan, SoLuong, DoanhSo };
				model.addRow(row);
			}
			rs.getStatement().getConnection().close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void ThongKeDS_byProduct(JTable table, Date from, Date to, String MaDM) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0);
		try {
			String call = "{call sp_ThongKeDS_byProduct (?,?,?)}";
			ResultSet rs = JDBCHelper.query(call, from, to, MaDM);
			while (rs.next()) {
				String MaHang = rs.getString("MaHang");
				String TenHang = rs.getString("TenHang");
				String NhomHang = rs.getString("NhomHang");
				Long DoanhSo = rs.getLong("DoanhSo");
				int SoLuong = rs.getInt("SoLuong");
				Object[] row = { MaHang, TenHang, NhomHang, SoLuong, DoanhSo };
				model.addRow(row);
			}
			rs.getStatement().getConnection().close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void ThongKeDS_byStaff(JTable table, Date from, Date to, String MaDM) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0);
		try {
			String call = "{call sp_ThongKeDS_byStaff (?,?,?)}";
			ResultSet rs = JDBCHelper.query(call, from, to, MaDM);
			while (rs.next()) {
				String NhanVien = rs.getString("TenNV");
				Long DoanhSo = rs.getLong("DoanhSo");
				int SoDonHang = rs.getInt("SoDonHang");
				int SLHang = rs.getInt("SoLuongHang");
				Object[] row = { NhanVien, DoanhSo, SoDonHang, SLHang };
				model.addRow(row);
			}
			rs.getStatement().getConnection().close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static HashMap<String, Object> ThongKeHH_getDataset(Date from, Date to, String MaDM) {
		HashMap<String, Object> map = new HashMap<>();
		try {
			String call = "{call sp_ThongKeHH_byMaDM (?,?,?)}";
			ResultSet rs = JDBCHelper.query(call, from, to, MaDM);
			while (rs.next()) {
				String NhomHang = rs.getString("TenDanhMuc");
				double SLHangBan = rs.getDouble("SLHangBan");
				map.put(NhomHang, SLHangBan);
			}
			rs.getStatement().getConnection().close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return map;
	}

	public static void ThongKeHH_byMaDM(JTable table, Date from, Date to, String MaDM) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0);
		try {
			String call = "{call sp_ThongKeHH_byMaDM (?,?,?)}";
			ResultSet rs = JDBCHelper.query(call, from, to, MaDM);
			while (rs.next()) {
				String NhomHang = rs.getString("TenDanhMuc");
				int SLHangBan = rs.getInt("SLHangBan");
				long DoanhSo = rs.getLong("DoanhSo");
				Object[] row = { NhomHang, SLHangBan, DoanhSo };
				model.addRow(row);
			}
			rs.getStatement().getConnection().close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static HashMap<String, List<TimeSeriesDataItem>> ThongKeLN_getLineChartDataset(Date from, Date to) {
		HashMap<String, List<TimeSeriesDataItem>> map = new HashMap<>();
		List<TimeSeriesDataItem> listDoanhSo = new ArrayList<>();
		List<TimeSeriesDataItem> listTienVon = new ArrayList<>();
		List<TimeSeriesDataItem> listTienLai = new ArrayList<>();
		try {
			String call = "{call sp_ThongKeLN_byTime (?,?)}";
			ResultSet rs = JDBCHelper.query(call, from, to);
			while (rs.next()) {
				Date date = rs.getDate("Ngay");
				Long ds = rs.getLong("DoanhSo");
				Long tv = rs.getLong("TienVon");
				Long tl = rs.getLong("TienLai");
				listDoanhSo.add(new TimeSeriesDataItem(new Day(date), ds));
				listTienVon.add(new TimeSeriesDataItem(new Day(date), tv));
				listTienLai.add(new TimeSeriesDataItem(new Day(date), tl));
			}
			rs.getStatement().getConnection().close();
			map.put("Doanh số", listDoanhSo);
			map.put("Tiền vốn", listTienVon);
			map.put("Tiền lãi", listTienLai);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return map;
	}

	public static void ThongKeLN_byTime(JTable table, Date from, Date to) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0);
		try {
			String call = "{call sp_ThongKeLN_byTime (?,?)}";
			ResultSet rs = JDBCHelper.query(call, from, to);
			while (rs.next()) {
				Date Ngay = rs.getDate(1);
				Long TienHang = rs.getLong(2);
				Long KM = rs.getLong(3);
				Long PhuThu = rs.getLong(2);
				rs.getLong(4);
				Long DoanhSo = rs.getLong(5);
				Long TienVon = rs.getLong(6);
				Long TienLai = rs.getLong(7);
				Object[] row = { Ngay, TienHang, KM, PhuThu, DoanhSo, TienVon, TienLai };
				model.addRow(row);
			}
			rs.getStatement().getConnection().close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void ThongKeLN_byMaDM(JTable table, Date from, Date to, String MaDM) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0);
		try {
			String call = "{call sp_ThongKeLN_byMaDM (?,?,?)}";
			ResultSet rs = JDBCHelper.query(call, from, to, MaDM);
			while (rs.next()) {
				String TenHang = rs.getString(1);
				long TienHang = rs.getLong(2);
				long KM = rs.getLong(3);
				long DoanhSo = rs.getLong(4);
				long TienVon = rs.getLong(5);
				long TienLai = rs.getLong(6);
				Object[] row = { TenHang, TienHang, KM, DoanhSo, TienVon, TienLai };
				model.addRow(row);
			}
			rs.getStatement().getConnection().close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static HashMap<String, HashMap<String, Long>> ThongKeLN_getBarChartDataset(Date from, Date to, String MaDM) {
		HashMap<String, HashMap<String, Long>> map = new HashMap<>();
		HashMap<String, Long> mapTienVon = new LinkedHashMap<>();
		HashMap<String, Long> mapTienLai = new LinkedHashMap<>();
		try {
			String call = "{call sp_ThongKeLN_byMaDM (?,?,?)}";
			ResultSet rs = JDBCHelper.query(call, from, to, MaDM);
			while (rs.next()) {
				String TenHang = rs.getString(1);
				long TienVon = rs.getLong(5);
				long TienLai = rs.getLong(6);
				mapTienVon.put(TenHang, TienVon);
				mapTienLai.put(TenHang, TienLai);
			}
			rs.getStatement().getConnection().close();
			map.put("Tiền Vốn", mapTienVon);
			map.put("Tiền Lãi", mapTienLai);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return map;
	}

	public static HashMap<String, HashMap<String, Long>> ThongKeBCThang_getBarChartDataset(int year) {
		HashMap<String, HashMap<String, Long>> map = new HashMap<>();
		HashMap<String, Long> mapDoanhSo = new LinkedHashMap<>();
		HashMap<String, Long> mapTienLai = new LinkedHashMap<>();
		for (int i = 1; i <= 12; i++) {
			String month = i + "";
			mapDoanhSo.put(month, (long) 0);
			mapTienLai.put(month, (long) 0);
		}
		try {
			String call = "{call sp_BCThang (?)}";
			ResultSet rs = JDBCHelper.query(call, year);
			while (rs.next()) {
				int m = rs.getInt(1);
				long DoanhSo = rs.getLong(6);
				long TienLai = rs.getLong(8);
				mapDoanhSo.replace(m + "", DoanhSo);
				mapTienLai.replace(m + "", TienLai);
			}
			map.put("Doanh số", mapDoanhSo);
			map.put("Tiền lãi", mapTienLai);
			rs.getStatement().getConnection().close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return map;
	}
	
	public static void ThongKeBCThang_byYear(JTable table, int year) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0);
		try {
			String call = "{call sp_BCThang (?)}";
			ResultSet rs = JDBCHelper.query(call, year);
			while (rs.next()) {
				int m = rs.getInt(1);
				int y = rs.getInt(2);
				String date = String.format("%d/%d", m, y);
				long TienHang = rs.getLong(3);
				long KMDonHang = rs.getLong(4);
				long PhuThu = rs.getLong(5);
				long DoanhSo = rs.getLong(6);
				long TienVon = rs.getLong(7);
				long TienLai = rs.getLong(8);
				int SL = rs.getInt(9);
				int SDH = rs.getInt(10);
				Object[] row = { date, TienHang, KMDonHang, PhuThu, DoanhSo, TienVon, TienLai, SL, SDH };
				model.addRow(row);
			}
			rs.getStatement().getConnection().close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static List<MatHang> ThongKeKho_byMaDM(String MaDM) {
		List<MatHang> list = new ArrayList<>();
		try {
			String SQL = "SELECT * FROM MatHang JOIN DanhMuc ON DanhMuc.MaDanhMuc = MatHang.MaDanhMuc JOIN KhoHang ON KhoHang.MaHang = MatHang.MaHang WHERE DanhMuc.MaDanhMuc LIKE ?";
			ResultSet rs = JDBCHelper.query(SQL, MaDM);
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
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return list;
	}
	
	public static HashMap<String, HashMap<String, Long>> ThongKeThuChi_getBarChartDataset(Date from, Date to) {
		HashMap<String, HashMap<String, Long>> map = new HashMap<>();
		HashMap<String, Long> mapThu = new LinkedHashMap<>();
		HashMap<String, Long> mapChi = new LinkedHashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
		try {
			String call = "SELECT * FROM fn_ThongKeThuChi(?,?)";
			ResultSet rs = JDBCHelper.query(call, from, to);
			while (rs.next()) {
				Date NgayThu = rs.getDate(1);
				Date NgayChi = rs.getDate(2);
				long Thu = rs.getLong(3);
				long Chi = rs.getLong(4);
				String date = NgayThu != null ? sdf.format(NgayThu) : sdf.format(NgayChi);
				mapThu.put(date, Thu);
				mapChi.put(date, Chi);
			}
			map.put("Doanh số", mapThu);
			map.put("Chi phí", mapChi);
			rs.getStatement().getConnection().close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return map;
	}
	
	public static void ThongKeThuChi_byTime(JTable table, Date from, Date to) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0);
		try {
			String call = "SELECT * FROM fn_ThongKeThuChi(?,?)";
			ResultSet rs = JDBCHelper.query(call, from, to);
			while (rs.next()) {
				Date NgayThu = rs.getDate(1);
				Date NgayChi = rs.getDate(2);
				long Thu = rs.getLong(3);
				long Chi = rs.getLong(4);
				Object[] row = { NgayThu != null ? NgayThu : NgayChi, Thu, Chi };
				model.addRow(row);
			}
			rs.getStatement().getConnection().close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static HashMap<String, String> ThongKeThuChi_Summary(Date from, Date to) {
		HashMap<String, String> map = new HashMap<>();
		try {
			String call = "SELECT * FROM fn_ThongKeThuChi_summary(?,?)";
			ResultSet rs = JDBCHelper.query(call, from, to);
			while (rs.next()) {
				long Thu = rs.getLong(1);
				long Chi = rs.getLong(2);
				map.put("Thu", String.valueOf(Thu));
				map.put("Chi", String.valueOf(Chi));
			}
			rs.getStatement().getConnection().close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return map;
	}
}
