package xyz.crosswars

import io.kotest.matchers.shouldBe
import io.mockk.mockk
import org.junit.jupiter.api.Test
import xyz.crosswars.repository.EntryRepository
import xyz.crosswars.service.EntryService
import java.time.ZonedDateTime

class EntryServiceTest {

    private val entryRepository = mockk<EntryRepository> {}
    private val entryService = EntryService(entryRepository)

    @Test
    fun `get puzzle date when date is mon-fri`() {
        val mon5pm = ZonedDateTime.parse("2021-06-28T17:00:00-04:00[US/Eastern]")
        val tus5pm = ZonedDateTime.parse("2021-06-29T17:00:00-04:00[US/Eastern]")
        val wed5pm = ZonedDateTime.parse("2021-06-30T17:00:00-04:00[US/Eastern]")
        val thu5pm = ZonedDateTime.parse("2021-07-01T17:00:00-04:00[US/Eastern]")
        val fri5pm = ZonedDateTime.parse("2021-07-02T17:00:00-04:00[US/Eastern]")

        // puzzle date should be current date
        entryService.getPuzzleDate(mon5pm) shouldBe mon5pm.toLocalDate()
        entryService.getPuzzleDate(tus5pm) shouldBe tus5pm.toLocalDate()
        entryService.getPuzzleDate(wed5pm) shouldBe wed5pm.toLocalDate()
        entryService.getPuzzleDate(thu5pm) shouldBe thu5pm.toLocalDate()
        entryService.getPuzzleDate(fri5pm) shouldBe fri5pm.toLocalDate()

        val mon11pm = ZonedDateTime.parse("2021-06-28T23:00:00-04:00[US/Eastern]")
        val tus11pm = ZonedDateTime.parse("2021-06-29T23:00:00-04:00[US/Eastern]")
        val wed11pm = ZonedDateTime.parse("2021-06-30T23:00:00-04:00[US/Eastern]")
        val thu11pm = ZonedDateTime.parse("2021-07-01T23:00:00-04:00[US/Eastern]")
        val fri11pm = ZonedDateTime.parse("2021-07-02T23:00:00-04:00[US/Eastern]")

        // puzzle date should be current date + 1
        entryService.getPuzzleDate(mon11pm) shouldBe mon11pm.toLocalDate().plusDays(1)
        entryService.getPuzzleDate(tus11pm) shouldBe tus11pm.toLocalDate().plusDays(1)
        entryService.getPuzzleDate(wed11pm) shouldBe wed11pm.toLocalDate().plusDays(1)
        entryService.getPuzzleDate(thu11pm) shouldBe thu11pm.toLocalDate().plusDays(1)
        entryService.getPuzzleDate(fri11pm) shouldBe fri11pm.toLocalDate().plusDays(1)
    }

    @Test
    fun `get puzzle date when date is sat-sun`() {
        val sat5pm = ZonedDateTime.parse("2021-07-03T17:00:00-04:00[US/Eastern]")
        val sun5pm = ZonedDateTime.parse("2021-07-04T17:00:00-04:00[US/Eastern]")

        // puzzle date should be current date
        entryService.getPuzzleDate(sat5pm) shouldBe sat5pm.toLocalDate()
        entryService.getPuzzleDate(sun5pm) shouldBe sun5pm.toLocalDate()

        val sat7pm = ZonedDateTime.parse("2021-07-03T19:00:00-04:00[US/Eastern]")
        val sun7pm = ZonedDateTime.parse("2021-07-04T19:00:00-04:00[US/Eastern]")

        // puzzle date should be current date
        entryService.getPuzzleDate(sat7pm) shouldBe sat7pm.toLocalDate().plusDays(1)
        entryService.getPuzzleDate(sun7pm) shouldBe sun7pm.toLocalDate().plusDays(1)
    }

    @Test
    fun `daylight savings is accounted for when creating an entry`() {
        val beforeDst = ZonedDateTime.parse("2021-03-13T19:00:00-05:00[US/Eastern]") // Saturday March 13th 7pm
        val afterDst = beforeDst.plusHours(24) // Sunday March 14th 7pm

        entryService.getPuzzleDate(beforeDst) shouldBe beforeDst.toLocalDate().plusDays(1)
        entryService.getPuzzleDate(afterDst) shouldBe afterDst.toLocalDate().plusDays(1)
    }
}