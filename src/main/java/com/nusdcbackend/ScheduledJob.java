package com.nusdcbackend;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.nusdcbackend.JobDeviceCount;
import com.nusdcbackend.LoginManager;

public class ScheduledJob implements org.quartz.Job {
	String token;
	private Calendar cal = Calendar.getInstance();
	private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

	public ScheduledJob() {
	}

	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {

			// login and obtain token
			LoginManager loginManager = new LoginManager();
			loginManager.login();
			token = loginManager.getToken();
			System.out.println(sdf.format(cal.getTime()));

			JobForecast jobForecast = new JobForecast();
			jobForecast.forecastZone();
			
//			JobDeviceCount jobDeviceCount = new JobDeviceCount(token);
//			jobDeviceCount.execute();
			
			
	

		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//
		}
	}
}
