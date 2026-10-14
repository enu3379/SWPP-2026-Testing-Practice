# Spec: `TasksListBindings.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `fun setDueDateLabel(textView: TextView, task: Task?)`

**Intent**: Show the D-day label of `task` in the given `TextView` of a task list row, using today's date. When the task has a label, the view shows it; when the task has no label, nothing is shown.

**Pre-conditions**
- Called on the main thread with a `TextView` from a task list row.
- Today is `LocalDate.now()`.

**Post-conditions**
- Success cases
  - Active task with a future due date shows `"D-N"`. `[Integration]`
    - Tip: Given a TextView and Task("Title", "Desc", false, dueDate = LocalDate.now().plusDays(3)) / When setDueDateLabel is called / Then text is `"D-3"` and visibility is `View.VISIBLE`.
  - Active task due today shows `"D-Day"`. `[Integration]`
    - Tip: Given a TextView and Task("Title", "Desc", false, dueDate = LocalDate.now()) / When setDueDateLabel is called / Then text is `"D-Day"` and visibility is `View.VISIBLE`.
  - Active overdue task shows `"D+N"`. `[Integration]`
    - Tip: Given a TextView and Task("Title", "Desc", false, dueDate = LocalDate.now().minusDays(3)) / When setDueDateLabel is called / Then text is `"D+3"` and visibility is `View.VISIBLE`.
  - Completed task shows `"CLEAR"`. `[Integration]`
    - Tip: Given a TextView and Task("Title", "Desc", true, dueDate = LocalDate.now().plusDays(3)) / When setDueDateLabel is called / Then text is `"CLEAR"` and visibility is `View.VISIBLE`.
  - Active task with no due date shows no label. `[Integration]`
    - Tip: Given a TextView and Task("Title", "Desc", false, dueDate = null) / When setDueDateLabel is called / Then text is `""`.
- Error cases
  - None. `[Integration]`
    - Tip: Given — / When — / Then no test is needed because every input is valid.
- Edge cases
  - Null task shows no label. `[Integration]`
    - Tip: Given a TextView and task = null / When setDueDateLabel is called / Then text is `""`.
  - Completed task with no due date shows `"CLEAR"`. `[Integration]`
    - Tip: Given a TextView and Task("Title", "Desc", true, dueDate = null) / When setDueDateLabel is called / Then text is `"CLEAR"` and visibility is `View.VISIBLE`.

**Undefined behavior** (optional)
- None.
