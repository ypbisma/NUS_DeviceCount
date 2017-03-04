package com.nusdcbackend;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import com.google.gson.Gson;

public class DeviceCountManager {

	private static final String API_URL = "https://api.ami-lab.org";
	// DEVICE COUNT
	private String zoneName;
	private String buildingName;
	private String floorName;
	private String deviceCountBranch;
	private String deviceLocationString;
	private DeviceCountObject deviceCountObject;
	private DeviceCount deviceCount;
	private String deviceCountUrl;

	// ZONEBUILDINGFLOOR LIST
	private String zoneListBranch;
	private String buildingListBranch;
	private String floorListBranch;
	private String zoneListUrl;
	private String buildingListUrl;
	private String floorListUrl;

	private String[] zoneList;
	private String[] buildingList;
	private String[] floorList;

	private String token;

	public DeviceCountManager(String token) {
		this.token = token;

	}

	public void syncDeviceCount() throws Exception {
		try {
			this.deviceCountBranch = "/api/v1/cisco" + "/zone/" + zoneName + "/building/" + buildingName + "/floor/"
					+ floorName + "/count" + "?token=";
			this.deviceCountUrl = API_URL + deviceCountBranch + token;

			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(this.deviceCountUrl);
			HttpResponse response = client.execute(request);
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";

			while ((line = reader.readLine()) != null) {
				result.append(line);
			}

			Gson gson = new Gson();

			this.setDeviceCountObject(gson.fromJson(result.toString(), DeviceCountObject.class));
			deviceCount = deviceCountObject.getDeviceCount();

		} catch (UnknownHostException e) {
			System.out.println("no internet connection!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

	

	

	public String getDeviceLocationString() {
		return deviceLocationString;
	}

	public void setDeviceLocationString(String deviceLocationString) {
		this.deviceLocationString = deviceLocationString;
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getFloorName() {
		return floorName;
	}

	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}

	public DeviceCountObject getDeviceCountObject() {
		return deviceCountObject;
	}

	public void setDeviceCountObject(DeviceCountObject deviceCountObject) {
		this.deviceCountObject = deviceCountObject;
	}

	public DeviceCount getDeviceCount() {
		return deviceCount;
	}

	public void setDeviceCount(DeviceCount deviceCount) {
		this.deviceCount = deviceCount;
	}

	public void setZoneList(String[] zoneList) {
		this.zoneList = zoneList;
	}

	public String[] getZoneList() {
		return zoneList;
	}

	public String[] getBuildingList() {
		return buildingList;
	}

	public void setBuildingList(String[] buildingList) {
		this.buildingList = buildingList;
	}

	public String[] getFloorList() {
		return floorList;
	}

	public void setFloorList(String[] floorList) {
		this.floorList = floorList;
	}

}
