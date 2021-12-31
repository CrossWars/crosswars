package xyz.crosswars.service

import org.springframework.stereotype.Service
import xyz.crosswars.entities.IsMemberId
import xyz.crosswars.entities.WinCount
import xyz.crosswars.repository.IsMemberRepository
import xyz.crosswars.repository.checkIfMemberOf

@Service
class IsMemberService(
        val isMemberRepository: IsMemberRepository
) {
    fun getIsMember(userId: String, groupId: String): Boolean {
        val memberId = IsMemberId(userId, groupId)
        return isMemberRepository.existsById(memberId)
    }

}