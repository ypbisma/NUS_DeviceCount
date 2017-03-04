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
			System.out.println(e.getMessage());
		}
		return conn;
	}

	public void insertDeviceCount(String zone, String building, String floor, String deviceCount, String time) {
		String insertSql = "INSERT INTO DeviceCount (zone, building, floor, count, time)" + " VALUES(?,?,?,?,?)";
		String updateSql = "UPDATE DeviceCount set count = ? WHERE floor = ?";
		String selectSql = "Select count(*) from DeviceCount WHERE floor = ?";

		try (Connection conn = this.connectDeviceCountDatabase();
				PreparedStatement updateStatement = conn.prepareStatement(updateSql);
				PreparedStatement selectStatement = conn.prepareStatement(selectSql);
				PreparedStatement insertStatement = conn.prepareStatement(insertSql);) {

			insertStatement.setString(1, zone);
			insertStatement.setString(2, building);
			insertStatement.setString(3, floor);
			insertStatement.setString(4, deviceCount);
			insertStatement.setString(5, time);
			insertStatement.executeUpdate();
			// }
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void insertBuildingAggregate(String buildingId, String buildingName, String deviceCount, String time) {
		String insertSql = "INSERT INTO AggregateBuilding (buildingId, buildingName, deviceCount, time)" + " VALUES(?,?,?,?)";

		try (Connection conn = this.connectDeviceCountDatabase();
				PreparedStatement insertStatement = conn.prepareStatement(insertSql);) {
			insertStatement.setString(1, buildingId);
			insertStatement.setString(2, buildingName);
			insertStatement.setString(3, deviceCount);
			insertStatement.setString(4, time);
			insertStatement.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void insertZoneAggregate(String buildingId, String buildingName, String deviceCount, String time) {
		String insertSql = "INSERT INTO AggregateBuilding (buildingId, buildingName, deviceCount, time)" + " VALUES(?,?,?,?)";

		try (Connection conn = this.connectDeviceCountDatabase();
				PreparedStatement insertStatement = conn.prepareStatement(insertSql);) {
			insertStatement.setString(1, buildingId);
			insertStatement.setString(2, buildingName);
			insertStatement.setString(3, deviceCount);
			insertStatement.setString(4, time);
			insertStatement.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void emptyDeviceCountDatabase() {

		String deleteZoneBuildingFloor = "DELETE from DeviceCount";
		String deleteBuilding = "DELETE from AggregateBuilding";
		String deleteZone = "DELETE from AggregateZone";
		String deleteUniversity = "DELETE from AggregateUniversity";

		try (Connection conn = this.connectDeviceCountDatabase();
				PreparedStatement deleteZbfStatement = conn.prepareStatement(deleteZoneBuildingFloor);
				PreparedStatement deleteBuildingStatement = conn.prepareStatement(deleteBuilding);
				PreparedStatement deleteZoneStatement = conn.prepareStatement(deleteZone);
				PreparedStatement deleteUniStatement = conn.prepareStatement(deleteUniversity);) {

			deleteZbfStatement.executeUpdate();
			deleteBuildingStatement.executeUpdate();
			deleteZoneStatement.executeUpdate();
			deleteUniStatement.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}