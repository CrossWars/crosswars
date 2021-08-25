package xyz.crosswars.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import xyz.crosswars.entities.IsMember
import xyz.crosswars.entities.IsMemberId
import xyz.crosswars.exception.BadRequestException

@Repository
interface IsMemberRepository : CrudRepository<IsMember, IsMemberId>

fun IsMemberRepository.checkIfMemberOf(memberId: IsMemberId) {
    if (!this.existsById(memberId)) {
        throw BadRequestException("user_id ${memberId.userId} is not a member of group_id ${memberId.groupId}")
    }
}