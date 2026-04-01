# Ryan - Project Portfolio Page

## Project: PathLock

PathLock is a desktop CLI application designed to help Computer Engineering (CEG) students at NUS plan and track
their multi-year academic journey. It enables students to record completed modules, monitor MC progress towards the
160-MC graduation requirement, check prerequisite and post-requisite chains, and organise planned modules across
semesters — all offline, with data stored in a human-editable file.

Given below are my contributions to the project.

---

- **New Feature**: Added the `prereq` command.
    - What it does: displays the prerequisite modules required before a student can take a specified module.
    - Justification: This feature helps students plan their semester workload by understanding module dependency
      chains before registering for modules.
    - Highlights: Retrieves prerequisite data from the JSON module database. Handles unrecognised modules,
      modules with no prerequisites, and case-insensitive input.

- **New Feature**: Added the `postreq` command.
    - What it does: shows all modules that are unlocked upon completing a given module.
    - Justification: This feature gives students forward visibility into how completing a module opens up future
      options, aiding long-term planning.
    - Highlights: Scans the full module list to find all modules listing the given module as a prerequisite.
      Required traversal of the prerequisite graph across all modules to perform the reverse lookup.

- **New Feature**: Added the `count` command.
    - What it does: summarises a student's MC progress towards the 160-MC graduation requirement, showing
      completed MCs, remaining MCs, and percentage progress.
    - Justification: Students need a quick way to check how far along they are in their degree without manually
      adding up MCs.
    - Highlights: Counts MCs from both internal (recognised) and external modules. Caps remaining MCs at 0
      when completed exceeds 160.

---

- **Enhancements to existing features**:
    - Added MC value mapping for all CEG required modules and implemented OR group handling for internship and
      capstone requirements (e.g., `CG4002 OR CG4001 OR CP4106`). This underpins the `count`, `list incomplete`,
      and `list needed` commands across the project
      (PR [#31](https://github.com/AY2526S2-CS2113-F14-3/tp/pull/31)).
    - Refactored the module data layer from hardcoded lists to JSON-based loading using Gson. Created `ModuleLoader`
      to parse `modules.json` at startup and restructured `Module.java` to support prerequisite, preclusion, and
      OR group data. This was a large refactor (16 files, ~600 lines added) that all team members' features depend on
      (PR [#68](https://github.com/AY2526S2-CS2113-F14-3/tp/pull/68)).
    - Added help entries for `prereq`, `postreq`, and `count` commands in `HelpCommand`.

---

- **Code contributed**:
  [RepoSense link](https://nus-cs2113-ay2526s2.github.io/tp-dashboard/?search=ryanzx-zone&breakdown=true)

---

- **Project management**:
    - Set up the initial JUnit test infrastructure (configured `build.gradle` for JUnit 5) and wrote the first
      test suite covering `Module`, `ModuleList`, `Parser`, and `DuplicateException`
      (PRs [#36](https://github.com/AY2526S2-CS2113-F14-3/tp/pull/36),
      [#48](https://github.com/AY2526S2-CS2113-F14-3/tp/pull/48))
    - Enabled assertions in `build.gradle` run configuration

---

- **Documentation**:
    - User Guide:
        - Added documentation for the `prereq`, `postreq`, and `count` commands with usage examples and
          expected output (PR [#96](https://github.com/AY2526S2-CS2113-F14-3/tp/pull/96))
    - Developer Guide:
        - Added implementation sections for the `prereq`, `postreq`, and `count` commands
          (PR [#92](https://github.com/AY2526S2-CS2113-F14-3/tp/pull/92))
        - Added sequence diagrams for all three commands and the `count` class diagram
          (PR [#94](https://github.com/AY2526S2-CS2113-F14-3/tp/pull/94))
        - Wrote the value proposition and non-functional requirements sections
          (PR [#63](https://github.com/AY2526S2-CS2113-F14-3/tp/pull/63))

---

- **Community**:
    - Fixed test compilation errors across 8 test files after the `AppState` refactor
      (PR [#80](https://github.com/AY2526S2-CS2113-F14-3/tp/pull/80))
    - Wrote and maintained tests for teammates' features (`DoneCommand`, `RemoveCommand`,
      `ListCompletedCommand`, `ListIncompleteCommand`, `ListNeededCommand`, `HelpCommand`)
    - Fixed `Storage` null `filePath` bug and renamed `storageTest.java` to follow naming conventions
