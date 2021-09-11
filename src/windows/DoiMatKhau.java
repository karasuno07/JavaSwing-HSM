package windows;

import java.awt.*;
import java.awt.event.*;
import java.util.ResourceBundle;

import javax.swing.*;

import dao.NhanVienDAO;
import entities.NhanVien;
import utils.Authorizer;
import utils.Language;
import utils.MessageBox;
import utils.Theme;
import utils.Validator;
import utils.WindowProperties;

public class DoiMatKhau extends JDialog {

	private JButton btnOK;
	private JLabel lblConfirmPassword;
	private JLabel lblHeader;
	private JLabel lblNewPassword;
	private JLabel lblPassword;
	private JLabel lblUsername;
	private JPanel pnlMain;
	private JPasswordField txtConfirmPassword;
	private JPasswordField txtNewPassword;
	private JPasswordField txtPassword;
	private JTextField txtUsername;
	private JTextField password;
	ResourceBundle languagePack = Language.getLanguagePack();

	public DoiMatKhau(Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
		Customize();
	}

	private void Customize() {
		WindowProperties.setApplicationIcon(this);
		WindowProperties.centeringWindow(this);
		txtUsername.setText(Authorizer.user.getMaNV());
		// event
		btnOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String old = String.valueOf(txtPassword.getPassword());
				String password = String.valueOf(txtNewPassword.getPassword());
				String confirm = String.valueOf(txtConfirmPassword.getPassword());
				Authorizer.changePassword(txtUsername.getText(), old, password, confirm, getOwner());
			}
		});
		final JPopupMenu popupMenu1 = new JPopupMenu();
		JMenuItem show1 = new JMenuItem(languagePack.getString("showPassword"));
		show1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showPasswordField(txtPassword, show1);
			}
		});
		popupMenu1.add(show1);
		txtPassword.setComponentPopupMenu(popupMenu1);
		final JPopupMenu popupMenu2 = new JPopupMenu();
		JMenuItem show2 = new JMenuItem(languagePack.getString("showPassword"));
		show2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showPasswordField(txtNewPassword, show2);
			}
		});
		popupMenu2.add(show2);
		txtNewPassword.setComponentPopupMenu(popupMenu2);
		final JPopupMenu popupMenu3 = new JPopupMenu();
		JMenuItem show3 = new JMenuItem(languagePack.getString("showPassword"));
		show3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showPasswordField(txtConfirmPassword, show3);
			}
		});
		popupMenu3.add(show3);
		txtConfirmPassword.setComponentPopupMenu(popupMenu3);
	}

	private void showPasswordField(JPasswordField field, JMenuItem item) {
		if (item.getText().equals(languagePack.getString("showPassword"))) {
			item.setText(languagePack.getString("hidePassword"));
			field.setEchoChar((char) 0);
		} else {
			item.setText(languagePack.getString("showPassword"));
			field.setEchoChar('●');
		}
	}

	private void initComponents() {

		pnlMain = new JPanel();
		lblHeader = new JLabel();
		lblUsername = new JLabel();
		txtUsername = new JTextField();
		lblPassword = new JLabel();
		txtPassword = new JPasswordField();
		lblNewPassword = new JLabel();
		txtNewPassword = new JPasswordField();
		lblConfirmPassword = new JLabel();
		txtConfirmPassword = new JPasswordField();
		btnOK = new JButton();
		password  = new JPasswordField();
		password.setVisible(false);

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(languagePack.getString("changePasswordTitle"));
		setResizable(false);

		pnlMain.setBackground(Theme.getColor("Login.background"));
		
		txtUsername.setBackground(Theme.getColor("TextField.disableBackground"));
		txtUsername.setForeground(Theme.getColor("TextField.foreground"));
		txtPassword.setBackground(Theme.getColor("TextField.background"));
		txtPassword.setForeground(Theme.getColor("TextField.foreground"));
		txtNewPassword.setBackground(Theme.getColor("TextField.background"));
		txtNewPassword.setForeground(Theme.getColor("TextField.foreground"));
		txtConfirmPassword.setBackground(Theme.getColor("TextField.background"));
		txtConfirmPassword.setForeground(Theme.getColor("TextField.foreground"));
		btnOK.setBackground(Theme.getColor("DMK.btnOK.background"));
		btnOK.setForeground(Theme.getColor("DMK.btnOK.foreground"));

		lblHeader.setBackground(new Color(102, 153, 255));
		lblHeader.setFont(new Font("Tahoma", 1, 14)); // NOI18N
		lblHeader.setForeground(new Color(255, 0, 51));
		lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeader.setText(languagePack.getString("changePasswordHeader"));
		lblHeader.setHorizontalTextPosition(SwingConstants.CENTER);
		lblHeader.setPreferredSize(new Dimension(33, 18));

		lblUsername.setFont(new Font("Tahoma", 0, 12)); // NOI18N
		lblUsername.setText(languagePack.getString("username"));
		lblUsername.setPreferredSize(new Dimension(100, 24));

		txtUsername.setEditable(false);
		txtUsername.setPreferredSize(new Dimension(226, 24));

		lblPassword.setFont(new Font("Tahoma", 0, 12)); // NOI18N
		lblPassword.setText(languagePack.getString("password"));
		lblPassword.setPreferredSize(new Dimension(100, 24));

		txtPassword.setPreferredSize(new Dimension(226, 24));

		lblNewPassword.setFont(new Font("Tahoma", 0, 12)); // NOI18N
		lblNewPassword.setText(languagePack.getString("newPassword"));
		lblNewPassword.setPreferredSize(new Dimension(100, 24));

		txtNewPassword.setPreferredSize(new Dimension(226, 24));

		lblConfirmPassword.setFont(new Font("Tahoma", 0, 12)); // NOI18N
		lblConfirmPassword.setText(languagePack.getString("confirmPassword"));
		lblConfirmPassword.setPreferredSize(new Dimension(100, 24));

		txtConfirmPassword.setPreferredSize(new Dimension(226, 24));

		btnOK.setFont(new Font("Tahoma", 1, 12)); // NOI18N
		btnOK.setText(languagePack.getString("btnOK"));
		btnOK.setBorderPainted(false);
		btnOK.setPreferredSize(new Dimension(100, 40));

		GroupLayout pnlMainLayout = new GroupLayout(pnlMain);
		pnlMain.setLayout(pnlMainLayout);
		pnlMainLayout.setHorizontalGroup(pnlMainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlMainLayout.createSequentialGroup().addGap(30, 30, 30).addGroup(pnlMainLayout
						.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addGroup(pnlMainLayout.createSequentialGroup()
								.addComponent(lblNewPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(txtNewPassword,
										GroupLayout.PREFERRED_SIZE, 226, GroupLayout.PREFERRED_SIZE))
						.addGroup(pnlMainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addGroup(pnlMainLayout.createSequentialGroup()
										.addComponent(lblPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(txtPassword, GroupLayout.PREFERRED_SIZE, 226,
												GroupLayout.PREFERRED_SIZE))
								.addGroup(pnlMainLayout.createSequentialGroup()
										.addComponent(lblUsername, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(txtUsername, GroupLayout.PREFERRED_SIZE, 226,
												GroupLayout.PREFERRED_SIZE)))
						.addGroup(pnlMainLayout.createSequentialGroup()
								.addComponent(lblConfirmPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
								.addGroup(pnlMainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(txtConfirmPassword, GroupLayout.PREFERRED_SIZE, 226,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(btnOK, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))))
						.addGap(30, 30, 30))
				.addComponent(lblHeader, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE,
						GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		pnlMainLayout.setVerticalGroup(pnlMainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlMainLayout.createSequentialGroup().addGap(15, 15, 15)
						.addComponent(lblHeader, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addGap(18, 18, 18)
						.addGroup(pnlMainLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(lblUsername, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(txtUsername, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(pnlMainLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(lblPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(txtPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(pnlMainLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(lblNewPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(txtNewPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(pnlMainLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(lblConfirmPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(txtConfirmPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(btnOK,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGap(15, 15, 15)));

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(pnlMain,
				GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(pnlMain,
				GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

		pack();
	}
}
