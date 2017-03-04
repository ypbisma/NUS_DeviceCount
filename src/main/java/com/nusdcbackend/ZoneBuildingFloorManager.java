package com.nusdcbackend;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.UnknownHostException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;

public class ZoneBuildingFloorManager {
	private String zoneListBranch;
	private int token;
	private String zoneListUrl;
	private String buildingListBranch;
	private String buildingListUrl;
	private String API_URL;
	private String floorListBranch;
	private String floorListUrl;

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
}
