package windows;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;

import org.jdatepicker.JDatePicker;
import org.jdatepicker.UtilDateModel;

import dao.HoaDonDAO;
import entities.HoaDon;
import utils.ImageHandler;
import utils.InvoicePrinting;
import utils.JDBCHelper;
import utils.MessageBox;
import utils.Theme;
import utils.Authorizer;
import utils.DateFormatter;
import utils.DocumentFiltering;
import utils.WindowProperties;

public class HoaDonChiTiet extends JDialog {

	private JButton btnThanhToan;
	private JLabel lblVND;
	private JLabel lblPhuThu;
	private JTextField txtPhuThu;
	private JLabel lblGhiChu;
	private JLabel lblGiamGia;
	private JLabel lblKhachHang;
	private JLabel lblNgayBan;
	private JLabel lblTenNV;
	private JLabel lblThanhToan;
	private JLabel lblTienHang;
	private JPanel pnlGhiChu;
	private JScrollPane scrollGhiChu;
	private JTextArea txtGhiChu;
	private JLabel txtGiamGia;
	private JLabel txtKhachHang;
	private JDatePicker txtNgayBan;
	private JTextField txtTenNV;
	private JLabel txtThanhToan;
	private JLabel txtTienHang;

	UtilDateModel dateModel = new UtilDateModel();
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy");
	Date now = new Date();
	String MaHD = null;
	int MaKH;
	String TenKH = "";
	int MaNhomKH = 1;
	String TenNhomKH = "";
	long TienHang;
	long GiamGia = 0;
	long PhuThu = 0;

	public HoaDonChiTiet() {
	}

	public HoaDonChiTiet(Frame parent, boolean modal, String maHD, int maKH, long tienHang) {
		super(parent, modal);
		this.MaHD = maHD;
		this.TienHang = tienHang;
		this.MaKH = maKH;
		initComponents();
		WindowProperties.centeringWindow(this);
		getInfoKH();
		GiamGia = getDiscount(MaNhomKH);
		KhoiTaoDonHang();
	}

	private void initComponents() {

		lblTenNV = new JLabel();
		lblNgayBan = new JLabel();
		txtTenNV = new JTextField();
		txtNgayBan = new JDatePicker(dateModel, "dd/MM/yyyy");
		lblTienHang = new JLabel();
		txtTienHang = new JLabel();
		lblKhachHang = new JLabel();
		txtKhachHang = new JLabel();
		lblGiamGia = new JLabel();
		txtGiamGia = new JLabel();
		lblPhuThu = new JLabel();
		txtPhuThu = new JTextField();
		lblThanhToan = new JLabel();
		lblVND = new JLabel();
		txtThanhToan = new JLabel();
		pnlGhiChu = new JPanel();
		lblGhiChu = new JLabel();
		scrollGhiChu = new JScrollPane();
		txtGhiChu = new JTextArea();
		btnThanhToan = new JButton();

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Chi tiết hóa đơn");
		setName("cthd");
		setResizable(false);
		setSize(new Dimension(450, 460));
		setType(Window.Type.UTILITY);

		lblTenNV.setFont(new Font("Tahoma", 1, 12));
		lblTenNV.setText("Nhân viên bán hàng");

		txtTenNV.setEditable(false);
		txtTenNV.setFocusable(false);

		txtNgayBan.setPreferredSize(new Dimension(154, 25));
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		dateModel.setDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
		dateModel.setSelected(true);
		txtNgayBan.setEnabled(Authorizer.user.isRole() ? true : false);

		lblNgayBan.setFont(new Font("Tahoma", 1, 12));
		lblNgayBan.setText("Ngày bán");

		lblTienHang.setFont(new Font("Tahoma", 1, 12));
		lblTienHang.setText("  Tiền hàng");
		lblTienHang.setIcon(ImageHandler.imageMaker("resources/icons/cart.png", 28, 28));

		txtTienHang.setFont(new Font("Tahoma", 1, 12));
		txtTienHang.setHorizontalAlignment(SwingConstants.TRAILING);
		txtTienHang.setText("0 VNĐ");

		lblKhachHang.setFont(new Font("Tahoma", 1, 12));
		lblKhachHang.setText("  Khách hàng");
		lblKhachHang.setIcon(ImageHandler.imageMaker("resources/icons/user-shape.png", 28, 28));

		txtKhachHang.setFont(new Font("Tahoma", 1, 12));
		txtKhachHang.setHorizontalAlignment(SwingConstants.TRAILING);
		txtKhachHang.setText("  Khách vãng lai");

		lblGiamGia.setFont(new Font("Tahoma", 1, 12));
		lblGiamGia.setText("  Giảm giá");
		lblGiamGia.setIcon(ImageHandler.imageMaker("resources/icons/discount.png", 28, 28));

		txtGiamGia.setFont(new Font("Tahoma", 1, 12));
		txtGiamGia.setHorizontalAlignment(SwingConstants.TRAILING);
		txtGiamGia.setText("0 VNĐ");

		lblPhuThu.setFont(new Font("Tahoma", 1, 12));
		lblPhuThu.setText("  Phụ thu");
		lblPhuThu.setIcon(ImageHandler.imageMaker("resources/icons/plus.png", 28, 28));

		txtPhuThu.setHorizontalAlignment(JTextField.TRAILING);

		lblThanhToan.setFont(new Font("Tahoma", 1, 12));
		lblThanhToan.setText("  Thanh toán");
		lblThanhToan.setIcon(ImageHandler.imageMaker("resources/icons/money.png", 28, 28));

		lblVND.setFont(new Font("Tahoma", 1, 12));
		lblVND.setHorizontalAlignment(SwingConstants.TRAILING);
		lblVND.setText("VNĐ");

		txtThanhToan.setFont(new Font("Tahoma", 1, 12));
		txtThanhToan.setHorizontalAlignment(SwingConstants.TRAILING);
		txtThanhToan.setText("0 VNĐ");

		lblGhiChu.setFont(new Font("Tahoma", 1, 12));
		lblGhiChu.setText("Ghi chú");

		txtGhiChu.setColumns(20);
		txtGhiChu.setFont(new Font("Courier New", 0, 12));
		txtGhiChu.setRows(5);
		scrollGhiChu.setViewportView(txtGhiChu);

		GroupLayout pnlGhiChuLayout = new GroupLayout(pnlGhiChu);
		pnlGhiChu.setLayout(pnlGhiChuLayout);
		pnlGhiChuLayout.setHorizontalGroup(pnlGhiChuLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlGhiChuLayout.createSequentialGroup()
						.addComponent(lblGhiChu, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
						.addGap(36, 36, 36).addComponent(scrollGhiChu)));
		pnlGhiChuLayout.setVerticalGroup(pnlGhiChuLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(lblGhiChu, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
				.addComponent(scrollGhiChu, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE));

		btnThanhToan.setBackground(Theme.getColor("GDTQ.btnThanhToan.background"));
		btnThanhToan.setForeground(new Color(248, 248, 248));
		;
		btnThanhToan.setFont(new Font("Tahoma", 1, 13));
		btnThanhToan.setText("Thanh toán");
		btnThanhToan.setBorderPainted(false);
		btnThanhToan.setHorizontalAlignment(JButton.CENTER);
		btnThanhToan.setIcon(ImageHandler.imageMaker("resources/icons/printer.png", 24, 24));
		btnThanhToan.putClientProperty("JButton.buttonType", "roundRect");

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addGap(45, 45, 45)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(pnlGhiChu, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout
										.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
										.addComponent(lblThanhToan, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(lblPhuThu, GroupLayout.Alignment.LEADING,
												GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(lblGiamGia, GroupLayout.Alignment.LEADING,
												GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(lblKhachHang, GroupLayout.Alignment.LEADING,
												GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(lblTienHang, GroupLayout.Alignment.LEADING,
												GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(txtTenNV, GroupLayout.Alignment.LEADING)
										.addComponent(lblTenNV, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
										.addComponent(txtGiamGia, GroupLayout.Alignment.TRAILING,
												GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(txtKhachHang, GroupLayout.Alignment.TRAILING,
												GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
										.addComponent(txtTienHang, GroupLayout.Alignment.TRAILING,
												GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(txtNgayBan, GroupLayout.Alignment.TRAILING)
										.addComponent(lblNgayBan, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addGroup(GroupLayout.Alignment.TRAILING,
												layout.createSequentialGroup().addComponent(txtPhuThu)
														.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
														.addComponent(lblVND))
										.addComponent(txtThanhToan, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE))))
				.addGap(47, 47, 47))
				.addGroup(GroupLayout.Alignment.TRAILING,
						layout.createSequentialGroup().addGap(160, 160, 160)
								.addComponent(btnThanhToan, GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
								.addGap(160, 160, 160)));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addContainerGap(19, Short.MAX_VALUE)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblTenNV)
						.addComponent(lblNgayBan))
				.addGap(11, 11, 11)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
						.addComponent(txtTenNV, GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE).addComponent(txtNgayBan))
				.addGap(11, 11, 11)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
						.addComponent(lblTienHang, GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
						.addComponent(txtTienHang, GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(lblKhachHang, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtKhachHang, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 28,
								Short.MAX_VALUE))
				.addGap(11, 11, 11)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
						.addComponent(lblGiamGia, GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
						.addComponent(txtGiamGia, GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
				.addGap(11, 11, 11)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(lblPhuThu, GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(txtPhuThu, GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
								.addComponent(lblVND, GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)))
				.addGap(11, 11, 11)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblThanhToan, GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
						.addComponent(txtThanhToan, GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
				.addGap(11, 11, 11)
				.addComponent(pnlGhiChu, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE)
				.addGap(15, 15, 15).addComponent(btnThanhToan, GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
				.addGap(15, 15, 15)));

		pack();
	}

	private void KhoiTaoDonHang() {
		txtTenNV.setText(Authorizer.user.getTenNV());
		txtTienHang.setText(String.valueOf(TienHang) + " VNĐ");
		txtKhachHang.setText(String.format("%s (%s)", TenKH, TenNhomKH));
		txtGiamGia.setText(String.valueOf(GiamGia) + " VNĐ");
		long total = TienHang - GiamGia;
		txtThanhToan.setText(String.valueOf(total) + " VNĐ");
		PlainDocument validatePhuThuDoc = (PlainDocument) txtPhuThu.getDocument();
		DocumentFiltering.LongFilter(validatePhuThuDoc);
		txtPhuThu.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				changeTotal(total);
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				changeTotal(total);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				changeTotal(total);
			}
		});
		btnThanhToan.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ThanhToanHoaDon();
			}
		});
	}

	private void changeTotal(Long TongTien) {
		long total = 0;
		if (txtPhuThu.getText().equals("")) {
			total = TongTien;
		} else {
			long PhuThu = Long.valueOf(txtPhuThu.getText());
			total = TongTien + PhuThu;
		}
		txtThanhToan.setText(String.valueOf(total) + " VNĐ");
	}

	private void getInfoKH() {
		String SELECT_BY_KEY = "SELECT KhachHang.TenKH, NhomKhachHang.MaNhomKH, TenNhomKH"
				+ "	FROM KhachHang JOIN NhomKhachHang ON KhachHang.MaNhomKH = NhomKhachHang.MaNhomKH WHERE MaKH=?";
		try {
			ResultSet rs = JDBCHelper.query(SELECT_BY_KEY, MaKH);
			while (rs.next()) {
				TenKH = rs.getString("TenKH");
				MaNhomKH = rs.getInt("MaNhomKH");
				TenNhomKH = rs.getString("TenNhomKH");
			}
			rs.getStatement().getConnection().close();
		} catch (Exception e) {
			System.out.println("get Info: " + e);
		}
	}

	public long getDiscount(int MaNhomKH) {
		long DISCOUNT_AMOUNT = 0;
		String SELECT_BY_KEY = "SELECT NhomKhachHang.MaNhomKH, NgayKetThuc, ChietKhau FROM NhomKhachHang LEFT JOIN ChiTietKhuyenMai ON NhomKhachHang.MaNhomKH = ChiTietKhuyenMai.MaNhomKH LEFT JOIN KhuyenMai ON KhuyenMai.MaKM = ChiTietKhuyenMai.MaKM WHERE NhomKhachHang.MaNhomKH=?";
		try {
			ResultSet rs = JDBCHelper.query(SELECT_BY_KEY, MaNhomKH);
			while (rs.next()) {
				String discount = rs.getString("ChietKhau");
				Date EndDate = rs.getDate("NgayKetThuc");
				String nowTime = DateFormatter.toString(now);
				String endTime = DateFormatter.toString(EndDate);
				if (EndDate.before(now) && !endTime.equals(nowTime))
					DISCOUNT_AMOUNT += 0;
				if (discount == null)
					DISCOUNT_AMOUNT += 0;
				if (discount.endsWith("%")) {
					DISCOUNT_AMOUNT += TienHang * Integer.valueOf(discount.substring(0, discount.indexOf("%"))) / 100;
				} else {
					DISCOUNT_AMOUNT += Long.valueOf(discount);
				}
			}
			rs.getStatement().getConnection().close();
		} catch (Exception e) {
			System.out.println("get Discount: " + e);
		}
		return DISCOUNT_AMOUNT;
	}

	private void ThanhToanHoaDon() {
		String MaNV = Authorizer.user.getMaNV();
		Date dateValue = dateModel.getValue();
		java.sql.Date date = utils.DateFormatter.toSqlDate(dateValue);
		long PhuThu = txtPhuThu.getText().isEmpty() ? 0 : Long.valueOf(txtPhuThu.getText());
		long TongTien = Long.valueOf(txtThanhToan.getText().substring(0, txtThanhToan.getText().indexOf(" ")));
		String GhiChu = txtGhiChu.getText();
		HoaDon hd = new HoaDon(MaHD, MaNV, date, MaKH, PhuThu, GiamGia, TongTien, GhiChu);
		if (new HoaDonDAO().insert(hd)) {
			this.dispose();
			GiaoDienBanHang.triggerCTHD(MaHD);
			GiaoDienBanHang.clearList();
			InvoicePrinting.XuatHD("MaHD", MaHD, "resources/printTemplate/HoaDonThu.jrxml");
		} else {
			MessageBox.alert("Đã có lỗi xảy ra, vui lòng thử lại");
		}
	}
}
