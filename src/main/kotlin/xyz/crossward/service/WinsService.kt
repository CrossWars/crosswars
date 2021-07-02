package xyz.crossward.service

import org.springframework.stereotype.Service
import xyz.crossward.entities.Wins
import xyz.crossward.entities.WinsId
import xyz.crossward.exception.NoContentException
import xyz.crossward.repository.WinsRepository
import xyz.crossward.util.unwrap

@Service
class WinsService(
    private val winsRepository: WinsRepository
) {
    fun recordWin(wins: Wins): Wins =
        winsRepository.findById(WinsId(wins.userId, wins.groupId)).unwrap().let { winRecord ->
            return if (winRecord == null) {
                winsRepository.save(Wins(wins.userId, wins.groupId, 1))
            } else {
                winsRepository.addWin(winRecord.userId, winRecord.groupId)
                Wins(winRecord.userId, winRecord.groupId, winRecord.wins + 1)
            }
        }

    fun getWins(userId: String, groupId: String): Wins =
        winsRepository.findById(WinsId(userId, groupId)).unwrap()
            ?: throw NoContentException("No wins could be found for user id $userId in group id $groupId")

    fun updateWinsRecord(wins: Wins): Wins =
        winsRepository.save(wins)
}
