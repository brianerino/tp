package seedu.pathlock.parser;

import seedu.pathlock.command.Command;
import seedu.pathlock.command.DoneCommand;
import seedu.pathlock.command.plannercommand.PlannerSwitchCommand;
import seedu.pathlock.command.plannercommand.AddToPlannerCommand;
import seedu.pathlock.command.plannercommand.EditPlannerCommand;
import seedu.pathlock.command.plannercommand.RemoveFromPlannerCommand;
import seedu.pathlock.command.plannercommand.PlannerListCommand;
import seedu.pathlock.command.RemoveCommand;
import seedu.pathlock.command.ListCompletedCommand;
import seedu.pathlock.command.ListIncompleteCommand;
import seedu.pathlock.command.ListNeededCommand;
import seedu.pathlock.command.CountCommand;
import seedu.pathlock.command.PrereqCommand;
import seedu.pathlock.command.PostreqCommand;
import seedu.pathlock.exception.MissingCommandException;
import seedu.pathlock.command.HelpCommand;
import seedu.pathlock.command.SwitchUserCommand;
import seedu.pathlock.command.plannercommand.ListPlannerCommand;

public class Parser {
    //Magic numbers for checking if Command is empty or contains follow up
    private static final int DONE_PREFIX_LENGTH = 5;
    private static final int REMOVE_PREFIX_LENGTH = 8;
    private static final int SWITCH_PREFIX_LENGTH = 8;
    private static final int ADD_PREFIX_LENGTH = 13;
    private static final int EDIT_PREFIX_LENGTH = 14;
    private static final int PARTS_LENGTH = 2;

    public static Command parseCommand(String input) {

        String normalised = input.trim().replaceAll("\\s+", " ");

        if (normalised.equals("list completed")) {
            return new ListCompletedCommand();
        }

        if (normalised.equals("list incomplete")) {
            return new ListIncompleteCommand();
        }

        if (normalised.equals("list needed")) {
            return new ListNeededCommand();
        }

        if (input.equals("count")) {
            return new CountCommand();
        }

        if (input.startsWith("done")) {
            return parseDone(input);
        }

        if (input.startsWith("remove")) {
            if (input.length() < REMOVE_PREFIX_LENGTH) {
                throw new MissingCommandException("Please input module code after 'remove '");
            }
            String moduleCode = input.substring(7).trim();
            return new RemoveCommand(moduleCode);
        }

        if (input.startsWith("switch")) {
            if (input.length() < SWITCH_PREFIX_LENGTH) {
                throw new MissingCommandException("Please input username after 'switch '");
            }
            String username = input.substring(7).trim();
            return new SwitchUserCommand(username);
        }
        //trims out planner and reads subsequent command
        if (input.startsWith("planner")) {
            input = input.substring(7).trim();
            String[] parts = input.split("\\s+");
            if (input.equals("list")) {
                return new ListPlannerCommand();
            }
            if (input.startsWith("add")) {
                if (input.length() < ADD_PREFIX_LENGTH) {
                    throw new MissingCommandException("Please input module code and semester after 'add '");
                }
                String param = input.substring(4);
                int seperator = param.indexOf(" ");
                String moduleCode = param.substring(0, seperator).trim();
                String semester = param.substring(seperator).trim();
                return new AddToPlannerCommand(moduleCode,semester);
            }
            if (input.startsWith("edit")) {
                if (input.length() < EDIT_PREFIX_LENGTH) {
                    throw new MissingCommandException("Please input module code and semester after 'edit '");
                }
                String param = input.substring(5);
                int seperator = param.indexOf(" ");
                String moduleCode = param.substring(0, seperator).trim();
                String semester = param.substring(seperator).trim();
                return new EditPlannerCommand(moduleCode, semester);
            }
            if (input.startsWith("remove")) {
                if (input.length() < REMOVE_PREFIX_LENGTH) {
                    throw new MissingCommandException("Please input module code after 'remove '");
                }
                String moduleCode = input.substring(7).trim();
                return new RemoveFromPlannerCommand(moduleCode);
            }
            if (input.startsWith("list plans")) {
                return new PlannerListCommand();
            }
            if (input.startsWith("switch")) {
                if (parts.length < PARTS_LENGTH) {
                    throw new IllegalArgumentException("Planner name required.");
                }
                return new PlannerSwitchCommand(parts[1]);
            } else {
                throw new IllegalArgumentException("Unknown planner command.");
            }
        }

        String prereqPrefix = "prereq ";
        if (input.equals("prereq")) {
            throw new MissingCommandException("Please input module code after 'prereq '");
        }
        if (input.startsWith(prereqPrefix)) {
            String moduleCode = input.substring(prereqPrefix.length()).trim();
            if (moduleCode.isEmpty()) {
                throw new MissingCommandException("Please input module code after 'prereq '");
            }
            return new PrereqCommand(moduleCode);
        }

        String postreqPrefix = "postreq ";
        if (input.equals("postreq")) {
            throw new MissingCommandException("Please input module code after 'postreq '");
        }
        if (input.startsWith(postreqPrefix)) {
            String moduleCode = input.substring(postreqPrefix.length()).trim();
            if (moduleCode.isEmpty()) {
                throw new MissingCommandException("Please input module code after 'postreq '");
            }
            return new PostreqCommand(moduleCode);
        }

        if (normalised.equals("help")) {
            return new HelpCommand();
        }

        if (normalised.startsWith("help ")) {
            String topic = input.substring(5).trim();
            if (topic.isEmpty()) {
                return new HelpCommand();
            }
            return new HelpCommand(topic);
        }

        return null;
    }

    public static DoneCommand parseDone(String input) {
        if (input.length() < DONE_PREFIX_LENGTH + 1) {
            throw new MissingCommandException("Please input module code after 'done '");
        }

        String remaining = input.substring(4).trim();

        String moduleCode;
        Integer mc = null;

        if (remaining.contains("/mc")) {
            String[] parts = remaining.split("/mc", 2);

            moduleCode = parts[0].trim();

            if (moduleCode.isEmpty()) {
                throw new IllegalArgumentException(
                        "Please provide a module code before /mc. Example: done CS2113 /mc 4");
            }

            String mcPart = parts.length > 1 ? parts[1].trim() : "";

            if (mcPart.isEmpty()) {
                throw new IllegalArgumentException(
                        "Please provide a number after /mc. Example: done CS2113 /mc 4");
            }

            try {
                mc = Integer.parseInt(mcPart);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                        "MC must be a whole number, but got: \"" + mcPart + "\". Example: done CS2113 /mc 4");
            }
        } else {
            moduleCode = remaining;
        }

        return new DoneCommand(moduleCode, mc);
    }

}
