# Spec: `TasksListBindings.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `fun setDueDateLabel(textView: TextView, task: Task?)`

**Intent**: Bind the D-day label of `task` (as defined by `dueDateLabel` with today's date) to the label `TextView` in a task list row. A non-empty label is shown; when the task should show no label, the view must show nothing — including when the same view previously showed a label for an earlier state of the task (rows are rebound when a task is checked or unchecked, and views are recycled).

**Pre-conditions**
- Called on the main thread with a `TextView` from a task list row.
- "Today" is `LocalDate.now()` at the time of the call.
- "Shows nothing" means: the view's text is `""` or its visibility is not `View.VISIBLE`.

**Post-conditions**
- Success cases
  - Active task due in 3 days shows `"D-3"` and the view is visible. `[Integration]`
    - Tip: Given a new TextView and Task("Title", "Desc", false, dueDate = LocalDate.now().plusDays(3)) / When setDueDateLabel is called / Then text is `"D-3"` and visibility is `View.VISIBLE`.
  - Active task due today shows `"D-Day"` and the view is visible. `[Integration]`
    - Tip: Given a new TextView and Task("Title", "Desc", false, dueDate = LocalDate.now()) / When setDueDateLabel is called / Then text is `"D-Day"` and visibility is `View.VISIBLE`.
  - Active task overdue by 3 days shows `"D+3"` and the view is visible. `[Integration]`
    - Tip: Given a new TextView and Task("Title", "Desc", false, dueDate = LocalDate.now().minusDays(3)) / When setDueDateLabel is called / Then text is `"D+3"` and visibility is `View.VISIBLE`.
  - Completed task with a due date shows `"CLEAR"` and the view is visible. `[Integration]`
    - Tip: Given a new TextView and Task("Title", "Desc", true, dueDate = LocalDate.now().plusDays(3)) / When setDueDateLabel is called / Then text is `"CLEAR"` and visibility is `View.VISIBLE`.
  - Completed task with no due date shows `"CLEAR"` and the view is visible. `[Integration]`
    - Tip: Given a new TextView and Task("Title", "Desc", true, dueDate = null) / When setDueDateLabel is called / Then text is `"CLEAR"` and visibility is `View.VISIBLE`.
  - Active task with no due date shows nothing. `[Integration]`
    - Tip: Given a new TextView and Task("Title", "Desc", false, dueDate = null) / When setDueDateLabel is called / Then the view shows nothing (text `""` or visibility not `View.VISIBLE`).
  - Null task shows nothing. `[Integration]`
    - Tip: Given a new TextView and task = null / When setDueDateLabel is called / Then the view shows nothing (text `""` or visibility not `View.VISIBLE`).
- Error cases
  - None. A null task is valid input and is covered above. `[Integration]`
    - Tip: Given no invalid input exists / When — / Then no test is needed.
- Edge cases
  - Completing then un-completing a task with no due date clears the stale `"CLEAR"`. `[Integration]`
    - Tip: Given a TextView bound once to Task("Title", "Desc", true, dueDate = null) (shows `"CLEAR"`) / When setDueDateLabel is called again on the same view with the same task after isCompleted = false / Then the view shows nothing (text `""` or visibility not `View.VISIBLE`).
  - Completing then un-completing a task with a due date replaces `"CLEAR"` with the D-day label. `[Integration]`
    - Tip: Given a TextView bound once to Task("Title", "Desc", true, dueDate = LocalDate.now().plusDays(3)) (shows `"CLEAR"`) / When setDueDateLabel is called again on the same view after isCompleted = false / Then text is `"D-3"` and visibility is `View.VISIBLE`.
  - Completing an active task with a due date replaces the D-day label with `"CLEAR"`. `[Integration]`
    - Tip: Given a TextView bound once to Task("Title", "Desc", false, dueDate = LocalDate.now().plusDays(3)) (shows `"D-3"`) / When setDueDateLabel is called again on the same view after isCompleted = true / Then text is `"CLEAR"` and visibility is `View.VISIBLE`.
  - A recycled view that showed a label is rebound to a task with no label. `[Integration]`
    - Tip: Given a TextView bound once to Task("A", "Desc", false, dueDate = LocalDate.now().plusDays(1)) (shows `"D-1"`) / When setDueDateLabel is called on the same view with Task("B", "Desc", false, dueDate = null) / Then the view shows nothing (text `""` or visibility not `View.VISIBLE`).
  - A view that showed nothing is rebound to a task with a label and becomes visible. `[Integration]`
    - Tip: Given a TextView bound once to Task("A", "Desc", false, dueDate = null) (shows nothing) / When setDueDateLabel is called on the same view with Task("B", "Desc", false, dueDate = LocalDate.now().plusDays(1)) / Then text is `"D-1"` and visibility is `View.VISIBLE`.

**Undefined behavior** (optional)
- A test that runs across midnight may see two different "today" values; tests should compute dates from `LocalDate.now()` immediately before the call.
