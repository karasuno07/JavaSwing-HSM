package windows;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.*;
import javax.swing.table.*;

import org.jdatepicker.JDatePicker;
import org.jdatepicker.UtilDateModel;

import dao.DanhMucDAO;
import dao.ThongKeDAO;
import entities.DanhMuc;
import utils.ChartGenerator;
import utils.MessageBox;
import utils.Theme;
import utils.WindowProperties;

public class TKHH extends JDialog {

	private JComboBox<String> cboNhomHang;
	private JLabel lblFrom;
	private JLabel lblHeader;
	private JLabel lblNhomHang;
	private JLabel lblTo;
	private JLabel lblTongDoanhSo;
	private JLabel lblTongHangBan;
	private JPanel pnlFilter;
	private JPanel pnlHeader;
	private JPanel pnlPieChart;
	private JPanel pnlSummary;
	private JPanel pnlTable1;
	private JPanel pnlTable2;
	private JScrollPane scrollTable1;
	private JScrollPane scrollTable2;
	private JTable table1;
	private JTable table2;
	private JDatePicker txtFrom;
	private JDatePicker txtTo;

	List<DanhMuc> listDM = new DanhMucDAO().selectAll();
	HashMap<String, Object> map = new HashMap<>();
	UtilDateModel dateFromModel = new UtilDateModel();
	UtilDateModel dateToModel = new UtilDateModel();
	String dateFormat = "dd/MM/yyyy";

	public TKHH(Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
		Customize();
	}

	private void Customize() {
		WindowProperties.setApplicationIcon(this);
		WindowProperties.centeringWindow(this);
		WindowProperties.CellRenderTable(table1, table1.getColumnCount());
		WindowProperties.CellRenderTable(table2, table2.getColumnCount());
		//
		Date now = new Date();
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DATE, 1);
		Date firstDateofMonth = c.getTime();
		dateFromModel.setValue(firstDateofMonth);
		dateToModel.setValue(now);
		LoadCboNhomHang();
	}

	private void LoadCboNhomHang() {
		cboNhomHang.removeAllItems();
		cboNhomHang.addItem("Tất cả mặt hàng");
		for (DanhMuc dm : listDM) {
			cboNhomHang.addItem(dm.getTenDanhMuc());
		}
		DrawPieChartPanel();
		cboNhomHang.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					DrawPieChartPanel();
				}
			}
		});
		dateFromModel.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("day")) {
					if (dateFromModel.getValue().compareTo(dateToModel.getValue()) > 0) {
						MessageBox.alert("Ngày bắt đầu không thể lớn hơn ngày kết thúc");
						dateFromModel.setValue(dateToModel.getValue());
						return;
					}
					DrawPieChartPanel();
				}
			}
		});
		dateToModel.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("day")) {
					if (dateToModel.getValue().compareTo(new Date()) > 0) {
						MessageBox.alert("Ngày kết thúc không thể lớn hơn ngày hiện tại");
						dateFromModel.setValue(new Date());
						return;
					}
					DrawPieChartPanel();
				}
			}
		});
	}
	

	private void DrawPieChartPanel() {
		pnlPieChart.removeAll();
		pnlPieChart.repaint();
		pnlPieChart.revalidate();
		Date from = dateFromModel.getValue();
		Date to = dateToModel.getValue();
		ThongKeDAO.ThongKeHH_byMaDM(table1, from, to, dateFormat);
		map.clear();
		if (cboNhomHang.getSelectedIndex() == 0) {
			map = ThongKeDAO.ThongKeHH_getDataset(from, to, "%");
			ThongKeDAO.ThongKeHH_byMaDM(table1, from, to, "%");
			ThongKeDAO.ThongKeDS_byProduct(table2, from, to, "%");
			setTextSumary(from, to, "%");
		} else {
			String MaDM = listDM.get(cboNhomHang.getSelectedIndex() - 1).getMaDanhMuc() + "";
			map = ThongKeDAO.ThongKeHH_getDataset(from, to, MaDM);
			ThongKeDAO.ThongKeHH_byMaDM(table1, from, to, MaDM);
			ThongKeDAO.ThongKeDS_byProduct(table2, from, to, MaDM);
			setTextSumary(from, to, MaDM);
		}
		JPanel chart = ChartGenerator.createPieChart("Biểu đồ hàng hóa", map);
		chart.setPreferredSize(new Dimension(pnlPieChart.getWidth(), pnlPieChart.getHeight()));
		pnlPieChart.add(chart, BorderLayout.CENTER);
	}
	
	private void setTextSumary(Date from, Date to, String MaDM) {
		HashMap<String, String> map = ThongKeDAO.ThongKeHH_Summary(from, to, MaDM);
		lblTongHangBan.setText("Tổng hàng bán:  " + map.get("TongHangBan"));
		lblTongDoanhSo.setText("Tổng doanh số:  " + map.get("TongDoanhSo"));
	}

	private void initComponents() {

		setTitle("Thống kê hàng hóa");

		pnlHeader = new JPanel();
		lblHeader = new JLabel();
		pnlFilter = new JPanel();
		lblNhomHang = new JLabel();
		cboNhomHang = new JComboBox<>();
		lblTo = new JLabel();
		txtTo = new JDatePicker(dateToModel, dateFormat);
		lblFrom = new JLabel();
		txtFrom = new JDatePicker(dateFromModel, dateFormat);
		pnlPieChart = new JPanel();
		pnlTable1 = new JPanel();
		scrollTable1 = new JScrollPane();
		table1 = new JTable();
		pnlTable2 = new JPanel();
		scrollTable2 = new JScrollPane();
		table2 = new JTable();
		pnlSummary = new JPanel();
		lblTongHangBan = new JLabel();
		lblTongDoanhSo = new JLabel();

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		
		lblHeader.setForeground(Theme.getColor("lblHeader.foreground"));
		lblHeader.setFont(new Font("Tahoma", 1, 15)); // NOI18N
		lblHeader.setHorizontalAlignment(SwingConstants.TRAILING);
		lblHeader.setText("THỐNG KÊ HÀNG HÓA");
		lblHeader.setHorizontalTextPosition(SwingConstants.CENTER);
		lblHeader.setPreferredSize(new Dimension(477, 30));

		GroupLayout pnlHeaderLayout = new GroupLayout(pnlHeader);
		pnlHeader.setLayout(pnlHeaderLayout);
		pnlHeaderLayout
				.setHorizontalGroup(pnlHeaderLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(lblHeader, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE));
		pnlHeaderLayout.setVerticalGroup(pnlHeaderLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlHeaderLayout.createSequentialGroup()
						.addComponent(lblHeader, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGap(0, 46, Short.MAX_VALUE)));

		lblNhomHang.setFont(new Font("Tahoma", 1, 14)); // NOI18N
		lblNhomHang.setText("Nhóm hàng");
		lblNhomHang.setPreferredSize(new Dimension(80, 20));

		cboNhomHang.setPreferredSize(new Dimension(180, 20));

		lblTo.setFont(new Font("Tahoma", 1, 14)); // NOI18N
		lblTo.setText("Đến");
		lblTo.setPreferredSize(new Dimension(35, 20));
		lblTo.setHorizontalAlignment(JLabel.CENTER);

		txtTo.setPreferredSize(new Dimension(150, 21));

		lblFrom.setFont(new Font("Tahoma", 1, 14)); // NOI18N
		lblFrom.setText("Từ");
		lblFrom.setPreferredSize(new Dimension(35, 20));
		lblFrom.setHorizontalAlignment(JLabel.CENTER);

		txtFrom.setPreferredSize(new Dimension(150, 21));

		GroupLayout pnlFilterLayout = new GroupLayout(pnlFilter);
		pnlFilter.setLayout(pnlFilterLayout);
		pnlFilterLayout.setHorizontalGroup(pnlFilterLayout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(GroupLayout.Alignment.TRAILING, pnlFilterLayout.createSequentialGroup()
						.addGroup(pnlFilterLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addGroup(pnlFilterLayout.createSequentialGroup()
										.addComponent(lblFrom, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(txtFrom, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addGap(11, 11, 11).addComponent(lblTo, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
								.addComponent(lblNhomHang, GroupLayout.PREFERRED_SIZE, 90,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(pnlFilterLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
								.addComponent(txtTo, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(cboNhomHang, 0, 1, Short.MAX_VALUE))
						.addGap(0, 0, 0)));
		pnlFilterLayout.setVerticalGroup(pnlFilterLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(GroupLayout.Alignment.TRAILING, pnlFilterLayout.createSequentialGroup()
						.addContainerGap()
						.addGroup(pnlFilterLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(lblNhomHang, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(cboNhomHang, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(pnlFilterLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(txtTo, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblFrom, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtFrom, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblTo, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addContainerGap()));

		pnlPieChart.setPreferredSize(new Dimension(338, 220));
		pnlPieChart.setLayout(new BorderLayout());

		table1.setAutoCreateRowSorter(true);
		table1.setModel(new DefaultTableModel(new Object[][] {

		}, new String[] { "Nhóm hàng", "SL hàng bán", "Doanh số" }) {
			Class[] types = new Class[] { java.lang.String.class, java.lang.String.class, java.lang.Long.class };
			boolean[] canEdit = new boolean[] { false, false, false };

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		table1.getTableHeader().setReorderingAllowed(false);
		scrollTable1.setViewportView(table1);

		GroupLayout pnlTable1Layout = new GroupLayout(pnlTable1);
		pnlTable1.setLayout(pnlTable1Layout);
		pnlTable1Layout
				.setHorizontalGroup(pnlTable1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(scrollTable1, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE));
		pnlTable1Layout.setVerticalGroup(pnlTable1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(scrollTable1, GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE));

		pnlTable2.setPreferredSize(new Dimension(815, 210));

		table2.setAutoCreateRowSorter(true);
		table2.setModel(new DefaultTableModel(new Object[][] {

		}, new String[] { "Mã hàng hóa", "Tên hàng hóa", "Nhóm hàng", "Số lượng bán", "Doanh số" }) {
			Class[] types = new Class[] { java.lang.String.class, java.lang.String.class, java.lang.String.class,
					java.lang.Integer.class, java.lang.Long.class };
			boolean[] canEdit = new boolean[] { false, false, false, false, false };

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		table2.getTableHeader().setReorderingAllowed(false);
		scrollTable2.setViewportView(table2);

		GroupLayout pnlTable2Layout = new GroupLayout(pnlTable2);
		pnlTable2.setLayout(pnlTable2Layout);
		pnlTable2Layout
				.setHorizontalGroup(pnlTable2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(scrollTable2, GroupLayout.Alignment.TRAILING));
		pnlTable2Layout.setVerticalGroup(pnlTable2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(scrollTable2, GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE));

		lblTongHangBan.setFont(new Font("Tahoma", 1, 13)); // NOI18N
		lblTongHangBan.setText("Tổng hàng bán:");

		lblTongDoanhSo.setFont(new Font("Tahoma", 1, 13)); // NOI18N
		lblTongDoanhSo.setText("Tổng doanh số:");
		lblTongDoanhSo.setPreferredSize(new Dimension(200, 27));

		GroupLayout pnlSummaryLayout = new GroupLayout(pnlSummary);
		pnlSummary.setLayout(pnlSummaryLayout);
		pnlSummaryLayout
				.setHorizontalGroup(pnlSummaryLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(pnlSummaryLayout.createSequentialGroup().addContainerGap(342, Short.MAX_VALUE)
								.addComponent(lblTongHangBan, GroupLayout.PREFERRED_SIZE, 200,
										GroupLayout.PREFERRED_SIZE)
								.addGap(18, 18, 18)
								.addComponent(lblTongDoanhSo, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addContainerGap()));
		pnlSummaryLayout
				.setVerticalGroup(pnlSummaryLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(pnlSummaryLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(lblTongDoanhSo, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblTongHangBan, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGap(15, 15, 15)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(pnlSummary, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(pnlTable2, GroupLayout.DEFAULT_SIZE, 770, Short.MAX_VALUE)
								.addGroup(layout.createSequentialGroup()
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
												.addComponent(pnlHeader, GroupLayout.DEFAULT_SIZE,
														GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(pnlPieChart, GroupLayout.DEFAULT_SIZE, 371,
														Short.MAX_VALUE))
										.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
												.addComponent(pnlTable1, GroupLayout.DEFAULT_SIZE,
														GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(pnlFilter, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE,
														GroupLayout.PREFERRED_SIZE))))
						.addGap(15, 15, 15)));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGap(15, 15, 15)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(pnlHeader, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(pnlFilter, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(pnlTable1, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(pnlPieChart, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGap(10, 10, 10)
						.addComponent(pnlTable2, GroupLayout.DEFAULT_SIZE,
								GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(pnlSummary, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));

		pack();
	}

}
