package seedu.duke.command.plannercommand;

import seedu.duke.appstate.AppState;
import seedu.duke.command.Command;
import seedu.duke.planner.PlannerList;

public class ListPlannerCommand extends Command {
    public String execute (AppState appState) {
        PlannerList course = appState.getPlanner();
        return course.list();
    }
}
