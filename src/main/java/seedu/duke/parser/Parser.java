package seedu.duke.parser;

import seedu.duke.command.Command;
import seedu.duke.command.DoneCommand;
import seedu.duke.command.RemoveCommand;
import seedu.duke.command.ListCompletedCommand;
import seedu.duke.command.ListIncompleteCommand;
import seedu.duke.command.ListNeededCommand;
import seedu.duke.command.CountCommand;
import seedu.duke.exception.MissingCommandException;

public class Parser {

    public static Command parseCommand(String input) {

        if (input.equals("list completed")) {
            return new ListCompletedCommand();
        }

        if (input.equals("list incomplete")) {
            return new ListIncompleteCommand();
        }

        if (input.equals("list needed")) {
            return new ListNeededCommand();
        }

        if (input.equals("count")) {
            return new CountCommand();
        }

        if (input.startsWith("done")) {
            if (input.length() < 6) {
                throw new MissingCommandException("Please input module code after 'done '");
            }
            String moduleCode = input.substring(5).trim();
            return new DoneCommand(moduleCode);
        }

        if (input.startsWith("remove")) {
            if (input.length() < 8) {
                throw new MissingCommandException("Please input module code after 'remove '");
            }
            String moduleCode = input.substring(7).trim();
            return new RemoveCommand(moduleCode);
        }

        return null;
    }
}
