package seedu.duke.command;

import org.junit.jupiter.api.Test;
import seedu.duke.appState.AppState;
import seedu.duke.module.ModuleList;
import seedu.duke.planner.PlannerList;
import seedu.duke.profile.UserProfile;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HelpCommandTest {
    @Test
    public void execute_generalHelp_returnsGroupedCommandList() {
        AppState state = new AppState(new ModuleList(), new PlannerList(), new UserProfile("Test User", 3.50));
        HelpCommand command = new HelpCommand();
        String result = command.execute(state);
        assertTrue(result.contains("PATHLOCK HELP"));
        assertTrue(result.contains("LIST COMMANDS"));
        assertTrue(result.contains("MODULE MANAGEMENT COMMANDS"));
        assertTrue(result.contains("SYSTEM COMMANDS"));
        assertTrue(result.contains("list completed"));
        assertTrue(result.contains("done MODULE_CODE"));
        assertTrue(result.contains("remove MODULE_CODE"));
        assertTrue(result.contains("help"));
        assertTrue(result.contains("exit"));
    }

    @Test
    public void execute_helpDone_returnsDetailedHelpForDone() {
        AppState state = new AppState(new ModuleList(), new PlannerList(), new UserProfile("Test User", 3.50));
        HelpCommand command = new HelpCommand("done");
        String result = command.execute(state);
        assertTrue(result.contains("COMMAND: done"));
        assertTrue(result.contains("Marks a module as completed"));
        assertTrue(result.contains("done MODULE_CODE"));
        assertTrue(result.contains("done MODULE_CODE /mc NUMBER"));
        assertTrue(result.contains("done CS2113"));
    }

    @Test
    public void execute_helpListCompleted_returnsDetailedHelpForListCompleted() {
        AppState state = new AppState(new ModuleList(), new PlannerList(), new UserProfile("Test User", 3.50));
        HelpCommand command = new HelpCommand("list completed");
        String result = command.execute(state);
        assertTrue(result.contains("COMMAND: list completed"));
        assertTrue(result.contains("Shows all modules you have completed"));
        assertTrue(result.contains("list completed"));
    }

    @Test
    public void execute_helpUnknownTopic_returnsNotFoundMessage() {
        AppState state = new AppState(new ModuleList(), new PlannerList(), new UserProfile("Test User", 3.50));
        HelpCommand command = new HelpCommand("nonsense");
        String result = command.execute(state);
        assertTrue(result.contains("No detailed help found"));
        assertTrue(result.contains("nonsense"));
        assertTrue(result.contains("Type 'help' to see all available commands"));
    }

    @Test
    public void execute_helpTopicWithExtraSpaces_returnsCorrectDetailedHelp() {
        AppState state = new AppState(new ModuleList(), new PlannerList(), new UserProfile("Test User", 3.50));
        HelpCommand command = new HelpCommand("   done   ");
        String result = command.execute(state);
        assertTrue(result.contains("COMMAND: done"));
        assertTrue(result.contains("done MODULE_CODE"));
    }
}
