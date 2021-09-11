package windows;

import utils.Theme;
import utils.WindowProperties;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;

import dao.DanhMucDAO;
import dao.MatHangDAO;
import entities.DanhMuc;
import entities.MatHang;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class QLKho extends JFrame {

	private JComboBox<String> cboDanhMuc;
	private JButton btnNhap;
	private JLabel lblHeader;
	private JScrollPane scrollList;
	private JTable tblList;
	private JTextField txtTimKiem;

	List<DanhMuc> listDM = new ArrayList<>();
	List<MatHang> listKho = new ArrayList<>();

	public QLKho() {
		initComponents();
		Customize();
	}

	public void Customize() {
		WindowProperties.centeringWindow(this);
		WindowProperties.resizableWindow(this, 800, 600);
		WindowProperties.setApplicationIcon(this);
		WindowProperties.CellRenderTable(tblList, tblList.getColumnCount());
		//
		tblList.setAutoCreateRowSorter(true);
		txtTimKiem.putClientProperty("JTextField.placeholderText", "Nhập mã hoặc tên hàng hóa...");
		txtTimKiem.requestFocus();
		// event
		btnNhap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NhapHang();
			}
		});
		LoadComboBox();
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
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				HienThiTable();
			}
		});
	}

	private void LoadComboBox() {
		cboDanhMuc.addItem("Tất cả mặt hàng");
		HienThiTable();
		listDM.clear();
		listDM = new DanhMucDAO().selectAll();
		for (DanhMuc dm : listDM) {
			cboDanhMuc.addItem(dm.getTenDanhMuc());
		}
		cboDanhMuc.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				txtTimKiem.setText("");
				HienThiTable();
			}
		});
	}

	private void HienThiTable() {
		int index = cboDanhMuc.getSelectedIndex() - 1;
		int MaDM = index >= 0 ? listDM.get(index).getMaDanhMuc() : -1;
		DefaultTableModel model = (DefaultTableModel) tblList.getModel();
		model.setRowCount(0);
		listKho.clear();
		if (MaDM == -1) {
			listKho = new MatHangDAO().selectAll();
		} else {
			String SELECT_BY_MDM = "SELECT * FROM MatHang JOIN DanhMuc ON DanhMuc.MaDanhMuc = MatHang.MaDanhMuc JOIN KhoHang ON KhoHang.MaHang = MatHang.MaHang WHERE DanhMuc.MaDanhMuc=?";
			listKho = new MatHangDAO().select(SELECT_BY_MDM, MaDM);
		}
		for (MatHang hang : listKho) {
			Object[] row = { hang.getMaHang(), hang.getTenHang(), hang.getSoLuong(), hang.getTinhTrangHang(),
					hang.getGiaNhap() * hang.getSoLuong(), hang.getGiaBan() * hang.getSoLuong() };
			model.addRow(row);
		}
	}

	private void HienThiTimKiem(String key) {
		List<MatHang> list = new ArrayList<>();
		for (MatHang hang : listKho) {
			if (hang.getMaHang().toLowerCase().contains(key.toLowerCase())
					|| hang.getTenHang().toLowerCase().contains(key.toLowerCase())) {
				list.add(hang);
			}
		}
		DefaultTableModel model = (DefaultTableModel) tblList.getModel();
		model.setRowCount(0);
		for (MatHang hang : list) {
			Object[] row = { hang.getMaHang(), hang.getTenHang(), hang.getSoLuong(), hang.getTinhTrangHang(),
					hang.getGiaNhap() * hang.getSoLuong(), hang.getGiaBan() * hang.getSoLuong() };
			model.addRow(row);
		}
	}

	private void NhapHang() {
		int[] rows = tblList.getSelectedRows();
		if (rows.length == 0) {
			new PhieuNhap(this, true).setVisible(true);
		} else {
			String[] MaMH = new String[rows.length];
			for (int i = 0; i < rows.length; i++) {
				MaMH[i] = tblList.getValueAt(rows[i], 0).toString();
			}
			new PhieuNhap(this, true, MaMH).setVisible(true);
		}
	}

	private void initComponents() {

		lblHeader = new JLabel();
		cboDanhMuc = new JComboBox<>();
		txtTimKiem = new JTextField();
		scrollList = new JScrollPane();
		tblList = new JTable();
		btnNhap = new JButton();

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Quản lý kho hàng");
		
		btnNhap.setBackground(Theme.getColor("QLKho.btnNhap.background"));
		btnNhap.setForeground(Theme.getColor("QLKho.btnNhap.foreground"));
		lblHeader.setForeground(Theme.getColor("lblHeader.foreground"));	

		lblHeader.setFont(new Font("Tahoma", 1, 15)); // NOI18N
		lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeader.setText("QUẢN LÝ THÔNG TIN KHO HÀNG");
		lblHeader.setHorizontalTextPosition(SwingConstants.CENTER);
		lblHeader.setPreferredSize(new Dimension(477, 30));

		cboDanhMuc.setPreferredSize(new Dimension(150, 24));

		txtTimKiem.setPreferredSize(new Dimension(400, 24));

		tblList.setModel(new DefaultTableModel(new Object[][] {

		}, new String[] { "Mã hàng", "Tên hàng hóa", "Số lượng", "Tình trạng hàng", "Vốn tồn kho", "Giá trị tồn" }) {
			Class[] types = new Class[] { java.lang.String.class, java.lang.String.class, java.lang.Integer.class,
					java.lang.String.class, java.lang.Long.class, java.lang.Long.class };
			boolean[] canEdit = new boolean[] { false, false, false, false, false, false };

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		tblList.getTableHeader().setReorderingAllowed(false);
		tblList.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		scrollList.setViewportView(tblList);

		btnNhap.setFont(new Font("Tahoma", 1, 11)); // NOI18N
		btnNhap.setText("Nhập hàng");
		btnNhap.setBorderPainted(false);
		btnNhap.setMargin(new Insets(2, 5, 2, 5));
		btnNhap.setPreferredSize(new Dimension(90, 30));

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGap(15, 15, 15)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(scrollList)
								.addComponent(lblHeader, GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE)
								.addGroup(layout.createSequentialGroup()
										.addComponent(cboDanhMuc, 0, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
										.addGap(30, 30, 30)
										.addComponent(txtTimKiem, GroupLayout.PREFERRED_SIZE, 400, Short.MAX_VALUE))
								.addGroup(GroupLayout.Alignment.TRAILING,
										layout.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE).addComponent(
												btnNhap, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)))
						.addGap(15, 15, 15)));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addGap(15, 15, 15)
				.addComponent(
						lblHeader, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(cboDanhMuc, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(txtTimKiem, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
								GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
				.addComponent(scrollList, GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
				.addComponent(btnNhap, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				.addGap(15, 15, 15)));

		pack();
	}

}
