package xyz.crossward.service

import org.springframework.stereotype.Service
import xyz.crossward.repository.EntryRepository

@Service
class StatsService(
    private val entryRepository: EntryRepository
) {
    fun getAverageTimeByUserId(userId: String): Double {
        val times = entryRepository.findTimesByUserId(userId)
        var count = 0
        var sum = 0.0
        times.forEach {
            it?.let {
                count += 1
                sum += it
            }
        }
        return sum / count
    }

    fun getBestTimeByUserId(userId: String): Int {
        val times = entryRepository.findTimesByUserId(userId)
        var best = Int.MAX_VALUE
        times.forEach {
            it?.let {
                if (it < best) {
                    best = it
                }
            }
        }
        return best
    }
}
