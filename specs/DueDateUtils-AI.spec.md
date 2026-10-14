# Spec: `DueDateUtils.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `internal fun dueDateLabel(dueDate: LocalDate?, today: LocalDate, isCompleted: Boolean): String`

**Intent**: Return the D-day label for a task in the task list: `D-N` when an active task is due in N days, `D-Day` when it is due today, `D+N` when it is overdue by N days, no label (`""`) when an active task has no due date, and `CLEAR` when the task is completed.

**Pre-conditions**
- `today` is the current date.
- `dueDate` is `null` when the task has no due date.

**Post-conditions**
- Success cases
  - Active task due in N days returns `"D-N"`. `[Unit]`
    - Tip: Given dueDate = 2026-10-17, today = 2026-10-14, isCompleted = false / When dueDateLabel is called / Then it returns `"D-3"`.
  - Active task due today returns `"D-Day"`. `[Unit]`
    - Tip: Given dueDate = 2026-10-14, today = 2026-10-14, isCompleted = false / When dueDateLabel is called / Then it returns `"D-Day"`.
  - Active task overdue by N days returns `"D+N"`. `[Unit]`
    - Tip: Given dueDate = 2026-10-11, today = 2026-10-14, isCompleted = false / When dueDateLabel is called / Then it returns `"D+3"`.
  - Active task with no due date returns `""`. `[Unit]`
    - Tip: Given dueDate = null, today = 2026-10-14, isCompleted = false / When dueDateLabel is called / Then it returns `""`.
  - Completed task with a due date returns `"CLEAR"`. `[Unit]`
    - Tip: Given dueDate = 2026-10-17, today = 2026-10-14, isCompleted = true / When dueDateLabel is called / Then it returns `"CLEAR"`.
  - Completed task with no due date returns `"CLEAR"`. `[Unit]`
    - Tip: Given dueDate = null, today = 2026-10-14, isCompleted = true / When dueDateLabel is called / Then it returns `"CLEAR"`.
- Error cases
  - None. `[Unit]`
    - Tip: Given — / When — / Then no test is needed because every input is valid.
- Edge cases
  - Due tomorrow returns `"D-1"`. `[Unit]`
    - Tip: Given dueDate = 2026-10-15, today = 2026-10-14, isCompleted = false / When dueDateLabel is called / Then it returns `"D-1"`.
  - Due today returns `"D-Day"`. `[Unit]`
    - Tip: Given dueDate = 2026-10-14, today = 2026-10-14, isCompleted = false / When dueDateLabel is called / Then it returns `"D-Day"`.
  - Due yesterday returns `"D+1"`. `[Unit]`
    - Tip: Given dueDate = 2026-10-13, today = 2026-10-14, isCompleted = false / When dueDateLabel is called / Then it returns `"D+1"`.
  - Due date in the next month counts calendar days across the month end. `[Unit]`
    - Tip: Given dueDate = 2026-11-02, today = 2026-10-30, isCompleted = false / When dueDateLabel is called / Then it returns `"D-3"`.
  - Due date in the next year counts calendar days across the year end. `[Unit]`
    - Tip: Given dueDate = 2027-01-02, today = 2026-12-31, isCompleted = false / When dueDateLabel is called / Then it returns `"D-2"`.
  - Due date across a leap day counts Feb 29. `[Unit]`
    - Tip: Given dueDate = 2028-03-01, today = 2028-02-28, isCompleted = false / When dueDateLabel is called / Then it returns `"D-2"`.

**Undefined behavior** (optional)
- None.
