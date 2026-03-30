package seedu.duke.parser;

import org.junit.jupiter.api.Test;
import seedu.duke.exception.MissingCommandException;
import seedu.duke.command.Command;
import seedu.duke.command.CountCommand;
import seedu.duke.command.DoneCommand;
import seedu.duke.command.ListCompletedCommand;
import seedu.duke.command.ListIncompleteCommand;
import seedu.duke.command.ListNeededCommand;
import seedu.duke.command.RemoveCommand;
import seedu.duke.command.HelpCommand;
import seedu.duke.command.PrereqCommand;
import seedu.duke.command.PostreqCommand;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParserTest {
    //@@author Kailer811
    @Test
    public void parseCommand_listCompleted_returnsListCompletedCommand() {
        Command result = Parser.parseCommand("list completed");
        assertTrue(result instanceof ListCompletedCommand);
    }

    @Test
    public void parseCommand_listIncomplete_returnsListIncompleteCommand() {
        Command result = Parser.parseCommand("list incomplete");
        assertTrue(result instanceof ListIncompleteCommand);
    }

    @Test
    public void parseCommand_listNeeded_returnsListNeededCommand() {
        Command result = Parser.parseCommand("list needed");
        assertTrue(result instanceof ListNeededCommand);
    }

    @Test
    public void parseCommand_count_returnsCountCommand() {
        Command result = Parser.parseCommand("count");
        assertTrue(result instanceof CountCommand);
    }

    @Test
    public void parseCommand_doneWithModuleCode_returnsDoneCommand() {
        Command result = Parser.parseCommand("done CS2113");
        assertTrue(result instanceof DoneCommand);
    }

    @Test
    public void parseCommand_removeWithModuleCode_returnsRemoveCommand() {
        Command result = Parser.parseCommand("remove CS2113");
        assertTrue(result instanceof RemoveCommand);
    }

    @Test
    public void parseCommand_invalidInput_returnsNull() {
        assertNull(Parser.parseCommand("blahblah"));
    }

    @Test
    public void parseCommand_emptyString_returnsNull() {
        assertNull(Parser.parseCommand(""));
    }

    @Test
    public void parseCommand_help_returnsHelpCommand() {
        Command result = Parser.parseCommand("help");
        assertTrue(result instanceof HelpCommand);
    }

    @Test
    public void parseCommand_helpWithTopic_returnsHelpCommand() {
        Command result = Parser.parseCommand("help done");
        assertTrue(result instanceof HelpCommand);
    }

    @Test
    public void parseCommand_helpWithListTopic_returnsHelpCommand() {
        Command result = Parser.parseCommand("help list completed");
        assertTrue(result instanceof HelpCommand);
    }

    @Test
    public void parseCommand_removeWithoutModuleCode_throwsMissingCommandException() {
        assertThrows(MissingCommandException.class, () -> Parser.parseCommand("remove"));
    }

    @Test
    public void parseCommand_doneWithoutModuleCode_throwsMissingCommandException() {
        assertThrows(MissingCommandException.class, () -> Parser.parseCommand("done"));
    }

    @Test
    public void parseDone_missingMcValue_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Parser.parseCommand("done CS2113 /mc"));
    }

    @Test
    public void parseDone_missingModuleCodeBeforeMc_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Parser.parseCommand("done /mc 4"));
    }

    @Test
    public void parseDone_nonNumericMc_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Parser.parseCommand("done CS2113 /mc abc"));
    }

    @Test
    public void parseCommand_prereqWithModuleCode_returnsPrereqCommand() {
        Command result = Parser.parseCommand("prereq CS2113");
        assertTrue(result instanceof PrereqCommand);
    }

    @Test
    public void parseCommand_prereqWithoutModuleCode_throwsMissingCommandException() {
        assertThrows(MissingCommandException.class, () -> Parser.parseCommand("prereq"));
    }

    @Test
    public void parseCommand_postreqWithModuleCode_returnsPostreqCommand() {
        Command result = Parser.parseCommand("postreq CS1010");
        assertTrue(result instanceof PostreqCommand);
    }

    @Test
    public void parseCommand_postreqWithoutModuleCode_throwsMissingCommandException() {
        assertThrows(MissingCommandException.class, () -> Parser.parseCommand("postreq"));
    }
}

