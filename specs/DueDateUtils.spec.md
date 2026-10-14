# Spec: `DueDateUtils.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `internal fun dueDateLabel(dueDate: LocalDate?, today: LocalDate, isCompleted: Boolean): String`

**Intent**: Return the D-day label shown next to a task in the task list. An active task due in N days shows `D-N`, due today shows `D-Day`, overdue by N days shows `D+N`; an active task with no due date shows no label (`""`); a completed task shows `CLEAR` with or without a due date. N is the total number of calendar days between `today` and `dueDate`.

**Pre-conditions**
- `today` is the current local date of the device.
- `dueDate` is `null` when the task has no due date.
- `dueDate` may be before `today`: the add/edit date picker has no minimum date, so a past date can be saved directly, not only reached as time passes.

**Post-conditions**
- Success cases
  - Active task due in N days (N ≥ 1) returns `"D-N"`. `[Unit]`
    - Tip: Given dueDate = 2026-10-17, today = 2026-10-14, isCompleted = false / When dueDateLabel is called / Then it returns `"D-3"`.
  - Active task due today returns `"D-Day"`. `[Unit]`
    - Tip: Given dueDate = 2026-10-14, today = 2026-10-14, isCompleted = false / When dueDateLabel is called / Then it returns `"D-Day"`.
  - Active task overdue by N days (N ≥ 1) returns `"D+N"`. `[Unit]`
    - Tip: Given dueDate = 2026-10-11, today = 2026-10-14, isCompleted = false / When dueDateLabel is called / Then it returns `"D+3"`.
  - Active task with no due date returns `""` (no label). `[Unit]`
    - Tip: Given dueDate = null, today = 2026-10-14, isCompleted = false / When dueDateLabel is called / Then it returns `""`.
  - Completed task with a due date returns `"CLEAR"`. `[Unit]`
    - Tip: Given dueDate = 2026-10-17, today = 2026-10-14, isCompleted = true / When dueDateLabel is called / Then it returns `"CLEAR"`.
  - Completed task with an overdue due date returns `"CLEAR"`. `[Unit]`
    - Tip: Given dueDate = 2026-10-11, today = 2026-10-14, isCompleted = true / When dueDateLabel is called / Then it returns `"CLEAR"`.
  - Completed task with no due date returns `"CLEAR"`. `[Unit]`
    - Tip: Given dueDate = null, today = 2026-10-14, isCompleted = true / When dueDateLabel is called / Then it returns `"CLEAR"`.
- Error cases
  - None. Every combination of arguments is valid input. `[Unit]`
    - Tip: Given no invalid input exists / When — / Then no test is needed.
- Edge cases
  - Due tomorrow (one day before the D-Day boundary) returns `"D-1"`. `[Unit]`
    - Tip: Given dueDate = 2026-10-15, today = 2026-10-14, isCompleted = false / When dueDateLabel is called / Then it returns `"D-1"`.
  - Due today (at the D-Day boundary) returns `"D-Day"`. `[Unit]`
    - Tip: Given dueDate = 2026-10-14, today = 2026-10-14, isCompleted = false / When dueDateLabel is called / Then it returns `"D-Day"`.
  - Due yesterday (one day after the D-Day boundary) returns `"D+1"`. `[Unit]`
    - Tip: Given dueDate = 2026-10-13, today = 2026-10-14, isCompleted = false / When dueDateLabel is called / Then it returns `"D+1"`.
  - Due exactly one month later counts total days, not the day-of-month difference. `[Unit]`
    - Tip: Given dueDate = 2026-11-14, today = 2026-10-14, isCompleted = false / When dueDateLabel is called / Then it returns `"D-31"`.
  - One day before the one-month mark returns the total day count. `[Unit]`
    - Tip: Given dueDate = 2026-11-13, today = 2026-10-14, isCompleted = false / When dueDateLabel is called / Then it returns `"D-30"`.
  - One day after the one-month mark returns the total day count. `[Unit]`
    - Tip: Given dueDate = 2026-11-15, today = 2026-10-14, isCompleted = false / When dueDateLabel is called / Then it returns `"D-32"`.
  - Overdue by exactly one month counts total days. `[Unit]`
    - Tip: Given dueDate = 2026-09-14, today = 2026-10-14, isCompleted = false / When dueDateLabel is called / Then it returns `"D+30"`.
  - Overdue by exactly one year (a past date picked directly) counts total days. `[Unit]`
    - Tip: Given dueDate = 2025-10-14, today = 2026-10-14, isCompleted = false / When dueDateLabel is called / Then it returns `"D+365"`.
  - Due date across a month end counts calendar days. `[Unit]`
    - Tip: Given dueDate = 2026-11-02, today = 2026-10-30, isCompleted = false / When dueDateLabel is called / Then it returns `"D-3"`.
  - Due date across a year end counts calendar days. `[Unit]`
    - Tip: Given dueDate = 2027-01-01, today = 2026-12-31, isCompleted = false / When dueDateLabel is called / Then it returns `"D-1"`.
  - Due date across a leap day counts Feb 29. `[Unit]`
    - Tip: Given dueDate = 2028-03-01, today = 2028-02-28, isCompleted = false / When dueDateLabel is called / Then it returns `"D-2"`.
  - Due exactly one year later counts total days. `[Unit]`
    - Tip: Given dueDate = 2027-10-14, today = 2026-10-14, isCompleted = false / When dueDateLabel is called / Then it returns `"D-365"`.
  - Completed task due today still returns `"CLEAR"` (completion wins over the D-Day boundary). `[Unit]`
    - Tip: Given dueDate = 2026-10-14, today = 2026-10-14, isCompleted = true / When dueDateLabel is called / Then it returns `"CLEAR"`.

**Undefined behavior** (optional)
- Dates outside the date picker's range (Android `DatePickerDialog` default: 1900-01-01 to 2100-12-31) cannot be saved from the UI, so they are left undefined.
