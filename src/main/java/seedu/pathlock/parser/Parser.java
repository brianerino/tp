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

        String trimmed = input.trim().replaceAll("\\s+", " ");
        String normalised = trimmed.toLowerCase();

        if (normalised.equals("list completed")) {
            return new ListCompletedCommand();
        }

        if (normalised.equals("list incomplete")) {
            return new ListIncompleteCommand();
        }

        if (normalised.equals("list needed")) {
            return new ListNeededCommand();
        }

        if (normalised.equals("count")) {
            return new CountCommand();
        }

        if (normalised.startsWith("done")) {
            return parseDone(trimmed);
        }

        if (normalised.startsWith("remove")) {
            if (trimmed.length() < REMOVE_PREFIX_LENGTH) {
                throw new MissingCommandException("Please input module code after 'remove '");
            }
            String moduleCode = trimmed.substring(7).trim();
            return new RemoveCommand(moduleCode);
        }

        if (normalised.startsWith("switch")) {
            if (trimmed.length() < SWITCH_PREFIX_LENGTH) {
                throw new MissingCommandException("Please input username after 'switch '");
            }
            String username = trimmed.substring(7).trim();
            return new SwitchUserCommand(username);
        }
        //trims out planner and reads subsequent command
        if (normalised.startsWith("planner")) {
            String subInput = trimmed.substring(7).trim();
            String subNormalised = subInput.toLowerCase();
            String[] parts = subInput.split("\\s+");
            if (subNormalised.equals("list")) {
                return new ListPlannerCommand();
            }
            if (subNormalised.startsWith("add")) {
                if (subInput.length() < ADD_PREFIX_LENGTH - 7) {
                    throw new MissingCommandException("Please input module code and semester after 'add '");
                }
                String param = subInput.substring(4);
                int seperator = param.indexOf(" ");
                String moduleCode = param.substring(0, seperator).trim();
                String semester = param.substring(seperator).trim();
                return new AddToPlannerCommand(moduleCode, semester);
            }
            if (subNormalised.startsWith("edit")) {
                if (subInput.length() < EDIT_PREFIX_LENGTH - 7) {
                    throw new MissingCommandException("Please input module code and semester after 'edit '");
                }
                String param = subInput.substring(5);
                int seperator = param.indexOf(" ");
                String moduleCode = param.substring(0, seperator).trim();
                String semester = param.substring(seperator).trim();
                return new EditPlannerCommand(moduleCode, semester);
            }
            if (subNormalised.startsWith("remove")) {
                if (subInput.length() < REMOVE_PREFIX_LENGTH) {
                    throw new MissingCommandException("Please input module code after 'remove '");
                }
                String moduleCode = subInput.substring(7).trim();
                return new RemoveFromPlannerCommand(moduleCode);
            }
            if (subNormalised.startsWith("list plans")) {
                return new PlannerListCommand();
            }
            if (subNormalised.startsWith("switch")) {
                if (parts.length < PARTS_LENGTH) {
                    throw new IllegalArgumentException("Planner name required.");
                }
                if (containsPipe(parts[1])) {
                    throw new IllegalArgumentException("Name cannot contain the '|' character.");
                }
                return new PlannerSwitchCommand(parts[1]);
            } else {
                throw new IllegalArgumentException("Unknown planner command.");
            }
        }

        String prereqPrefix = "prereq ";
        if (normalised.equals("prereq")) {
            throw new MissingCommandException("Please input module code after 'prereq '");
        }
        if (normalised.startsWith(prereqPrefix)) {
            String moduleCode = trimmed.substring(prereqPrefix.length()).trim();
            if (moduleCode.isEmpty()) {
                throw new MissingCommandException("Please input module code after 'prereq '");
            }
            return new PrereqCommand(moduleCode);
        }

        String postreqPrefix = "postreq ";
        if (normalised.equals("postreq")) {
            throw new MissingCommandException("Please input module code after 'postreq '");
        }
        if (normalised.startsWith(postreqPrefix)) {
            String moduleCode = trimmed.substring(postreqPrefix.length()).trim();
            if (moduleCode.isEmpty()) {
                throw new MissingCommandException("Please input module code after 'postreq '");
            }
            return new PostreqCommand(moduleCode);
        }

        if (normalised.equals("help")) {
            return new HelpCommand();
        }

        if (normalised.startsWith("help ")) {
            String topic = trimmed.substring(5).trim();
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
    public static boolean containsPipe(String input) {
        return input != null && input.contains("|");
    }

}
