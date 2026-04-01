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

## Implementation: Russell

### Class Structure

The diagrams below show the key classes involved in `Storage` and `ProfileStorage`, and their relationships.

![Class diagram of Storage](https://img.plantuml.biz/plantuml/png/RL5DImCn4BtdL-IOKYsUHKfXGH0KX3ru7fDnEpGV9PdjmTB_RZQRrBMOqynxRrxc9Rl447bqLkp9eDGKjKMH3kIF4C59RI4bO8xqEJA-wE1x-aRv80eXUmuH1fHd8VEvb-TinwGlqePXm2WVvMifRkalilWElC-2FdlxeGZMpK-D2QC7XrT-LGKsW56hoPSIFxq5CrhSLlS1UGQK9RyOpTMCLuy49rIk8Ed6QTAU0Xbn4JNXJDI0wzeg7Spl1NVIwToGvxRbTPCkwygwVr8s6c8NqH2d8idpbNSG3rrmHukCSzgsKtllpMRRCYsXP_e7-8Z4-GUMSA_1-uo97NepVVuF)

![Class diagram of ProfileStorage](https://img.plantuml.biz/plantuml/png/VL9DQyCm3BqR_1zqpEXMTjvXzDGn6ACGoiwgexccZXrifuUM_lSf7zwIBSmNIthlwLd2cvWmfBQsKXnDHQ1CK9QaY2VZ6WnOWx8O8wOKpA5DzAgyAys5u56A7e5Ty9_6KfbyG4rmbGBuFC2LEoUZRc2zrXJW2Txw8EEQqYZTOJeMRQJWi2RcbUpbzDrtD2XMt0YhKR9CttDn96HDn3DbJJFSSsAdEtjJIN6J8iyqmVD0gscnc5dVWswGiygt1swO-JpWEzEAluCmyj9W3moQcVcsV_vF_15CwjOsL4g5pdMf5Byslru_ppUmW6__0xBH0ZnntP2h-Bzy0G00)

### Storage Implementation

#### Overview

The Storage class is responsible for persisting and retrieving a user’s completed modules from a text file.

Each user has a separate storage file stored under:
`data/users/<username>_modules.txt`
Each line represents one completed module:
`MODULE_CODE|MC`

Example:
CS2113|4
MA1521|4

### Design

The storage flow follows this pipeline:
```
Command / App → Storage.save(...) → text file → Storage.load() → List<Module> 
```
Key design decisions:

- Each user has a separate file (avoids data clashes)
- Storage centralises all file I/O logic
- Parsing is delegated to getModule() for cleaner code
- Assertions enforce valid inputs and file format

### Implementation

**initialization**
```java
public Storage(String username) {
   assert username != null && !username.trim().isEmpty() : "Username cannot be empty";
   this.filePath = "data/users/" + username.trim() + "_modules.txt";
}
```

**Loading Modules**
```java 
public List<Module> load() throws IOException
```

Steps:

1) Create file and parent directory if necessary
2) Read file line by line
3) Skip empty lines
4) Parse each line into a Module using getModule()
5) Return list of modules

***Parsing Logic***
```java
private Module getModule(String line)
```

- Splits line using "\\|"
- Validates format (exactly 2 fields)
- Parses module code and modular credits
- Creates a Module and marks it as completed

***Saving Data*** 
```java
public void save(List<Module> modules) throws IOException
```

- Overwrites file with current module list
- Writes each module as:
```java
moduleCode|mc 
```

### Sequence Diagram

Save flow

![](https://img.plantuml.biz/plantuml/png/ZPBFQiCm3CRl1h-3ZkcXBx33Q2diDYYCiREmrTRCZesjhAzVEKv7ieLkEGGX_VW-_V6kC6OUWjSA3SSCxuMoLacIphY4FSC-fMNYJJWyqpBvulnAvYCx_gdt6krGMQsg5soVgmCymP0iCe698NbYiX16i6XLwu9Dle8M_A9kAc-gqhqaLBKLfAVh67Od-HsHBvirwanyn4mzZ-Wg4ZwjowLRisdJpSnQOcwfbOynmKasIsWkqqnQWNmpjRQJrb2B3z6E3LLmgcuNEp02_As3-P39EYuFjOBvfRIOxrg3gNUGA-5_SEzwWa9oUXHEA-hjenqQ7ylo1DyluxwqdKZ8Kxq1)

Load Flow

![](https://img.plantuml.biz/plantuml/png/VP9DQiD038NtXhc3bZR45-YYf1IwImc47a2CbMJe_35MI7lxQiRZj7P2lJ2stZTwpvAt8sFYYxEgGSqXmST8IJLHYaQRY9xX9SPbuI0SdcXPV3DsLJbez7xTR1U6ImPLausWVyxjuGbA3C0OhT789dZLP47qwYQC3JYqNJuxrqJElg9vkfR7zYYtFVAvUqITYFUOo7D17oBqPKOU9nkq3BaV3856y84dLJKWr_rHdGYPtkawToS_hDBMcTJ0zQraBeOsX0X7sGAXFekXxyb5_WPhF5NNsg1VJkPN_-Dra-Mpfxct6Upko4cieIgfmq0RSFeVsc7wV0qkMc8gYybWLPVvNDtwUY2tzS_0LWZv6Ny0)

Why This Design?
- Separates persistence logic from business logic
- Easy to modify file format in one place
- Cleaner and more maintainable code
- Supports multiple users naturally

### ProfileStorage Implementation

The ProfileStorage class manages user profile data.

Each user profile is stored at:
`data/users/<username>_profile.txt`

Format:
`NAME|GPA`

Example:
`Russell|4.5`

### Design

Pipeline:
```
App → saveProfile() → file → loadProfile() → UserProfile
```

Key design decisions:

- Profile storage is separate from module storage
- Each user has a dedicated profile file
- getProfilePath() centralises file path logic
- Returns null if profile does not exist

### Implentation

Profile Path
```
private String getProfilePath(String username)
```
builds
```
data/users/<username>_profile.txt
```

Loading Profile

```java 
public UserProfile loadProfile(String username) throws IOException
```

Steps:

1) Validate username
2) Check if file exists
3) Return null if not found
4) Read first valid line
5) Split into name and GPA
6) Return UserProfile

Saving Profile

```java
public void saveProfile(UserProfile profile) throws IOException
```

Writes to file:
```
NAME|GPA
```

Sequence Diagram:

### Save Profile

![](https://img.plantuml.biz/plantuml/png/jLEz3e8m4Dxx58rJYV4578mkk1aJOfmlS68ZbAQjklZmNg4QVjN5XRJVVJ_7SQoj0-EkPS4WTPNX1uk6QO9aAZKenpTQT-vxKvraWGcH8STEAIPy01oDT3rBdn5i6FCNlbZv7Bxa5cx8TQXvY2hTn40Ae0ZSYB4UZOIj75Bbw7PGeeXO6r-C1IZYZVWDU6GPi3sui_2oqKRYfWE5z_huQjgBeccwTmU3ojMQ3yJoaabZnMHqymbQ3JI0Q0Rt_xaD_BOQVh7BDNnxexi_r8FdSpvxpEX9ggbPzMI5L9WWRIOGxQicBIgBOqD-BszxhzUeClghdW00)

### Load Profile
![](https://img.plantuml.biz/plantuml/png/VPDDReGm38NtIDp1IqRggFikHjDDroEDUe08N1erE55YrFRsjSDFWK5aWHBdUyzE7Aw9JUI-SsLXQlOHtXF6iWWIjBKDXXXUGrW7Rj5_M8TtmKsBwxqtsLX7xhKXsdfgbj6cBCf2bt2-Q2fu0UTRi0JFCZ4DX0dJJM7MsJDkcZ5OzM94fiEJkcx8FMsBFCPkXZ-NyaUn7aqaXDzvMeL_uH6lAKn4uYmw8hklniPqYE2FJPmHwPTZK0eQZmd8yx1R5Y1Zwp1VBlLEUeqkuI0U7FT5bwbvux77LQLGBa55pli0FR5rOXWJIoLqnYxmBpXBE40w9g_pRXDd5AcPh1_hMRoRiQP5fDBMFK4Rp6dGB-dThFFREhoocSgbNUKh_yiV)

### Why This Design?
- Clear separation of concerns (modules vs profile)
- Easy to extend (e.g. add more profile fields)
- Avoids duplicated file path logic
- Simple and robust handling of missing profiles

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

![Sequence Diagram for Internal Module](https://img.plantuml.biz/plantuml/png/bLPTR-8u57r7uZzuwGkLfgkaGA1TQJKqmAWI7QhIQbzM6hdn1ImSE-iu7JhL_lUzpWT9QDAP3Iyuvzvpppt9ZTtjV8uDrIO9HRTZk150rl1F0Z80CkDqgsbuJPYIG7mLXbGoqh-L1hIaWjmfbWWuxtQw7HeOfSbJ3Dgk8YJa0O-eDAHtVK_DRgc2VO_Gc1IBzq4Qa_CGdVBK0sQe8PUGHbNMfw6PhYMFJHfPBay3roYQRY6DolzR-RODSl7dKIkv9htCWlMbwmvwQRlyXHgej7CCJXDj1vWMsJ-vodwDuxn6aZMSfrtKIrSQxVVox4z49a8W1uC6bb989HUPu011Og1FesXjCAMgeMPdjoCLtjDykpD4RKveNgZW37CoNtt5e7zE0Ye4CBBXEZP__2tDJgllCU4Rib4wf8PmcQPPxYeRQYmV3tBp-042jPKy1fO1nvwhQgkFj99_yQvbenEekLJD_5WAPR-ge6vMZSJVGR177sAi4WammFhdfJsp918yGBhv8O38S2MjOrHeeEmrdRgVj1SdGG1n_EiTvc-vJQKlq0yALLbINEtmE115padvMj7AzDGgdanHPYJPuRmz0qZhCR3VUpMgMedoPKQcftoeD9Sc9XhYH9YJ4IYcqVv-Ox3XE75Fjz_yrNBrGCxSsUJASTuZtnvTSZOPpCVULHDuISu62t_itZI10wJrl4apxP2SEVEXQy4INgw-tFg5f05U_h0HJ5Nv4t-ocBRWACoRUjEPtuAdsbplncd1KPxhkCx4gygxMytcoqBUrSnntCj6C9Mtk1gx4xS5HtdO_6BWjU0eRtpZECwy1KTv8_JEcLRbhHzN3zClyqBWT30PLBslmPd4nNmm7hP6M94pVtJPpe4o9uxdVL05EKR3uR3-c9_MyuT2fPDU5ettNEAkG4DyMOEz7QktReVWTOFBhLQ9PBuIk9UYRdlbeD8CzEEE1tk9yqxSxFP2IRFcFu2uQF6_TNfVK0nyAmCRQClyqOTrijiD16iy2O0ohn5yKE4h3O5CDQVYdSXfUcnuUr-bD2_3ck_-XFruaCaBt65BwFXKyAqa0TXZJ57vq635uHu_JrnkYLEB-lzTtbSE7MqF96SgIjhNyTGhZNj6AjEyuYdjMGbMXU-uJ1_vNvoP7HavBGAA0cujOBsZJ7stzpTKn525Ph2X-9rzX8D9ZGlHjwDvPLYKlSsPDjp33PLRaUu7Eyu0Jm7AFAj3lWeXV0OTvwk0wy26VWR9qcFeVm00)

The diagram below shows the external module path (`done GEC1001 /mc 4`):

![Sequence Diagram for External Module](https://img.plantuml.biz/plantuml/png/bPPTR-eu48RlaVWFNdf3fUrkKaAWtGyT6a9rjB1KfTNUh7JaEWDO97PER7heLllVTvmFaa3IemqtE3FppZDZEtQtyqbfckXz57OxtOxcEWIoWgzx40p8bDDDGgDR4aW1n9DHH4L0-lv1Go9eI1Oos8TmQI8fqp8XpmeICufHaZCUKw59x_Q1wkrSibsFK4MAmRbJWi6vIvBgr1sco90Zf5wLyQbhnZNdIgUUvV3KSONGT0EfL_xVu9iooDNlHrPoItff2-vzpxOic_mSCUBqqghvAzLGgUeOayQRGZ0wDdzos7wJr6LDAoi_ZxwgWrJAxlToaTwFn46501eqIffA9Eh00TXUGv_6yKfZI9MWrjfg6AgKpKGHhXw1oOtW2eBCK39L1QipK16uEgDQqr11hP9gQBSZ9BvA-6QhYLoVnBxIa0SOic1zvv8m6eOG_FAtqDj4VbE4hmbscYiYzc58SAt--XknZEH7zT4WA1RxQNbPoninwv0rGMyBRAS8aq9JBjGVyDQ_B1cd-pZa3ENy0uDOSoaCDXmmLdsdSMhF62ZrGUjEwioNkmcbhz1dCef3qCKqgcfjnvrHn9oahoMjRADQnfFjL2p_BUwy5m1XMWt1JxsQLYr7kPHHwZdVLmaNMf44r3xK9nkWs93czqC0QuwRxldp5syvNpwI2tiwlh6iSykN9vjSZ0V-oBrfCbwJYy7C6zbtJSO1ohhkk5dM8HUMxzYMLUMPB-y_UmNI01_FQJIcLDxO6yycBNO4SoVkPEgrs5CssxspMko8PrksDNQhU8lbr9yNU3TJdA_hHcEADxiPsMExnOvuMFniuBRO4MzqPrcMts97l27spffKyLPFoyV9lLy0JWRZGRNtDNE6EFC78wVLmq1ElU5rkmPYZYtN_I0BQWmTnwbFy_FAVomehVGnLhNZ0Wy66k5YPJkphzwx7OBF7GutYToBm9CXVgcGspovKIO194zRpdO2DponizSp_AAi-3z0R6pnltLvBwJe-5uwDiYMySCFysIl6mHM-9a4gTzY-233dpG2Caau3SyW9-a5ulqyIzcypDRyzZlgnojDdc219M779o7V2C90Q7DOPfaF3QquFE3dYOkDEJ4gNl-_oeVAzQDjGdAb8gLPZgUzIl2uI37r6zxNNcGOLCqBBj8f_ui7Uak6LWj0aS2kEQosD93VpFlyB2vDKrXJ_Cu-mK6dZOkmRyVcbMvnjfepDhmVQ2esONeWR7a0P2sbVf67V1H1z0A9oaUC9youVW8Hf5VI_m00)

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
![Sequence Diagram for Remove Command](https://img.plantuml.biz/plantuml/png/bLPDZw904BrFud_erOjxc0H44UUm6KITJABhH3HxdBHGAX5ejsbddPtiVz-Y0VaGPhDueVkzL_MggWLQTu-He5oS0xzrrxgZZc2SR2BWyUg4WETu9neAqdvueU8mPywnJMX4iiKjYQCufN0PfqnOGS3Um691G4DNyaexLVg2kMSVvbua93TVLeas6gTxaApqFYuXBeNSVxlw9G-apML2OjazLTNQicZlZGee57MbIsbS1mfFyKst8lzQqYSLfuhxSjw7Ig4kr0Zpj2Ksok42pbb0bvvEja1HCN4bSbcAiVAsOQ34VxBLRTUKKMnoGNbVOqhmCvGgAMflRAHpGLr4tw3hiE3aWm0t4KL53zSHPfgRxAMqLtebPsNoIR7aW6Ta2n2chN3RfLYLBFbCCDWcFGdS2qM4ykZiYyeaioCK_xwuiFD289ldLsivNwv8HvqO8qMvHLxNAkaOsdIeZ-h07kbeCskeZkj03SFgkb4VjaywohIlnc0EpvTFprPcISFBwjU2qfLbMCRCRC3Hc6xgviHgmAKtLHyh3JZQKnLLCVIYlSLoCfrdzaOJHL5xjQ2qDniDLKDjmD4U5Z_Jz0OSxGt7Yg9C6t2qDy3UAMRHdhrUhiodQMRGr0ojsFiId5YSJRLXlv4HcvnOWrvp3BHfABh-IHQCCUZt--KnR-pfAdEfo2j6ewCNugEC1dXOdMFyt_fetH6ynhZSStOENOlv-1f0t_6LWeoxmDS7ppc644L4JRPdB1Isznk8YYt-Kmvl-HI97pcn9cok7toQ9zck2M0BuC34-ma-oV2T1a1Cxb7_ngGfttWVjrdovYLO_Uu_ejlmymoXSstn0al2Zfk-jm-90w68d_f9vajDAoulr7MzS4-K4kl_gtmfl2kR1f96obB6nx7QAyxPBsbJlFj0jintY_322-N8VtYkE921qc0WIw2M2FQ1kknNlB-ZVWH5Q08xYi_PDLo4R5o0VRisBwUTajESU5k18sYuz-L7mC5pWUmO4rjsILS115lWKRfoFEx4r4S8NVabz1S0)

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

![Class Diagram of list and help Commands](https://img.plantuml.biz/plantuml/png/fLNBRjim4BmBq3yGEhbf7RHNmn0Is45g84u2ECTUQ6bh4wL8WQJGHvf_xvAY7hJefe-JHExix8WxIrrfGvLfAfucXXaEPCMfrcJDw47HQa4uquPGKP8Zy9giP5NPXGE1JfCqER8IIC5KmI5Dz1ScQeg9fB28QsCKspS6DeKKE_O3o7kRuc8aUwPxRHGjpCiduOapGdLVv3bD21b0v2ryWmB9PhIkTuOQM91h__PcGNPOIXpIv6MeS8VQaQpcOA0CMF-2Qaos4OMd-rUsWUiUeFn_LH-nAm77tCYQ5GFwJranKn_brriGe2YtjBDfT0s6CWwb2mU5GgoGggBe77Yoc1bweZ5nvBvXtD5jgIk8ZsKRVkTX-SaNFrAbGNbpe7aDAz0QjTtr1p6fv_SZ6MifO0AtcttAg6lTgVbc7S1iPTbmSBhTc-rha39i-oG-jcGRrU51EyXWjFEOra3nE6gBLR52bkqwZ-Zq3Yfa2K6JAcmp4wQr0Pv0exiFthcLAybqNz6Msal9WOfEpOEwLR89FENAWsYgW8iibtOFJMYO57dUQjhShnwsZtStJpTsrRslFKat0CkVytbqw2EGoIX7CC68diH7WvSctfENbtdypbcGGW4wHq-WqnlbF3QyCUAuyNqmGaHLMNplwGL1RAd9PoMh5Z-Lzaf2gEyLS5JDQ5nGFELS_qxAEVHKHmpPQvlFJm17oKi4ZFQo3ncvmAPd5ratAiWZ1Tw6eaSw8eiCx8nqTFXFEhj3LPllf89g3mf3Lo1Az__z1G00)

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

![Sequence Diagram for help](https://img.plantuml.biz/plantuml/png/RP4nReGm44LxIxx3Y8eKUm6AQ2KYXMABbL0EO9aHM07RiGSsny_OG8BOrF_DVr_sDP8AD4-Z56HeH6ZnQqQh4TwCweEQAXXmz52xQLAsaq9fSW4-8WOfF9yQRRoo14LrLpJSdFuiG4MutvxZm4ThUCuQLco633Ir558a2LnUkGigADAC8llDeWYpGGhE4i60J_rRIRdIBqSyqt3P-mxAahaET_AljQcPms_KCs6fl6-9usFdEfEn7Ow3UxoZnQ36r5_-3__I58pjp-wDo2Fxu6QF7UXrABW5msBm0UtJAUox-JO_7By8CnGXO9n7EckkQBlyxJy0)

The diagram below shows the execution path for `help done`:

![Sequence Diagram for help done](https://img.plantuml.biz/plantuml/png/ZPDBJiCm48RtaNA7KLPXKGwG1QgKBh8eKgN20KmyD1P-OK-04t00ZiX9i9sQHg6jMC_tFsoFrvwOer79iY114c6FhoFg7c4ZsC4nrS20qW8t6g4rIZ7Dow8iM4_6mQD7LnOsE8XUMAO9gcR7QDYQ_gK2vc6t_JjsGJGDbxCk91qp4j2rCIg6mEekU443rM-RAfKGRummnPH5028MA1j3ZaxrJ78proG9hBBpBBokCiqnywU0hWqqVc0_4jRCsZs5yTnvgXsm3Fl1l6-Gc938Oyp9_xBcQHGomVVCrkV9Z0uJjMmxCzF6AIQ5nmTZHP_ZuVlp2whbNIxy6o0db49-nVvOuFQ0TFLecNVeHqdd33Fn70iJ8P-sRnywwCC5GNgzVmdY-mL1MgYm904XVw7C6ZLFl-87)

#### Why This Design?

Storing all help content in `buildHelpMap()` as a `LinkedHashMap` means that adding a new command to the help system requires only one change, a new entry in the map, without touching control flow or the general help string separately.

`normaliseTopic()` acting as a pre-processing step keeps `showDetailedHelp()` clean. If future commands have aliases or shorthand (e.g. `lc` for `list completed`), `normaliseTopic()` is the only place that needs updating.

---
## Implementation: Brian

### Class Structure

The diagram below show the key classes involved in `UserProfile` and `ProfileStorage`.

![Class Diagram for UserProfile and ProfileStorage](https://img.plantuml.biz/plantuml/png/RLFBJiCm4Bn7oZ_iCLLfuPm3AYe28V6eg107n65hczHgR4UsKo4G_yuwhTEuRIvnTcTcPoV9p7cqlbLrdkMPbxuMCA_HERYLM5bK9RmvOHVMV4bgeUQmFo-ziLY9G5jBBe19osfwT1kf6oGciA1puxsLgzQB1sRqKlu8k0gKhfT8_-OPq3K1ZKgKi2IQhcAjQh04RjfLc11Auo6t658kXk1HH5V2FnEe6ANANUE-mL1FvQd-AfXHIcWk-1D-lnkxgGto09NQvzdVOOyqaiCg9B9lBT2lYvOi3wsUc0dwSQSpuC7AaU5mAoAZsTqJ7B6sHlBKBNbvD6mJVOv1eTG5sih5OH-VFR7VvCNEhK3VZtCCjHQMABl3EOCJ85EgyNV6XfJFK6BgdR_UsNHwVHng2PI8Ey8CFw0IMESlO5BgAJ84xIv3U8IUqIkc7-0V)

### UserProfile Implementation

#### Overview

The `UserProfile` class stores the user's name and GPA, and uses the GPA to derive a recommended maximum semester workload in MCs. This profile is created once at startup, either loaded from file via `ProfileStorage`, or created fresh from user input.

Each user profile is stored at:
`data/users/<username>_profile.txt`

Format:
`NAME|GPA`

Example:
`Alice|4.50`

### Design

The profile creation and workload recommendation flow follows this pipeline:
```
PathLock (Main) → getOrCreateProfile() → ProfileStorage.loadProfile() → UserProfile
                                       ↘ (if not found) prompt user → UserProfile → ProfileStorage.saveProfile()
```

### Implementation

**Construction**
```java
public UserProfile(String name, double gpa) {
    if (name == null || name.trim().isEmpty()) {
        throw new IllegalArgumentException("Name cannot be empty.");
    }
    if (gpa < 2.0 || gpa > 5.0) {
        throw new IllegalArgumentException("GPA must be between 2.0 and 5.0.");
    }
    this.name = name.trim();
    this.gpa = gpa;
}
```

**Recommended Max Workload**
```java
public int getRecommendedMaxWorkload()
```

Maps the user's GPA to a recommended MC maximum workload per semester:

| GPA Range     | Recommended Max Workload |
|---------------|--------------------------|
| 4.5 and above | 32 MCs                   |
| 4.0 – 4.49    | 28 MCs                   |
| 3.0 – 3.99    | 26 MCs                   |
| Below 3.0     | 24 MCs                   |

### Sequence Diagram

![Sequence Diagram: UserProfile and ProfileStorage](https://img.plantuml.biz/plantuml/png/jLLDIyD04Bq7yXz6JnPg4S5BaLBHuaMbM4IlOp9DboQxSRFn0_pnpiQqsQJRKaLJ2DlPdVVUphmXuyAWDcMMXe4H9YMOqKj9CYAu4fXep8RmK92UQZKNl8ioXdezCqfZGc604Q-gKs6GCvY8H8xIm1JDuaP5oqwqa-tkEKnX40RsBnoFra0mX1HB6Iq0yxpY5Qzeo24AW_LPpgloXt0uaOON7whK834ZZdYnxLuNegtM2b0Ory4Zcz1pTNIknb3jT1zWGgQEMTvVOFf0PqOBcOH1J3k0c2KraDUThLxqBWfJX07miuTsJHZ3tSNjvFDwUW5phJBuxftDtyfiD_qKFexlapikiXrkrB8kDvZUt4XLwpuaEVOwYbi-RF1Tj4sjcrBTtEqNRWnOu2l5BTx6YewT7ocDL4RmZD6o3-Vs5gC3jZIb1QtRAPM1kIfbl0-nBDEqI-WT7zlcezF12HW5PuEJNZLAgz9qnedhwiVynuOqxBbxsj-IdNDkUx6ILMFrzfwBDorOz7emk5X_FHO_cGgVXrPLgomtW7jYHsKaOueXm_SXFC7jPN6uWY5h8_iRyug_5Ly0)


### Why This Design?
- GPA validation at construction time prevents invalid state from continuing through the app
- Centralising the GPA-to-workload mapping in `UserProfile` means the thresholds only need updating in one place

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
    if (moduleCode.isEmpty()) {
        throw new MissingCommandException("Please input module code after 'prereq '");
    }
    return new PrereqCommand(moduleCode);
}
```

**Execution**

1. `PrereqCommand.execute()` retrieves `ModuleList` from `AppState`.
2. `ModuleList.getPrerequisites()` looks up the module in `allModules` and calls `Module.getPrerequisites()` to get the prerequisite list.
3. If the module is not recognised, a message is returned. If recognised but has no prerequisites, a different message is returned.

#### Sequence Diagram

The diagram below shows the execution path for `prereq CS2113`:

![Sequence Diagram for prereq Command](https://img.plantuml.biz/plantuml/png/ZLJ1QkCm4BqN-W-3JsaWfTtsP4feS8ys148_GDYphgWjkz9usSzVZKPA7Bk5pSN8yzwRzqOJ0xmwKfQclhkz0N2VsepAgXuSVareQgpGETNYeTEjWHeDeMKWQUeGjjjJXC6RLgtdvJ1QjFW5nT3toZJRBQDLJOE5ToTStu1qhKTb2BBygEYZ7EhF39I3Oxa279NrFAb-mmxTOZC15MlKOHnFf0W3u71Q0wgXcJeijQC0gTOs8aJAjpTSvpomstlJa4EMSfz-FJu-PLptxxdgYoDdY2Ot2-HT793-umrAH83QOZWLSZm6eS8hlRn8QR_VP7E1kKGY5R1ZMYx71jS80R3zkc_utHma8MC8xer_iPu8DofES-4yY7BJlH-nXxT8ChFDDL3cxo6x9Dm7c5iOODn5iYf5KYvyqauJkkiPd9boVLLvvgck4olktalCMO7NKRG0PY3uFKocaG69IB8PQQ1Tm2cmM0-jqGwRoByFzuwTmoIu4lEElWjobp2N46TMS11ooERmcgRYR5vnAuE8JsI628yjKksdL1_AhJYIXtyX4qF-J_Cgd1-2ZTkUy_FX-Q6u2CjumcEn07oDQG7IdbfqKqVBLItIfdfO-5VogBGXDyE_-XR-A_SF)

#### Why This Design?

`PrereqCommand` is deliberately thin — it retrieves `ModuleList` from `AppState` and delegates entirely to `getPrerequisites()`. This keeps prerequisite lookup logic in one place (`ModuleList`), making it easy to test and modify without touching the command class.

---
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

#### Sequence Diagram

The diagram below shows the execution path for `postreq CS1010`:

![Sequence Diagram for postreq Command](https://img.plantuml.biz/plantuml/png/pLLDZvim4Br7odyOSMAZ9ifMFQ5Lj2azWeGKxGzmOHgi69l5GzNy-_gBn25jrLEqlY0pRzwyUJE8LqrieB4Jy57ESW6WJrpsR60TQ7mVKTPOejRiY7l1Zn9gb8J3mrKH9u6mMXs29lZ6sT68pA1NcWPyXrnr7PFDQlL0LQmcDS2RVh0X_pXMbPaUyPhtJ18aMSMeBLHzIe1fg8gFeQfYWp7DI_g3P3_IC56FokzX-xu_42DqWFtX7b2gPXHOS4qEKXP_W_ZmSc7ZRfhhdRKgW-IoNOVdxVCsiG0Ji64JxMqNGMKwXpw51U4_ZKJV9K0zeBlb_bevfjnGlRpuq6wN9Z51J34bvR2sQPc_DdYFy03RGDqrzbqCycs6Bcnj_8NrI9YouETCkIErHNV6P0C_8ddNqbkWdu21h2VYpi7qJ835NIdB8gRKRrAONSNcw_dMXqks7Q9z8PJgmP2seGfm2Ko8ybhIdj9rMVeIrO1cFYFNd52dJnFcIdt9SYquPRtBiIiq1QDEobaPPtYBzGduZoWJQYM9SQaXYTO8PUCxOO90giai68C3CVTaQCY7HXcWL1J0mEGP5BLcSGFBnfSG6g4rg_rVtEKC1D_FOPSpy_oy_RBDFy5cixbL7zjHN0SxxT-0UXGqNy5ysa03Sz-RDPfBqX7-rPGm_yRbuZS0)

#### Why This Design?

The split into separate `PrereqCommand` and `PostreqCommand` classes (rather than a single command with a flag) follows the Single Responsibility Principle. Each command has one clear purpose and can be tested independently. The reverse lookup in `getModulesUnlockedBy()` avoids the need for a separate "post-requisite" data structure — it reuses the existing prerequisite data by searching in the opposite direction.

---
### `count` Command Implementation

#### Overview

The `count` command displays the user's MC progress towards the 160 MCs required for CEG graduation. It shows completed MCs, remaining MCs, and percentage progress.

#### Design

```
PathLock (Main) → Parser → CountCommand → AppState → ModuleList
```

`CountCommand` is the simplest of the three commands — it has no fields (unlike `PrereqCommand` and `PostreqCommand` which store a module code). It retrieves `ModuleList` from `AppState` and delegates entirely to `ModuleList.countMcs()`.

#### Implementation

**Parsing**

```java
if (input.equals("count")) {
    return new CountCommand();
}
```

**Execution**

`CountCommand.execute()` retrieves `ModuleList` from `AppState` and calls `countMcs()`, which iterates over all completed modules (both internal and external), sums their MC values, and returns a formatted progress string.

#### Sequence Diagram

The diagram below shows the execution path for `count`:

![Sequence Diagram for count Command](https://img.plantuml.biz/plantuml/png/pPJFQiCm3CRlXRw3oAaBz0L22ItPCG53sGCOHsIcYPFQojZZpxBT_KdN7Rlw5Ci_Ivy_YUmTIKlpqCEZhFKAm9sqcQIL0pWypsWKDkYSxF3Gwyw0GaDexwo9DFK8UNvCYk1PoyvBB42Dio6enc6GfitpkwgIYaOBB-wkTlovM9Nl7Mcb9-bzoGeXwRKUrIa3wK3KZw5AIEjDYvB-HT5lJbKyIMcGWhIgkeOEqtGaIHY0m_4QQCPsxh7MZWBIrEiLGs58jYLte80i36t6SR_dg0zEx4aglqd4Kveo_UPBWk2TiiZVKPlWorNbR-zsyQ0iDpylHT0pMwvJyAR5nQc8XonD3Uq24V41KMYeXU-ePiADJ5xSAbXwWppU8KLJ5igYUnxPNX8F0NCB0SnzfMo2IdQdKMwHaSq69ZupMdHpJuk4bvvSJAblWScTc2zqqBV9QaFk6xz7q_fV-7hr0G00)

#### Why This Design?

`CountCommand` has no fields and no branching — it is a single delegation to `ModuleList.countMcs()`. This keeps the command class minimal and puts all MC calculation logic in `ModuleList`, consistent with how `PrereqCommand` and `PostreqCommand` delegate to `ModuleList` for their respective lookups.

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

### `planner list` Command Implementation
#### Overview
The `planner list` command displays all the mods the user has added into the planner in order of semesters.

### Design

The command follows the standard execution pipeline:
```
PathLock (Main) → Parser → ListPlannerCommand → AppState → PlannerList
```

It then loops through the 2D array `course` stored in PlannerList and prints the moduleCode out.A

#### Implementation

**Parsing**

`Parser.parseCommand()` checks for the `planner` then `list`

**Execution**

1. `ListPlannerCommand.execute()` retrieves `PlannerList` from `AppState`.
2. Returns `PlannerList.list()`
3. `PlannerList.list()`
4. The planner stores its modules in `course`, an `ArrayList` of size 8, where each index corresponds to a semester.
5. The method creates a `StringBuilder` named `output` to build the final string efficiently.
6. It iterates through `course` sequentially from index `0` to `7`, ensuring that semesters are listed in order.
7. For each semester, the method first appends a semester header in the format `Semester: X`, where `X` ranges from `1` to `8`.
8. It then retrieves the `ArrayList<Module>` corresponding to that semester.
9. The method iterates through all `Module` objects stored in that semester and appends each module’s code on its own line.
10. After all 8 semesters have been processed, the method returns the constructed string.
11. Even if a semester does not contain any modules, its header is still displayed. This ensures that the planner structure remains explicit and consistently formatted for the user.

#### Sequence Diagram

The diagram below shows the sequence of action upon the user inputting `planner list`
![sequence diagram of planner list](./Diagrams/plannerlist.png)

### `planner add` Command Implementation

#### Overview

The `planner add` command allows the User to add modules to the planner which can then be show with `planner list`

#### Design
```
PathLock (Main) → Parser → AddToPlannerCommand → AppState → PlannerList
```

Key design decisions:
- PlannerList is a separate list from ModuleList as whether a mod is plan or unplanned is independent on whether the User has completed the mod or not
- PlannerList uses a 2D arrayList to allow for easy insertion and removal of mods across all 8 semesters

#### Implementation

**Parsing**
`Parser.parseCommand()` checks for the `planner` then `add`. If subsequent input is bare a `MissingCommandException` is thrown. Otherwise, the module code and semester is extracted and passed to the `AddToPlannerCommand` constructor.

**Execution**
1. `AddToPlannerCommand.execute()` retrieves `ModuleList` and `PlannerList` from `Appstate`
2. `moduleList.getModule(moduleCode)` retrieves Module based of `moduleCode`
3. If module null, means moduleCode does not exist, `IllegalArgumentException` is thrown
4. It sets `Module` semester attribute, if semester is of incorrect format `IllegalArgumentException` is thrown
5. Executes `PlannerList.addModule(module)`
6. It extracts semester, and inserts it into the respective array index of course

#### Sequence Diagram

The diagram below shows the sequence of action upon the user inputting `planner add cs2113 y2s2`
![sequence diagram of planner add](./Diagrams/plannerAdd.png)

### `planner remove` Command Implementation
#### Overview
The `planner remove` command allows the User to remove the modules that are in the planner should they not want it

#### Design
```
PathLock (Main) → Parser → RemoveFromPlannerCommand → AppState → PlannerList
```

Key design decisions:
- `planner remove` does not care if moduleCode exists or not as for it to be added to planner it should exist based on `planner add` implementation

#### Implementation

**Parsing**
`Parser.parseCommand()` checks for the `planner` then `remove`. If subsequent input is bare a `MissingCommandException` is thrown. Otherwise, the module code is extracted and passed to the `RemoveFromPlannerCommand` constructor.

**Execution**
1. `RemoveFromPlanner.execute()` retrieves `PlannerList` from `AppState`
2. Sweeps through every `Module` in `course` and retrieves their `ModuleCode`
3. If it matches, `Module` is removed
4. If no matches found, `NoSuchElementException` is thrown

#### Sequence Diagram

The diagram below shows the sequence of action upon the user inputting `planner remove cs2113`
![sequence diagram of planner remove](./Diagrams/plannerRemove.png)


### `planner edit` Command Implementation
#### Overview
The `planner edit` command allows the User to make changes to what semester they plan to take a module.

#### Design
```
PathLock (Main) → Parser → EditPlannerCommand → AppState → PlannerList
```
Key design decisions:
- `planner edit` removes the current module, changes the `semester` attribute then reinserts it
- This is done to help update the position of the module with respect to the course arrayList

#### Implementation
**Parsing**
`Parser.parseCommand()` checks for the `planner` then `edit`. If subsequent input is bare a `MissingCommandException` is thrown. Otherwise, the module code and semester is extracted and passed to the `EditPlannerCommand` constructor.

**Execution**
1. `EditPlannerCommand.execute()` retrieves `ModuleList` and `PlannerList` from `AppState`
2. `moduleList.getModule(moduleCode)` retrieves Module based of `moduleCode`
3. If module null, means moduleCode does not exist, `IllegalArgumentException` is thrown
4. Sets module's `semester` to inputted semester, if semester is in wrong format `IllegalArgumentException` is thrown 
5. Executes `PlannerList.removeModule(moduleCode)` (see `planner remove` for execution)
6. Executes `PlannerList.addModule(module)` (see `planner add` for execution)

#### Sequence Diagram

The diagram below shows the sequence of action upon the user inputting `planner edit cs2113 y2s2`
![sequence diagram of planner edit](./Diagrams/editplanner.png)

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
