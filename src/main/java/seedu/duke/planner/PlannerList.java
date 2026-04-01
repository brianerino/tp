package seedu.duke.planner;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import seedu.duke.module.Module;
public class PlannerList {
    private ArrayList<ArrayList<Module>> course;

    public PlannerList() {
        course = new ArrayList<>(8);
        for (int i = 0; i < 8; i++) {
            course.add(new ArrayList<Module>());
        }
    }

    public String list () {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            switch(i) {
            case 0: output.append("y1s1:");
            break;
            case 1: output.append("y1s2:");
                break;
            case 2: output.append("y2s1:");
                break;
            case 3: output.append("y2s2:");
                break;
            case 4: output.append("y3s1:");
                break;
            case 5: output.append("y3s2:");
                break;
            case 6: output.append("y4s1:");
                break;
            case 7: output.append("y4s2:");
                break;
            default:
                break;
            }
            output.append("\n");
            ArrayList<Module> currSem = course.get(i);
            for (Module currModule : currSem) {
                output.append(currModule.getModuleCode());
                output.append("\n");
            }
        }
        return output.toString();
    }

    private boolean isModuleInPlanner (String moduleCode) {
        for (int i = 0; i < 8; i++) {
            ArrayList<Module> currSem = course.get(i);
            for (Module currModule : currSem) {
                if (currModule.getModuleCode().equals(moduleCode)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void addModule(Module module) {
        String semester = module.getSemester();
        course.get(getSemesterIndex(semester)).add(module);
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

    public void removeModule(String moduleCode) {
        boolean isModulePresent = false;
        for (int i = 0; i < 8; i++) {
            ArrayList<Module> currSem = course.get(i);
            for (int j = 0; j < currSem.size(); j++) {
                Module currModule = currSem.get(j);
                if (currModule.getModuleCode().equals(moduleCode)) {
                    currSem.remove(j);
                    isModulePresent = true;
                    break;
                }
            }
        }
        if (!isModulePresent) {
            throw new NoSuchElementException(moduleCode + " is not found in planner");
        }
    }

    public void editModule(Module editedModule, String semester, String moduleCode) {
        editedModule.setSemester(semester);
        removeModule(moduleCode);
        addModule(editedModule);
    }
}
