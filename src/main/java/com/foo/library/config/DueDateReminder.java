package com.foo.library.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.foo.library.service.NotificationService;

public class DueDateReminder {
	
	@Autowired
	private NotificationService notificationService;
	
    @Scheduled(cron="${dueDateReminderCron}")
    public void reportCurrentTime() {
        notificationService.notifyUpcomingDueDates();
    }
}
