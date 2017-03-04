package com.nusdcbackend;

public class Floor {
	
	public Floor(String floorId, String floorName, String buildingId){
		this.floorId = floorId;
		this.floorName = floorName;
		this.buildingId = buildingId;
	}
	String floorId;
	String floorName;
	String buildingId;
	public String getFloorId() {
		return floorId;
	}
	public void setFloorId(String floorId) {
		this.floorId = floorId;
	}
	public String getFloorName() {
		return floorName;
	}
	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}
	public String getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}
	
	
}
