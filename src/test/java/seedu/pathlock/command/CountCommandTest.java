package seedu.pathlock.command;

import org.junit.jupiter.api.Test;
import seedu.pathlock.appstate.AppState;
import seedu.pathlock.exception.DuplicateException;
import seedu.pathlock.module.Module;
import seedu.pathlock.module.ModuleList;
import seedu.pathlock.planner.PlannerList;
import seedu.pathlock.profile.UserProfile;
import seedu.pathlock.storage.PlannerStorage;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CountCommandTest {
    private final int mc = 4;

    private AppState createAppState(ModuleList ml) {
        return new AppState(
                ml,
                new PlannerList(),
                new UserProfile("Test User", 3.50),
                new PlannerStorage("Test User", "plan1")
        );
    }

    @Test
    public void execute_emptyModuleList_showsZeroMcs() {
        ModuleList ml = new ModuleList();
        AppState state = createAppState(ml);

        CountCommand cmd = new CountCommand();
        String result = cmd.execute(state);

        assertTrue(result.contains("Completed: 0 / 160 MCs"));
        assertTrue(result.contains("0.0%"));
    }

    @Test
    public void execute_oneModule_showsCorrectMcs() throws DuplicateException {
        ModuleList ml = new ModuleList();
        ml.addModule(new Module("CS2113", 4));
        AppState state = createAppState(ml);

        CountCommand cmd = new CountCommand();
        String result = cmd.execute(state);

        assertTrue(result.contains("Completed: 4 / 160 MCs"));
    }

    @Test
    public void execute_multipleModules_showsCumulativeMcs() throws DuplicateException {
        ModuleList ml = new ModuleList();
        ml.addModule(new Module("MA1511", 2));
        ml.addModule(new Module("MA1512", 2));
        ml.addModule(new Module("CS2113", 4));
        AppState state = createAppState(ml);

        CountCommand cmd = new CountCommand();
        String result = cmd.execute(state);

        assertTrue(result.contains("Completed: 8 / 160 MCs"));
        assertTrue(result.contains("5.0%"));
    }

    @Test
    public void execute_externalModule_countsTowardsTotalMcs() {
        ModuleList ml = new ModuleList();
        AppState state = createAppState(ml);

        DoneCommand doneCommand = new DoneCommand("SEP1001", 4);
        doneCommand.execute(state);

        CountCommand cmd = new CountCommand();
        String result = cmd.execute(state);

        assertTrue(result.contains("Completed: 4 / 160 MCs"));
    }

    @Test
    public void execute_moreThan160Mcs_capsRemainingAtZero() {
        ModuleList ml = new ModuleList();
        AppState state = createAppState(ml);

        for (int i = 1; i <= 41; i++) {
            String code = String.format("EX%04d", i);
            DoneCommand doneCommand = new DoneCommand(code, mc);
            doneCommand.execute(state);
        }

        CountCommand cmd = new CountCommand();
        String result = cmd.execute(state);

        assertTrue(result.contains("Completed: 164 / 160 MCs"));
        assertTrue(result.contains("Incomplete: 0 MCs"));
        assertTrue(result.contains("(100.0%)"));
        assertTrue(result.contains("0.0%)"));
        assertFalse(result.contains("-"));
    }
}
