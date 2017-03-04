package com.nusdcbackend;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;

import com.nusdcbackend.ZoneBuildingFloor;

public class ZoneBuildingFloorDatabaseManager {
	private static final String API_URL = null;
	private String[] allZones;

	private String[] allBuildings;
	private String[] allFloors;
	private String zoneListBranch;
	private String zoneListUrl;
	private String token;
	private String buildingListBranch;
	private String buildingListUrl;
	private String floorListBranch;
	private String floorListUrl;

	ZoneBuildingFloorDatabaseManager(String token){
		this.token = token;
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

	public void syncZoneList() throws Exception {
		try {
			this.zoneListBranch = "/api/v1/cisco/zones" + "?token=";
			this.zoneListUrl = API_URL + zoneListBranch + token;
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(this.zoneListUrl);

			HttpResponse response = client.execute(request);
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";

			while ((line = reader.readLine()) != null) {
				result.append(line);
			}
			Gson gson = new Gson();

			this.setAllZones(gson.fromJson(result.toString(), String[].class));

		} catch (UnknownHostException e) {
			System.out.println("no internet connection!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void syncBuildingList(String zone) {
		try {
			this.buildingListBranch = "/api/v1/cisco/zone/" + zone + "/buildings" + "?token=";
			this.buildingListUrl = API_URL + buildingListBranch + token;
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(this.buildingListUrl);

			HttpResponse response = client.execute(request);
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";

			while ((line = reader.readLine()) != null) {
				result.append(line);
			}

			Gson gson = new Gson();
			this.setAllBuildings(gson.fromJson(result.toString(), String[].class));

		} catch (UnknownHostException e) {
			System.out.println("no internet connection!");
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	private void syncFloorList(String zone, String building) {
		try {
			this.floorListBranch = "/api/v1/cisco/zone/" + zone + "/building/" + building + "/floors" + "?token=";
			this.floorListUrl = API_URL + floorListBranch + token;
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(this.floorListUrl);

			HttpResponse response = client.execute(request);
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";

			while ((line = reader.readLine()) != null) {
				result.append(line);
			}

			Gson gson = new Gson();
			this.setAllFloors(gson.fromJson(result.toString(), String[].class));

		} catch (UnknownHostException e) {
			System.out.println("no internet connection!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void writeZoneBuildingFloor() throws Exception {
		this.syncZoneList();
		for (String zone : allZones) {
			Integer i = 1;
			this.syncBuildingList(zone);
			this.insertZone(i.toString(), zone);
			i++;
			for (String building : allBuildings) {
				this.syncFloorList(zone, building);
				for (String floor : allFloors) {
					insertZoneBuildingFloor(zone, building, floor);
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

		String selectSql = "DELETE from ZoneBuildingFloor";

		try (Connection conn = this.connectZoneBuildingFloorDatabase();
				PreparedStatement selectStatement = conn.prepareStatement(selectSql);) {

			selectStatement.executeUpdate();

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
