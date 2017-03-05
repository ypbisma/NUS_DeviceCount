package com.nusdcbackend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.nusdcbackend.ZoneBuildingFloor;

public class ZoneBuildingFloorDatabaseManager {
	private String[] allZones;
	private String[] allBuildings;
	private String[] allFloors;
	private ZoneBuildingFloorManager zoneBuildingFloorManager;

	public ZoneBuildingFloorDatabaseManager(String token) {
		zoneBuildingFloorManager = new ZoneBuildingFloorManager(token);
	}

	private Connection connectZoneBuildingFloorDatabase() {
		// SQLite connection string
		String url = "jdbc:sqlite:/Users/Bisma/DeviceCount/DeviceCount_Serverapp/zonebuildingfloor.db";
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
		return conn;
	}

	public void insertZoneBuildingFloor(String zone, String building, String floor) {
		String insertSql = "INSERT INTO ZoneBuildingFloor (zone, building, floor)" + " VALUES(?,?,?)";
		String selectSql = "Select count(*) from ZoneBuildingFloor WHERE floor = ?";

		try (Connection conn = this.connectZoneBuildingFloorDatabase();
				PreparedStatement insertStatement = conn.prepareStatement(insertSql);
				PreparedStatement selectStatement = conn.prepareStatement(selectSql);) {
			int count = 0;
			selectStatement.setString(1, floor);
			ResultSet resultSet = selectStatement.executeQuery();
			if (resultSet.next()) {
				count = resultSet.getInt(1);
			}
			if (count <= 0) {
				insertStatement.setString(1, zone);
				insertStatement.setString(2, building);
				insertStatement.setString(3, floor);
				insertStatement.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private void insertZone(String zoneId, String zoneName) {
		String insertSql = "INSERT INTO Zone (zoneId, zoneName)" + " VALUES(?,?)";
		String selectSql = "Select count(*) from Zone WHERE zoneName = ?";

		try (Connection conn = this.connectZoneBuildingFloorDatabase();
				PreparedStatement insertStatement = conn.prepareStatement(insertSql);
				PreparedStatement selectStatement = conn.prepareStatement(selectSql);) {
			int count = 0;
			selectStatement.setString(1, zoneName);

			ResultSet resultSet = selectStatement.executeQuery();
			if (resultSet.next()) {
				count = resultSet.getInt(1);
			}
			if (count <= 0) {
				insertStatement.setString(1, zoneId);
				insertStatement.setString(2, zoneName);
				insertStatement.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private void insertBuilding(String buildingId, String buildingName, String zoneId) {
		String insertSql = "INSERT INTO Building (buildingId, buildingName, zoneId)" + " VALUES(?,?,?)";
		String selectSql = "Select count(*) from Building WHERE buildingName = ?";

		try (Connection conn = this.connectZoneBuildingFloorDatabase();
				PreparedStatement insertStatement = conn.prepareStatement(insertSql);
				PreparedStatement selectStatement = conn.prepareStatement(selectSql);) {
			int count = 0;
			selectStatement.setString(1, buildingName);

			ResultSet resultSet = selectStatement.executeQuery();
			if (resultSet.next()) {
				count = resultSet.getInt(1);
			}
			if (count <= 0) {
				insertStatement.setString(1, buildingId);
				insertStatement.setString(2, buildingName);
				insertStatement.setString(3, zoneId);
				insertStatement.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private void insertFloor(String floorId, String floorName, String buildingId) {
		String insertSql = "INSERT INTO Floor (floorId, floorName, buildingId)" + " VALUES(?,?,?)";
		String selectSql = "Select count(*) from Floor WHERE floorName = ?";

		try (Connection conn = this.connectZoneBuildingFloorDatabase();
				PreparedStatement insertStatement = conn.prepareStatement(insertSql);
				PreparedStatement selectStatement = conn.prepareStatement(selectSql);) {
			int count = 0;
			selectStatement.setString(1, floorName);

			ResultSet resultSet = selectStatement.executeQuery();
			if (resultSet.next()) {
				count = resultSet.getInt(1);
			}
			if (count <= 0) {
				insertStatement.setString(1, floorId);
				insertStatement.setString(2, floorName);
				insertStatement.setString(3, buildingId);
				insertStatement.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void writeZoneBuildingFloor() throws Exception {
		Integer zoneId = 0;
		Integer buildingId = 0;
		Integer floorId = 1;
		zoneBuildingFloorManager.syncZoneList();
		allZones = zoneBuildingFloorManager.getAllZones();
		for (String zone : allZones) {
			zoneId++;
			zoneBuildingFloorManager.syncBuildingList(zone);
			allBuildings = zoneBuildingFloorManager.getAllBuildings();
			this.insertZone(zoneId.toString(), zone);
			for (String building : allBuildings) {
				buildingId++;
				zoneBuildingFloorManager.syncFloorList(zone, building);
				allFloors = zoneBuildingFloorManager.getAllFloors();
				this.insertBuilding(buildingId.toString(), building, zoneId.toString());

				for (String floor : allFloors) {
					insertZoneBuildingFloor(zone, building, floor);
					this.insertFloor(floorId.toString(), floor, buildingId.toString());
				}
			}
		}
	}

	public boolean zoneBuildingFloorIsEmpty() throws SQLException {

		String selectSql = "Select count(*) FROM ZoneBuildingFloor";

		try (Connection conn = this.connectZoneBuildingFloorDatabase();
				PreparedStatement selectStatement = conn.prepareStatement(selectSql);) {
			boolean empty = true;
			int count = 0;
			ResultSet resultSet = selectStatement.executeQuery();
			while (resultSet.next()) {
				count = resultSet.getInt(1);
			}
			if (count > 0) {
				empty = false;
			}
			return empty;
		}
	}

	public void emptyZoneBuildingFloorDatabase() {

		String deleteZoneBuildingFloor = "DELETE from ZoneBuildingFloor";
		String deleteZone = "DELETE from Zone";
		String deleteBuilding = "DELETE from Building";

		try (Connection conn = this.connectZoneBuildingFloorDatabase();
				PreparedStatement deleteZbfStatement = conn.prepareStatement(deleteZoneBuildingFloor);
				PreparedStatement deleteZoneStatement = conn.prepareStatement(deleteZone);
				PreparedStatement deleteBuildingStatement = conn.prepareStatement(deleteBuilding);) {

			deleteZbfStatement.executeUpdate();
			deleteZoneStatement.executeUpdate();
			deleteBuildingStatement.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<ZoneBuildingFloor> getZoneBuildingFloor() {
		ArrayList<ZoneBuildingFloor> zoneBuildingFloorList = new ArrayList<>();
		try (Connection conn = this.connectZoneBuildingFloorDatabase();) {
			Statement stmt;
			stmt = conn.createStatement();
			String sql = "SELECT * from ZoneBuildingFloor";
			ResultSet res;
			res = stmt.executeQuery(sql);

			while (res.next()) {
				ZoneBuildingFloor zoneBuildingFloorItem = new ZoneBuildingFloor(res.getString("zone"),
						res.getString("building"), res.getString("floor"));
				zoneBuildingFloorList.add(zoneBuildingFloorItem);
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return zoneBuildingFloorList;
	}

	public ArrayList<Zone> getZones() {
		ArrayList<Zone> zoneList = new ArrayList<>();
		try (Connection conn = this.connectZoneBuildingFloorDatabase();) {
			Statement stmt;
			stmt = conn.createStatement();
			String sql = "SELECT * from Zone";
			ResultSet res;
			res = stmt.executeQuery(sql);

			while (res.next()) {
				Zone zoneItem = new Zone(res.getString("zoneId"), res.getString("zoneName"));
				zoneList.add(zoneItem);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return zoneList;
	}

	public ArrayList<Building> getBuildings() {
		ArrayList<Building> buildingList = new ArrayList<>();
		try (Connection conn = this.connectZoneBuildingFloorDatabase();) {
			Statement stmt;
			stmt = conn.createStatement();
			String sql = "SELECT * from Building";
			ResultSet res;
			res = stmt.executeQuery(sql);

			while (res.next()) {
				Building buildingItem = new Building(res.getString("buildingId"), res.getString("buildingName"),
						res.getString("zoneId"));
				buildingList.add(buildingItem);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return buildingList;
	}
	
	public ArrayList<Floor> getFloors() {
		ArrayList<Floor> floorList = new ArrayList<>();
		try (Connection conn = this.connectZoneBuildingFloorDatabase();) {
			Statement stmt;
			stmt = conn.createStatement();
			String sql = "SELECT * from Floor";
			ResultSet res;
			res = stmt.executeQuery(sql);

			while (res.next()) {
				Floor floorItem = new Floor(res.getString("floorId"), res.getString("floorName"),
						res.getString("buildingId"));
				floorList.add(floorItem);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return floorList;
	}

	public String[] getAllZones() {
		return allZones;
	}

	public void setAllZones(String[] allZones) {
		this.allZones = allZones;
	}

	public String[] getAllBuildings() {
		return allBuildings;
	}

	public void setAllBuildings(String[] allBuildings) {
		this.allBuildings = allBuildings;
	}

	public String[] getAllFloors() {
		return allFloors;
	}

	public void setAllFloors(String[] allFloors) {
		this.allFloors = allFloors;
	}
}
