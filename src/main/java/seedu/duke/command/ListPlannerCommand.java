package seedu.duke.command;

import seedu.duke.appState.AppState;
import seedu.duke.planner.PlannerList;

public class ListPlannerCommand extends Command{
    public String execute (AppState appState) {
        PlannerList course = appState.getPlanner();
        return course.list();
    }
}
