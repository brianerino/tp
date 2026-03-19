package seedu.duke.command;

import seedu.duke.module.ModuleList;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ListIncompleteCommand extends Command {
    private final Logger logger = Logger.getLogger(ListIncompleteCommand.class.getName());

    @Override
    public String execute(ModuleList modules) {
        // Defensive check: modules should never be null when execute is called
        assert modules != null : "ModuleList should not be null";

        // Logs that this command has started running
        logger.log(Level.FINE, "Executing ListIncompleteCommand");

        String result = modules.listIncompleteModules();

        // Logs that the command finished successfully
        logger.log(Level.INFO, "Incomplete modules listed successfully");

        return result;
    }

}
