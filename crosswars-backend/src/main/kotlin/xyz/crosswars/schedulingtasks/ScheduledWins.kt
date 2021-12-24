package xyz.crosswars.schedulingtasks

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import xyz.crosswars.service.GroupService
import xyz.crosswars.service.WinService

@Component
class ScheduledWins(private val winService: WinService, private val groupService: GroupService) {

        //At 10PM EST on weekdays
        @Transactional
        @Scheduled(cron = "0 0 22 * * MON-FRI", zone="EST")
        fun recordWeekWins() {
                winService.recordWinsForAllGroups()
        }
        //At 6PM EST on weekends
        @Transactional
        @Scheduled(cron = "0 0 18 * * SAT-SUN", )
        fun recordWeekendWins() {
                winService.recordWinsForAllGroups()
        }
}
