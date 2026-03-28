package seedu.duke.command;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import seedu.duke.appState.AppState;
import seedu.duke.exception.DuplicateException;
import seedu.duke.module.Module;
import seedu.duke.module.ModuleList;
import seedu.duke.planner.PlannerList;
import seedu.duke.profile.UserProfile;

public class ListIncompleteCommandTest {
    @Test
    public void execute_noCompletedModules_returnsAllIncompleteModules() {
        AppState state = new AppState(new ModuleList(), new PlannerList(), new UserProfile("Test User", 3.50));
        ListIncompleteCommand command = new ListIncompleteCommand();
        String result = command.execute(state);
        assertTrue(result.contains("Incomplete modules:"));
        assertTrue(result.contains("CS2113"));
        assertTrue(result.contains("MA1511"));
        assertTrue(result.contains("CS1010"));
    }

    @Test
    public void execute_someCompletedModules_returnsRemainingIncompleteModules() throws DuplicateException {
        ModuleList modules = new ModuleList();
        modules.addModule(new Module("CS2113",4));
        modules.addModule(new Module("CS1231",4));
        modules.addModule(new Module("CP3880",10));
        AppState state = new AppState(modules, new PlannerList(), new UserProfile("Test User", 3.50));
        ListIncompleteCommand command = new ListIncompleteCommand();
        String result = command.execute(state);
        assertTrue(result.contains("Incomplete modules:"));
        assertFalse(result.contains("CS2113"));
        assertFalse(result.contains("CS1231"));
        assertTrue(result.contains("MA1511"));
    }
}
