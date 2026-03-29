# Developer Guide

## Acknowledgements

{list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

## Design & implementation

{Describe the design and implementation of the product. Use UML diagrams and short code snippets where applicable.}

### Command component
API: Command.java

{insert UML diagram here}

How the `Command` Component work:
1. When the user enters a command, the `Command` component identifies the type of command and creates the corresponding Command object.
2. This command is represented as a subclass of `Command`, such as `DoneCommand`, `RemoveCommand`, `CountCommand`, `ListCompletedCommand`, `ListIncompleteCommand`, `ListNeededCommand`, or `AddToPlannerCommand`.
3. The selected command is then executed by calling its `execute(ModuleList modules)` method. During execution, the command interacts with the ModuleList to retrieve, add, remove, or count modules. For example:
- `DoneCommand` adds a completed module to the list and saves the updated data to storage.
- `RemoveCommand` removes a module from the list and saves the updated data to storage.
- `CountCommand` retrieves the total number of MCs completed.
- `ListCompletedCommand`, `ListIncompleteCommand`, and `ListNeededCommand` retrieve different filtered views of the module list. 
4. After execution, the command returns a String result, which is then shown to the user as the system response. This is evident from the shared method signature in Command and the implementations in each subclass.

### Storage component
API: `Storage.java`

{insert UML diagram here}

The storage component:
- can save current completed mods as well as planned mods in a text file
- It can also create more than one list for different users
- different user can also have different iteration of their plan

---

### Class Structure

The diagram below shows the key classes involved in the `done` and `remove` commands and their relationship.

![Class diagram of done & remove command](https://img.plantuml.biz/plantuml/png/bLNBRjim4BmBq3yGFibf5BHN8J0In2vW86c3kCXzJMvb8XGfa9JXDis_bzIRjfmsFedBcPtbx9A-CHQqhGeHHvPRWUHEW35aoI7NK6I4AOa492CQ2xLtswee_BA6eOcZ7MT8i2ZjBr9WiKNjOkQ5on8Sdr0FkxLMysrbSKsLtF3VI3x7KHp1rbWDr3QWFlDh717t-uG7f8wJG5bkB5ZCo6txDS_8ncMKUHoz-KGDV-agFSgH5efLmaKP3fHcWsPaBItcg3yYLtDtJa6WPsi9ug6MI1ebCurT7Vw17HtZgVRvVm_oOPSUGHlKlSclhlsMqpTI-dYhchZkLRQZkpnjV9BZRJyXZ7eHg7UrzVevsjOKnsg-xhcn0Ma8zpHWBF1oRCnym4pwFiPU4Zy8NDgEr4pv-TmPsIeb46IBv-O9gSebD_XEHSSiVshwLUdt6Awe0UsyB0NQJiDujxmXryrwCU5NFpfqUlhx_AblPsKw-pVrgfKkGB_qsiby55rBUXBdPeGTdVcajcSGd85L4tEuRxRQ5dXF4ZhHt5CyJKPt-YoilxKqCLLP2exiak2XH6hzbvUQ1mLlN9MGuscX1lRzJGwRqOaB1UnCer1MHU1tbt_zxUxxm-FzwiUgMNUhPKliteNhFsaQth9W9tX4ugYzz5TNY-2Lp0ZLw4mn0I9yW4QOCJLD5yDbpjoV0p13VcfLn578VmVOXprcTCapibE2JGW660f2765yzSS3blMjDsJFOQgygRIZsEoBwyECN3XRPnUpxdOvf98Mk0p1leHkPB9wHbhPbZRejNrtXmNZEtk3ahc_uRy0)

### `done` Command Implementation

### Overview

The `done` command marks a module as completed and records it towards the user's graduation progress.

- **Internal module** (e.g `done CS2113`): the MC value is lookup automatically from the moduel database.
- **External module** (e.g `done GEC1001`): the user must supply the MC count explicitly via `/mc`.

#### Design

The command follows the standard execution pipeline:

```
PathLock (Main) → Parser → DoneCommand → ModuleValidator → ModuleList → Storage
```

Key design decisions:
- **`ModuleValidator`** centralises all input validation (module code format, MC value, MC mismatch), keeping `DoneCommand` focused solely on orchestration.
- **`DoneCommand`** delegates to two private helpers — `handleInternalModule()` and `handleExternalModule()` — to cleanly separate the two execution paths.

#### Implementation

**Parsing**

`parseDone()` checks for the `/mc` flag by splitting on `"/mc"`. If found, the module code and MC integer are extracted separately. If not found, `mc` is passed as `null`. This means `DoneCommand` receives a fully-parsed command object and never needs to interpret raw strings itself.

```java
if (remaining.contains("/mc")) {
    String[] parts = remaining.split("/mc", 2);
    moduleCode = parts[0].trim();
    mc = Integer.parseInt(parts[1].trim());
} else {
    moduleCode = remaining;
}
```

**Execution**

1. `ModuleValidator.validateModuleCode()` rejects codes that do not match the NUS format (2–3 letters + 4 digits + optional letter).
2. `isRecognisedModule()` determines whether to take the internal or external path.
3. Both paths call `Storage.save(modules.getCompletedModules())` as the final step to persist the change.

#### Sequence Diagram

The diagram below shows the internal module path (`done CS2113`):

![Sequence Diagram for Internal Module](https://img.plantuml.biz/plantuml/png/RPF1Rjim38Rl1lmEX4ysi0JejbC630MI7GeamB2WjrrOYOc5of8hqLtszgDa9qlSt2Jmvy-F5FKO6GCFdIqBDcm9zlGsa5C46uF7WDqAj7S4Qzzrw3JKJuufEBImytgmr9H5QpJ1mNj-yQUoA0jKx0CyHmfbqOkvKQP7nr2jVYAtMwzUAy08vyj7KP3KIHBkk6o4PyB9gdplz5msycrDvApQRUU2lM3YaN9qEYVy10wBxnS-M44rZc3_-U7XIrKMbqXM9KJHzEamOTNBvR99kXnRt9fTuTVLvFa9t61jayfVcq-TIHATI0rCDVRzdf7Js9rdWc2EBOC_N4dVqHejah7_jTTKDw3GMj9mC27ojz-Es-3_H307URVG8ODnEQqipZvIVRSLDnD_aV97PoBfqV42BGmYMTnmSXZo4ESsgEzattQGTUkMrAk0HPbGRub9ryr_titGMwEajnydHJqRxr8BQ0EX_fltz0vI79IY66TKquEdEBvJhSxbHihON8bdkp3oGojRyqBaKciosVehxDQnMTwiXsG-JziHZEC8WU9WEQ4yajFow_u1)

The diagram below shows the external module path (`done GEC1001 /mc 4`):

![Sequence Diagram for External Module](https://img.plantuml.biz/plantuml/png/RPD1Rzim38Nl1lmV29ziOCbI8AUi6meamGwBWM51Rxke4XCBbILFf3lltqzIxDP9UvF0fwVlKTG3iV3SjoRFMBD1EEAV7gr4s6bnzgBTW78MOUlQLbW5vNvWz5OOg9tg3LPvrcY5S7AEdzoGPtac93iFZuG-pxfWhgNkX6KeDZy5DmSddmiG1DFclSY7ewF4V-2o2pmZJbAbVQjkPHU-WoPEglfmApW6J776L1tN4J-2m-BRAnzie4Wj-BxVtgrMT_2vbR0kyknLaCIHD4Yxk1ZfokLoMKLTgYskFMSfocAqBZx1keeCS-inNZY20ygUiHHTTsJ1iVTJCUZgGv1e-eNIdQqcL9S6l9a7UT0ihYnFmb1ARHqZU7rk6Dnf9dWHHgjmLotBTGLI68Fgos_BZNT_2VG9GZCqWUsDWV3gzrzXzPvAA3MDpItK9V7rxUdCja7vJ22TPQ4j_S1_PVLsyQxlZ9Q1Qpz8x5Wx6rbm2AzD7yIaNaeaYa5dREEZnxfumLAwjZF8K-DetjwRUPX6eGcZyuHeOq9KSP243HJZ3scJ3vHhgBTKBQz69XW-ZXFZjMK2ZzGRZeGFQ5NuaFy1)

#### Why This Design?

The split into `handleInternalModule()` and `handleExternalModule()` avoids a deeply nested `if/else` block inside `execute()`. Each helper has a clear, single responsibility — easy to read, test, and extend independently.

Delegating validation to `ModuleValidator` means that if the NUS module code format ever changes, only one class needs updating.

---

### `remove` Command Implementation

#### Overview

The `remove` command undoes a previously recorded completion. For example, `remove CS2113` resets `CS2113` back to incomplete and removes it from the saved progress. It supports both internal and external modules.

#### Design

```
PathLock (Main) → Parser → RemoveCommand → ModuleList → Storage
```

`RemoveCommand` is deliberately thin. It calls `modules.removeModule()`, always saves, and returns a result string. No validation class is needed because `Parser` already ensures the module code is non-empty.

`removeModule()` does **not** delete any `Module` object. For internal modules, it calls `module.markIncompleted()` — reverting status to `INCOMPLETE`. This preserves the integrity of the `allModules` map, which is used by all other commands.

#### Sequence Diagram
The diagram below shows remove module path (`remove CS2113`):
![Sequence Diagram for Remove Command](https://img.plantuml.biz/plantuml/png/XPB1Ri8m38RlbVeELa_b01BRZSE4j1sBDAtY0RpMG4JJT8c3UFmv8PIrcxIUqlZpxzzsjexHijTTdh7YZg2cBqzzG_2cy6XHRy2IDXU2LwCrzcsUdLHBS326Fyqrp_8C6pOMzevidWqYfXerOCzGRDwHJvLfpWMWW_lFRyXAQaBi7oeViNug7xbqezivkZEjxwXIZYEtg-P0BLRnI36Qpg65O1wMBwD7s41nRxj-Mg-VYpmROv4BHeKQmY5PArUhrI9mCRQSoasQA8kakWZ5VmgFdKaANQdnJ2KEGyt89F0ZBEokWljoRdrFPR5ZqF4Ub8F6wA4Z9XaPo2UvoravjbvcGPqZw0tBWdtVWYms_FoVVK19bFJmG6O-qvm5SdYXSjIwcNRJjgPJi-HyQ8ESYvjwZ4guVLg1LJsxX8RYMp4X3_eR)

#### Why This Design?

Using `markIncompleted()` rather than deleting the `Module` object keeps the `allModules` map intact. This map is the shared database for `isRecognisedModule()`, `getMcForModule()`, and `listNeededModules()` — all of which depend on all modules being present regardless of completion status.

### Duplicate Module Check Implementation

#### Overview

The duplicate module check prevents a user from recording the same module as completed more than once. It is enforced inside `ModuleList` for both internal and external modules, and surfaces to the user as:
`"Module <code> has already been completed"`.

#### Sequence Diagram
The diagram below shows duplicate module check path (`remove CS2113`):
![Sequence Diagram for Duplicate Module Check](https://img.plantuml.biz/plantuml/png/XPB1QiCm38Rl1h-3o2azRD1jfnx68DqjWK7P0xYsjfWwjcShh7txAKdJhgLikHZpVv7-N_9B9fMexvmKPCaXRF2hHwyHrbPz9jMjODr7PxKYX2QOdf6gHRsJehK6uIC4seQz558eJI71UyOaHUIYLjkeF46nUbFKra7l2b0Pviirj0uUgz1rofkH6--TkSOc3xNDD59DVHkOn606OuCbk7i-TOOL58QhGxLvM2uV2obE2bF7lWpX7dLFMAeODyJv5rBC8dDDpOWoPkfN5iTY3B4ow66eOJDdYWu9JRaOf-G8b4kep07qB4a1_0t_NHYWDeNlV5x0wrvZ91lyn9_CN6MRrpGQWfO7CJVS8lfprs8gSmxFTSPPhI0cwoa3fXHuc-WoWWy41wHRfkUmdKgxYxWtK-LUQypvxmo_p8-cUT1ee7o2figB-_-T9yoz8xRknoVmmYS_yXy0)

## Product scope
### Target user profile
- Y1-Y4 Computer Engineering Undergraduate Students (JC path)
- did not follow the recommended TimeTable
- has a need to manage complex multi-year university pathways
- can type fast
- is reasonably comfortable using CLI apps


### Value proposition

PathLock provides a lightweight, offline CLI tool for CEG students to organise complex multi-year university pathways,
tracking completed modules, monitoring MC progress, and managing graduation requirements without needing a
database or internet connection.

## User Stories

|Version| As a ... | I want to ...                                           | So that I can ...                                           |
|--------|----------|---------------------------------------------------------|-------------------------------------------------------------|
|v1.0|New User| see usage instructions                                  | refer to them when I forget how to use the application      |
|v1.0|User| see my output                                           | know what I entered                                         |
|v2.0|User| have a planner mode                                     | plan which mods I want to take when                         |
|v2.0|User| add mods to the planner                                 | -                                                           |
|v2.0|User| be able to edit the mods I have indicated in my planenr | correct any mistakes I made                                 |
|v2.0|User| have a visual indication of my planner                  | so that I can see my whole planned timetable over my course |

## Non-Functional Requirements

1. Should work on any mainstream OS (Windows, macOS, Linux) with Java 17 or above installed.
2. All data is stored locally and the application should work fully without internet connectivity.
3. The saved plan file should remain human-readable and editable with a standard text editor.

## Glossary

* *glossary item* - Definition

## Instructions for manual testing

{Give instructions on how to do a manual product testing e.g., how to load sample data to be used for testing}
