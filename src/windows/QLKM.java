package windows;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JTable;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.PlainDocument;

import org.jdatepicker.JDatePicker;
import org.jdatepicker.UtilDateModel;

import dao.KhuyenMaiDAO;
import dao.NhomKhachHangDAO;
import entities.KhuyenMai;
import entities.NhomKhachHang;
import utils.DocumentFiltering;
import utils.ImageHandler;
import utils.MessageBox;
import utils.Theme;
import utils.Validator;
import utils.WindowProperties;

public class QLKM extends JFrame {

	private JButton btnFirst;
	private ButtonGroup btnGrpKM;
	private JButton btnLast;
	private JButton btnMoi;
	private JButton btnNext;
	private JButton btnPrevious;
	private JButton btnSua;
	private JButton btnThem;
	private JButton btnXoa;
	private JLabel lblGhiChu;
	private JLabel lblHeader;
	private JLabel lblLoaiKM;
	private JLabel lblNgayBatDau;
	private JLabel lblNgayKetThuc;
	private JLabel lblNoiDung;
	private JLabel lblTenKM;
	private JPanel pnlButton;
	private JPanel pnlCapNhat;
	private JPanel pnlChiTietKM;
	private JPanel pnlDanhSach;
	private JRadioButton rdoCash;
	private JRadioButton rdoPercent;
	private JScrollPane scrollGhiChu;
	private JScrollPane scrollTable;
	private JTabbedPane tabs;
	private JTable tblList;
	private JTextArea txtGhiChu;
	private JDatePicker txtNgayBatDau;
	private JDatePicker txtNgayKetThuc;
	private JTextField txtNoiDung;
	private JTextField txtTenKM;
	private JTextField txtTimKiem;
	private JLabel lblDoiTuong;
	private JList<String> lstDoiTuong;
	private JScrollPane scrollList;

	List<KhuyenMai> listKM = new ArrayList<>();
	List<NhomKhachHang> listNKH = new ArrayList<>();
	int index = 0;
	UtilDateModel beginDateModel = new UtilDateModel();
	UtilDateModel endDateModel = new UtilDateModel();

	public QLKM() {
		initComponents();
		Customize();
	}

	private void Customize() {
		WindowProperties.centeringWindow(this);
		WindowProperties.resizableWindow(this, 800, this.getHeight());
		WindowProperties.setApplicationIcon(this);
		WindowProperties.CellRenderTable(tblList, tblList.getModel().getColumnCount());
		// set icon
		btnFirst.setIcon(ImageHandler.imageMaker("resources/icons/first-index.png", 15, 15));
		btnPrevious.setIcon(ImageHandler.imageMaker("resources/icons/previous.png", 15, 15));
		btnNext.setIcon(ImageHandler.imageMaker("resources/icons/next.png", 15, 15));
		btnLast.setIcon(ImageHandler.imageMaker("resources/icons/last-index.png", 15, 15));
		///
		txtTimKiem.putClientProperty("JTextField.placeholderText", "Nhập tên khuyến mãi...");
		PlainDocument kmDoc = (PlainDocument) txtNoiDung.getDocument();
		DocumentFiltering.IntFilter(kmDoc);
		// event
		HienThiTable();
		LoadList();
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
				MouseClick();
			}
		});
		tblList.setAutoCreateRowSorter(true);
		TableRowSorter<TableModel> rowSorter = (TableRowSorter<TableModel>) tblList.getRowSorter();
		rowSorter.setSortable(3, false);
		rowSorter.setSortable(4, false);
		TableColumnModel tcm = tblList.getColumnModel();
		tcm.getColumn(0).setMinWidth(0);
		tcm.getColumn(0).setMaxWidth(0);
		tcm.getColumn(0).setWidth(0);
		//
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
		//
		HienThiForm();
	}

	private void HienThiTable() {
		DefaultTableModel model = (DefaultTableModel) tblList.getModel();
		model.setRowCount(0);
		listKM.clear();
		listKM = new KhuyenMaiDAO().selectAll();
		for (KhuyenMai km : listKM) {
			Object[] row = { km.getTenKM(), km.getNgayBatDau(), km.getNgayKetThuc(), km.getChietKhau(),
					km.getGhiChu() };
			model.addRow(row);
		}
		if (listKM.size() == 0) newRecord();
	}

	private void HienThiTimKiem(String key) {
		List<KhuyenMai> listTimKiem = new ArrayList<>();
		for (KhuyenMai khuyenMai : listKM) {
			if (khuyenMai.getTenKM().toLowerCase().contains(key.toLowerCase())) {
				listTimKiem.add(khuyenMai);
			}
		}
		DefaultTableModel model = (DefaultTableModel) tblList.getModel();
		model.setRowCount(0);
		for (KhuyenMai khuyenMai : listTimKiem) {
			Object[] row = {};
			model.addRow(row);
		}
	}

	private void MouseClick() {
		int row = tblList.getSelectedRow();
		if (row < 0)
			return;
		for (KhuyenMai khuyenMai : listKM) {
			if (khuyenMai.getMaKM() == (int) tblList.getValueAt(row, 0))
				index = listKM.indexOf(khuyenMai);
		}
		tabs.setSelectedIndex(1);
		HienThiForm();
	}

	private void HienThiForm() {
		if (listKM.size() == 0)
			return;
		KhuyenMai km = listKM.get(index);
		txtTenKM.setText(km.getTenKM());
		beginDateModel.setValue(km.getNgayBatDau());
		endDateModel.setValue(km.getNgayKetThuc());
		String NoiDungKM = km.getChietKhau();
		if (km.getChietKhau().endsWith("%")) {
			txtNoiDung.setText(NoiDungKM.substring(0, NoiDungKM.indexOf("%")));
			rdoPercent.setSelected(true);
		} else {
			txtNoiDung.setText(NoiDungKM);
			rdoCash.setSelected(true);
		}
		txtGhiChu.setText(km.getGhiChu());
		btnFirst.setEnabled(index == 0 ? false : true);
		btnLast.setEnabled(index == listKM.size() - 1 ? false : true);
		btnThem.setEnabled(false);
		btnSua.setEnabled(true);
		btnXoa.setEnabled(true);
		fillList(km.getMaKM());

	}

	private KhuyenMai readForm() {
		if (Validator.isNotEmpty(txtTenKM, "Chưa nhập tên khuyến mãi") == false
				|| Validator.isSetDate(txtNgayBatDau, "Chưa chọn ngày bắt đầu") == false
				|| Validator.isSetDate(txtNgayKetThuc, "Chưa chọn ngày kết thúc") == false
				|| Validator.isNotEmpty(txtNoiDung, "Chưa nhập nội dung khuyến mãi") == false)
			return null;
		String TenKM = txtTenKM.getText();
		Date NgayBatDau = beginDateModel.getValue();
		Date NgayKetThuc = endDateModel.getValue();
		String ChietKhau = rdoPercent.isSelected() ? txtNoiDung.getText() + "%" : txtNoiDung.getText();
		String GhiChu = txtGhiChu.getText();
		KhuyenMai km = new KhuyenMai(TenKM, NgayBatDau, NgayKetThuc, ChietKhau, GhiChu);
		return km;
	}

	private void clearForm() {
		txtTenKM.setText("");
		beginDateModel.setValue(null);
		endDateModel.setValue(null);
		rdoPercent.setSelected(true);
		txtNoiDung.setText("");
		txtGhiChu.setText("");
		lstDoiTuong.setSelectedIndex(0);
	}

	private void LoadList() {
		listNKH.clear();
		listNKH = new NhomKhachHangDAO().selectAll();

		DefaultListModel<String> listModel = new DefaultListModel<>();
		for (NhomKhachHang nhomKhachHang : listNKH) {
			listModel.addElement(nhomKhachHang.getTenNhomKH());
		}
		lstDoiTuong.setModel(listModel);
		lstDoiTuong.setSelectionBackground(new Color(91, 81, 120));
		lstDoiTuong.setSelectionForeground(new Color(232, 232, 232));
		lstDoiTuong.setFont(new Font("Tahoma", 1, 12));
		scrollList.putClientProperty("JScrollPane.smoothScrolling", true);
	}

	private void fillList(int MaKM) {

		lstDoiTuong.clearSelection();
		lstDoiTuong.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		String SELECT_BY_KEY = "SELECT NhomKhachHang.MaNhomKH, TenNhomKH FROM ChiTietKhuyenMai JOIN KhuyenMai ON KhuyenMai.MaKM = ChiTietKhuyenMai.MaKM JOIN NhomKhachHang ON NhomKhachHang.MaNhomKH = ChiTietKhuyenMai.MaNhomKH WHERE KhuyenMai.MaKM=?";
		List<NhomKhachHang> listHienThi = new NhomKhachHangDAO().select(SELECT_BY_KEY, MaKM);
		if (listHienThi.size() == 0)
			return;
		int[] indices = new int[listHienThi.size()];
		int j = 0;
		ListModel<String> model = lstDoiTuong.getModel();
		for (int i = 0; i < model.getSize(); i++) {
			if (j == listHienThi.size())
				break;
			String key = model.getElementAt(i);
			for (NhomKhachHang nhomKhachHang : listHienThi) {
				if (key.equals(nhomKhachHang.getTenNhomKH())) {
					indices[j++] = i;
				}
			}

		}
		lstDoiTuong.setSelectedIndices(indices);
	}

	private List<NhomKhachHang> readList() {
		int[] indices = lstDoiTuong.getSelectedIndices();

		List<NhomKhachHang> list = new ArrayList<>();
		for (int i = 0; i < indices.length; i++) {
			list.add(listNKH.get(indices[i]));
		}
		if (list.size() == 0) {
			MessageBox.alert("Chưa chọn đối tượng áp dụng cho khuyến mãi");
			lstDoiTuong.requestFocus();
			return null;
		}

		return list;
	}

	private void newRecord() {
		clearForm();
		btnThem.setEnabled(true); 
		btnSua.setEnabled(false);
		btnXoa.setEnabled(false);
	}

	private void insert() {
		KhuyenMai km = readForm();
		if (km == null)
			return;

		List<NhomKhachHang> list = readList();
		if (list == null)
			return;

		if (MessageBox.confirm("Bạn có muốn thêm khuyến mãi này?") == false)
			return;

		if (new KhuyenMaiDAO().insert(km)) {
			HienThiTable();
			new KhuyenMaiDAO().insertDetails(list, listKM.get(listKM.size() - 1).getMaKM());
			btnThem.setEnabled(false);
			index = listKM.size() - 1;
			HienThiForm();
			MessageBox.notif("Thêm khuyến mãi thành công");
		} else {
			MessageBox.alert("Đã có lỗi xảy ra, vui lòng thử lại!");
		}
	}

	private void update() {
		KhuyenMai cu = listKM.get(index);
		KhuyenMai km = readForm();
		if (km == null)
			return;

		List<NhomKhachHang> list = readList();
		if (list == null)
			return;

		if (MessageBox.confirm("Bạn có muốn sửa thông khuyến mãi này?") == false)
			return;
		if (new KhuyenMaiDAO().update(cu, km) && new KhuyenMaiDAO().deleteDetails(cu.getMaKM())
				&& new KhuyenMaiDAO().insertDetails(list, cu.getMaKM())) {
			HienThiTable();
			MessageBox.notif("Sửa thông tin khuyến mãi thành công");
		} else {
			MessageBox.alert("Đã có lỗi xảy ra, vui lòng thử lại!");
		}

	}

	private void delete() {
		KhuyenMai km = listKM.get(index);
		if (MessageBox.confirm("Bạn có muốn xóa khuyến mãi này?") == false)
			return;
		if (new KhuyenMaiDAO().delete(km)) {
			HienThiTable();
			index = 0;
			HienThiForm();
			MessageBox.notif("Xóa khuyến mãi thành công");
		} else {
			MessageBox.alert("Đã có lỗi xảy ra, vui lòng thử lại!");
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
		if (index == listKM.size() - 1)
			return;
		index++;
		HienThiForm();
	}

	private void goToLast() {
		index = listKM.size() - 1;
		HienThiForm();
	}

	private void initComponents() {

		setTitle("Quản lý khuyến mãi");

		btnGrpKM = new ButtonGroup();
		lblHeader = new JLabel();
		tabs = new JTabbedPane();
		pnlDanhSach = new JPanel();
		txtTimKiem = new JTextField();
		scrollTable = new JScrollPane();
		pnlCapNhat = new JPanel();
		lblNgayBatDau = new JLabel();
		lblTenKM = new JLabel();
		lblNgayKetThuc = new JLabel();
		pnlChiTietKM = new JPanel();
		lblLoaiKM = new JLabel();
		rdoPercent = new JRadioButton();
		rdoCash = new JRadioButton();
		lblNoiDung = new JLabel();
		txtNoiDung = new JTextField();
		lblGhiChu = new JLabel();
		scrollGhiChu = new JScrollPane();
		txtGhiChu = new JTextArea();
		pnlButton = new JPanel();
		btnMoi = new JButton();
		btnThem = new JButton();
		btnSua = new JButton();
		btnXoa = new JButton();
		btnFirst = new JButton();
		btnPrevious = new JButton();
		btnNext = new JButton();
		btnLast = new JButton();
		txtTenKM = new JTextField();
		txtNgayBatDau = new JDatePicker(beginDateModel, "dd/MM/yyyy");
		txtNgayKetThuc = new JDatePicker(endDateModel, "dd/MM/yyyy");
		tblList = new JTable();
		lblDoiTuong = new JLabel();
		lstDoiTuong = new JList<>();
		scrollList = new JScrollPane();

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		lblHeader.setForeground(Theme.getColor("lblHeader.foreground"));

		lblHeader.setFont(new Font("Tahoma", 1, 15)); // NOI18N
		lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeader.setText("QUẢN LÝ THÔNG TIN KHUYẾN MÃI");
		lblHeader.setHorizontalTextPosition(SwingConstants.CENTER);
		lblHeader.setPreferredSize(new Dimension(477, 30));

		tblList.setModel(new DefaultTableModel(new Object[][] {

		}, new String[] { "Mã khuyến mãi", "Tên khuyến mãi", "Ngày bắt đầu", "Ngày kết thúc", "Chiết khấu",
				"Ghi chú" }) {
			Class[] types = new Class[] { Integer.class, String.class, java.util.Date.class, java.util.Date.class,
					String.class, String.class };
			boolean[] canEdit = new boolean[] { false, false, false, false, false, false };

			public Class<?> getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				KhuyenMai km = listKM.get(rowIndex);
				Object returnValue = null;

				switch (columnIndex) {
				case 0:
					returnValue = km.getMaKM();
					break;
				case 1:
					returnValue = km.getTenKM();
					break;
				case 2:
					returnValue = km.getNgayBatDau();
					break;
				case 3:
					returnValue = km.getNgayKetThuc();
					break;
				case 4:
					returnValue = km.getChietKhau();
					break;
				case 5:
					returnValue = km.getGhiChu();
					break;
				default:
					throw new IllegalArgumentException("Invalid column index");
				}

				return returnValue;
			}
		});

		tblList.getTableHeader().setReorderingAllowed(false);
		scrollTable.setViewportView(tblList);
		if (tblList.getColumnModel().getColumnCount() > 0) {
			tblList.getColumnModel().getColumn(0).setPreferredWidth(100);
		}

		GroupLayout pnlDanhSachLayout = new GroupLayout(pnlDanhSach);
		pnlDanhSach.setLayout(pnlDanhSachLayout);
		pnlDanhSachLayout.setHorizontalGroup(pnlDanhSachLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlDanhSachLayout.createSequentialGroup().addContainerGap()
						.addGroup(pnlDanhSachLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(txtTimKiem)
								.addComponent(scrollTable, GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE))
						.addContainerGap()));
		pnlDanhSachLayout.setVerticalGroup(pnlDanhSachLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlDanhSachLayout.createSequentialGroup().addContainerGap()
						.addComponent(txtTimKiem, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(scrollTable, GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE).addContainerGap()));

		tabs.addTab("Danh sách", pnlDanhSach);

		lblNgayBatDau.setFont(new Font("Tahoma", 0, 12)); // NOI18N
		lblNgayBatDau.setText("Ngày bắt đầu");
		lblNgayBatDau.setPreferredSize(new Dimension(100, 24));

		lblTenKM.setFont(new Font("Tahoma", 0, 12)); // NOI18N
		lblTenKM.setText("Tên khuyến mãi");
		lblTenKM.setPreferredSize(new Dimension(100, 24));

		lblNgayKetThuc.setFont(new Font("Tahoma", 0, 12)); // NOI18N
		lblNgayKetThuc.setText("Ngày kết thúc");
		lblNgayKetThuc.setPreferredSize(new Dimension(100, 24));

		pnlChiTietKM.setBackground(new Color(232, 232, 232));

		lblLoaiKM.setFont(new Font("Tahoma", 0, 12)); // NOI18N
		lblLoaiKM.setText("Loại khuyến mãi");
		lblLoaiKM.setPreferredSize(new Dimension(100, 24));

		rdoPercent.setText("Phần trăm (%)");
		rdoPercent.setMinimumSize(new Dimension(100, 24));
		rdoPercent.setPreferredSize(new Dimension(100, 24));

		rdoCash.setText("Số tiền cụ thể (VNĐ)");
		rdoCash.setMinimumSize(new Dimension(120, 24));
		rdoCash.setPreferredSize(new Dimension(150, 24));

		lblNoiDung.setFont(new Font("Tahoma", 0, 12)); // NOI18N
		lblNoiDung.setText("Nội dung khuyến mãi");
		lblNoiDung.setPreferredSize(new Dimension(120, 24));

		txtNoiDung.setPreferredSize(new Dimension(218, 24));

		lblGhiChu.setFont(new Font("Tahoma", 0, 12)); // NOI18N
		lblGhiChu.setText("Ghi chú");
		lblGhiChu.setPreferredSize(new Dimension(120, 24));

		scrollGhiChu.setPreferredSize(new Dimension(218, 113));

		txtGhiChu.setColumns(20);
		txtGhiChu.setRows(5);
		scrollGhiChu.setViewportView(txtGhiChu);

		btnGrpKM.add(rdoPercent);
		btnGrpKM.add(rdoCash);

		lblDoiTuong.setFont(new java.awt.Font("Tahoma", 0, 12));
		lblDoiTuong.setText("Đối tượng áp dụng");

		lstDoiTuong.setPreferredSize(new java.awt.Dimension(150, 50));
		scrollList.setViewportView(lstDoiTuong);
		lblDoiTuong.setPreferredSize(new Dimension(120, 24));

		lstDoiTuong.setPreferredSize(new Dimension(150, 50));
		scrollList.setViewportView(lstDoiTuong);

		GroupLayout pnlChiTietKMLayout = new GroupLayout(pnlChiTietKM);
		pnlChiTietKM.setLayout(pnlChiTietKMLayout);
		pnlChiTietKMLayout.setHorizontalGroup(pnlChiTietKMLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlChiTietKMLayout.createSequentialGroup().addGap(77, 77, 77).addGroup(pnlChiTietKMLayout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(pnlChiTietKMLayout.createSequentialGroup()
								.addGroup(pnlChiTietKMLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(lblNoiDung, GroupLayout.PREFERRED_SIZE, 120,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(lblLoaiKM, GroupLayout.PREFERRED_SIZE, 100,
												GroupLayout.PREFERRED_SIZE))
								.addGap(18, 18, 18)
								.addGroup(pnlChiTietKMLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(txtNoiDung, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addGroup(pnlChiTietKMLayout.createSequentialGroup()
												.addComponent(rdoPercent, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addGap(18, 18, 18).addComponent(rdoCash, GroupLayout.DEFAULT_SIZE, 145,
														Short.MAX_VALUE))))
						.addGroup(pnlChiTietKMLayout.createSequentialGroup()
								.addGroup(pnlChiTietKMLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(lblDoiTuong, GroupLayout.PREFERRED_SIZE, 120,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(lblGhiChu, GroupLayout.PREFERRED_SIZE, 120,
												GroupLayout.PREFERRED_SIZE))
								.addGap(18, 18, 18)
								.addGroup(pnlChiTietKMLayout
										.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(scrollGhiChu,
												GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(scrollList))))
						.addGap(77, 77, 77)));
		pnlChiTietKMLayout.setVerticalGroup(pnlChiTietKMLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlChiTietKMLayout.createSequentialGroup().addContainerGap()
						.addGroup(pnlChiTietKMLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(lblLoaiKM, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(rdoPercent, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(rdoCash, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addGap(10, 10, 10)
						.addGroup(pnlChiTietKMLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(lblNoiDung, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(txtNoiDung, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addGap(10, 10, 10)
						.addGroup(pnlChiTietKMLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addGroup(pnlChiTietKMLayout.createSequentialGroup()
										.addComponent(lblDoiTuong, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addGap(0, 0, Short.MAX_VALUE))
								.addComponent(scrollList, GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(pnlChiTietKMLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(lblGhiChu, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(scrollGhiChu, GroupLayout.PREFERRED_SIZE, 115,
										GroupLayout.PREFERRED_SIZE))
						.addContainerGap()));

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
		;
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
								.addComponent(btnFirst, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnPrevious, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnNext, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnLast, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))));

		txtTenKM.setPreferredSize(new Dimension(200, 24));

		txtNgayBatDau.setPreferredSize(new Dimension(200, 24));

		txtNgayKetThuc.setPreferredSize(new Dimension(200, 24));

		GroupLayout pnlCapNhatLayout = new GroupLayout(pnlCapNhat);
		pnlCapNhat.setLayout(pnlCapNhatLayout);
		pnlCapNhatLayout.setHorizontalGroup(pnlCapNhatLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlCapNhatLayout.createSequentialGroup()
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(pnlCapNhatLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(lblNgayKetThuc, GroupLayout.PREFERRED_SIZE, 100,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblTenKM, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNgayBatDau, GroupLayout.PREFERRED_SIZE, 100,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(pnlCapNhatLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(txtTenKM, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtNgayBatDau, GroupLayout.PREFERRED_SIZE, 200,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(txtNgayKetThuc, GroupLayout.PREFERRED_SIZE, 200,
										GroupLayout.PREFERRED_SIZE))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addGroup(pnlCapNhatLayout.createSequentialGroup().addContainerGap()
						.addGroup(pnlCapNhatLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(pnlChiTietKM, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(pnlButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE))
						.addContainerGap()));
		pnlCapNhatLayout.setVerticalGroup(pnlCapNhatLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlCapNhatLayout.createSequentialGroup().addGap(24, 24, 24)
						.addGroup(pnlCapNhatLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addGroup(pnlCapNhatLayout.createSequentialGroup()
										.addGroup(pnlCapNhatLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(lblTenKM, GroupLayout.PREFERRED_SIZE,
														GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(txtTenKM, GroupLayout.PREFERRED_SIZE,
														GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
										.addGap(10, 10, 10)
										.addComponent(lblNgayBatDau, GroupLayout.PREFERRED_SIZE,
												GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
										.addGap(10, 10, 10))
								.addGroup(pnlCapNhatLayout.createSequentialGroup()
										.addComponent(txtNgayBatDau, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addGap(12, 12, 12)))
						.addGroup(pnlCapNhatLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(lblNgayKetThuc, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(txtNgayKetThuc, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(pnlChiTietKM, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(pnlButton,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));

		tabs.addTab("Cập nhật", pnlCapNhat);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(
								layout.createSequentialGroup().addContainerGap()
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
												.addComponent(lblHeader, GroupLayout.DEFAULT_SIZE,
														GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(tabs))
										.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(lblHeader, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(tabs)
						.addContainerGap()));

		pack();
	}
}
