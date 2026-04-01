# Shi Yong - Project Portfolio Page

## PathLock: Overview
PathLock is a desktop CLI application designed to help Computer Engineering (CEG) students at NUS plan and track their multi-year academic journey. It enables students to record completed modules, monitor MC progress towards the 160-MC graduation requirement, check prerequisite and post-requisite chains, and organise planned modules across semesters — all offline, with data stored in a human-editable file.

## Summary of Contributions

---

### Code Contributed

View my code contributions on the tP Code Dashboard: [ShiYong: RepoSense tP Code Contributions](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=shiyong52&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2026-02-20T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&filteredFileName=)

---

## Enhancements Implemented

### 1. `done` Command (Internal and External Modules)

Implemented the full `done` command, which marks a module as completed and records it towards the user's graduation progress.

- **Internal modules** (e.g. `done CS2113`): the MC value is looked up automatically from the module database.
- **External modules** (e.g. `done GEC1001 /mc 4`): the user supplies the MC count via the `/mc` flag.

**Key implementation details:**

- Designed and implemented `ModuleValidator`, a dedicated validation class that centralises all input checks — NUS module code format (2–3 letters + 4 digits + optional letter), MC value validity, and MC mismatch detection. This keeps `DoneCommand` focused solely on orchestration.
- Split `DoneCommand.execute()` into two private helpers — `handleInternalModule()` and `handleExternalModule()` — to cleanly separate the two execution paths, avoiding deeply nested if/else logic.
- Parsing is handled by `parseDone()`, which splits on the `/mc` flag so that `DoneCommand` always receives a fully-parsed command object and never needs to interpret raw strings itself.
- Both paths persist changes by calling `Storage.save(modules.getCompletedModules())` as a final step.

This enhancement is non-trivial because it handles two structurally different execution paths through a shared command interface, enforces NUS-specific validation rules, and integrates tightly with both `ModuleList` and `Storage`.

---

### 2. `remove` Command

Implemented `RemoveCommand` and `ModuleList.removeModule()`. The key design decision is calling `module.markIncompleted()` rather than deleting the `Module` object, which preserves the shared `allModules` map that `list`, `count`, `prereq`, and `postreq` commands all depend on.

---

### 3. Duplicate Module Check

Implemented a duplicate module check inside `ModuleList`, preventing a user from recording the same module as completed more than once. The check applies to both internal and external modules and surfaces to the user as a clear error message.

---

## Contributions to the User Guide

- **`done` command:** Wrote full documentation covering both the internal and external module paths, all error messages (duplicate module, invalid format, missing `/mc`), and usage examples.
- **`remove` command:** Wrote usage documentation, including both the success and not-found output messages.

---

## Contributions to the Developer Guide

- **`done` Command** — Class Structure, Design, Implementation, Sequence Diagrams, and Design Rationale
    - Includes the class diagram for `DoneCommand` and related classes
    - Includes two sequence diagrams: one for the internal module path (`done CS2113`) and one for the external module path (`done GEC1001 /mc 4`)
- **`remove` Command** — Design, Implementation, Sequence Diagram, and Design Rationale
    - Includes the sequence diagram for `remove CS2113`
- **Duplicate Module Check** — Overview and Sequence Diagram
    - Includes the sequence diagram for the duplicate check flow

---

## Contributions to Team-Based Tasks

- **Module validation rules:** Helped set up and agree on the NUS module code format rules now enforced consistently across the codebase by `ModuleValidator`.
- **Project architecture setup:** Set up the initial package structure (`commands` and `module` packages), and wrote the foundational code for `Module.java` and `ModuleList.java` that all other teammates built their features on top of.
- **Code quality enforcement:** Identified and fixed Checkstyle errors early in the project, and reminded teammates to ensure their branches pass the GitHub CI check (`Java CI / build`) before merging pull requests. This helped keep the main branch in a consistently buildable state throughout the project.
- **Release management:** Generated the v1.0 release on GitHub.
- **Issue tracker management:** Monitored the progress of issues on the issue tracker and ensured all issues were marked as completed before closing each project iteration.