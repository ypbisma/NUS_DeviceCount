package com.nusdcbackend;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class DeviceCountForecaster {

	//ZONEFORECASTER
	public Zone zoneMovingAverage(ArrayList<Zone> inputZoneArray, int step, String zoneId, String zoneName) {
		Zone zoneToWrite = new Zone(zoneId, zoneName);
		ArrayList<Integer> integerList = new ArrayList<Integer>();
		Calendar maTime = null;
		Double average = 0.0;

		for (Zone zoneAggregate : inputZoneArray) {
			if (zoneAggregate.getZoneName().equals(zoneName)) {
				integerList.add(Integer.parseInt(zoneAggregate.getCount()));
				maTime = zoneAggregate.getTime();
			}
		}

		HashMap<String, Double> zoneForecastKey = new HashMap<String, Double>();

		DescriptiveStatistics stats = new DescriptiveStatistics();
		stats.setWindowSize(step);

		if (integerList.size() >= step) {
			long nLines = 0;
			for (int d : integerList) {
				stats.addValue((double) d);
				nLines++;
				if (nLines >= step) {
					average = stats.getMean();
				}
			}
		} else {
			average = 0.0;
		}

		zoneToWrite.setCount(average.toString());
		zoneToWrite.setTime(maTime);
		return zoneToWrite;
	}

	public Zone zoneWeightedAverage(ArrayList<Zone> inputZoneArray, double[] weights, String zoneName, String zoneId) {
		Zone zoneToWrite = new Zone(zoneId, zoneName);
		ArrayList<Integer> integerList = new ArrayList<Integer>();
		Calendar waTime = null;
		Double average = 0.0;
		int n = weights.length;

		for (Zone zoneAggregate : inputZoneArray) {
			if (zoneAggregate.getZoneName().equals(zoneName)) {
				integerList.add(Integer.parseInt(zoneAggregate.getCount()));
				waTime = zoneAggregate.getTime();
			}
		}
		if (integerList.size() >= n) {
			for (int i = 1; i <= n; i++) {
				average = average + (double) weights[i - 1] * integerList.get(integerList.size() - i);
			}
		} else {
			average = 0.0;
		}
		zoneToWrite.setCount(average.toString());
		zoneToWrite.setTime(waTime);

		return zoneToWrite;
	}

	public Zone zoneExponentialSmoothing(ArrayList<ForecastData> inputZoneForecast, ArrayList<Zone> inputZoneCount,
			double alpha, String zoneId, String zoneName) {
		Zone zoneToWrite = new Zone(zoneId, zoneName);
		Calendar esTime = null;
		ArrayList<Double> forecastData = new ArrayList<>();
		ArrayList<Double> zoneData = new ArrayList<>();
		Double forecast;
		int iter = 0;

		for (Zone zoneAggregate : inputZoneCount) {
			if (zoneAggregate.getZoneName().equals(zoneName)) {
				zoneData.add((double) Integer.parseInt(zoneAggregate.getCount()));
				esTime = zoneAggregate.getTime();
			}
		}

		for (ForecastData forecastItem : inputZoneForecast) {
			if (forecastItem.getLocation().equals(zoneName)) {
				forecastData.add(Double.parseDouble(forecastItem.getEs()));
			}
		}

		if (forecastData.size() == 0) {
			forecastData.add((double) zoneData.get(zoneData.size() - 1));
		}

		if (forecastData.size() > 1 && zoneData.size() > 1) {
			forecast = forecastData.get(forecastData.size() - 1)
					+ alpha * (zoneData.get(zoneData.size() - 1) - forecastData.get(forecastData.size() - 1));
		} else {
			forecast = forecastData.get(0);
		}

		zoneToWrite.setCount(forecast.toString());
		zoneToWrite.setTime(esTime);
		return zoneToWrite;
	}
	
	//BUILDINGFORECASTER
	public Building buildingMovingAverage(ArrayList<Building> inputBuildingArray, int step, String buildingId, String buildingName) {
		Building buildingToWrite = new Building(buildingId, buildingName);
		ArrayList<Integer> integerList = new ArrayList<Integer>();
		Calendar maTime = null;
		Double average = 0.0;

		for (Building buildingAggregate : inputBuildingArray) {
			if (buildingAggregate.getBuildingName().equals(buildingName)) {
				integerList.add(Integer.parseInt(buildingAggregate.getCount()));
				maTime = buildingAggregate.getTime();
			}
		}

		HashMap<String, Double> buildingForecastKey = new HashMap<String, Double>();

		DescriptiveStatistics stats = new DescriptiveStatistics();
		stats.setWindowSize(step);

		if (integerList.size() >= step) {
			long nLines = 0;
			for (int d : integerList) {
				stats.addValue((double) d);
				nLines++;
				if (nLines >= step) {
					average = stats.getMean();
				}
			}
		} else {
			average = 0.0;
		}

		buildingToWrite.setCount(average.toString());
		buildingToWrite.setTime(maTime);
		return buildingToWrite;
	}
	
	public Building buildingWeightedAverage(ArrayList<Building> inputBuildingArray, double[] weights, String buildingName, String buildingId) {
		Building buildingToWrite = new Building(buildingId, buildingName);
		ArrayList<Integer> integerList = new ArrayList<Integer>();
		Calendar waTime = null;
		Double average = 0.0;
		int n = weights.length;

		for (Building buildingAggregate : inputBuildingArray) {
			if (buildingAggregate.getBuildingName().equals(buildingName)) {
				integerList.add(Integer.parseInt(buildingAggregate.getCount()));
				waTime = buildingAggregate.getTime();
			}
		}
		if (integerList.size() >= n) {
			for (int i = 1; i <= n; i++) {
				average = average + (double) weights[i - 1] * integerList.get(integerList.size() - i);
			}
		} else {
			average = 0.0;
		}
		buildingToWrite.setCount(average.toString());
		buildingToWrite.setTime(waTime);

		return buildingToWrite;
	}
	
}