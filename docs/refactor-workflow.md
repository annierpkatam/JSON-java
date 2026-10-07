# Gated safe-refactor workflow

**Start → Survey → Brief → Pin → Move → Verify → human acceptance**

This document defines a workflow, not authorization to execute it. Creating setup files, discussing refactoring, or committing setup assets does not start a run. Begin only on an explicit human request, such as invoking `/start-refactor` to request a run. Use a thinking and reasoning model such as GPT Sol or Astra through proposal completion where selection is available; never claim model selection without evidence. Keep implementation out of scope until explicitly authorized.

## Roles and rules

- [Scout](../.github/agents/scout.agent.md) surveys and orchestrates. It writes only the current run's shortlist.md and brief.md, never tests or source. It delegates only Surgeon and Reviewer.
- [Surgeon](../.github/agents/surgeon.agent.md) performs authorized Pin, approved test-only commit, and one authorized Move. It saves exact check evidence in the approved current-run evidence.md, not Scout's reports.
- [Reviewer](../.github/agents/reviewer.agent.md) independently inspects evidence and the complete diff after green Move. It writes only the approved current-run verdict.md; it never repairs code/tests or grants human acceptance.
- Follow [repository instructions](../.github/copilot-instructions.md) and [baseline-test instructions](../.github/instructions/baseline-tests.instructions.md). Tool aliases do not technically enforce path or phase boundaries; these are instruction-level restrictions.
- Never bypass human gates or infer later approval from earlier approval. Never edit baseline tests and production source in the same workflow step; separate tool calls do not create separate steps.
- Preserve current observable behavior, including defects. Exclude migrations, behavior fixes, public-contract changes, and bundled cleanup. Never rewrite, weaken, skip, or delete pinned tests/helpers/fixtures to make a Move pass.
- Preserve pre-existing tracked, untracked, and staged work. If overlapping changes prevent safe isolation, stop and ask; do not silently stash, unstage, discard, or commit unrelated work.

## One run, four artifacts

At Start, propose a unique, path-safe run ID and check that docs/refactor-runs/<run-id>/ does not exist. `<run-id>` is generated at invocation, not a literal directory name or a value created during setup. Obtain the human's confirmation of the concrete directory before writing. On collision, choose a fresh ID; never overwrite a prior run.

Create exactly one directory for the run. All stages reuse it, including resumed delegations; never allocate a second run directory for Brief, Pin, Move, or Verify. If the active run is unclear, ask for its path rather than choosing the latest directory.

| Artifact in that directory | Writer | Purpose |
| --- | --- | --- |
| shortlist.md | Scout | Saved numbered candidates and ranking evidence |
| brief.md | Scout | Selection, scope, human approvals, phase, evidence references, commit ID, and handoff state |
| evidence.md | Surgeon | Exact baseline, post-move, and any rollback check output and metadata |
| verdict.md | Reviewer | Independent cited verdict and next human decision |

Confirm each artifact by reading it back after saving. A write attempt alone is not persistence evidence. On failure, report BLOCKED with the concrete path and observed error; never claim the artifact exists or advance a gate dependent on it.

Approve the concrete evidence.md path with the Pin scope and the verdict.md path for independent review. These destinations fit the agents' existing approved-artifact permissions; Scout must not write them. Scout may retain summaries and evidence references in brief.md, but summaries do not replace the exact evidence in evidence.md.

## 1. Start

1. Read this workflow and applicable instructions before candidate analysis. Confirm the explicit run request and survey scope; ask about materially missing constraints.
2. Resolve and confirm the unique run directory as described above. Record the starting revision and any observed pre-existing work needed for later comparisons, without running checks or altering the index.
3. Proceed only with survey/proposal work. Invocation does not authorize test execution, test changes, commits, source edits, or Verify.

Entry prompt: [start-refactor](../.github/prompts/start-refactor.prompt.md).

## 2. Survey — save before offering choices

1. Scout uses the [survey-select rubric](../.github/skills/survey-select/SKILL.md) within the authorized scope. Rank small, testable candidates by seam, coverage evidence, and exactly one proposed structural move. Cite real source/test evidence and label unknowns; static test inspection is not measured coverage or successful execution.
2. Scout saves the numbered list to this run's shortlist.md and reads it back. Only then show the saved IDs and choices. If writing/read-back fails, stop with a blocker; never offer an unsaved menu. If no eligible candidates exist, save that result and stop without inventing candidates.
3. Ask the human to select an ID, narrow the survey, or decline. Do not choose on the human's behalf.

**Human gate: candidate selection.** A reply of `1` selects candidate ID `1` from this run's saved shortlist and starts Brief only. Re-read the list; ask for reconfirmation if it changed or selection is ambiguous. `1` is not scope approval or implementation authorization.

## 3. Brief — approve Pin scope

1. Scout creates brief.md in the same run directory using the [Brief prompt](../.github/prompts/brief.prompt.md).
2. Record the selected ID, approved-run identity, source/test evidence, bounded seam, exactly one proposed Move, allowed test/source paths, returns/effects/errors to preserve, defects/quirks, exclusions, and unanswered questions.
3. Specify proposed evidence-backed check commands and configuration, permitted side effects, the concrete evidence.md destination, and phase-specific permissions. Use commands established by repository guidance; do not invent variants or claim execution. Source-mutating strict-mode checks cannot run during a test-only Pin step.
4. Save/read back the brief, present it, and request explicit scope approval for **Pin only**, including the specified test changes, checks, and evidence writing. Record the actual human decision; renew approval if scope materially changes.

**Human gate: scope/check approval.** After approval, Scout delegates Surgeon with the saved brief and a strict Pin-only instruction, or shows a copy-ready handoff if delegation is unavailable. No commit, Move, or Reviewer is authorized by this gate. Scout never performs test edits itself.

## 4. Pin — green evidence, then stop

1. Surgeon follows the [pin-and-move skill](../.github/skills/pin-and-move/SKILL.md). Leave production source unchanged. Characterize current returns, observable effects, and errors with approved tests/helpers/fixtures, including relevant normal, edge, and reproducible defect cases.
2. Identify all baseline files and assertions to freeze. Run only the approved checks against unchanged production source.
3. Save exact baseline results in evidence.md using the evidence requirements below. Include failed or incomplete results rather than suppressing them. Read the saved evidence back and inspect the test-only diff.
4. If red or blocked, stop without source edits, commit, or Reviewer. Do not manufacture green by fixing source or weakening tests.
5. If green, return the evidence path, actual results, exact proposed test-only commit contents, and baseline file list to Scout. Scout records them in brief.md and requests separate human commit approval.

**Human gate: green test-only commit approval.** Pause here even if the initial run or scope was approved. Present the specific tests and checked state. Request Move authorization distinctly if the next delegation is to include source editing; commit approval alone is not Move approval. Never begin independent Verify at Pin or expose an unconditional Review action there.

### Required exact evidence

For each baseline, post-move, or rollback check record in evidence.md:

- Phase, executor, exact command and working directory, observed environment/configuration, and source revision plus applicable test/source diff identity.
- Actual status: PASSED, FAILED, NOT RUN, or BLOCKED; completion evidence and exit code when available. Unavailable completion proof is not green.
- Exact captured check output, including reported test counts, failures, errors, and skipped tests. Keep explanatory summaries separate from verbatim output. Do not silently truncate output; if full required output cannot be saved, disclose the gap and block advancement pending evidence.
- Baseline file list, pre-existing change ownership, proposed commit contents, verified commit ID once created, Move diff, and any rollback result relevant to interpreting the checks.

Configuration verification, attempted invocation, missing output, and a CI success assertion without applicable output are not proof of execution. Rechecks get separate entries; preserve prior failures. Evidence must remain associated with the exact checked state, not merely a branch name.

## 5. Move — test-only commit first, source move second

1. After explicit human approval, Scout delegates Surgeon again for the approved test-only commit and, only if separately authorized, the one specified Move. If approval/evidence is stale, renew it before proceeding.
2. Surgeon rechecks the index and approved state, stages only approved tests and necessary test helpers/fixtures, and inspects the entire staged diff. Exclude production source, setup assets, logs, reports, and unrelated work. If unrelated staged changes prevent isolation, stop for the human to resolve them.
3. Commit only the approved tests. Verify the commit succeeded and inspect its complete contents. Record the full baseline commit ID in evidence.md and return it for Scout to record in brief.md. If commit creation or contents cannot be verified, stop before source changes.
4. **A setup commit is not the baseline.** A commit containing agents, skills, prompts, instructions, workflow documentation, or run reports cannot substitute for the verified characterization-test-only commit. Do not invent a baseline ID or create an empty commit to simulate Pin; if no test changes exist, stop for a human decision about the unmet test-only commit prerequisite.
5. Enter the separate source-only Move step only after the verified baseline commit and explicit Move authorization. Freeze baseline tests/helpers/fixtures. Capture the exact pre-move source state for isolated rollback, including any earlier edits.
6. Make exactly one authorized structural source move. Re-run the same approved checks with comparable configuration, save exact post-move results in evidence.md, read them back, and inspect the complete diff for unchanged baseline and approved scope.
7. On red, save the failure and undo only this Move's source edits. Retain the test-only commit, evidence, and all pre-existing/unrelated work. Avoid broad reset, clean, restoration, or history rewriting that could discard other changes. If rollback ownership is unclear, report BLOCKED and request help instead of discarding work.
8. Verify the rollback diff and rerun baseline checks only if authorized; save actual results and stop. A green rollback is not a green Move and does not trigger Reviewer. If checks cannot complete, report BLOCKED and request retry/rollback guidance, never claim green.

**Green-Move gate:** Require the verified test-only commit ID, exactly the approved source move still present, completed green post-move evidence, and unchanged pinned baseline. Only then may Scout delegate independent Reviewer, subject to any remaining human gate. No source commit, push, or additional move is implicitly authorized.

## 6. Verify — independent verdict

1. Scout delegates Reviewer in a fresh independent context only after the green-Move gate. Surgeon does not duplicate that delegation in a Scout-managed run. If delegation is unavailable, report BLOCKED and offer the gated handoff; never replace independence with self-review.
2. Supply the approved brief/approval records, full baseline commit ID and contents, exact evidence.md output, recorded pre-move state, complete committed/staged/unstaged diff, relevant untracked changes, and approved same-run verdict.md path. Use the [Verify prompt](../.github/prompts/verify.prompt.md) when applicable.
3. Reviewer cites evidence for unchanged pinned tests/helpers/fixtures, actual check results and revision applicability, one-move scope, preserved behavior, and gate compliance. Missing proof prevents a favorable verdict; distinguish missing evidence (BLOCKED) from demonstrated violations (CHANGES REQUIRED).
4. Reviewer saves and reads back verdict.md in this same run directory. It may not edit source/tests, fix defects, rewrite evidence, or run artifact-producing checks under verdict-only permissions. Additional checks require separate authorization by the working agent.
5. Scout may record the verdict reference and findings in brief.md. A FAVORABLE verdict is a review assessment, not acceptance or permission to commit/merge.

**Human gate: acceptance or repair.** Present the cited verdict and suggest human acceptance if supported, or separately authorized evidence gathering/repair otherwise. Stop. Do not repair, merge, commit source, or start another Move automatically. Any changed scope or baseline requires explicit decisions and renewed applicable approvals.

## Start when ready

Invoke `/start-refactor` with a survey scope to request a run. This document's creation does not invoke it, generate a run directory, execute checks, or grant any later approval.