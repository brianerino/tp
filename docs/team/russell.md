# Russell Ng - Project Portfolio Page
## Overview
NoNeck is a command-line module planning application designed for NUS Computer Engineering students to track completed modules and plan their academic journey. It supports personalised user profiles, module tracking, and semester-based planning with persistent storage.

---

## Summary of Contributions

### Code contributed
*((https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2026-02-20T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=RNJH224&tabRepo=AY2526S2-CS2113-F14-3%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)*

---

### Enhancements implemented

#### 1. Multi-User Storage System (Core Feature)

I designed and implemented a full multi-user storage architecture, enabling each user to have independent data for modules, planner, and profile.

- Implemented user-specific file paths:
- Integrated storage across:
- Module completion (`Storage`)
- Planner (`PlannerStorage`)
- Profile (`ProfileStorage`)

Example:
- Storage uses: `data/users/<username>/modules.txt`
- PlannerStorage uses: `data/users/<username>/planner.txt`
- ProfileStorage uses: `data/users/<username>/profile.txt`

**Why this is significant:**
- Enables true multi-user support (not originally in Duke)
- Required redesign of storage layer and constructor logic
- Ensures data isolation and persistence per user

---

#### 2. Planner Storage System

Implemented a dedicated `PlannerStorage` class to persist planned modules.

Key features:
- Saves modules with format:
- - Loads planner data into `PlannerList`
- Ensures:
- Directory auto-creation
- Input validation using assertions
- Logging for debugging

**Technical depth:**
- Parsing structured file format safely
- Linking storage to `Module` objects with semester assignment
- Handling corrupted/invalid input using assertions

---

#### 3. Profile Storage System

Implemented `ProfileStorage` to manage user identity and metadata.

Features:
- Stores:
- Username
- GPA
- Validates file format and throws errors for corruption
- Returns `null` for non-existent users (used for control flow)

**Why it matters:**
- Acts as the entry point for user system
- Enables switching users and personalised state
- Handles error cases robustly (invalid format, missing user)

---

#### 4. User Switching Command

Implemented `SwitchUserCommand` to dynamically switch between user profiles.

Features:
- Loads:
- Profile
- Completed modules
- Planner
- Reconstructs application state via `AppState.update(...)`
- Handles:
- Unknown modules
- External modules
- Partial failures with logging

**Why this is complex:**
- Coordinates multiple storage systems together
- Requires careful ordering:
1. Load profile
2. Load modules
3. Load planner
4. Update app state
- Ensures system consistency across components

---

#### 5. Robust Storage Design (Assertions + Logging)

Enhanced reliability of storage layer by:

- Adding assertions:
- Prevent null inputs
- Enforce correct file formats
- Validate modular credits and fields

- Adding logging:
- INFO → file operations
- WARNING → file creation / failures
- FINE → debugging file parsing

**Impact:**
- Prevents silent data corruption
- Improves debugging and maintainability
- Aligns with good SE practices taught in CS2113

---

### Contributions to the User Guide

- Wrote documentation for:
- Profile creation and switching
- Planner commands
- List commands (completed, incomplete, needed)
- Standardised command format and examples for clarity

---

### Contributions to the Developer Guide

- Documented:
- Storage architecture (multi-user design)
- Profile and planner storage logic
- Created/updated:
- Sequence diagrams for storage operations
- Class diagrams for storage components

---

### Contributions to team-based tasks

- Integrated storage with planner and module systems
- Helped resolve merge conflicts in storage-related features
- Standardised file formats across team components

---

### Review / mentoring contributions

- Helped teammates debug:
- Static vs non-static storage bug
- File path initialisation issues
- Reviewed PRs related to storage and commands

---

### Contributions beyond the project team

- Assisted peers with:
- Git workflows (branching, merging)
- Storage implementation concepts
- Shared debugging strategies for file I/O and assertions