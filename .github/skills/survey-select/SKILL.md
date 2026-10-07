---
name: survey-select
description: "Use when explicitly asked to survey and rank small, testable, behavior-preserving refactor candidates for human selection. Compare seams, test evidence, and one safe move; exclude migrations and behavior fixes. Read-only: never edit files or choose a candidate for the user."
disable-model-invocation: true
---

# Survey candidates for human selection

## When to use

Use for an explicitly authorized safe-refactor survey within a user-approved scope, before the human chooses a candidate. Ranking is decision support, not selection or implementation approval. Do not use for setup-file creation, unsolicited refactor discovery, migrations, dependency/platform upgrades, bug fixes, or other intentional behavior changes.

Use a thinking and reasoning model such as GPT Sol or Astra for survey and proposal work where model selection is available. Do not claim a model was selected without evidence.

## Preconditions and boundaries

- First read applicable repository instructions, including .github/copilot-instructions.md. On an explicit safe-refactor request, read and follow docs/refactor-workflow.md before candidate analysis. If it is missing or unreadable, report BLOCKED and stop; do not invent a replacement workflow.
- Require explicit authorization for the survey and honor every human gate. Invocation does not approve later workflow stages. If the permitted survey scope is materially unclear, ask for it before scanning.
- Operate read-only. Do not edit, create, delete, or stage files; execute tests/builds; install dependencies; or perform any proposed move. Return the survey in the response, not a saved report.
- Consult .github/instructions/baseline-tests.instructions.md when evaluating characterization-test needs. Preserve current observable behavior, including defects. Never propose rewriting pinned tests to make a move pass.

## Procedure

1. Confirm authorization, scope, workflow prerequisites, and the current human gate. Stop if they do not permit surveying.
2. Inspect only the authorized source scope and relevant callers, tests, and available check/coverage evidence. Use actual repository paths and symbols; do not invent candidates or report results from unexecuted checks.
3. Find bounded candidates with an identifiable seam: a local boundary where a structural change can be isolated and its behavior observed through tests. Reject candidates needing a migration, behavior fix, public-contract change, or a chain of dependent refactors.
4. Describe exactly one proposed behavior-preserving move per candidate. State the affected boundary, limited change scope, observable invariants, test evidence, gaps, and why the move appears safe. A proposal is not proof of safety.
5. Rank eligible candidates using the rubric below. Cite source/test locations supporting each assessment. Report fewer candidates or none when evidence does not support a useful shortlist; do not pad the list.
6. Present the ranked shortlist, exclusions, uncertainties, and check status. Ask the human to select a candidate by ID, request a narrower survey, or decline. Stop without choosing, defaulting to the top rank, or proceeding to baseline creation or implementation.

## Ranking rubric

Score each dimension from 0 to 2; sum known scores only when all three dimensions are supported by evidence. Label unsupported dimensions UNKNOWN, not zero, and leave the candidate provisionally unranked until evidence is sufficient.

| Dimension | 0 | 1 | 2 |
| --- | --- | --- | --- |
| Seam | No clear isolation boundary; exclude | Local boundary with visible coupling to inspect | Clear, bounded seam with limited dependencies and observable behavior |
| Coverage evidence | No relevant tests found in the inspected scope | Relevant assertions exist, but affected cases or execution evidence are incomplete | Relevant normal, edge, and defect/quirk behavior is tested, with available execution evidence for the inspected revision |
| One safe move | Multiple moves or intentional behavior change required; exclude | One plausible structural move, with unresolved safety risks | One bounded structural move with supported invariants and limited impact |

- Rank by descending total; break ties by seam, then coverage evidence, then one-move score. Explain ties without choosing on the human's behalf. Scores are survey heuristics, not runtime coverage percentages or guarantees.
- Distinguish static test inspection from executed results and measured coverage. Never infer runtime coverage from test names, counts, or the existence of a configured coverage task. Missing coverage reports or execution evidence remain UNKNOWN.
- A score of 2 requires cited evidence, not confidence alone. Treat unresolved risks as prerequisites for a later authorized step, not permission to expand the survey or modify tests/source.

## Expected response

- Authorization/scope and prerequisite status.
- A table with candidate ID, rank, source seam, relevant tests/evidence, three scores and total (or UNKNOWN), exactly one proposed move, invariants, and risks/gaps.
- Brief excluded candidates with reasons, if encountered within scope. Do not investigate excluded migrations or behavior fixes further.
- Actual check status: PASSED, FAILED, NOT RUN, or BLOCKED. For supplied execution evidence, identify its command, working directory, exit code when available, output, and revision applicability; distinguish supplied evidence from checks performed in this survey. Tests/builds performed by this skill are NOT RUN.
- An explicit human-selection question followed by a stop. No selected candidate, file edits, or implementation.

## Completion criteria

The response is evidence-backed and scoped; ranking uses all three dimensions; each eligible candidate has only one proposed structural move; migrations and behavior fixes are excluded; unknowns are explicit; and the human retains selection authority. No files or pinned tests are changed and no later human gate is bypassed.