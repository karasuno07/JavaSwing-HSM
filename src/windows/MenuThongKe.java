package windows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import utils.Language;
import utils.WindowProperties;

public class MenuThongKe extends JPanel {
	
	private JButton btnBCThang;
    private JButton btnDoanhSo;
    private JButton btnHangHoa;
    private JButton btnKhoHang;
    private JButton btnLoiNhuan;
    private JButton btnThuChi;
	ResourceBundle languagePack = Language.getLanguagePack();
    
	public MenuThongKe() {
		btnDoanhSo = new JButton();
        btnThuChi = new JButton();
        btnLoiNhuan = new JButton();
        btnHangHoa = new JButton();
        btnKhoHang = new JButton();
        btnBCThang = new JButton();

        setBackground(new Color(221, 221, 221));

        btnDoanhSo.setBackground(new Color(234, 153, 153));
        btnDoanhSo.setFont(new Font("Consolas", 1, 36)); 
        btnDoanhSo.setForeground(new Color(248, 248, 248));
        btnDoanhSo.setText(languagePack.getString("btnDoanhSo"));
        btnDoanhSo.setBorderPainted(false);
        btnDoanhSo.setFocusable(false);
        btnDoanhSo.setHorizontalTextPosition(SwingConstants.CENTER);

        btnThuChi.setBackground(new Color(230, 145, 56));
        btnThuChi.setFont(new Font("Consolas", 1, 36)); 
        btnThuChi.setForeground(new Color(248, 248, 248));
        btnThuChi.setText(languagePack.getString("btnThuChi"));
        btnThuChi.setBorderPainted(false);
        btnThuChi.setFocusable(false);
        btnThuChi.setHorizontalTextPosition(SwingConstants.CENTER);

        btnLoiNhuan.setBackground(new Color(153, 0, 255));
        btnLoiNhuan.setFont(new Font("Consolas", 1, 36));
        btnLoiNhuan.setForeground(new Color(248, 248, 248));
        btnLoiNhuan.setText(languagePack.getString("btnLoiNhuan"));
        btnLoiNhuan.setBorderPainted(false);
        btnLoiNhuan.setFocusable(false);
        btnLoiNhuan.setHorizontalTextPosition(SwingConstants.CENTER);

        btnHangHoa.setBackground(new Color(249, 203, 156));
        btnHangHoa.setFont(new Font("Consolas", 1, 36));
        btnHangHoa.setForeground(new Color(248, 248, 248));
        btnHangHoa.setText(languagePack.getString("btnHangHoa"));
        btnHangHoa.setBorderPainted(false);
        btnHangHoa.setFocusable(false);
        btnHangHoa.setHorizontalTextPosition(SwingConstants.CENTER);

        btnKhoHang.setBackground(new Color(111, 168, 220));
        btnKhoHang.setFont(new Font("Consolas", 1, 36));
        btnKhoHang.setForeground(new Color(248, 248, 248));
        btnKhoHang.setText(languagePack.getString("btnKhoHang"));
        btnKhoHang.setBorderPainted(false);
        btnKhoHang.setFocusable(false);
        btnKhoHang.setHorizontalTextPosition(SwingConstants.CENTER);

        btnBCThang.setBackground(new Color(153, 153, 153));
        btnBCThang.setFont(new Font("Consolas", 1, 36));
        btnBCThang.setForeground(new Color(248, 248, 248));
        btnBCThang.setText(languagePack.getString("btnBCThang"));
        btnBCThang.setBorderPainted(false);
        btnBCThang.setFocusable(false);
        btnBCThang.setHorizontalTextPosition(SwingConstants.CENTER);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(btnLoiNhuan, GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                    .addComponent(btnThuChi, GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                    .addComponent(btnDoanhSo, GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(btnBCThang, GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                    .addComponent(btnKhoHang, GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                    .addComponent(btnHangHoa, GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDoanhSo, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHangHoa, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThuChi, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnKhoHang, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(btnBCThang, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLoiNhuan, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        
        btnDoanhSo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new TKDS(null, true).setVisible(true);
			}
		});
        btnHangHoa.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new TKHH(null, true).setVisible(true);
			}
		});
        btnLoiNhuan.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new TKLN(null, true).setVisible(true);
			}
		});
        btnThuChi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new TKTC(null, true).setVisible(true);
			}
		});
        btnKhoHang.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new TKKH(null, true).setVisible(true);
			}
		});
        btnBCThang.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new TK_BCT(null, true).setVisible(true);
			}
		});
	}
}
