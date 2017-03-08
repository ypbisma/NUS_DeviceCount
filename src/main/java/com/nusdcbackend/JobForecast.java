package com.nusdcbackend;

import java.util.ArrayList;

public class JobForecast {
	private DeviceCountForecaster forecaster;
	private DeviceCountDatabaseManager dcDatabaseManager;
	private ZoneBuildingFloorDatabaseManager zbfDatabaseManager;
	private String token;

	private ArrayList<Zone> zoneList = new ArrayList<Zone>();
	private ArrayList<Building> buildingList = new ArrayList<Building>();

	public JobForecast(String token) {
		this.token = token;
		zbfDatabaseManager = new ZoneBuildingFloorDatabaseManager(token);
		dcDatabaseManager = new DeviceCountDatabaseManager();
	}

	public void forecastZone() {
		zoneList = zbfDatabaseManager.getZones();
		buildingList = zbfDatabaseManager.getBuildings();
		forecaster = new DeviceCountForecaster();

		// WeightedAverageMethod
		double[] weights = { 0.4, 0.25, 0.15, 0.1, 0.1 };

		for (Zone zone : zoneList) {
			ArrayList<Zone> zoneAggregate = dcDatabaseManager.getAggregateZones();
			Zone zoneMa3 = forecaster.movingAverage(zoneAggregate, 3, zone.getZoneId(), zone.getZoneName());
			dcDatabaseManager.insertZoneForecast(zoneMa3.getZoneId(), zoneMa3.getZoneName(), zoneMa3.getCount(),
					zoneMa3.getTime(), "ma3");

			Zone zoneMa5 = forecaster.movingAverage(zoneAggregate, 5, zone.getZoneId(), zone.getZoneName());
			dcDatabaseManager.insertZoneForecast(zoneMa5.getZoneId(), zoneMa5.getZoneName(),
					zoneMa5.getCount(), zoneMa5.getTime(), "ma5");

			Zone zoneWam = forecaster.weightedAverage(zoneAggregate, weights, zone.getZoneName(), zone.getZoneId());
			dcDatabaseManager.insertZoneForecast(zoneWam.getZoneId(), zoneWam.getZoneName(),
					zoneWam.getCount(), zoneWam.getTime(), "wam");

			Zone zoneEs = forecaster.exponentialSmoothing(dcDatabaseManager.getForecastZones("zone"),
					dcDatabaseManager.getAggregateZones(), 0.5, zone.getZoneId(), zone.getZoneName());
			dcDatabaseManager.insertZoneForecast(zoneEs.getZoneId(), zoneEs.getZoneName(),
					zoneEs.getCount(), zoneEs.getTime(), "es");
		}
		
		for (Building building : buildingList){
			ArrayList<Building> buildingAggregate = dcDatabaseManager.getAggregateBuilding();
			
		}
	}
}