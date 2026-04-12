package seedu.pathlock.command.plannerCommand;

import org.junit.jupiter.api.Test;
import seedu.pathlock.appstate.AppState;
import seedu.pathlock.command.plannercommand.AddToPlannerCommand;
import seedu.pathlock.command.plannercommand.EditPlannerCommand;
import seedu.pathlock.module.ModuleList;
import seedu.pathlock.planner.PlannerList;
import seedu.pathlock.profile.UserProfile;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EditPlannerCommandTest {

    @Test
    public void execute_moduleHasNotBeenAdded_returnsErrorMessage() {
        ModuleList modules = new ModuleList();
        PlannerList planner = new PlannerList();
        AppState state = new AppState(
                modules,
                planner,
                new UserProfile("Test User", 3.50),
                "Test User"
        );

        EditPlannerCommand command = new EditPlannerCommand("CS1231", "y2s1");
        String result = command.execute(state);

        assertTrue(result != null && !result.isEmpty());
    }

    @Test
    public void execute_wrongSemester_returnsErrorMessage() {
        ModuleList modules = new ModuleList();
        PlannerList planner = new PlannerList();
        AppState state = new AppState(
                modules,
                planner,
                new UserProfile("Test User", 3.50),
                "Test User"
        );

        new AddToPlannerCommand("CS1231", "y1s1").execute(state);

        EditPlannerCommand command = new EditPlannerCommand("CS1231", "wrongSem");
        String result = command.execute(state);

        assertTrue(result != null && !result.isEmpty());
    }
}
