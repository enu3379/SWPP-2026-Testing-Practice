package com.example.swpp.tododapp.tasks

import android.view.View
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.swpp.tododapp.data.Task
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@SmallTest
@RunWith(AndroidJUnit4::class)
class TasksListBindingsTest {

    private lateinit var textView: TextView

    @Before
    fun createTextView() {
        runOnMain {
            textView = TextView(InstrumentationRegistry.getInstrumentation().targetContext)
        }
    }

    // Success cases

    @Test
    fun setDueDateLabel_activeDueIn3Days_showsDMinus3() {
        // GIVEN - An active task due in 3 days
        val task = Task("Title", "Desc", false, dueDate = LocalDate.now().plusDays(3))

        // WHEN - Bind the label
        bind(task)

        // THEN - D-3 is shown
        assertShows("D-3")
    }

    @Test
    fun setDueDateLabel_activeDueToday_showsDDay() {
        // GIVEN - An active task due today
        val task = Task("Title", "Desc", false, dueDate = LocalDate.now())

        // WHEN - Bind the label
        bind(task)

        // THEN - D-Day is shown
        assertShows("D-Day")
    }

    @Test
    fun setDueDateLabel_activeOverdueBy3Days_showsDPlus3() {
        // GIVEN - An active task overdue by 3 days
        val task = Task("Title", "Desc", false, dueDate = LocalDate.now().minusDays(3))

        // WHEN - Bind the label
        bind(task)

        // THEN - D+3 is shown
        assertShows("D+3")
    }

    @Test
    fun setDueDateLabel_completedWithDueDate_showsClear() {
        // GIVEN - A completed task due in 3 days
        val task = Task("Title", "Desc", true, dueDate = LocalDate.now().plusDays(3))

        // WHEN - Bind the label
        bind(task)

        // THEN - CLEAR is shown
        assertShows("CLEAR")
    }

    @Test
    fun setDueDateLabel_completedNoDueDate_showsClear() {
        // GIVEN - A completed task without a due date
        val task = Task("Title", "Desc", true, dueDate = null)

        // WHEN - Bind the label
        bind(task)

        // THEN - CLEAR is shown
        assertShows("CLEAR")
    }

    @Test
    fun setDueDateLabel_activeNoDueDate_showsNothing() {
        // GIVEN - An active task without a due date
        val task = Task("Title", "Desc", false, dueDate = null)

        // WHEN - Bind the label
        bind(task)

        // THEN - No label is shown
        assertShowsNothing()
    }

    @Test
    fun setDueDateLabel_nullTask_showsNothing() {
        // GIVEN - No task

        // WHEN - Bind a null task
        bind(null)

        // THEN - No label is shown
        assertShowsNothing()
    }

    // Edge cases

    @Test
    fun setDueDateLabel_completeThenUncheckNoDueDate_clearsLabel() {
        // GIVEN - A completed task without a due date is shown as CLEAR
        val task = Task("Title", "Desc", true, dueDate = null)
        bind(task)
        assertShows("CLEAR")

        // WHEN - The task is unchecked and the same view is rebound
        task.isCompleted = false
        bind(task)

        // THEN - The stale CLEAR label is gone
        assertShowsNothing()
    }

    @Test
    fun setDueDateLabel_completeThenUncheckWithDueDate_showsDMinus3() {
        // GIVEN - A completed task due in 3 days is shown as CLEAR
        val task = Task("Title", "Desc", true, dueDate = LocalDate.now().plusDays(3))
        bind(task)
        assertShows("CLEAR")

        // WHEN - The task is unchecked and the same view is rebound
        task.isCompleted = false
        bind(task)

        // THEN - The D-day label comes back
        assertShows("D-3")
    }

    @Test
    fun setDueDateLabel_activeThenComplete_showsClear() {
        // GIVEN - An active task due in 3 days is shown as D-3
        val task = Task("Title", "Desc", false, dueDate = LocalDate.now().plusDays(3))
        bind(task)
        assertShows("D-3")

        // WHEN - The task is completed and the same view is rebound
        task.isCompleted = true
        bind(task)

        // THEN - CLEAR replaces the D-day label
        assertShows("CLEAR")
    }

    @Test
    fun setDueDateLabel_recycledFromLabelToNoLabel_showsNothing() {
        // GIVEN - The view shows D-1 for task A
        bind(Task("A", "Desc", false, dueDate = LocalDate.now().plusDays(1)))
        assertShows("D-1")

        // WHEN - The same view is rebound to task B without a due date
        bind(Task("B", "Desc", false, dueDate = null))

        // THEN - No label is shown
        assertShowsNothing()
    }

    @Test
    fun setDueDateLabel_recycledFromNoLabelToLabel_showsDMinus1() {
        // GIVEN - The view shows nothing for task A without a due date
        bind(Task("A", "Desc", false, dueDate = null))
        assertShowsNothing()

        // WHEN - The same view is rebound to task B due tomorrow
        bind(Task("B", "Desc", false, dueDate = LocalDate.now().plusDays(1)))

        // THEN - D-1 is shown
        assertShows("D-1")
    }

    private fun bind(task: Task?) {
        runOnMain { setDueDateLabel(textView, task) }
    }

    private fun assertShows(expected: String) {
        assertEquals(expected, textView.text.toString())
        assertEquals(View.VISIBLE, textView.visibility)
    }

    private fun assertShowsNothing() {
        val hidden = textView.visibility != View.VISIBLE
        val empty = textView.text.isNullOrEmpty()
        assertTrue(
            "Expected no label but saw \"${textView.text}\" (visibility=${textView.visibility})",
            hidden || empty
        )
    }

    private fun runOnMain(block: () -> Unit) {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(block)
    }
}
