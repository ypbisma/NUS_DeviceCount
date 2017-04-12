//**NUSWATCH-DEVICECOUNT**
//**A FINAL YEAR PROJECT**
//**BY YOHANES PAULUS BISMA**
//**A0115902N**
//**INDUSTRIAL SYSTEMS ENGINEERING & MANAGEMENT**
//**2016/2017**


//This class is designed to contain the information from the building device count database
package com.nusdcbackend;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Building {
	
	private String buildingId;
	private String buildingName;
	private String zoneId;
	private String count;
	private String calendar;
	private Calendar time;
	
	public Building(String buildingId, String buildingName, String zoneId){
		this.buildingId = buildingId;
		this.buildingName = buildingName;
		this.zoneId = zoneId;
	}
	
	public Building(String buildingId, String buildingName){
		this.buildingId = buildingId;
		this.buildingName = buildingName;
	}
	
	public Building(String buildingId, String buildingName, String count, String timeString, String dateString){
		this.buildingId = buildingId;
		this.buildingName = buildingName;
		this.count = count;
		this.calendar = dateString + " " + timeString;
		setTime(this.stringToCalendar(calendar));
	}
	
	public String getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	
	//stringToCalendar takes in the time in string format and convert it to a Calendar object
	private Calendar stringToCalendar(String timeString) {
		Calendar time = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
		try {
			time.setTime(sdf.parse(timeString));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time;
	}

	public Calendar getTime() {
		return time;
	}

	public void setTime(Calendar time) {
		this.time = time;
	}
}
