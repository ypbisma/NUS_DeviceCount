package com.nusdcbackend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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

	// FORECAST

	public void insertZoneForecast(String zoneId, String zoneName, String forecast, Calendar time, String method) {
		String insertSql = null;
		String updateSql = null;
		String selectSql = null;

		String lastZoneId = null;
		String lastTime = null;

		boolean itemExists = false;
		Integer row = 1;
		Integer itemExistsRow = 0;

		switch (method) {
		case "ma3":
			insertSql = "INSERT INTO ForecastZone (zoneId, zoneName, ma3, time)" + " VALUES(?,?,?,?)";
			updateSql = "UPDATE ForecastZone set ma3 = ? where rowid = ?";
			break;
		case "ma5":
			insertSql = "INSERT INTO ForecastZone (zoneId, zoneName, ma5, time)" + " VALUES(?,?,?,?)";
			updateSql = "UPDATE ForecastZone set ma5 = ? where rowid = ?";
			break;
		case "wam":
			insertSql = "INSERT INTO ForecastZone (zoneId, zoneName, wa, time)" + " VALUES(?,?,?,?)";
			updateSql = "UPDATE ForecastZone set wa = ? where rowid = ?";
		default:
		}

		try (Connection conn = this.connectDeviceCountDatabase();
				PreparedStatement insertStatement = conn.prepareStatement(insertSql);
				PreparedStatement updateStatement = conn.prepareStatement(updateSql);
				Statement statement = conn.createStatement()) {

			ResultSet res = statement.executeQuery("SELECT * FROM ForecastZone");
			while (res.next()) {
				lastZoneId = res.getString("zoneId");
				lastTime = res.getString("time");

				if (lastZoneId.equals(zoneId) && lastTime.equals(this.calendarToString(time))) {
					itemExists = true;
					itemExistsRow = row;
				}
				row++;
			}

			if (itemExists) {
				updateStatement.setString(1, forecast);
				updateStatement.setString(2, itemExistsRow.toString());
				updateStatement.executeUpdate();
			} else {
				insertStatement.setString(1, zoneId);
				insertStatement.setString(2, zoneName);
				insertStatement.setString(3, forecast);
				insertStatement.setString(4, this.calendarToString(time));
				insertStatement.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// DATABASE GETTER

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

	public ArrayList<ForecastData> getForecastZones() {

		ArrayList<ForecastData> forecastList = new ArrayList<>();

		try (Connection conn = this.connectDeviceCountDatabase();) {
			Statement stmt;
			stmt = conn.createStatement();

			String sql = "SELECT * from ForecastZone";
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

	// EMPTY FUNCTIONS

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

	public void emptyForecastTable() {

		String deleteZoneBuildingFloor = "DELETE from DeviceCount";
		String deleteBuilding = "DELETE from ForecastBuilding";
		String deleteZone = "DELETE from ForecastZone";
		String deleteUniversity = "DELETE from ForecastUniversity";

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

	private Calendar stringToCalendar(String timeString) {
		Calendar time = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss", Locale.ENGLISH);
		try {
			time.setTime(sdf.parse(timeString));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time;
	}

	private String calendarToString(Calendar timeCalendar) {
		String time;
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		time = sdf.format(timeCalendar.getTime());
		return time;
	}

}