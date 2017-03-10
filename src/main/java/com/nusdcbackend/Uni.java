package com.nusdcbackend;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Uni {
	private String uniId;
	private String uniName;
	private String count;
	private String calendar;
	private Calendar time;

	public Uni(String uniId, String uniName) {
		this.setUniId(uniId);
		this.setUniName(uniName);
	}

	public Uni(String uniId, String uniName, String count, String timeString, String dateString) {
		this.setUniId(uniId);
		this.setUniName(uniName);
		this.count = count;
		this.calendar = dateString + " " + timeString;
		time = this.stringToCalendar(calendar);
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public Calendar getTime() {
		return time;
	}

	public void setTime(Calendar time) {
		this.time = time;
	}

	private Calendar stringToCalendar(String timeString) {
		Calendar time = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.ENGLISH);
		try {
			time.setTime(sdf.parse(timeString));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time;
	}

	public String getUniId() {
		return uniId;
	}

	public void setUniId(String uniId) {
		this.uniId = uniId;
	}

	public String getUniName() {
		return uniName;
	}

	public void setUniName(String uniName) {
		this.uniName = uniName;
	}
}
