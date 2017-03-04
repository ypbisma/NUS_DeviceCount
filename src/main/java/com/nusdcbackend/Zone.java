package com.nusdcbackend;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Zone {
	
	public Zone(String zoneId, String zoneName, String timeString){
		this.zoneId = zoneId;
		this.zoneName = zoneName;
		this.timeString = timeString;
		time = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
		try {
			time.setTime(sdf.parse(timeString));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	private String zoneId;
	private String zoneName;
	private String count;
	private String timeString;
	private Calendar time;
	
	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
	public String getZoneName() {
		return zoneName;
	}
	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
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
}
