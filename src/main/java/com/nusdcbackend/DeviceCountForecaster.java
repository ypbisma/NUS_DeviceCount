package com.nusdcbackend;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import net.sourceforge.openforecast.DataPoint;
import net.sourceforge.openforecast.DataSet;
import net.sourceforge.openforecast.ForecastingModel;
import net.sourceforge.openforecast.Observation;
import net.sourceforge.openforecast.models.WeightedMovingAverageModel;

public class DeviceCountForecaster {

	public Zone movingAverage(ArrayList<Zone> inputZoneArray, int step, String zoneId, String zoneName) {
		Zone zoneToWrite = new Zone(zoneId, zoneName);
		ArrayList<Integer> integerList = new ArrayList<Integer>();
		Calendar time = null;
		Double average = 0.0;

		for (Zone zoneAggregate : inputZoneArray) {
			if (zoneAggregate.getZoneName().equals(zoneName)) {
				integerList.add(Integer.parseInt(zoneAggregate.getCount()));
				time = zoneAggregate.getTime();
			}
		}

		HashMap<String, Double> zoneForecastKey = new HashMap<String, Double>();

		DescriptiveStatistics stats = new DescriptiveStatistics();
		stats.setWindowSize(step);

		long nLines = 0;
		for (int d : integerList) {
			stats.addValue((double) d);
			nLines++;
			if (nLines >= step) {
				average = stats.getMean();
			}
		}
		zoneToWrite.setCount(average.toString());
		zoneToWrite.setTime(this.addTenMinutes(time));
		return zoneToWrite;
	}

	public Zone weightedAverage(ArrayList<Zone> inputZoneArray, double[] weights, String zoneName, String zoneId) {
		Zone zoneToWrite = new Zone(zoneId, zoneName);
		ArrayList<Integer> integerList = new ArrayList<Integer>();
		Calendar time = null;
		Double average = 0.0;
		int n = weights.length;

		for (Zone zoneAggregate : inputZoneArray) {
			if (zoneAggregate.getZoneName().equals(zoneName)) {
				integerList.add(Integer.parseInt(zoneAggregate.getCount()));
				time = zoneAggregate.getTime();
			}
		}

		for (int i = 1; i <= n; i++) {
			average = average + (double) weights[i - 1] * integerList.get(integerList.size() - i);
		}
		zoneToWrite.setCount(average.toString());
		zoneToWrite.setTime(this.addTenMinutes(time));
		return zoneToWrite;
	}
	
	public Zone exponentialSmoothing(ArrayList<Zone> inputZoneArray, double[] weights, String zoneName, String zoneId) {


	private Calendar addTenMinutes(Calendar time) {
		time.add(Calendar.MINUTE, 10);
		return time;
	}
}