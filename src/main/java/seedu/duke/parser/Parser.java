package seedu.duke.parser;

import seedu.duke.command.Command;
import seedu.duke.command.DoneCommand;
import seedu.duke.command.EditPlannerCommand;
import seedu.duke.command.ListPlannerCommand;
import seedu.duke.command.RemoveCommand;
import seedu.duke.command.ListCompletedCommand;
import seedu.duke.command.ListIncompleteCommand;
import seedu.duke.command.ListNeededCommand;
import seedu.duke.command.CountCommand;
import seedu.duke.command.AddToPlannerCommand;
import seedu.duke.command.RemoveFromPlannerCommand;
import seedu.duke.exception.MissingCommandException;
import seedu.duke.command.HelpCommand;

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
            return parseDone(input);
        }

        if (input.startsWith("remove")) {
            if (input.length() < 8) {
                throw new MissingCommandException("Please input module code after 'remove '");
            }
            String moduleCode = input.substring(7).trim();
            return new RemoveCommand(moduleCode);
        }

        if (input.startsWith("planner")) {
            input = input.substring(8).trim();
            if (input.equals("list")) {
                return new ListPlannerCommand();
            }
            if (input.startsWith("add")) {
                if (input.length() < 13) {
                    throw new MissingCommandException("Please input module code and semester after 'add '");
                }
                //calculated from the back as y1s1 will be standard
                int seperator = input.length()-5;
                String semester = input.substring(seperator).trim();
                String moduleCode = input.substring(4, seperator).trim();
                return new AddToPlannerCommand(moduleCode,semester);
            }
            if (input.startsWith("edit")) {
                String param = input.substring(5);
                int seperator = param.indexOf(" ");
                String moduleCode = param.substring(0, seperator).trim();
                String semester = param.substring(seperator).trim();
                System.out.println(moduleCode + " " + semester);
                return new EditPlannerCommand(moduleCode, semester);
            }

            if (input.startsWith("remove")) {
                if (input.length() < 8) {
                    throw new MissingCommandException("Please input module code after 'remove '");
                }
                String moduleCode = input.substring(7).trim();
                return new RemoveFromPlannerCommand(moduleCode);
            }
        }

        if (input.equals("help")) {
            return new HelpCommand();
        }

        if (input.startsWith("help ")) {
            String topic = input.substring(5).trim();
            if (topic.isEmpty()) {
                return new HelpCommand();
            }
            return new HelpCommand(topic);
        }

        return null;
    }

    public static DoneCommand parseDone(String input) {
        if (input.length() < 6) {
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
