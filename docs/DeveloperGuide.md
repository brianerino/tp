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

![Class diagram of done & remove command](https://img.plantuml.biz/plantuml/png/bLN1Rjim3BqRy3y8EJbTKsnNemXQj1aGeDsAPkXf5rRYKg6oP4Xo42pBlq-oBKTAt6xD9HBro4SzKlILiQ1jKuayU-4C2UMQ2impowr0SYF067BBOQsXAWbJ4Wb8HZHMQkEELLNvRGj3asVcbSiQ79PGRxgsLlFdnkAIAhdYlv5yphCyWsTZDL3RWSXa5mpxdpB4d10mG_nTdX7t-uHRf2vQ0NMzic2n9DVzQbgIbUEIwppRUuekygtBzoZ6h5Ai4SxAyE3I7T2IBAN5DUgFqBM-Buv1eFVM4iHzIr5qJ6OSkzZ-0njTux5L-tylyc6L7a0Rr8F8EzSHbjCzgRszPorSNHiRt5sStZxgusK_SOXgaR27t0R-DTfU5EVLhUwuiOdJmJou0ME9bh4mqmDcLFSOUqvy8t1fWrFNws_7BicpKW91zdXk7f6gjUG6tydesClVcdvL-ZqFbzG1xRIi1Th0ORnQNf3BRZyVqMje7JhU_KFycIzdOuBygtRNKrUWNmVkOXfPbvAUsBc9iD6TJt9x0i4PM3NIXvlkg2-1rwIW8yKznTCYUjDlmePNImlJrBNWoCu9RckargyyrJH9UEMoX3MU9cfWCxpaj1Y1N2XWRnGAPLEvKUZ_KBjuIlff61UW7Ocn8MIm_7Rp_VxXRl5ZqUt3xhOF7IR7vP_PB7s7oKaoPlAi7miN5_DaZfQ4QdIocGIHZgW84xlEPlF3So_THmIO8R-qgl8izNy7E9Wz9Xe39NbHWeqG9XWAGXnXVFR7BLWEpM78XiDOUcDX8zlaYwl3X9mvMITda_3-75992rocO9z2QAgoxQAUjdSxyFLrTvS5uojxXPAvR_TV)

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

![Sequence Diagram for Internal Module](https://img.plantuml.biz/plantuml/png/XLJ1Yjim5BmR_0yFdsne0jlsb49PIFQma41fs5ilMkab5YjBhlISfd_V9zjnv6JPtYmyCs_cQUp7GC9JrvWy8qq6OO-_ExGIOQF5qOjc1SfPXBLh6c4LbC-Mq5jXOETKPx3AirehX8DpzEhEUPPdGfBpy1BGvrdBubhgLbY2OlL3KBrrygq04U1ok0TvfeuG_uxAXlsCTdfKVsxKBMpmjzM1UjHkUmlOiqrnnFxj-1pjH-Em-3xvWnKKmmhsdnyUlXHzG7qIX4c4F9lWFJNwPc8R7qQlvNAvh6Ra8Tm8Nin79Y7BOfpy2Mnd36iel5EP6nXNmZfuHjaHbg9jzyI4rC6qk3opZizU7si2TqZe9s6qOkompBLJM5OWXJ6euA1ze6-_BDNU_GcW3rm2tmW2RNjQrBqeiQVTblLq-8dI7QqEg0RDALtgB5uMunUpAEGxd6MFgFiHGlrFUHws9wrhb6-S8V3EMuE4ggokqJPTQxHa-jDPOalQsPXM68z2_Us_ZtViXavA3E5ZnsFl8boSi9INwKE2a3gVAdhX9EDcGv8kJvDk-Zmi1cfk_YkYZJlZ2os_mcuTgcKnaqjQSgqOwxsC5VVQKW2FeJDqKythr_68Ll5FvXy0)

The diagram below shows the external module path (`done GEC1001 /mc 4`):

![Sequence Diagram for External Module](https://img.plantuml.biz/plantuml/png/RPH1Zzem48NlaV8VHZabKg6in8bkgvK0zL0YLKLxwyLh3yHQnqvjXwR_lkFWG9BbveZtPZu_FVFYFBE-hLMQUEaLmX7_jAWvmawoisNr1eJH25jJrqmBoFUTHwkPWjA8Lc6H9fKK22TZ_9lfqYHD6FV6mgj3coOD3PTSDanxo3O_cQyEXhzdm1mC7nz5bgnHOXzCsH5FnEbL_NSjvh8htq4wtwlAmrnm94nsnlxNU0xu0HmMtsvyi86iZ-3xVlkqMZt1vvh3EkllAI_Cu-WcQN9pzHC2FlcRS8Z8-NAvB2RcwnsZU37TFBfhdaM0x1EiQOB03pEcwsCkD0KxvAt7d3NDqPDXlF-MNfeC1lAM1x99zmkvEMlfK5pJl3ECHuHeoR2Ox3mnvN126MHfeWqThJnN7inffBym9GN9Ifwl2-1CAHHVVcjVMVFNWJm1fIuTw5Ofe7ezVuNL8sOcnD3GEV8rjWdE5AOViAsGlplWHdicjVk1__BYph5h6oKvsVSTnyPBemCQTjGozo04rtAEpZs8OSGTknVax88vDtMZq0-Hkp7phQQ3PxHkicJKvzkcMQU7EbTK_pT47Q934Hu3qtIZ-Chh7WBoDPHRLoopoPPHT-x5epMliVPMQk_0eckLdnNs_c9UK0lw__aF)

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
![Sequence Diagram for Remove Command](https://img.plantuml.biz/plantuml/png/XLFDReCm3BuZyGuM9tPefMwt7gPAsv5Aqr0V80EtZKO8IqpLnvyJKfhGIUEKmFVZppOxIyBGgBey8qaTGeq_8_ODmhiK9oFK5WmgVK5uqqg9lispismHZbhJbxxcMPw9XhI1WqMJPmEBoKOEeYSejX-2pfLklWiG5cwNHv1XQe2OFrG-lN-mzxZmHhLBw5wtOuULjEHn-se9gBbKSKB_DPnT15Syh5xd6c4BnIrs_RpPl1G-fhm8mYX8diq4JtRLCtLmXr1lkLwldnBo5360Lwbf4hKiWZVpMtnGICtdbh0ANh4P2Kin33Kn9VQFMfTdTn8hx2kupNfgO-m_yrrB-TeHA0y2QQ7HQkYGa3M17nPQBIeYCtAViRC8lIPUdh5lWPV6NVvd7mKJcUwMBsh4L4gI8OoKELPSi9obfq0s3Z9F_yP9N1EZQ54YjtHo1kteceBMUcX4Jrf-dndp3s5VZEp91ed5hE-hjkFy_9_-0W00)

#### Why This Design?

Using `markIncompleted()` rather than deleting the `Module` object keeps the `allModules` map intact. This map is the shared database for `isRecognisedModule()`, `getMcForModule()`, and `listNeededModules()` — all of which depend on all modules being present regardless of completion status.

### Duplicate Module Check Implementation

#### Overview

The duplicate module check prevents a user from recording the same module as completed more than once. It is enforced inside `ModuleList` for both internal and external modules, and surfaces to the user as:
`"Module <code> has already been completed"`.

#### Sequence Diagram
The diagram below shows duplicate module check path:
![Sequence Diagram for Duplicate Module Check](https://img.plantuml.biz/plantuml/png/dPJFQkCm4CRl0ht3u5DoQ27Rdd9O2abl3XJ2Fe0gJTSYjkHAuztaxJkIwtzfMDZwOYnztymtFma_HLA1kgQMWYpL24Tyxz1fXBrLluDgjh3lsjfgHGW7RpgMx2hK9oagQn3UlATNVvP22gN91_WLCKZHSb6hRQiSGR5zKLILNfyAK166ZslHtZlS-QPHpcHT_cxCjQpFKDf8MNKeRmlwJMzIi1G9xdwEdM4BXU7gi3l-s6mUYXpT_aaJJk6a6ELi_Gp3JZoZxWXNgcsFn9Rrp3r6bc8miFTGiaqPqmTR5PzTvyOqXHGiJ7AVsjZ8BDeQ2SrgeKmZ9SbThYo5mUKUQsi2LGTKvW9wA285y9CwBAQXAlY_SJhYvxF6bgntlNvUlEoNni6kW0vt8my75TCVChmYkYb8yQNoI2sjJz2vVZwuBRJ1Eeg08VZmnJsT6DOHmNc22zAjmGUqZGgd1TmaM4BCbXdaQnOzw9j4OwSrnlZV_6RWCZ2-C6XWzGY7NLpVVvzQmDZLz2ziBW_pn6_-3Nm0)

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
