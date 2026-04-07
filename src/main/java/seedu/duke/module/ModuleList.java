package seedu.duke.module;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.duke.exception.DuplicateException;

public class ModuleList {

    private static final Logger logger = Logger.getLogger(ModuleList.class.getName());

    private static final int TOTAL_GRADUATION_MCS = 160;

    public final Map<String, Module> allModules;
    private final Map<String, Module> externalModules = new LinkedHashMap<>();

    public ModuleList() {
        this.allModules = ModuleLoader.loadModules();
    }

    public Map<String, Module> getAllModules() {
        return allModules;
    }

    public Module getModule(String moduleCode) {
        return allModules.get(moduleCode.toUpperCase());
    }

    /**
     * Returns the MC value for a given module code.
     *
     * @param moduleCode Module code to look up.
     * @return MC value, or 4 as default if not found.
     */
    public int getMcForModule(String moduleCode) {
        assert moduleCode != null : "Module code should not be null";
        assert !moduleCode.trim().isEmpty() : "Module code should not be empty";

        String code = moduleCode.toUpperCase();

        Module internal = allModules.get(code);
        if (internal != null) {
            int mc = internal.getModularCredits();
            assert mc >= 0 : "MC value should be positive";
            logger.log(Level.INFO, "Retrieved MC for {0}: {1}", new Object[]{code, mc});
            return mc;
        }

        Module external = externalModules.get(code);
        if (external != null) {
            int mc = external.getModularCredits();
            logger.log(Level.INFO, "Retrieved MC for external module {0}: {1}",
                    new Object[]{code, external.getModularCredits()});
            return mc;
        }

        throw new IllegalArgumentException("\"" + code + "\" is not a recognised module.");
    }

    public static int getTotalGraduationMcs() {
        return TOTAL_GRADUATION_MCS;
    }

    /**
     * Checks if the given module code is a recognised required module.
     */
    public boolean isRecognisedModule(String moduleCode) {
        assert moduleCode != null : "moduleCode cannot be null";
        return allModules.containsKey(moduleCode.toUpperCase());
    }

    public void addModule(String moduleCode) throws DuplicateException {
        assert moduleCode != null : "Module code should not be null";
        assert !moduleCode.trim().isEmpty() : "Module code should not be empty";

        String code = moduleCode.toUpperCase();
        logger.log(Level.FINE, "Attempting to add module: {0}", code);

        if (!isRecognisedModule(code)) {
            logger.log(Level.FINE, "Unrecognised module: {0}", code);
            throw new IllegalArgumentException(code
                    + " is not a recognised module in the required list.");
        }

        Module module = allModules.get(code);
        if (module.isCompleted()) {
            logger.log(Level.FINE, "Duplicate module: {0}", code);
            throw new DuplicateException(code);
        }

        module.markCompleted();
        logger.log(Level.FINE, "Module marked as completed: {0}", code);
    }

    /**
     * Overload for backward compatibility with existing code that passes Module objects.
     */
    public void addModule(Module newModule) throws DuplicateException {
        addModule(newModule.getModuleCode());
    }

    public void addExternalModule(String moduleCode, int mc) throws DuplicateException {
        assert moduleCode != null && !moduleCode.isBlank() : "moduleCode must not be null or blank";
        assert mc > 0 : "MC must be positive";

        String code = moduleCode.toUpperCase();
        logger.log(Level.FINE, "Attempting to add external module: {0} (MC={1})",
                new Object[]{code, mc});

        if (externalModules.containsKey(code)) {
            logger.log(Level.WARNING, "Duplicate external module: {0}", code);
            throw new DuplicateException(code);
        }

        externalModules.put(code, new Module(code, mc));

        Module externalModule = externalModules.get(code);
        externalModule.markCompleted();

        logger.log(Level.FINE, "External module added: {0} (MC={1})", new Object[]{code, mc});
    }

    public boolean removeModule(String moduleCode) {
        String code = moduleCode.toUpperCase();
        Module module;
        if (isRecognisedModule(code)) {
            module = allModules.get(code);
        } else {
            module = externalModules.get(code);
        }

        if (module != null && module.isCompleted()) {
            module.markIncompleted();
            return true;
        }
        return false;
    }

    /**
     * Returns a list of completed modules.
     */
    public List<Module> getCompletedModules() {
        List<Module> completed = new ArrayList<>();
        for (Module module : allModules.values()) {
            if (module.isCompleted()) {
                completed.add(module);
            }
        }

        for (Module module : externalModules.values()) {
            if (module.isCompleted()) {
                completed.add(module);
            }
        }


        return completed;
    }

    /**
     * Returns a list of completed modules.
     *
     * @return A numbered list of completed modules or a message
     *         indicating that no modules have been completed.
     */
    public String listCompletedModules() {
        List<Module> completed = getCompletedModules();
        if (completed.isEmpty()) {
            return "No modules completed yet.";
        }

        StringBuilder sb = new StringBuilder("Completed modules:\n");
        for (int i = 0; i < completed.size(); i++) {
            sb.append(i + 1)
                    .append(". ")
                    .append(completed.get(i).getModuleCode())
                    .append("\n");
        }
        return sb.toString().trim();
    }

    /**
     * Returns a list of all modules required for graduation.
     *
     * @return A numbered list of required modules and OR module groups
     *         that are needed for graduation.
     */
    public String listNeededModules() {
        StringBuilder sb = new StringBuilder("Modules required for graduation:\n");
        int index = 1;
        List<String> listedOrGroups = new ArrayList<>();

        for (Module module : allModules.values()) {
            String orGroup = module.getOrGroup();
            if (orGroup != null) {
                if (!listedOrGroups.contains(orGroup)) {
                    listedOrGroups.add(orGroup);
                    List<String> groupMembers = getOrGroupMembers(orGroup);
                    sb.append(index).append(". ")
                            .append(String.join(" OR ", groupMembers))
                            .append("\n");
                    index++;
                }
            } else {
                sb.append(index).append(". ").append(module.getModuleCode()).append("\n");
                index++;
            }
        }
        return sb.toString().trim();
    }

    private List<String> getOrGroupMembers(String orGroup) {
        List<String> members = new ArrayList<>();
        for (Module module : allModules.values()) {
            if (orGroup.equals(module.getOrGroup())) {
                members.add(module.getModuleCode());
            }
        }
        return members;
    }

    private boolean isOrGroupFulfilled(String orGroup) {
        for (Module module : allModules.values()) {
            if (orGroup.equals(module.getOrGroup()) && module.isCompleted()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a summary of completed and remaining MCs with percentage.
     */
    public String countMcs() {
        int completedMcs = 0;
        for (Module module : allModules.values()) {
            if (module.isCompleted()) {
                completedMcs += module.getModularCredits();
            }
        }

        for (Module module : externalModules.values()) {
            if (module.isCompleted()) {
                completedMcs += module.getModularCredits();
            }
        }

        assert completedMcs >= 0 : "Completed MCs should not be negative";
        int remainingMcs = TOTAL_GRADUATION_MCS - completedMcs;
        if (remainingMcs < 0) {
            remainingMcs = 0;
        }
        double percentage = (double) completedMcs / TOTAL_GRADUATION_MCS * 100;
        double remainingPercentage = 100.0 - percentage;

        logger.log(Level.INFO, "MC progress: {0}/{1} MCs completed ({2}%)",
                new Object[]{completedMcs, TOTAL_GRADUATION_MCS, String.format("%.1f", percentage)});

        return String.format("Completed: %d / %d MCs (%.1f%%)\n"
                + "Incomplete: %d MCs (%.1f%%)",
                completedMcs, TOTAL_GRADUATION_MCS, percentage,
                remainingMcs, remainingPercentage);
    }

    /**
     * Returns a list of required modules that have not yet been completed.
     *
     * @return A numbered list of incomplete modules or a message indicating
     *         that all required modules have been completed.
     */
    public String listIncompleteModules() {
        List<String> incompleteModules = new ArrayList<>();
        List<String> listedOrGroups = new ArrayList<>();

        for (Module module : allModules.values()) {
            String orGroup = module.getOrGroup();
            if (orGroup != null) {
                if (!listedOrGroups.contains(orGroup) && !isOrGroupFulfilled(orGroup)) {
                    listedOrGroups.add(orGroup);
                    List<String> groupMembers = getOrGroupMembers(orGroup);
                    incompleteModules.add(String.join(" OR ", groupMembers));
                }
            } else if (!module.isCompleted()) {
                incompleteModules.add(module.getModuleCode());
            }
        }

        if (incompleteModules.isEmpty()) {
            return "All required modules have been completed.";
        }

        StringBuilder sb = new StringBuilder("Incomplete modules:\n");
        for (int i = 0; i < incompleteModules.size(); i++) {
            sb.append(i + 1)
                    .append(". ")
                    .append(incompleteModules.get(i))
                    .append("\n");
        }
        return sb.toString().trim();
    }

    /**
     * Returns the prerequisites for a given module.
     *
     * @param moduleCode The module code to look up prerequisites for.
     * @return A formatted string listing the prerequisites, or a message
     *         indicating no prerequisites or an unrecognised module.
     */
    public String getPrerequisites(String moduleCode) {
        assert moduleCode != null : "Module code should not be null";
        assert !moduleCode.trim().isEmpty() : "Module code should not be empty";

        String code = moduleCode.toUpperCase();
        logger.log(Level.FINE, "Looking up prerequisites for: {0}", code);

        Module module = allModules.get(code);
        if (module == null) {
            logger.log(Level.WARNING, "Unrecognised module: {0}", code);
            return code + " is not a recognised module.";
        }
        List<String> prereqs = module.getPrerequisites();
        if (prereqs == null || prereqs.isEmpty()) {
            logger.log(Level.FINE, "No prerequisites found for: {0}", code);
            return code + " has no prerequisites.";
        }
        logger.log(Level.FINE, "Prerequisites for {0}: {1}", new Object[]{code, prereqs});
        return "Prerequisites for " + code + ": "
                + String.join(", ", prereqs);
    }

    /**
     * Returns the modules that are unlocked by completing a given module.
     *
     * @param moduleCode The module code to look up postrequisites for.
     * @return A formatted string listing modules unlocked, or a message
     *         indicating no modules are unlocked.
     */
    public String getModulesUnlockedBy(String moduleCode) {
        assert moduleCode != null : "Module code should not be null";
        assert !moduleCode.trim().isEmpty() : "Module code should not be empty";

        String code = moduleCode.toUpperCase();
        logger.log(Level.FINE, "Looking up modules unlocked by: {0}", code);

        List<String> unlocked = new ArrayList<>();
        for (Module module : allModules.values()) {
            List<String> prereqs = module.getPrerequisites();
            if (prereqs != null && prereqs.contains(code)) {
                unlocked.add(module.getModuleCode());
            }
        }
        if (unlocked.isEmpty()) {
            logger.log(Level.FINE, "No modules unlocked by: {0}", code);
            return code + " does not unlock any other modules.";
        }
        logger.log(Level.FINE, "Modules unlocked by {0}: {1}", new Object[]{code, unlocked});
        return "Modules unlocked by " + code + ": " + String.join(", ", unlocked);
    }
}
