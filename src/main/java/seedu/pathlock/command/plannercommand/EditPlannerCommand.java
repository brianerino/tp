package seedu.pathlock.command.plannercommand;

import seedu.pathlock.appstate.AppState;
import seedu.pathlock.command.Command;
import seedu.pathlock.module.ModuleList;
import seedu.pathlock.module.Module;
import seedu.pathlock.planner.PlannerList;

public class EditPlannerCommand extends Command {
    private final String moduleCode;
    private final String semester;

    public EditPlannerCommand(String moduleCode, String semester) {
        this.moduleCode = moduleCode.toUpperCase();
        this.semester = semester.toLowerCase();
    }
    public String execute (AppState appState) {
        ModuleList moduleList = appState.getModule();
        PlannerList course = appState.getPlanner();
        Module original = moduleList.getModule(moduleCode);
        if (original == null) {
            return "\"" + moduleCode + "\" is not a recognised module.";
        }
        Module editedModule = new Module(original.getModuleCode(), original.getModularCredits());

        try {
            course.editModule(editedModule, semester, moduleCode);
            appState.getPlannerStorage().save(course);
        } catch (Exception e) {
            return e.getMessage();
        }

        return "Edited " + moduleCode + " to be in " + semester;
    }
}
