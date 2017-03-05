package com.nusdcbackend;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class DeviceCountForecaster {

	public Zone movingAverage(ArrayList<Zone> inputZoneArray, int step, String zoneName, String zoneId) {
		Zone zoneToWrite = new Zone(zoneName, zoneId);
		ArrayList<Integer> integerList = new ArrayList<Integer>();
		Calendar time = null;
		Double average = 0.0;
		
		for (Zone zoneAggregate : inputZoneArray) {
			if (zoneAggregate.getZoneName().equals(zoneName)){
				integerList.add(Integer.parseInt(zoneAggregate.getCount()));
				time = zoneAggregate.getTime();
			}
		}

		HashMap<String, Double> zoneForecastKey = new HashMap<String, Double>();

		DescriptiveStatistics stats = new DescriptiveStatistics();
		stats.setWindowSize(step);

		// Read data from an input stream,
		// displaying the mean of the most recent 100 observations
		// after every 100 observations
		long nLines = 0;
		for (int d : integerList) {
			stats.addValue((double) d);
			nLines++;
			if (nLines >= step) {
				average = stats.getMean();
			}
		}
		zoneToWrite.setCount(average.toString());
		zoneToWrite.setTime(time);
		return zoneToWrite;
	}
	
//	private Calendar stringToCalendar (String timeString){
//		Calendar time = Calendar.getInstance();
//		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss", Locale.ENGLISH);
//		try {
//			time.setTime(sdf.parse(timeString));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		return time;
//	}
//	
//	private String calendarToString (Calendar timeCalendar){
//		String time;
//		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
//		time = sdf.format(timeCalendar.getTime()); 
//		return time;
//	}
}
