package xyz.crossward.repository

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import xyz.crossward.entities.IsMember
import xyz.crossward.entities.IsMemberId
import java.util.stream.Stream

@Repository
interface IsMemberRepository : CrudRepository<IsMember, IsMemberId>