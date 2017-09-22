package iot.pi.queue.util;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class DBUtil { 
	
	static {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static Connection getConnection() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/iot", "max", "isaac34");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	private static void closeConnection(Connection connection) { 
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void resetQueue() throws SQLException { 
		Connection connection = getConnection();
		String updateTableSQL = "UPDATE queue SET called = 'N', finished = 'N', queue = 0";
		PreparedStatement preparedStatement = connection.prepareStatement(updateTableSQL);
		preparedStatement.executeUpdate();
		closeConnection(connection);
	}
	
}