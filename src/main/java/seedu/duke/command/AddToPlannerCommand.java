package seedu.duke.command;

import seedu.duke.appState.AppState;
import seedu.duke.module.ModuleList;
import seedu.duke.module.Module;
import seedu.duke.planner.PlannerList;

public class AddToPlannerCommand extends Command {
    private final String moduleCode;
    private final String semester;

    public AddToPlannerCommand(String moduleCode, String semester) {
        this.moduleCode = moduleCode.toUpperCase();
        this.semester = semester;
    }

    public String execute(AppState appState) {
        ModuleList moduleList = appState.getModule();
        PlannerList course = appState.getPlanner();

        Module module = moduleList.allModules.get(moduleCode);
        module.setSemester(semester);
        course.addModule(module);

        return ("module: " + module.getModuleCode() + " added");
    }
}
