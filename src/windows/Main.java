package windows;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.intellijthemes.FlatDarkFlatIJTheme;

import java.awt.*;

import utils.Authorizer;
import utils.ImageHandler;
import utils.Language;
import utils.MessageBox;
import utils.Theme;
import utils.WindowProperties;

public class Main extends JFrame {
	private JToggleButton btnHome;
	private JToggleButton btnBanHang;
	private JToggleButton btnThongKe;
	private ButtonGroup navBtnGrp;
	private JButton btnDangXuat;
	private JLabel lblDateTime;
	private JLabel lblIUserIcon;
	private JLabel lblTenNV;
	private JLabel lblVaiTro;
	private JMenuBar menuBar;
	private JMenuItem mniDangXuat;
	private JMenuItem mniDoiMatKhau;
	private JMenu mnuHangHoa;
	private JMenuItem mniHuongDan;
	private JMenuItem mniKhachHang;
	private JMenuItem mniKhuyenMai;
	private JMenuItem mniNhaCungCap;
	private JMenuItem mniQLTK;
	private JMenuItem mniTKBCThang;
	private JMenuItem mniTKDoanhSo;
	private JMenuItem mniTKHangHoa;
	private JMenuItem mniTKLoiNhuan;
	private JMenuItem mniTKThuChi;
	private JMenuItem mniTKTonKho;
	private JMenuItem mniThoat;
	private JMenuItem mniThongTin;
	private JMenuItem mniMatHang;
	private JMenuItem mniKhoHang;
	private JMenu mnuDanhMuc;
	private JMenu mnuGiaoDien;
	private JMenu mnuHeThong;
	private JMenu mnuNgonNgu;
	private JMenu mnuThongKe;
	private JMenu mnuTienIch;
	private JMenu mnuTroGiup;
	private JPanel panelContent;
	private JPanel panelNavigationBar;
	private JPanel panelStatusBar;
	private JRadioButtonMenuItem rdoMniSang;
	private JRadioButtonMenuItem rdoMniToi;
	private ButtonGroup btnGroupInterface;
	private JRadioButtonMenuItem rdoMniTiengViet;
	private JRadioButtonMenuItem rdoMniTiengAnh;
	private ButtonGroup btnGroupLanguage;
	private JPopupMenu.Separator s1;
	private JPopupMenu.Separator s2;
	private JPopupMenu.Separator s3;

	public Main() {
		initComponents();
		getLanguagePack();
		getThemePack();
		customizeComponents();
		lblTenNV.setText(Authorizer.user.getTenNV());
		lblVaiTro.setText(Authorizer.user.isRole() ? "Admin" : "Nhân viên");
		setPrivilege();
	}

	private void setPrivilege() {
		if (Authorizer.user.isRole() == false) {
			mnuDanhMuc.setEnabled(false);
			mnuThongKe.setEnabled(false);
			mniQLTK.setEnabled(false);
			btnThongKe.setEnabled(false);
		}
	}

	private void initComponents() {

		panelNavigationBar = new JPanel();
		lblIUserIcon = new JLabel();
		lblTenNV = new JLabel();
		lblVaiTro = new JLabel();
		btnHome = new JToggleButton();
		btnBanHang = new JToggleButton();
		btnThongKe = new JToggleButton();
		navBtnGrp = new ButtonGroup();
		btnDangXuat = new JButton();
		panelContent = new JPanel();
		panelStatusBar = new JPanel();
		lblDateTime = new JLabel();
		menuBar = new JMenuBar();
		mnuHeThong = new JMenu();
		mniQLTK = new JMenuItem();
		s1 = new JPopupMenu.Separator();
		mniDoiMatKhau = new JMenuItem();
		s2 = new JPopupMenu.Separator();
		mniDangXuat = new JMenuItem();
		mniThoat = new JMenuItem();
		mnuDanhMuc = new JMenu();
		mnuHangHoa = new JMenu();
		mniKhachHang = new JMenuItem();
		mniNhaCungCap = new JMenuItem();
		s3 = new JPopupMenu.Separator();
		mniKhuyenMai = new JMenuItem();
		mnuThongKe = new JMenu();
		mniTKDoanhSo = new JMenuItem();
		mniTKHangHoa = new JMenuItem();
		mniTKThuChi = new JMenuItem();
		mniTKTonKho = new JMenuItem();
		mniTKLoiNhuan = new JMenuItem();
		mniTKBCThang = new JMenuItem();
		mnuTienIch = new JMenu();
		mnuGiaoDien = new JMenu();
		rdoMniSang = new JRadioButtonMenuItem();
		rdoMniToi = new JRadioButtonMenuItem();
		mnuNgonNgu = new JMenu();
		btnGroupInterface = new ButtonGroup();
		rdoMniTiengViet = new JRadioButtonMenuItem();
		rdoMniTiengAnh = new JRadioButtonMenuItem();
		btnGroupLanguage = new ButtonGroup();
		mnuTroGiup = new JMenu();
		mniHuongDan = new JMenuItem();
		mniThongTin = new JMenuItem();
		mniMatHang = new JMenuItem();
		mniKhoHang = new JMenuItem();

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setName("Main");
		setMaximumSize(new Dimension(1024, 768));
		setMinimumSize(new Dimension(1024, 768));

		panelNavigationBar.setPreferredSize(new Dimension(180, 719));

		lblIUserIcon.setHorizontalAlignment(SwingConstants.CENTER);

		lblTenNV.setFont(new Font("Tahoma", 1, 12));
		lblTenNV.setHorizontalAlignment(SwingConstants.LEADING);
		lblTenNV.setText("Tên nhân viên");

		lblVaiTro.setFont(new Font("Tahoma", 1, 12));
		lblVaiTro.setForeground(new Color(255, 0, 0));
		lblVaiTro.setHorizontalAlignment(SwingConstants.LEADING);
		lblVaiTro.setText(" Role");
		lblVaiTro.setToolTipText("");
		lblVaiTro.setHorizontalTextPosition(SwingConstants.CENTER);

		btnHome.setFont(new Font("Tahoma", 1, 12));
		btnHome.setHorizontalAlignment(SwingConstants.LEADING);
		btnHome.setHorizontalTextPosition(SwingConstants.TRAILING);
		btnHome.setPreferredSize(new Dimension(80, 40));
		navBtnGrp.add(btnHome);

		btnBanHang.setFont(new Font("Tahoma", 1, 12));
		btnBanHang.setHorizontalAlignment(SwingConstants.LEADING);
		btnBanHang.setHorizontalTextPosition(SwingConstants.TRAILING);
		btnBanHang.setPreferredSize(new Dimension(80, 40));
		navBtnGrp.add(btnBanHang);

		btnThongKe.setFont(new Font("Tahoma", 1, 12));
		btnThongKe.setHorizontalAlignment(SwingConstants.LEADING);
		btnThongKe.setHorizontalTextPosition(SwingConstants.TRAILING);
		btnThongKe.setPreferredSize(new Dimension(80, 40));
		navBtnGrp.add(btnThongKe);

		btnDangXuat.setFont(new Font("Tahoma", 1, 12));
		btnDangXuat.setHorizontalAlignment(SwingConstants.CENTER);
		btnDangXuat.setPreferredSize(new Dimension(80, 40));

		GroupLayout panelNavigationBarLayout = new GroupLayout(panelNavigationBar);
		panelNavigationBar.setLayout(panelNavigationBarLayout);
		panelNavigationBarLayout.setHorizontalGroup(panelNavigationBarLayout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(panelNavigationBarLayout.createSequentialGroup().addGroup(panelNavigationBarLayout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(panelNavigationBarLayout.createSequentialGroup().addGap(30, 30, 30)
								.addGroup(panelNavigationBarLayout
										.createParallelGroup(GroupLayout.Alignment.LEADING, false)
										.addComponent(btnHome, GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
										.addComponent(btnBanHang, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(btnThongKe, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(btnDangXuat, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE))
								.addGap(0, 20, Short.MAX_VALUE))
						.addGroup(panelNavigationBarLayout.createSequentialGroup()
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblIUserIcon, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
								.addGroup(panelNavigationBarLayout
										.createParallelGroup(GroupLayout.Alignment.LEADING, false)
										.addComponent(lblTenNV, GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
										.addComponent(lblVaiTro, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE))))
						.addContainerGap()));
		panelNavigationBarLayout.setVerticalGroup(panelNavigationBarLayout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(panelNavigationBarLayout.createSequentialGroup().addGap(27, 27, 27)
						.addGroup(panelNavigationBarLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
								.addComponent(lblIUserIcon, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
								.addGroup(panelNavigationBarLayout.createSequentialGroup().addComponent(lblTenNV)
										.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(lblVaiTro)))
						.addGap(35, 35, 35)
						.addComponent(btnHome, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
						.addGap(18, 18, 18)
						.addComponent(btnBanHang, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
						.addGap(18, 18, 18)
						.addComponent(btnThongKe, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 375, Short.MAX_VALUE)
						.addComponent(btnDangXuat, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
						.addGap(21, 21, 21)));

		panelContent.setLayout(new BorderLayout());
		panelStatusBar.setPreferredSize(new Dimension(1012, 30));

		lblDateTime.setHorizontalAlignment(SwingConstants.TRAILING);
		lblDateTime.setText("HH:MM:SS XM | dd-MM-yyyy");

		GroupLayout panelStatusBarLayout = new GroupLayout(panelStatusBar);
		panelStatusBar.setLayout(panelStatusBarLayout);
		panelStatusBarLayout.setHorizontalGroup(panelStatusBarLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(GroupLayout.Alignment.TRAILING,
						panelStatusBarLayout.createSequentialGroup().addGap(814, 814, 814)
								.addComponent(lblDateTime, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
								.addContainerGap()));
		panelStatusBarLayout.setVerticalGroup(
				panelStatusBarLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(lblDateTime,
						GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE));

		menuBar.setPreferredSize(new Dimension(320, 30));

		mnuHeThong.add(mniQLTK);
		mnuHeThong.add(s1);

		mnuHeThong.add(mniDoiMatKhau);
		mnuHeThong.add(s2);

		mniDangXuat.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
		mnuHeThong.add(mniDangXuat);

		mniThoat.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK));
		mnuHeThong.add(mniThoat);

		menuBar.add(mnuHeThong);

		mnuHangHoa.setMnemonic('H');
		mniMatHang.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.CTRL_DOWN_MASK));
		mniKhoHang.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.CTRL_DOWN_MASK));
		mnuHangHoa.add(mniMatHang);
		mnuHangHoa.add(mniKhoHang);
		mnuDanhMuc.add(mnuHangHoa);

		mniKhachHang.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, InputEvent.CTRL_DOWN_MASK));
		mnuDanhMuc.add(mniKhachHang);

		mniNhaCungCap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, InputEvent.CTRL_DOWN_MASK));
		mnuDanhMuc.add(mniNhaCungCap);
		mnuDanhMuc.add(s3);

		mniKhuyenMai.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_5, InputEvent.CTRL_DOWN_MASK));
		mnuDanhMuc.add(mniKhuyenMai);

		menuBar.add(mnuDanhMuc);

		mniTKDoanhSo.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.SHIFT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK));
		mnuThongKe.add(mniTKDoanhSo);

		mniTKHangHoa.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.SHIFT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK));
		mnuThongKe.add(mniTKHangHoa);

		mniTKThuChi.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_3, InputEvent.SHIFT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK));
		mnuThongKe.add(mniTKThuChi);

		mniTKTonKho.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_4, InputEvent.SHIFT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK));
		mnuThongKe.add(mniTKTonKho);

		mniTKLoiNhuan.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_5, InputEvent.SHIFT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK));
		mnuThongKe.add(mniTKLoiNhuan);

		mniTKBCThang.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_6, InputEvent.SHIFT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK));
		mnuThongKe.add(mniTKBCThang);

		menuBar.add(mnuThongKe);

		rdoMniSang.setSelected(true);
		;
		mnuGiaoDien.add(rdoMniSang);
		btnGroupInterface.add(rdoMniSang);
		mnuGiaoDien.add(rdoMniToi);
		btnGroupInterface.add(rdoMniToi);

		mnuTienIch.add(mnuGiaoDien);
		rdoMniTiengViet.setSelected(true);
		mnuNgonNgu.add(rdoMniTiengViet);
		btnGroupLanguage.add(rdoMniTiengViet);
		mnuNgonNgu.add(rdoMniTiengAnh);
		btnGroupLanguage.add(rdoMniTiengAnh);

		mnuTienIch.add(mnuNgonNgu);

		menuBar.add(mnuTienIch);

		mniHuongDan.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		mnuTroGiup.add(mniHuongDan);

		mniThongTin.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0));
		mnuTroGiup.add(mniThongTin);

		menuBar.add(mnuTroGiup);

		setJMenuBar(menuBar);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addComponent(panelNavigationBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addGap(0, 0, 0).addComponent(panelContent, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE))
				.addComponent(panelStatusBar, GroupLayout.DEFAULT_SIZE, 1024, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addGap(0, 0, 0)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addComponent(panelContent, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(panelNavigationBar, GroupLayout.DEFAULT_SIZE, 695, Short.MAX_VALUE))
				.addGap(0, 0, 0)
				.addComponent(panelStatusBar, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
				.addGap(0, 0, 0)));

		pack();

	}

	private void customizeComponents() {
		// customize window property
		WindowProperties.centeringWindow(this);
		WindowProperties.setApplicationIcon(this);
		// customize UI
		btnHome.putClientProperty("JButton.buttonType", "roundRect");
		btnBanHang.putClientProperty("JButton.buttonType", "roundRect");
		btnThongKe.putClientProperty("JButton.buttonType", "roundRect");
		btnDangXuat.putClientProperty("JButton.buttonType", "roundRect");
		// set icon for menu bar
		mniQLTK.setIcon(ImageHandler.imageMaker("resources/icons/account.png", 24, 24));
		mniDoiMatKhau.setIcon(ImageHandler.imageMaker("resources/icons/key.png", 24, 24));
		mniDangXuat.setIcon(ImageHandler.imageMaker("resources/icons/log-out.png", 24, 24));
		mniThoat.setIcon(ImageHandler.imageMaker("resources/icons/exit.png", 24, 24));

		mnuHangHoa.setIcon(ImageHandler.imageMaker("resources/icons/merchandise.png", 24, 24));
		mniMatHang.setIcon(ImageHandler.imageMaker("resources/icons/product.png", 24, 24));
		mniKhoHang.setIcon(ImageHandler.imageMaker("resources/icons/storage.png", 24, 24));
		mniKhachHang.setIcon(ImageHandler.imageMaker("resources/icons/customer.png", 24, 24));
		mniNhaCungCap.setIcon(ImageHandler.imageMaker("resources/icons/supplier.png", 24, 24));
		mniKhuyenMai.setIcon(ImageHandler.imageMaker("resources/icons/promotions.png", 24, 24));

		mniTKDoanhSo.setIcon(ImageHandler.imageMaker("resources/icons/sales.png", 24, 24));
		mniTKHangHoa.setIcon(ImageHandler.imageMaker("resources/icons/items.png", 24, 24));
		mniTKThuChi.setIcon(ImageHandler.imageMaker("resources/icons/revenue.png", 24, 24));
		mniTKTonKho.setIcon(ImageHandler.imageMaker("resources/icons/warehouse.png", 24, 24));
		mniTKLoiNhuan.setIcon(ImageHandler.imageMaker("resources/icons/profits.png", 24, 24));
		mniTKBCThang.setIcon(ImageHandler.imageMaker("resources/icons/report.png", 24, 24));

		mnuGiaoDien.setIcon(ImageHandler.imageMaker("resources/icons/interface.png", 24, 24));
		mnuNgonNgu.setIcon(ImageHandler.imageMaker("resources/icons/language.png", 24, 24));

		mniHuongDan.setIcon(ImageHandler.imageMaker("resources/icons/instructions.png", 24, 24));
		mniThongTin.setIcon(ImageHandler.imageMaker("resources/icons/info.png", 24, 24));
		// set icon for navigation bar
		lblIUserIcon.setIcon(
				ImageHandler.imageMaker("resources/icons/user.png", lblIUserIcon.getWidth(), lblIUserIcon.getHeight()));
		btnHome.setIcon(ImageHandler.imageMaker("resources/icons/homepage.png", 24, 24));
		btnBanHang.setIcon(ImageHandler.imageMaker("resources/icons/shopping-bag.png", 26, 24));
		btnThongKe.setIcon(ImageHandler.imageMaker("resources/icons/statistics.png", 24, 24));
		btnDangXuat.setIcon(ImageHandler.imageMaker("resources/icons/log-out-2.png", 24, 24));
		// show date and time
		showDateTime();
		// initial home content
		btnHome.setSelected(true);
		GotoHome();
		// event
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (MessageBox.confirm("Xác nhận thoát ứng dụng?") == false)
					return;
				System.exit(0);
			}
		});
		btnHome.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GotoHome();
			}
		});

		btnBanHang.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GoToPOS();
			}
		});

		btnThongKe.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GotoStatistics();
			}
		});
		btnDangXuat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LogOut();
			}
		});

		mniQLTK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new QLNV().setVisible(true);
			}
		});
		mniDoiMatKhau.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new DoiMatKhau(null, true).setVisible(true);
			}
		});
		mniDangXuat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LogOut();
			}
		});
		mniThoat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (MessageBox.confirm(Language.getMessagePack().getString("exitConfirm")) == false)
					return;
				System.exit(0);
			}
		});
		mniMatHang.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new QLHH().setVisible(true);
			}
		});
		mniKhoHang.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new QLKho().setVisible(true);
			}
		});
		mniKhachHang.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new QLKH(0).setVisible(true);
			}
		});
		mniKhuyenMai.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new QLKM().setVisible(true);
			}
		});
		mniNhaCungCap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new QLNCC().setVisible(true);
			}
		});
		mniTKDoanhSo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new TKDS(null, true).setVisible(true);
			}
		});
		mniTKHangHoa.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new TKHH(null, true).setVisible(true);
			}
		});
		mniTKLoiNhuan.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new TKLN(null, true).setVisible(true);
			}
		});
		mniTKTonKho.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new TKKH(null, true).setVisible(true);
			}
		});
		mniTKThuChi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new TKTC(null, true).setVisible(true);
			}
		});

		mniTKBCThang.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new TK_BCT(null, true).setVisible(true);
			}
		});
		mniThongTin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Info(null, false).setVisible(true);
			}
		});
		mniHuongDan.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().browse(new File("resources/helps/help.html").toURI());
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});

		rdoMniSang.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (MessageBox.confirm(Language.getMessagePack().getString("changeLocale"))) {
					rdoMniToi.setSelected(true);
					FlatIntelliJLaf.install();
					Theme.isLight = true;
					dispose();
					new Main().setVisible(true);
				} else {
					rdoMniSang.setSelected(true);
				}
			}
		});

		rdoMniTiengViet.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				Language.isVi = e.getStateChange() == ItemEvent.SELECTED ? true : false;
				dispose();
				new Main().setVisible(true);
			}
		});

		rdoMniSang.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					FlatIntelliJLaf.install();
					Theme.isLight = true;
				} else {
					FlatDarkFlatIJTheme.install();
					Theme.isLight = false;
				}
				dispose();
				new Main().setVisible(true);
			}
		});
	}

	private void getThemePack() {

		s1.setForeground(Theme.getColor("Separator.foreground"));
		s2.setForeground(Theme.getColor("Separator.foreground"));
		s3.setForeground(Theme.getColor("Separator.foreground"));
		panelNavigationBar.setBackground(Theme.getColor("Main.navbar.background"));
		UIManager.put("ToggleButton.selectedBackground",
				new ColorUIResource(Theme.getColor("Main.ToggleButton.selected.background")));
		UIManager.put("ToggleButton.selectedForeground", Theme.getColor("Main.ToggleButton.selected.foreground"));
		SwingUtilities.updateComponentTreeUI(btnHome);
		btnHome.setBackground(Theme.getColor("Main.ToggleButton.background"));
		btnBanHang.setBackground(Theme.getColor("Main.ToggleButton.background"));
		btnThongKe.setBackground(Theme.getColor("Main.ToggleButton.background"));
		btnHome.setForeground(Theme.getColor("Main.ToggleButton.foreground"));
		btnBanHang.setForeground(Theme.getColor("Main.ToggleButton.foreground"));
		btnThongKe.setForeground(Theme.getColor("Main.ToggleButton.foreground"));
		btnDangXuat.setBackground(Theme.getColor("Main.LogoutButton.background"));
		panelStatusBar.setBackground(Theme.getColor("Main.statusBar.background"));
		rdoMniToi.setSelected(Theme.isLight ? false : true);
	}

	private void getLanguagePack() {
		ResourceBundle pack = Language.getLanguagePack();

		setTitle(pack.getString("appTitle"));

		mnuHeThong.setText(pack.getString("mnuHeThong"));
		mniQLTK.setText(pack.getString("mniQLTK"));
		mniDoiMatKhau.setText(pack.getString("mniDoiMatKhau"));
		mniDangXuat.setText(pack.getString("mniDangXuat"));
		mniThoat.setText(pack.getString("mniThoat"));
		mnuDanhMuc.setText(pack.getString("mnuDanhMuc"));
		mnuHangHoa.setText(pack.getString("mnuHangHoa"));
		mniMatHang.setText(pack.getString("mniMatHang"));
		mniKhoHang.setText(pack.getString("mniKhoHang"));
		mniKhachHang.setText(pack.getString("mniKhachHang"));
		mniNhaCungCap.setText(pack.getString("mniNhaCungCap"));
		mniKhuyenMai.setText(pack.getString("mniKhuyenMai"));
		mnuThongKe.setText(pack.getString("mnuThongKe"));
		mniTKDoanhSo.setText(pack.getString("mniTKDoanhSo"));
		mniTKHangHoa.setText(pack.getString("mniTKHangHoa"));
		mniTKThuChi.setText(pack.getString("mniTKThuChi"));
		mniTKTonKho.setText(pack.getString("mniTKTonKho"));
		mniTKLoiNhuan.setText(pack.getString("mniTKLoiNhuan"));
		mniTKBCThang.setText(pack.getString("mniTKBCThang"));
		mnuTienIch.setText(pack.getString("mnuTienIch"));
		mnuGiaoDien.setText(pack.getString("mnuGiaoDien"));
		rdoMniSang.setText(pack.getString("rdoMniSang"));
		rdoMniToi.setText(pack.getString("rdoMniToi"));
		mnuNgonNgu.setText(pack.getString("mnuNgonNgu"));
		rdoMniTiengAnh.setText(pack.getString("rdoMniTiengAnh"));
		rdoMniTiengViet.setText(pack.getString("rdoMniTiengViet"));
		mnuTroGiup.setText(pack.getString("mnuTroGiup"));
		mniHuongDan.setText(pack.getString("mniHuongDan"));
		mniThongTin.setText(pack.getString("mniThongTin"));

		btnHome.setText(pack.getString("btnHome"));
		btnBanHang.setText(pack.getString("btnBanHang"));
		btnThongKe.setText(pack.getString("btnThongKe"));
		btnDangXuat.setText(pack.getString("btnDangXuat"));

		rdoMniTiengAnh.setSelected(Language.isVi ? false : true);
	}

	private void showDateTime() {
		SimpleDateFormat sdf;
		if (Language.isVi) {
			DateFormatSymbols weekdaysSymbols = new DateFormatSymbols(new Locale("vi"));
			weekdaysSymbols.setWeekdays(
					new String[] { "", "Chủ Nhật", "Thứ Hai", "Thứ Ba", "Thứ Tư", "Thứ Năm", "Thứ Sáu", "Thứ Bảy" });
			sdf = new SimpleDateFormat("hh:mm:ss a  EEEEE dd/MM/yyyy", weekdaysSymbols);
		} else {
			sdf = new SimpleDateFormat("HH:mm:ss  E MM-dd-yyyy");
		}
		new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lblDateTime.setText(sdf.format(new Date()));
			}
		}).start();
	}

	public void GotoHome() {
		panelContent.removeAll();
		panelContent.repaint();
		panelContent.validate();
		panelContent.add(new GiaoDienTongQuan(), BorderLayout.CENTER);
	}

	public void GoToPOS() {
		panelContent.removeAll();
		panelContent.repaint();
		panelContent.validate();
		panelContent.add(new GiaoDienBanHang(), BorderLayout.CENTER);
		GiaoDienBanHang.clearList();
	}

	public void GotoStatistics() {
		panelContent.removeAll();
		panelContent.repaint();
		panelContent.validate();
		panelContent.add(new MenuThongKe(), BorderLayout.CENTER);
	}

	private void LogOut() {
		if (MessageBox.confirm(Language.getMessagePack().getString("logoutConfirm")) == false)
			return;
		this.dispose();
		new Login().setVisible(true);
	}

	public static void main(String[] args) {
		WindowProperties.setFlatLaf_LookAndFeel();
		setDefaultLookAndFeelDecorated(true);
		new Splash().loading();
	}

}
