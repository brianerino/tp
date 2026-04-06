package seedu.duke.command.plannercommand;

import seedu.duke.appstate.AppState;
import seedu.duke.command.Command;

import java.util.ArrayList;

public class PlannerListCommand extends Command {
    @Override
    public String execute(AppState appState) {
        ArrayList<String> planners = appState.getPlannerStorage().listPlannerNames();

        if (planners.isEmpty()) {
            return "No planner variations found.";
        }

        StringBuilder sb = new StringBuilder("Planner variations:\n");
        for (int i = 0; i < planners.size(); i++) {
            sb.append(i + 1).append(". ").append(planners.get(i));
            if (planners.get(i).equals(appState.getActivePlannerName())) {
                sb.append(" (active)");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
