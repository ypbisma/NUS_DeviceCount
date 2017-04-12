//**NUSWATCH-DEVICECOUNT**
//**A FINAL YEAR PROJECT**
//**BY YOHANES PAULUS BISMA**
//**A0115902N**
//**INDUSTRIAL SYSTEMS ENGINEERING & MANAGEMENT**
//**2016/2017**

//This class is responsible for writing the actual device count and forecasted device count into the database file
package com.nusdcbackend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DeviceCountDatabaseManager {

	private Connection connectDeviceCountDatabase() {
		//url is the address of the database
		String url = "jdbc:sqlite:/Users/Bisma/DeviceCount/DeviceCount_Serverapp/nusdc.db";
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}
	
	//for individual floor device count insertFloorAggregate takes in the variables of floorId, floorName, deviceCount and cal and write them into the database, in the AggregateFloor table
	public void insertFloorAggregate(String floorId, String floorName, String deviceCount, Calendar cal) {
		String insertSql = "INSERT INTO AggregateFloor (floorId, floorName, deviceCount, time, date)"
				+ " VALUES(?,?,?,?,?)";

		try (Connection conn = this.connectDeviceCountDatabase();
				PreparedStatement insertStatement = conn.prepareStatement(insertSql);) {

			insertStatement.setString(1, floorId);
			insertStatement.setString(2, floorName);
			insertStatement.setString(3, deviceCount);
			insertStatement.setString(4, this.getTime(cal));
			insertStatement.setString(5, this.getDate(cal));
			insertStatement.executeUpdate();
			// }
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	//for building device counts insertBuildingAggregate takes in the variables of buildingId, buildingName, deviceCount and cal and write them into the database, in the AggregateFloor table
	public void insertBuildingAggregate(String buildingId, String buildingName, String deviceCount, Calendar cal) {
		String insertSql = "INSERT INTO AggregateBuilding (buildingId, buildingName, deviceCount, time, date)"
				+ " VALUES(?,?,?,?,?)";

		try (Connection conn = this.connectDeviceCountDatabase();
				PreparedStatement insertStatement = conn.prepareStatement(insertSql);) {
			insertStatement.setString(1, buildingId);
			insertStatement.setString(2, buildingName);
			insertStatement.setString(3, deviceCount);
			insertStatement.setString(4, this.getTime(cal));
			insertStatement.setString(5, this.getDate(cal));
			insertStatement.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	//for zone device counts insertZoneAggregate takes in the variables of zoneId, zoneName, deviceCount and cal and write them into the database, in the AggregateFloor table
	public void insertZoneAggregate(String zoneId, String zoneName, String deviceCount, Calendar cal) {
		String insertSql = "INSERT INTO AggregateZone (zoneId, zoneName, deviceCount, time, date)"
				+ " VALUES(?,?,?,?,?)";

		try (Connection conn = this.connectDeviceCountDatabase();
				PreparedStatement insertStatement = conn.prepareStatement(insertSql);) {
			insertStatement.setString(1, zoneId);
			insertStatement.setString(2, zoneName);
			insertStatement.setString(3, deviceCount);
			insertStatement.setString(4, this.getTime(cal));
			insertStatement.setString(5, this.getDate(cal));
			insertStatement.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	//for the device count of entire university insertUniAggregate takes in the variables of uniId, uniName, deviceCount and cal and write them into the database, in the AggregateFloor table

	public void insertUniAggregate(String uniId, String uniName, String deviceCount, Calendar cal) {
		String insertSql = "INSERT INTO AggregateUniversity (uniId, uniName, deviceCount, time, date)"
				+ " VALUES(?,?,?,?,?)";

		try (Connection conn = this.connectDeviceCountDatabase();
				PreparedStatement insertStatement = conn.prepareStatement(insertSql);) {
			insertStatement.setString(1, uniId);
			insertStatement.setString(2, uniName);
			insertStatement.setString(3, deviceCount);
			insertStatement.setString(4, this.getTime(cal));
			insertStatement.setString(5, this.getDate(cal));
			insertStatement.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// FORECAST
	
	//forecasts made based on zone device counts
	public void insertZoneForecast(String zoneId, String zoneName, String forecast, Calendar cal, String method) {
		String insertSql = null;
		String updateSql = null;
		String selectString = null;
		String lastZoneId = null;
		String lastTime = null;
		String lastDate = null;

		boolean itemExists = false;
		Integer row = 1;
		Integer itemExistsRow = 0;

		switch (method) {
		//choosing the method of forecast
		case "ma3":
			insertSql = "INSERT INTO ForecastZoneMa3 (zoneId, zoneName, ma3, time, date)" + " VALUES(?,?,?,?,?)";
			updateSql = "UPDATE ForecastZoneMa3 set ma3 = ? where rowid = ?";
			selectString = "SELECT * FROM ForecastZoneMa3";
			break;
		case "ma5":
			insertSql = "INSERT INTO ForecastZoneMa5 (zoneId, zoneName, ma5, time, date)" + " VALUES(?,?,?,?,?)";
			updateSql = "UPDATE ForecastZoneMa5 set ma5 = ? where rowid = ?";
			selectString = "SELECT * FROM ForecastZoneMa5";
			break;
		case "wam":
			insertSql = "INSERT INTO ForecastZoneWa (zoneId, zoneName, wa, time, date)" + " VALUES(?,?,?,?,?)";
			updateSql = "UPDATE ForecastZoneWa set wa = ? where rowid = ?";
			selectString = "SELECT * FROM ForecastZoneWa";
			break;
		case "es":
			insertSql = "INSERT INTO ForecastZoneEs (zoneId, zoneName, es, time, date)" + " VALUES(?,?,?,?,?)";
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
				lastDate = res.getString("date");

				if (lastZoneId.equals(zoneId) && lastTime.equals(this.getTime(cal))
						&& lastDate.equals(this.getDate(cal))) {
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
				insertStatement.setString(4, this.getTime(cal));
				insertStatement.setString(5, this.getDate(cal));
				insertStatement.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	//forecasts made based on building device counts
	public void insertBuildingForecast(String buildingId, String buildingName, String forecast, Calendar cal,
			String method) {
		String insertSql = null;
		String updateSql = null;
		String selectString = null;
		String lastBuildingId = null;
		String lastTime = null;
		String lastDate = null;

		boolean itemExists = false;
		Integer row = 1;
		Integer itemExistsRow = 0;

		switch (method) {
		//selecting method of forecast
		case "ma3":
			insertSql = "INSERT INTO ForecastBuildingMa3 (buildingId, buildingName, ma3, time, date)"
					+ " VALUES(?,?,?,?,?)";
			updateSql = "UPDATE ForecastBuildingMa3 set ma3 = ? where rowid = ?";
			selectString = "SELECT * FROM ForecastBuildingMa3";
			break;
		case "ma5":
			insertSql = "INSERT INTO ForecastBuildingMa5 (buildingId, buildingName, ma5, time, date)"
					+ " VALUES(?,?,?,?,?)";
			updateSql = "UPDATE ForecastBuildingMa5 set ma5 = ? where rowid = ?";
			selectString = "SELECT * FROM ForecastBuildingMa5";
			break;
		case "wam":
			insertSql = "INSERT INTO ForecastBuildingWa (buildingId, buildingName, wa, time, date)"
					+ " VALUES(?,?,?,?,?)";
			updateSql = "UPDATE ForecastBuildingWa set wa = ? where rowid = ?";
			selectString = "SELECT * FROM ForecastBuildingWa";
			break;
		case "es":
			insertSql = "INSERT INTO ForecastBuildingEs (buildingId, buildingName, es, time, date)"
					+ " VALUES(?,?,?,?,?)";
			updateSql = "UPDATE ForecastBuildingEs set es = ? where rowid = ?";
			selectString = "SELECT * FROM ForecastBuildingEs";
			break;
		default:
			break;
		}

		if (cal != null) {
			try (Connection conn = this.connectDeviceCountDatabase();
					PreparedStatement insertStatement = conn.prepareStatement(insertSql);
					PreparedStatement updateStatement = conn.prepareStatement(updateSql);
					Statement statement = conn.createStatement()) {

				ResultSet res = statement.executeQuery(selectString);
				while (res.next()) {
					lastBuildingId = res.getString("buildingId");
					lastTime = res.getString("time");
					lastDate = res.getString("date");

					if (lastBuildingId.equals(buildingId) && lastTime.equals(this.getTime(cal))
							&& lastDate.equals(this.getDate(cal))) {
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
					insertStatement.setString(1, buildingId);
					insertStatement.setString(2, buildingName);
					insertStatement.setString(3, forecast);
					insertStatement.setString(4, this.getTime(cal));
					insertStatement.setString(5, this.getDate(cal));
					insertStatement.executeUpdate();
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	//forecasts made based on university device count
	public void insertUniForecast(String uniId, String uniName, String forecast, Calendar cal, String method) {
		String insertSql = null;
		String updateSql = null;
		String selectString = null;
		String lastUniId = null;
		String lastTime = null;

		boolean itemExists = false;
		Integer row = 1;
		Integer itemExistsRow = 0;

		switch (method) {
		case "ma3":
			insertSql = "INSERT INTO ForecastUniMa3 (uniId, uniName, ma3, time, date)" + " VALUES(?,?,?,?,?)";
			updateSql = "UPDATE ForecastUniMa3 set ma3 = ? where rowid = ?";
			selectString = "SELECT * FROM ForecastUniMa3";
			break;
		case "ma5":
			insertSql = "INSERT INTO ForecastUniMa5 (uniId, uniName, ma5, time, date)" + " VALUES(?,?,?,?,?)";
			updateSql = "UPDATE ForecastUniMa5 set ma5 = ? where rowid = ?";
			selectString = "SELECT * FROM ForecastUniMa5";
			break;
		case "wam":
			insertSql = "INSERT INTO ForecastUniWa (uniId, uniName, wa, time, date)" + " VALUES(?,?,?,?,?)";
			updateSql = "UPDATE ForecastUniWa set wa = ? where rowid = ?";
			selectString = "SELECT * FROM ForecastUniWa";
			break;
		case "es":
			insertSql = "INSERT INTO ForecastUniEs (uniId, uniName, es, time, date)" + " VALUES(?,?,?,?,?)";
			updateSql = "UPDATE ForecastUniEs set es = ? where rowid = ?";
			selectString = "SELECT * FROM ForecastUniEs";
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
				lastUniId = res.getString("uniId");
				lastTime = res.getString("time");

				if (lastUniId.equals(uniId) && lastTime.equals(this.getTime(cal))) {
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
				insertStatement.setString(1, uniId);
				insertStatement.setString(2, uniName);
				insertStatement.setString(3, forecast);
				insertStatement.setString(4, this.getTime(cal));
				insertStatement.setString(5, this.getDate(cal));
				insertStatement.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// DATABASE GETTER
	//getting the data of the zone device counts
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
						res.getString("deviceCount"), res.getString("time"), res.getString("date"));
				zoneList.add(zoneItem);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return zoneList;
	}

	//getting the data of the building device counts
	public ArrayList<Building> getAggregateBuilding() {

		ArrayList<Building> buildingList = new ArrayList<>();

		try (Connection conn = this.connectDeviceCountDatabase();) {
			Statement stmt;
			stmt = conn.createStatement();

			String sql = "SELECT * from AggregateBuilding";
			ResultSet res;
			res = stmt.executeQuery(sql);
			while (res.next()) {
				Building buildingItem = new Building(res.getString("buildingId"), res.getString("buildingName"),
						res.getString("deviceCount"), res.getString("time"), res.getString("date"));
				buildingList.add(buildingItem);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return buildingList;
	}

	//getting the data of the university device counts	
	public ArrayList<Uni> getAggregateUni() {

		ArrayList<Uni> uniList = new ArrayList<>();

		try (Connection conn = this.connectDeviceCountDatabase();) {
			Statement stmt;
			stmt = conn.createStatement();

			String sql = "SELECT * from AggregateUniversity";
			ResultSet res;
			res = stmt.executeQuery(sql);
			while (res.next()) {
				Uni uniItem = new Uni(res.getString("uniId"), res.getString("uniName"),
						res.getString("deviceCount"), res.getString("time"), res.getString("date"));
				uniList.add(uniItem);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return uniList;
	}

	//getting the data of the zone forecasts
	public ArrayList<ForecastData> getForecastZones(String type) {

		ArrayList<ForecastData> forecastList = new ArrayList<>();
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
						res.getString("es"), res.getString("time"), res.getString("date"));

				forecastList.add(forecastItem);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return forecastList;
	}

	//getting the data of the building forecast
	public ArrayList<ForecastData> getForecastBuildings(String type) {

		ArrayList<ForecastData> forecastList = new ArrayList<>();
		String typeId = type + "Id";
		String typeName = type + "Name";

		try (Connection conn = this.connectDeviceCountDatabase();) {
			Statement stmt;
			stmt = conn.createStatement();

			String sql = "SELECT * from ForecastBuildingEs";
			ResultSet res;
			res = stmt.executeQuery(sql);

			while (res.next()) {
				ForecastData forecastItem = new ForecastData(type, res.getString(typeId), res.getString(typeName),
						res.getString("es"), res.getString("time"), res.getString("date"));

				forecastList.add(forecastItem);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return forecastList;
	}

	//getting the data of the uni forecast
	public ArrayList<ForecastData> getForecastUni(String type) {

		ArrayList<ForecastData> forecastList = new ArrayList<>();
		String typeId = type + "Id";
		String typeName = type + "Name";

		try (Connection conn = this.connectDeviceCountDatabase();) {
			Statement stmt;
			stmt = conn.createStatement();

			String sql = "SELECT * from ForecastUniEs";
			ResultSet res;
			res = stmt.executeQuery(sql);

			while (res.next()) {
				ForecastData forecastItem = new ForecastData(type, res.getString(typeId), res.getString(typeName),
						res.getString("es"), res.getString("time"), res.getString("date"));

				forecastList.add(forecastItem);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return forecastList;
	}

	// EMPTY FUNCTIONS
	//delete all actual device count tables
	public void emptyDeviceCountDatabase() {

		String deleteZoneBuildingFloor = "DELETE from AggregateFloor";
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
		//delete all tables containing forecast data
		String deleteZoneEs = "DELETE from ForecastZoneEs";
		String deleteZoneWa = "DELETE from ForecastZoneWa";
		String deleteZoneMa3 = "DELETE from ForecastZoneMa3";
		String deleteZoneMa5 = "DELETE from ForecastZoneMa5";

		String deleteBuildingEs = "DELETE from ForecastBuildingEs";
		String deleteBuildingWa = "DELETE from ForecastBuildingWa";
		String deleteBuildingMa3 = "DELETE from ForecastBuildingMa3";
		String deleteBuildingMa5 = "DELETE from ForecastBuildingMa5";

		String deleteUniEs = "DELETE from ForecastUniEs";
		String deleteUniWa = "DELETE from ForecastUniWa";
		String deleteUniMa3 = "DELETE from ForecastUniMa3";
		String deleteUniMa5 = "DELETE from ForecastUniMa5";

		try (Connection conn = this.connectDeviceCountDatabase();

				PreparedStatement deleteZoneEsPstmt = conn.prepareStatement(deleteZoneEs);
				PreparedStatement deleteZoneWaPstmt = conn.prepareStatement(deleteZoneWa);
				PreparedStatement deleteZoneMa3Pstmt = conn.prepareStatement(deleteZoneMa3);
				PreparedStatement deleteZoneMa5Pstmt = conn.prepareStatement(deleteZoneMa5);

				PreparedStatement deleteBuildingEsPstmt = conn.prepareStatement(deleteBuildingEs);
				PreparedStatement deleteBuildingWaPstmt = conn.prepareStatement(deleteBuildingWa);
				PreparedStatement deleteBuildingMa3Pstmt = conn.prepareStatement(deleteBuildingMa3);
				PreparedStatement deleteBuildingMa5Pstmt = conn.prepareStatement(deleteBuildingMa5);

				PreparedStatement deleteUniEsPstmt = conn.prepareStatement(deleteUniEs);
				PreparedStatement deleteUniWaPstmt = conn.prepareStatement(deleteUniWa);
				PreparedStatement deleteUniMa3Pstmt = conn.prepareStatement(deleteUniMa3);
				PreparedStatement deleteUniMa5Pstmt = conn.prepareStatement(deleteUniMa5);) {

			deleteZoneEsPstmt.executeUpdate();
			deleteZoneWaPstmt.executeUpdate();
			deleteZoneMa3Pstmt.executeUpdate();
			deleteZoneMa5Pstmt.executeUpdate();

			deleteBuildingEsPstmt.executeUpdate();
			deleteBuildingWaPstmt.executeUpdate();
			deleteBuildingMa3Pstmt.executeUpdate();
			deleteBuildingMa5Pstmt.executeUpdate();

			deleteUniEsPstmt.executeUpdate();
			deleteUniWaPstmt.executeUpdate();
			deleteUniMa3Pstmt.executeUpdate();
			deleteUniMa5Pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getTime(Calendar cal) {
		String time;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		time = sdf.format(cal.getTime());
		return time;
	}

	private String getDate(Calendar cal) {
		String date = "" + cal.get(Calendar.DATE);
		String month = "" + (cal.get(Calendar.MONTH) + 1);
		String year = "" + cal.get(Calendar.YEAR);
		return date + "-" + month + "-" + year;
	}
}