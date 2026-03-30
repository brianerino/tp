package seedu.duke.command;

import org.junit.jupiter.api.Test;
import seedu.duke.appstate.AppState;
import seedu.duke.module.ModuleList;
import seedu.duke.planner.PlannerList;
import seedu.duke.profile.UserProfile;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DoneCommandTest {
    private final int mc = 4;

    @Test
    public void execute_validModule_success() {
        ModuleList modules = new ModuleList();
        AppState state = new AppState(modules, new PlannerList(), new UserProfile("Test User", 3.50));
        DoneCommand command = new DoneCommand("CS1231", mc);

        String result = command.execute(state);

        assertEquals("CS1231 has been added.", result);
        assertEquals(1, modules.getCompletedModules().size());
    }

    @Test
    public void execute_lowercaseInput_convertedToUppercase() {
        ModuleList modules = new ModuleList();
        AppState state = new AppState(modules, new PlannerList(), new UserProfile("Test User", 3.50));

        DoneCommand command = new DoneCommand("cs1231", mc);
        String result = command.execute(state);

        assertEquals("CS1231 has been added.", result);
    }
}
