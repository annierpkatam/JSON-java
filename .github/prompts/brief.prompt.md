---
name: brief
description: "Under Scout, resolve a human selection from the saved shortlist, persist and verify a brief in the same run, request scope approval, then delegate Pin only or provide its handoff."
agent: Scout
tools: [read, search, edit, agent]
argument-hint: "Provide the current run's saved shortlist path and candidate ID (for example, 1), or continue an identified current run."
---

# Brief the selected candidate

Act as [Scout](../agents/scout.agent.md). First read and follow the [safe-refactor workflow](../../docs/refactor-workflow.md) and [repository instructions](../copilot-instructions.md). If the workflow is missing or unreadable, report BLOCKED and stop; do not substitute an invented workflow. Creating this prompt does not invoke it or authorize a refactor.

Use a thinking and reasoning model such as GPT Sol or Astra through proposal completion where model selection is available. Do not claim selection without evidence. Scout never edits tests or production source, runs tests, or commits. Its writes remain limited to this run's shortlist.md and brief.md; tool aliases are not a technical path sandbox.

## Resolve the saved selection

1. Establish the current human-approved run and its concrete saved shortlist path. If no current run can be identified unambiguously, ask for the path; do not pick the newest directory, create a new run, or invent a shortlist.
2. Read the existing shortlist and verify its run identity and authorized survey scope. A reply of `1` selects candidate ID `1` from that saved shortlist, not an in-memory candidate or implicit top recommendation. Resolve other supplied IDs the same way.
3. If the file is absent/unreadable, the ID is missing or ambiguous, or the list changed since it was shown to the human, report the blocker or request reconfirmation and stop. Selection authorizes Brief only, not Pin, commit, Move, or Verify.

## Save Brief in the same run

1. Resolve brief.md as a sibling of the verified shortlist.md, in the same docs/refactor-runs/<run-id>/ directory. `<run-id>` denotes the already established run identity, not a literal directory name or a new value to generate.
2. Inspect any existing brief before writing. Preserve unrelated content and human approval records. Do not silently replace another candidate's approved brief; ask for an explicit decision if scope or ownership conflicts.
3. Prepare and save a bounded scope proposal containing:
   - Run identity, saved shortlist reference, selected ID, and source/test evidence.
   - Objective, seam, exactly one proposed behavior-preserving source move, allowed test/source paths, and exclusions (including migrations, behavior fixes, and bundled cleanup).
   - Current returns, observable effects, and errors to pin, including reproducible defects/quirks; distinguish established evidence from UNKNOWN behavior.
   - Proposed test-only characterization work, evidence-backed check commands/configuration, known side effects, and missing prerequisites. Proposed or unexecuted checks remain NOT RUN; do not invent results or coverage.
   - The proposed durable Pin evidence destination: a separate path only if human-approved, otherwise actual output returned by Surgeon for Scout to save in this brief. Do not invent a log path.
   - Explicit gates: scope/check approval; Pin only; saved actual green evidence; separate human test-only commit approval; verified test-only commit ID; separately authorized one Move with tests frozen; green post-move evidence before independent Reviewer; human acceptance.
4. Re-read the exact saved brief to confirm it exists and contains the intended proposal before claiming success or requesting approval. A write attempt alone is not persistence evidence.
5. On write/read-back failure, report BLOCKED with the concrete attempted path and observed error. Do not claim the brief exists, request approval against an unsaved version, delegate Pin, or edit tests yourself.

## Ask for scope approval; stop

Present the concrete saved brief path and summarize the proposed Pin scope, check commands/side effects, and unresolved questions. Ask the human to approve that scope and authorize its specified test-only Pin checks, or request changes. Stop until an explicit decision is supplied. A reply of `1`, candidate selection, or general agreement is not automatically scope/check approval.

If the proposal changes materially, save and verify the revised brief and renew approval. Record actual human approval in the brief; do not manufacture an approval record.

## After approval — Pin delegation or handoff

1. Once explicit scope/check approval is recorded and the approved brief is saved/read back, delegate [Surgeon](../agents/surgeon.agent.md) with the concrete run/brief/shortlist paths, selected ID, approved test scope, authorized checks/side effects, evidence destination, applicable instructions, and [pin-and-move skill](../skills/pin-and-move/SKILL.md).
2. Specify **Pin only**: characterize current returns/effects/errors including defects; keep source unchanged; edit only approved tests/helpers/fixtures; run only approved checks; save or return real output; and stop after green for separate human test-only commit approval. No commit, Move, independent Verify, or Reviewer delegation at Pin.
3. Surgeon must not edit Scout's shortlist or brief. When output is returned for Scout to save, preserve actual command, working directory, revision/test diff, completion/exit status, observed output, and reported counts/failures in the brief. Confirm durable persistence before asking for commit approval.
4. If delegation is unavailable, report BLOCKED and offer a copy-ready Surgeon handoff with those concrete paths, approval, scope/checks, evidence requirements, and Pin-only stop. State that no Pin was executed. Do not substitute another agent, bypass gates, or edit tests as Scout.
5. After Pin returns, follow Scout's gates: incomplete/red evidence blocks progress; actual green Pin leads only to the separate human test-only commit approval request, never directly to Move or Reviewer.

## Response at each stop

Report the current phase, concrete run/shortlist/brief paths, resolved candidate ID, actual persistence and check statuses (PASSED, FAILED, NOT RUN, or BLOCKED), relevant observed output/errors, and the exact human decision needed. Never invent saved files, results, commit IDs, or authorization.