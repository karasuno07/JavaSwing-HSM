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
import org.jfree.data.time.TimeSeriesDataItem;

import dao.DanhMucDAO;
import dao.ThongKeDAO;
import entities.DanhMuc;
import utils.ChartGenerator;
import utils.Theme;
import utils.WindowProperties;

public class TKLN extends JDialog {

	private JLabel lblDoanhSo;
	private JLabel lblHeader;
	private JLabel lblTienLai;
	private JLabel lblTienVon;
	private JPanel pnlChart;
	private JPanel pnlFilter;
	private JPanel pnlSummary;
	private JPanel pnlTable;
	private JScrollPane scrollTable;
	private JRadioButton rdoDate;
	private JRadioButton rdoProduct;
	private JLabel lblFrom;
	private JDatePicker txtFrom;
	private JLabel lblTo;
	private JDatePicker txtTo;
	private ButtonGroup btnFilter;
	private JTable tblList;
	JComboBox<String> cboNhomHang;

	UtilDateModel dateFromModel = new UtilDateModel();
	UtilDateModel dateToModel = new UtilDateModel();
	String dateFormat = "dd/MM/yyyy";
	List<DanhMuc> listDM = new DanhMucDAO().selectAll();
	HashMap<String,List<TimeSeriesDataItem>> mapByTime = new HashMap<>();
	HashMap<String, HashMap<String, Long>> mapByMaDM = new HashMap<>();

	public TKLN(Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
		Customize();
	}

	private void Customize() {
		WindowProperties.centeringWindow(this);
		WindowProperties.setApplicationIcon(this);
		//
		Date now = new Date();
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DATE, 1);
		Date firstDateofMonth = c.getTime();
		dateFromModel.setValue(firstDateofMonth);
		dateToModel.setValue(now);
		setTable(true);
		DrawXYLineChartPanel();
		setSummaryText("%");
		//
		rdoDate.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					setTable(true);
					DrawXYLineChartPanel();
				} else {
					setTable(false);
					DrawBarChartPanel();
				}
			}
		});
		dateFromModel.addPropertyChangeListener(new PropertyChangeListener() {	
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (rdoDate.isSelected()) {
					setTable(true);
					DrawXYLineChartPanel();
				} else {
					setTable(false);
					DrawBarChartPanel();
				}
			}
		});
		dateToModel.addPropertyChangeListener(new PropertyChangeListener() {	
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (rdoDate.isSelected()) {
					setTable(true);
					DrawXYLineChartPanel();
				} else {
					setTable(false);
					DrawBarChartPanel();
				}
			}
		});
	}
	
	private void DrawXYLineChartPanel() {
		pnlChart.removeAll();
		pnlChart.repaint();
		pnlChart.revalidate();
		Date from = dateFromModel.getValue();
		Date to = dateToModel.getValue();
		JPanel chart;
		mapByTime.clear();
		mapByTime = ThongKeDAO.ThongKeLN_getLineChartDataset(from, to);
		chart = ChartGenerator.createXYLineChart("Thống kê lợi nhuận theo ngày", null, null, mapByTime);
		chart.setPreferredSize(new Dimension(pnlChart.getWidth(), pnlChart.getHeight()));
		pnlChart.add(chart, BorderLayout.CENTER);
		setSummaryText("%");
	}
	
	private void DrawBarChartPanel() {
		String MaDM = "%";
		int cboIndex = cboNhomHang.getSelectedIndex();
		if (cboIndex != 0) {
			MaDM = String.valueOf(listDM.get(cboIndex - 1).getMaDanhMuc());
		}
		pnlChart.removeAll();
		pnlChart.repaint();
		pnlChart.revalidate();
		Date from = dateFromModel.getValue();
		Date to = dateToModel.getValue();
		JPanel chart;
		mapByMaDM.clear();
		mapByMaDM = ThongKeDAO.ThongKeLN_getBarChartDataset(from, to, MaDM);
		chart = ChartGenerator.createStackedBarChart("Thống kê lợi nhuận theo từng mặt hàng", null, null, mapByMaDM);
		chart.setPreferredSize(new Dimension(pnlChart.getWidth(), pnlChart.getHeight()));
		pnlChart.add(chart, BorderLayout.CENTER);
		setSummaryText(MaDM);
	}

	private void setTable(boolean tab) {
		pnlTable.removeAll();
		pnlTable.repaint();
		pnlTable.revalidate();
		Date from = dateFromModel.getValue();
		Date to = dateToModel.getValue();
		if (tab) {
			tblList = new TableByTime(scrollTable, from, to);
		} else {
			tableProduct_byMaDM(from, to);
		}
		pnlTable.add(scrollTable, BorderLayout.CENTER);
	}
	
	private void tableProduct_byMaDM(Date from, Date to) {
		JPanel pnlNhomHang = new JPanel(null);
		pnlNhomHang.setPreferredSize(new Dimension(770, 30));
		cboNhomHang = new JComboBox<>();
		cboNhomHang.setMinimumSize(new Dimension(200, 25));
		cboNhomHang.setMinimumSize(new Dimension(200, 25));
		cboNhomHang.addItem("Tất cả nhóm hàng");
		for (DanhMuc dm : listDM) {
			cboNhomHang.addItem(dm.getTenDanhMuc());
		}
		pnlNhomHang.add(cboNhomHang);
		cboNhomHang.setBounds(0, 0, 200, 25);
		pnlTable.add(pnlNhomHang, BorderLayout.NORTH);
		tblList = new TableByProduct(scrollTable, from, to, "%");
		cboNhomHang.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				int cboIndex = cboNhomHang.getSelectedIndex();
				if (cboIndex == 0) {
					tblList = new TableByProduct(scrollTable, from, to, "%");
					DrawBarChartPanel();
				} else {
					String MaDM = String.valueOf(listDM.get(cboIndex - 1).getMaDanhMuc());
					tblList = new TableByProduct(scrollTable, from, to, MaDM);
					DrawBarChartPanel();
				}
			}
		});
	}
	
	private void setSummaryText(String MaDM) {
		Date from = dateFromModel.getValue();
		Date to = dateToModel.getValue();
		HashMap<String, String> map = ThongKeDAO.ThongKeLN_Summary(from, to, MaDM);
		lblDoanhSo.setText("Doanh số:  " + map.get("DoanhSo"));
		lblTienLai.setText("Tiền lãi:  " + map.get("TienLai"));
		lblTienVon.setText("Tiền vốn:  " + map.get("TienVon"));
	}

	private void initComponents() {
		setTitle("Thống kê lợi nhuận");
		
		lblHeader = new JLabel();
		pnlFilter = new JPanel();
		rdoDate = new JRadioButton();
		rdoProduct = new JRadioButton();
		lblFrom = new JLabel();
		txtFrom = new JDatePicker(dateFromModel, dateFormat);
		lblTo = new JLabel();
		txtTo = new JDatePicker(dateToModel, dateFormat);
		pnlChart = new JPanel();
		pnlTable = new JPanel();
		pnlSummary = new JPanel();
		lblTienVon = new JLabel();
		lblDoanhSo = new JLabel();
		lblTienLai = new JLabel();
		scrollTable = new JScrollPane();
		btnFilter = new ButtonGroup();
		btnFilter.add(rdoDate);
		btnFilter.add(rdoProduct);

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setPreferredSize(new Dimension(950, 700));
		setResizable(false);

		lblHeader.setForeground(Theme.getColor("lblHeader.foreground"));
		lblHeader.setFont(new Font("Tahoma", 1, 15)); // NOI18N
		lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeader.setText("THỐNG KÊ LỢI NHUẬN");
		lblHeader.setHorizontalTextPosition(SwingConstants.CENTER);
		lblHeader.setPreferredSize(new Dimension(477, 30));

		rdoDate.setFont(new Font("Tahoma", 0, 12)); // NOI18N
		rdoDate.setSelected(true);
		rdoDate.setText("Theo ngày");
		rdoDate.setPreferredSize(new Dimension(90, 25));

		rdoProduct.setFont(new Font("Tahoma", 0, 12)); // NOI18N
		rdoProduct.setText("Theo hàng hóa");
		rdoProduct.setPreferredSize(new Dimension(110, 25));

		lblTo.setFont(new Font("Tahoma", 1, 12)); // NOI18N
		lblTo.setText("Đến");
		lblTo.setPreferredSize(new Dimension(35, 25));

		txtTo.setPreferredSize(new Dimension(150, 25));

		lblFrom.setFont(new Font("Tahoma", 1, 12)); // NOI18N
		lblFrom.setText("Từ");
		lblFrom.setPreferredSize(new Dimension(35, 25));

		txtFrom.setPreferredSize(new Dimension(150, 25));

		GroupLayout pnlFilterLayout = new GroupLayout(pnlFilter);
		pnlFilter.setLayout(pnlFilterLayout);
		pnlFilterLayout
				.setHorizontalGroup(pnlFilterLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(pnlFilterLayout.createSequentialGroup().addGap(10, 10, 10)
								.addComponent(rdoDate, GroupLayout.PREFERRED_SIZE, 90,
										GroupLayout.PREFERRED_SIZE)
								.addGap(10, 10, 10)
								.addComponent(rdoProduct, GroupLayout.PREFERRED_SIZE, 110,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblFrom, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(txtFrom, GroupLayout.PREFERRED_SIZE, 150,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(lblTo, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(txtTo, GroupLayout.PREFERRED_SIZE, 150,
										GroupLayout.PREFERRED_SIZE)
								.addContainerGap()));
		pnlFilterLayout.setVerticalGroup(pnlFilterLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlFilterLayout.createSequentialGroup().addContainerGap().addGroup(pnlFilterLayout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(pnlFilterLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(rdoDate, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(rdoProduct, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(pnlFilterLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(lblTo, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtTo, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGroup(
										pnlFilterLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(lblFrom, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(txtFrom, GroupLayout.DEFAULT_SIZE,
														GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
						.addContainerGap(12, Short.MAX_VALUE)));

		pnlChart.setPreferredSize(new Dimension(772, 201));
		pnlChart.setLayout(new BorderLayout());

		pnlTable.setLayout(new BorderLayout());
		pnlTable.setPreferredSize(new Dimension(770, 220));

		lblTienVon.setFont(new Font("Tahoma", 1, 12)); // NOI18N
		lblTienVon.setText("Tiền vốn:");
		lblTienVon.setPreferredSize(new Dimension(130, 31));

		lblDoanhSo.setFont(new Font("Tahoma", 1, 12)); // NOI18N
		lblDoanhSo.setText("Doanh số:");
		lblDoanhSo.setPreferredSize(new Dimension(130, 31));

		lblTienLai.setFont(new Font("Tahoma", 1, 12)); // NOI18N
		lblTienLai.setText("Tiền lãi:");
		lblTienLai.setPreferredSize(new Dimension(130, 31));

		GroupLayout pnlSummaryLayout = new GroupLayout(pnlSummary);
		pnlSummary.setLayout(pnlSummaryLayout);
		pnlSummaryLayout
				.setHorizontalGroup(pnlSummaryLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(pnlSummaryLayout.createSequentialGroup()
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblTienVon, GroupLayout.PREFERRED_SIZE, 120,
										GroupLayout.PREFERRED_SIZE)
								.addGap(30, 30, 30)
								.addComponent(lblDoanhSo, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGap(30, 30, 30)
								.addComponent(lblTienLai, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addContainerGap()));
		pnlSummaryLayout
				.setVerticalGroup(pnlSummaryLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(pnlSummaryLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(lblTienLai, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblDoanhSo, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblTienVon, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGap(15, 15, 15)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(pnlSummary, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(pnlTable, GroupLayout.DEFAULT_SIZE, 770, Short.MAX_VALUE)
								.addComponent(pnlChart, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
								.addComponent(pnlFilter, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblHeader, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGap(15, 15, 15)));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGap(15, 15, 15)
						.addComponent(lblHeader, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(pnlFilter, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(pnlChart, GroupLayout.DEFAULT_SIZE,
								GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGap(10, 10, 10)
						.addComponent(pnlTable, GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(pnlSummary, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGap(15, 15, 15)));

		pack();
	}

}

class TableByTime extends JTable {
	private JTable table;

	public TableByTime(JScrollPane scroll, Date from, Date to) {
		table = new JTable();
		table.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Ngày", "Tiền hàng", "KM đơn hàng", "Phụ thu", "Doanh số", "Tiền vốn", "Tiền lãi" }) {
			Class[] types = { java.util.Date.class, Long.class, Long.class, Long.class, Long.class, Long.class, Long.class };

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			boolean[] canEdit = { false, false, false, false, false, false, false };

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoCreateRowSorter(true);
		scroll.setViewportView(table);
		WindowProperties.CellRenderTable(table, table.getColumnCount());
		ThongKeDAO.ThongKeLN_byTime(table, from, to);
	}
}

class TableByProduct extends JTable {
	private JTable table;

	public TableByProduct(JScrollPane scroll, Date from, Date to, String MaDM) {
		table = new JTable();
		table.setModel(new DefaultTableModel(
				new Object[][] {},
				new String[] {"Hàng hóa", "Tiền hàng", "KM đơn hàng", "Doanh số", "Tiền vốn", "Tiền lãi"}
				) {
			Class[] types = { String.class, Long.class, Long.class, Long.class, Long.class, Long.class};
			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}
			boolean[] canEdit = {false, false, false, false, false, false};
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoCreateRowSorter(true);
		scroll.setViewportView(table);
		WindowProperties.CellRenderTable(table, table.getColumnCount());
		ThongKeDAO.ThongKeLN_byMaDM(table, from, to, MaDM);
	}
}