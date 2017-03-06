package com.nusdcbackend;

import java.util.ArrayList;

public class JobForecast {
	private DeviceCountForecaster forecaster;
	private DeviceCountDatabaseManager dcDatabaseManager;
	private ZoneBuildingFloorDatabaseManager zbfDatabaseManager;
	private String token;

	private ArrayList<Zone> zoneList = new ArrayList<Zone>();

	public JobForecast(String token) {
		this.token = token;
		zbfDatabaseManager = new ZoneBuildingFloorDatabaseManager(token);
		dcDatabaseManager = new DeviceCountDatabaseManager();
	}

	public void forecastZone() {
		zoneList = zbfDatabaseManager.getZones();
		ArrayList<Zone> zoneAggregate = new ArrayList<>();
		zoneAggregate = dcDatabaseManager.getAggregateZones();
		forecaster = new DeviceCountForecaster();
		
		//WeightedAverageMethod
		double[] weights = { 0.5, 0.25, 0.15, 0.1, 0.1 };

		for (Zone zone : zoneList) {
			Zone zoneToWrite = forecaster.movingAverage(zoneAggregate, 3, zone.getZoneId(), zone.getZoneName());
			dcDatabaseManager.insertZoneForecast(zoneToWrite.getZoneId(), zoneToWrite.getZoneName(),
					zoneToWrite.getCount(), zoneToWrite.getTime(), "ma3");

			zoneToWrite = forecaster.movingAverage(zoneAggregate, 5, zone.getZoneId(), zone.getZoneName());
			dcDatabaseManager.insertZoneForecast(zoneToWrite.getZoneId(), zoneToWrite.getZoneName(),
					zoneToWrite.getCount(), zoneToWrite.getTime(), "ma5");


			zoneToWrite = forecaster.weightedAverage(zoneAggregate, weights, zone.getZoneName(), zone.getZoneId());
			dcDatabaseManager.insertZoneForecast(zoneToWrite.getZoneId(), zoneToWrite.getZoneName(),
					zoneToWrite.getCount(), zoneToWrite.getTime(), "wam");
		}
	}
}