package com.nusdcbackend;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class JobDeviceCount {
	private DeviceCountManager deviceCountManager;
	private ArrayList<ZoneBuildingFloor> zoneBuildingFloorList = new ArrayList<>();
	private ArrayList<Zone> zoneList = new ArrayList<>();
	private ArrayList<Building> buildingList = new ArrayList<>();
	private ArrayList<Floor> floorList = new ArrayList<>();
	private DeviceCountDatabaseManager deviceCountDatabaseManager = new DeviceCountDatabaseManager();
	private ZoneBuildingFloorDatabaseManager zoneBuildingFloorDatabaseManager;
	private Calendar cal = Calendar.getInstance();
	private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

	private Integer zoneSum;
	private Integer buildingSum;
	private Integer uniSum;

	HashMap<String, String> buildingMap = new HashMap<String, String>();
	HashMap<String, String> zoneMap = new HashMap<String, String>();

	public JobDeviceCount(String token) throws Exception {
		deviceCountManager = new DeviceCountManager(token);
		zoneBuildingFloorDatabaseManager = new ZoneBuildingFloorDatabaseManager(token);
	}

	public void execute() throws Exception {
		String executeTime = sdf.format(cal.getTime());

		zoneBuildingFloorList = zoneBuildingFloorDatabaseManager.getZoneBuildingFloor();
		zoneList = zoneBuildingFloorDatabaseManager.getZones();
		buildingList = zoneBuildingFloorDatabaseManager.getBuildings();
		floorList = zoneBuildingFloorDatabaseManager.getFloors();

		uniSum = 0;
		for (ZoneBuildingFloor item : zoneBuildingFloorList) {
			deviceCountManager.setZoneName(item.getZone());
			deviceCountManager.setBuildingName(item.getBuilding());
			deviceCountManager.setFloorName(item.getFloor());
			deviceCountManager.syncDeviceCount();
			deviceCountDatabaseManager.insertDeviceCount(deviceCountManager.getZoneName(),
					deviceCountManager.getBuildingName(), deviceCountManager.getFloorName(),
					deviceCountManager.getDeviceCount().getCount().toString(), executeTime);
			item.setCount(deviceCountManager.getDeviceCount().getCount().toString());

			if (buildingMap.containsKey(item.getBuilding())) {
				Integer newBuildingSum = Integer.parseInt(buildingMap.get(item.getBuilding())) + Integer.parseInt(item.getCount());
				buildingMap.put(item.getBuilding(), newBuildingSum.toString());
			} else {
				buildingMap.put(item.getBuilding(), item.getCount());
			}

			if (zoneMap.containsKey(item.getZone())) {
				Integer newZoneSum = Integer.parseInt(zoneMap.get(item.getZone())) + Integer.parseInt(item.getCount());
				zoneMap.put(item.getZone(), newZoneSum.toString());
			} else {
				zoneMap.put(item.getZone(), item.getCount());
			}

			uniSum = uniSum + Integer.parseInt(item.getCount());
		}

		for (String building : buildingMap.keySet()) {
			deviceCountDatabaseManager.insertBuildingAggregate(this.getBuildingId(building, buildingList), building,
					buildingMap.get(building), executeTime);
		}

		for (String zone : zoneMap.keySet()) {
			deviceCountDatabaseManager.insertZoneAggregate(this.getZoneId(zone, zoneList), zone, zoneMap.get(zone),
					executeTime);
		}
		deviceCountDatabaseManager.insertUniAggregate("1", "NUS", uniSum.toString(), executeTime);
	}

	public String getBuildingId(String key, ArrayList<Building> list) {
		for (Building item : list) {
			if (item.getBuildingName().equals(key)) {
				return item.getBuildingId();
			}
		}
		return null;
	}

	public String getZoneId(String key, ArrayList<Zone> list) {
		for (Zone item : list) {
			if (item.getZoneName().equals(key)) {
				return item.getZoneId();
			}
		}
		return null;
	}
}
