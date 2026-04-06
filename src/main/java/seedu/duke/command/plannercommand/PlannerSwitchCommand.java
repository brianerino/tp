package seedu.duke.command.plannercommand;

import seedu.duke.appstate.AppState;
import seedu.duke.command.Command;
import seedu.duke.planner.PlannerList;

public class PlannerSwitchCommand extends Command {
    private final String plannerName;

    public PlannerSwitchCommand(String plannerName) {
        this.plannerName = plannerName;
    }

    @Override
    public String execute(AppState appState) {
        try {
            String username = appState.getProfile().getName();
            appState.switchPlanner(username, plannerName);

            PlannerList loadedPlanner = appState.getPlannerStorage().load();
            appState.setPlanner(loadedPlanner);

            return "Switched to planner: " + plannerName;
        } catch (Exception e) {
            return "Failed to switch planner: " + e.getMessage();
        }
    }
}
