package com.nusdcbackend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class DeviceCountDatabaseManager {

	private Connection connectDeviceCountDatabase() {
		// SQLite connection string
		String url = "jdbc:sqlite:/Users/Bisma/DeviceCount/DeviceCount_Serverapp/nusdc.db";
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println("problem here");
		}
		return conn;
	}


	public void insertDeviceCount(String zone, String building, String floor, String deviceCount) {
		String insertSql = "INSERT INTO DeviceCount (zone, building, floor, count)" + " VALUES(?,?,?,?)";
		String updateSql = "UPDATE DeviceCount set count = ? WHERE floor = ?";
		String selectSql = "Select count(*) from DeviceCount WHERE floor = ?";

		try (Connection conn = this.connectDeviceCountDatabase();
				PreparedStatement updateStatement = conn.prepareStatement(updateSql);
				PreparedStatement selectStatement = conn.prepareStatement(selectSql);
				PreparedStatement insertStatement = conn.prepareStatement(insertSql);) {

			// int count = 0;
			// selectStatement.setString(1, floor);
			// ResultSet resultSet = selectStatement.executeQuery();
			// if (resultSet.next()) {
			// count = resultSet.getInt(1);
			// }
			//
			// if (count > 0) {
			// // set the preparedstatement parameters
			// updateStatement.setString(1, deviceCount);
			// updateStatement.setString(2, floor);
			// updateStatement.executeUpdate();
			// } else {

			insertStatement.setString(1, zone);
			insertStatement.setString(2, building);
			insertStatement.setString(3, floor);
			insertStatement.setString(4, deviceCount);
			insertStatement.executeUpdate();
			// }
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	
}