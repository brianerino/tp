# Brian - Project Portfolio Page

---

## Pathlock: Overview

PathLock is a desktop CLI application designed to help Computer Engineering (CEG) students at NUS plan and track their multi-year academic journey. It enables students to record completed modules, monitor MC progress towards the 160-MC graduation requirement, check prerequisite and post-requisite chains, and organise planned modules across semesters — all offline, with data stored in a human-editable file.

---

## Summary of Contributions

### Code Contributed

View my code contributions on the tP Code Dashboard: [Brian: RepoSense tP Code Contributions](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2026-02-20T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=brianerino&tabRepo=AY2526S2-CS2113-F14-3%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

---

### Enhancements Implemented

#### 1. UserProfile & GPA-Based Workload Recommendation

I designed and implemented the `UserProfile` class, which stores the user's name and GPA and derives a recommended maximum semester workload from it. The GPA-to-workload mapping follows a tiered system:

| GPA Range     | Recommended Max Workload |
|---------------|--------------------------|
| 4.5 and above | 32 MCs                   |
| 4.0 – 4.49    | 28 MCs                   |
| 3.0 – 3.99    | 26 MCs                   |
| Below 3.0     | 24 MCs                   |

The class enforces input validation at construction time. Invalid names or GPAs outside the 2.0–5.0 range throw an `IllegalArgumentException` immediately, so invalid `UserProfile` objects can never exist in the system. 

> **Note:** For students in their first semester of study, acknowledging that their GPA is 0.00, the system also allows for their input, which will automatically assign their workload to a maximum default of 20 MCs.

**Why this matters:** Students planning their semesters have very different capacities depending on their academic standing. A student with a 4.8 GPA taking 32 MCs is very different from a student with a 2.5 GPA attempting the same — this feature gives personalised guidance rather than a one-size-fits-all cap.

---

#### 2. ProfileStorage (Persistent User Profile)

I implemented the baseline `ProfileStorage`, which persists and retrieves the user's profile between sessions. Each user gets a dedicated file at `data/users/<username>_profile.txt` in the format `NAME|GPA`.

On app launch, PathLock checks for a saved profile. If found, it loads it directly and greets the returning user. If not found, it prompts for a GPA and saves the new profile. This means returning users never have to re-enter their GPA.

---

#### 3. Semester Workload Warnings in `planner add`

I extended `AddToPlannerCommand` to check the user's planned workload against both their GPA-based recommended maximum and a fixed minimum of 18 MCs, displaying targeted warnings after every `planner add` call.

Exceeding Maximum Workload Example output:
```
Module CS2113 added to y2s1.
Current workload for y2s1: 36 MCs
[INFO] Maximum workload based on GPA 3.50: 26 MCs
[WARNING] You are exceeding the maximum semester workload.
```

Below Minimum Workload Example output:
```
Module CS2113 added to y2s1.
Current workload for y2s1: 4 MCs
[INFO] Maximum workload based on GPA 3.50: 26 MCs
[WARNING] You are below the minimum workload of 18 MCs for this semester.
```

The module is always added. The warnings inform rather than block, keeping the user in full control of their planner.

**Why this matters:** Over-loading a semester is one of the most common planning mistakes for CEG students. Surfacing this warning in real time, personalised to the user's GPA, is more useful than a generic cap.

---

#### 4. Added `help` Command

I implemented the `HelpCommand`, which supports two modes:
- `help` — displays a grouped overview of all available commands
- `help <command>` — displays detailed usage, examples, and expected output for a specific command

The command uses a `LinkedHashMap<String, String>` (`buildHelpMap()`) to store all detailed help strings keyed by normalised topic name, and a `normaliseTopic()` method to handle case variations before lookup. This means adding a new command to the help system only requires adding one entry to the map.

---

#### 5. List Commands (`list completed`, `list incomplete`, `list needed`)

I implemented the three list commands, which give users different filtered views of their module list:

- `list completed` — shows all modules the user has marked as done, drawn from both internal and external modules
- `list incomplete` — shows all required CEG modules not yet completed, correctly handling OR-group modules (e.g. `CS2103 OR CS2113`) so the group only appears once and is hidden once any member is completed
- `list needed` — shows every required module for graduation regardless of completion status, also handling OR groups

All three commands are read-only and follow the same thin-wrapper pattern: each command retrieves `ModuleList` from `AppState` and delegates entirely to a single method on `ModuleList`. This keeps the command classes minimal and puts all filtering and formatting logic in one place.

---

### Contributions to the User Guide (UG)

- Wrote the skeleton of the User Guide including Table of Contents, Quick Start, FAQ, Known Issues and Command Summary for future modifying. 
- Wrote the PathLock System Commands Section which included:
   - `help` command section, including usage format, both modes (`help` and `help <command>`), and example outputs for all supported topics.
   - `exit` command section
- Wrote the `switch` command section, including usage format and example outputs.
- Wrote the `list completed`, `list incomplete`, and `list needed` command sections, including usage format and example outputs.

---

### Contributions to the Developer Guide (DG)

- Formatted the entire DG and sectioned it to be more readable. Produced the Table of Contents

- Wrote the **Command.java** API section:
    - Explanation on how the Command.java API works
    - Class diagram for related classes
    - Sequence diagrams for general command flow

- Wrote the `list` Commands implementation section:
    - Design pipeline and rationale
    - Parsing and execution walkthrough with code snippets
    - Class diagram for related classes
    - Sequence diagrams for all three list commands

- Wrote the `help` Command implementation section:
    - Design pipeline
    - Parsing and execution walkthrough with code snippets
    - Class diagram for related classes
    - Sequence diagrams for `help` and `help done`
    - Rationale for the `LinkedHashMap` and `normaliseTopic()` design

- Wrote the **UserProfile** and **ProfileStorage** implementation section:
    - Design pipeline 
    - Implementation breakdowns for load/save flows
    - Recommended maximum workload logic
    - Class diagrams for related class
    - Sequence diagrams for **UserProfile** and **ProfileStorage**
  
---

### Contributions to Team-Based Tasks

- Maintained and updated the `HelpCommand` to stay in sync with new commands added by teammates throughout the project
- Updated the User/Developer Guide structure to ensure consistent section formatting across all team members' contributions
- Helped teammates fix Checkstyle or Code errors when there were problems with the Java CI checks. Also edited EXPECTED.txt to fit new implementations.
- Renamed files, reviewed comments whenever applicable for teammates.
- Provided input and thoughts in group chat and during meetings to certain functionalities in design for groupmates.





