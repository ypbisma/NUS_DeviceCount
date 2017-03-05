package com.nusdcbackend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DeviceCountDatabaseManager {

	private Connection connectDeviceCountDatabase() {
		// SQLite connection strings
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
		String insertSql = "INSERT INTO AggregateBuilding (buildingId, buildingName, deviceCount, time)"
				+ " VALUES(?,?,?,?)";

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

	public void insertZoneAggregate(String zoneId, String zoneName, String deviceCount, String time) {
		String insertSql = "INSERT INTO AggregateZone (zoneId, zoneName, deviceCount, time)" + " VALUES(?,?,?,?)";

		try (Connection conn = this.connectDeviceCountDatabase();
				PreparedStatement insertStatement = conn.prepareStatement(insertSql);) {
			insertStatement.setString(1, zoneId);
			insertStatement.setString(2, zoneName);
			insertStatement.setString(3, deviceCount);
			insertStatement.setString(4, time);
			insertStatement.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void insertUniAggregate(String uniId, String uniName, String deviceCount, String time) {
		String insertSql = "INSERT INTO AggregateUniversity (uniId, uniName, deviceCount, time)" + " VALUES(?,?,?,?)";

		try (Connection conn = this.connectDeviceCountDatabase();
				PreparedStatement insertStatement = conn.prepareStatement(insertSql);) {
			insertStatement.setString(1, uniId);
			insertStatement.setString(2, uniName);
			insertStatement.setString(3, deviceCount);
			insertStatement.setString(4, time);
			insertStatement.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void insertZoneForecast(String zoneId, String zoneName, String deviceCount, String time) {
		String insertSql = "INSERT INTO ForecastZone (zoneId, zoneName, deviceCount, time)" + " VALUES(?,?,?,?)";

		try (Connection conn = this.connectDeviceCountDatabase();
				PreparedStatement insertStatement = conn.prepareStatement(insertSql);) {
			insertStatement.setString(1, zoneId);
			insertStatement.setString(2, zoneName);
			insertStatement.setString(3, deviceCount);
			insertStatement.setString(4, time);
			insertStatement.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public ArrayList<Zone> getAggregateZones() {

		ArrayList<Zone> zoneList = new ArrayList<>();

		try (Connection conn = this.connectDeviceCountDatabase();) {
			Statement stmt;
			stmt = conn.createStatement();

			String sql = "SELECT * from AggregateZone";
			ResultSet res;
			res = stmt.executeQuery(sql);

			while (res.next()) {
				Zone zoneItem = new Zone(res.getString("zoneId"), res.getString("zoneName"),
						res.getString("deviceCount"), res.getString("time"));
				
				zoneList.add(zoneItem);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return zoneList;
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