package xyz.crosswars.service

import org.springframework.stereotype.Service
import xyz.crosswars.entities.IsMemberId
import xyz.crosswars.entities.Win
import xyz.crosswars.entities.WinCount
import xyz.crosswars.entities.WinId
import xyz.crosswars.exception.NoContentException
import xyz.crosswars.repository.*
import xyz.crosswars.util.getPuzzleDateInEST
import xyz.crosswars.util.unwrap

@Service
class WinService(
    private val winRepository: WinRepository,
    private val isMemberRepository: IsMemberRepository,
    private val groupRepository: GroupRepository
) {
    fun validateWin(win: Win) {
        groupRepository.checkIfGroupExists(win.groupId)
        isMemberRepository.checkIfMemberOf(IsMemberId(win.userId, win.groupId))
    }

    fun recordWin(win: Win): Win {
        validateWin(win)
        return winRepository.save(win)
    }

    fun getWins(win: Win): Win =
        winRepository.findById(WinId(win.userId, win.groupId, win.date)).unwrap()
            ?: throw NoContentException("No wins could be found for user id ${win.userId} in group id ${win.groupId} and date ${win.date}")

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
}
