package com.nusdcbackend;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;

import static org.quartz.SimpleScheduleBuilder.*;

public class ProjectMain {
	private static ZoneBuildingFloorDatabaseManager zoneBuildingFloorList;
	private static LoginManager loginManager = new LoginManager();

	public static void main(String[] args) throws Exception {
		loginManager.login();
		String token = loginManager.getToken();
		zoneBuildingFloorList = new ZoneBuildingFloorDatabaseManager(token);

		zoneBuildingFloorList.emptyZoneBuildingFloorDatabase();

		if (zoneBuildingFloorList.zoneBuildingFloorIsEmpty()) {
			zoneBuildingFloorList.writeZoneBuildingFloor();
		}
		try {
			// Grab the Scheduler instance from the Factory
			Scheduler scheduler1 = StdSchedulerFactory.getDefaultScheduler();

			// define the job and tie it to our MyJob class
			JobDetail job1 = newJob(ScheduledJob.class).withIdentity("job1", "group1").build();

			// Trigger the job to run now, and then repeat every 40 seconds
			Trigger trigger1 = newTrigger().withIdentity("trigger1", "group1").startNow()
					.withSchedule(simpleSchedule().withIntervalInSeconds(120).repeatForever()).build();

			// Tell quartz to schedule the job using our trigger
			scheduler1.scheduleJob(job1, trigger1);

			// and start it off
			scheduler1.start();

		} catch (NullPointerException e) {
			System.out.println("array is empty!");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
