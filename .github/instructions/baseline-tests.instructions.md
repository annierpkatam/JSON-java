---
description: "Use when creating or reviewing baseline characterization tests for JSON-java, recording current behavior and defects, or preserving pinned tests during an authorized safe refactor."
applyTo: "src/test/java/org/json/junit/**/*.java"
---

# Baseline characterization tests

- The workspace-relative `applyTo` pattern was verified by file search against existing Java files, including [XMLTokenerTest.java](../../src/test/java/org/json/junit/XMLTokenerTest.java). It covers tests and their Java helpers beneath this directory; it does not cover production source or test resources. Pattern verification is not evidence of test execution or instruction auto-attachment at runtime.
- Follow the authorization and human-gate rules in [repository instructions](../copilot-instructions.md). Creating this file does not authorize test changes, test execution, refactor discovery, or implementation.

## Record current behavior

- When baseline work is explicitly authorized, characterize the unmodified production code, not the desired behavior after a change. Record inputs, setup, observable outputs, exceptions, and relevant side effects as supported by evidence.
- Include reproducible current defects and quirks in the characterization baseline. Clearly distinguish observed behavior from intended behavior; preserving a defect as a baseline does not endorse it or authorize fixing it.
- Do not invent defects, expected values, or passing results. If behavior has not been executed or otherwise established, mark it UNKNOWN and identify the evidence or authorized check needed.
- Identify which tests, assertions, helpers, and fixtures form the pinned baseline. Record their revision or diff and the actual pre-change check results so later comparisons have a stable reference.

## Separate baseline and source steps

- Never edit baseline tests and production source in the same step. Treat baseline establishment and source changes as separate, reviewable workflow steps, not merely separate tool calls in one step.
- Establish and review the baseline against unchanged production source first. Obtain any required human approval before starting a separate, explicitly authorized source-change step.
- During a source-change step, keep pinned tests and their supporting helpers and fixtures unchanged. Never rewrite, weaken, remove, skip, or change pinned expectations to make a move or refactor pass.
- If a pinned test fails after a move, report the mismatch and investigate the source change rather than adapting the baseline to it. Stop at applicable human gates.
- If an intentional behavior change or a baseline error requires different expectations, stop and request an explicit decision. Handle any approved re-baselining in a separate test-only step with the rationale and old/new behavior recorded; never use it to conceal a failed move.

## Verification evidence

- Use only authorized checks and the evidence-backed test guidance in [repository instructions](../copilot-instructions.md). Do not silently install dependencies or use source-mutating strict-mode checks during a baseline-only step.
- Report PASSED, FAILED, NOT RUN, or BLOCKED for each check. For executions, include the exact command, working directory, exit code when available, relevant observed output, and reported test counts/failures. Identify existing failures separately from new mismatches; neither may be hidden.
- Inspect the step's diff to confirm baseline and production-source edits are not mixed and pinned tests, helpers, and fixtures remain unchanged during source moves. Missing output or an unexecuted command is not a passing result.