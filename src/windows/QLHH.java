package windows;

import utils.DocumentFiltering;
import utils.ImageHandler;
import utils.Language;
import utils.MessageBox;
import utils.Theme;
import utils.Validator;
import utils.WindowProperties;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import javax.swing.text.PlainDocument;

import dao.DanhMucDAO;
import dao.MatHangDAO;
import entities.DanhMuc;
import entities.MatHang;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class QLHH extends JFrame {

	private JButton btnFirst;
	private JButton btnLast;
	private JButton btnMoi;
	private JButton btnNext;
	private JButton btnPrevious;
	private JButton btnSua;
	private JButton btnThem;
	private JButton btnThemNhomHang;
	private JButton btnXoa;
	private JComboBox<String> cboNhomHang;
	private JLabel lblDonGia;
	private JLabel lblDonVi;
	private JLabel lblHeader;
	private JLabel lblImage;
	private JLabel lblMaHang;
	private JLabel lblNSX;
	private JLabel lblNhomHang;
	private JLabel lblSoLuong;
	private JLabel lblTenHang;
	private JLabel lblGiaNhap;
	private JPanel pnlButton;
	private JPanel pnlCapNhat;
	private JPanel pnlDanhSach;
	private JPanel pnlNullLayout;
	private JScrollPane scrollTable;
	private JTable tblList;
	private JTextField txtDonGia;
	private JTextField txtDonVi;
	private JTextField txtMaHang;
	private JTextField txtNSX;
	private JTextField txtSoLuong;
	private JTextField txtTenHang;
	private JTextField txtGiaNhap;
	private JTextField txtTimKiem;

	String defaultImage = "resources/images/products/no_image.png";
	String currentImage = null;
	File tempImage = null;
	List<MatHang> listMH = new ArrayList<>();
	List<DanhMuc> listDM = new ArrayList<>();
	int index = 0;

	ResourceBundle languagePack = Language.getLanguagePack();
	ResourceBundle messagePack = Language.getMessagePack();

	public QLHH() {
		initComponents();
		Customize();
	}

	private void Customize() {
		WindowProperties.centeringWindow(this);
		WindowProperties.resizableWindow(this, 950, this.getHeight());
		WindowProperties.setApplicationIcon(this);
		WindowProperties.CellRenderTable(tblList, tblList.getModel().getColumnCount());
		// set icon
		lblImage.setIcon(ImageHandler.imageMaker(defaultImage, lblImage.getWidth() - 2, lblImage.getHeight() - 2));
		btnFirst.setIcon(ImageHandler.imageMaker("resources/icons/first-index.png", 15, 15));
		btnPrevious.setIcon(ImageHandler.imageMaker("resources/icons/previous.png", 15, 15));
		btnNext.setIcon(ImageHandler.imageMaker("resources/icons/next.png", 15, 15));
		btnLast.setIcon(ImageHandler.imageMaker("resources/icons/last-index.png", 15, 15));
		btnThemNhomHang.setIcon(ImageHandler.imageMaker("resources/icons/plus.png", 18, 18));
		txtTimKiem.putClientProperty("JTextField.placeholderText", languagePack.getString("plhdProduct"));
		// event
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
		loadComBoBox();
		tblList.setAutoCreateRowSorter(true);
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
		lblImage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				getImageFromFile();
			}
		});
		btnThemNhomHang.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ThemDanhMuc();
			}
		});
		//
		HienThiTable();
		HienThiForm();
		PlainDocument ThueDocument = (PlainDocument) txtGiaNhap.getDocument();
		DocumentFiltering.IntFilter(ThueDocument);
		PlainDocument DonGiaDocument = (PlainDocument) txtDonGia.getDocument();
		DocumentFiltering.IntFilter(DonGiaDocument);
		// popup menu in combobox
		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem updateDM = new JMenuItem(languagePack.getString("renameCategory"));
		updateDM.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SuaDanhMuc(cboNhomHang.getSelectedItem().toString());
			}
		});
		JMenuItem deleteDM = new JMenuItem(languagePack.getString("removeCategory"));
		deleteDM.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				XoaDanhMuc(cboNhomHang.getSelectedItem().toString());
			}
		});
		popupMenu.add(updateDM);
		popupMenu.add(deleteDM);
		cboNhomHang.setComponentPopupMenu(popupMenu);
	}
	

	private void HienThiTable() {
		DefaultTableModel model = (DefaultTableModel) tblList.getModel();
		model.setRowCount(0);
		listMH.clear();
		listMH = new MatHangDAO().selectAll();
		for (MatHang mh : listMH) {
			Object[] row = { mh.getMaHang(), mh.getTenHang(), mh.getTenDanhMuc(), mh.getDonViTinh(), mh.getSoLuong(),
					mh.getGiaNhap(), mh.getGiaBan(), mh.getNSX() };
			model.addRow(row);
		}
		if (listMH.size() == 0)
			newRecord();
	}

	private void HienThiTimKiem(String key) {
		List<MatHang> listMHTimKiem = new ArrayList<>();
		for (MatHang mh : listMH) {
			if (mh.getMaHang().toLowerCase().contains(key.toLowerCase())
					|| mh.getTenHang().toLowerCase().contains(key.toLowerCase())) {
				listMHTimKiem.add(mh);
			}
		}
		DefaultTableModel model = (DefaultTableModel) tblList.getModel();
		model.setRowCount(0);
		for (MatHang mh : listMHTimKiem) {
			Object[] row = { mh.getMaHang(), mh.getTenHang(), mh.getTenDanhMuc(), mh.getDonViTinh(), mh.getSoLuong(),
					mh.getGiaNhap(), mh.getGiaBan(), mh.getNSX() };
			model.addRow(row);
		}
	}

	private void MouseClick() {
		int row = tblList.getSelectedRow();
		if (row < 0)
			return;

		String key = tblList.getValueAt(row, 0).toString();
		for (MatHang mh : listMH) {
			if (mh.getMaHang().equals(key))
				index = listMH.indexOf(mh);
		}

		HienThiForm();
	}

	private void loadComBoBox() {
		listDM.clear();
		cboNhomHang.removeAllItems();
		listDM = new DanhMucDAO().selectAll();
		for (DanhMuc dm : listDM) {
			cboNhomHang.addItem(dm.getTenDanhMuc());
		}
	}

	private void HienThiForm() {
		if (listMH.size() == 0)
			return;
		MatHang mh = listMH.get(index);
		txtMaHang.setText(mh.getMaHang());
		txtTenHang.setText(mh.getTenHang());
		txtNSX.setText(mh.getNSX());
		txtDonVi.setText(mh.getDonViTinh());
		txtDonGia.setText(mh.getGiaBan() + "");
		txtSoLuong.setText(mh.getSoLuong() + "");
		txtGiaNhap.setText(mh.getGiaNhap() + "");
		cboNhomHang.setSelectedItem(mh.getTenDanhMuc());
		setImage(mh.getHinh());
		txtGiaNhap.setEditable(false);
		btnFirst.setEnabled(index == 0 ? false : true);
		btnLast.setEnabled(index == listMH.size() - 1 ? false : true);
		btnThem.setEnabled(false);
		btnSua.setEnabled(true);
		btnXoa.setEnabled(true);
		if (mh.getSoLuong() <= 0) {
			txtSoLuong.setForeground(Color.red);
		} else {
			txtSoLuong.setForeground(new Color(56, 118, 29));
		}
	}

	private MatHang readForm() {
		// validate
		if (Validator.isNotEmpty(txtMaHang, messagePack.getString("emptyProductID")) == false
				|| Validator.isNotEmpty(txtTenHang, messagePack.getString("emptyProductName")) == false
				|| Validator.isNotEmpty(txtDonVi, messagePack.getString("emptyUnit")) == false
				|| Validator.isNotEmpty(txtDonGia, messagePack.getString("emptySaleprice")) == false
				|| Validator.isNotEmpty(txtGiaNhap, languagePack.getString("emptyImportPrice")) == false)
			return null;
		if (Validator.isMeetLengthCondition(txtMaHang, 10,
				Language.getLanguagePack().getString("productIDMeetLength")) == false)
			return null;
		String MaHang = txtMaHang.getText().toUpperCase();
		String TenHang = txtTenHang.getText();
		int MaDanhMuc = 0;
		String TenDanhMuc = cboNhomHang.getSelectedItem().toString();
		for (DanhMuc dm : listDM) {
			if (dm.getTenDanhMuc().equals(TenDanhMuc)) {
				MaDanhMuc = dm.getMaDanhMuc();
				break;
			}
		}
		String NSX = txtNSX.getText();
		String DonVi = txtDonVi.getText();
		long DonGia = Long.valueOf(txtDonGia.getText());
		int SoLuong = Integer.valueOf(txtSoLuong.getText());
		long GiaNhap = Long.valueOf(txtGiaNhap.getText());
		String Hinh = currentImage;
		if (MaDanhMuc == 0)
			return null;
		MatHang mh = new MatHang(MaHang, TenHang, MaDanhMuc, TenDanhMuc, DonVi, SoLuong, GiaNhap, DonGia, NSX, Hinh);
		return mh;
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
		tempImage = ImageHandler.getImageFile(languagePack.getString("chooseProductImageDialog"));
		if (tempImage == null)
			return;
		currentImage = "resources/images/products/" + tempImage.getName();
		lblImage.setIcon(ImageHandler.imageMaker(tempImage, lblImage.getWidth() - 2, lblImage.getHeight() - 2));
	}

	private void saveImage() {
		if (tempImage == null)
			return;
		String dir = "resources/images/staffs/";
		ImageHandler.saveFile(tempImage, dir);
	}

	private void clearForm() {
		txtMaHang.setText("");
		txtTenHang.setText("");
		txtNSX.setText("");
		txtDonVi.setText("");
		txtDonGia.setText("");
		txtSoLuong.setText("0");
		txtGiaNhap.setText("0");
		setImage(null);
		tblList.clearSelection();
	}

	private void newRecord() {
		clearForm();
		btnThem.setEnabled(true);
		btnSua.setEnabled(false);
		btnXoa.setEnabled(false);
	}

	private void insert() {
		MatHang mh = readForm();
		if (mh == null)
			return;
		for (MatHang mathang : listMH) {
			if (mh.getMaHang().equals(mathang.getMaHang())) {
				MessageBox.alert(messagePack.getString("duplicatedProductID"));
				txtMaHang.requestFocus();
				return;
			}
		}
		if (MessageBox.confirm(messagePack.getString("confirmAddHH")) == false)
			return;
		if (new MatHangDAO().insert(mh)) {
			saveImage();
			HienThiTable();
			index = listMH.size() - 1;
			HienThiForm();
			MessageBox.notif(messagePack.getString("addHHSucessfully"));
		} else {
			MessageBox.alert(messagePack.getString("errorOccurred"));
		}
	}

	private void update() {
		MatHang old = listMH.get(index);
		MatHang mh = readForm();
		if (mh == null)
			return;
		if (MessageBox.confirm(messagePack.getString("confirmModifyHH")) == false)
			return;
		if (new MatHangDAO().update(old, mh)) {
			saveImage();
			HienThiTable();
			MessageBox.notif(messagePack.getString("modifyHHSucessfully"));
		} else {
			MessageBox.alert(messagePack.getString("errorOccurred"));
		}
	}

	private void delete() {
		MatHang mh = listMH.get(index);
		if (MessageBox.confirm(messagePack.getString("confirmDeleteHH")) == false)
			return;
		if (new MatHangDAO().delete(mh)) {
			HienThiTable();
			index = 0;
			HienThiForm();
			MessageBox.notif(messagePack.getString("deleteHHSucessfully"));
		} else {
			MessageBox.alert(messagePack.getString("errorOccurred"));
		}
	}

	private void ThemDanhMuc() {
		String TenDanhMuc = MessageBox.prompt(languagePack.getString("addCategoryHeader"),
				languagePack.getString("addCategoryDialog"));
		if (TenDanhMuc == null)
			return;
		for (DanhMuc danhMuc : listDM) {
			if (danhMuc.getTenDanhMuc().equals(TenDanhMuc)) {
				MessageBox.alert(messagePack.getString("duplicatedCategoryName"));
				return;
			}
		}
		DanhMuc dm = new DanhMuc(TenDanhMuc);
		if (new DanhMucDAO().insert(dm)) {
			MessageBox.notif(messagePack.getString("addCategorySucessfully"));
			loadComBoBox();
			cboNhomHang.setSelectedItem(TenDanhMuc);
		} else {
			MessageBox.alert(messagePack.getString("errorOccurred"));
		}
	}

	private void SuaDanhMuc(String key) {
		if (key.isEmpty())
			return;
		DanhMuc cu = null;
		for (DanhMuc danhMuc : listDM) {
			if (danhMuc.getTenDanhMuc().equals(key))
				cu = danhMuc;
		}

		String TenMoi = MessageBox.prompt(languagePack.getString("modifyCategoryHeader"),
				languagePack.getString("modifyCategoryDialog"));
		if (TenMoi == null)
			return;

		for (DanhMuc dm : listDM) {
			if (dm.getTenDanhMuc().equals(TenMoi)) {
				MessageBox.alert(messagePack.getString("duplicatedCategoryName"));
				return;
			}
		}
		DanhMuc moi = new DanhMuc(TenMoi);
		if (new DanhMucDAO().update(cu, moi)) {
			MessageBox.notif(
					String.format(messagePack.getString("modifyCategorySucessfully"), cu.getTenDanhMuc(), TenMoi));
			loadComBoBox();
			cboNhomHang.setSelectedItem(TenMoi);
		} else {
			MessageBox.alert(messagePack.getString("errorOccurred"));
		}

	}

	private void XoaDanhMuc(String key) {
		if (key.isEmpty())
			return;
		DanhMuc xoa = null;
		for (DanhMuc danhMuc : listDM) {
			if (danhMuc.getTenDanhMuc().equals(key))
				xoa = danhMuc;
		}
		if (MessageBox.confirm(messagePack.getString("deleteCategoryConfirm")) == false)
			return;
		if (new DanhMucDAO().delete(xoa)) {
			MessageBox.notif(String.format(messagePack.getString("deleteCategorySucessfully"), xoa.getTenDanhMuc()));
			loadComBoBox();
			cboNhomHang.setSelectedItem(0);
		} else {
			MessageBox.alert(String.format(messagePack.getString("unableToDeleteCategory"), xoa.getTenDanhMuc()));
		}
	}

	private void goToFirst() {
		index = 0;
		HienThiForm();
		tblList.setRowSelectionInterval(index, index);
	}

	private void previous() {
		if (index <= 0)
			return;
		index--;
		HienThiForm();
		tblList.setRowSelectionInterval(index, index);
	}

	private void next() {
		if (index == listMH.size() - 1)
			return;
		index++;
		HienThiForm();
		tblList.setRowSelectionInterval(index, index);
	}

	private void goToLast() {
		index = listMH.size() - 1;
		HienThiForm();
		tblList.setRowSelectionInterval(index, index);
	}

	private void initComponents() {
		setTitle(languagePack.getString("qlmhTitle"));

		lblHeader = new JLabel();
		pnlCapNhat = new JPanel();
		lblImage = new JLabel();
		lblMaHang = new JLabel();
		txtMaHang = new JTextField();
		lblTenHang = new JLabel();
		txtTenHang = new JTextField();
		lblNhomHang = new JLabel();
		cboNhomHang = new JComboBox<>();
		btnThemNhomHang = new JButton();
		lblNSX = new JLabel();
		txtNSX = new JTextField();
		pnlNullLayout = new JPanel();
		lblDonVi = new JLabel();
		txtDonVi = new JTextField();
		lblSoLuong = new JLabel();
		txtSoLuong = new JTextField();
		lblDonGia = new JLabel();
		txtDonGia = new JTextField();
		lblGiaNhap = new JLabel();
		txtGiaNhap = new JTextField();
		pnlButton = new JPanel();
		btnMoi = new JButton();
		btnThem = new JButton();
		btnSua = new JButton();
		btnXoa = new JButton();
		btnFirst = new JButton();
		btnPrevious = new JButton();
		btnNext = new JButton();
		btnLast = new JButton();
		pnlDanhSach = new JPanel();
		txtTimKiem = new JTextField();
		scrollTable = new JScrollPane();
		tblList = new JTable();

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setPreferredSize(new Dimension(700, 750));
		
		lblHeader.setForeground(Theme.getColor("lblHeader.foreground"));

		btnThem.setEnabled(false);

		lblHeader.setFont(new Font("Tahoma", 1, 15)); // NOI18N
		lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeader.setText(languagePack.getString("qlmhHeader"));
		lblHeader.setHorizontalTextPosition(SwingConstants.CENTER);
		lblHeader.setPreferredSize(new Dimension(477, 30));

		lblImage.setHorizontalAlignment(SwingConstants.CENTER);
		lblImage.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204)));
		lblImage.setFocusable(false);
		lblImage.setHorizontalTextPosition(SwingConstants.CENTER);
		lblImage.setPreferredSize(new Dimension(200, 200));

		lblMaHang.setFont(new Font("Tahoma", 0, 12)); // NOI18N
		lblMaHang.setText(languagePack.getString("lblMaHang"));
		lblMaHang.setPreferredSize(new Dimension(100, 24));

		txtMaHang.setPreferredSize(new Dimension(342, 24));
		;

		lblTenHang.setFont(new Font("Tahoma", 0, 12)); // NOI18N
		lblTenHang.setText(languagePack.getString("lblTenHang"));
		lblTenHang.setPreferredSize(new Dimension(100, 24));

		txtTenHang.setPreferredSize(new Dimension(342, 24));

		lblNhomHang.setFont(new Font("Tahoma", 0, 12)); // NOI18N
		lblNhomHang.setText(languagePack.getString("category"));
		lblNhomHang.setPreferredSize(new Dimension(100, 24));

		cboNhomHang.setPreferredSize(new Dimension(316, 24));

		btnThemNhomHang.setMargin(new Insets(2, 2, 2, 2));
		btnThemNhomHang.setPreferredSize(new Dimension(24, 24));
		btnThemNhomHang.setBackground(this.getBackground());
		btnThemNhomHang.setBorder(null);

		lblNSX.setFont(new Font("Tahoma", 0, 12)); // NOI18N
		lblNSX.setText(languagePack.getString("producer"));
		lblNSX.setPreferredSize(new Dimension(100, 24));

		txtNSX.setPreferredSize(new Dimension(342, 24));

		pnlNullLayout.setLayout(null);

		lblDonVi.setFont(new Font("Tahoma", 0, 12)); // NOI18N
		lblDonVi.setText(languagePack.getString("unit"));
		lblDonVi.setPreferredSize(new Dimension(60, 20));
		pnlNullLayout.add(lblDonVi);
		lblDonVi.setBounds(25, 10, 60, 20);

		txtDonVi.setPreferredSize(new Dimension(348, 20));
		pnlNullLayout.add(txtDonVi);
		txtDonVi.setBounds(95, 10, 110, 20);

		lblSoLuong.setFont(new Font("Tahoma", 0, 12)); // NOI18N
		lblSoLuong.setText(languagePack.getString("lblSoLuong"));
		lblSoLuong.setPreferredSize(new Dimension(70, 20));
		pnlNullLayout.add(lblSoLuong);
		lblSoLuong.setBounds(245, 10, 70, 20);

		txtSoLuong.setEditable(false);
		txtSoLuong.setFont(new Font("Tahoma", 1, 12));
		txtSoLuong.setPreferredSize(new Dimension(348, 20));
		pnlNullLayout.add(txtSoLuong);
		txtSoLuong.setBounds(320, 10, 110, 20);

		lblDonGia.setFont(new Font("Tahoma", 0, 12)); // NOI18N
		lblDonGia.setText(languagePack.getString("lblDonGia"));
		lblDonGia.setPreferredSize(new Dimension(60, 20));
		pnlNullLayout.add(lblDonGia);
		lblDonGia.setBounds(25, 40, 60, 20);

		txtDonGia.setPreferredSize(new Dimension(348, 20));
		pnlNullLayout.add(txtDonGia);
		txtDonGia.setBounds(95, 40, 110, 20);

		lblGiaNhap.setFont(new Font("Tahoma", 0, 12)); // NOI18N
		lblGiaNhap.setText(languagePack.getString("lblGiaNhap"));
		lblGiaNhap.setPreferredSize(new Dimension(70, 20));
		pnlNullLayout.add(lblGiaNhap);
		lblGiaNhap.setBounds(245, 40, 70, 20);

		txtGiaNhap.setEditable(false);
		txtGiaNhap.setPreferredSize(new Dimension(348, 20));
		pnlNullLayout.add(txtGiaNhap);
		txtGiaNhap.setBounds(320, 40, 110, 20);

		btnMoi.setFont(new Font("Tahoma", 1, 12)); // NOI18N
		btnMoi.setText(languagePack.getString("btnMoi"));
		btnMoi.setMargin(new Insets(2, 5, 2, 5));
		btnMoi.setPreferredSize(new Dimension(60, 32));

		btnThem.setFont(new Font("Tahoma", 1, 12)); // NOI18N
		btnThem.setText(languagePack.getString("btnThem"));
		btnThem.setMargin(new Insets(2, 5, 2, 5));
		btnThem.setPreferredSize(new Dimension(60, 32));

		btnSua.setFont(new Font("Tahoma", 1, 12)); // NOI18N
		btnSua.setText(languagePack.getString("btnSua"));
		btnSua.setMargin(new Insets(2, 5, 2, 5));
		btnSua.setPreferredSize(new Dimension(60, 32));

		btnXoa.setFont(new Font("Tahoma", 1, 12)); // NOI18N
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
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(btnLast,
										GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)));
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
								.addComponent(btnFirst, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(btnPrevious, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(btnNext, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(btnLast, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))));

		GroupLayout pnlCapNhatLayout = new GroupLayout(pnlCapNhat);
		pnlCapNhat.setLayout(pnlCapNhatLayout);
		pnlCapNhatLayout.setHorizontalGroup(pnlCapNhatLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlCapNhatLayout.createSequentialGroup().addContainerGap().addGroup(pnlCapNhatLayout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(pnlCapNhatLayout.createSequentialGroup()
								.addComponent(lblImage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addGap(10, 10, 10)
								.addGroup(pnlCapNhatLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(pnlNullLayout, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addGroup(pnlCapNhatLayout.createSequentialGroup()
												.addGroup(pnlCapNhatLayout
														.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
														.addComponent(lblNSX, GroupLayout.DEFAULT_SIZE,
																GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
														.addComponent(lblMaHang, GroupLayout.Alignment.LEADING,
																GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(lblNhomHang, GroupLayout.DEFAULT_SIZE,
																GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
														.addComponent(lblTenHang, GroupLayout.DEFAULT_SIZE,
																GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
												.addGap(10, 10, 10)
												.addGroup(pnlCapNhatLayout
														.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(txtMaHang, GroupLayout.Alignment.TRAILING,
																GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addGroup(GroupLayout.Alignment.TRAILING,
																pnlCapNhatLayout.createSequentialGroup()
																		.addGap(0, 0, Short.MAX_VALUE)
																		.addComponent(btnThemNhomHang,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE))
														.addComponent(txtTenHang, GroupLayout.Alignment.TRAILING,
																GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(txtNSX, GroupLayout.Alignment.TRAILING,
																GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addGroup(pnlCapNhatLayout.createSequentialGroup()
																.addComponent(cboNhomHang, GroupLayout.PREFERRED_SIZE,
																		316, GroupLayout.PREFERRED_SIZE)
																.addGap(26, 26, 26))))))
						.addComponent(pnlButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addContainerGap()));
		pnlCapNhatLayout.setVerticalGroup(pnlCapNhatLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlCapNhatLayout.createSequentialGroup()
						.addGroup(pnlCapNhatLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addGroup(pnlCapNhatLayout.createSequentialGroup()
										.addGroup(pnlCapNhatLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
												.addComponent(lblMaHang, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(txtMaHang, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
										.addGroup(pnlCapNhatLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
												.addComponent(lblTenHang, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(txtTenHang, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
										.addGroup(pnlCapNhatLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(lblNhomHang, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(cboNhomHang, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(btnThemNhomHang, GroupLayout.PREFERRED_SIZE, 21,
														GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
										.addGroup(pnlCapNhatLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
												.addComponent(lblNSX, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(txtNSX, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(pnlNullLayout, GroupLayout.PREFERRED_SIZE, 80,
												GroupLayout.PREFERRED_SIZE))
								.addComponent(lblImage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
						.addComponent(pnlButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addGap(0, 0, 0)));

		pnlCapNhatLayout.linkSize(SwingConstants.VERTICAL, new Component[] { lblMaHang, txtMaHang });

		pnlDanhSach.setPreferredSize(new Dimension(680, 400));

		txtTimKiem.setPreferredSize(new Dimension(660, 24));

		scrollTable.setAutoscrolls(true);
		scrollTable.setPreferredSize(new Dimension(452, 350));

		tblList.setModel(new DefaultTableModel(new Object[][] {

		}, new String[] { languagePack.getString("productID"), languagePack.getString("productName"),
				languagePack.getString("category"), languagePack.getString("unit"),
				languagePack.getString("lblSoLuong"), languagePack.getString("lblGiaNhap"),
				languagePack.getString("lblDonGia"), languagePack.getString("producer") }) {
			Class[] types = new Class[] { java.lang.String.class, java.lang.String.class, java.lang.String.class,
					java.lang.String.class, java.lang.Integer.class, java.lang.Long.class, Long.class,
					java.lang.String.class };
			boolean[] canEdit = new boolean[] { false, false, false, false, false, false, false, false };

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		tblList.setFocusable(false);
		tblList.setPreferredSize(new Dimension(525, 350));
		tblList.getTableHeader().setReorderingAllowed(false);
		scrollTable.setViewportView(tblList);
		tblList.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		GroupLayout pnlDanhSachLayout = new GroupLayout(pnlDanhSach);
		pnlDanhSach.setLayout(pnlDanhSachLayout);
		pnlDanhSachLayout.setHorizontalGroup(pnlDanhSachLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlDanhSachLayout.createSequentialGroup().addContainerGap()
						.addGroup(pnlDanhSachLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(txtTimKiem, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
										Short.MAX_VALUE)
								.addComponent(scrollTable, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE))
						.addContainerGap()));
		pnlDanhSachLayout.setVerticalGroup(pnlDanhSachLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlDanhSachLayout.createSequentialGroup().addGap(0, 0, 0)
						.addComponent(txtTimKiem, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(scrollTable, GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)));

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(lblHeader, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE,
						GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(pnlCapNhat, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(pnlDanhSach, GroupLayout.DEFAULT_SIZE, 682, Short.MAX_VALUE))
						.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGap(10, 10, 10)
						.addComponent(lblHeader, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addGap(10, 10, 10)
						.addComponent(pnlCapNhat, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addGap(15, 15, 15).addComponent(pnlDanhSach, GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
						.addGap(15, 15, 15)));
		pack();
	}
}
