package windows;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import com.formdev.flatlaf.ui.FlatBorder;

import dao.MatHangDAO;
import entities.DanhMuc;
import entities.HoaDon;
import entities.KhachHang;
import entities.MatHang;
import dao.DanhMucDAO;
import dao.HoaDonDAO;
import dao.KhachHangDAO;
import utils.Authorizer;
import utils.ImageHandler;
import utils.InvoicePrinting;
import utils.JDBCHelper;
import utils.Language;
import utils.MessageBox;
import utils.Theme;
import utils.WindowProperties;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Date;

public class GiaoDienBanHang extends JPanel {

	private JButton btnChiTiet;
	public JButton btnThanhToan;
	private JComboBox<String> cboDanhMucHang;
	private JLabel lblAddCustomer;
	private JPanel pnlDanhSach;
	public static JPanel pnlList;
	private JPanel pnlSearchProducts;
	private JPanel pnlThanhToan;
	private JSeparator s1;
	private JScrollPane scrollPnlList;
	private JScrollPane scrollTblList;
	public static JTable tblList;
	private JComboBox<String> cboCustomers;
	private JTextField txtSearchProducts;
	private JPopupMenu popupTable;
	private JPopupMenu popupList;

	List<DanhMuc> listDM = new ArrayList<>();
	List<MatHang> listMH = new ArrayList<>();
	List<KhachHang> listKH = new KhachHangDAO().selectAll();
	public static List<DonHang> listDH = new ArrayList<>();
	static int STT = 0;
	int MaKH = 1;
	ResourceBundle languagePack = Language.getLanguagePack();
	ResourceBundle messagePack = Language.getMessagePack();

	public GiaoDienBanHang() {
		pnlThanhToan = new JPanel();
		pnlSearchProducts = new JPanel();
		txtSearchProducts = new JTextField();
		scrollPnlList = new JScrollPane();
		pnlList = new JPanel();
		s1 = new JSeparator();
		cboCustomers = new JComboBox<>();
		lblAddCustomer = new JLabel();
		btnThanhToan = new JButton();
		btnChiTiet = new JButton();
		pnlDanhSach = new JPanel();
		cboDanhMucHang = new JComboBox<>();
		scrollTblList = new JScrollPane();
		tblList = new JTable();

		setPreferredSize(new Dimension(844, 695));

		pnlThanhToan.setPreferredSize(new Dimension(350, 688));

		pnlSearchProducts.setBackground(new Color(8, 83, 148));
		pnlSearchProducts.setFocusable(false);
		pnlSearchProducts.setLayout(null);

		txtSearchProducts.setFont(new Font("Tahoma", 0, 12));
		txtSearchProducts.setForeground(new Color(102, 102, 102));
		txtSearchProducts.setMargin(new Insets(2, 5, 2, 5));
		pnlSearchProducts.add(txtSearchProducts);
		txtSearchProducts.setBounds(10, 12, 330, 21);
		txtSearchProducts.putClientProperty("JTextField.placeholderText", languagePack.getString("plhdProduct"));

		scrollPnlList.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPnlList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

		pnlList.setName("List DH");
		pnlList.setLayout(new GridLayout(20, 1, 0, 10));
		
		scrollPnlList.setViewportView(pnlList);
		scrollPnlList.setWheelScrollingEnabled(true);

		s1.setForeground(new Color(204, 204, 204));
		s1.setAlignmentX(0.0F);
		s1.setAlignmentY(0.0F);
		s1.setRequestFocusEnabled(false);
		s1.setVerifyInputWhenFocusTarget(false);

		cboCustomers.setFont(new Font("Tahoma", 0, 12));
		cboCustomers.setEditable(true);
		cboCustomers.setForeground(new Color(102, 102, 102));
		AutoCompleteDecorator.decorate(cboCustomers);
		cboCustomers.putClientProperty("JTextField.placeholderText", languagePack.getString("plhdCustomer"));

		lblAddCustomer.setHorizontalAlignment(SwingConstants.CENTER);
		lblAddCustomer.setCursor(new Cursor(Cursor.HAND_CURSOR));
		lblAddCustomer.setHorizontalTextPosition(SwingConstants.CENTER);

		btnThanhToan.setBackground(new Color(111, 168, 220));
		btnThanhToan.setFont(new Font("Tahoma", 1, 16));
		btnThanhToan.setText(languagePack.getString("btnThanhToan"));
		btnThanhToan.setBorder(null);
		btnThanhToan.setBorderPainted(false);
		btnThanhToan.setHorizontalTextPosition(SwingConstants.TRAILING);
		btnThanhToan.setMargin(new Insets(2, 20, 2, 20));

		btnChiTiet.setBorder(null);
		btnChiTiet.setMargin(new Insets(2, 5, 2, 5));

		GroupLayout pnlThanhToanLayout = new GroupLayout(pnlThanhToan);
		pnlThanhToan.setLayout(pnlThanhToanLayout);
		pnlThanhToanLayout.setHorizontalGroup(pnlThanhToanLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlThanhToanLayout.createSequentialGroup()
						.addGroup(pnlThanhToanLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addGroup(pnlThanhToanLayout.createSequentialGroup().addGap(10, 10, 10)
										.addComponent(cboCustomers, GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE))
								.addComponent(btnThanhToan, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE))
						.addGroup(pnlThanhToanLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(btnChiTiet, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addGroup(
										pnlThanhToanLayout.createSequentialGroup()
												.addComponent(lblAddCustomer, GroupLayout.DEFAULT_SIZE, 35,
														Short.MAX_VALUE)
												.addContainerGap())))
				.addComponent(scrollPnlList).addComponent(s1, GroupLayout.Alignment.TRAILING)
				.addComponent(pnlSearchProducts, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		pnlThanhToanLayout.setVerticalGroup(pnlThanhToanLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlThanhToanLayout.createSequentialGroup()
						.addComponent(pnlSearchProducts, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
						.addGap(0, 0, 0).addComponent(scrollPnlList, GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE)
						.addGap(0, 0, 0).addComponent(s1, GroupLayout.PREFERRED_SIZE, 4, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(pnlThanhToanLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
								.addComponent(cboCustomers).addComponent(lblAddCustomer, GroupLayout.PREFERRED_SIZE, 21,
										GroupLayout.PREFERRED_SIZE))
						.addGap(10, 10, 10)
						.addGroup(pnlThanhToanLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
								.addComponent(btnThanhToan, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(btnChiTiet, GroupLayout.PREFERRED_SIZE, 75,
										GroupLayout.PREFERRED_SIZE))));

		cboDanhMucHang.setFont(new Font("Tahoma", 0, 12));

		tblList.setAutoCreateRowSorter(true);
		tblList.setFont(new Font("Tahoma", 1, 12));
		tblList.setModel(new DefaultTableModel(new Object[][] {

		}, new String[] { languagePack.getString("image"), languagePack.getString("productID"),
				languagePack.getString("productName"), languagePack.getString("quantity"),
				languagePack.getString("price") }) {
			Class[] types = new Class[] { ImageIcon.class, String.class, String.class, Integer.class, Long.class };
			boolean[] canEdit = new boolean[] { false, false, false, false, false };

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		if (tblList.getColumnModel().getColumnCount() > 0) {
			tblList.getColumnModel().getColumn(0).setResizable(false);
			tblList.getColumnModel().getColumn(0).setPreferredWidth(120);
			tblList.getColumnModel().getColumn(3).setPreferredWidth(25);
		}
		tblList.setRowHeight(120);
		tblList.setFocusable(false);
		scrollTblList.setViewportView(tblList);
		tblList.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		WindowProperties.CellRenderTable(tblList, tblList.getModel().getColumnCount());

		GroupLayout pnlDanhSachLayout = new GroupLayout(pnlDanhSach);
		pnlDanhSach.setLayout(pnlDanhSachLayout);
		pnlDanhSachLayout.setHorizontalGroup(pnlDanhSachLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlDanhSachLayout.createSequentialGroup().addContainerGap()
						.addGroup(pnlDanhSachLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(cboDanhMucHang, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(scrollTblList, GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE))
						.addContainerGap()));
		pnlDanhSachLayout.setVerticalGroup(pnlDanhSachLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlDanhSachLayout.createSequentialGroup().addContainerGap()
						.addComponent(cboDanhMucHang, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(scrollTblList)));

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addComponent(pnlThanhToan, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addGap(0, 0, 0).addComponent(pnlDanhSach, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(pnlThanhToan, GroupLayout.DEFAULT_SIZE, 695, Short.MAX_VALUE)
				.addComponent(pnlDanhSach, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

		// set icons
		lblAddCustomer.setIcon(ImageHandler.imageMaker("resources/icons/add-user.png", 25, 25));
		btnChiTiet.setIcon(ImageHandler.imageMaker("resources/icons/right-arrow.png", 25, 25));
		btnThanhToan.setIcon(ImageHandler.imageMaker("resources/icons/cart.png", 32, 32));
		// set cursors
		lblAddCustomer.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnChiTiet.setCursor(new Cursor(Cursor.HAND_CURSOR));
		// event
		txtSearchProducts.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				TimSanPham(txtSearchProducts.getText());

			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				TimSanPham(txtSearchProducts.getText());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				TimSanPham(txtSearchProducts.getText());
			}
		});

		lblAddCustomer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new QLKH(1).setVisible(true);
			}
		});
		tblList.setSelectionBackground(new Color(250, 206, 112));
		tblList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if (e.getClickCount() == 2 && !e.isConsumed()) {
					e.consume();
					TaoDonHang();
				}
			}
		});
		
		scrollTblList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				tblList.clearSelection();
				popupTable.setVisible(false);
			}
		});
		
		triggerKeyF2();
		btnThanhToan.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == btnThanhToan) {
					ThanhToanHoaDon();
				}
			}
		});
		btnChiTiet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MoCTHD();

			}
		});
		getThemePack();
		LoadComboBox();
		popupMenuInTable();
		popupMenuInList();
		HienDSKhachHang();
	}
	
	
	private void getThemePack() {
		pnlList.setBackground(Theme.getColor("GDTQ.panelDonHang.background"));
		scrollPnlList.setBorder(Theme.isLight ? new FlatBorder() : null);
		txtSearchProducts.setBackground(Theme.getColor("TextField.background"));
		txtSearchProducts.setForeground(Theme.getColor("TextField.foreground"));
		btnThanhToan.setBackground(Theme.getColor("GDTQ.btnThanhToan.background"));
		btnThanhToan.setForeground(Theme.getColor("GDTQ.btnThanhToan.foreground"));
		pnlThanhToan.setBackground(Theme.getColor("GDTQ.panelThanhToan.background"));
		btnChiTiet.setBackground(Theme.getColor("GDTQ.btnChiTiet.background"));
	}
	
	// Nhận diện phím F2
	private void triggerKeyF2() {
		try {
			GlobalScreen.registerNativeHook();
			Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
			logger.setLevel(Level.OFF);
			logger.setUseParentHandlers(false);
			GlobalScreen.addNativeKeyListener(new NativeKeyListener() {
				@Override
				public void nativeKeyTyped(NativeKeyEvent nativeEvent) {
				}

				@Override
				public void nativeKeyReleased(NativeKeyEvent nativeEvent) {
				}

				@Override
				public void nativeKeyPressed(NativeKeyEvent nativeEvent) {
					if (nativeEvent.getKeyCode() == NativeKeyEvent.VC_F2) {
						ThanhToanHoaDon();
					}
				}
			});
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// tạo đơn hàng mới vào danh sách từ bảng hàng hóa
	private void TaoDonHang() {
		int[] rows = tblList.getSelectedRows();
		if (rows.length == 0)
			return;
		
		// kiểm tra số lượng đơn hàng đã vượt quá giới hạn chưa
		int maxRow = 20;
		if (listDH.size() >= maxRow) {
			MessageBox.alert(String.format(messagePack.getString("maxRowReached"), maxRow));
			return;
		}
		// thêm các mặt hàng vào giỏ, số mặt hàng là số hàng mà người dùng chọn trên bảng
		for (int row : rows) {
			boolean check = true;
			
			String MaSP = tblList.getValueAt(row, 1).toString();
			String TenSP = tblList.getValueAt(row, 2).toString();
			int SoLuongHang = Integer.valueOf(tblList.getValueAt(row, 3).toString());
			long GiaSP = Long.parseLong(tblList.getValueAt(row, 4).toString());
			// kiểm tra sản phẩm đã tồn tại trong giỏ hay chưa
			for (DonHang dh : listDH) {
				if (dh.getMaSP().equals(MaSP)) {
					MessageBox.alert(String.format(messagePack.getString("alreadyInCart"), TenSP, MaSP));
					check = false;
				}
			}
			// kiểm tra số lượng sản phẩm còn lại
			if (SoLuongHang == 0) {
				MessageBox.alert(String.format(messagePack.getString("outOfStock"), TenSP, MaSP));
				check = false;
			}
			if (check) {
				DonHang dh = new DonHang(++STT, TenSP, MaSP, GiaSP, 1);
				tblList.setValueAt(SoLuongHang - 1, row, 3);
				pnlList.add(dh);
				listDH.add(dh);
			}
			tblList.clearSelection();
		}
		// hiển thị scrollbar nếu số lượng đơn hàng vượt quá vùng hiển thị của panel
		if (listDH.size() >= 5) {
			scrollPnlList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		} else {
			scrollPnlList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		}
		// kéo scrollbar theo danh sách
		JScrollBar vbar = scrollPnlList.getVerticalScrollBar();
		double position = Double.valueOf(listDH.size()) / 5;
		if (position > 0.9) {
			vbar.setValue((int) ((vbar.getVisibleAmount() + 50 * (position - 1)) * (position - 1)));
		}
		// giải phóng không gian và bộ nhớ cho panel
		pnlList.revalidate();
		pnlList.repaint();
	}

	// Mở dialog thông tin chi tiết hơn của hóa đơn thanh toán
	private void MoCTHD() {
		if (pnlList.getComponents().length == 0)
			return;

		String MaHD = InvoicePrinting.generateInvoiceID("HD");
		long TongTien = 0;

		for (DonHang dh : listDH) {
			TongTien += dh.getThanhTien();
		}

		String item = cboCustomers.getEditor().getItem().toString();
		if (item.isEmpty()) {
			MaKH = 1;
		} else {
			MaKH = Integer.valueOf(item.substring(0, item.indexOf(" ")));
		}

		new HoaDonChiTiet(null, true, MaHD, MaKH, TongTien).setVisible(true);
	}

	// insert các chi tiết hóa đơn vào CSDL
	public static void triggerCTHD(String MaHD) {
		for (DonHang dh : listDH) {
			dh.insert(MaHD, dh);
		}
	}

	// làm trống danh sách đơn hàng
	public static void clearList() {
		if (listDH.size() == 0)
			return;
		pnlList.removeAll();
		pnlList.revalidate();
		pnlList.repaint();
		listDH.clear();
		STT = 0;
	}
	
	public static void rollback(String MaSP, int SoLuong) {
		int rowCount = GiaoDienBanHang.tblList.getRowCount();
		for (int i = 0; i < rowCount; i++) {
			if (GiaoDienBanHang.tblList.getValueAt(i, 1).equals(MaSP)) {
				int SL = Integer.valueOf(GiaoDienBanHang.tblList.getValueAt(i, 3).toString());
				GiaoDienBanHang.tblList.setValueAt(SL + SoLuong, i, 3);
			}
		}
	}

	// event nút thanh toán đơn hàng
	public void ThanhToanHoaDon() {
		if (pnlList.getComponents().length == 0)
			return;

		if (MessageBox.confirm(messagePack.getString("confirmCash")) == false)
			return;

		String MaNV = Authorizer.user.getMaNV();
		// tạo mã hoá đơn
		String MaHD = InvoicePrinting.generateInvoiceID("HD");

		java.sql.Date date = utils.DateFormatter.toSqlDate(new Date());

		String item = cboCustomers.getEditor().getItem().toString();

		if (item.isEmpty()) {
			MaKH = 1;
		} else {
			MaKH = Integer.valueOf(item.substring(0, item.indexOf(" ")));
		}

		long TienHang = 0;
		for (DonHang dh : listDH) {
			TienHang += dh.getThanhTien();
		}

		long GiamGia = new HoaDonChiTiet().getDiscount(MaKH);

		long TongTien = TienHang - GiamGia;

		HoaDon hd = new HoaDon(MaHD, MaNV, date, MaKH, GiamGia, TongTien);
		if (new HoaDonDAO().insert(hd)) {
			triggerCTHD(MaHD);
			clearList();
			InvoicePrinting.XuatHD("MaHD", MaHD, "resources/printTemplate/HoaDonThu.jrxml");
		} else {
			MessageBox.alert(messagePack.getString("errorOccurred"));
		}
	}

	// đổ dữ liệu vào bảng danh sách hàng hóa và hiển thị ra màn hình
	private void HienThiTable(String TenDanhMuc) {
		String SQL = "SELECT * FROM MatHang JOIN DanhMuc ON DanhMuc.MaDanhMuc = MatHang.MaDanhMuc JOIN KhoHang ON MatHang.MaHang = KhoHang.MaHang WHERE TenDanhMuc=?";
		DefaultTableModel model = (DefaultTableModel) tblList.getModel();
		model.setRowCount(0);
		listMH.clear();
		if (TenDanhMuc.equals(languagePack.getString("allCategories"))) {
			listMH = new MatHangDAO().selectAll();
		} else {
			listMH = new MatHangDAO().select(SQL, TenDanhMuc);
		}
		listMH.forEach(mh -> {
			ImageIcon image = ImageHandler.imageMaker(mh.getHinh(), 115, 115);
			Object[] row = { image, mh.getMaHang(), mh.getTenHang(), mh.getSoLuong(), mh.getGiaBan() };
			model.addRow(row);
		});

	}

	// đổ dữ liệu vào combobox danh mục, thêm đổ dữ liệu lên bảng khi chọn combobox
	private void LoadComboBox() {
		cboDanhMucHang.addItem(languagePack.getString("allCategories"));
		HienThiTable((String) cboDanhMucHang.getSelectedItem());

		listDM = new DanhMucDAO().selectAll();
		for (DanhMuc dm : listDM) {
			cboDanhMucHang.addItem(dm.getTenDanhMuc());
		}
		cboDanhMucHang.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				HienThiTable((String) cboDanhMucHang.getSelectedItem());
			}
		});
	}
	
	// hiển thị pop up menu trong bảng danh sách hàng hóa
	private void popupMenuInTable() {
		popupTable = new JPopupMenu();
		JMenuItem addItems = new JMenuItem(languagePack.getString("addToCart"));
		addItems.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TaoDonHang();
			}
		});
		popupTable.add(addItems);
		tblList.setComponentPopupMenu(popupTable);
	}

	// hiển thị pop up menu trong danh sách đơn hàng
	private void popupMenuInList() {
		final JPopupMenu popupList = new JPopupMenu();
		JMenuItem deleteAll = new JMenuItem(languagePack.getString("removeAllFromCart"));
		deleteAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearList();
				if (listDH.size() == 0) return;
				for (DonHang dh : listDH) {
					String MaSP = dh.getMaSP();
					int SoLuong = dh.getSoLuong();
					rollback(MaSP, SoLuong);
				}
			}
		});
		popupList.add(deleteAll);
		pnlList.setComponentPopupMenu(popupList);
	}

	// Tìm kiếm sản phẩm theo từ khóa và đổ dữ liệu hiển thị lên bảng hàng hóa
	private void TimSanPham(String key) {
		List<MatHang> listTimKiem = new ArrayList<>();
		for (MatHang matHang : listMH) {
			if (matHang.getMaHang().toLowerCase().contains(key.toLowerCase())
					|| matHang.getTenHang().toLowerCase().contains(key.toLowerCase())) {
				listTimKiem.add(matHang);
			}
		}
		DefaultTableModel model = (DefaultTableModel) tblList.getModel();
		model.setRowCount(0);
		listTimKiem.forEach(mh -> {
			ImageIcon image = ImageHandler.imageMaker(mh.getHinh(), 115, 115);
			Object[] row = { image, mh.getMaHang(), mh.getTenHang(), mh.getSoLuong(), mh.getGiaBan() };
			model.addRow(row);
		});
	}

	// event hiện danh sách dropdown khách hàng khi nhập từ khóa vào combobox
	private void HienDSKhachHang() {
		for (KhachHang khachHang : listKH) {
			cboCustomers.addItem(khachHang.getMaKH() + " | " + khachHang.getTenKH());
		}
		cboCustomers.getEditor().setItem("");
	}

}

class DonHang extends JPanel {

	private JButton btnCong;
	private JLabel lblMaHangxGia;
	private JLabel lblSTT;
	private JLabel lblSoLuong;
	private JLabel lblTenHang;
	private JLabel lblTongTien;
	private JButton btnTru;
	public JButton btnXoa;

	private String MaSP;
	private String TenSP;
	private long DonGia;
	private int SoLuong;
	private long ThanhTien;

	ResourceBundle languagePack = Language.getLanguagePack();
	ResourceBundle messagePack = Language.getMessagePack();

	public DonHang(int STT, String Ten, String Ma, long Gia, int SL) {
		MaSP = Ma;
		TenSP = Ten;
		DonGia = Gia;
		SoLuong = SL;
		ThanhTien = DonGia * SoLuong;

		lblSTT = new JLabel();
		lblTenHang = new JLabel();
		lblMaHangxGia = new JLabel();
		lblSoLuong = new JLabel();
		lblTongTien = new JLabel();
		btnTru = new JButton();
		btnCong = new JButton();
		btnXoa = new JButton();

		setName("Don Hang " + STT);
		setBackground(Theme.getColor("DonHang.background"));

		lblSTT.setFont(new Font("Tahoma", 1, 14));
		lblSTT.setHorizontalAlignment(SwingConstants.CENTER);
		lblSTT.setText(String.valueOf(STT));
		lblSTT.setForeground(Theme.getColor("DonHang.foreground"));

		lblTenHang.setFont(new Font("Tahoma", 1, 12));
		lblTenHang.setText(Ten);
		lblTenHang.setName("TenSP");
		lblTenHang.setForeground(Theme.getColor("DonHang.foreground"));
		
		lblMaHangxGia.setFont(new Font("Tahoma", 1, 12));
		lblMaHangxGia.setText(Ma + " x " + Gia);
		lblMaHangxGia.setName("MaSPxGia");
		lblMaHangxGia.setForeground(Theme.getColor("DonHang.foreground"));

		lblSoLuong.setFont(new Font("Tahoma", 1, 12));
		lblSoLuong.setText(languagePack.getString("amount") + String.valueOf(SoLuong));
		lblSoLuong.setName("SL");
		lblSoLuong.setForeground(Theme.getColor("DonHang.foreground"));

		long TongTien = Gia * SoLuong;

		lblTongTien.setFont(new Font("Tahoma", 1, 12));
		lblTongTien.setName("Tong");
		lblTongTien.setHorizontalAlignment(SwingConstants.TRAILING);
		lblTongTien.setText(TongTien + " VNĐ");
		lblTongTien.setForeground(Theme.getColor("DonHang.foreground"));

		btnTru.setFont(new Font("Tahoma", 1, 18));
		btnTru.setHorizontalAlignment(SwingConstants.CENTER);
		btnTru.setText("-");
		btnTru.setBorder(null);
		btnTru.setHorizontalTextPosition(SwingConstants.CENTER);
		btnTru.setPreferredSize(new Dimension(30, 30));
		btnTru.setBackground(Theme.getColor("DonHang.background"));
		btnTru.setForeground(Theme.getColor("DonHang.foreground"));
		btnTru.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnTru.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (SoLuong == 1) {
					MessageBox.alert(messagePack.getString("minQTY"));
					return;
				}

				DonHang dh = find(MaSP);
				if (dh == null) {
					return;
				}
				// Kiểm tra số lượng hàng còn trong kho
				int rowCount = GiaoDienBanHang.tblList.getRowCount();
				for (int i = 0; i < rowCount; i++) {
					if (GiaoDienBanHang.tblList.getValueAt(i, 1) == MaSP) {
						int SL = Integer.valueOf(GiaoDienBanHang.tblList.getValueAt(i, 3).toString());
						GiaoDienBanHang.tblList.setValueAt(SL + 1, i, 3);
					}
				}
				// Tìm chỉ mục của đơn hàng trong list đơn hàng
				int index = GiaoDienBanHang.listDH.indexOf(dh);
				// Sửa số lượng hàng trong đơn hàng tìm được
				DonHang edited = new DonHang(STT, TenSP, MaSP, DonGia, --SoLuong);
				GiaoDienBanHang.listDH.set(index, edited);
				// Sửa số lượng hàng hiển thị trên ứng dụng
				lblSoLuong.setText(languagePack.getString("amount") + SoLuong);
				long total = SoLuong * DonGia;
				// Sửa tổng tiền đơn hàng hiển thị trên ứng dụng
				lblTongTien.setText(total + " VNĐ");
				// Sửa số lượng hàng trên danh sách sản phẩm
			}
		});

		btnCong.setFont(new Font("Tahoma", 1, 18));
		btnCong.setHorizontalAlignment(SwingConstants.CENTER);
		btnCong.setText("+");
		btnCong.setBorder(null);
		btnCong.setHorizontalTextPosition(SwingConstants.CENTER);
		btnCong.setPreferredSize(new Dimension(30, 30));
		btnCong.setBackground(Theme.getColor("DonHang.background"));
		btnCong.setForeground(Theme.getColor("DonHang.foreground"));
		btnCong.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnCong.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DonHang dh = find(MaSP);
				if (dh == null) {
					return;
				}
				// Kiểm tra số lượng hàng còn trong kho
				int rowCount = GiaoDienBanHang.tblList.getRowCount();
				for (int i = 0; i < rowCount; i++) {
					if (GiaoDienBanHang.tblList.getValueAt(i, 1) == MaSP) {
						int SL = Integer.valueOf(GiaoDienBanHang.tblList.getValueAt(i, 3).toString());
						if (SL == 0) {
							MessageBox.alert(String.format(messagePack.getString("outOfStock"), TenSP, MaSP));
							return;
						}
						GiaoDienBanHang.tblList.setValueAt(SL - 1, i, 3);
					}
				}
				// Tìm chỉ mục của đơn hàng trong list đơn hàng
				int index = GiaoDienBanHang.listDH.indexOf(dh);
				// Sửa số lượng hàng trong đơn hàng tìm được
				DonHang edited = new DonHang(STT, TenSP, MaSP, DonGia, ++SoLuong);
				GiaoDienBanHang.listDH.set(index, edited);
				// Sửa số lượng hàng hiển thị trên ứng dụng
				lblSoLuong.setText(languagePack.getString("amount") + SoLuong);
				long total = SoLuong * DonGia;
				// Sửa tổng tiền đơn hàng hiển thị trên ứng dụng
				lblTongTien.setText(total + " VNĐ");
				// Sửa số lượng hàng trên danh sách sản phẩm
			}
		});

		btnXoa.setPreferredSize(new Dimension(30, 30));
		btnXoa.setName("Xoa");
		btnXoa.setIcon(ImageHandler.imageMaker("resources/icons/delete.png", 24, 24));
		btnXoa.setBorder(null);
		btnXoa.setBackground(Theme.getColor("DonHang.background"));
		btnXoa.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnXoa.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DonHang remove = find(MaSP);
				if (remove == null) {
					return;
				}
				// trả về số lượng hàng đã hủy
				GiaoDienBanHang.rollback(MaSP, SoLuong);				//
				GiaoDienBanHang.listDH.remove(remove);
				GiaoDienBanHang.pnlList.removeAll();
				GiaoDienBanHang.pnlList.repaint();
				GiaoDienBanHang.pnlList.revalidate();

				if (GiaoDienBanHang.listDH.size() == 0) {
					GiaoDienBanHang.STT = 0;
					return;
				}

				for (DonHang dh : GiaoDienBanHang.listDH) {
					GiaoDienBanHang.pnlList.add(dh);
				}
			}
		});

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addGap(6, 6, 6)
				.addComponent(lblSTT, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(lblTenHang, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblMaHangxGia, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(lblSoLuong, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addComponent(btnTru, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(btnCong, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
								.addGap(6, 6, 6).addComponent(btnXoa, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(lblTongTien))
				.addGap(20, 20, 20)));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(lblSTT, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblSoLuong, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
						.addGap(37, 37, 37))
				.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGap(16, 16, 16)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addGroup(layout.createSequentialGroup()
										.addComponent(lblTongTien, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
														.addComponent(btnTru, GroupLayout.PREFERRED_SIZE, 30,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(btnCong, GroupLayout.PREFERRED_SIZE, 30,
																GroupLayout.PREFERRED_SIZE))
												.addComponent(btnXoa, GroupLayout.PREFERRED_SIZE, 30,
														GroupLayout.PREFERRED_SIZE)))
								.addGroup(layout.createSequentialGroup()
										.addComponent(lblTenHang, GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
										.addGap(18, 18, 18)
										.addComponent(lblMaHangxGia, GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)))
						.addGap(16, 16, 16)));

	}

	public String getMaSP() {
		return MaSP;
	}

	public void setMaSP(String maSP) {
		MaSP = maSP;
	}

	public String getTenSP() {
		return TenSP;
	}

	public void setTenSP(String tenSP) {
		TenSP = tenSP;
	}

	public int getSoLuong() {
		return SoLuong;
	}

	public void setSoLuong(int sL) {
		SoLuong = sL;
	}

	public long getDonGia() {
		return DonGia;
	}

	public void setDonGia(long donGia) {
		DonGia = donGia;
	}

	public long getThanhTien() {
		return ThanhTien;
	}

	public void setThanhTien(long thanhTien) {
		ThanhTien = thanhTien;
	}

	public static DonHang find(String MaSP) {
		for (DonHang dh : GiaoDienBanHang.listDH) {
			if (dh.getMaSP().equals(MaSP))
				return dh;
		}
		return null;
	}

	@Override
	public String toString() {
		return "DonHang [MaSP=" + MaSP + ", TenSP=" + TenSP + ", DonGia=" + DonGia + ", SoLuong=" + SoLuong
				+ ", ThanhTien=" + ThanhTien + "]";
	}

	String insertSQL = "INSERT INTO HoaDonChiTiet(MaHD, MaHang, TenHang, SoLuong, DonGia, ThanhTien) VALUES (?,?,?,?,?,?)";

	public boolean insert(String MaHD, DonHang dh) {
		try {
			JDBCHelper.update(insertSQL, MaHD, dh.getMaSP(), dh.getTenSP(), dh.getSoLuong(), dh.getDonGia(),
					dh.getThanhTien());
			return true;
		} catch (Exception e) {
			System.out.println("Insert HDCT failed: " + e);
			return false;
		}
	}
}
