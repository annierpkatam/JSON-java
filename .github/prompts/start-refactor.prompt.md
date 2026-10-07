---
name: start-refactor
description: "Start an explicitly authorized Scout survey, persist a unique run's shortlist before displaying choices, create a brief on human selection, and delegate Pin only after scope approval."
agent: Scout
tools: [read, search, edit, agent]
argument-hint: "Provide the survey scope and constraints; confirm the proposed unique run directory when asked."
---

# Start a gated safe-refactor run

Act as [Scout](../agents/scout.agent.md). First read and follow the [safe-refactor workflow](../../docs/refactor-workflow.md) and [repository instructions](../copilot-instructions.md). If the workflow is missing or unreadable, report BLOCKED and stop before surveying or creating run reports. Do not invent a workflow. Creating this prompt is not an invocation or authorization to run it.

Use a thinking and reasoning model such as GPT Sol or Astra through proposal completion where selection is available. Do not claim a model was selected without evidence. Implementation remains out of scope until separately authorized through the required gates.

## Survey inputs and run identity

1. Treat an explicit human invocation requesting a run as authorization for survey/proposal work only, not tests, commits, source edits, or independent Verify. Ask for materially missing survey scope or constraints before inspecting candidates.
2. Allocate a unique, path-safe run ID beneath docs/refactor-runs/. The `<run-id>` notation below is a runtime-generated value, not a literal directory name or a placeholder the human must manually replace. Check that the proposed directory does not exist; choose a new ID on collision rather than reusing or overwriting an earlier run.
3. Present the concrete proposed docs/refactor-runs/<run-id>/ directory and obtain the human's confirmation before writing, as Scout requires. Record that approved run identity for the conversation. If ownership, path safety, or collision status is uncertain, ask and stop.
4. Scout may create this unique run directory as the container for only its shortlist.md and brief.md. Use file-edit capabilities that create required parent directories; do not invoke a shell, broaden tool permissions, or modify unrelated files. If the available tools cannot create the directory/files, report BLOCKED.

## Save first, then show numbered choices

1. Survey only the authorized scope. Use the ranking rubric in the [survey-select skill](../skills/survey-select/SKILL.md): identifiable seam, actual test/coverage evidence, and exactly one proposed behavior-preserving move. Exclude migrations and behavior fixes; retain observed defects as behavior to preserve.
2. Apply the skill's read-only survey procedure; Scout owns the separately authorized persistence of its run reports. Do not ask the survey skill to edit files or choose for the human.
3. Save the evidence-backed numbered candidate list to the concrete docs/refactor-runs/<run-id>/shortlist.md. Include run identity, scope, stable candidate IDs, rankings, source/test evidence, one move per candidate, invariants, risks, and UNKNOWNs. Do not invent candidates or pad an empty shortlist.
4. Re-read that exact saved file and confirm it exists and contains the intended list before displaying numbered choices. A successful-looking write attempt without read-back is not persistence evidence. Display the saved IDs and order, not a different in-memory ranking.
5. If directory creation, writing, or read-back fails, report BLOCKED with the concrete attempted path and observed error. Do not show an unsaved menu, accept a selection against it, or claim the shortlist was saved. If no eligible candidates exist, save/read back that outcome and stop without offering numbered choices.
6. Once persistence is confirmed, report the concrete shortlist path and ask the human to select an ID, narrow the survey, or decline. Stop without choosing or starting Pin.

## `1` starts Brief, not implementation

1. A reply of `1` selects candidate ID `1` from this run's saved shortlist and starts the Brief stage only. Re-read the saved shortlist to resolve the choice. If it is absent, ambiguous, or changed since presentation, stop and ask for reconfirmation.
2. Save docs/refactor-runs/<run-id>/brief.md for the selected candidate: approved-run identity, scope proposal, exactly one source move, returns/effects/errors to pin, allowed test/source paths, proposed evidence-backed checks and side effects, exclusions, unresolved questions, and required approval gates. Proposed checks remain NOT RUN.
3. Re-read the brief to confirm persistence. If saving fails, report BLOCKED and do not delegate. Present the brief and request explicit scope approval for test-only Pin and its specified checks. Stop. Candidate selection or `1` is not scope, commit, or Move approval.

## Scope approval starts Pin only

1. After explicit human scope/check approval, record the approval in the saved brief. Delegate [Surgeon](../agents/surgeon.agent.md) using the agent capability with the selected candidate, concrete saved brief path, approved test scope/checks, and [pin-and-move skill](../skills/pin-and-move/SKILL.md).
2. Limit the delegation to **Pin only**: characterize current returns, effects, and errors, including defects; keep production source unchanged; execute only approved checks; save or return real output for durable persistence; and stop after green for separate human test-only commit approval. Do not commit, Move, or delegate Reviewer at Pin.
3. Surgeon must not edit Scout's shortlist or brief. Provide a human-approved separate evidence path if available; otherwise require actual output returned to Scout for saving in the brief and confirm persistence before requesting commit approval.
4. If delegation is unavailable, report BLOCKED and show a copy-ready Surgeon handoff containing concrete run paths, approved scope/checks, the explicit Pin-only restriction, evidence requirements, and the mandatory commit-approval stop. Showing a handoff is not execution; do not substitute another agent or perform Pin yourself.
5. Follow Scout and the workflow for subsequent gates. Green Pin is not independent Verify. Any later test-only commit, source Move, and Reviewer delegation require their respective approvals and evidence; Reviewer is permitted only after a verified baseline commit and green post-move checks.

## Report at each stop

State the current phase, concrete saved report paths, actual persistence/check status (PASSED, FAILED, NOT RUN, or BLOCKED), observed errors/output where applicable, and the exact next human decision. Never invent authorization, results, commit IDs, or saved files.