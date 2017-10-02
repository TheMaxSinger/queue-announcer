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
	
	public static String loadQueue() throws SQLException { 
		int[] slots = new int[3];
		int[] queues = new int[3];
		Connection connection = getConnection();
		String selectMaxQueue = "SELECT slot_number, queue FROM queue ORDER BY slot_sequence";
		PreparedStatement statement = connection.prepareStatement(selectMaxQueue);
		ResultSet result = statement.executeQuery();
		int index = 0;
		while(result.next()) {
		    	queues[index] = result.getInt("queue");
			slots[index] = result.getInt("slot_number");
			index++;
		}
		closeResultSet(result);
		closeConnection(connection);
		return StringUtil.getInputString(slots, queues);
	}

	public static int repeatQueue(int slot) throws SQLException { 
		Connection connection = getConnection();
		String selectQueue = "SELECT queue FROM queue WHERE slot_number = ?";
		PreparedStatement statement = connection.prepareStatement(selectQueue);
		statement.setInt(1, slot);
		ResultSet result = statement.executeQuery();
		int queue = 0;
		while(result.next()) {
		    queue = result.getInt("queue");
		}
		closeResultSet(result);
		closeConnection(connection);
		return queue;
	}
	
	public static void resetQueue() throws SQLException { 
		Connection connection = getConnection();
		String updateTableSQL = "UPDATE queue SET queue = 0, last_called = 'N'";
		PreparedStatement preparedStatement = connection.prepareStatement(updateTableSQL);
		preparedStatement.executeUpdate();
		updateTableSQL = "UPDATE queue SET slot_sequence = 1 WHERE slot_number = 1";
		preparedStatement = connection.prepareStatement(updateTableSQL);
		preparedStatement.executeUpdate();
		updateTableSQL = "UPDATE queue SET slot_sequence = 2 WHERE slot_number = 2";
		preparedStatement = connection.prepareStatement(updateTableSQL);
		preparedStatement.executeUpdate();
		updateTableSQL = "UPDATE queue SET slot_sequence = 3 WHERE slot_number = 3";
		preparedStatement = connection.prepareStatement(updateTableSQL);
		preparedStatement.executeUpdate();
		closeConnection(connection);
	}
	
	public static int updateQueue(int slot, int queue, boolean afterFix) throws SQLException { 
		int zeros = 0;		
		int max = 0;
		int newQueue = 0;
		int lastCalledSlot = 0;
		Connection connection = getConnection();
		String selectZeros = "SELECT COUNT(queue) AS zeros FROM queue WHERE queue = 0";
		PreparedStatement statement = connection.prepareStatement(selectZeros);
		ResultSet result = statement.executeQuery();
		while(result.next()) {
		    	zeros = result.getInt("zeros");
		}
		closeResultSet(result);
		String updateTableSQL = "";
		if (zeros != 0) { 
			updateTableSQL = "UPDATE queue SET slot_sequence = ? WHERE slot_number = ?";
			statement = connection.prepareStatement(updateTableSQL);
			statement.setInt(1, zeros);
			statement.setInt(2, slot);
			statement.executeUpdate();
		}
		String selectLastCalled = "SELECT slot_number AS slot FROM queue WHERE last_called = 'Y'";
		statement = connection.prepareStatement(selectLastCalled);
		result = statement.executeQuery();
		while(result.next()) {
		    	lastCalledSlot = result.getInt("slot");
		}
		if (lastCalledSlot == 0) { 
			lastCalledSlot = 3;
		}
		closeResultSet(result);
		if (afterFix) { 
			updateTableSQL = "UPDATE queue SET queue = ?, last_called = 'Y' WHERE slot_number = ?";
			statement = connection.prepareStatement(updateTableSQL);
			statement.setInt(1, queue);
			statement.setInt(2, slot);
			statement.executeUpdate();
			updateTableSQL = "UPDATE queue SET last_called = 'N' WHERE slot_number <> ?";
			statement = connection.prepareStatement(updateTableSQL);
			statement.setInt(1, slot);
			statement.executeUpdate();
			// update sequece 3 to this slot
			// update sequece 2 to last called slot
			// update sequence 1 to another slot

			closeConnection(connection);
			newQueue = queue;
		} else { 
			String selectMaxQueue = "SELECT MAX(queue) AS max_queue FROM queue";
			statement = connection.prepareStatement(selectMaxQueue);
			result = statement.executeQuery();
			while(result.next()) {
		    		max = result.getInt("max_queue");
			}
			if (max > 999) { 
				max = 0;
			}
			closeResultSet(result);
			updateTableSQL = "UPDATE queue SET queue = ?, last_called = 'Y' WHERE slot_number = ?";
			statement = connection.prepareStatement(updateTableSQL);
			newQueue = ++max;
			statement.setInt(1, newQueue);
			statement.setInt(2, slot);
			statement.executeUpdate();
			updateTableSQL = "UPDATE queue SET last_called = 'N' WHERE slot_number <> ?";
			statement = connection.prepareStatement(updateTableSQL);
			statement.setInt(1, slot);
			statement.executeUpdate();
			closeConnection(connection);
		}
		return newQueue;
	}
	
	public static void terminateQueue(int slot) throws SQLException { 
		Connection connection = getConnection();
		String updateTableSQL = "UPDATE queue SET finished = 'Y' WHERE slot_number = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(updateTableSQL);
		preparedStatement.setInt(1, slot);
		preparedStatement.executeUpdate();
		closeConnection(connection);
	}
	
}