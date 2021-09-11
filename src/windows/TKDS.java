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
import utils.MessageBox;
import utils.Theme;
import utils.WindowProperties;

public class TKDS extends JDialog {
	private JComboBox<String> cboFilter;
	private JLabel lblDoanhSoTheo;
	private JLabel lblFrom;
	private JLabel lblHeader;
	private JLabel lblSoDonHang;
	private JLabel lblTienBanHang;
	private JLabel lblTo;
	private JPanel pnlFilter;
	private JPanel pnlSoDonHang;
	private JPanel pnlSummary;
	private JPanel pnlTable;
	private JPanel pnlTienBanHang;
	private JScrollPane scrollTable;
	private JTable tblList;
	private JDatePicker txtFromTime;
	private JLabel txtSoDonHang;
	private JLabel txtTienBanHang;
	private JDatePicker txtToTime;

	UtilDateModel dateFromModel = new UtilDateModel();
	UtilDateModel dateToModel = new UtilDateModel();
	String dateFormat = "dd/MM/yyyy";
	List<DanhMuc> listDM = new DanhMucDAO().selectAll();

	public TKDS(Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
		Customize();
	}

	private void Customize() {
		WindowProperties.setApplicationIcon(this);
		WindowProperties.centeringWindow(this);
//	
		String[] object = { "Hàng hóa", "Thời gian", "Người bán" };
		cboFilter.removeAllItems();
		for (int i = 0; i < object.length; i++) {
			cboFilter.addItem(object[i]);
		}
		Date now = new Date();
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DATE, 1);
		Date firstDateofMonth = c.getTime();
		dateFromModel.setValue(firstDateofMonth);
		dateToModel.setValue(now);
		setTable(0);
		cboFilter.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					setTable(cboFilter.getSelectedIndex());
				}
			}
		});

		dateFromModel.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ("day".equals(evt.getPropertyName())) {
					if (dateFromModel.getValue().compareTo(dateToModel.getValue()) > 0) {
						MessageBox.alert("Ngày bắt đầu không được lớn hơn ngày hết thúc");
						dateFromModel.setValue(dateToModel.getValue());
						return;
					}
					setTable(cboFilter.getSelectedIndex());
				}
			}
		});

		dateToModel.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ("day".equals(evt.getPropertyName())) {
					if (dateToModel.getValue().compareTo(new Date()) > 0) {
						MessageBox.alert("Ngày kết thúc không được lớn hơn ngày hiện tại");
						dateToModel.setValue(new Date());
						return;
					}
					setTable(cboFilter.getSelectedIndex());
				}
			}
		});
	}

	private void setSummaryText(Date from, Date to) {
		HashMap<String, String> map = ThongKeDAO.ThongKeDS_Summary(from, to);
		txtTienBanHang.setText(map.get("TienBanHang"));
		txtSoDonHang.setText(map.get("SoDonHang"));
	}

	private void setTable(int index) {
		pnlTable.removeAll();
		pnlTable.repaint();
		pnlTable.revalidate();
		Date from = dateFromModel.getValue(), to = dateToModel.getValue();
		setSummaryText(from, to);
		switch (index) {
		case 0:
			tblList = new TableHangHoa(scrollTable, from, to);
			break;
		case 1:
			tblList = new TableThoiGian(scrollTable, from, to);
			break;
		case 2:
			tableDS_byNV(from, to);
			break;
		default:
			tblList = new TableThoiGian(scrollTable, from, to);
			break;
		}
		pnlTable.add(scrollTable, BorderLayout.CENTER);
	}

	private void tableDS_byNV(Date from, Date to) {
		JPanel pnlNhomHang = new JPanel(null);
		pnlNhomHang.setPreferredSize(new Dimension(770, 30));
		JComboBox<String> cboNhomHang = new JComboBox<>();
		cboNhomHang.setMinimumSize(new Dimension(200, 25));
		cboNhomHang.addItem("Tất cả nhóm hàng");
		for (DanhMuc dm : listDM) {
			cboNhomHang.addItem(dm.getTenDanhMuc());
		}
		pnlNhomHang.add(cboNhomHang);
		cboNhomHang.setBounds(0, 0, 200, 25);
		pnlTable.add(pnlNhomHang, BorderLayout.NORTH);
		tblList = new TableNguoiBan(scrollTable, from, to, "%");
		cboNhomHang.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				int cboIndex = cboNhomHang.getSelectedIndex();
				if (cboIndex == 0) {
					tblList = new TableNguoiBan(scrollTable, from, to, "%");
				} else {
					String MaDM = String.valueOf(listDM.get(cboIndex - 1).getMaDanhMuc());
					tblList = new TableNguoiBan(scrollTable, from, to, MaDM);
				}
			}
		});
	}

	private void initComponents() {
		setTitle("Thống kê doanh số");

		pnlSummary = new JPanel();
		lblHeader = new JLabel();
		pnlTienBanHang = new JPanel();
		lblTienBanHang = new JLabel();
		txtTienBanHang = new JLabel();
		pnlSoDonHang = new JPanel();
		lblSoDonHang = new JLabel();
		txtSoDonHang = new JLabel();
		pnlFilter = new JPanel();
		lblDoanhSoTheo = new JLabel();
		cboFilter = new JComboBox<>();
		lblFrom = new JLabel();
		txtFromTime = new JDatePicker(dateFromModel, dateFormat);
		lblTo = new JLabel();
		txtToTime = new JDatePicker(dateToModel, dateFormat);
		pnlTable = new JPanel();
		scrollTable = new JScrollPane();

		setPreferredSize(new Dimension(800, 600));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);

		lblHeader.setForeground(Theme.getColor("lblHeader.foreground"));
		lblHeader.setFont(new Font("Tahoma", 1, 15)); // NOI18N
		lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeader.setText("THỐNG KÊ DOANH SỐ");
		lblHeader.setHorizontalTextPosition(SwingConstants.CENTER);
		lblHeader.setPreferredSize(new Dimension(477, 30));

		pnlTienBanHang.setBackground(new Color(43, 120, 228));
		pnlTienBanHang.setForeground(new Color(230, 230, 230));

		lblTienBanHang.setFont(new Font("Tahoma", 1, 14)); // NOI18N
		lblTienBanHang.setForeground(new Color(235, 235, 235));
		lblTienBanHang.setHorizontalAlignment(SwingConstants.CENTER);
		lblTienBanHang.setText("Tiền bán hàng");

		txtTienBanHang.setFont(new Font("Tahoma", 1, 14)); // NOI18N
		txtTienBanHang.setForeground(new Color(235, 235, 235));
		txtTienBanHang.setHorizontalAlignment(SwingConstants.CENTER);
		txtTienBanHang.setText("0");

		GroupLayout pnlTienBanHangLayout = new GroupLayout(pnlTienBanHang);
		pnlTienBanHang.setLayout(pnlTienBanHangLayout);
		pnlTienBanHangLayout.setHorizontalGroup(pnlTienBanHangLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlTienBanHangLayout.createSequentialGroup().addContainerGap()
						.addGroup(pnlTienBanHangLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(txtTienBanHang, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(lblTienBanHang, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE))
						.addContainerGap()));
		pnlTienBanHangLayout.setVerticalGroup(pnlTienBanHangLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlTienBanHangLayout.createSequentialGroup().addContainerGap().addComponent(lblTienBanHang)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(txtTienBanHang,
								GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap()));

		pnlSoDonHang.setBackground(new Color(0, 158, 15));
		pnlSoDonHang.setForeground(new Color(230, 230, 230));

		lblSoDonHang.setFont(new Font("Tahoma", 1, 14)); // NOI18N
		lblSoDonHang.setForeground(new Color(235, 235, 235));
		lblSoDonHang.setHorizontalAlignment(SwingConstants.CENTER);
		lblSoDonHang.setText("Số đơn hàng");

		txtSoDonHang.setFont(new Font("Tahoma", 1, 14)); // NOI18N
		txtSoDonHang.setForeground(new Color(235, 235, 235));
		txtSoDonHang.setHorizontalAlignment(SwingConstants.CENTER);
		txtSoDonHang.setText("0");

		GroupLayout pnlSoDonHangLayout = new GroupLayout(pnlSoDonHang);
		pnlSoDonHang.setLayout(pnlSoDonHangLayout);
		pnlSoDonHangLayout.setHorizontalGroup(pnlSoDonHangLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlSoDonHangLayout.createSequentialGroup().addContainerGap()
						.addGroup(pnlSoDonHangLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(lblSoDonHang, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(txtSoDonHang, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE))
						.addContainerGap()));
		pnlSoDonHangLayout.setVerticalGroup(pnlSoDonHangLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlSoDonHangLayout.createSequentialGroup().addContainerGap().addComponent(lblSoDonHang)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(txtSoDonHang, GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE).addContainerGap()));

		GroupLayout pnlSummaryLayout = new GroupLayout(pnlSummary);
		pnlSummary.setLayout(pnlSummaryLayout);
		pnlSummaryLayout
				.setHorizontalGroup(
						pnlSummaryLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addGroup(pnlSummaryLayout.createSequentialGroup().addContainerGap()
										.addGroup(pnlSummaryLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
												.addComponent(lblHeader, GroupLayout.DEFAULT_SIZE,
														GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addGroup(pnlSummaryLayout.createSequentialGroup()
														.addComponent(pnlTienBanHang, GroupLayout.DEFAULT_SIZE,
																GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
														.addGap(29, 29, 29)
														.addComponent(pnlSoDonHang, GroupLayout.DEFAULT_SIZE,
																GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
														.addGap(182, 182, 182)))
										.addContainerGap()));
		pnlSummaryLayout.setVerticalGroup(pnlSummaryLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlSummaryLayout.createSequentialGroup()
						.addComponent(lblHeader, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addGap(15, 15, 15)
						.addGroup(pnlSummaryLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(pnlTienBanHang, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(pnlSoDonHang, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))));

		pnlTable.setLayout(new BorderLayout());
		pnlTable.setPreferredSize(new Dimension(770, 442));

		lblDoanhSoTheo.setFont(new Font("Tahoma", 1, 12)); // NOI18N
		lblDoanhSoTheo.setText("Doanh số theo");
		lblDoanhSoTheo.setPreferredSize(new Dimension(84, 25));

		cboFilter.setModel(new DefaultComboBoxModel<>(new String[] { " " }));
		cboFilter.setPreferredSize(new Dimension(84, 25));

		lblFrom.setFont(new Font("Tahoma", 1, 12)); // NOI18N
		lblFrom.setText("Từ");
		lblFrom.setPreferredSize(new Dimension(84, 25));

		txtFromTime.setPreferredSize(new Dimension(84, 25));

		lblTo.setFont(new Font("Tahoma", 1, 12)); // NOI18N
		lblTo.setText("Đến");
		lblTo.setPreferredSize(new Dimension(84, 25));

		txtToTime.setPreferredSize(new Dimension(84, 25));

		GroupLayout pnlFilterLayout = new GroupLayout(pnlFilter);
		pnlFilter.setLayout(pnlFilterLayout);
		pnlFilterLayout.setHorizontalGroup(pnlFilterLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
				GroupLayout.Alignment.TRAILING,
				pnlFilterLayout.createSequentialGroup().addGap(0, 0, 0).addGroup(pnlFilterLayout
						.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
						.addComponent(lblFrom, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
								GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lblTo, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lblDoanhSoTheo, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE))
						.addGap(10, 10, 10)
						.addGroup(pnlFilterLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
								.addComponent(txtFromTime, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(txtToTime, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 170,
										Short.MAX_VALUE)
								.addComponent(cboFilter, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGap(0, 0, Short.MAX_VALUE)));
		pnlFilterLayout.setVerticalGroup(pnlFilterLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlFilterLayout.createSequentialGroup()
						.addGroup(pnlFilterLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(lblDoanhSoTheo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(cboFilter, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addGap(15, 15, 15)
						.addGroup(pnlFilterLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(lblFrom, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(txtFromTime, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(pnlFilterLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(lblTo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(txtToTime, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
				GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup().addGap(15, 15, 15).addGroup(layout
						.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addComponent(pnlTable, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(layout.createSequentialGroup()
								.addComponent(pnlSummary, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addGap(9, 9, 9).addComponent(pnlFilter, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGap(15, 15, 15)));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addGap(15, 15, 15)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(pnlFilter, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(pnlSummary, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
				.addComponent(pnlTable, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGap(15, 15, 15)));

		pack();
	}

}

class TableHangHoa extends JTable {

	private JTable table;

	public TableHangHoa(JScrollPane scroll, Date from, Date to) {
		table = new JTable();
		table.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Mã hàng hóa", "Tên hàng hóa", "Nhóm hàng", "Doanh số", "Số lượng" }) {
			Class[] types = { String.class, String.class, String.class, Long.class, Integer.class };

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			boolean[] canEdit = new boolean[] { false, false, false, false, false };

			public boolean isCellEditable(int row, int column) {
				return canEdit[column];
			}
		});
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoCreateRowSorter(true);
		scroll.setViewportView(table);
		WindowProperties.CellRenderTable(table, table.getColumnCount());
		ThongKeDAO.ThongKeDS_byProduct(table, from, to, "%");
	}
}

class TableThoiGian extends JTable {

	private JTable table;

	public TableThoiGian(JScrollPane scroll, Date from, Date to) {
		table = new JTable();
		table.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Đơn hàng", "Ngày bán", "Thu ngân", "Số lượng", "Doanh số" }) {
			Class[] types = { String.class, Date.class, String.class, Integer.class, Long.class };

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			boolean[] canEdit = new boolean[] { false, false, false, false, false};

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoCreateRowSorter(true);
		scroll.setViewportView(table);
		if (table.getColumnModel().getColumnCount() > 0) {
			table.getColumnModel().getColumn(3).setPreferredWidth(50);
		}
		WindowProperties.CellRenderTable(table, table.getColumnCount());
		ThongKeDAO.ThongKeDS_byTime(table, from, to);
	}

}

class TableNguoiBan extends JTable {

	private JTable table;

	public TableNguoiBan(JScrollPane scroll, Date from, Date to, String MaDM) {

		table = new JTable();
		table.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Nhân viên", "Doanh số", "Số đơn hàng", "Số lượng hàng" }) {
			Class[] types = { String.class, Long.class, Integer.class, Integer.class };

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			boolean[] canEdit = new boolean[] { false, false, false, false };

			public boolean isCellEditable(int row, int column) {
				return canEdit[column];
			}
		});
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoCreateRowSorter(true);
		scroll.setViewportView(table);
		WindowProperties.CellRenderTable(table, table.getColumnCount());
		ThongKeDAO.ThongKeDS_byStaff(table, from, to, MaDM);
	}
}
