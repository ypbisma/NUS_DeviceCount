//**NUSWATCH-DEVICECOUNT**
//**A FINAL YEAR PROJECT**
//**BY YOHANES PAULUS BISMA**
//**A0115902N**
//**INDUSTRIAL SYSTEMS ENGINEERING & MANAGEMENT**
//**2016/2017**


//This class is designed to extract the data from the actual device count database, execute the calculation and write the forecast information back into the database
//4 methods are used in each function: 3-step moving average, 5-step moving average, Weighted Average and Exponential Smoothing

package com.nusdcbackend;

import java.util.ArrayList;

public class JobForecast {
	private DeviceCountForecaster forecaster;
	private DeviceCountDatabaseManager dcDatabaseManager;
	private ZoneBuildingFloorDatabaseManager zbfDatabaseManager;

	private ArrayList<Zone> zoneList = new ArrayList<Zone>();
	private ArrayList<Building> buildingList = new ArrayList<Building>();

	public JobForecast(String token) {
		zbfDatabaseManager = new ZoneBuildingFloorDatabaseManager(token);
		dcDatabaseManager = new DeviceCountDatabaseManager();
	}

	public void forecastZone() {
		zoneList = zbfDatabaseManager.getZones();
		forecaster = new DeviceCountForecaster();

		// WeightedAverageMethod
		double[] weights = { 0.8, 0.1, 0.1 };

		for (Zone zone : zoneList) {
			ArrayList<Zone> zoneAggregate = dcDatabaseManager.getAggregateZones();
			Zone zoneMa3 = forecaster.zoneMovingAverage(zoneAggregate, 3, zone.getZoneId(), zone.getZoneName());
			dcDatabaseManager.insertZoneForecast(zoneMa3.getZoneId(), zoneMa3.getZoneName(), zoneMa3.getCount(),
					zoneMa3.getTime(), "ma3");

			Zone zoneMa5 = forecaster.zoneMovingAverage(zoneAggregate, 5, zone.getZoneId(), zone.getZoneName());
			dcDatabaseManager.insertZoneForecast(zoneMa5.getZoneId(), zoneMa5.getZoneName(), zoneMa5.getCount(),
					zoneMa5.getTime(), "ma5");

			Zone zoneWam = forecaster.zoneWeightedAverage(zoneAggregate, weights, zone.getZoneName(), zone.getZoneId());
			dcDatabaseManager.insertZoneForecast(zoneWam.getZoneId(), zoneWam.getZoneName(), zoneWam.getCount(),
					zoneWam.getTime(), "wam");

			Zone zoneEs = forecaster.zoneExponentialSmoothing(dcDatabaseManager.getForecastZones("zone"),
					dcDatabaseManager.getAggregateZones(), 1, zone.getZoneId(), zone.getZoneName());
			dcDatabaseManager.insertZoneForecast(zoneEs.getZoneId(), zoneEs.getZoneName(), zoneEs.getCount(),
					zoneEs.getTime(), "es");
		}
	}

	public void forecastBuilding() {
		buildingList = zbfDatabaseManager.getBuildings();
		forecaster = new DeviceCountForecaster();
		// WeightedAverageMethod
		double[] weights = { 0.8, 0.1, 0.1 };

		for (Building building : buildingList) {
			ArrayList<Building> buildingAggregate = dcDatabaseManager.getAggregateBuilding();
			Building buildingMa3 = forecaster.buildingMovingAverage(buildingAggregate, 3, building.getBuildingId(),
					building.getBuildingName());
			dcDatabaseManager.insertBuildingForecast(buildingMa3.getBuildingId(), buildingMa3.getBuildingName(),
					buildingMa3.getCount(), buildingMa3.getTime(), "ma3");

			Building buildingMa5 = forecaster.buildingMovingAverage(buildingAggregate, 5, building.getBuildingId(),
					building.getBuildingName());
			dcDatabaseManager.insertBuildingForecast(buildingMa5.getBuildingId(), buildingMa5.getBuildingName(),
					buildingMa5.getCount(), buildingMa5.getTime(), "ma5");

			Building buildingWam = forecaster.buildingWeightedAverage(buildingAggregate, weights,
					building.getBuildingName(), building.getBuildingId());
			dcDatabaseManager.insertBuildingForecast(buildingWam.getBuildingId(), buildingWam.getBuildingName(),
					buildingWam.getCount(), buildingWam.getTime(), "wam");

		}
	}

	public void forecastUni() {
		forecaster = new DeviceCountForecaster();
		// WeightedAverageMethod
		double[] weights = { 0.8, 0.1, 0.1 };

		ArrayList<Uni> uniAggregate = dcDatabaseManager.getAggregateUni();
		Uni uniMa3 = forecaster.uniMovingAverage(uniAggregate, 3, "1", "NUS");
		dcDatabaseManager.insertUniForecast(uniMa3.getUniId(), uniMa3.getUniName(), uniMa3.getCount(), uniMa3.getTime(),
				"ma3");
		
		Uni uniMa5 = forecaster.uniMovingAverage(uniAggregate, 5, "1", "NUS");
		dcDatabaseManager.insertUniForecast(uniMa5.getUniId(), uniMa5.getUniName(), uniMa5.getCount(), uniMa5.getTime(),
				"ma5");
		
		Uni uniWa = forecaster.uniWeightedAverage(uniAggregate, weights, "NUS", "1");
		dcDatabaseManager.insertUniForecast(uniWa.getUniId(), uniWa.getUniName(), uniWa.getCount(), uniWa.getTime(),
				"wam");

	}
	
	public void fillForecastUni(){
		forecaster = new DeviceCountForecaster();
		ArrayList<Uni> uniCountHolder = new ArrayList<>();
		ArrayList<Uni> uniAggregate = dcDatabaseManager.getAggregateUni();
		
		double[] weights = { 0.8, 0.1, 0.1 };
		double alpha = 1.0;

		
		for(Uni uniCount:uniAggregate){
			uniCountHolder.add(uniCount);
			Uni uniMa3 = forecaster.uniMovingAverage(uniCountHolder, 3, "1", "NUS");
			dcDatabaseManager.insertUniForecast(uniMa3.getUniId(), uniMa3.getUniName(), uniMa3.getCount(), uniMa3.getTime(),
					"ma3");
			Uni uniMa5 = forecaster.uniMovingAverage(uniCountHolder, 5, "1", "NUS");
			dcDatabaseManager.insertUniForecast(uniMa5.getUniId(), uniMa5.getUniName(), uniMa5.getCount(), uniMa5.getTime(),
					"ma5");
			Uni uniWa = forecaster.uniWeightedAverage(uniCountHolder, weights, "NUS", "1");
			dcDatabaseManager.insertUniForecast(uniWa.getUniId(), uniWa.getUniName(), uniWa.getCount(), uniWa.getTime(),
					"wam");
			ArrayList<ForecastData> uniForecast = dcDatabaseManager.getForecastUni("uni");
			Uni uniEs = forecaster.uniExponentialSmoothing(uniForecast, uniCountHolder, alpha, "1", "NUS");
			dcDatabaseManager.insertUniForecast(uniEs.getUniId(), uniEs.getUniName(), uniEs.getCount(), uniWa.getTime(),
					"es");
		}
	}
	
	public void fillForecastZone(){
		zoneList = zbfDatabaseManager.getZones();
		forecaster = new DeviceCountForecaster();
		double alpha = 1.0;

		ArrayList<Zone> zoneIdSpecifiedCountHolder = new ArrayList<>();
		ArrayList<Zone> zoneCountHolderSpecifiedId = new ArrayList<>();

		// WeightedAverageMethod
		double[] weights = { 0.8, 0.1, 0.1 };
		
		for (Zone zone : zoneList) {
			zoneIdSpecifiedCountHolder.clear();

			ArrayList<Zone> zoneAggregate = dcDatabaseManager.getAggregateZones();
			for(Zone zoneCount:zoneAggregate){
				if(zoneCount.getZoneId().equals(zone.getZoneId())){
					zoneIdSpecifiedCountHolder.add(zoneCount);
				}
			}
			
			for(Zone zoneIdSpecifiedItem : zoneIdSpecifiedCountHolder){
				
				zoneCountHolderSpecifiedId.add(zoneIdSpecifiedItem);
				Zone zoneMa3 = forecaster.zoneMovingAverage(zoneCountHolderSpecifiedId, 3, zone.getZoneId(), zone.getZoneName());
				dcDatabaseManager.insertZoneForecast(zoneMa3.getZoneId(), zoneMa3.getZoneName(), zoneMa3.getCount(),
						zoneMa3.getTime(), "ma3");

				Zone zoneMa5 = forecaster.zoneMovingAverage(zoneCountHolderSpecifiedId, 5, zone.getZoneId(), zone.getZoneName());
				dcDatabaseManager.insertZoneForecast(zoneMa5.getZoneId(), zoneMa5.getZoneName(), zoneMa5.getCount(),
						zoneMa5.getTime(), "ma5");

				Zone zoneWam = forecaster.zoneWeightedAverage(zoneCountHolderSpecifiedId, weights, zone.getZoneName(), zone.getZoneId());
				dcDatabaseManager.insertZoneForecast(zoneWam.getZoneId(), zoneWam.getZoneName(), zoneWam.getCount(),
						zoneWam.getTime(), "wam");

				Zone zoneEs = forecaster.zoneExponentialSmoothing(dcDatabaseManager.getForecastZones("zone"),
						zoneCountHolderSpecifiedId, alpha, zone.getZoneId(), zone.getZoneName());
				dcDatabaseManager.insertZoneForecast(zoneEs.getZoneId(), zoneEs.getZoneName(), zoneEs.getCount(),
						zoneEs.getTime(), "es");
				
			}
			
		}
	}
	
	public void fillForecastBuilding(){
		buildingList = zbfDatabaseManager.getBuildings();
		forecaster = new DeviceCountForecaster();
		double alpha = 1.0;

		ArrayList<Building> buildingIdSpecifiedCountHolder = new ArrayList<>();
		ArrayList<Building> buildingCountHolderSpecifiedId = new ArrayList<>();

		// WeightedAverageMethod
		double[] weights = { 0.8, 0.1, 0.1 };
		
		for (Building building : buildingList) {
			buildingIdSpecifiedCountHolder.clear();
			buildingCountHolderSpecifiedId.clear();
			ArrayList<Building> buildingAggregate = dcDatabaseManager.getAggregateBuilding();
			
			for(Building buildingCount:buildingAggregate){
				if(buildingCount.getBuildingId().equals(building.getBuildingId())){
					buildingIdSpecifiedCountHolder.add(buildingCount);
				}
			}
			
			for(Building buildingIdSpecifiedItem : buildingIdSpecifiedCountHolder){
				buildingCountHolderSpecifiedId.add(buildingIdSpecifiedItem);
				Building buildingMa3 = forecaster.buildingMovingAverage(buildingCountHolderSpecifiedId, 3, building.getBuildingId(),
						building.getBuildingName());
				dcDatabaseManager.insertBuildingForecast(buildingMa3.getBuildingId(), buildingMa3.getBuildingName(),
						buildingMa3.getCount(), buildingMa3.getTime(), "ma3");

				Building buildingMa5 = forecaster.buildingMovingAverage(buildingCountHolderSpecifiedId, 5, building.getBuildingId(),
						building.getBuildingName());
				dcDatabaseManager.insertBuildingForecast(buildingMa5.getBuildingId(), buildingMa5.getBuildingName(),
						buildingMa5.getCount(), buildingMa5.getTime(), "ma5");

				Building buildingWam = forecaster.buildingWeightedAverage(buildingCountHolderSpecifiedId, weights,
						building.getBuildingName(), building.getBuildingId());
				dcDatabaseManager.insertBuildingForecast(buildingWam.getBuildingId(), buildingWam.getBuildingName(),
						buildingWam.getCount(), buildingWam.getTime(), "wam");
			
				Building buildingEs = forecaster.buildingExponentialSmoothing(dcDatabaseManager.getForecastBuildings("building"),
						buildingCountHolderSpecifiedId, alpha, building.getBuildingId(), building.getBuildingName());
				dcDatabaseManager.insertBuildingForecast(buildingEs.getBuildingId(), buildingEs.getBuildingName(), buildingEs.getCount(),
						buildingEs.getTime(), "es");
				
			}
			
		}
	}
}