package com.app.covidtracker.serviceImpl;

import com.app.covidtracker.domain.ScheduledTasks;
import com.app.covidtracker.service.GlobalStatsService;
import com.app.covidtracker.service.IndianStatsService;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class schedulerService {

    @Autowired
    private GlobalStatsService globalStatsService;

    @Autowired
    private IndianStatsService indianStatsService;


    // Update every 1 hour
    @Scheduled(cron = "0 0 0/8 * * *")
    @SchedulerLock(name = ScheduledTasks.COUNTRY_STATS, lockAtLeastFor = "PT2M", lockAtMostFor = "PT4M")
    public void updateCountryStats() {
        log.info("Starting scheduled job: Updating country stats");
        globalStatsService.updateAllCountryStats();
        log.info("Finishing scheduled job: Updating country stats");
    }

    // Update every 8 hours after country stats are updated. Hence the difference of 5 minutes,
    @Scheduled(cron = "0 5 0/8 * * *")
    @SchedulerLock(name = ScheduledTasks.GLOBAL_STATS, lockAtLeastFor = "PT1M", lockAtMostFor = "PT2M")
    public void updateGlobalStats() {
        log.info("Starting scheduled job: Updating global stats");
        globalStatsService.updateGlobalStats();
        log.info("Finishing scheduled job: Updating global stats");
    }
}
