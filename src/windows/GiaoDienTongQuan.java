package windows;


import javax.swing.*;
import javax.swing.text.*;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import utils.ImageHandler;
import utils.JDBCHelper;
import utils.Language;
import utils.Theme;

public class GiaoDienTongQuan extends JPanel {

	private JComboBox<String> cboPickDay;
	private JLabel iconGhiChu;
	private JLabel iconKho;
	private JLabel iconKhuyenMai;
	private JLabel iconTien;
	private JLabel lblChiPhi;
	private JLabel lblGhiChu;
	private JLabel lblSLTonKho;
	private JLabel lblHangHet;
	private JLabel lblHangCon;
	private JLabel lblKho;
	private JLabel lblKhuyenMai;
	private JLabel lblSeparator;
	private JLabel lblTien;
	private JLabel lblTienThu;
	private JLabel lblTong;
	private JPanel pnlGhiChu;
	private JPanel pnlTienBac;
	private JPanel pnlTinhTrangKho;
	private JPanel pnlKhuyenMai;
	private JScrollPane scrollGhiChu;
	private JScrollPane scrollKhuyenMai;
	private JTextPane txtGhiChu;
	private JTextPane txtKhuyenMai;
	
	File note = new File("resources/tempData/notes.txt");
	ResourceBundle pack = Language.getLanguagePack();

	public GiaoDienTongQuan() {
		pnlTinhTrangKho = new JPanel();
		lblKho = new JLabel();
		iconKho = new JLabel();
		lblSLTonKho = new JLabel();
		lblHangHet = new JLabel();
		lblHangCon = new JLabel();
		pnlTienBac = new JPanel();
		lblTien = new JLabel();
		cboPickDay = new JComboBox<>();
		iconTien = new JLabel();
		lblTienThu = new JLabel();
		lblChiPhi = new JLabel();
		lblSeparator = new JLabel();
		lblTong = new JLabel();
		pnlGhiChu = new JPanel();
		lblGhiChu = new JLabel();
		iconGhiChu = new JLabel();
		scrollGhiChu = new JScrollPane();
		txtGhiChu = new JTextPane();
		pnlKhuyenMai = new JPanel();
		lblKhuyenMai = new JLabel();
		iconKhuyenMai = new JLabel();
		scrollKhuyenMai = new JScrollPane();
		txtKhuyenMai = new JTextPane();

		pnlTinhTrangKho.setBorder(BorderFactory.createLineBorder(new Color(153, 153, 153), 2));

		lblKho.setFont(new Font("Tahoma", 1, 14)); // NOI18N
		lblKho.setForeground(new Color(89, 126, 170));

		iconKho.setHorizontalAlignment(SwingConstants.CENTER);

		lblSLTonKho.setFont(new Font("Tahoma", 0, 14));


		lblHangHet.setFont(new Font("Tahoma", 0, 14));

		lblHangCon.setFont(new Font("Tahoma", 0, 14));

		GroupLayout pnlTinhTrangKhoLayout = new GroupLayout(pnlTinhTrangKho);
		pnlTinhTrangKho.setLayout(pnlTinhTrangKhoLayout);
		pnlTinhTrangKhoLayout.setHorizontalGroup(pnlTinhTrangKhoLayout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlTinhTrangKhoLayout.createSequentialGroup().addContainerGap().addGroup(pnlTinhTrangKhoLayout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(pnlTinhTrangKhoLayout.createSequentialGroup()
								.addComponent(lblKho, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(iconKho, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
						.addGroup(pnlTinhTrangKhoLayout.createSequentialGroup().addGroup(pnlTinhTrangKhoLayout
								.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(lblSLTonKho, GroupLayout.PREFERRED_SIZE, 220, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblHangHet, GroupLayout.PREFERRED_SIZE, 220, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblHangCon, GroupLayout.PREFERRED_SIZE, 220, GroupLayout.PREFERRED_SIZE))
								.addGap(0, 8, Short.MAX_VALUE)))
						.addContainerGap()));
		pnlTinhTrangKhoLayout.setVerticalGroup(pnlTinhTrangKhoLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlTinhTrangKhoLayout.createSequentialGroup().addContainerGap()
						.addGroup(pnlTinhTrangKhoLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
								.addComponent(lblKho, GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE).addComponent(
										iconKho, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGap(26, 26, 26).addComponent(lblSLTonKho)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(lblHangHet)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(lblHangCon)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		pnlTienBac.setBorder(BorderFactory.createLineBorder(new Color(153, 153, 153), 2));

		lblTien.setFont(new Font("Tahoma", 1, 14));
		lblTien.setForeground(new Color(89, 126, 170));


		iconTien.setForeground(new Color(51, 153, 255));
		iconTien.setHorizontalAlignment(SwingConstants.CENTER);

		lblTienThu.setFont(new Font("Tahoma", 0, 14));

		lblChiPhi.setFont(new Font("Tahoma", 0, 14));

		lblSeparator.setFont(new Font("Tahoma", 0, 14));
		lblSeparator.setText("==========================");

		lblTong.setFont(new Font("Tahoma", 1, 14));

		GroupLayout pnlTienBacLayout = new GroupLayout(pnlTienBac);
		pnlTienBac.setLayout(pnlTienBacLayout);
		pnlTienBacLayout
				.setHorizontalGroup(pnlTienBacLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(pnlTienBacLayout.createSequentialGroup().addContainerGap().addGroup(pnlTienBacLayout
								.createParallelGroup(
										GroupLayout.Alignment.LEADING)
								.addGroup(pnlTienBacLayout.createSequentialGroup()
										.addComponent(lblTien, GroupLayout.PREFERRED_SIZE, 70,
												GroupLayout.PREFERRED_SIZE)
										.addGap(18, 26, Short.MAX_VALUE)
										.addComponent(cboPickDay, GroupLayout.PREFERRED_SIZE, 150,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(
												iconTien, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
								.addGroup(pnlTienBacLayout.createSequentialGroup().addGroup(pnlTienBacLayout
										.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
										.addComponent(lblSeparator, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(lblChiPhi, GroupLayout.Alignment.LEADING,
												GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(lblTienThu, GroupLayout.Alignment.LEADING,
												GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
										.addGap(0, 0, Short.MAX_VALUE))
								.addComponent(lblTong, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE))
								.addContainerGap()));
		pnlTienBacLayout.setVerticalGroup(pnlTienBacLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlTienBacLayout.createSequentialGroup().addContainerGap()
						.addGroup(pnlTienBacLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(lblTien, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 25,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(iconTien, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
								.addComponent(cboPickDay, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE,
										27, GroupLayout.PREFERRED_SIZE))
						.addGap(26, 26, 26).addComponent(lblTienThu)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(lblChiPhi)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(lblSeparator)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(lblTong)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		pnlGhiChu.setBorder(BorderFactory.createLineBorder(new Color(153, 153, 153), 2));

		lblGhiChu.setFont(new Font("Tahoma", 1, 14));
		lblGhiChu.setForeground(new Color(89, 126, 170));

		iconGhiChu.setForeground(new Color(51, 153, 255));
		iconGhiChu.setHorizontalAlignment(SwingConstants.CENTER);

		txtGhiChu.setFont(new Font("Tahoma", 0, 14));
		scrollGhiChu.setViewportView(txtGhiChu);

		GroupLayout pnlGhiChuLayout = new GroupLayout(pnlGhiChu);
		pnlGhiChu.setLayout(pnlGhiChuLayout);
		pnlGhiChuLayout.setHorizontalGroup(pnlGhiChuLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlGhiChuLayout.createSequentialGroup().addContainerGap().addGroup(pnlGhiChuLayout
						.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(scrollGhiChu)
						.addGroup(pnlGhiChuLayout.createSequentialGroup().addComponent(lblGhiChu)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 133, Short.MAX_VALUE)
								.addComponent(iconGhiChu, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)))
						.addContainerGap()));
		pnlGhiChuLayout.setVerticalGroup(pnlGhiChuLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlGhiChuLayout.createSequentialGroup().addContainerGap()
						.addGroup(pnlGhiChuLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
								.addComponent(lblGhiChu, GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
								.addComponent(iconGhiChu, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(scrollGhiChu)
						.addContainerGap()));

		pnlKhuyenMai.setBorder(BorderFactory.createLineBorder(new Color(153, 153, 153), 2));

		lblKhuyenMai.setFont(new Font("Tahoma", 1, 14));
		lblKhuyenMai.setForeground(new Color(89, 126, 170));

		iconKhuyenMai.setForeground(new Color(51, 153, 255));
		iconKhuyenMai.setHorizontalAlignment(SwingConstants.CENTER);

		txtKhuyenMai.setEditable(false);
		txtKhuyenMai.setFocusable(false);
		txtKhuyenMai.setFont(new Font("Tahoma", 0, 14));
		scrollKhuyenMai.setViewportView(txtKhuyenMai);

		GroupLayout pnlKhuyenMaiLayout = new GroupLayout(pnlKhuyenMai);
		pnlKhuyenMai.setLayout(pnlKhuyenMaiLayout);
		pnlKhuyenMaiLayout.setHorizontalGroup(pnlKhuyenMaiLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlKhuyenMaiLayout.createSequentialGroup().addContainerGap()
						.addGroup(pnlKhuyenMaiLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(scrollKhuyenMai)
								.addGroup(pnlKhuyenMaiLayout.createSequentialGroup().addComponent(lblKhuyenMai)
										.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(iconKhuyenMai, GroupLayout.PREFERRED_SIZE, 50,
												GroupLayout.PREFERRED_SIZE)))
						.addContainerGap()));
		pnlKhuyenMaiLayout.setVerticalGroup(pnlKhuyenMaiLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlKhuyenMaiLayout.createSequentialGroup().addContainerGap()
						.addGroup(pnlKhuyenMaiLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(iconKhuyenMai, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblKhuyenMai, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(scrollKhuyenMai, GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
						.addContainerGap()));

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addContainerGap()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addComponent(pnlTinhTrangKho, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(pnlTienBac,
										GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addComponent(pnlKhuyenMai, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
				.addComponent(pnlGhiChu, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addContainerGap()
				.addGroup(layout
						.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
										.addComponent(pnlTinhTrangKho, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(pnlTienBac, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE))
								.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(pnlKhuyenMai,
										GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addComponent(pnlGhiChu, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addContainerGap()));
		// set theme pack
		getThemePack();
		// set language pack
		getLanguagePack();
		// set icon for panels
		iconKho.setIcon(ImageHandler.imageMaker("resources/icons/cube.png", 28, 28));
		iconTien.setIcon(ImageHandler.imageMaker("resources/icons/money.png", 28, 28));
		iconGhiChu.setIcon(ImageHandler.imageMaker("resources/icons/notes.png", 28, 28));
		iconKhuyenMai.setIcon(ImageHandler.imageMaker("resources/icons/ads.png", 40, 35));
		//
		UpdateStatus();
	}
	
	private void getThemePack() {
		setBackground(Theme.getColor("GDTQ.background"));
	}
	
	private void getLanguagePack() {
		lblKho.setText(pack.getString("lblKho"));
		lblSLTonKho.setText(pack.getString("lblSLTonKho"));
		lblHangHet.setText(pack.getString("lblHangHet"));
		lblHangCon.setText(pack.getString("lblHangCon"));
		
		lblTien.setText(pack.getString("lblTien"));
		lblTienThu.setText(pack.getString("lblTienThu"));
		lblChiPhi.setText(pack.getString("lblChiPhi"));
		lblTong.setText(pack.getString("lblTong"));
		
		String[] cboItems =  { pack.getString("today"), pack.getString("yesterday"), pack.getString("7recentdays"), pack.getString("30recentdays") };
		for (String item : cboItems) {
			cboPickDay.addItem(item);
		}
		
		lblGhiChu.setText(pack.getString("lblGhiChu"));
		
		lblKhuyenMai.setText(pack.getString("lblKhuyenMai"));
	}
 
	private void UpdateStatus() {
		TinhTrangKho();
		// TienBac
		TienBac(1);
		cboPickDay.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				int selectedIndex = cboPickDay.getSelectedIndex();
				switch (selectedIndex) {
				case 0:
					TienBac(1);
					break;
				case 1:
					TienBac(2);
					break;
				case 2:
					TienBac(7);
					break;
				case 3:
					TienBac(30);
					break;
				default:
					TienBac(1);
					break;
				}

			}
		});
		// GhiChu
		GhiChu();
		// KhuyenMai
		KhuyenMai();
	}

	private void TinhTrangKho() {
		String SQL = " SELECT SUM(SoLuong) as SoLuongTonKho,"
				+ "		(SELECT COUNT(*) FROM KhoHang WHERE TinhTrangHang = N'Hết hàng') AS HetHang,"
				+ "		(SELECT COUNT(*) FROM KhoHang WHERE TinhTrangHang = N'Còn hàng') AS ConHang" + "  FROM KhoHang";
		try {
			ResultSet rs = JDBCHelper.query(SQL);
			if (rs.next()) {
				lblSLTonKho.setText(lblSLTonKho.getText() + rs.getInt(1));
				lblHangHet.setText(lblHangHet.getText() + rs.getInt(2));
				lblHangCon.setText(lblHangCon.getText() + rs.getInt(3));
			}
			rs.getStatement().getConnection().close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void TienBac(int dayGap) {
		String SQL = "SELECT * FROM fn_TongQuanThuChi(?)";
		try {
			ResultSet rs = JDBCHelper.query(SQL, dayGap);
			if (rs.next()) {
				long Thu = rs.getLong(1), Chi = rs.getLong(2);
				lblTienThu.setText(pack.getString("lblTienThu") + Thu);
				lblChiPhi.setText(pack.getString("lblChiPhi") + Chi);
				long Tong = Thu - Chi;
				lblTong.setForeground(Tong >= 0 ? new Color(56, 118, 29) : Color.red);
				lblTong.setText(pack.getString("lblTong") + Tong);
			}
			rs.getStatement().getConnection().close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void GhiChu() {
		ReadTextGhiChu();
		txtGhiChu.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				SaveTextGhiChu();
			}
		});
	}
	
	private void ReadTextGhiChu() {
		if (!note.exists()) return;
		try {
			FileReader fr = new FileReader(note);
			BufferedReader br = new BufferedReader(fr);
			StringBuilder lines = new StringBuilder("");
			String line;
			while ((line = br.readLine()) != null) {
				lines.append(line + "\n");
			}
			txtGhiChu.setText(lines.toString());
			br.close();
			fr.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void SaveTextGhiChu() {
		if (note.exists()) note.delete();
		try {
			FileWriter fw = new FileWriter(note);
			String[] lines = txtGhiChu.getText().split("\n");
			for (String line : lines) {
				System.out.println(line);
				fw.write(line + "\n");
			}
			fw.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	private void KhuyenMai() {
		String SQL = "SELECT TenKM, ChietKhau, GhiChu FROM KhuyenMai WHERE DATEDIFF(DAY, GETDATE(), NgayKetThuc) >= 0";
		SimpleAttributeSet title = new SimpleAttributeSet();
		StyleConstants.setFontSize(title, 15);
		StyleConstants.setBold(title, true);
		StyleConstants.setForeground(title, Color.RED);
		StyledDocument doc = txtKhuyenMai.getStyledDocument();
		try {
			ResultSet rs = JDBCHelper.query(SQL);
			while (rs.next()) {
				doc.insertString(doc.getLength(), rs.getString(1) + "\n", title);
				doc.insertString(doc.getLength(), pack.getString("promotion") + rs.getString(2) + "\n", null);
				if (rs.getString(3).length() > 0) {
					doc.insertString(doc.getLength(), pack.getString("promotionDetails") + rs.getString(3) + "\n", null);
				}
				doc.insertString(doc.getLength(), "======================================================\n", null);
			}
			rs.getStatement().getConnection().close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
