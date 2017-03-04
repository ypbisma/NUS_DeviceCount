package com.nusdcbackend;

public class Zone {
	
	public Zone(String zoneId, String zoneName){
		this.zoneId = zoneId;
		this.zoneName = zoneName;
	}
	private String zoneId;
	private String zoneName;
	private String count;
	
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
}
