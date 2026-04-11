package seedu.pathlock.command.plannerCommand;

import org.junit.jupiter.api.Test;
import seedu.pathlock.appstate.AppState;
import seedu.pathlock.command.plannercommand.AddToPlannerCommand;
import seedu.pathlock.module.ModuleList;
import seedu.pathlock.planner.PlannerList;
import seedu.pathlock.profile.UserProfile;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddToPlannerCommandTest {

    @Test
    public void execute_moduleCodeDoesNotExist_throwsException() {
        ModuleList modules = new ModuleList();
        AppState state = new AppState(
                modules,
                new PlannerList(),
                new UserProfile("Test User", 3.50),
                "Test User"
        );

        AddToPlannerCommand command = new AddToPlannerCommand("CS9999", "y1s1");

        assertThrows(IllegalArgumentException.class, () -> command.execute(state));
    }

    @Test
    public void execute_moduleCodeDoesExist_success() {
        ModuleList modules = new ModuleList();
        AppState state = new AppState(
                modules,
                new PlannerList(),
                new UserProfile("Test User", 3.50),
                "Test User"
        );

        AddToPlannerCommand command = new AddToPlannerCommand("CS1231", "y1s1");
        String result = command.execute(state);

        assertTrue(result.startsWith("Module CS1231 added to y1s1."));
    }

    @Test
    public void execute_semesterWrongFormat_returnsErrorMessage() {
        ModuleList modules = new ModuleList();
        AppState state = new AppState(
                modules,
                new PlannerList(),
                new UserProfile("Test User", 3.50),
                "Test User"
        );

        AddToPlannerCommand command = new AddToPlannerCommand("CS1231", "wrongSem");
        String result = command.execute(state);

        assertTrue(result != null && !result.isEmpty());
    }

    @Test
    public void execute_semesterRightFormat_success() {
        ModuleList modules = new ModuleList();
        AppState state = new AppState(
                modules,
                new PlannerList(),
                new UserProfile("Test User", 3.50),
                "Test User"
        );

        AddToPlannerCommand command = new AddToPlannerCommand("CS1231", "y1s1");
        String result = command.execute(state);

        assertTrue(result.startsWith("Module CS1231 added to y1s1."));
    }

    @Test
    public void execute_moduleAlreadyAdded_throwsException() {
        ModuleList modules = new ModuleList();
        PlannerList planner = new PlannerList();
        AppState state = new AppState(
                modules,
                planner,
                new UserProfile("Test User", 3.50),
                "Test User"
        );

        AddToPlannerCommand firstCommand = new AddToPlannerCommand("CS1231", "y1s1");
        firstCommand.execute(state);

        AddToPlannerCommand secondCommand = new AddToPlannerCommand("CS1231", "y1s1");

        assertThrows(IllegalArgumentException.class, () -> secondCommand.execute(state));
    }
}
