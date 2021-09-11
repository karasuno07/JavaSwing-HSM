package windows;

import javax.swing.*;

import com.formdev.flatlaf.FlatLaf;

import java.awt.*;
import java.awt.event.*;
import java.util.ResourceBundle;

import utils.Authorizer;
import utils.ImageHandler;
import utils.Language;
import utils.Theme;
import utils.Validator;
import utils.WindowProperties;

public class Login extends JFrame {

	private JButton btnSignIn;
	public static JCheckBox chkSave;
	private JLabel lblForget;
	private JLabel lblIcon; 
	private JLabel lblPassword;
	private JLabel lblUsername;
	private JPanel pnlContainer;
	private JPasswordField txtPassword;
	private JTextField txtUsername;

	ResourceBundle languagePack = Language.getLanguagePack();
	ResourceBundle themePack = Theme.getThemePack();

	public Login() {
		initComponents();
		WindowProperties.centeringWindow(this);
		WindowProperties.setApplicationIcon(this);

	}

	private void initComponents() {
		pnlContainer = new JPanel();
		lblIcon = new JLabel();
		lblUsername = new JLabel();
		txtUsername = new JTextField();
		lblPassword = new JLabel();
		txtPassword = new JPasswordField();
		chkSave = new JCheckBox();
		lblForget = new JLabel();
		btnSignIn = new JButton();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		setPreferredSize(new Dimension(446, 350));
		setResizable(false);
		
		pnlContainer.setBackground(Color.decode(themePack.getString("Login.background")));
		
		setTitle(languagePack.getString("loginTitle"));
		lblUsername.setText(languagePack.getString("username"));
		lblPassword.setText(languagePack.getString("password"));
		chkSave.setText(languagePack.getString("savelogin"));
		btnSignIn.setText(languagePack.getString("loginbtn"));

		
		lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
		lblIcon.setIcon(ImageHandler.imageMaker("resources/icons/user.png", 64, 64));

		lblIcon.setHorizontalTextPosition(SwingConstants.CENTER);

		lblUsername.setFont(new Font("Tahoma", 1, 13));

		lblPassword.setFont(new Font("Tahoma", 1, 13));
		
		txtUsername.setBackground(Theme.getColor("TextField.background"));
		txtUsername.setForeground(Theme.getColor("TextField.foreground"));
		
		txtPassword.setBackground(Theme.getColor("TextField.background"));
		txtPassword.setForeground(Theme.getColor("TextField.foreground"));

		chkSave.setBackground(Color.decode(themePack.getString("Login.background")));

		btnSignIn.setBackground(Color.decode(themePack.getString("Login.Button.background")));
		btnSignIn.setFont(new Font("Tahoma", 1, 12));

		GroupLayout pnlContainerLayout = new GroupLayout(pnlContainer);
		pnlContainer.setLayout(pnlContainerLayout);
		pnlContainerLayout.setHorizontalGroup(pnlContainerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlContainerLayout.createSequentialGroup().addGap(30, 30, 30)
						.addGroup(pnlContainerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(lblUsername, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblPassword, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
						.addGap(30, 30, 30)
						.addGroup(pnlContainerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addGroup(pnlContainerLayout.createSequentialGroup()
										.addComponent(btnSignIn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addGap(124, 124, 124))
								.addGroup(GroupLayout.Alignment.TRAILING,
										pnlContainerLayout.createSequentialGroup().addComponent(chkSave)
												.addGap(60, 60, 60).addComponent(lblForget, GroupLayout.DEFAULT_SIZE,
														GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addComponent(txtUsername).addComponent(txtPassword))
						.addGap(30, 30, 30))
				.addGroup(GroupLayout.Alignment.TRAILING,
						pnlContainerLayout.createSequentialGroup().addGap(180, 180, 180)
								.addComponent(lblIcon, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
								.addGap(180, 180, 180)));
		pnlContainerLayout.setVerticalGroup(pnlContainerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(pnlContainerLayout.createSequentialGroup().addGap(30, 30, 30)
						.addComponent(lblIcon, GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE).addGap(18, 18, 18)
						.addGroup(pnlContainerLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(lblUsername, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtUsername, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE))
						.addGap(20, 20, 20)
						.addGroup(pnlContainerLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(txtPassword, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblPassword, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(pnlContainerLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(lblForget, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
								.addComponent(chkSave, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
						.addGap(20, 20, 20)
						.addComponent(btnSignIn, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
						.addGap(30, 30, 30)));

		GroupLayout layout = new GroupLayout(getContentPane());

		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(pnlContainer,
				GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(pnlContainer,
				GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

		pack();

		txtUsername.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					txtPassword.requestFocus();
				}
			}
		});

		txtPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					login();
				}

			}
		});

		btnSignIn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});

		Authorizer.checkLoginInfo(txtUsername, txtPassword, chkSave, btnSignIn);
	}

	private void login() {
		String username = txtUsername.getText();
		String password = String.valueOf(txtPassword.getPassword());
		if (Validator.isNotEmpty(txtUsername, Language.getMessagePack().getString("emptyUsername")) == false
				|| Validator.isNotEmpty(txtPassword, Language.getMessagePack().getString("emptyPassword")) == false) {
			System.out.println(false);
			return;
		}
		Authorizer.isLogin(username.toLowerCase(), password, this);
		if (chkSave.isSelected()) {
			Authorizer.saveLoginInfo(txtUsername, txtPassword);
		} else {
			Authorizer.deleteLoginInfo();
		}
	}

}
