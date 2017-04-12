//**NUSWATCH-DEVICECOUNT**
//**A FINAL YEAR PROJECT**
//**BY YOHANES PAULUS BISMA**
//**A0115902N**
//**INDUSTRIAL SYSTEMS ENGINEERING & MANAGEMENT**
//**2016/2017**

package com.nusdcbackend;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class DeviceCountForecaster {

	// ZONEFORECASTER
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

		if (forecastData.size() >= 1 && zoneData.size() >= 1) {
			forecast = forecastData.get(forecastData.size() - 1)
					+ alpha * (zoneData.get(zoneData.size() - 2) - forecastData.get(forecastData.size() - 1));

		} else {
			forecastData.add((double) zoneData.get(zoneData.size() - 1));
			forecast = forecastData.get(0);
		}

		zoneToWrite.setCount(forecast.toString());
		zoneToWrite.setTime(esTime);
		return zoneToWrite;
	}

	// BUILDINGFORECASTER
	public Building buildingMovingAverage(ArrayList<Building> inputBuildingArray, int step, String buildingId,
			String buildingName) {
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

	public Building buildingWeightedAverage(ArrayList<Building> inputBuildingArray, double[] weights,
			String buildingName, String buildingId) {
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
	
	public Building buildingExponentialSmoothing(ArrayList<ForecastData> inputBuildingForecast, ArrayList<Building> inputBuildingCount,
			double alpha, String buildingId, String buildingName) {
		Building buildingToWrite = new Building(buildingId, buildingName);
		Calendar esTime = null;
		ArrayList<Double> forecastData = new ArrayList<>();
		ArrayList<Double> buildingData = new ArrayList<>();
		Double forecast;

		for (Building buildingAggregate : inputBuildingCount) {
			if (buildingAggregate.getBuildingName().equals(buildingName)) {
				buildingData.add((double) Integer.parseInt(buildingAggregate.getCount()));
				esTime = buildingAggregate.getTime();
			}
		}

		for (ForecastData forecastItem : inputBuildingForecast) {
			if (forecastItem.getLocation().equals(buildingName)) {
				forecastData.add(Double.parseDouble(forecastItem.getEs()));
			}
		}
		
		
		if (forecastData.size() >= 1 && buildingData.size() >= 1) {
			forecast = forecastData.get(forecastData.size() - 1)
					+ alpha * (buildingData.get(buildingData.size() - 2) - forecastData.get(forecastData.size() - 1));

		} else {
			forecastData.add((double) buildingData.get(buildingData.size() - 1));
			forecast = forecastData.get(0);
		}
		buildingToWrite.setCount(forecast.toString());
		buildingToWrite.setTime(esTime);
		return buildingToWrite;
	}

	// UNIFORECASTER
	public Uni uniMovingAverage(ArrayList<Uni> inputUniArray, int step, String uniId, String uniName) {
		Uni uniToWrite = new Uni(uniId, uniName);
		ArrayList<Integer> integerList = new ArrayList<Integer>();
		Calendar maTime = null;
		Double average = 0.0;

		for (Uni uniAggregate : inputUniArray) {
			if (uniAggregate.getUniName().equals(uniName)) {
				integerList.add(Integer.parseInt(uniAggregate.getCount()));
				maTime = uniAggregate.getTime();
			}
		}

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

		uniToWrite.setCount(average.toString());
		uniToWrite.setTime(maTime);
		return uniToWrite;
	}

	public Uni uniWeightedAverage(ArrayList<Uni> inputUniArray, double[] weights, String uniName, String uniId) {
		Uni uniToWrite = new Uni(uniId, uniName);
		ArrayList<Integer> integerList = new ArrayList<Integer>();
		Calendar waTime = null;
		Double average = 0.0;
		int n = weights.length;

		for (Uni uniAggregate : inputUniArray) {
			if (uniAggregate.getUniName().equals(uniName)) {
				integerList.add(Integer.parseInt(uniAggregate.getCount()));
				waTime = uniAggregate.getTime();
			}
		}
		if (integerList.size() >= n) {
			for (int i = 1; i <= n; i++) {
				average = average + (double) weights[i - 1] * integerList.get(integerList.size() - i);
			}
		} else {
			average = 0.0;
		}
		uniToWrite.setCount(average.toString());
		uniToWrite.setTime(waTime);

		return uniToWrite;
	}

	public Uni uniExponentialSmoothing(ArrayList<ForecastData> inputUniForecast, ArrayList<Uni> inputUniCount,
			double alpha, String uniId, String uniName) {
		Uni uniToWrite = new Uni(uniId, uniName);
		Calendar esTime = null;
		ArrayList<Double> forecastData = new ArrayList<>();
		ArrayList<Double> uniData = new ArrayList<>();
		Double forecast;

		for (Uni uniAggregate : inputUniCount) {
			if (uniAggregate.getUniName().equals(uniName)) {
				uniData.add((double) Integer.parseInt(uniAggregate.getCount()));
				esTime = uniAggregate.getTime();
			}
		}

		for (ForecastData forecastItem : inputUniForecast) {

			if (forecastItem.getLocation().equals(uniName)) {
				forecastData.add(Double.parseDouble(forecastItem.getEs()));
			}
		}

		if (forecastData.size() >= 1 && uniData.size() >= 1) {
			forecast = forecastData.get(forecastData.size() - 1)
					+ alpha * (uniData.get(uniData.size() - 2) - forecastData.get(forecastData.size() - 1));

		} else {
			forecastData.add((double) uniData.get(uniData.size() - 1));
			forecast = forecastData.get(0);
		}

		uniToWrite.setCount(forecast.toString());
		uniToWrite.setTime(esTime);
		return uniToWrite;
	}
}