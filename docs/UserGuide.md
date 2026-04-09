# Pathlock User Guide

PathLock is a **desktop app for planning your CEG modules**, optimised for use via a **Command Line Interface (CLI)**. If you can type fast, PathLock can help you track completed modules, plan future semesters, and monitor your MC progress faster than traditional planners. The app works fully offline, and stores plans in a human-editable file for flexible iteration.

## Table of Contents

- [Quick Start](#quick-start)
- [Features](#features)
    - [Viewing help: `help`](#viewing-help--help)
    - [Listing completed modules: `list completed`](#listing-completed-modules--list-completed)
    - [Listing incomplete modules: `list incomplete`](#listing-incomplete-modules--list-incomplete)
    - [Listing all required modules: `list needed`](#listing-all-required-modules--list-needed)
    - [Marking a module as completed: `done`](#marking-a-module-as-completed--done)
    - [Removing a completed module: `remove`](#removing-a-completed-module--remove)
    - [Counting MC progress: `count`](#counting-mc-progress--count)
    - [Viewing prerequisites: `prereq MODULE_CODE`](#viewing-prerequisites--prereq-module_code)
    - [Viewing modules unlocked: `postreq MODULE_CODE`](#viewing-modules-unlocked--postreq-module_code)
    - [Adding a module to your planner: `planner add MODULE_CODE y#s#`](#adding-a-module-to-your-planner--planner-add)
    - [Removing a module from your planner: `planner remove MODULE_CODE`](#removing-a-module-from-your-planner--planner-remove)
    - [Shifting a module to another semester on your planner: `planner edit MODULE_CODE y#s#`](#shifting-a-module-on-your-planner--planner-edit)
    - [Viewing your planner: `planner list`](#viewing-your-planner--planner)
    - [Saving your data](#saving-your-data)
    - [Exiting the program: `exit`](#exiting-the-program--exit)
- [FAQ](#faq)
- [Known Issues](#known-issues)
- [Command Summary](#command-summary)

---

## Quick Start

1. Ensure you have **Java 17** or above installed on your computer.

2. Download the latest `pathlock.jar` from the releases page.

3. Copy the file to the folder you want to use as the **home folder** for PathLock.

4. Open a command terminal, `cd` into the folder you placed the jar file in, and run:
   ```
   java -jar pathlock.jar
   ```

5. On first launch, PathLock will ask for your **name** and **GPA (2.0–5.0)**. This is used to recommend a maximum semester workload.

6. Type a command and press **Enter** to execute it. Some commands you can try:
  - `list needed` — Lists all modules required for graduation
  - `done CS1010` — Marks CS1010 as completed
  - `count` — Shows your MC progress
  - `help` — Shows all available commands
  - `exit` — Exits the app

7. Refer to the [Features](#features) section below for details on each command.

---

## Features 

> **Notes:**
> - Words in `UPPER_CASE` are parameters to be supplied by you. e.g. in `done MODULE_CODE`, `MODULE_CODE` is a parameter such as `done CS2113`.
> - Module codes must follow the NUS format: 2–3 uppercase letters, 4 digits, and an optional trailing letter (e.g. `CS2040C`, `EE2026`).
> - Commands are **case-insensitive** for module codes (e.g. `done cs2113` works the same as `done CS2113`).

---
### PathLock System Commands

#### Viewing help : `help`

Shows a grouped overview of all commands, or detailed help for a specific command.

**Format:** `help` or `help COMMAND`

**Examples:**
- `help` — shows all commands
- `help done` — shows detailed help for the `done` command
- `help list completed` — shows detailed help for `list completed`

**Example output (help done):**
```
=======================================================================
COMMAND: done
=======================================================================
Purpose:
  Marks a module as completed.

Usage:
  done MODULE_CODE
  done MODULE_CODE /mc NUMBER

Examples:
  done CS2113
  done SEP101 /mc 4
=======================================================================
```

---

### Profile Creation

#### Creating or loading a profile at startup
When the program starts, it prompts the user to enter their name

If the name is not found in storage, the program creates a new profile with that name and then prompts the user to enter their GPA.

If the name is found in storage, the program loads all saved data under that profile.

Example output (new profile):
```
Enter your name: russell
Enter your GPA (2.0 to 5.0): 4.5
Profile saved for russell.
Mar 31, 2026 9:49:27 PM seedu.pathlock.storage.ProfileStorage saveProfile
INFO: Saved profile for user: russell
Recommended max semester workload: 32 MCs
=======================================================================
Mar 31, 2026 9:49:27 PM seedu.pathlock.module.ModuleLoader loadModules
INFO: Loaded 34 modules from JSON
Mar 31, 2026 9:49:27 PM seedu.pathlock.storage.ModStorage load
INFO: Loading modules from file: data/users/russell_modules.txt
Mar 31, 2026 9:49:27 PM seedu.pathlock.storage.ModStorage load
WARNING: Module file not found. Created new file at data/users/russell_modules.txt
```
Example output (existing profile):

```
Enter your name: russell
Mar 31, 2026 10:06:14 PM seedu.pathlock.storage.ProfileStorage loadProfile
INFO: Loaded profile for user: russell
Welcome back, russell!
Saved GPA: 4.50
Recommended maximum semester workload: 32 MCs
```

---
### List Commands

#### Listing completed modules : `list completed`

Shows all modules you have marked as completed.

**Format:** `list completed`

**Example output:**
```
Completed modules:
1. CS1010
2. CS2113
3. CG2111A
```

#### Listing incomplete modules : `list incomplete`

Shows all required CEG modules that you have not yet completed.

**Format:** `list incomplete`

**Example output:**
```
Incomplete modules:
1. CS2040C
2. EE2026
3. CS2103 OR CS2113
```

#### Listing all required modules : `list needed`

Shows the full list of modules required to graduate from CEG.

**Format:** `list needed`

**Example output:**
```
Modules required for graduation:
1. CS1010
2. CS2040C
3. CS2103 OR CS2113
4. CG2111A
...
```

---
### Module Management Commands


#### Marking a module as completed : `done`

Marks a module as completed and records it towards your graduation progress.

**Format:** `done MODULE_CODE` or `done MODULE_CODE /mc NUMBER`

- For **core CEG modules** (e.g. `done CS2113`), the MC value is retrieved automatically from the database.
- For **external modules** not in the CEG curriculum (e.g. `done GEC1001 /mc 4`), you must supply the MC count using `/mc`.
- Module codes are case-insensitive (e.g. `done cs2113` works the same as `done CS2113`).

**Examples:**
- `done CS2113` — marks CS2113 as completed (MC looked up automatically)
- `done GEC1001 /mc 4` — marks external module GEC1001 as completed with 4 MCs

**Example output:**
```
CS2113 has been added.
```

**Example error outputs:**
```
Module CS2113 has already been completed
```
```
Invalid module code format: "BADCODE".
```
```
"GEC1001" is not a recognised module. If this is an external module,
provide its MCs using /mc. Example: done GEC1001 /mc 4
```

---
#### Removing a completed module : `remove`

Removes a previously recorded module completion, resetting it back to incomplete.

**Format:** `remove MODULE_CODE`

- Works for both internal CEG modules and external modules added via `/mc`.
- Module codes are case-insensitive.

**Example:**
- `remove CS2113` — marks CS2113 as incomplete and removes it from your completed list

**Example output:**
```
CS2113 has been removed
```

**Example output (module not in your completed list):**
```
CS2113 is not in your module list
```

---

#### Counting MC progress : `count`

Shows your completed and remaining MC progress towards the 160 MCs required for graduation. The count includes both internal CEG modules and any external modules you have added via `done MODULE_CODE /mc NUMBER`.

**Format:** `count`

**Example output:**
```
Completed: 40 / 160 MCs (25.0%)
Incomplete: 120 MCs (75.0%)
```

#### Viewing prerequisites : `prereq MODULE_CODE`

Shows the prerequisites needed before taking a specified module. Only modules within the CEG curriculum are recognised.

**Format:** `prereq MODULE_CODE`

**Examples:**
- `prereq CS2113` — shows prerequisites for CS2113
- `prereq EE2211` — shows prerequisites for EE2211

**Example output (`prereq CS2113`):**
```
Prerequisites for CS2113: CS2040C
```

**Example output (`prereq EE2211`):**
```
Prerequisites for EE2211: CS1010, MA1511, MA1508E
```

**Example output (module with no prerequisites):**
```
CS1010 has no prerequisites.
```

**Example output (unrecognised module):**
```
FAKE1234 is not a recognised module.
```

#### Viewing modules unlocked : `postreq MODULE_CODE`

Shows the CEG modules that are unlocked by completing a specified module. In other words, it lists all CEG modules that have the given module as a prerequisite.

**Format:** `postreq MODULE_CODE`

**Examples:**
- `postreq CS1010` — shows CEG modules that require CS1010
- `postreq CS2040C` — shows CEG modules that require CS2040C

**Example output (`postreq CS1010`):**
```
Modules unlocked by CS1010: EE2211, CG2111A, CG2028, CS2040C, CS2107, EE2026
```

**Example output (module that does not unlock any others):**
```
EE4204 does not unlock any other modules.
```

> **Note:** Only modules within the CEG curriculum are checked. Modules outside the CEG required list will not appear in the results.

---
### Module Planner Commands

#### Displaying current planner : `planner list`

Shows all mods the user has added to planner, separated by semesters.

**Format** `planner list`

**Example**
```
=======================================================================
y1s1:
CG1111A
y1s2:
CG2111A
y2s1:
y2s2:
CS2113
y3s1:
y3s2:
y4s1:
y4s2:
```
> **Note** If planner list is empty it will still display the semesters 
> ```
> =======================================================================
> y1s1:
> y1s2:
> y2s1:
> y2s2:
> y3s1:
> y3s2:
> y4s1:
> y4s2:
>=======================================================================
> ```

#### Adding mods to planner : `planner add`

Allows the user to add modules for a specific semester in the planner

**Format** `planner add MODULE_CODE SEMESTER`

**Examples**
- `planner add cg1111a y1s1` - adds the module CG1111A to y1s1 of the planner
- `planner add CS2113 y2s2` - adds the module CS2113 to y2s2 of the planner

**Example output (`planner add cg1111a y1s1`):**
```
=======================================================================
Module CG1111A added to y1s1.
Current workload for y1s1: 4 MCs
[INFO] Maximum workload based on GPA 4.09: 28 MCs
[WARNING] You are below the minimum workload of 18 MCs for this semester.
=======================================================================
```
> **Note:** modules are not cap sensitive
> 
#### Removing mods from planner : `planner remove`

Allows the user to remove modules from the planner

**Format** `planner remove MODULE_CODE`

**Examples**
- `planner remove cg1111a` - removes module CG1111a from the planner
- `planner remove CS2113` - removes module CS2113 from the planner

**Example output (`planner remove cg1111a`):**

assumption that cg1111a is in planner
```
=======================================================================
CG1111A has been removed from planner
=======================================================================
```
**Example output (`planner remove cs1231`):**

assumption that cs1231 is not in planner
```
=======================================================================
CS1231 is not found in planner
=======================================================================
```

#### Editing mods in planner : `planner edit`

Allows the user to change which semester modules are shown in planner

**Format** `planner edit MODULE_CODE SEMESTER`

**Examples**
- `planner edit cs1231 y2s2` - changes the module cs1231 to be in y2s2 of the planner

**Example output (`planner edit cs1231 y2s2`):**

assumption cs1231 is in planner
```
=======================================================================
Edited CS1231 to be in y2s2
=======================================================================
```
**Example output (`planner edit cs2113 y2s2`):**

assumption cs2113 is not in planner
```
=======================================================================
CS2113 is not found in planner
=======================================================================
```

---

## FAQ

**Q: How do I transfer my data to another computer?**  
**A**: Copy the entire `data/` folder from your current computer to the same location on the new computer (the folder where `pathlock.jar` is).

**Q: Can I edit the plan file directly?**  
**A**: Yes. The plan file is human-editable. However, if the format is modified incorrectly, Path Lock may fail to load the file or may reset the data.

**Q: Can I add modules that are not in the CEG required list?**  
**A**: Yes. Use `done MODULE_CODE /mc NUMBER` to add external or SEP modules with a custom MC value.

**Q: Is Path Lock case-sensitive?**  
**A**: No. Module codes are case-insensitive.

**Q: Can I use Path Lock without internet access?**  
**A**: Yes. Path Lock runs fully offline and does not require internet connectivity.

---
## Known Issues

1. **Profile is loaded from `data/profile.txt`** — If this file is deleted or corrupted, PathLock will prompt you to create a new profile on the next run.

---
## Command Summary
| Action | Format                         | Example                   |
|---|--------------------------------|---------------------------|
| View all commands | `help`                         | `help`                    |
| View command details | `help COMMAND`                 | `help done`               |
| Mark module as done | `done MODULE_CODE`             | `done CS2113`             |
| Add external module | `done MODULE_CODE /mc NUMBER`  | `done SEP101 /mc 4`       |
| Remove a module | `remove MODULE_CODE`           | `remove CS2113`           |
| List completed modules | `list completed`               | `list completed`          |
| List incomplete modules | `list incomplete`              | `list incomplete`         |
| List all required modules | `list needed`                  | `list needed`             |
| Count MC progress | `count`                        | `count`                   |
| View prerequisites | `prereq MODULE_CODE`           | `prereq CS2113`           |
| View modules unlocked | `postreq MODULE_CODE`          | `postreq CS1010`          |
| Add module to planner | `planner add MODULE_CODE y#s#` | `planner add CS1010 y1s1` |
| View planner | `planner list`                 | `planner list`            |
| Exit PathLock | `exit`                         | `exit`                    |

