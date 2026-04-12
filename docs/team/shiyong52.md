# Shi Yong - Project Portfolio Page

### Project: PathLock

PathLock is a desktop CLI application designed to help Computer Engineering (CEG) students
at NUS plan and track their multi-year academic journey. It enables students to record completed
modules, monitor MC progress towards the 160-MC graduation requirement, check prerequisite
and post-requisite chains, and organise planned modules across semesters — all offline, with
data stored in a human-editable file.

Given below are my contributions to the project.


- **New Feature:** `done` Command (Internal and External Modules)

  - What it does: Marks a module as completed and records it towards the user's graduation
    progress. Supports both internal CEG modules (MC looked up automatically) and external
    modules (user supplies MC via `/mc`).
  - Justification: This is the core input mechanism of PathLock — without it, no progress
    can be tracked. Supporting external modules is essential because CEG students routinely
    take cross-faculty electives not in the built-in database.
  - Highlights: Handles two structurally different execution paths through a shared command
    interface. Required designing `ModuleValidator` from scratch to centralise NUS-specific
    validation rules (module code format, MC value, MC mismatch), and splitting
    `DoneCommand.execute()` into `handleInternalModule()` and `handleExternalModule()` to
    avoid deeply nested logic. Integrates tightly with both `ModuleList` and `Storage`.


- **New Feature:** `remove` Command

  - What it does: Undoes a previously recorded module completion, resetting it back to
    incomplete. Supports both internal and external modules.
  - Justification: Users may record a module by mistake or change their academic plan.
    Without `remove`, the only fix would be manually editing the save file.
  - Highlights: The key design decision was calling `module.markIncompleted()` rather than
    deleting the `Module` object for internal modules. This preserves the shared `allModules`
    map that `list`, `count`, `prereq`, and `postreq` all depend on. External modules are
    fully discarded via `externalModules.remove()` since they have no shared dependents.
    An `isRemovable()` guard ensures only completed modules can be removed.


- **New Feature:** Duplicate Module Check

  - What it does: Prevents a user from recording the same module as completed more than
    once. Applies to both internal and external modules and surfaces as a clear error message.
  - Justification: Without this check, duplicate entries would silently inflate the MC count
    and corrupt graduation progress tracking.


- **Code Contributed:** [RepoSense link](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=shiyong52&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2026-02-20T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&filteredFileName=)


- **Project Management:**

  - Generated the v1.0 release on GitHub.
  - Monitored the issue tracker and ensured all issues were closed before each iteration end.

- **Enhancements to existing features:**
  - Added structured `java.util.logging` across `DoneCommand`, `RemoveCommand`,
    `ModuleList`, and `ModuleValidator` at appropriate log levels (`FINE`, `WARNING`,
    `SEVERE`), making runtime behaviour traceable without modifying production output.
  - Enforced case-insensitive module code input consistently via `toUpperCase()` at command
    boundaries (`RemoveCommand` constructor, `removeModule()`), preventing silent mismatches
    from mixed-case user input.
  - Added defensive `assert` statements throughout (`modules != null`, `mc > 0`,
    `moduleCode != null`) to catch programming errors at the earliest possible point.

- **Documentation:**

  - **User Guide:** 
    - Wrote full documentation for `done` (both paths, all error messages, usage examples) and `remove` (success and not-found outputs).
  - **Developer Guide:** 
    - Wrote the `done` command section (class diagram, two sequence
      diagrams, design rationale), `remove` command section (sequence diagram, design
      rationale), duplicate module check section, and the architecture overview with diagram.
  
- **Community:**
  - Set up the initial package structure (`commands` and `module` packages) and wrote the
    foundational `Module.java` and `ModuleList.java` that all teammates built on top of.
  - Identified and fixed Checkstyle errors early and enforced CI hygiene (`Java CI / build`)
    across the team, keeping the main branch consistently buildable.
  - Helped establish the NUS module code format rules now enforced consistently across the
    codebase by `ModuleValidator`.