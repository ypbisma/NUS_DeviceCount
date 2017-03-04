package com.nusdcbackend;

public class Building {
	
	public Building(String buildingId, String buildingName, String zoneId){
		this.buildingId = buildingId;
		this.buildingName = buildingName;
		this.zoneId = zoneId;
	}
	
	private String buildingId;
	private String buildingName;
	private String zoneId;
	private String count;
	
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
}
