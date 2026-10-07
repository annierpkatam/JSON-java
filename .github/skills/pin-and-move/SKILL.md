---
name: pin-and-move
description: "Use after human selection and explicit authorization to pin current returns, effects, and errors in characterization tests, save green check evidence, pause for separate test-only commit approval, then perform one authorized source move with frozen tests and move-only rollback on red."
disable-model-invocation: true
---

# Pin behavior, then make one move

## When to use

Use for an explicitly authorized, small, behavior-preserving refactor already selected by the human. Do not choose candidates, perform migrations, fix behavior, or expand the approved move. Creating or loading this skill does not authorize tests, commits, or source edits.

Use a thinking and reasoning model such as GPT Sol or Astra through proposal completion where selection is available; do not claim selection without evidence. Implementation requires explicit authorization.

## Prerequisites

- Read .github/copilot-instructions.md, .github/instructions/baseline-tests.instructions.md, and docs/refactor-workflow.md before refactor analysis or action. If the workflow is missing or unreadable, report BLOCKED and stop. Follow every human gate; approval of one gate does not approve later ones.
- Confirm the selected seam, exactly one proposed move, allowed test/source paths, authorized check commands and side effects, and an approved destination for durable check evidence. Ask for materially missing information; do not invent a report path or command.
- Inspect and record the starting revision, working-tree changes, and staged paths. Preserve all pre-existing work. If overlapping edits or unrelated staged changes prevent a safe isolated test commit or rollback, stop for a human decision; do not stash, unstage, discard, or commit them silently.
- Never edit baseline tests and production source in the same workflow step. Separate tool calls are not separate workflow steps. Do not use source-mutating strict-mode checks in a test-only step.

## Step 1 — Pin current behavior, tests only

1. Leave production source unchanged. Create or strengthen characterization tests and necessary test-only helpers/fixtures within the approved scope.
2. Pin observed return values, observable effects (including mutation and relevant ordering), and errors (including exception types and messages where observable). Cover relevant normal, edge, and reproducible defect/quirk cases without asserting invented behavior or fixing defects.
3. Identify the baseline tests, helpers, fixtures, and assertions to freeze. Record inputs/setup and the observable invariants they capture.
4. Run only authorized checks against the unchanged source. A green check requires actual completed successful output and a successful exit code when available, not merely an invocation, configuration evidence, or absence of output. Report unavailable completion evidence as UNKNOWN, NOT RUN, or BLOCKED as appropriate, never PASSED.
5. If checks fail, record the failure and stop; do not modify source, suppress failures, or weaken expectations to manufacture green. Distinguish pre-existing failures without calling the baseline green.
6. On green, save durable evidence at the approved destination: exact command, working directory, revision, test diff identity, environment details actually observed, completion/exit status, relevant output, test counts/failures when reported, and the baseline file list. Keep this evidence separate from the test-only commit unless it is an explicitly approved test fixture; do not silently include documentation or logs in that commit.
7. Review the diff to verify production source is unchanged. Present the saved evidence and exact proposed test-only commit contents. **STOP for separate, explicit human approval to make that commit.** Initial workflow authorization or a green check is not commit approval.

## Step 2 — Approved test-only commit

1. Resume only after explicit approval of the test-only commit and all applicable workflow gates. Reinspect the index and working tree. If the approved tests or checked source changed after green, rerun authorized checks and renew approval before committing.
2. Stage only approved characterization tests and necessary test helpers/fixtures. Verify the entire staged diff contains no production source, unrelated tests, setup assets, or evidence reports. Do not commit if unrelated staged work remains; ask the human to resolve it without altering their work yourself.
3. Commit only the approved test changes. Verify the commit succeeded and inspect its contents; record its full commit ID and associate it with the saved green evidence. If committing fails or its result is unavailable, report FAILED or BLOCKED and stop before source changes.
4. Freeze the committed baseline tests, helpers, and fixtures. Confirm explicit authorization for the specified source move; if it was not supplied or a later human gate requires approval, stop and ask. Commit approval alone is not source-move approval.

## Step 3 — One source move, tests frozen

1. Record the exact pre-move source state and scoped diff needed for a safe move-only rollback, including any pre-existing edits. If rollback cannot be isolated, stop before editing.
2. Make exactly the approved structural source move. Preserve pinned returns, effects, and errors, including current defects. Do not change tests, helpers, fixtures, public behavior, or unrelated source; do not bundle cleanup or a second move.
3. Re-run the same authorized checks used for green, with comparable configuration, and inspect the diff to verify the pinned baseline is unchanged. Report actual results and save them with the baseline commit ID and move diff at the approved evidence destination.
4. On green, report the one move, actual check results, unchanged pinned baseline, and remaining risks; stop at the next human gate. Do not commit source, push, or start another move without separate authorization.

## On red — Undo only this move

- Record the failed check output before rollback. Never rewrite, skip, weaken, or delete pinned tests to make the move pass.
- Undo only the source edits introduced by this move, restoring the exact pre-move source state while retaining the test-only commit, evidence, and all unrelated or pre-existing work. Do not use broad reset, clean, whole-file restore, or history rewriting that could discard other work.
- If concurrent changes or ambiguous ownership make rollback unsafe, stop and report BLOCKED; request human help instead of discarding changes.
- Verify the scoped rollback diff and unchanged pinned tests. Re-run the baseline checks only if authorized, record the actual result, and stop. If baseline green is not restored, report that fact; do not repair other code or try another move automatically.
- If the post-move check cannot complete, report BLOCKED rather than green; leave no implication of success and request a decision about retry or scoped rollback.

## Required handoff and completion evidence

At each stop, report the current step/gate, changed paths, saved evidence location, actual check statuses (PASSED, FAILED, NOT RUN, or BLOCKED), relevant output, and the specific human decision required. After committing, include the verified test-only commit ID. After moving or rolling back, include the scoped diff result and confirmation of whether pinned tests remain unchanged. Never invent outputs, commit IDs, approval, or success.