package windows;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import java.awt.*;
import java.awt.event.*;

import org.jdatepicker.JDatePicker;
import org.jdatepicker.UtilDateModel;

import dao.KhachHangDAO;
import dao.NhomKhachHangDAO;
import entities.KhachHang;
import entities.NhomKhachHang;
import utils.ImageHandler;
import utils.MessageBox;
import utils.Theme;
import utils.Validator;
import utils.WindowProperties;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class QLKH extends JFrame {

	private JButton btnAddNhom;
	private JButton btnFirst;
	private JButton btnLast;
	private JButton btnMoi;
	private JButton btnNext;
	private JButton btnPrevious;
	private JButton btnSua;
	private JButton btnThem;
	private JButton btnXoa;
	private JComboBox<String> cboNhom;
	private JTable tblList;
	private JLabel lblDiaChi;
	private JLabel lblEmail;
	private JLabel lblGIoiTinh;
	private JLabel lblHeader;
	private JLabel lblHoTen;
	private JLabel lblNgaySinh;
	private JLabel lblNhom;
	private JLabel lblSDT;
	private JPanel pnlButton;
	private JPanel pnlCapNhat;
	private JPanel pnlDanhSach;
	private JRadioButton rdoFemale;
	private JRadioButton rdoMale;
	private JScrollPane scrollDiaCHi;
	private JTabbedPane tabs;
	private JScrollPane scrollTable;
	private JTextArea txtDiaChi;
	private JTextField txtEmail;
	private JTextField txtHoTen;
	private JDatePicker txtNgaySinh;
	private JTextField txtSDT;
	private JTextField txtTimKiem;
	private ButtonGroup btnGrpGender;
	
	UtilDateModel dateModel = new UtilDateModel();
	List<KhachHang> listKH = new ArrayList<>();
	List<NhomKhachHang> listNhomKH = new ArrayList<>();
	int index = 0;

	public QLKH(int tab) {
		initComponents();
		Customize();
		tabs.setSelectedIndex(tab);
		if (tab == 1) {
			newRecord();
		}
	}

	private void Customize() {
		WindowProperties.centeringWindow(this);
		WindowProperties.setApplicationIcon(this);
		WindowProperties.CellRenderTable(tblList, tblList.getModel().getColumnCount());
		WindowProperties.resizableWindow(this, 800, 500);
		// set icon
		btnFirst.setIcon(ImageHandler.imageMaker("resources/icons/first-index.png", 15, 15));
		btnPrevious.setIcon(ImageHandler.imageMaker("resources/icons/previous.png", 15, 15));
		btnNext.setIcon(ImageHandler.imageMaker("resources/icons/next.png", 15, 15));
		btnLast.setIcon(ImageHandler.imageMaker("resources/icons/last-index.png", 15, 15));
		btnAddNhom.setIcon(ImageHandler.imageMaker("resources/icons/plus.png", 16, 16));
		//
		txtTimKiem.putClientProperty("JTextField.placeholderText", "Nhập tên hoặc số điện thoại khách hàng...");	
		// event
		HienThiTable();
		HienThiForm();
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
		tblList.setAutoCreateRowSorter(true);
		TableColumnModel tcm = tblList.getColumnModel();
		tcm.getColumn(0).setMinWidth(0);
		tcm.getColumn(0).setMaxWidth(0);
		tcm.getColumn(0).setWidth(0);
		tblList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && !e.isConsumed()) {
					e.consume();
					MouseClick();
				}
			}
		});
		btnFirst.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				goToFirst();
			}
		});
		btnPrevious.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				previous();
			}
		});
		btnNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				next();
			}
		});
		btnLast.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				goToLast();
			}
		});
		btnMoi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newRecord();
			}
		});
		btnThem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				insert();
			}
		});
		btnSua.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				update();
			}
		});
		btnXoa.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				delete();
			}
		});
		btnAddNhom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addNhomKH();
			}
		});
		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem updateNKH = new JMenuItem("Đổi tên nhóm");
		updateNKH.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UpdateNhomKH(cboNhom.getSelectedItem().toString());
			}
		});
		
		JMenuItem deleteNKH = new JMenuItem("Xóa nhóm");
		deleteNKH.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DeleteNhomKH(cboNhom.getSelectedItem().toString());
			}
		});
		
		popupMenu.add(updateNKH);
		popupMenu.add(deleteNKH);
		cboNhom.setComponentPopupMenu(popupMenu);
	}

	private void HienThiTable() {
		DefaultTableModel model = (DefaultTableModel) tblList.getModel();
		model.setRowCount(0);
		listKH.clear();
		listKH = new KhachHangDAO().selectAll();
		for (KhachHang kh : listKH) {
			Object[] row = { kh.getMaKH(), kh.getTenKH(), kh.getTenNhomKH(), kh.isGioiTinh() ? "Nam" : "Nữ", kh.getNgaySinh(),
					kh.getSDT(), kh.getEmail(), kh.getDiaChi() };
			model.addRow(row);
		}
		if (listKH.size() == 0) newRecord();
	}

	private void HienThiTimKiem(String key) {
		List<KhachHang> list = new ArrayList<>();
		
		for (KhachHang kh : listKH) {
			if (kh.getTenKH().toLowerCase().contains(key.toLowerCase()) || (kh.getSDT() != null && kh.getSDT().contains(key))) {
				list.add(kh);
			}
		}
		
		DefaultTableModel model = (DefaultTableModel) tblList.getModel();
		model.setRowCount(0);
		for (KhachHang kh : list) {
			Object[] row = { kh.getTenKH(), kh.getTenNhomKH(), kh.isGioiTinh() ? "Nam" : "Nữ", kh.getNgaySinh(),
					kh.getSDT(), kh.getEmail(), kh.getDiaChi() };
			model.addRow(row);
		}
	}

	private void LoadComboBox() {
		listNhomKH.clear();
		cboNhom.removeAllItems();
		listNhomKH = new NhomKhachHangDAO().selectAll();
		for (NhomKhachHang nkh : listNhomKH) {
			cboNhom.addItem(nkh.getTenNhomKH());
		}
	}	
	
	private void MouseClick() {
		int row = tblList.getSelectedRow();
		if (row < 0) return;
		for (KhachHang kh : listKH) {
			if (kh.getMaKH() == (int) tblList.getValueAt(row, 0)) {
				index = listKH.indexOf(kh);
			}
		}
		tabs.setSelectedIndex(1);
		HienThiForm();
	}
	
	private void HienThiForm() {
		if (listKH.size() == 0) return;
		KhachHang kh = listKH.get(index);
		txtHoTen.setText(kh.getTenKH());
		txtSDT.setText(kh.getSDT());
		txtEmail.setText(kh.getEmail());
		if (kh.isGioiTinh()) {
			rdoMale.setSelected(true);
		} else {
			rdoFemale.setSelected(true);
		}
		txtDiaChi.setText(kh.getDiaChi());
		cboNhom.setSelectedItem(kh.getTenNhomKH());
		Date dateValue = kh.getNgaySinh();
		if (dateValue == null) {
			dateModel.setValue(null);
		} else {
			Calendar c = Calendar.getInstance();
			c.setTime(dateValue);
			dateModel.setDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
			dateModel.setSelected(true);
		}
		btnFirst.setEnabled(index == 0 ? false : true);
		btnLast.setEnabled(index == listKH.size() -1 ? false : true);
		btnThem.setEnabled(false);
		btnSua.setEnabled(true);
		btnXoa.setEnabled(true);
	}
	
	private KhachHang readForm() {
		if (Validator.isNotEmpty(txtHoTen, "Chưa nhập họ tên khách hàng") == false ||
				Validator.isEmptyComboBox(cboNhom, "Chưa chọn nhóm khách hàng") == false) return null;
		String TenKH = txtHoTen.getText();
		boolean gender = rdoMale.isSelected() ? true : false;
		int MaNhomKH = 0;
		String TenNhomKH = cboNhom.getSelectedItem().toString();
		for (NhomKhachHang nkh : listNhomKH) {
			if (nkh.getTenNhomKH().equals(TenNhomKH)) {
				MaNhomKH = nkh.getMaNhomKH();
			}
		}
		String DiaChi = txtDiaChi.getText();
		Date NgaySinh = dateModel.getValue();
		String Email = txtEmail.getText();
		String SDT = txtSDT.getText();
		KhachHang kh = new KhachHang(TenKH, gender, MaNhomKH, TenNhomKH, DiaChi, NgaySinh, Email, SDT);
		return kh;
	}
	
	private void clearForm() {
		txtHoTen.setText("");
		txtHoTen.requestFocus();
		txtSDT.setText("");
		txtEmail.setText("");
		rdoMale.setSelected(true);
		txtDiaChi.setText("");
		dateModel.setValue(null);
	}
	
	private void newRecord() {
		clearForm();
		btnThem.setEnabled(true);
		btnSua.setEnabled(false);
		btnXoa.setEnabled(false);
	}

	private void insert() {
		KhachHang kh = readForm();
		if (kh == null)
			return;
		if (MessageBox.confirm("Bạn có muốn thêm khách hàng này?") == false)
			return;
		if (new KhachHangDAO().insert(kh)) {
			HienThiTable();
			btnThem.setEnabled(false);
			index = listKH.size() - 1;
			HienThiForm();
			MessageBox.notif("Thêm khách hàng thành công");
		} else {
			MessageBox.alert("Đã có lỗi xảy ra, vui lòng thử lại.");
		}
	}

	private void update() {
		KhachHang old = listKH.get(index);
		KhachHang kh = readForm();
		if (kh == null)
			return;
		if (MessageBox.confirm("Bạn có muốn sửa thông tin khách hàng này?") == false)
			return;
		if (new KhachHangDAO().update(old, kh)) {
			HienThiTable();
			MessageBox.notif("Sửa thông tin khách hàng thành công");
		} else {
			MessageBox.alert("Đã có lỗi xảy ra, vui lòng thử lại.");
		}
	}

	private void delete() {
		KhachHang kh = listKH.get(index);
		if (MessageBox.confirm("Bạn có muốn xóa khách hàng này?") == false)
			return;
		if (new KhachHangDAO().delete(kh)) {
			HienThiTable();
			index = 0;
			HienThiForm();
			MessageBox.notif("Xóa khách hàng thành công");
		} else {
			MessageBox.alert("Đã có lỗi xảy ra, vui lòng thử lại.");
		}
	}

	private void addNhomKH() {
		String TenNhomKH = MessageBox.prompt("Nhập tên nhóm khách hàng mới cần tạo", "Thêm nhóm khách hàng");
		if (TenNhomKH == null) return;
		for (NhomKhachHang nhomKhachHang : listNhomKH) {
			if (nhomKhachHang.getTenNhomKH().equals(TenNhomKH) ) {
				MessageBox.alert("Trùng tên nhóm khách hàng đã tồn tại, vui lòng thử lại.");
				return;
			}
		}
		NhomKhachHang nkh = new NhomKhachHang(TenNhomKH);
		if (new NhomKhachHangDAO().insert(nkh)) {
			MessageBox.notif("Tạo nhóm khách hàng " + TenNhomKH + " thành công.");
			LoadComboBox();
			cboNhom.setSelectedItem(TenNhomKH);
		} else {
			MessageBox.alert("Đã có lỗi xảy ra, vui lòng thử lại.");
		}
	}
	
	private void DeleteNhomKH(String key) {
		if (key.isEmpty()) return;
		NhomKhachHang xoa = null;
		for (NhomKhachHang nhomKhachHang : listNhomKH) {
			if (nhomKhachHang.getTenNhomKH().equals(key)) xoa = nhomKhachHang;
		}
		
		if (MessageBox.confirm("Bạn có chắc muốn xóa nhóm khách hàng này?") == false) return;
		
		if (new NhomKhachHangDAO().delete(xoa)) {
			MessageBox.notif(String.format("Xóa nhóm khách hàng '%s' thành công.", xoa.getTenNhomKH()));
			LoadComboBox();
			cboNhom.setSelectedItem(0);
		} else {
			MessageBox.alert(String.format("Có nhiều khách hàng hiện tại đang thuộc nhóm khách hàng '%', thao tác xóa bị từ chối.", xoa.getTenNhomKH()));
		}
	}
	
	private void UpdateNhomKH(String key) {
		if (key.isEmpty()) return;
		NhomKhachHang cu = null;
		for (NhomKhachHang nhomKhachHang : listNhomKH) {
			if (nhomKhachHang.getTenNhomKH().equals(key)) cu = nhomKhachHang;
		}
		
		String TenMoi = MessageBox.prompt("Nhập tên mới cho nhóm khách hàng", "Đổi tên nhóm khách hàng");
		if (TenMoi == null) return;
		
		for (NhomKhachHang nkh : listNhomKH) {
			if (nkh.getTenNhomKH().equals(TenMoi) ) {
				MessageBox.alert("Trùng tên nhóm khách hàng đã tồn tại, vui lòng thử lại.");
				return;
			}
		}
		NhomKhachHang moi = new NhomKhachHang(TenMoi);
		if (new NhomKhachHangDAO().update(cu, moi)) {
			MessageBox.notif(String.format("Đổi tên nhóm '%s' thành '%s' thành công.", cu.getTenNhomKH(), TenMoi));
			LoadComboBox();
			cboNhom.setSelectedItem(TenMoi);
		} else {
			MessageBox.alert("Đã có lỗi xảy ra, vui lòng thử lại.");
		}
	}
	
	private void goToFirst() {
		index = 0;
		HienThiForm();
	}

	private void previous() {
		if (index <= 0) return;
		index--;
		HienThiForm();
	}
	
	private void next() {
		if (index == listKH.size() - 1) return;
		index++;
		HienThiForm();
	}

	private void goToLast() {
		index = listKH.size() - 1;
		HienThiForm();
	}

	private void initComponents() {
		
		setTitle("Quản lý khách hàng");
		
		lblHeader = new JLabel();
		tabs = new JTabbedPane();
		pnlDanhSach = new JPanel();
		txtTimKiem = new JTextField();
		scrollTable = new JScrollPane();
		tblList = new JTable();
		pnlCapNhat = new JPanel();
		lblHoTen = new JLabel();
		lblGIoiTinh = new JLabel();
		txtHoTen = new JTextField();
		rdoMale = new JRadioButton();
		rdoFemale = new JRadioButton();
		lblEmail = new JLabel();
		txtEmail = new JTextField();
		lblNhom = new JLabel();
		cboNhom = new JComboBox<>();
		btnAddNhom = new JButton();
		lblNgaySinh = new JLabel();
		txtNgaySinh = new JDatePicker(dateModel, "dd/MM/yyyy");
		lblSDT = new JLabel();
		txtSDT = new JTextField();
		lblDiaChi = new JLabel();
		scrollDiaCHi = new JScrollPane();
		txtDiaChi = new JTextArea();
		pnlButton = new JPanel();
		btnMoi = new JButton();
		btnThem = new JButton();
		btnSua = new JButton();
		btnXoa = new JButton();
		btnFirst = new JButton();
		btnPrevious = new JButton();
		btnNext = new JButton();
		btnLast = new JButton();
		btnGrpGender = new ButtonGroup();

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setPreferredSize(new Dimension(650, 450));
		
		lblHeader.setForeground(Theme.getColor("lblHeader.foreground"));
		
		lblHeader.setFont(new Font("Tahoma", 1, 15)); // NOI18N
		lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeader.setText("QUẢN LÝ THÔNG TIN KHÁCH HÀNG");
		lblHeader.setHorizontalTextPosition(SwingConstants.CENTER);
		lblHeader.setPreferredSize(new Dimension(477, 30));

		tblList.setShowHorizontalLines(false);
		tblList.getTableHeader().setReorderingAllowed(false);
		tblList.setModel(new DefaultTableModel(new Object[][] {

		}, new String[] { "Mã khách hàng", "Họ và tên", "Nhóm khách hàng", "Giới tính", "Ngày sinh", "SĐT", "Email", "Địa chỉ" }) {
			Class[] types = new Class[] {Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class,
					Date.class, java.lang.String.class, java.lang.String.class, java.lang.String.class };
			boolean[] canEdit = new boolean[] { false, false, false, false, false, false, false, false };

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		scrollTable.setViewportView(tblList);
		if (tblList.getColumnModel().getColumnCount() > 0) {
			tblList.getColumnModel().getColumn(1).setPreferredWidth(150);
			tblList.getColumnModel().getColumn(2).setPreferredWidth(120);
			tblList.getColumnModel().getColumn(4).setPreferredWidth(70);
		}

		GroupLayout pnlDanhSachLayout = new GroupLayout(pnlDanhSach);
		pnlDanhSach.setLayout(pnlDanhSachLayout);
		pnlDanhSachLayout
				.setHorizontalGroup(pnlDanhSachLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(pnlDanhSachLayout.createSequentialGroup().addContainerGap().addGroup(pnlDanhSachLayout
								.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(txtTimKiem)
								.addComponent(scrollTable, GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE))
								.addContainerGap()));
		pnlDanhSachLayout
				.setVerticalGroup(pnlDanhSachLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(pnlDanhSachLayout.createSequentialGroup().addContainerGap()
								.addComponent(txtTimKiem, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(scrollTable, GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
								.addContainerGap()));

		tabs.addTab("Danh sách", pnlDanhSach);

		pnlCapNhat.setPreferredSize(new Dimension(80, 20));

		lblHoTen.setFont(new Font("Tahoma", 0, 12)); // NOI18N
		lblHoTen.setText("Họ và tên");
		lblHoTen.setPreferredSize(new Dimension(80, 24));

		lblGIoiTinh.setFont(new Font("Tahoma", 0, 12)); // NOI18N
		lblGIoiTinh.setText("Giới tính");
		lblGIoiTinh.setPreferredSize(new Dimension(80, 24));

		txtHoTen.setPreferredSize(new Dimension(150, 24));

		rdoMale.setText("Nam");
		rdoMale.setSelected(true);
		rdoFemale.setText("Nữ");
		
		btnGrpGender.add(rdoFemale);
		btnGrpGender.add(rdoMale);

		lblEmail.setFont(new Font("Tahoma", 0, 12)); // NOI18N
		lblEmail.setText("Email");
		lblEmail.setPreferredSize(new Dimension(80, 24));

		txtEmail.setPreferredSize(new Dimension(150, 24));

		lblNhom.setFont(new Font("Tahoma", 0, 12)); // NOI18N
		lblNhom.setText("Nhóm khách hàng");
		lblNhom.setPreferredSize(new Dimension(125, 24));

		cboNhom.setPreferredSize(new Dimension(135, 24));

		btnAddNhom.setPreferredSize(new Dimension(24, 24));
		btnAddNhom.setBackground(this.getBackground());
		btnAddNhom.setBorder(null);

		lblNgaySinh.setFont(new Font("Tahoma", 0, 12)); // NOI18N
		lblNgaySinh.setText("Ngày sinh");
		lblNgaySinh.setPreferredSize(new Dimension(125, 24));

		txtNgaySinh.setPreferredSize(new Dimension(150, 24));
		txtNgaySinh.setTextEditable(true);

		lblSDT.setFont(new Font("Tahoma", 0, 12)); // NOI18N
		lblSDT.setText("SĐT");
		lblSDT.setPreferredSize(new Dimension(125, 24));

		txtSDT.setPreferredSize(new Dimension(150, 24));

		lblDiaChi.setFont(new Font("Tahoma", 0, 12)); // NOI18N
		lblDiaChi.setText("Địa chỉ");
		lblDiaChi.setPreferredSize(new Dimension(80, 24));

		scrollDiaCHi.setPreferredSize(new Dimension(465, 130));

		txtDiaChi.setColumns(24);
		txtDiaChi.setRows(5);
		scrollDiaCHi.setViewportView(txtDiaChi);

		btnMoi.setFont(new Font("Tahoma", 1, 12)); // NOI18N
		btnMoi.setText("Mới");
		btnMoi.setMargin(new Insets(2, 5, 2, 5));
		btnMoi.setPreferredSize(new Dimension(60, 32));

		btnThem.setFont(new Font("Tahoma", 1, 12)); // NOI18N
		btnThem.setText("Thêm");
		btnThem.setMargin(new Insets(2, 5, 2, 5));
		btnThem.setPreferredSize(new Dimension(60, 32));

		btnSua.setFont(new Font("Tahoma", 1, 12)); // NOI18N
		btnSua.setText("Sửa");
		btnSua.setMargin(new Insets(2, 5, 2, 5));
		btnSua.setPreferredSize(new Dimension(60, 32));

		btnXoa.setFont(new Font("Tahoma", 1, 12)); // NOI18N
		btnXoa.setText("Xóa");
		btnXoa.setMargin(new Insets(2, 5, 2, 5));
		btnXoa.setPreferredSize(new Dimension(60, 32));

		btnFirst.setMargin(new Insets(2, 2, 2, 2));
		btnFirst.setPreferredSize(new Dimension(28, 28));

		btnPrevious.setMargin(new Insets(2, 2, 2, 2));
		btnPrevious.setPreferredSize(new Dimension(28, 28));

		btnNext.setMargin(new Insets(2, 2, 2, 2));
		btnNext.setPreferredSize(new Dimension(28, 28));

		btnLast.setMargin(new Insets(2, 2, 2, 2));
		btnLast.setPreferredSize(new Dimension(28, 28));

		GroupLayout pnlButtonLayout = new GroupLayout(pnlButton);
		pnlButton.setLayout(pnlButtonLayout);
		pnlButtonLayout
				.setHorizontalGroup(pnlButtonLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(pnlButtonLayout.createSequentialGroup().addGap(0, 0, 0)
								.addComponent(btnMoi, GroupLayout.PREFERRED_SIZE, 60,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(btnThem, GroupLayout.PREFERRED_SIZE, 60,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(btnSua, GroupLayout.PREFERRED_SIZE, 60,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(btnXoa, GroupLayout.PREFERRED_SIZE, 60,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnFirst, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGap(10, 10, 10)
								.addComponent(btnPrevious, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGap(10, 10, 10)
								.addComponent(btnNext, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGap(10, 10, 10)
								.addComponent(btnLast, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGap(0, 0, 0)));
		pnlButtonLayout.setVerticalGroup(pnlButtonLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(GroupLayout.Alignment.TRAILING, pnlButtonLayout.createSequentialGroup()
						.addGap(0, 0, 0)
						.addGroup(pnlButtonLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(btnMoi, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnThem, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnSua, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnXoa, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnFirst, GroupLayout.PREFERRED_SIZE, 28,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(btnPrevious, GroupLayout.PREFERRED_SIZE, 28,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(btnNext, GroupLayout.PREFERRED_SIZE, 28,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(btnLast, GroupLayout.PREFERRED_SIZE, 28,
										GroupLayout.PREFERRED_SIZE))));

		GroupLayout pnlCapNhatLayout = new GroupLayout(pnlCapNhat);
		pnlCapNhat.setLayout(pnlCapNhatLayout);
		pnlCapNhatLayout.setHorizontalGroup(pnlCapNhatLayout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlCapNhatLayout.createSequentialGroup().addContainerGap().addGroup(pnlCapNhatLayout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(pnlCapNhatLayout.createSequentialGroup().addGroup(pnlCapNhatLayout
								.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
								.addComponent(lblDiaChi, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblEmail, GroupLayout.Alignment.LEADING,
										GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(lblGIoiTinh, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblHoTen, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addGap(10, 10, 10)
								.addGroup(pnlCapNhatLayout
										.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addGroup(pnlCapNhatLayout.createSequentialGroup().addGroup(pnlCapNhatLayout
												.createParallelGroup(GroupLayout.Alignment.LEADING)
												.addGroup(pnlCapNhatLayout.createSequentialGroup().addComponent(rdoMale)
														.addGap(28, 28, 28).addComponent(rdoFemale).addPreferredGap(
																LayoutStyle.ComponentPlacement.RELATED,
																GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
												.addComponent(txtEmail, GroupLayout.DEFAULT_SIZE,
														GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(txtHoTen, GroupLayout.DEFAULT_SIZE,
														GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
												.addGap(54, 54, 54)
												.addGroup(pnlCapNhatLayout
														.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(lblSDT, GroupLayout.PREFERRED_SIZE,
																101, GroupLayout.PREFERRED_SIZE)
														.addComponent(lblNgaySinh,
																GroupLayout.PREFERRED_SIZE, 101,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(lblNhom, GroupLayout.PREFERRED_SIZE,
																101, GroupLayout.PREFERRED_SIZE))
												.addGap(10, 10, 10)
												.addGroup(pnlCapNhatLayout
														.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addGroup(pnlCapNhatLayout.createSequentialGroup()
																.addComponent(cboNhom, 0, 1, Short.MAX_VALUE)
																.addPreferredGap(
																		LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(btnAddNhom,
																		GroupLayout.PREFERRED_SIZE, 24,
																		GroupLayout.PREFERRED_SIZE))
														.addComponent(txtNgaySinh, GroupLayout.PREFERRED_SIZE,
																GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
														.addComponent(txtSDT, GroupLayout.DEFAULT_SIZE,
																GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
										.addComponent(scrollDiaCHi, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addComponent(pnlButton, GroupLayout.DEFAULT_SIZE,
								GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addContainerGap()));
		pnlCapNhatLayout.setVerticalGroup(pnlCapNhatLayout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlCapNhatLayout.createSequentialGroup().addGap(30, 30, 30).addGroup(pnlCapNhatLayout
						.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblHoTen, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(pnlCapNhatLayout.createSequentialGroup().addGap(0, 0, 0).addGroup(pnlCapNhatLayout
								.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(txtHoTen, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(cboNhom, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNhom, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addComponent(btnAddNhom, GroupLayout.PREFERRED_SIZE, 24,
								GroupLayout.PREFERRED_SIZE))
						.addGap(22, 22, 22)
						.addGroup(pnlCapNhatLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addGroup(
										pnlCapNhatLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(txtNgaySinh, GroupLayout.PREFERRED_SIZE,
														GroupLayout.PREFERRED_SIZE,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(lblNgaySinh, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE,
														GroupLayout.PREFERRED_SIZE))
								.addGroup(
										pnlCapNhatLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(lblGIoiTinh, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(rdoMale).addComponent(rdoFemale,
														GroupLayout.PREFERRED_SIZE, 25,
														GroupLayout.PREFERRED_SIZE)))
						.addGap(18, 18, 18)
						.addGroup(pnlCapNhatLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addGroup(
										pnlCapNhatLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(txtEmail, GroupLayout.PREFERRED_SIZE, 24,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(lblEmail, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE,
														GroupLayout.PREFERRED_SIZE))
								.addComponent(txtSDT, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblSDT, GroupLayout.PREFERRED_SIZE, 24,
										GroupLayout.PREFERRED_SIZE))
						.addGap(20, 20, 20)
						.addGroup(pnlCapNhatLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addGroup(pnlCapNhatLayout.createSequentialGroup()
										.addComponent(lblDiaChi, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addGap(0, 126, Short.MAX_VALUE))
								.addComponent(scrollDiaCHi, GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(pnlButton, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));

		tabs.addTab("Cập nhật", pnlCapNhat);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(lblHeader, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(tabs))
						.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(lblHeader, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(tabs)
						.addContainerGap()));

		pack();
	}
}
