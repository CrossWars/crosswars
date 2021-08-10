package xyz.crosswars.service

import org.springframework.stereotype.Service
import xyz.crosswars.exception.BadRequestException
import xyz.crosswars.repository.EntryRepository
import xyz.crosswars.repository.UserRepository

@Service
class StatsService(
    private val entryRepository: EntryRepository,
    private val userRepository: UserRepository
) {
    fun getAverageTimeByUserId(userId: String): Double {
        if (!userRepository.existsById(userId)) {
            throw BadRequestException("A user with ID $userId does not exist")
        }
        val average = entryRepository.getTimesByUserId(userId).toList().average()
        if (average.isNaN()) {
            throw BadRequestException("No entries found for user with ID $userId")
        }
        return average
    }

    fun getBestTimeByUserId(userId: String): Int {
        if (!userRepository.existsById(userId)) {
            throw BadRequestException("A user with ID $userId does not exist")
        }
        return entryRepository.getTimesByUserId(userId).toList()?.minOrNull()
            ?: throw BadRequestException("No entries found for user with ID: $userId")
    }
}
