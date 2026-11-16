package id.ac.binus.bloomapp

import id.ac.binus.bloomapp.data.network.Lesson
import id.ac.binus.bloomapp.screens.lessons.lessonIllustrations
import id.ac.binus.bloomapp.screens.lessons.parseSections
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class LessonsContentTest {
    @Test
    fun articleSectionsKeepFourHeadingsAndDistinctIllustrations() {
        val content = "## One\nfirst\n## Two\nsecond\n## Three\nthird\n## Four\nfourth"
        val sections = parseSections(content)
        val images = lessonIllustrations(Lesson("1", "Period Cramps 101", content, 1))
        assertEquals(4, sections.size)
        assertEquals(4, images.distinct().size)
        assertNotEquals(sections[0].heading, sections[1].heading)
    }
}
