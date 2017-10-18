package iot.pi.queue.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public class DBUtil { 
	
	static {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static final List<Integer> slot23 = Arrays.asList(2, 3);
	private static final List<Integer> slot13 = Arrays.asList(1, 3);
	private static final List<Integer> slot12 = Arrays.asList(1, 2);

	private static Connection getConnection() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/iot", "max", "isaac34");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	private static void closeStatement(Statement statement) { 
		try {
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
		closeStatement(statement);
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
		closeStatement(statement);
		closeConnection(connection);
		return queue;
	}
	
	public static void resetQueue() throws SQLException { 
		Connection connection = getConnection();
		String updateTableSQL = "UPDATE queue SET queue = 0, last_called = 'N'";
		PreparedStatement statement = connection.prepareStatement(updateTableSQL);
		statement.executeUpdate();
		updateTableSQL = "UPDATE queue SET slot_sequence = 1 WHERE slot_number = 1";
		statement = connection.prepareStatement(updateTableSQL);
		statement.executeUpdate();
		updateTableSQL = "UPDATE queue SET slot_sequence = 2 WHERE slot_number = 2";
		statement = connection.prepareStatement(updateTableSQL);
		statement.executeUpdate();
		updateTableSQL = "UPDATE queue SET slot_sequence = 3 WHERE slot_number = 3";
		statement = connection.prepareStatement(updateTableSQL);
		statement.executeUpdate();
		closeStatement(statement);
		closeConnection(connection);
	}
	
	public static int updateNextQueue(int slot) throws SQLException { 
		int max = 0;
		int newQueue = 0;
		int lastCalledSlot = 0;
		String updateTableSQL;
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
		closeStatement(statement);
		String selectLastCalled = "SELECT slot_number FROM queue WHERE last_called = 'Y'";
		statement = connection.prepareStatement(selectLastCalled);
		result = statement.executeQuery();
		while(result.next()) {
	    	lastCalledSlot = result.getInt("slot_number");
		}
		closeResultSet(result);
		closeStatement(statement);
		if (lastCalledSlot != 0) { 
			updateTableSQL = "UPDATE queue SET slot_sequence = 2, last_called = 'N' WHERE slot_number = ?";
			statement = connection.prepareStatement(updateTableSQL);
			statement.setInt(1, lastCalledSlot);
			statement.executeUpdate();
			closeStatement(statement);
			updateTableSQL = "UPDATE queue SET slot_sequence = 1, last_called = 'N' WHERE slot_number <> ? AND slot_number <> ?";
			statement = connection.prepareStatement(updateTableSQL);
			statement.setInt(1, lastCalledSlot);
			statement.setInt(2, slot);
			statement.executeUpdate();
			closeStatement(statement);
		} else { 
			updateAnotherSlots(connection, statement, slot);
		}
		updateTableSQL = "UPDATE queue SET queue = ?, slot_sequence = 3, last_called = 'Y' WHERE slot_number = ?";
		statement = connection.prepareStatement(updateTableSQL);
		newQueue = ++max;
		statement.setInt(1, newQueue);
		statement.setInt(2, slot);
		statement.executeUpdate();
		closeStatement(statement);
		closeConnection(connection);
		return newQueue;
	}
	
	private static List<Integer> getAnotherSlots(int slot) { 
		if (slot == 1) { 
			return slot23;
		} else if (slot == 2) { 
			return slot13;
		} else { 
			return slot12;
		}
	}
	
	private static void updateAnotherSlots(Connection connection, PreparedStatement statement, int slot) throws SQLException { 
		String updateTableSQL;
		int shiftingSlot = 1;
		for (int anotherSlot : getAnotherSlots(slot)) { 
			updateTableSQL = "UPDATE queue SET slot_sequence = ?, last_called = 'N' WHERE slot_number = ?";
			statement = connection.prepareStatement(updateTableSQL);
			statement.setInt(1, shiftingSlot++);
			statement.setInt(2, anotherSlot);
			statement.executeUpdate();
			closeStatement(statement);
		}
	}
	
	public static void updateQueue(int slot, int queue) throws SQLException { 
		Connection connection = getConnection();
		String updateTableSQL = "UPDATE queue SET slot_sequence = 3, queue = ?, last_called = 'Y' WHERE slot_number = ?";
		PreparedStatement statement = connection.prepareStatement(updateTableSQL);
		statement.setInt(1, queue);
		statement.setInt(2, slot);
		statement.executeUpdate();
		closeStatement(statement);
		updateAnotherSlots(connection, statement, slot);
		closeConnection(connection);
	}
	
	public static void main(String[] args) throws SQLException { 
		System.out.println(DBUtil.loadQueue());
	}
	
}