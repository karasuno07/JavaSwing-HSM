package windows;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.calendar.DateSelectionModel.SelectionMode;

import dao.DanhMucDAO;
import dao.ThongKeDAO;
import entities.DanhMuc;
import entities.MatHang;
import utils.MessageBox;
import utils.Theme;
import utils.WindowProperties;

public class TKKH extends JDialog {
	
	private javax.swing.JComboBox<String> cboNhomHang;
	private javax.swing.JLabel lblGiaTriTon;
	private javax.swing.JLabel lblHeader;
	private javax.swing.JLabel lblNhomHang;
	private javax.swing.JLabel lblTongTonKho;
	private javax.swing.JLabel lblVonTonKho;
	private javax.swing.JPanel pnlFilter;
	private javax.swing.JPanel pnlSummary;
	private javax.swing.JScrollPane scrollTable;
	private javax.swing.JTable tblList;
	private javax.swing.JTextField txtTimKiem;
	
	List<DanhMuc> listDM = new DanhMucDAO().selectAll();
	List<MatHang> listKho = new ArrayList<>();

	public TKKH(Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
		Customize();
	}
	
	private void Customize() {
		WindowProperties.setApplicationIcon(this);
		WindowProperties.centeringWindow(this);
		WindowProperties.CellRenderTable(tblList, tblList.getColumnCount());
		txtTimKiem.putClientProperty("JTextField.placeholderText", "Nhập tên hoặc mã hàng hóa...");
		loadCboNhomHang();
		txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				HienThiTimKiem(txtTimKiem.getText());
			}	
			@Override
			public void insertUpdate(DocumentEvent e) {
				HienThiTimKiem(txtTimKiem.getText());
			}	
			@Override
			public void changedUpdate(DocumentEvent e) {
				HienThiTimKiem(txtTimKiem.getText());
			}
		});
		tblList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					openDSPN();
				}
			}
		});
	}
	
	private void loadCboNhomHang() {
		cboNhomHang.removeAllItems();
		cboNhomHang.addItem("Tất cả hàng hóa");
		loadTable();
		for (DanhMuc dm : listDM) {
			cboNhomHang.addItem(dm.getTenDanhMuc());
		}
		cboNhomHang.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					txtTimKiem.setText("");
					loadTable();
				}
			}
		});
	}
	
	private void loadTable() {
		String MaDM = "%";
		int index = cboNhomHang.getSelectedIndex();
		if (index > 0) {
			MaDM = String.valueOf(listDM.get(index - 1).getMaDanhMuc());
		}
		DefaultTableModel model = (DefaultTableModel) tblList.getModel();
		model.setRowCount(0);
		listKho.clear();
		listKho = ThongKeDAO.ThongKeKho_byMaDM(MaDM);
		int TongSL = 0;
		long TongVonTon = 0, TongGiaTriTon = 0;
		for (MatHang mh : listKho) {
			Object[] row = { mh.getMaHang(), mh.getTenHang(), mh.getSoLuong(), mh.getGiaNhap(), mh.getGiaBan(), mh.getGiaNhap() * mh.getSoLuong(), mh.getGiaBan() *  mh.getSoLuong() };
			TongSL += mh.getSoLuong();
			TongVonTon += mh.getGiaNhap() * mh.getSoLuong();
			TongGiaTriTon += mh.getGiaBan() * mh.getSoLuong();
			model.addRow(row);
		}
		popupMenuInTable();
		lblTongTonKho.setText("Tổng hàng tồn kho:  " + String.valueOf(TongSL));
		lblVonTonKho.setText("Tổng vốn tồn kho:  " + String.valueOf(TongVonTon));
		lblGiaTriTon.setText("Tổng giá trị tồn:  " + String.valueOf(TongGiaTriTon));
	}
	
	private void HienThiTimKiem(String key) {
		List<MatHang> list = new ArrayList<>();
		for (MatHang mh : listKho) {
			if (mh.getMaHang().toLowerCase().contains(key.toLowerCase()) || mh.getTenHang().toLowerCase().contains(key.toLowerCase())) {
				list.add(mh);
			}
		}
		DefaultTableModel model = (DefaultTableModel) tblList.getModel();
		model.setRowCount(0);
		for (MatHang mh : list) {
			Object[] row = { mh.getMaHang(), mh.getTenHang(), mh.getSoLuong(), mh.getGiaNhap(), mh.getGiaBan(), mh.getGiaNhap() * mh.getSoLuong(), mh.getGiaBan() *  mh.getSoLuong() };
			model.addRow(row);
		}
	}
	
	private void popupMenuInTable() {
		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem seeList = new JMenuItem("Xem danh sách phiếu nhập");
		seeList.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				openDSPN();
			}
		});
		popupMenu.add(seeList);
		tblList.setComponentPopupMenu(popupMenu);
	}
	
	private void openDSPN() {
		int row = tblList.getSelectedRow();
		if (row < 0) {
			MessageBox.alert("Chưa chọn hàng hóa");
			return;
		}
		String MaHang = tblList.getValueAt(row, 0).toString();
		String TenHang = tblList.getValueAt(row, 1).toString();
		new DSPN(null, true, MaHang, TenHang).setVisible(true);
	}

	private void initComponents() {
		setTitle("Thống kê kho hàng");
		
		lblHeader = new javax.swing.JLabel();
		pnlFilter = new javax.swing.JPanel();
		txtTimKiem = new javax.swing.JTextField();
		cboNhomHang = new javax.swing.JComboBox<>();
		lblNhomHang = new javax.swing.JLabel();
		scrollTable = new javax.swing.JScrollPane();
		tblList = new javax.swing.JTable();
		pnlSummary = new javax.swing.JPanel();
		lblTongTonKho = new javax.swing.JLabel();
		lblVonTonKho = new javax.swing.JLabel();
		lblGiaTriTon = new javax.swing.JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);

		lblHeader.setForeground(Theme.getColor("lblHeader.foreground"));
		lblHeader.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
		lblHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		lblHeader.setText("THỐNG KÊ KHO HÀNG");
		lblHeader.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		lblHeader.setPreferredSize(new java.awt.Dimension(477, 30));

		txtTimKiem.setPreferredSize(new java.awt.Dimension(400, 25));

		cboNhomHang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
		cboNhomHang.setPreferredSize(new java.awt.Dimension(180, 25));

		javax.swing.GroupLayout pnlFilterLayout = new javax.swing.GroupLayout(pnlFilter);
		pnlFilter.setLayout(pnlFilterLayout);
		pnlFilterLayout
				.setHorizontalGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(pnlFilterLayout.createSequentialGroup()
								.addComponent(txtTimKiem, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGap(190, 190, 190)
								.addComponent(cboNhomHang, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		pnlFilterLayout.setVerticalGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 26,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addComponent(cboNhomHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
						javax.swing.GroupLayout.PREFERRED_SIZE));

		lblNhomHang.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
		lblNhomHang.setText("Nhóm hàng:");
		lblNhomHang.setPreferredSize(new java.awt.Dimension(70, 25));

		tblList.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

		}, new String[] { "Mã hàng hóa", "Tên hàng hóa", "SL tồn kho", "Giá nhập", "Giá bán hiện tại", "Vốn tồn kho",
				"Giá trị tồn" }) {
			Class[] types = new Class[] { java.lang.String.class, java.lang.String.class, java.lang.Integer.class,
					java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class };
			boolean[] canEdit = new boolean[] { false, false, false, false, false, false, false };

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		tblList.getTableHeader().setReorderingAllowed(false);
		scrollTable.setViewportView(tblList);
		tblList.setAutoCreateRowSorter(true);
		tblList.setFocusable(false);

		pnlSummary.setPreferredSize(new java.awt.Dimension(770, 30));

		lblTongTonKho.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
		lblTongTonKho.setText("Tổng hàng tồn kho:");
		lblTongTonKho.setPreferredSize(new java.awt.Dimension(160, 30));

		lblVonTonKho.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
		lblVonTonKho.setText("Vốn tồn kho:");
		lblVonTonKho.setPreferredSize(new java.awt.Dimension(180, 30));

		lblGiaTriTon.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
		lblGiaTriTon.setText("Giá trị tồn:");
		lblGiaTriTon.setPreferredSize(new java.awt.Dimension(180, 30));

		javax.swing.GroupLayout pnlSummaryLayout = new javax.swing.GroupLayout(pnlSummary);
		pnlSummary.setLayout(pnlSummaryLayout);
		pnlSummaryLayout
				.setHorizontalGroup(pnlSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(pnlSummaryLayout.createSequentialGroup()
								.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblTongTonKho, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(18, 18, 18)
								.addComponent(lblVonTonKho, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(18, 18, 18)
								.addComponent(lblGiaTriTon, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addContainerGap()));
		pnlSummaryLayout
				.setVerticalGroup(pnlSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(pnlSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(lblGiaTriTon, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblVonTonKho, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblTongTonKho, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGap(15, 15, 15)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(scrollTable, javax.swing.GroupLayout.Alignment.TRAILING)
								.addComponent(pnlFilter, javax.swing.GroupLayout.Alignment.TRAILING,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(lblHeader, javax.swing.GroupLayout.Alignment.TRAILING,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(pnlSummary, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGap(15, 15, 15))
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup().addGap(365, 365, 365)
								.addComponent(lblNhomHang, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addContainerGap(365, Short.MAX_VALUE))));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGap(15, 15, 15)
						.addComponent(lblHeader, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(pnlFilter, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(scrollTable, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(pnlSummary, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(18, 18, 18))
				.addGroup(
						layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(layout.createSequentialGroup().addGap(270, 270, 270)
										.addComponent(lblNhomHang, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addGap(271, 271, 271))));

		pack();
	}


}
