package xyz.crosswars.service

import org.springframework.stereotype.Service
import xyz.crosswars.entities.*
import xyz.crosswars.exception.NoContentException
import xyz.crosswars.repository.*
import xyz.crosswars.util.getPuzzleDateInEST
import xyz.crosswars.util.currentDateInEST

@Service
class WinService(
    private val winRepository: WinRepository,
    private val isMemberRepository: IsMemberRepository,
    private val groupRepository: GroupRepository,
    private val groupService: GroupService,
    private val entryService: EntryService
) {
    fun validateWin(win: Win) {
        groupRepository.checkIfGroupExists(win.groupId)
        isMemberRepository.checkIfMemberOf(IsMemberId(win.userId, win.groupId))
    }

    fun recordWin(win: Win): Win {
        validateWin(win)
        return winRepository.save(win)
    }

    fun getWinner(groupId: String, date: String?): Winner {
        return winRepository.getWinByGroupId(groupId, date ?: getPuzzleDateInEST())?.toWinner()
            ?: throw NoContentException("No winner in group id $groupId and date $date could be found")
    }

    fun updateWinsRecord(win: Win): Win {
        validateWin(win)
        return winRepository.save(win)
    }

    fun getWinCount(userId: String, groupId: String, fromDate: String?, toDate: String?): WinCount {

        validateWin(Win(userId, groupId, getPuzzleDateInEST()))

        // only fromDate is specified
        if (fromDate != null && toDate == null) {
            val winCount = winRepository.getWinCountByDate(userId, groupId, fromDate, getPuzzleDateInEST())
            return WinCount(
                userId = userId,
                groupId = groupId,
                wins = winCount,
                fromDate = fromDate,
                toDate = null
            )
        }
        // both fromDate and toDate are specified
        else if (fromDate != null && toDate != null) {
            val winCount = winRepository.getWinCountByDate(userId, groupId, fromDate, toDate)
            return WinCount(
                userId = userId,
                groupId = groupId,
                wins = winCount,
                fromDate = fromDate,
                toDate = toDate
            )
        } else {
            // No date is specified
            val winCount = winRepository.getWinCountAllTime(userId, groupId)
            return WinCount(
                userId = userId,
                groupId = groupId,
                wins = winCount,
                fromDate = null,
                toDate = null
            )
        }
    }

    fun getWinCountsForAllUsersInGroup(groupId: String): List<WinCount> =
        winRepository.getWinCountsForAllUsersInGroup(groupId).toList()

    fun recordWinsForAllGroups() {
        val groups = groupService.findAllGroups()
        groups.forEach { group ->
            recordWinsForGroup(group)
        }
    }

    fun recordWinsForGroup(group: Group) {
        val date = currentDateInEST().toLocalDate().entryDateString()
        val entries = entryService.getEntriesByGroupAndDate(group.id, date, date)
        val winnerIds = getWinnerIdsFromEntries(entries)
        winnerIds.forEach { winnerId ->
            val win = Win(winnerId, group.id, date)
            recordWin(win)
        }
    }
    fun getWinnerIdsFromEntries(entries: List<Entry>): List<String>
    {
        //ties can lead to multiple winners
        if (entries.isEmpty()) return listOf()
        val min = entries.minOf { it.time }
        return entries.filter { it.time == min}.map { it.userId}
    }
}
