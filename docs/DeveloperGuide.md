# Pathlock Developer Guide

---
## Acknowledgements

{list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

---
## Design

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
## Implementation: Shi Yong

### Class Structure

The diagram below shows the key classes involved in the `done` and `remove` commands and their relationship.

![Class diagram of done & remove command](https://www.plantuml.com/plantuml/png/bLHFJni_4BtlfnZjAU57qA_N5K8We58aQ16fUkfbiAU9XTTUsRCHRSfthyskzmyRelIqw_5xyppFCxxqZ8xhKcSR9Gc4Sa9JneermRL6x-56uTfXMO2qXW2D14UbtOPjMvPXsS38P_vPcGe354ICN34xzLGpBOKrA_MRuFz6WygAVq59lB7IPna-UNOek651lak1rnycQUI0ljFRPG00_z4BYP1zXbMrOcGguAhzcnUm2jfcdRrccMYAi8R6_4LfPQr3K5A2jt5HmD8mhSaTAPIOcv1TK-GwW_h-BV-iaV5xeRSl1w7zyH-ZuqSMy6_6FA3pv9AdEnzWIhn25S99RwPCLNEYXXnjV1yb-Zp13yplXTEPalIQkBKXC9g_E-Mv9_InH44fMyhKYda7IIP_23s5UENASCjfsllZp0KyMQi9JGDN_f64NHlbIHv5IjMApzOT8YZ37JZOLsdYfE2ZIT6AysPzyTwcr2DYRtkdp8juTxLFVg_ULercYUwv4vtD--3IYFTXvNjaN-Zx8tr7hIIodJRRjjcX-yw5cPXUvHGkPeDP_GYLnZ6WVLrLMf4y1NgfI73yYqBp_gWhjWxND3cYnsqtdsFdMrrjKUwx539r2VakViWXAslYipRqExvjeq0hllno_VN-uUxss-tRCYrkOjRq4fp_MIo6upNU6BqQMJlfPsSNGmOK81mXany04dC26H83hX_Y0W92UlZfR9drxCEuBhfON0p7ku2DrNAYDeO8r7e2cRHQqVM1Xwt2TBJ3IGUX_1C0v70ItCnFyXOSWTOmAZF2ncofVuhOCOrc0-cBJDSSoqIVPPTaP5tglm00)

### `done` Command Implementation

#### Overview

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

---
### Duplicate Module Check Implementation

#### Overview

The duplicate module check prevents a user from recording the same module as completed more than once. It is enforced inside `ModuleList` for both internal and external modules, and surfaces to the user as:
`"Module <code> has already been completed"`.

#### Sequence Diagram
The diagram below shows duplicate module check path:
![Sequence Diagram for Duplicate Module Check](https://img.plantuml.biz/plantuml/png/dPJFQkCm4CRl0ht3u5DoQ27Rdd9O2abl3XJ2Fe0gJTSYjkHAuztaxJkIwtzfMDZwOYnztymtFma_HLA1kgQMWYpL24Tyxz1fXBrLluDgjh3lsjfgHGW7RpgMx2hK9oagQn3UlATNVvP22gN91_WLCKZHSb6hRQiSGR5zKLILNfyAK166ZslHtZlS-QPHpcHT_cxCjQpFKDf8MNKeRmlwJMzIi1G9xdwEdM4BXU7gi3l-s6mUYXpT_aaJJk6a6ELi_Gp3JZoZxWXNgcsFn9Rrp3r6bc8miFTGiaqPqmTR5PzTvyOqXHGiJ7AVsjZ8BDeQ2SrgeKmZ9SbThYo5mUKUQsi2LGTKvW9wA285y9CwBAQXAlY_SJhYvxF6bgntlNvUlEoNni6kW0vt8my75TCVChmYkYb8yQNoI2sjJz2vVZwuBRJ1Eeg08VZmnJsT6DOHmNc22zAjmGUqZGgd1TmaM4BCbXdaQnOzw9j4OwSrnlZV_6RWCZ2-C6XWzGY7NLpVVvzQmDZLz2ziBW_pn6_-3Nm0)

---
## Implementation: Brian

### Class Structure

The diagram below shows the key classes involved in the `list` commands and the `help` command, and their relationships.

![Class Diagram of list and help Commands](https://img.plantuml.biz/plantuml/png/fLNHRjiW57stv7z0VihQkTfUhSXgrLHTfAQjb3xsXS1TWuR10gnDo_Bl0unZKzDjMf-2xpttt0ESIowrmSfq3S-pFQE025E4GvxfRqoqME464OwrlZ96iLrdO4sasBAVW3xbMPxv72eEUAUDmiGSdyMrR1eiQ86mHc6D3dc6q3dy068P5hXjjmORgD1LM7seqDRoYvSyEuwqTqmRoz1oC43_bsfAjXOaqBrVs3q0VR-gBy3RYACqicLaHFyhgwtKU_dz5WGep1tjOfvTWS6C0_Nfg56C5L8rc3CDJmwpi4ReJ1gvwnZtT1li6zgVPP_-663rgnq_OgL1zPizMEyPHexel7NHS12pVY6Ug5X90JEu2vvA9hSsd5guh1Sm6qax3bwtNxbxZKh6S2ZYKqlsMHqVj8UCHdiRqniedRVQKbsSa6N_NITq1WSHII6wfCPUCnEcju4zWKuF6zxwBz-IwLD7rsed9GSi1ZKFwbR9BlAKRm-YQo8kj5owc3MXOLBKTQzfVN_zi7cyktcwSL_3UXL8XW5O_YhBvD0d8BDHJc2Y4NsLdmnUdWLFNbpKwJUdGaI1TOwUGUSlojlO-CL8uwRlmGIHL5MMzUZf2bairUYhaasFdqlxIq6ixm_WKzXX9YDIeRtarCgTn8hF1JgpvrsS5ODBxRwai0-HE24dHC55bWhRz2K8Ql-QVWC0)

### `list` Commands Implementation

#### Overview

The three list commands provide different filtered views of the module list:

- `list completed` — shows all modules the user has marked as done.
- `list incomplete` — shows all required CEG modules not yet completed, including OR-group modules (e.g. `CS2103 OR CS2113`).
- `list needed` — shows every required module for graduation.

All three commands are **read-only** — they do not modify any data or write to storage.

#### Design

All three commands follow the same execution pipeline:

```
PathLock (Main) → Parser → ListCommand → AppState → ModuleList → returns String
```

Key design decisions:
- All three commands are **thin wrappers** — each one extracts `ModuleList` from `AppState` and delegates to a single method on `ModuleList`. The formatting and filtering logic lives entirely in `ModuleList`, keeping the command classes focused solely on retrieval.
- Using **three separate command classes** (rather than a single `ListCommand` with a flag) keeps each class independently testable and avoids branching logic inside `execute()`.
- `listIncompleteModules()` and `listNeededModules()` handle **OR groups** by tracking which `orGroup` labels have already been listed, preventing duplicates when multiple modules share the same OR group.

#### Implementation

**Parsing**

`Parser.parseCommand()` performs exact string matching for the three list commands:

```java
if (input.equals("list completed")) {
    return new ListCompletedCommand();
}
if (input.equals("list incomplete")) {
    return new ListIncompleteCommand();
}
if (input.equals("list needed")) {
    return new ListNeededCommand();
}
```

**Execution**

Each command's `execute()` follows the same pattern:

```java
@Override
public String execute(AppState appState) {
    ModuleList modules = appState.getModule();
    assert modules != null : "ModuleList should not be null";
    String result = modules.listCompletedModules(); // or listIncompleteModules() / listNeededModules()
    return result;
}
```
Inside `ModuleList`:

- `listCompletedModules()` iterates over both `allModules` and `externalModules`, collecting entries where `module.isCompleted()` is true, then formats them as a numbered list.
- `listIncompleteModules()` iterates over `allModules`, skipping OR groups already listed and modules already completed. It uses `isOrGroupFulfilled()` to check if any member of an OR group has been completed before listing the group.
- `listNeededModules()` iterates over `allModules` and lists every module or OR group without filtering by completion status.

#### Sequence Diagram

The diagrams below shows the execution path for `list completed`, `list incomplete` and `list needed`.

![Sequence Diagram for list completed](https://img.plantuml.biz/plantuml/png/TP9DQeKm4CVtWTnXS6KNNi15UMAN2W-a1mWwjQ69-fAnzFYT95hKjklv_JzcafidvMYrHWhIP10wVAmuzGWlMerEsHACzWJzR1U3X0FKixLg6gIGGlKqEtZrwAHOs4RtUb4JGLRU5RqtS_-HWV9mRtwF7OlIy3fhEAxUqxQmI3Rr5QsMfID56CTLTnstyx0Q37uHQ9lGERI5ufbBGGdPURSihgG_sJAMbeAfm3AwXCfRdVogUpeAemwAFUElVN8M37YHnAYcPWb-ORyIvchRxsWVhY05OmnN7SCZKbeqFsBX6b8mKEocDaB-59oePDQ6ikPODjdwasyw8hC_lc-piuh2p-Z8wMay1IGy1EpdSEXNGqTeNvw9-3XyQ_OX_mzwmsdW__eD)

![Sequence Diagram for list incomplete](https://img.plantuml.biz/plantuml/png/TPBDQeGm4CVlWRp3uCaUzWKyb1LxAIWiI1yWwDI69kecO-dZTvBeehOzp-__CPDSFIb7YpLIa2Q3qE9zmR53UD5gSCeMOBGdq6CtsTaW8LIJjMhifP12TJGvUFFef9ZPHtTwLYD1LjmKVTHJzvc1yd2hVuyTYzBmFAivx_KHjwP5jAxER3dFBId2E2xRyxYP-iLWC8n0KuVIeIvSdhaL592TjilYKlgBFMDjAHWBCBDpABtsoS_2FdlQIwCIBblCdplbhEZnf8bfTSKQ_CPk8SpLkkFUFrn22iOOBbk61wIqRBx7maMaOE2ocTe8-KDmef9P4zgQGzlawm_6wP8CFupV9sSLyHsW9QV7uP2Gy92m7SIXNmpjeMrx9k8ByRtPX_m_w1N7dh_j3m00)

![Sequence Diagram for list needed](https://img.plantuml.biz/plantuml/png/TP91ReGm34NtaN87YnLTS04MrI2wP4OZeHuW0fT6JO0cfkhnwoGm0aPT-_dvsyd5atAqM2C5QJ88BNulE7O8RreDJjaIZFO48sAFFTIJjMhifP12TJGvUFVef9ZPeJizgv4WA--AFfkf-yf0URWtpsN7JQbuhZKywnP7hPCYrjHdhfhdbXHXB5VjkNoT-iLWa4NWseJ08IeKhvm8IiXsMsLn6Vt3lXXN2aO2oDmSmgvPyaFJoxuhDa6nsP_Mo9dkySJ78KtDFFvYjn3cQbrhxmxBIy4OmrNBy82KziltM3Y250mKczH6o1y69oh9hY6fUIHDJd-GfiCn-91-JCug8luABJaz3WTxmeDzEuH3lnZQGzlcJCG7uHEpX_m_w0N7dd_d7m00)

#### Why This Design?

Delegating all formatting logic to `ModuleList` means the three command classes stay extremely lightweight and identical in structure. If the output format ever needs to change (e.g. adding MC counts next to each module), only `ModuleList` needs updating — not three separate command classes.

---
### `help` Command Implementation

#### Overview

The `help` command gives users two views:
- `help` (no argument) — prints a grouped overview of all available commands.
- `help <command>` (with argument) — prints a detailed breakdown of a specific command, including its purpose, usage format, and example output.

#### Design

```
PathLock (Main) → Parser → HelpCommand → returns String
```

`HelpCommand` does not interact with `AppState` or `ModuleList` for its core logic. All help content is inside `HelpCommand` itself via `buildHelpMap()`. 

Key design decisions:
- **`buildHelpMap()`** stores all detailed help strings in a `LinkedHashMap<String, String>`, keyed by normalised topic name. This separates content from lookup logic, making it easy to add new commands to the help system without changing control flow.
- **`normaliseTopic()`** handles variations in how users might type command names (e.g. extra spaces, mixed case), mapping them to a canonical key before querying the map.

#### Implementation

**Parsing**

```java
if (input.equals("help")) {
    return new HelpCommand(); // no argument constructor, topic = null
}
if (input.startsWith("help ")) {
    String topic = input.substring(5).trim();
    return new HelpCommand(topic); // topic argument constructor
}
```

**Execution**

```java
@Override
public String execute(AppState appState) {
    if (topic == null || topic.isEmpty()) {
        return showGeneralHelp();
    }
    return showDetailedHelp(topic);
}
```

`showGeneralHelp()` builds and returns a fixed formatted string grouping all commands by category (List Commands, Module Management, Module Planner, PathLock System).

`showDetailedHelp()` calls `normaliseTopic()` on the input, then looks up the result in `buildHelpMap()`. If no match is found, it returns a fallback message directing the user to `help`.

```java
private String showDetailedHelp(String inputTopic) {
    Map<String, String> helpMap = buildHelpMap();
    String normalisedTopic = normaliseTopic(inputTopic);
    if (helpMap.containsKey(normalisedTopic)) {
        return helpMap.get(normalisedTopic);
    }
    return dash + "\nNo detailed help found for \"" + inputTopic + "\".\n"
            + "Type 'help' to see all available commands.\n" + dash;
}
```

#### Sequence Diagram

The diagram below shows the execution path for `help`:

![Sequence Diagram for help](https://img.plantuml.biz/plantuml/png/VP51QiCm44Nt0jrXyCfTv09Ub80BpIABWEa11dcmHMr9aSPEZz-H7LFZGDV_pNyVwPmO8q_Ze1LR7WWQka_a3C67nIxYM45FGu3QZoEwLYiqx2Dy9OfQ1JcrnWPq34LrG-wltdmNW0bkrzSuoj4Q7hEBA9w61RZKMccL9N1wboweeCWpYiLlPsHQ35f9bWa1Gkv_bfGh_RR75nfEMzzEMGhNqaR-jLvguUY7pCHKOWWDIxplN6SAji6fzuzFSXHnoFtb_p17wxgZEWFRnYxwAL0BPjq9VgOuMtf0yt8AssovNTvD7aGOJX0fJGCVDMTohVpwBm00)

The diagram below shows the execution path for `help done`:

![Sequence Diagram for help done](https://img.plantuml.biz/plantuml/png/XP9DJiD034RtSmehgx0e1yW2LMeNNHIfKk409cEQ4VF7Z0ESW0DmH4w2fqsgq1An_fxVvyIpp8o9UsSL6xO4UthjoRS4QuE7XAw1Zcm47Jp1AZY7NYk5BOS4ZvcIYZ9jMXFHCrJD3hdRXlQb0iomsrwbIKPEsKMqaP91VymtAwMA7nPtue46glCQLQatRyXK6fIaXH4a5lqWgIVypMH0U5YCpabfNGsefX4UrPkLeFH1RSzKOunxbd3YBAiACvAv2-zhOZIMT2auc-S6ddfZZ-GzndeUwqx4hmKdiG_9eJMP7a8qxLWAtvzVK5tyjQcu0Kx6ET9ptg7sza3yxvSaohtbETDpsP2PzEb5xQNO7wxWyJR_q-KwGXzVYHnVSiv_hR0ah-Lz_m00)

#### Why This Design?

Storing all help content in `buildHelpMap()` as a `LinkedHashMap` means that adding a new command to the help system requires only one change, a new entry in the map, without touching control flow or the general help string separately.

`normaliseTopic()` acting as a pre-processing step keeps `showDetailedHelp()` clean. If future commands have aliases or shorthand (e.g. `lc` for `list completed`), `normaliseTopic()` is the only place that needs updating.

---
## Implementation: Ryan

### Class Structure

The diagram below shows the key classes involved in the `prereq`, `postreq`, and `count` commands and their relationships.

![Class diagram of prereq, postreq and count commands](https://img.plantuml.biz/plantuml/png/lLRFRzCm5BvFsl_mr5CRw0JNQAesiXqcjJ1fSEFYEe_MccC7sv60qlyTssaIkquhc2PwgHD_x_lUxnRWHXkYJ5Fnomi0_Sn4JHIfW7AYzQqnYgqRWmzKYYVs2-4T_EUFyshBIWJA5ENOFwvasN1yCiGq6YeqMrdgomjtRPZX25bMk0ZWZf6DZIU7Mg72xs-Xbjhu0n4bKDa80uMiAlVaBL1dMKlaKm2J7LbR2qBD_giu82T0T6-rSrZtUGC35F07KfkuAQbhvoJcS7iupN9uicaL6sUt3wKUlOi9xnnsjaQ6qJrmkTcWoc7flmT0vRDHo193yjQfY7MQ8cS3z4LEj_byTQyZsjTtkt2L_tUdNvWZeZIgg5CwcTfTTL7QuMjC7s3UjLZ2roc6Z1iqxKnChM5xMZ9j4jlXwiYwxPE6QMPGZoKyBLAlgRy8BkapbXz-_fsb7xmLrTFJIMkPmzhGaKwnNKHpOC8CB-em45j06-frQxGOSTqvEZqJ6r65mf8PxsgWCvApsIanfcqbQuu6ImTQIycHY2JYoGv5Kw6odI65tJRXZlCh9rfwVM8UFXQVLe_B-y_ttPlk-QwNS8Ukswz_p-VnLaXle-bBhiFXQRaF7NvrjJZQOZbGXRPLzJ4gNXkdOD6O1Y0FYuGSkf-B72omrF1DoMeS6eo1fIMSwv5w7dHYLyyBfTXnfNHkPbsLWeZFQYN84QLXpbhsdcm-Ny3a_IIssKhU_Oe6xnqiy_Yimpj-AGnX8e8ViHuQrV8INzSR5AN_e_47)

### `prereq` Command Implementation

#### Overview

The `prereq` command displays the prerequisites needed before taking a specified module. For example, `prereq CS2113` shows that CS2040C must be completed before taking CS2113.

#### Design

The command follows the standard execution pipeline:

```
PathLock (Main) → Parser → PrereqCommand → AppState → ModuleList → Module
```

Key design decisions:
- **`PrereqCommand`** delegates all prerequisite lookup logic to `ModuleList.getPrerequisites()`, keeping the command focused solely on orchestration.
- The module code is normalised to uppercase in the constructor, ensuring case-insensitive matching throughout.

#### Implementation

**Parsing**

`Parser.parseCommand()` checks for the `prereq ` prefix. If the input is bare `prereq` with no module code, a `MissingCommandException` is thrown. Otherwise, the module code is extracted and passed to the `PrereqCommand` constructor.

```java
String prereqPrefix = "prereq ";
if (input.equals("prereq")) {
    throw new MissingCommandException("Please input module code after 'prereq '");
}
if (input.startsWith(prereqPrefix)) {
    String moduleCode = input.substring(prereqPrefix.length()).trim();
    return new PrereqCommand(moduleCode);
}
```

**Execution**

1. `PrereqCommand.execute()` retrieves `ModuleList` from `AppState`.
2. `ModuleList.getPrerequisites()` looks up the module in `allModules` and calls `Module.getPrerequisites()` to get the prerequisite list.
3. If the module is not recognised, a message is returned. If recognised but has no prerequisites, a different message is returned.

### `postreq` Command Implementation

#### Overview

The `postreq` command displays the modules unlocked by completing a specified module. For example, `postreq CS1010` shows all modules that list CS1010 as a prerequisite.

#### Design

```
PathLock (Main) → Parser → PostreqCommand → AppState → ModuleList
```

Key design decisions:
- **`PostreqCommand`** follows the same pattern as `PrereqCommand` for consistency.
- `ModuleList.getModulesUnlockedBy()` iterates through all modules and checks if the given module code appears in each module's prerequisite list. This is a reverse lookup compared to `getPrerequisites()`.

#### Implementation

**Parsing**

Identical pattern to `prereq` — checks for the `postreq ` prefix and throws `MissingCommandException` if no module code is provided.

**Execution**

1. `PostreqCommand.execute()` retrieves `ModuleList` from `AppState`.
2. `ModuleList.getModulesUnlockedBy()` iterates through every module in `allModules`, checking if the target module code appears in each module's prerequisite list.
3. Matching modules are collected and returned as a formatted string.

#### Why This Design?

The split into separate `PrereqCommand` and `PostreqCommand` classes (rather than a single command with a flag) follows the Single Responsibility Principle. Each command has one clear purpose and can be tested independently. The reverse lookup in `getModulesUnlockedBy()` avoids the need for a separate "post-requisite" data structure — it reuses the existing prerequisite data by searching in the opposite direction.

---
## Implementation: Kailer<br>Planner Feature Classes
### Class Structure 

The diagrams below show the key classes involved in the planner feature and their relationships

`planner list`<br>
![Class Diagram of planner list](./Diagrams/PlannerList.png)

`planner add`<br>
![Class Diagram of planner add](./Diagrams/AddToPlannerCommand.png)

`planner remove`<br>
![Class Diagram of planner remove](./Diagrams/RemovePlannerCommand.png)

`planner edit`<br>
![Class Diagram of planner edit](./Diagrams/EditPlannerCommand.png)

---
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

---
## User Stories

|Version| As a ... | I want to ...                                           | So that I can ...                                           |
|--------|----------|---------------------------------------------------------|-------------------------------------------------------------|
|v1.0|New User| see usage instructions                                  | refer to them when I forget how to use the application      |
|v1.0|User| see my output                                           | know what I entered                                         |
|v2.0|User| have a planner mode                                     | plan which mods I want to take when                         |
|v2.0|User| add mods to the planner                                 | -                                                           |
|v2.0|User| be able to edit the mods I have indicated in my planenr | correct any mistakes I made                                 |
|v2.0|User| have a visual indication of my planner                  | so that I can see my whole planned timetable over my course |

---
## Non-Functional Requirements

1. Should work on any mainstream OS (Windows, macOS, Linux) with Java 17 or above installed.
2. All data is stored locally and the application should work fully without internet connectivity.
3. The saved plan file should remain human-readable and editable with a standard text editor.

---
## Glossary

* *glossary item* - Definition
---

## Instructions for manual testing

{Give instructions on how to do a manual product testing e.g., how to load sample data to be used for testing}
