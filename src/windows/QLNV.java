package windows;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import javax.swing.text.PlainDocument;

import dao.NhanVienDAO;
import entities.NhanVien;
import utils.DocumentFiltering;
import utils.ImageHandler;
import utils.Language;
import utils.MessageBox;
import utils.Theme;
import utils.Validator;
import utils.WindowProperties;

public class QLNV extends JFrame {

	private JButton btnFirst;
	private ButtonGroup btnGrpGender;
	private ButtonGroup btnGrpRole;
	private JButton btnLast;
	private JButton btnMoi;
	private JButton btnNext;
	private JButton btnPrevious;
	private JButton btnSua;
	private JButton btnThem;
	private JButton btnXoa;
	private JLabel lblAnh;
	private JLabel lblDiaChi;
	private JLabel lblGioiTinh;
	private JLabel lblHeader;
	private JLabel lblHoTen;
	private JLabel lblImage;
	private JLabel lblMaNV;
	private JLabel lblMatKhau;
	private JLabel lblSDT;
	private JLabel lblVaiTro;
	private JPanel pnlAnh;
	private JPanel pnlButton;
	private JPanel pnlCapNhat;
	private JPanel pnlDanhSach;
	private JPanel pnlThongTin;
	private JRadioButton rdoAdmin;
	private JRadioButton rdoFemale;
	private JRadioButton rdoMale;
	private JRadioButton rdoNhanVien;
	private JScrollPane scrollDiaChi;
	private JScrollPane scrollTable;
	private JTabbedPane tabs;
	private JTable tblList;
	private JTextArea txtDiaChi;
	private JTextField txtHoTen;
	private JTextField txtMaNV;
	private JTextField txtMatKhau;
	private JTextField txtSDT;
	private JTextField txtTimKiem;

	List<NhanVien> listNV = new ArrayList<>();
	int index = 0;
	final String defaultImage = "resources/images/staffs/image-not-available.png";
	String currentImage = null;
	File tempImage = null;
	ResourceBundle languagePack = Language.getLanguagePack();
	ResourceBundle messagePack = Language.getMessagePack();

	public QLNV() {
		initComponents();
		customize();
	}

	private void customize() {
		WindowProperties.centeringWindow(this);
		WindowProperties.resizableWindow(this, 800, this.getHeight());
		WindowProperties.CellRenderTable(tblList, tblList.getModel().getColumnCount());
		WindowProperties.setApplicationIcon(this);
		txtTimKiem.putClientProperty("JTextField.placeholderText", languagePack.getString("plhdStaff"));
		// set Icon
		lblImage.setIcon(ImageHandler.imageMaker(defaultImage, lblImage.getWidth() - 2, lblImage.getHeight() - 2));
		btnFirst.setIcon(ImageHandler.imageMaker("resources/icons/first-index.png", 15, 15));
		btnPrevious.setIcon(ImageHandler.imageMaker("resources/icons/previous.png", 15, 15));
		btnNext.setIcon(ImageHandler.imageMaker("resources/icons/next.png", 15, 15));
		btnLast.setIcon(ImageHandler.imageMaker("resources/icons/last-index.png", 15, 15));
		// event
		HienThiTable();
		HienThiForm();
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
		lblImage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				getImageFromFile();
			}
		});
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
		tblList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && !e.isConsumed()) {
					e.consume();
					MouseClick();
				}

			}
		});
		PlainDocument SDTDocument = (PlainDocument) txtSDT.getDocument();
		DocumentFiltering.IntFilter(SDTDocument);
	}

	private void HienThiTable() {
		DefaultTableModel model = (DefaultTableModel) tblList.getModel();
		model.setRowCount(0);
		listNV.clear();
		listNV = new NhanVienDAO().selectAll();
		for (NhanVien nv : listNV) {
			Object[] row = { nv.getMaNV(), nv.getTenNV(), nv.getMatKhau(),
					nv.isGioiTinh() ? languagePack.getString("rdoMale") : languagePack.getString("rdoFemale"),
					nv.getDiaChi(), nv.getSDT(),
					nv.isRole() ? languagePack.getString("rdoAdmin") : languagePack.getString("rdoStaff"),
					nv.getHinh() };
			model.addRow(row);
		}
		if (listNV.size() == 0)
			newRecord();
	}

	private void HienThiTimKiem(String key) {
		List<NhanVien> list = new ArrayList<>();
		for (NhanVien nv : listNV) {
			if (nv.getMaNV().toLowerCase().contains(key.toLowerCase())
					|| nv.getTenNV().toLowerCase().contains(key.toLowerCase())) {
				list.add(nv);
			}
		}
		DefaultTableModel model = (DefaultTableModel) tblList.getModel();
		model.setRowCount(0);
		for (NhanVien nv : list) {
			Object[] row = { nv.getMaNV(), nv.getTenNV(), nv.getMatKhau(),
					nv.isGioiTinh() ? languagePack.getString("rdoMale") : languagePack.getString("rdoFemale"),
					nv.getDiaChi(), nv.getSDT(),
					nv.isRole() ? languagePack.getString("rdoAdmin") : languagePack.getString("rdoStaff"),
					nv.getHinh() };
			model.addRow(row);
		}
	}

	private void MouseClick() {
		int row = tblList.getSelectedRow();
		if (row < 0)
			return;

		String key = tblList.getValueAt(row, 0).toString();

		for (NhanVien nv : listNV) {
			if (nv.getMaNV().equals(key))
				index = listNV.indexOf(nv);
		}

		tabs.setSelectedIndex(1);
		HienThiForm();
	}

	private void HienThiForm() {
		NhanVien nv = listNV.get(index);
		txtMaNV.setText(nv.getMaNV());
		txtHoTen.setText(nv.getTenNV());
		txtMatKhau.setText(nv.getMatKhau());
		if (nv.isGioiTinh()) {
			rdoMale.setSelected(true);
		} else {
			rdoFemale.setSelected(true);
		}
		txtDiaChi.setText(nv.getDiaChi());
		txtSDT.setText(nv.getSDT());
		if (nv.isRole()) {
			rdoAdmin.setSelected(true);
		} else {
			rdoNhanVien.setSelected(true);
		}
		setImage(nv.getHinh());
		btnFirst.setEnabled(index == 0 ? false : true);
		btnLast.setEnabled(index == listNV.size() - 1 ? false : true);
		btnThem.setEnabled(false);
		btnSua.setEnabled(true);
		btnXoa.setEnabled(true);
	}

	private void setImage(String dir) {
		if (dir == null)
			dir = defaultImage;
		if (ImageHandler.isExistedInFolder(dir.substring(dir.lastIndexOf("/") + 1),
				dir.substring(0, dir.lastIndexOf("/"))) == false) {
			dir = defaultImage;
		}
		lblImage.setIcon(ImageHandler.imageMaker(dir, lblImage.getWidth() - 2, lblImage.getHeight() - 2));
		currentImage = dir;
		tempImage = new File(dir);
	}

	private void getImageFromFile() {
		tempImage = ImageHandler.getImageFile(languagePack.getString("chooseStaffImageDialog"));
		if (tempImage == null)
			return;
		currentImage = "resources/images/staffs/" + tempImage.getName();
		lblImage.setIcon(ImageHandler.imageMaker(tempImage, lblImage.getWidth() - 2, lblImage.getHeight() - 2));
	}

	private void saveImage() {
		if (tempImage == null)
			return;
		String dir = "resources/images/staffs/";
		ImageHandler.saveFile(tempImage, dir);
	}

	private NhanVien readForm() {
		// validate form
		if (Validator.isNotEmpty(txtMaNV, messagePack.getString("emptyUsername")) == false
				|| Validator.isNotEmpty(txtHoTen, messagePack.getString("emptyName")) == false
				|| Validator.isNotEmpty(txtMatKhau, messagePack.getString("emptyPassword")) == false)
			return null;
		//
		String MaNV = txtMaNV.getText();
		String HoTen = txtHoTen.getText();
		String MatKhau = txtMatKhau.getText();
		boolean GioiTinh = rdoMale.isSelected() ? true : false;
		String DiaChi = txtDiaChi.getText();
		String SDT = txtSDT.getText();
		boolean VaiTro = rdoAdmin.isSelected() ? true : false;
		String Hinh = currentImage;
		NhanVien nv = new NhanVien(MaNV, HoTen, MatKhau, GioiTinh, DiaChi, SDT, VaiTro, Hinh);
		return nv;
	}

	private void clearForm() {
		txtMaNV.setText("");
		txtHoTen.setText("");
		txtMatKhau.setText("");
		rdoMale.setSelected(true);
		txtSDT.setText("");
		rdoAdmin.setSelected(true);
		setImage(null);
	}

	private void newRecord() {
		clearForm();
		btnThem.setEnabled(true);
		btnSua.setEnabled(false);
		btnXoa.setEnabled(false);
	}

	private void insert() {
		NhanVien nv = readForm();
		if (nv == null)
			return;
		for (NhanVien nhanVien : listNV) {
			if (txtMaNV.getText().equals(nhanVien.getMaNV())) {
				txtMaNV.requestFocus();
				MessageBox.alert(messagePack.getString("duplicatedUsername"));
				return;
			}
		}
		if (MessageBox.confirm(messagePack.getString("confirmAddNV")) == false)
			return;
		if (new NhanVienDAO().insert(nv)) {
			saveImage();
			HienThiTable();
			btnThem.setEnabled(false);
			index = listNV.size() - 1;
			HienThiForm();
			MessageBox.notif(messagePack.getString("addNVSucessfully"));
		} else {
			MessageBox.alert(messagePack.getString("errorOccurred"));
		}
	}

	private void update() {
		NhanVien old = listNV.get(index);
		NhanVien nv = readForm();
		if (nv == null)
			return;
		if (MessageBox.confirm(messagePack.getString("confirmModifyNV")) == false)
			return;
		if (new NhanVienDAO().update(old, nv)) {
			saveImage();
			HienThiTable();
			MessageBox.notif(messagePack.getString("modifyNVSucessfully"));
		} else {
			MessageBox.alert(messagePack.getString("errorOccurred"));
		}
	}

	private void delete() {
		NhanVien nv = listNV.get(index);
		if (MessageBox.confirm(messagePack.getString("confirmDeleteNV")) == false)
			return;
		if (new NhanVienDAO().delete(nv)) {
			HienThiTable();
			index = 0;
			HienThiForm();
			MessageBox.notif(messagePack.getString("deleteNVSucessfully"));
		} else {
			MessageBox.alert(messagePack.getString("errorOccurred"));
		}
	}

	private void goToFirst() {
		index = 0;
		HienThiForm();
	}

	private void previous() {
		if (index <= 0)
			return;
		index--;
		HienThiForm();
	}

	private void next() {
		if (index == listNV.size() - 1)
			return;
		index++;
		HienThiForm();
	}

	private void goToLast() {
		index = listNV.size() - 1;
		HienThiForm();
	}

	private void initComponents() {
		setTitle(languagePack.getString("accountTitle"));

		btnGrpGender = new ButtonGroup();
		btnGrpRole = new ButtonGroup();
		lblHeader = new JLabel();
		tabs = new JTabbedPane();
		pnlDanhSach = new JPanel();
		txtTimKiem = new JTextField();
		scrollTable = new JScrollPane();
		tblList = new JTable();
		pnlCapNhat = new JPanel();
		pnlAnh = new JPanel();
		lblImage = new JLabel();
		lblAnh = new JLabel();
		pnlThongTin = new JPanel();
		lblMaNV = new JLabel();
		txtMaNV = new JTextField();
		lblHoTen = new JLabel();
		txtHoTen = new JTextField();
		lblMatKhau = new JLabel();
		txtMatKhau = new JTextField();
		lblGioiTinh = new JLabel();
		rdoMale = new JRadioButton();
		rdoFemale = new JRadioButton();
		lblSDT = new JLabel();
		txtSDT = new JTextField();
		lblVaiTro = new JLabel();
		rdoAdmin = new JRadioButton();
		rdoNhanVien = new JRadioButton();
		lblDiaChi = new JLabel();
		scrollDiaChi = new JScrollPane();
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

		btnThem.setEnabled(false);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		lblHeader.setForeground(Theme.getColor("lblHeader.foreground"));

		lblHeader.setFont(new Font("Tahoma", 1, 15));
		lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeader.setText(languagePack.getString("accountHeader"));
		lblHeader.setHorizontalTextPosition(SwingConstants.CENTER);
		lblHeader.setPreferredSize(new Dimension(477, 30));

		tblList.setModel(new DefaultTableModel(new Object[][] {

		}, new String[] { languagePack.getString("staffID"), languagePack.getString("fullname"),
				languagePack.getString("password"), languagePack.getString("gender"), languagePack.getString("address"),
				languagePack.getString("phoneNum"), languagePack.getString("role") }) {
			Class[] types = new Class[] { java.lang.String.class, java.lang.String.class, java.lang.String.class,
					java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class };
			boolean[] canEdit = new boolean[] { false, false, false, false, false, false, false };

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		tblList.setAutoCreateRowSorter(true);
		tblList.setShowHorizontalLines(false);
		tblList.getTableHeader().setReorderingAllowed(false);
		scrollTable.setViewportView(tblList);
		tblList.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		GroupLayout pnlDanhSachLayout = new GroupLayout(pnlDanhSach);
		pnlDanhSach.setLayout(pnlDanhSachLayout);
		pnlDanhSachLayout.setHorizontalGroup(pnlDanhSachLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(GroupLayout.Alignment.TRAILING,
						pnlDanhSachLayout.createSequentialGroup().addContainerGap()
								.addGroup(pnlDanhSachLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addComponent(scrollTable, GroupLayout.DEFAULT_SIZE, 554, Short.MAX_VALUE)
										.addComponent(txtTimKiem))
								.addContainerGap()));
		pnlDanhSachLayout.setVerticalGroup(pnlDanhSachLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlDanhSachLayout.createSequentialGroup().addContainerGap()
						.addComponent(txtTimKiem, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(scrollTable, GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE).addContainerGap()));

		tabs.addTab(languagePack.getString("tabList"), pnlDanhSach);

		lblImage.setHorizontalAlignment(SwingConstants.CENTER);
		lblImage.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204)));
		lblImage.setHorizontalTextPosition(SwingConstants.CENTER);
		lblImage.setPreferredSize(new Dimension(150, 200));

		lblAnh.setFont(new Font("Tahoma", 0, 12));
		lblAnh.setHorizontalAlignment(SwingConstants.CENTER);
		lblAnh.setText(languagePack.getString("staffImage"));
		lblAnh.setHorizontalTextPosition(SwingConstants.CENTER);

		GroupLayout pnlAnhLayout = new GroupLayout(pnlAnh);
		pnlAnh.setLayout(pnlAnhLayout);
		pnlAnhLayout.setHorizontalGroup(pnlAnhLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(lblImage, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(GroupLayout.Alignment.TRAILING,
						pnlAnhLayout
								.createSequentialGroup().addContainerGap().addComponent(lblAnh,
										GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addContainerGap()));
		pnlAnhLayout.setVerticalGroup(pnlAnhLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlAnhLayout.createSequentialGroup()
						.addComponent(lblImage, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(lblAnh, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));

		lblMaNV.setFont(new Font("Tahoma", 0, 12));
		lblMaNV.setText(languagePack.getString("staffID"));
		lblMaNV.setPreferredSize(new Dimension(106, 20));

		txtMaNV.setPreferredSize(new Dimension(266, 20));

		lblHoTen.setFont(new Font("Tahoma", 0, 12));
		lblHoTen.setText(languagePack.getString("fullname"));
		lblHoTen.setPreferredSize(new Dimension(70, 20));

		txtHoTen.setPreferredSize(new Dimension(266, 20));

		lblMatKhau.setFont(new Font("Tahoma", 0, 12));
		lblMatKhau.setText(languagePack.getString("password"));
		lblMatKhau.setPreferredSize(new Dimension(70, 20));

		txtMatKhau.setPreferredSize(new Dimension(266, 20));

		lblGioiTinh.setFont(new Font("Tahoma", 0, 12));
		lblGioiTinh.setText(languagePack.getString("gender"));
		lblGioiTinh.setPreferredSize(new Dimension(106, 20));

		btnGrpGender.add(rdoMale);
		rdoMale.setSelected(true);
		rdoMale.setText(languagePack.getString("rdoMale"));

		btnGrpGender.add(rdoFemale);
		rdoFemale.setText(languagePack.getString("rdoFemale"));

		lblSDT.setFont(new Font("Tahoma", 0, 12));
		lblSDT.setText(languagePack.getString("phoneNum"));
		lblSDT.setPreferredSize(new Dimension(70, 20));

		txtSDT.setPreferredSize(new Dimension(266, 20));

		lblVaiTro.setFont(new Font("Tahoma", 0, 12));
		lblVaiTro.setText(languagePack.getString("role"));
		lblVaiTro.setPreferredSize(new Dimension(106, 20));

		btnGrpRole.add(rdoAdmin);
		rdoAdmin.setSelected(true);
		rdoAdmin.setText(languagePack.getString("rdoAdmin"));

		btnGrpRole.add(rdoNhanVien);
		rdoNhanVien.setText(languagePack.getString("rdoStaff"));

		lblDiaChi.setFont(new Font("Tahoma", 0, 12));
		lblDiaChi.setText(languagePack.getString("address"));
		lblDiaChi.setPreferredSize(new Dimension(106, 20));

		txtDiaChi.setColumns(20);
		txtDiaChi.setFont(new Font("Courier New", 0, 14));
		txtDiaChi.setRows(5);
		scrollDiaChi.setViewportView(txtDiaChi);

		GroupLayout pnlThongTinLayout = new GroupLayout(pnlThongTin);
		pnlThongTin.setLayout(pnlThongTinLayout);
		pnlThongTinLayout.setHorizontalGroup(pnlThongTinLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlThongTinLayout.createSequentialGroup().addContainerGap().addGroup(pnlThongTinLayout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(pnlThongTinLayout.createSequentialGroup()
								.addComponent(lblHoTen, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(txtHoTen, GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE))
						.addGroup(pnlThongTinLayout.createSequentialGroup()
								.addComponent(lblMatKhau, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(txtMatKhau, GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE))
						.addGroup(pnlThongTinLayout.createSequentialGroup()
								.addComponent(lblMaNV, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(txtMaNV, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
						.addGroup(pnlThongTinLayout.createSequentialGroup()
								.addComponent(lblGioiTinh, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(rdoMale, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(rdoFemale,
										GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(GroupLayout.Alignment.TRAILING, pnlThongTinLayout.createSequentialGroup()
								.addComponent(lblSDT, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(txtSDT, GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE))
						.addGroup(pnlThongTinLayout.createSequentialGroup()
								.addComponent(lblVaiTro, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(rdoAdmin, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(rdoNhanVien,
										GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(pnlThongTinLayout.createSequentialGroup()
								.addComponent(lblDiaChi, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(scrollDiaChi)))
						.addContainerGap()));
		pnlThongTinLayout.setVerticalGroup(pnlThongTinLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlThongTinLayout.createSequentialGroup().addGap(0, 0, 0)
						.addGroup(pnlThongTinLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(txtMaNV, GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
								.addComponent(lblMaNV, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(pnlThongTinLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(txtHoTen, GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
								.addComponent(lblHoTen, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(pnlThongTinLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(lblMatKhau, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtMatKhau, GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(pnlThongTinLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(lblGioiTinh, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(rdoMale).addComponent(rdoFemale))
						.addGap(11, 11, 11)
						.addGroup(pnlThongTinLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(txtSDT, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addGroup(pnlThongTinLayout.createSequentialGroup()
										.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 5, Short.MAX_VALUE)
										.addComponent(lblSDT, GroupLayout.PREFERRED_SIZE, 21,
												GroupLayout.PREFERRED_SIZE)))
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(pnlThongTinLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(lblVaiTro, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(rdoAdmin).addComponent(rdoNhanVien))
						.addGap(11, 11, 11)
						.addGroup(pnlThongTinLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addGroup(pnlThongTinLayout.createSequentialGroup()
										.addComponent(lblDiaChi, GroupLayout.PREFERRED_SIZE, 24,
												GroupLayout.PREFERRED_SIZE)
										.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addGroup(pnlThongTinLayout.createSequentialGroup()
										.addComponent(scrollDiaChi, GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
										.addGap(1, 1, 1)))));

		btnMoi.setFont(new Font("Tahoma", 1, 12));
		btnMoi.setText(languagePack.getString("btnMoi"));
		btnMoi.setMargin(new Insets(2, 5, 2, 5));
		btnMoi.setPreferredSize(new Dimension(60, 32));

		btnThem.setFont(new Font("Tahoma", 1, 12));
		btnThem.setText(languagePack.getString("btnThem"));
		btnThem.setMargin(new Insets(2, 5, 2, 5));
		btnThem.setPreferredSize(new Dimension(60, 32));

		btnSua.setFont(new Font("Tahoma", 1, 12));
		btnSua.setText(languagePack.getString("btnSua"));
		btnSua.setMargin(new Insets(2, 5, 2, 5));
		btnSua.setPreferredSize(new Dimension(60, 32));

		btnXoa.setFont(new Font("Tahoma", 1, 12));
		btnXoa.setText(languagePack.getString("btnXoa"));
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
								.addComponent(btnMoi, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(btnThem, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(btnSua, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(btnXoa, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(btnFirst, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(btnPrevious, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(btnNext, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(btnLast, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addContainerGap()));
		pnlButtonLayout.setVerticalGroup(pnlButtonLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(GroupLayout.Alignment.TRAILING, pnlButtonLayout.createSequentialGroup().addGap(0, 0, 0)
						.addGroup(pnlButtonLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(btnMoi, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(btnThem, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(btnSua, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(btnXoa, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(btnFirst, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnPrevious, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnNext, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnLast, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))));

		GroupLayout pnlCapNhatLayout = new GroupLayout(pnlCapNhat);
		pnlCapNhat.setLayout(pnlCapNhatLayout);
		pnlCapNhatLayout.setHorizontalGroup(pnlCapNhatLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(GroupLayout.Alignment.TRAILING, pnlCapNhatLayout.createSequentialGroup().addContainerGap()
						.addGroup(pnlCapNhatLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(pnlButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addGroup(pnlCapNhatLayout.createSequentialGroup()
										.addComponent(pnlAnh, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(pnlThongTin, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)))
						.addContainerGap()));
		pnlCapNhatLayout.setVerticalGroup(pnlCapNhatLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlCapNhatLayout.createSequentialGroup().addContainerGap()
						.addGroup(pnlCapNhatLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addGroup(pnlCapNhatLayout.createSequentialGroup()
										.addComponent(pnlThongTin, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addGap(11, 11, 11))
								.addGroup(pnlCapNhatLayout.createSequentialGroup()
										.addComponent(pnlAnh, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addComponent(pnlButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));

		tabs.addTab(languagePack.getString("tabUpdate"), pnlCapNhat);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap().addComponent(tabs).addContainerGap())
				.addComponent(lblHeader, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(lblHeader, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(tabs)
						.addContainerGap()));

		pack();
	}
}
