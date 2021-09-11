package windows;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;

import java.util.ArrayList;
import java.util.List;


import dao.NhaCungCapDAO;
import entities.NhaCungCap;
import utils.ImageHandler;
import utils.MessageBox;
import utils.Theme;
import utils.Validator;
import utils.WindowProperties;

public class QLNCC extends JFrame {

	private JButton btnFirst;
	private JButton btnLast;
	private JButton btnMoi;
	private JButton btnNext;
	private JButton btnPrevious;
	private JButton btnSua;
	private JButton btnThem;
	private JButton btnXoa;
	private JScrollPane scrollList;
	private JTable tblList;
	private JLabel lblDiaChi;
	private JLabel lblGhiChu;
	private JLabel lblHeader;
	private JLabel lblSDT;
	private JLabel lblTenNCC;
	private JLabel lblTenNLH;
	private JPanel pnlButton;
	private JPanel pnlDanhSach;
	private JScrollPane scrollDiaChi;
	
	private JScrollPane scrollGhiChu;
	private JTextArea txtDiaChi;
	private JTextArea txtGhiChu;
	private JTextField txtSDT;
	private JTextField txtTenNCC;
	private JTextField txtTenNLH;
	private JTextField txtTimKiem;

	List<NhaCungCap> listNCC = new ArrayList<>();
	int index = 0;

	public QLNCC() {
		initComponents();
		Customize();
	}

	private void Customize() {
		WindowProperties.centeringWindow(this);
		WindowProperties.CellRenderTable(tblList, tblList.getColumnCount());
		WindowProperties.resizableWindow(this, 700, this.getHeight());
		WindowProperties.setApplicationIcon(this);
		// set icon
		btnFirst.setIcon(ImageHandler.imageMaker("resources/icons/first-index.png", 15, 15));
		btnPrevious.setIcon(ImageHandler.imageMaker("resources/icons/previous.png", 15, 15));
		btnNext.setIcon(ImageHandler.imageMaker("resources/icons/next.png", 15, 15));
		btnLast.setIcon(ImageHandler.imageMaker("resources/icons/last-index.png", 15, 15));
		//
		txtTimKiem.putClientProperty("JTextField.placeholderText", "Nhập tên nhà cung cấp...");
		tblList.setAutoCreateRowSorter(true);
		tblList.getColumnModel().getColumn(0).setMinWidth(0);
		tblList.getColumnModel().getColumn(0).setMaxWidth(0);
		tblList.getColumnModel().getColumn(0).setWidth(0);
		tblList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				MouseClick();
			}
		});
		// event
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
		HienThiTable();
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
		HienThiForm();
	}

	private void HienThiTable() {
		DefaultTableModel model = (DefaultTableModel) tblList.getModel();
		model.setRowCount(0);
		listNCC.clear();
		listNCC = new NhaCungCapDAO().selectAll();
		for (NhaCungCap ncc : listNCC) {
			Object[] row = { ncc.getMaNCC(), ncc.getTenNCC(), ncc.getSDT(), ncc.getNguoiLienHe(), ncc.getDiaChi() };
			model.addRow(row);
		}
		if (listNCC.size() == 0) newRecord();
	}

	private void HienThiTimKiem(String key) {
		List<NhaCungCap> list = new ArrayList<>();
		for (NhaCungCap ncc : listNCC) {
			if (ncc.getTenNCC().toLowerCase().contains(key.toLowerCase()))
				list.add(ncc);
		}
		DefaultTableModel model = (DefaultTableModel) tblList.getModel();
		model.setRowCount(0);
		for (NhaCungCap ncc : list) {
			Object[] row = { ncc.getMaNCC(), ncc.getTenNCC(), ncc.getSDT(), ncc.getNguoiLienHe(), ncc.getDiaChi() };
			model.addRow(row);
		}
	}

	private void MouseClick() {
		int row = tblList.getSelectedRow();
		if (row < 0)
			return;
		for (NhaCungCap ncc : listNCC) {
			if (ncc.getMaNCC() == (int) tblList.getValueAt(row, 0))
				index = listNCC.indexOf(ncc);
		}
		HienThiForm();
	}

	private void HienThiForm() {
		if (listNCC.size() == 0)
			return;
		NhaCungCap ncc = listNCC.get(index);
		txtTenNCC.setText(ncc.getTenNCC());
		txtSDT.setText(ncc.getSDT());
		txtTenNLH.setText(ncc.getNguoiLienHe());
		txtDiaChi.setText(ncc.getDiaChi());
		txtGhiChu.setText(ncc.getGhiChu());
		btnFirst.setEnabled(index == 0 ? false : true);
		btnLast.setEnabled(index == listNCC.size() - 1 ? false : true);
		btnThem.setEnabled(false);
		btnSua.setEnabled(true);
		btnXoa.setEnabled(true);
	}

	private NhaCungCap readForm() {
		if (Validator.isNotEmpty(txtTenNCC, "Chưa nhập tên nhà cung cấp") == false)
			return null;
		String TenNCC = txtTenNCC.getText();
		String SDT = txtSDT.getText();
		String NLH = txtTenNLH.getText();
		String DiaChi = txtDiaChi.getText();
		String GhiChu = txtGhiChu.getText();
		NhaCungCap ncc = new NhaCungCap(TenNCC, SDT, DiaChi, NLH, GhiChu);
		return ncc;
	}

	private void clearForm() {
		txtTenNCC.setText("");
		txtSDT.setText("");
		txtTenNLH.setText("");
		txtDiaChi.setText("");
		txtGhiChu.setText("");
	}

	private void newRecord() {
		clearForm();
		btnThem.setEnabled(true);
		btnSua.setEnabled(false);
		btnXoa.setEnabled(false);
	}

	private void insert() {
		NhaCungCap ncc = readForm();
		if (ncc == null)
			return;
		if (MessageBox.confirm("Bạn có muốn thêm nhà cung cấp này?") == false)
			return;
		if (new NhaCungCapDAO().insert(ncc)) {
			HienThiTable();
			index = listNCC.size() - 1;
			HienThiForm();
			MessageBox.notif("Thêm nhà cung cấp thành công");
		} else {
			MessageBox.alert("Đã có lỗi xảy ra, vui lòng thử lại");
		}
	}

	private void update() {
		NhaCungCap old = listNCC.get(index);
		NhaCungCap ncc = readForm();
		if (MessageBox.confirm("Bạn có muốn sửa thông tin nhà cung cấp này?") == false)
			return;
		if (new NhaCungCapDAO().update(old, ncc)) {
			HienThiTable();
			HienThiForm();
			MessageBox.notif("Sửa thông tin nhà cung cấp thành công");
		} else {
			MessageBox.alert("Đã có lỗi xảy ra, vui lòng thử lại");
		}
	}

	private void delete() {
		NhaCungCap xoa = listNCC.get(index);
		if (MessageBox.confirm("Bạn có muốn xóa nhà cung cấp này?") == false)
			return;
		if (new NhaCungCapDAO().delete(xoa)) {
			HienThiTable();
			index = 0;
			HienThiForm();
			MessageBox.notif("Xóa nhà cung cấp thành công");
		} else {
			MessageBox.alert("Đã có lỗi xảy ra, vui lòng thử lại");
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
		if (index == listNCC.size() - 1)
			return;
		index++;
		HienThiForm();
	}

	private void goToLast() {
		index = listNCC.size() - 1;
		HienThiForm();
	}

	private void initComponents() {

		lblHeader = new JLabel();
		lblTenNCC = new JLabel();
		txtTenNCC = new JTextField();
		lblTenNLH = new JLabel();
		txtTenNLH = new JTextField();
		txtSDT = new JTextField();
		lblSDT = new JLabel();
		scrollDiaChi = new JScrollPane();
		txtDiaChi = new JTextArea();
		lblDiaChi = new JLabel();
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
		pnlDanhSach = new JPanel();
		txtTimKiem = new JTextField();
		scrollList = new JScrollPane();
		tblList = new JTable();

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Quản lý nhà cung cấp");
		setPreferredSize(new Dimension(630, 690));
		
		lblHeader.setForeground(Theme.getColor("lblHeader.foreground"));

		lblHeader.setFont(new Font("Tahoma", 1, 15)); // NOI18N
		lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeader.setText("QUẢN LÝ THÔNG TIN NHÀ CUNG CẤP");
		lblHeader.setHorizontalTextPosition(SwingConstants.CENTER);
		lblHeader.setPreferredSize(new Dimension(477, 30));

		lblTenNCC.setFont(new Font("Tahoma", 1, 12)); // NOI18N
		lblTenNCC.setHorizontalAlignment(SwingConstants.TRAILING);
		lblTenNCC.setText("Tên nhà cung cấp");
		lblTenNCC.setPreferredSize(new Dimension(120, 24));

		txtTenNCC.setPreferredSize(new Dimension(180, 24));

		lblTenNLH.setFont(new Font("Tahoma", 1, 12)); // NOI18N
		lblTenNLH.setHorizontalAlignment(SwingConstants.TRAILING);
		lblTenNLH.setText("Tên người liên hệ");
		lblTenNLH.setPreferredSize(new Dimension(120, 24));

		txtTenNLH.setPreferredSize(new Dimension(180, 24));

		txtSDT.setPreferredSize(new Dimension(180, 24));

		lblSDT.setFont(new Font("Tahoma", 1, 12)); // NOI18N
		lblSDT.setText("SĐT");
		lblSDT.setPreferredSize(new Dimension(115, 24));

		scrollDiaChi.setPreferredSize(new Dimension(180, 90));

		txtDiaChi.setColumns(15);
		txtDiaChi.setRows(5);
		txtDiaChi.setPreferredSize(new Dimension(100, 50));
		scrollDiaChi.setViewportView(txtDiaChi);

		lblDiaChi.setFont(new Font("Tahoma", 1, 12)); // NOI18N
		lblDiaChi.setHorizontalAlignment(SwingConstants.TRAILING);
		lblDiaChi.setText("Địa chỉ");
		lblDiaChi.setPreferredSize(new Dimension(120, 24));

		lblGhiChu.setFont(new Font("Tahoma", 1, 12)); // NOI18N
		lblGhiChu.setHorizontalAlignment(SwingConstants.TRAILING);
		lblGhiChu.setText("Ghi chú");
		lblGhiChu.setPreferredSize(new Dimension(105, 24));

		scrollGhiChu.setPreferredSize(new Dimension(180, 90));

		txtGhiChu.setColumns(15);
		txtGhiChu.setRows(5);
		txtGhiChu.setPreferredSize(new Dimension(100, 50));
		scrollGhiChu.setViewportView(txtGhiChu);

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
										GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
								.addComponent(btnFirst, GroupLayout.PREFERRED_SIZE,
										GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(btnPrevious, GroupLayout.PREFERRED_SIZE,
										GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(btnNext, GroupLayout.PREFERRED_SIZE,
										GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(btnLast, GroupLayout.PREFERRED_SIZE,
										GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)));
		pnlButtonLayout.setVerticalGroup(pnlButtonLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(GroupLayout.Alignment.TRAILING, pnlButtonLayout.createSequentialGroup()
						.addGap(0, 0, 0)
						.addGroup(pnlButtonLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(btnMoi, GroupLayout.PREFERRED_SIZE,
										GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnThem, GroupLayout.PREFERRED_SIZE,
										GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnSua, GroupLayout.PREFERRED_SIZE,
										GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnXoa, GroupLayout.PREFERRED_SIZE,
										GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnFirst, GroupLayout.PREFERRED_SIZE, 28,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(btnPrevious, GroupLayout.PREFERRED_SIZE, 28,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(btnNext, GroupLayout.PREFERRED_SIZE, 28,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(btnLast, GroupLayout.PREFERRED_SIZE, 28,
										GroupLayout.PREFERRED_SIZE))));

		pnlDanhSach.setBorder(BorderFactory.createTitledBorder(null, "Danh sách nhà cung cấp",
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
				new Font("Tahoma", 1, 14))); // NOI18N

		tblList.setModel(new DefaultTableModel(new Object[][] {

		}, new String[] { "#", "Tên nhà cung cấp", "SĐT", "Người liên hệ", "Địa chỉ" }) {
			Class[] types = new Class[] { java.lang.Integer.class, java.lang.String.class, java.lang.String.class,
					java.lang.String.class, java.lang.String.class };
			boolean[] canEdit = new boolean[] { false, false, false, false, false };

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		tblList.getTableHeader().setReorderingAllowed(false);
		scrollList.setViewportView(tblList);

		GroupLayout pnlDanhSachLayout = new GroupLayout(pnlDanhSach);
		pnlDanhSach.setLayout(pnlDanhSachLayout);
		pnlDanhSachLayout
				.setHorizontalGroup(pnlDanhSachLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(GroupLayout.Alignment.TRAILING,
								pnlDanhSachLayout.createSequentialGroup().addGap(5, 5, 5)
										.addGroup(pnlDanhSachLayout
												.createParallelGroup(GroupLayout.Alignment.TRAILING)
												.addComponent(scrollList).addComponent(txtTimKiem))
										.addGap(5, 5, 5)));
		pnlDanhSachLayout
				.setVerticalGroup(pnlDanhSachLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(pnlDanhSachLayout.createSequentialGroup().addGap(5, 5, 5)
								.addComponent(txtTimKiem, GroupLayout.PREFERRED_SIZE,
										GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(scrollList, GroupLayout.PREFERRED_SIZE, 293, Short.MAX_VALUE)
								.addGap(5, 5, 5)));

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addContainerGap()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(lblHeader, GroupLayout.PREFERRED_SIZE,
								GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
										.addComponent(lblTenNCC, GroupLayout.PREFERRED_SIZE,
												GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
										.addComponent(lblTenNLH, GroupLayout.PREFERRED_SIZE,
												GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
										.addComponent(lblDiaChi, GroupLayout.Alignment.TRAILING,
												GroupLayout.PREFERRED_SIZE,
												GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
								.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
										.addComponent(txtTenNLH, GroupLayout.PREFERRED_SIZE,
												GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
										.addComponent(txtTenNCC, GroupLayout.PREFERRED_SIZE,
												GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
										.addComponent(scrollDiaChi, GroupLayout.PREFERRED_SIZE,
												GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 19,
										Short.MAX_VALUE)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addGroup(GroupLayout.Alignment.TRAILING, layout
												.createSequentialGroup()
												.addComponent(lblSDT, GroupLayout.PREFERRED_SIZE, 30,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
												.addComponent(txtSDT, GroupLayout.PREFERRED_SIZE, 180,
														GroupLayout.PREFERRED_SIZE))
										.addGroup(GroupLayout.Alignment.TRAILING, layout
												.createSequentialGroup()
												.addComponent(lblGhiChu, GroupLayout.PREFERRED_SIZE, 86,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
												.addComponent(scrollGhiChu, GroupLayout.PREFERRED_SIZE,
														GroupLayout.PREFERRED_SIZE,
														GroupLayout.PREFERRED_SIZE))))
						.addComponent(pnlButton, GroupLayout.PREFERRED_SIZE,
								GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
						.addComponent(pnlDanhSach, GroupLayout.PREFERRED_SIZE,
								GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
				.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(lblHeader, GroupLayout.PREFERRED_SIZE,
								GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGap(20, 20, 20)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(lblTenNCC, GroupLayout.PREFERRED_SIZE,
										GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtTenNCC, GroupLayout.PREFERRED_SIZE,
										GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtSDT, GroupLayout.PREFERRED_SIZE,
										GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblSDT, GroupLayout.PREFERRED_SIZE,
										GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGap(15, 15, 15)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(lblTenNLH, GroupLayout.PREFERRED_SIZE,
										GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtTenNLH, GroupLayout.PREFERRED_SIZE,
										GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGap(15, 15, 15)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(scrollDiaChi, GroupLayout.PREFERRED_SIZE, 90,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblDiaChi, GroupLayout.PREFERRED_SIZE,
										GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(scrollGhiChu, GroupLayout.PREFERRED_SIZE, 90,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblGhiChu, GroupLayout.PREFERRED_SIZE,
										GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addComponent(pnlButton, GroupLayout.PREFERRED_SIZE,
								GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(pnlDanhSach, GroupLayout.PREFERRED_SIZE,
								GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
						.addContainerGap()));

		pack();
	}
}
