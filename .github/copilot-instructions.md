# Repository instructions

## Setup and proposal scope

- Use a thinking and reasoning model such as GPT Sol or Astra for setup-file creation through proposal completion, where model selection is available. Do not claim a model was selected without evidence.
- Keep implementation out of scope unless explicitly requested. Creating these instructions is not authorization to run tests, discover refactor candidates, create a refactor workflow, or implement a refactor.
- Preserve existing instructions and unrelated content when updating this file. Ask before resolving material conflicts; prefer minimal, nonduplicative changes.

## Evidence-backed test guidance

The following commands are verified from repository documentation/configuration, not from successful execution. Run from the repository root only when execution is authorized. No tests or build commands were executed during this setup.

| Command | Evidence | Execution status |
| --- | --- | --- |
| `mvn clean test` | [README](../README.md), “Tools to build the package and execute the unit tests”; [Maven configuration](../pom.xml) defines test dependencies. | NOT RUN |
| `gradlew clean build test` | [README](../README.md), same section; [Gradle configuration](../build.gradle) applies the Java plugin and configures tests. | NOT RUN |
| `./gradlew.bat clean build test` (Windows PowerShell invocation of the documented Gradle command) | [Windows wrapper](../gradlew.bat) passes arguments to Gradle; [Gradle configuration](../build.gradle) defines the build/test setup. | NOT RUN |
| `mvn test -P test-strict-mode` (optional, source-mutating) | [README](../README.md) and the `test-strict-mode` profile in [Maven configuration](../pom.xml). | NOT RUN |
| `gradlew testWithStrictMode` (optional, source-mutating) | [README](../README.md) and `testWithStrictMode`, `modifyStrictMode`, and `restoreStrictMode` in [Gradle configuration](../build.gradle). | NOT RUN |

The Gradle build/test command also builds artifacts; its `test` task finalizes with `jacocoTestReport`. Strict-mode commands temporarily modify [JSONParserConfiguration](../src/main/java/org/json/JSONParserConfiguration.java). Gradle also creates a backup. Do not treat strict-mode execution as a read-only check or assume restoration succeeds; require authorization for these side effects and verify the resulting working tree when execution is authorized.

Verified paths (repository-relative):

- Test sources: src/test/java/org/json/junit/; test helper data: src/test/java/org/json/junit/data/. Verified by directory inspection.
- Test resources: src/test/resources/. Verified by directory inspection.
- Maven test-result output: target/surefire-reports/; generated site output: target/site/. These are configured artifact paths in [CI](workflows/pipeline.yml), not evidence that reports currently exist or that CI passed.
- Gradle coverage output: build/reports/jacoco/test/html/index.html. Documented in [Gradle configuration](../build.gradle); generation and current existence are UNKNOWN.

Prerequisites and unknowns:

- [Maven configuration](../pom.xml) targets Java 8 source/bytecode and declares JUnit 4.13.2, JSONPath 2.9.0, and Mockito 4.2.0 as test dependencies. [Gradle configuration](../build.gradle) declares the same test dependencies and Java 8 source compatibility.
- [CI configuration](workflows/pipeline.yml) defines Maven test jobs using JDK 8, 11, 17, 21, and 25. This is configuration evidence only, not actual CI status.
- The [wrapper configuration](../gradle/wrapper/gradle-wrapper.properties) specifies Gradle 6.3. The [Windows wrapper](../gradlew.bat) requires Java through JAVA_HOME or PATH; wrapper JAR presence was verified by directory inspection.
- UNKNOWN: installed JDK/Maven versions, local JAVA_HOME/PATH validity, dependency/download availability, and compatibility of the configured Gradle version with the current build script and local JDK.
- UNKNOWN: successful execution, actual test counts/results, runtime coverage, and any uninspected command variants or additional environment prerequisites. Do not invent them. Runtime verification requires explicit execution authorization.

## Gated safe-refactor workflow

- Begin a safe-refactor workflow only after an explicit user request to run one. Setup-file creation or discussion is not that authorization.
- On that request, first read and follow docs/refactor-workflow.md, once it exists, before any refactor analysis or action. If it is missing or unreadable, stop and report the missing prerequisite; do not invent or substitute a workflow.
- Never pre-scan for refactor candidates before explicit authorization. Limited inspection to establish setup/test guidance is not permission for refactor discovery.
- Never skip, assume approval for, or bypass human gates. Pause at every gate until the required human decision is supplied; approval of one gate does not approve later gates.
- Keep implementation outside setup and proposal stages unless explicitly requested, and continue to honor all workflow gates even when implementation is authorized.

## Actual check evidence

- For every check, report its actual status: PASSED, FAILED, NOT RUN, or BLOCKED. For executed checks, include the exact command, working directory, exit code when available, and relevant observed output, including test counts/failures when reported.
- Distinguish configuration verification from successful execution. A proposed command, attempted invocation, absent output, or configured CI job is not evidence of success.
- If a check cannot run, state the reason and missing prerequisite or authorization. If output or completion status is unavailable, say so; do not label the check passed.
- Execute only authorized checks. Do not silently install dependencies, change configuration, or bypass human gates to obtain a passing result.