package utils;

import java.awt.Window;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import entities.NhanVien;
import windows.Main;

public class Authorizer {

	public static NhanVien user = new NhanVien();
	private static String url = "resources/tempData/loginInfo.txt";
	private static File loginInfo = new File(url);

	public static boolean isLogin(String username, String password, Window window) {
		String LOGIN_SQL = "SELECT * FROM NhanVien WHERE MaNV=? AND MatKhau=?";
		try {
			ResultSet rs = JDBCHelper.query(LOGIN_SQL, username, password);
			if (rs.next()) {
				MessageBox.notif(Language.getMessagePack().getString("loggedinSucessfully"));
				Authorizer.user.setMaNV(rs.getString("MaNV"));
				Authorizer.user.setTenNV(rs.getString("TenNV"));
				Authorizer.user.setMatKhau(rs.getString("MatKhau"));
				Authorizer.user.setRole(rs.getBoolean("VaiTro"));
				window.dispose();
				new Main().setVisible(true);
				rs.getStatement().getConnection().close();
				return true;
			} else {
				MessageBox.alert(Language.getMessagePack().getString("loginFailed"));
				rs.getStatement().getConnection().close();
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public static void checkLoginInfo(JTextField usernameField, JPasswordField passwordField, JCheckBox chkSave,
			JButton btn) {
		if (!loginInfo.exists()) System.out.println(true);
		try {
			FileReader fr = new FileReader(loginInfo);
			BufferedReader br = new BufferedReader(fr);
			String line;
			final ArrayList<String> lines = new ArrayList<>();
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
			if (lines.size() != 0) {
				usernameField.setText(lines.get(0));
				passwordField.setText(lines.get(1));
				chkSave.setSelected(true);
				MessageBox.notif(Language.getMessagePack().getString("saveLoginInfo"));
				btn.requestFocus();
			}
			br.close();
			fr.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void saveLoginInfo(JTextField usernameField, JPasswordField passwordField) {
		try {
			FileWriter fw = new FileWriter(loginInfo);
			String username = usernameField.getText();
			String password = String.valueOf(passwordField.getPassword());
			fw.write(username + "\n");
			fw.write(password);
			fw.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}	

	public static void deleteLoginInfo() {
		if (loginInfo.exists()) {
			loginInfo.delete();
		} 
	}


	public static void changePassword(String username, String password, String newPassword, String confirmPassword,
			Window window) {
		try {
			String find_sql = "select * from NhanVien where MaNV=? and MatKhau=?";
			if (JDBCHelper.value(find_sql, username, password) != null) {
				if (newPassword.equals(confirmPassword)) {
					String update_sql = "update NhanVien set MatKhau=? where MaNV=?";
					JDBCHelper.update(update_sql, confirmPassword, username);
					MessageBox.notif(Language.getMessagePack().getString("changePasswordSucessfully"));
					window.dispose();
				} else {
					MessageBox.alert(Language.getMessagePack().getString("confirmedNotMatch"));
				}
			} else {
				MessageBox.alert(Language.getMessagePack().getString("incorrectPassword"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
