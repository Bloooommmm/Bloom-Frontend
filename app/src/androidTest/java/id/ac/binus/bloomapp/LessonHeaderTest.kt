package id.ac.binus.bloomapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import id.ac.binus.bloomapp.data.network.Lesson
import id.ac.binus.bloomapp.screens.lessons.LessonDetail
import id.ac.binus.bloomapp.ui.theme.BloomAppTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class LessonHeaderTest {
    @get:Rule val compose = createComposeRule()

    @Test fun headerStaysVisibleAndBackWorksAfterScrolling() {
        var wentBack = false
        val content = (1..20).joinToString("\n") { "## Section $it\n" + "A gentle lesson about your cycle. ".repeat(10) }
        compose.setContent {
            BloomAppTheme { LessonDetail(Lesson("test", "Period guide", content, 10)) { wentBack = true } }
        }
        val back = compose.onNodeWithContentDescription("Back to Learn")
        val before = back.fetchSemanticsNode().boundsInRoot
        val sectionBefore = compose.onNodeWithText("Section 1").fetchSemanticsNode().boundsInRoot
        compose.onNode(hasScrollAction()).performTouchInput { swipeUp() }
        compose.waitForIdle()
        assertTrue(compose.onNodeWithText("Section 1").fetchSemanticsNode().boundsInRoot.top < sectionBefore.top)
        assertEquals(before, back.assertIsDisplayed().fetchSemanticsNode().boundsInRoot)
        compose.onNodeWithText("Learn").assertIsDisplayed()
        back.performClick()
        compose.runOnIdle { assertTrue(wentBack) }
    }
}
