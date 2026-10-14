package com.example.swpp.tododapp.tasks

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class DueDateUtilsTest {

    private val today = LocalDate.of(2026, 10, 14)

    // Success cases

    @Test
    fun dueDateLabel_activeDueIn3Days_returnsDMinus3() {
        // GIVEN - An active task due on 2026-10-17
        val dueDate = LocalDate.of(2026, 10, 17)

        // WHEN - Build the label on 2026-10-14
        val label = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN - It counts 3 days left
        assertEquals("D-3", label)
    }

    @Test
    fun dueDateLabel_activeDueToday_returnsDDay() {
        // GIVEN - An active task due today
        val dueDate = LocalDate.of(2026, 10, 14)

        // WHEN - Build the label
        val label = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN - It shows D-Day
        assertEquals("D-Day", label)
    }

    @Test
    fun dueDateLabel_activeOverdueBy3Days_returnsDPlus3() {
        // GIVEN - An active task due on 2026-10-11
        val dueDate = LocalDate.of(2026, 10, 11)

        // WHEN - Build the label on 2026-10-14
        val label = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN - It is 3 days overdue
        assertEquals("D+3", label)
    }

    @Test
    fun dueDateLabel_activeNoDueDate_returnsEmpty() {
        // GIVEN - An active task without a due date
        val dueDate: LocalDate? = null

        // WHEN - Build the label
        val label = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN - No label is shown
        assertEquals("", label)
    }

    @Test
    fun dueDateLabel_completedWithDueDate_returnsClear() {
        // GIVEN - A completed task due on 2026-10-17
        val dueDate = LocalDate.of(2026, 10, 17)

        // WHEN - Build the label
        val label = dueDateLabel(dueDate, today, isCompleted = true)

        // THEN - It shows CLEAR
        assertEquals("CLEAR", label)
    }

    @Test
    fun dueDateLabel_completedOverdue_returnsClear() {
        // GIVEN - A completed task that was due on 2026-10-11
        val dueDate = LocalDate.of(2026, 10, 11)

        // WHEN - Build the label on 2026-10-14
        val label = dueDateLabel(dueDate, today, isCompleted = true)

        // THEN - It shows CLEAR instead of D+3
        assertEquals("CLEAR", label)
    }

    @Test
    fun dueDateLabel_completedNoDueDate_returnsClear() {
        // GIVEN - A completed task without a due date
        val dueDate: LocalDate? = null

        // WHEN - Build the label
        val label = dueDateLabel(dueDate, today, isCompleted = true)

        // THEN - It shows CLEAR
        assertEquals("CLEAR", label)
    }

    // Edge cases

    @Test
    fun dueDateLabel_activeDueTomorrow_returnsDMinus1() {
        // GIVEN - An active task due on 2026-10-15
        val dueDate = LocalDate.of(2026, 10, 15)

        // WHEN - Build the label on 2026-10-14
        val label = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN - One day before the D-Day boundary
        assertEquals("D-1", label)
    }

    @Test
    fun dueDateLabel_activeDueTodayBoundary_returnsDDay() {
        // GIVEN - An active task due on the D-Day boundary
        val dueDate = LocalDate.of(2026, 10, 14)

        // WHEN - Build the label on 2026-10-14
        val label = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN - It shows D-Day
        assertEquals("D-Day", label)
    }

    @Test
    fun dueDateLabel_activeDueYesterday_returnsDPlus1() {
        // GIVEN - An active task due on 2026-10-13
        val dueDate = LocalDate.of(2026, 10, 13)

        // WHEN - Build the label on 2026-10-14
        val label = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN - One day after the D-Day boundary
        assertEquals("D+1", label)
    }

    @Test
    fun dueDateLabel_activeDueInOneMonth_returnsDMinus31() {
        // GIVEN - An active task due exactly one month later, on 2026-11-14
        val dueDate = LocalDate.of(2026, 11, 14)

        // WHEN - Build the label on 2026-10-14
        val label = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN - It counts the total 31 days, not the day-of-month difference
        assertEquals("D-31", label)
    }

    @Test
    fun dueDateLabel_activeDueDayBeforeOneMonth_returnsDMinus30() {
        // GIVEN - An active task due on 2026-11-13
        val dueDate = LocalDate.of(2026, 11, 13)

        // WHEN - Build the label on 2026-10-14
        val label = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN - It counts the total 30 days
        assertEquals("D-30", label)
    }

    @Test
    fun dueDateLabel_activeDueDayAfterOneMonth_returnsDMinus32() {
        // GIVEN - An active task due on 2026-11-15
        val dueDate = LocalDate.of(2026, 11, 15)

        // WHEN - Build the label on 2026-10-14
        val label = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN - It counts the total 32 days
        assertEquals("D-32", label)
    }

    @Test
    fun dueDateLabel_activeOverdueByOneMonth_returnsDPlus30() {
        // GIVEN - An active task that was due on 2026-09-14
        val dueDate = LocalDate.of(2026, 9, 14)

        // WHEN - Build the label on 2026-10-14
        val label = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN - It counts the total 30 days overdue
        assertEquals("D+30", label)
    }

    @Test
    fun dueDateLabel_activeOverdueByOneYear_returnsDPlus365() {
        // GIVEN - An active task with a past due date picked directly, 2025-10-14
        val dueDate = LocalDate.of(2025, 10, 14)

        // WHEN - Build the label on 2026-10-14
        val label = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN - It counts the total 365 days overdue
        assertEquals("D+365", label)
    }

    @Test
    fun dueDateLabel_activeDueAcrossMonthEnd_returnsDMinus3() {
        // GIVEN - Today is 2026-10-30 and the task is due on 2026-11-02
        val monthEnd = LocalDate.of(2026, 10, 30)
        val dueDate = LocalDate.of(2026, 11, 2)

        // WHEN - Build the label
        val label = dueDateLabel(dueDate, monthEnd, isCompleted = false)

        // THEN - It counts calendar days across the month end
        assertEquals("D-3", label)
    }

    @Test
    fun dueDateLabel_activeDueAcrossYearEnd_returnsDMinus1() {
        // GIVEN - Today is 2026-12-31 and the task is due on 2027-01-01
        val yearEnd = LocalDate.of(2026, 12, 31)
        val dueDate = LocalDate.of(2027, 1, 1)

        // WHEN - Build the label
        val label = dueDateLabel(dueDate, yearEnd, isCompleted = false)

        // THEN - It counts calendar days across the year end
        assertEquals("D-1", label)
    }

    @Test
    fun dueDateLabel_activeDueAcrossLeapDay_returnsDMinus2() {
        // GIVEN - Today is 2028-02-28 and the task is due on 2028-03-01
        val beforeLeapDay = LocalDate.of(2028, 2, 28)
        val dueDate = LocalDate.of(2028, 3, 1)

        // WHEN - Build the label
        val label = dueDateLabel(dueDate, beforeLeapDay, isCompleted = false)

        // THEN - Feb 29 is counted
        assertEquals("D-2", label)
    }

    @Test
    fun dueDateLabel_activeDueInOneYear_returnsDMinus365() {
        // GIVEN - An active task due on 2027-10-14
        val dueDate = LocalDate.of(2027, 10, 14)

        // WHEN - Build the label on 2026-10-14
        val label = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN - It counts the total 365 days
        assertEquals("D-365", label)
    }

    @Test
    fun dueDateLabel_completedDueToday_returnsClear() {
        // GIVEN - A completed task due today
        val dueDate = LocalDate.of(2026, 10, 14)

        // WHEN - Build the label
        val label = dueDateLabel(dueDate, today, isCompleted = true)

        // THEN - Completion wins over D-Day
        assertEquals("CLEAR", label)
    }
}
