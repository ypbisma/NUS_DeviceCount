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
		String selectString = null;
		String lastZoneId = null;
		String lastTime = null;

		boolean itemExists = false;
		Integer row = 1;
		Integer itemExistsRow = 0;

		switch (method) {
		case "ma3":
			insertSql = "INSERT INTO ForecastZoneMa3 (zoneId, zoneName, ma3, time)" + " VALUES(?,?,?,?)";
			updateSql = "UPDATE ForecastZoneMa3 set ma3 = ? where rowid = ?";
			selectString = "SELECT * FROM ForecastZoneMa3";
			break;
		case "ma5":
			insertSql = "INSERT INTO ForecastZoneMa5 (zoneId, zoneName, ma5, time)" + " VALUES(?,?,?,?)";
			updateSql = "UPDATE ForecastZoneMa5 set ma5 = ? where rowid = ?";
			selectString = "SELECT * FROM ForecastZoneMa5";
			break;
		case "wam":
			insertSql = "INSERT INTO ForecastZoneWa (zoneId, zoneName, wa, time)" + " VALUES(?,?,?,?)";
			updateSql = "UPDATE ForecastZoneWa set wa = ? where rowid = ?";
			selectString = "SELECT * FROM ForecastZoneWa";
			break;
		case "es":
			insertSql = "INSERT INTO ForecastZoneEs (zoneId, zoneName, es, time)" + " VALUES(?,?,?,?)";
			updateSql = "UPDATE ForecastZoneEs set es = ? where rowid = ?";
			selectString = "SELECT * FROM ForecastZoneEs";
			break;
		default:
			break;
		}

		try (Connection conn = this.connectDeviceCountDatabase();
				PreparedStatement insertStatement = conn.prepareStatement(insertSql);
				PreparedStatement updateStatement = conn.prepareStatement(updateSql);
				Statement statement = conn.createStatement()) {

			ResultSet res = statement.executeQuery(selectString);
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

	public ArrayList<ForecastData> getForecastZones(String type) {

		ArrayList<ForecastData> forecastList = new ArrayList<>();
		String capitalisedType = type.substring(0, 1).toUpperCase() + type.substring(1);
		String typeId = type + "Id";
		String typeName = type + "Name";

		try (Connection conn = this.connectDeviceCountDatabase();) {
			Statement stmt;
			stmt = conn.createStatement();

			String sql = "SELECT * from ForecastZoneEs";
			ResultSet res;
			res = stmt.executeQuery(sql);

			while (res.next()) {
				ForecastData forecastItem = new ForecastData(type, res.getString(typeId), res.getString(typeName),
						res.getString("es"), res.getString("time"));

				forecastList.add(forecastItem);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return forecastList;
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
		String deleteEs = "DELETE from ForecastZoneEs";
		String deleteWa = "DELETE from ForecastZoneWa";
		String deleteMa3 = "DELETE from ForecastZoneMa3";
		String deleteMa5 = "DELETE from ForecastZoneMa5";
		String deleteUniversity = "DELETE from ForecastUniversity";

		try (Connection conn = this.connectDeviceCountDatabase();
				PreparedStatement deleteZbfStatement = conn.prepareStatement(deleteZoneBuildingFloor);
				PreparedStatement deleteBuildingStatement = conn.prepareStatement(deleteBuilding);
				PreparedStatement deleteZoneEs = conn.prepareStatement(deleteEs);
				PreparedStatement deleteZoneWa = conn.prepareStatement(deleteWa);
				PreparedStatement deleteZoneMa3 = conn.prepareStatement(deleteMa3);
				PreparedStatement deleteZoneMa5 = conn.prepareStatement(deleteMa5);

				PreparedStatement deleteUniStatement = conn.prepareStatement(deleteUniversity);) {

			deleteZbfStatement.executeUpdate();
			deleteBuildingStatement.executeUpdate();
			deleteZoneEs.executeUpdate();
			deleteZoneWa.executeUpdate();
			deleteZoneMa3.executeUpdate();
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