package xyz.crosswars.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import xyz.crosswars.entities.IsMember
import xyz.crosswars.entities.IsMemberId

@Repository
interface IsMemberRepository : CrudRepository<IsMember, IsMemberId>