package com.nusdcbackend;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ForecastData {
	private String id = null;
	private String location = null;
	private String locationType = null;
	private String es = null;
	private Calendar time;

	public String getId() {
		return id;
	}

	public ForecastData(String locationType, String id, String location, String es, String time) {
		this.locationType = locationType;
		this.id = id;
		this.location = location;

		this.es = es;
		this.time = this.stringToCalendar(time);
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public String getEs() {
		return es;
	}

	public void setEs(String es) {
		this.es = es;
	}

	public Calendar getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = this.stringToCalendar(time);
	}

	private Calendar stringToCalendar(String timeString) {
		Calendar time = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss", Locale.ENGLISH);
		try {
			time.setTime(sdf.parse(timeString));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time;
	}
}
