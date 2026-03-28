package seedu.duke.command;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import seedu.duke.appState.AppState;
import seedu.duke.exception.DuplicateException;
import seedu.duke.module.Module;
import seedu.duke.module.ModuleList;
import seedu.duke.planner.PlannerList;
import seedu.duke.profile.UserProfile;

public class ListCompletedCommandTest {
    @Test
    public void execute_noCompletedModules_returnsEmptyMessage() {
        AppState state = new AppState(new ModuleList(), new PlannerList(), new UserProfile("Test User", 3.50));
        ListCompletedCommand command = new ListCompletedCommand();
        String result = command.execute(state);
        assertEquals("No modules completed yet.", result);
    }

    @Test
    public void execute_withCompletedModules_returnsCompletedList() throws DuplicateException {
        ModuleList modules = new ModuleList();
        modules.addModule(new Module("CS2113",4));
        modules.addModule(new Module("CS1231",4));
        AppState state = new AppState(modules, new PlannerList(), new UserProfile("Test User", 3.50));
        ListCompletedCommand command = new ListCompletedCommand();
        String result = command.execute(state);
        assertTrue(result.contains("CS2113"));
        assertTrue(result.contains("CS1231"));
    }
}
