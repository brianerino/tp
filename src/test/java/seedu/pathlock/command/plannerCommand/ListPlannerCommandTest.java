package seedu.pathlock.command.plannerCommand;

import org.junit.jupiter.api.Test;
import seedu.pathlock.appstate.AppState;
import seedu.pathlock.command.plannercommand.AddToPlannerCommand;
import seedu.pathlock.command.plannercommand.ListPlannerCommand;
import seedu.pathlock.module.ModuleList;
import seedu.pathlock.planner.PlannerList;
import seedu.pathlock.profile.UserProfile;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ListPlannerCommandTest {

    @Test
    public void execute_emptyPlanner_returnsListOutput() {
        AppState state = new AppState(
                new ModuleList(),
                new PlannerList(),
                new UserProfile("Test User", 3.50),
                "Test User"
        );

        ListPlannerCommand command = new ListPlannerCommand();
        String result = command.execute(state);

        assertNotNull(result);
    }

    @Test
    public void execute_oneModuleInPlanner_success() {
        AppState state = new AppState(
                new ModuleList(),
                new PlannerList(),
                new UserProfile("Test User", 3.50),
                "Test User"
        );

        new AddToPlannerCommand("CS1231", "y1s1").execute(state);

        ListPlannerCommand command = new ListPlannerCommand();
        String result = command.execute(state);

        assertTrue(result.contains("CS1231"));
    }

    @Test
    public void execute_modulesInDifferentSemesters_success() {
        AppState state = new AppState(
                new ModuleList(),
                new PlannerList(),
                new UserProfile("Test User", 3.50),
                "Test User"
        );

        new AddToPlannerCommand("CS1231", "y1s1").execute(state);
        new AddToPlannerCommand("CS2113", "y1s2").execute(state);

        ListPlannerCommand command = new ListPlannerCommand();
        String result = command.execute(state);

        assertTrue(result.contains("CS1231"));
        assertTrue(result.contains("CS2113"));
    }
}
