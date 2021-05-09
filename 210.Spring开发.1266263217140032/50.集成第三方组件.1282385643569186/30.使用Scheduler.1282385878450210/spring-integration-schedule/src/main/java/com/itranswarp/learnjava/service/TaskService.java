package com.itranswarp.learnjava.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TaskService {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Scheduled(initialDelay = 60_000, fixedRate = 60_000)
	public void checkSystemStatusEveryMinute() {
		logger.info("Start check system status...");
		if (Math.random() > 0.8) {
			throw new RuntimeException("check system status task failed.");
		}
	}

	@Scheduled(initialDelay = 30_000, fixedDelayString = "${task.checkDiskSpace:30000}")
	public void checkDiskSpaceEveryMinute() {
		logger.info("Start check disk space...");
	}

	@Scheduled(cron = "${task.report:0 15 2 * * *}")
	public void cronDailyReport() {
		logger.info("Start daily report task...");
	}

	@Scheduled(cron = "${task.weekday:0 0 12 * * MON-FRI}")
	public void cronWeekdayTask() {
		logger.info("Start weekday task...");
	}
}
