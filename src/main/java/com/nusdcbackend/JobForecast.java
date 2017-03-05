package com.nusdcbackend;

import java.util.ArrayList;

public class JobForecast {
	private DeviceCountForecaster forecaster;
	private DeviceCountDatabaseManager databaseManager = new DeviceCountDatabaseManager();

	public void forecastZone() {
		ArrayList<Zone> zoneAggregate = new ArrayList<>();
		zoneAggregate = databaseManager.getAggregateZones();

		forecaster = new DeviceCountForecaster();
		forecaster.movingAverage(zoneAggregate, 3, "KR-ENGADM", "1");

	}
}