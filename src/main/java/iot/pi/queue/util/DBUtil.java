package iot.pi.queue.util;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
	
	private static void closeResultSet(ResultSet resultSet) { 
		try {
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static int[] firstLoadQueue() throws SQLException { 
		int[] queues = new int[3];
		Connection connection = getConnection();
		String selectMaxQueue = "SELECT queue FROM queue ORDER BY slot";
		PreparedStatement statement = connection.prepareStatement(selectMaxQueue);
		ResultSet result = statement.executeQuery();
		int index = 0;
		while(result.next()) {
		    queues[index++] = result.getInt("queue");
		}
		closeResultSet(result);
		return queues;
	}
	
	public static void resetQueue() throws SQLException { 
		Connection connection = getConnection();
		String updateTableSQL = "UPDATE queue SET called = 'N', finished = 'N', queue = 0";
		PreparedStatement preparedStatement = connection.prepareStatement(updateTableSQL);
		preparedStatement.executeUpdate();
		closeConnection(connection);
	}
	
	public static void updateQueue(int slot) throws SQLException { 
		int max = 0;
		Connection connection = getConnection();
		String selectMaxQueue = "SELECT MAX(queue) AS max_queue FROM queue";
		PreparedStatement statement = connection.prepareStatement(selectMaxQueue);
		ResultSet result = statement.executeQuery();
		while(result.next()) {
		    max = result.getInt("max_queue");
		}
		if (max > 999) { 
			max = 0;
		}
		closeResultSet(result);
		String updateTableSQL = "UPDATE queue SET called = 'Y', finished = 'N', queue = ? WHERE slot = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(updateTableSQL);
		preparedStatement.setInt(1, ++max);
		preparedStatement.setInt(2, slot);
		preparedStatement.executeUpdate();
		closeConnection(connection);
	}
	
	public static void terminateQueue(int slot) throws SQLException { 
		Connection connection = getConnection();
		String updateTableSQL = "UPDATE queue SET finished = 'Y' WHERE slot = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(updateTableSQL);
		preparedStatement.setInt(1, slot);
		preparedStatement.executeUpdate();
		closeConnection(connection);
	}
	
}