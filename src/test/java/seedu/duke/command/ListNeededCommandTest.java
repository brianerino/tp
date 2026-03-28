package seedu.duke.command;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import seedu.duke.appState.AppState;
import seedu.duke.module.ModuleList;
import seedu.duke.planner.PlannerList;
import seedu.duke.profile.UserProfile;

public class ListNeededCommandTest {
    @Test
    public void execute_returnsAllRequiredModules() {
        AppState state = new AppState(new ModuleList(), new PlannerList(), new UserProfile("Test User", 3.50));
        ListNeededCommand command = new ListNeededCommand();
        String result = command.execute(state);
        assertTrue(result.contains("Modules required for graduation:"));
        assertTrue(result.contains("CS2113"));
        assertTrue(result.contains("MA1511"));
        assertTrue(result.contains("CG4002"));
        assertTrue(result.contains("CS1010"));
    }
}
