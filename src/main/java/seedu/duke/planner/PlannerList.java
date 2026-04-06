package seedu.duke.planner;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import seedu.duke.module.Module;
public class PlannerList {
    private final ArrayList<ArrayList<Module>> course;

    public PlannerList() {
        course = new ArrayList<>(8);
        for (int i = 0; i < 8; i++) {
            course.add(new ArrayList<Module>());
        }
        // Assertion to ensure planner is initialised correctly
        assert course.size() == 8 : "Planner should have exactly 8 semesters.";
    }

    /**
     * Returns the planner list with semesters listed in each semester
     * @return String of planner list
     */
    public String list() {
        StringBuilder output = new StringBuilder();

        output.append(formatYearBlock("Y1", "Y2", 0, 1, 2, 3));
        output.append("\n");
        output.append(formatYearBlock("Y3", "Y4", 4, 5, 6, 7));

        assert !output.isEmpty() : "Output list should not be empty.";
        return output.toString();
    }

    private String formatYearBlock(String leftYear, String rightYear,
                                   int leftS1Index, int leftS2Index,
                                   int rightS1Index, int rightS2Index) {
        StringBuilder block = new StringBuilder();

        block.append("+----------------------+----------------------+\n");
        block.append(String.format("| %-20s | %-20s |\n", leftYear, rightYear));
        block.append("+----------------------+----------------------+\n");

        appendSemesterRows(block, "S1", course.get(leftS1Index), "S1", course.get(rightS1Index));
        block.append("|----------------------|----------------------|\n");
        appendSemesterRows(block, "S2", course.get(leftS2Index), "S2", course.get(rightS2Index));

        block.append("+----------------------+----------------------+\n");
        return block.toString();
    }

    private void appendSemesterRows(StringBuilder output,
                                    String leftSemLabel, ArrayList<Module> leftModules,
                                    String rightSemLabel, ArrayList<Module> rightModules) {
        int maxRows = Math.max(Math.max(leftModules.size(), rightModules.size()), 1);

        for (int i = 0; i < maxRows; i++) {
            String leftText;
            String rightText;

            if (i == 0) {
                leftText = leftSemLabel + ":"
                        + (i < leftModules.size() ? " " + leftModules.get(i).getModuleCode() : "");
                rightText = rightSemLabel + ":"
                        + (i < rightModules.size() ? " " + rightModules.get(i).getModuleCode() : "");
            } else {
                leftText = i < leftModules.size() ? "    " + leftModules.get(i).getModuleCode() : "";
                rightText = i < rightModules.size() ? "    " + rightModules.get(i).getModuleCode() : "";
            }

            output.append(String.format("| %-20s | %-20s |\n", leftText, rightText));
        }
    }

    /**
     * Adds the specified module to the semester list indicated by the module's semester.
     * Also marks the module as planned after it has been added.
     *
     * @param module The module to be added to the planner.
     * @throws IllegalArgumentException If the module's semester is invalid.
     */
    public void addModule(Module module) {
        String semester = module.getSemester();
        course.get(getSemesterIndex(semester)).add(module);
        module.setIsPlanned(true);
        // Assertion to check if module was successfully added
        assert containsModule(module.getModuleCode()) : "Module should have been added successfully.";
    }

    public boolean containsModule(String moduleCode) {
        String code = moduleCode.toUpperCase();
        for (ArrayList<Module> semesterModules : course) {
            for (Module module : semesterModules) {
                if (module.getModuleCode().equalsIgnoreCase(code)) {
                    return true;
                }
            }
        }
        // Assert that the module exists in the planner
        assert !moduleCode.isEmpty() : "Module code should not be empty when checking.";
        return false;
    }

    public String findSemesterOfModule(String moduleCode) {
        String code = moduleCode.toUpperCase();
        for (int i = 0; i < course.size(); i++) {
            for (Module module : course.get(i)) {
                if (module.getModuleCode().equalsIgnoreCase(code)) {
                    return getSemesterLabel(i);
                }
            }
        }
        return null;
    }

    public int getSemesterWorkload(String semester) {
        ArrayList<Module> semesterModules = course.get(getSemesterIndex(semester));
        int total = 0;
        for (Module module : semesterModules) {
            total += module.getModularCredits();
        }
        return total;
    }

    private int getSemesterIndex(String semester) {
        switch (semester.toLowerCase()) {
        case "y1s1":
            return 0;
        case "y1s2":
            return 1;
        case "y2s1":
            return 2;
        case "y2s2":
            return 3;
        case "y3s1":
            return 4;
        case "y3s2":
            return 5;
        case "y4s1":
            return 6;
        case "y4s2":
            return 7;
        default:
            throw new IllegalArgumentException("Invalid semester: " + semester);
        }
    }

    private String getSemesterLabel(int index) {
        switch (index) {
        case 0:
            return "y1s1";
        case 1:
            return "y1s2";
        case 2:
            return "y2s1";
        case 3:
            return "y2s2";
        case 4:
            return "y3s1";
        case 5:
            return "y3s2";
        case 6:
            return "y4s1";
        case 7:
            return "y4s2";
        default:
            throw new IllegalArgumentException("Invalid semester index: " + index);
        }
    }

    /**
     * Removes a module from the planner based on its module code.
     * The module is deleted from the semester in which it is stored
     * and its planned status is updated accordingly.
     *
     * @param moduleCode The code of the module to remove.
     * @throws NoSuchElementException If the module cannot be found in the planner.
     */
    public void removeModule(String moduleCode) {
        boolean isModulePresent = false;
        for (int i = 0; i < 8; i++) {
            ArrayList<Module> currSem = course.get(i);
            for (int j = 0; j < currSem.size(); j++) {
                Module currModule = currSem.get(j);
                if (currModule.getModuleCode().equals(moduleCode)) {
                    currSem.remove(j);
                    currModule.setIsPlanned(false);
                    isModulePresent = true;
                    break;
                }
            }
        }
        if (!isModulePresent) {
            throw new NoSuchElementException(moduleCode + " is not found in planner");
        }
    }

    /**
     * Replaces an existing module in the planner with an edited version.
     * The edited module is assigned the given semester, the original module
     * is removed using its module code, and the updated module is then added
     * back into the planner.
     *
     * @param editedModule The modified module to store.
     * @param semester The semester assigned to the modified module.
     * @param moduleCode The module code of the module being replaced.
     * @throws NoSuchElementException If the original module is not found in the planner.
     * @throws IllegalArgumentException If the given semester is not recognised.
     */
    public void editModule(Module editedModule, String semester, String moduleCode) {
        editedModule.setSemester(semester);
        removeModule(moduleCode);
        addModule(editedModule);
        // Assertion to verify the edited module is correctly placed
        assert containsModule(editedModule.getModuleCode())
                : "Edited module should exist in the planner after modification.";
    }

    public ArrayList<Module> getAllModules() {
        ArrayList<Module> allModules = new ArrayList<>();

        for (ArrayList<Module> semesterModules : course) {
            allModules.addAll(semesterModules);
        }

        return allModules;
    }
}
