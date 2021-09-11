package windows;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.jdatepicker.JDatePicker;
import org.jdatepicker.UtilDateModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import dao.MatHangDAO;
import dao.NhaCungCapDAO;
import dao.PhieuNhapDAO;
import entities.MatHang;
import entities.NhaCungCap;
import entities.PhieuNhapCT;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import utils.Authorizer;
import utils.DateFormatter;
import utils.ImageHandler;
import utils.InvoicePrinting;
import utils.MessageBox;
import utils.Theme;
import utils.Validator;
import utils.WindowProperties;

public class PhieuNhap extends JDialog {

	private JButton btnLuu;
	private JButton btnLuuIn;
	private JButton btnThemNCC;
	private JComboBox<String> cboNCC;
	private JLabel lblGhiChu;
	private JLabel lblHeader;
	private JLabel lblNCC;
	private JLabel lblNgayTao;
	private JScrollPane scrollGhiChu;
	private JScrollPane scrollList;
	private JTable tblList;
	private JTextArea txtGhiChu;
	private JDatePicker txtNgayTao;
	private JComboBox<String> txtTimKiem;

	List<MatHang> listMH = new MatHangDAO().selectAll();
	List<NhaCungCap> listNCC = new ArrayList<>();
	List<entities.PhieuNhap> listPN = new ArrayList<>();
	UtilDateModel dateModel = new UtilDateModel();

	public PhieuNhap(Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
		Customize();
	}

	public PhieuNhap(Frame parent, boolean modal, String... MaMH) {
		super(parent, modal);
		initComponents();
		Customize();
		addToList(MaMH);
	}


	private void Customize() {
		WindowProperties.centeringWindow(this);
		WindowProperties.setApplicationIcon(this);
		WindowProperties.setApplicationIcon(this);
		WindowProperties.CellRenderTable(tblList, tblList.getColumnCount());
		//
		btnThemNCC.setIcon(ImageHandler.imageMaker("resources/icons/plus.png", 15, 15));
		btnThemNCC.setBackground(getBackground());
		btnLuu.setBackground(Theme.getColor("PhieuNhap.btnLuu.background"));
		btnLuuIn.setForeground(new Color(242, 242, 242));
		btnLuuIn.setBackground(Theme.getColor("PhieuNhap.btnLuuIn.background"));
		txtTimKiem.putClientProperty("JTextField.placeholderText", "Nhập mã hoặc tên hàng hóa...");
		tblList.putClientProperty("terminateEditOnFocusLost", true);
		tblList.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ("tableCellEditor".equals(evt.getPropertyName())) {
					if (!tblList.isEditing())
						updateValue();
				}
			}
		});
		loadListMH();
		LoadComboBox();
		//
		Date now = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		dateModel.setDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
		dateModel.setSelected(true);
		//
		deletePopupMenu();
		btnThemNCC.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ThemNCC();
			}
		});
		btnLuu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LuuPhieuNhap(false);
			}
		});
		btnLuuIn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LuuPhieuNhap(true);
			}
		});
	}

	private void LuuPhieuNhap(boolean isToPrint) {
		if (Validator.isEmptyComboBox(cboNCC, "Chưa chọn nhà cung cấp") == false) return;
		String MaPhieuNhap = InvoicePrinting.generateInvoiceID("PN");
		String MaNV = Authorizer.user.getMaNV();
		int MaNCC = listNCC.get(cboNCC.getSelectedIndex()).getMaNCC();
		Date date = dateModel.getValue();
		java.sql.Date NgayNhap = DateFormatter.toSqlDate(date);
		int rowCount = tblList.getRowCount();
		long TongTien = 0;
		for (int i = 0; i < rowCount; i++) {
			if (tblList.getValueAt(i, 5) == null) {
				MessageBox.alert("Chưa thêm số lượng và giá nhập cho sản phẩm " + tblList.getValueAt(i, 2));
				return;
			}
			TongTien += (long) tblList.getValueAt(i, 5);
		}
		
		String GhiChu = txtGhiChu.getText();
		entities.PhieuNhap pn = new entities.PhieuNhap(MaPhieuNhap, MaNV, MaNCC, NgayNhap, TongTien, GhiChu);
		if (new PhieuNhapDAO().insert(pn)) {
			listPN.clear();
			listPN = new PhieuNhapDAO().selectAll();
			// insert thông tin chi tiết của phiếu nhập
			PNCT(MaPhieuNhap);
			if (isToPrint) {
				InvoicePrinting.XuatHD("MaPhieu", MaPhieuNhap, "resources/printTemplate/HoaDonNhapHang.jrxml");
			} else {
				MessageBox.notif("Lưu phiếu nhập thành công");
			}
			dispose();
		} else {
			MessageBox.alert("Đã có lỗi xảy ra, vui lòng thử lại");
		}
	}
	
	private void PNCT(String MaPhieu) {
		int rowCount = tblList.getRowCount();
		for (int i = 0; i < rowCount; i++) {
			String MaHang = tblList.getValueAt(i, 1).toString();
			String TenHang = tblList.getValueAt(i, 2).toString();
			int SL = (int) tblList.getValueAt(i, 3);
			long GiaNhap = (long) tblList.getValueAt(i, 4);
			long ThanhTien = (long) tblList.getValueAt(i, 5);
			PhieuNhapCT pnct = new PhieuNhapCT(MaPhieu, MaHang, TenHang, SL, GiaNhap, ThanhTien);
			new PhieuNhapDAO().insertDetails(pnct);
		}
	}

	private void ThemNCC() {
		String TenNCC = MessageBox.prompt("Nhập tên nhà cung cấp cần tạo", "Thêm nhà cung cấp mới");
		if (TenNCC == null)
			return;
		NhaCungCap ncc = new NhaCungCap(TenNCC, null, null, null, null);
		if (new NhaCungCapDAO().insert(ncc)) {
			MessageBox.notif("Thêm nhà cung cấp thành công");
			LoadComboBox();
			cboNCC.setSelectedItem(TenNCC);
		} else {
			MessageBox.alert("Đã có lỗi xảy ra, vui lòng thử lại");
		}
	}

	private void loadListMH() {

		AutoCompleteDecorator.decorate(txtTimKiem);
		for (MatHang mh : listMH) {
			txtTimKiem.addItem(mh.getMaHang() + " | " + mh.getTenHang());
		}
		txtTimKiem.getEditor().setItem("");
		txtTimKiem.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String item = txtTimKiem.getEditor().getItem().toString();
				for (MatHang mh : listMH) {
					if (mh.getTenHang().toLowerCase().equals(item.toLowerCase())) {
						txtTimKiem.setSelectedIndex(listMH.indexOf(mh));
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER && txtTimKiem.getSelectedIndex() != -1) {
					String MaMH = item.substring(0, item.indexOf(" "));
					addToList(MaMH);
				}
			}
		});
	}

	private void LoadComboBox() {
		cboNCC.removeAllItems();
		listNCC.clear();
		listNCC = new NhaCungCapDAO().selectAll();
		for (NhaCungCap ncc : listNCC) {
			cboNCC.addItem(ncc.getTenNCC());
		}
	}

	private void addToList(String... MaMH) {
		DefaultTableModel model = (DefaultTableModel) tblList.getModel();
		for (int i = 0; i < MaMH.length; i++) {
			for (MatHang hang : listMH) {
				if (hang.getMaHang().equals(MaMH[i])) {
					int stt = tblList.getRowCount() + 1;
					Object[] row = { stt, hang.getMaHang(), hang.getTenHang(), null, null, null };
					model.addRow(row);
				}
			}
		}
	}

	private void deletePopupMenu() {
		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem delete = new JMenuItem("Xóa khỏi danh sách");
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = tblList.getSelectedRow();
				if (row < 0)
					return;
				DefaultTableModel model = (DefaultTableModel) tblList.getModel();
				model.removeRow(row);
			}
		});
		popupMenu.add(delete);
		tblList.setComponentPopupMenu(popupMenu);
		tblList.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				int row = tblList.rowAtPoint(e.getPoint());
				if (row >= 0)
					tblList.setRowSelectionInterval(row, row);
			}
		});
		tblList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				int row = tblList.rowAtPoint(e.getPoint());
				if (row < 0)
					tblList.clearSelection();
			}
		});
	}

	private void updateValue() {
		int row = tblList.getSelectedRow();
		if (row < 0)
			return;
		if (tblList.getValueAt(row, 3) == null || tblList.getValueAt(row, 4) == null)
			return;
		int SL = (int) tblList.getValueAt(row, 3);
		long Gia = (long) tblList.getValueAt(row, 4);
		long ThanhTien = SL * Gia;
		tblList.setValueAt(ThanhTien, row, 5);
	}

	private void initComponents() {
		setTitle("Phiếu nhập hàng");

		lblHeader = new JLabel();
		txtTimKiem = new JComboBox<>();
		scrollList = new JScrollPane();
		tblList = new JTable();
		lblNCC = new JLabel();
		cboNCC = new JComboBox<>();
		btnThemNCC = new JButton();
		lblNgayTao = new JLabel();
		txtNgayTao = new JDatePicker(dateModel, "dd/MM/yyyy");
		lblGhiChu = new JLabel();
		scrollGhiChu = new JScrollPane();
		txtGhiChu = new JTextArea();
		btnLuu = new JButton();
		btnLuuIn = new JButton();

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		
		lblHeader.setForeground(Theme.getColor("lblHeader.foreground"));
		lblHeader.setFont(new Font("Tahoma", 1, 15)); // NOI18N
		lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeader.setText("PHIẾU NHẬP");
		lblHeader.setHorizontalTextPosition(SwingConstants.CENTER);
		lblHeader.setPreferredSize(new Dimension(477, 30));

		txtTimKiem.setPreferredSize(new Dimension(420, 24));
		txtTimKiem.setEditable(true);

		tblList.setModel(new DefaultTableModel(new Object[][] {

		}, new String[] { "#", "Mã hàng", "Hàng hóa", "Số lượng", "Giá nhập", "Thành tiền"}) {
			Class[] types = new Class[] { java.lang.Integer.class, String.class, java.lang.String.class, java.lang.Integer.class,
					java.lang.Long.class, java.lang.Long.class};
			boolean[] canEdit = new boolean[] { false, false, false, true, true, false};

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		scrollList.setViewportView(tblList);
		if (tblList.getColumnModel().getColumnCount() > 0) {
			tblList.getColumnModel().getColumn(1).setMaxWidth(0);
			tblList.getColumnModel().getColumn(1).setMinWidth(0);
			tblList.getColumnModel().getColumn(1).setPreferredWidth(0);
			tblList.getColumnModel().getColumn(2).setPreferredWidth(120);
			tblList.getColumnModel().getColumn(0).setPreferredWidth(20);
			tblList.getColumnModel().getColumn(3).setPreferredWidth(60);
		}
		tblList.getTableHeader().setReorderingAllowed(false);

		lblNCC.setFont(new Font("Tahoma", 1, 12)); // NOI18N
		lblNCC.setText("Nhà cung cấp");
		lblNCC.setPreferredSize(new Dimension(100, 24));

		cboNCC.setPreferredSize(new Dimension(315, 24));

		btnThemNCC.setBorder(null);
		btnThemNCC.setMargin(new Insets(0, 0, 0, 0));
		btnThemNCC.setPreferredSize(new Dimension(24, 24));

		lblNgayTao.setFont(new Font("Tahoma", 1, 12)); // NOI18N
		lblNgayTao.setText("Ngày tạo");
		lblNgayTao.setPreferredSize(new Dimension(100, 24));

		txtNgayTao.setPreferredSize(new Dimension(321, 24));
		txtNgayTao.setTextEditable(true);

		lblGhiChu.setFont(new Font("Tahoma", 1, 12)); // NOI18N
		lblGhiChu.setText("Ghi chú");
		lblGhiChu.setVerticalAlignment(SwingConstants.TOP);
		lblGhiChu.setPreferredSize(new Dimension(100, 100));
		lblGhiChu.setVerticalTextPosition(SwingConstants.TOP);

		txtGhiChu.setColumns(20);
		txtGhiChu.setRows(5);
		scrollGhiChu.setViewportView(txtGhiChu);

		btnLuu.setFont(new Font("Tahoma", 1, 12)); // NOI18N
		btnLuu.setText("Lưu");
		btnLuu.setBorderPainted(false);
		btnLuu.setMargin(new Insets(2, 5, 2, 5));
		btnLuu.setPreferredSize(new Dimension(60, 30));

		btnLuuIn.setFont(new Font("Tahoma", 1, 12)); // NOI18N
		btnLuuIn.setText("Lưu & In");
		btnLuuIn.setBorderPainted(false);
		btnLuuIn.setMargin(new Insets(2, 5, 2, 5));
		btnLuuIn.setPreferredSize(new Dimension(75, 30));

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGap(15, 15, 15)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addGroup(layout
										.createSequentialGroup().addGroup(layout.createParallelGroup(
												GroupLayout.Alignment.LEADING).addComponent(lblHeader,
														GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 0,
														Short.MAX_VALUE)
												.addComponent(txtTimKiem, GroupLayout.DEFAULT_SIZE,
														GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(scrollList, GroupLayout.PREFERRED_SIZE, 0,
														Short.MAX_VALUE)
												.addGroup(layout.createSequentialGroup().addGroup(
														layout.createParallelGroup(
																GroupLayout.Alignment.TRAILING)
																.addComponent(lblGhiChu, GroupLayout.PREFERRED_SIZE,
																		GroupLayout.DEFAULT_SIZE,
																		GroupLayout.PREFERRED_SIZE)
																.addComponent(
																		lblNgayTao, GroupLayout.PREFERRED_SIZE,
																		GroupLayout.PREFERRED_SIZE,
																		GroupLayout.PREFERRED_SIZE))
														.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
														.addGroup(layout
																.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addGroup(layout.createSequentialGroup()
																		.addComponent(btnLuu,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				LayoutStyle.ComponentPlacement.UNRELATED)
																		.addComponent(btnLuuIn,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE)
																		.addGap(0, 0, Short.MAX_VALUE))
																.addComponent(scrollGhiChu)
																.addComponent(txtNgayTao, GroupLayout.DEFAULT_SIZE,
																		GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
										.addGap(15, 15, 15))
								.addGroup(
										layout.createSequentialGroup()
												.addComponent(lblNCC, GroupLayout.PREFERRED_SIZE, 100,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(cboNCC, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
												.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
												.addComponent(btnThemNCC, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addGap(18, 18, 18)))));
		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup().addGap(15, 15, 15)
								.addComponent(lblHeader, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(txtTimKiem, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(scrollList, GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
								.addGap(18, 18, 18)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(lblNCC, GroupLayout.PREFERRED_SIZE,
														GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(cboNCC, GroupLayout.PREFERRED_SIZE,
														GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
										.addComponent(btnThemNCC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(lblNgayTao, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(txtNgayTao, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
								.addGap(11, 11, 11)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(lblGhiChu, GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
										.addComponent(scrollGhiChu))
								.addGap(18, 18, 18)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
										.addComponent(btnLuu, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(btnLuuIn, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
								.addGap(15, 15, 15)));

		pack();
	}

	public static void main(String args[]) {
		WindowProperties.setFlatLaf_LookAndFeel();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				PhieuNhap dialog = new PhieuNhap(new JFrame(), true);
				dialog.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						System.exit(0);
					}
				});
				dialog.setVisible(true);
			}
		});
	}

}
