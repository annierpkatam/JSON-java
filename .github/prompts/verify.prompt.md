---
name: verify
description: "Independently review an active run's approved scope, pinned test-only commit, actual check output, and complete tracked/untracked changes; save a cited same-run verdict and stop for human acceptance or separately authorized repair."
agent: Reviewer
tools: [read, search, execute, edit]
argument-hint: "Identify the active run or its approved brief, baseline commit ID, and baseline/post-move evidence."
---

# Verify a green Move independently

Act as [Reviewer](../agents/reviewer.agent.md). First read and follow the [safe-refactor workflow](../../docs/refactor-workflow.md), [repository instructions](../copilot-instructions.md), and [baseline-test instructions](../instructions/baseline-tests.instructions.md). If the workflow is missing or unreadable, report BLOCKED and stop before reviewing. Creating this prompt does not invoke Verify.

## Resolve the active run and entry gate

1. Identify the active run and its concrete approved brief path. If run identity is absent or ambiguous, ask the human which run to review and stop. Do not choose the newest directory, combine evidence from different runs, or create a new run.
2. Resolve verdict.md as a sibling of that run's brief.md in docs/refactor-runs/<run-id>/. `<run-id>` is the established run identity, not a literal directory name or a value to invent. Explicit invocation of this prompt authorizes saving only that same-run verdict once the run is clear; ask if the destination is uncertain or conflicts with another run's artifact.
3. Require the human-approved brief and approval records; verified test-only baseline commit ID and commit contents; actual green Pin output; evidence of exactly the approved source Move; and completed green post-move output tied to the inspected state. Never start independent Verify at Pin. If entry proof is missing, do not proceed as though the gate passed: report BLOCKED and identify the missing prerequisite, saving a blocked verdict only when its same-run destination is established and permitted by the workflow.
4. Confirm independence from test/source authorship. Disclose a conflict and stop for a different Reviewer rather than reviewing your own implementation.

## Required evidence inspection

1. Read the approved brief, not merely its summary. Extract scope, seam, exactly one move, allowed paths, returns/effects/errors to preserve (including defects), exclusions, checks/configuration, and human approvals. Do not infer approval from an agent-authored document.
2. Inspect the full baseline commit object/diff and record its verified full ID. Confirm it contains only approved tests and necessary test helpers/fixtures. Identify the complete pinned baseline, not just the newly added test methods.
3. Inspect complete change evidence: baseline commit diff, source Move compared with recorded pre-move state, subsequent committed changes if any, staged and unstaged tracked changes, and relevant untracked file contents. Inventory additions, deletions, renames, configuration, tests/helpers/fixtures, and run reports; separate pre-existing work from this move with evidence. Account for the authorized verdict artifact itself without mistaking it for a source move.
4. Do not rely on diff stats, a filtered source-only diff, or truncated output. Retrieve omitted relevant contents before a favorable verdict. If pre-move state, ownership, or complete changes cannot be established, record the limitation as blocking.
5. Cite the comparisons proving pinned tests/helpers/fixtures are unchanged after the test-only commit. If any were rewritten, weakened, skipped, deleted, or modified to make the move pass, mark that criterion FAILED.
6. Read real baseline and post-move check output. Cite its location, exact command, working directory, completion/exit code when available, reported counts/failures, configuration, and revision/diff applicability. Flag missing, stale, skipped, failed, or incomparable checks. Attribute execution to the actual executor; never claim Reviewer ran supplied checks.
7. Compare the complete move with approved scope and pinned invariants. Cite source/test file locations and commit hunks supporting behavior preservation or violations. Reject migrations, behavior fixes, unrelated cleanup, additional moves, and gate bypasses.

## Fail closed; never repair here

- Missing proof fails the verification requirement: no favorable verdict or acceptance recommendation when required evidence is absent. Use BLOCKED for missing evidence/prerequisites and CHANGES REQUIRED for demonstrated violations; do not invent a runtime failure when the check was not run.
- Report each actual check status as PASSED, FAILED, NOT RUN, or BLOCKED, with relevant observed output and limitations. UNKNOWN coverage or behavior remains unknown; a configured test task or success summary is not execution proof.
- Reviewer may write only this run's approved verdict.md. Do not edit source, tests, helpers, fixtures, brief, shortlist, or check evidence; fix defects; stage/commit; delegate; or run tests/builds that create other artifacts. Use terminal capability only for read-only Git/evidence inspection. These restrictions are instruction-level, not a technical sandbox.

## Save the cited verdict; stop

1. Inspect any existing verdict before updating. Preserve unrelated content and approval records; ask before replacing conflicting review state.
2. Save verdict.md in the established same-run directory with run identity, approved brief reference, baseline commit ID, inspected state and full changed-file inventory, gate evidence, unchanged-baseline proof, actual output citations, scope/invariant assessment, findings, missing proof, and residual risks. Use Reviewer's verdict format: FAVORABLE, CHANGES REQUIRED, or BLOCKED. Every material conclusion must cite concrete inspected evidence; clearly label unsupported items.
3. Re-read the saved file to confirm it exists and contains the intended verdict. On write/read-back failure, report BLOCKED with the attempted concrete path and observed error; do not claim persistence or successful completion.
4. If FAVORABLE, suggest that the human consider acceptance, without accepting on their behalf or approving your own verdict. Otherwise suggest supplying missing proof or a separately authorized repair step by the working agent. Do not repair, rewrite tests, or proceed yourself.
5. Report the concrete verdict path, result, key evidence/findings, actual check statuses, and next human decision. Stop; do not commit, merge, or begin another move.