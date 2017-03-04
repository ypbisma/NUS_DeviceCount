package com.nusdcbackend;

import java.util.Date;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.nusdcbackend.JobDeviceCount;
import com.nusdcbackend.LoginManager;

public class ScheduledJob implements org.quartz.Job {
	String token;
	private static Date now = new Date(); // to display current time


	public ScheduledJob() {
	}

	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {

			// login and obtain token
			LoginManager loginManager = new LoginManager();
			loginManager.login();
			token = loginManager.getToken();

			JobDeviceCount jobDeviceCount = new JobDeviceCount(token);
			jobDeviceCount.execute();
			System.out.println(now);

		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//
		}
	}
}
