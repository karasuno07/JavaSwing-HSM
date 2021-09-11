package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class JDBCHelper {

	private static String serverName = "localhost";
	private static String dbName = "quanlycf";
	private static String portNumber = "1433";
	private static String instance = "";
	private static String username = "sa";
	private static String password = "79461382465";

	public static Connection getConnection() throws Exception {
		String URL = "jdbc:sqlserver://" + serverName + ":" + portNumber + "\\" + instance + ";databaseName=" + dbName;

		if (instance == null || instance.trim().isEmpty()) {
			URL = "jdbc:sqlserver://" + serverName + ":" + portNumber + ";databaseName=" + dbName;
		}

		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		return DriverManager.getConnection(URL, username, password);
	}

	public static PreparedStatement getStatement(String sql, Object... args) throws Exception {
		Connection connection = JDBCHelper.getConnection();
		PreparedStatement statement;
		if (sql.trim().startsWith("{")) {
			statement = connection.prepareCall(sql);
		} else {
			statement = connection.prepareStatement(sql);
		}
		for (int i = 0; i < args.length; i++) {
			statement.setObject(i + 1, args[i]);
		}
		return statement;
	}

	public static ResultSet query(String sql, Object... args) throws Exception {
		PreparedStatement statement = JDBCHelper.getStatement(sql, args);
		return statement.executeQuery();
	}

	public static Object value(String sql, Object... args) {
		try {
			ResultSet rs = JDBCHelper.query(sql, args);
			if (rs.next()) {
				return rs.getObject(1);
			}
			rs.getStatement().getConnection().close();
			return null;

		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	public static int update(String sql, Object... args) throws Exception {
		PreparedStatement statement = JDBCHelper.getStatement(sql, args);
		try {
			return statement.executeUpdate();
		} finally {
			statement.getConnection().close();
		}
	}
}

