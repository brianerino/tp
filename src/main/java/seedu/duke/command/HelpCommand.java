package seedu.duke.command;

import seedu.duke.module.ModuleList;

import java.util.LinkedHashMap;
import java.util.Map;

public class HelpCommand extends Command {

    private final String topic;
    private final String dash = "=======================================================================";

    // help --> Shows a grouped overview of all commands
    // help <command> --> Shows the example and sample output for the specified command
    public HelpCommand() {
        this.topic = null;
    }
    public HelpCommand(String topic) {
        this.topic = topic == null ? null : topic.trim().toLowerCase();
    }

    @Override
    public String execute(ModuleList modules) {
        assert modules != null : "ModuleList should not be null";
        if (topic == null || topic.isEmpty()) {
            return showGeneralHelp();
        }
        return showDetailedHelp(topic);
    }

    private String showGeneralHelp() {
        StringBuilder sb = new StringBuilder();
        sb.append(dash + "\n");
        sb.append("PATHLOCK HELP\n");
        sb.append(dash + "\n");
        sb.append("Type 'help <command>' to see details, examples, and sample output.\n");
        sb.append("Example: help done\n\n");

        // List Commands
        sb.append("LIST COMMANDS\n");
        sb.append("  list completed   - Show all completed modules\n");
        sb.append("  list incomplete  - Show required modules not yet completed\n");
        sb.append("  list needed      - Show all required modules\n\n");

        // Module Tracker Management
        sb.append("MODULE MANAGEMENT COMMANDS\n");
        sb.append("  done MODULE_CODE            - Mark a recognised module as completed\n");
        sb.append("  done MODULE_CODE /mc NUMBER - Add an external module with MCs\n");
        sb.append("  remove MODULE_CODE          - Remove a completed module\n");
        sb.append("  count                       - Show your MC progress\n\n");

        // Module Planner
        sb.append("MODULE PLANNER COMMANDS\n");
        sb.append("  y...                        - Semester planner command (coming soon)\n\n");

        // Pathlock Commands
        sb.append("PATHLOCK SYSTEM COMMANDS\n");
        sb.append("  help            - Show all commands\n");
        sb.append("  help <command>  - Show detailed help for one command\n");
        sb.append("  exit            - Close PathLock\n");
        sb.append(dash);

        return sb.toString();
    }

    private String showDetailedHelp(String inputTopic) {
        Map<String, String> helpMap = buildHelpMap();
        String normalisedTopic = normaliseTopic(inputTopic);

        if (helpMap.containsKey(normalisedTopic)) {
            return helpMap.get(normalisedTopic);
        }

        //
        return dash + "\n"
                + "No detailed help found for \"" + inputTopic + "\".\n"
                + "Type 'help' to see all available commands.\n"
                + dash;
    }

    private String normaliseTopic(String input) {
        String trimmed = input.trim().toLowerCase();

        if (trimmed.equals("done")) {
            return "done";
        }
        if (trimmed.equals("remove")) {
            return "remove";
        }
        if (trimmed.equals("count")) {
            return "count";
        }
        if (trimmed.equals("list completed")) {
            return "list completed";
        }
        if (trimmed.equals("list incomplete")) {
            return "list incomplete";
        }
        if (trimmed.equals("list needed")) {
            return "list needed";
        }
        if (trimmed.equals("help")) {
            return "help";
        }
        if (trimmed.equals("exit")) {
            return "exit";
        }

        return trimmed;
    }

    private Map<String, String> buildHelpMap() {
        Map<String, String> helpMap = new LinkedHashMap<>();

        helpMap.put("done",
                dash + "\n"
                        + "COMMAND: done\n"
                        + dash + "\n"
                        + "Purpose:\n"
                        + "  Marks a module as completed.\n\n"
                        + "Usage:\n"
                        + "  done MODULE_CODE\n"
                        + "  done MODULE_CODE /mc NUMBER\n\n"
                        + "Examples:\n"
                        + "  done CS2113\n"
                        + "  done SEP101 /mc 4\n\n"
                        + "Example output:\n"
                        + "  CS2113 has been added (4 MCs).\n"
                        + "  SEP101 has been added as an external module (4 MCs).\n"
                        + dash);

        helpMap.put("remove",
                dash + "\n"
                        + "COMMAND: remove\n"
                        + dash + "\n"
                        + "Purpose:\n"
                        + "  Removes a completed module from your module list.\n\n"
                        + "Usage:\n"
                        + "  remove MODULE_CODE\n\n"
                        + "Example:\n"
                        + "  remove CS2113\n\n"
                        + "Example output:\n"
                        + "  CS2113 has been removed\n"
                        + dash);

        helpMap.put("count",
                dash + "\n"
                        + "COMMAND: count\n"
                        + dash + "\n"
                        + "Purpose:\n"
                        + "  Shows your completed and remaining MC progress.\n\n"
                        + "Usage:\n"
                        + "  count\n\n"
                        + "Example:\n"
                        + "  count\n\n"
                        + "Example output:\n"
                        + "  Completed: 40/160 MCs\n"
                        + "  Remaining: 120/160 MCs\n"
                        + dash);

        helpMap.put("list completed",
                dash + "\n"
                        + "COMMAND: list completed\n"
                        + dash + "\n"
                        + "Purpose:\n"
                        + "  Shows all modules you have completed.\n\n"
                        + "Usage:\n"
                        + "  list completed\n\n"
                        + "Example:\n"
                        + "  list completed\n\n"
                        + "Example output:\n"
                        + "  1. CS1010\n"
                        + "  2. CS2113\n"
                        + dash);

        helpMap.put("list incomplete",
                dash + "\n"
                        + "COMMAND: list incomplete\n"
                        + dash + "\n"
                        + "Purpose:\n"
                        + "  Shows required modules that are not yet completed.\n\n"
                        + "Usage:\n"
                        + "  list incomplete\n\n"
                        + "Example:\n"
                        + "  list incomplete\n\n"
                        + "Example output:\n"
                        + "  1. CS2040C\n"
                        + "  2. EE2026\n"
                        + dash);

        helpMap.put("list needed",
                dash + "\n"
                        + "COMMAND: list needed\n"
                        + dash + "\n"
                        + "Purpose:\n"
                        + "  Shows all required core modules to graduate.\n\n"
                        + "Usage:\n"
                        + "  list needed\n\n"
                        + "Example:\n"
                        + "  list needed\n\n"
                        + "Example output:\n"
                        + "  1. CS1010\n"
                        + "  2. CS2040C\n"
                        + "  3. CS2103 OR CS2113\n"
                        + dash);

        helpMap.put("help",
                dash + "\n"
                        + "COMMAND: help\n"
                        + dash + "\n"
                        + "Purpose:\n"
                        + "  Shows all available commands, or detailed help for one command.\n\n"
                        + "Usage:\n"
                        + "  help\n"
                        + "  help done\n"
                        + "  help list completed\n\n"
                        + "Example:\n"
                        + "  help remove\n"
                        + dash);

        helpMap.put("exit",
                dash + "\n"
                        + "COMMAND: exit\n"
                        + dash + "\n"
                        + "Purpose:\n"
                        + "  Closes the PathLock application.\n\n"
                        + "Usage:\n"
                        + "  exit\n"
                        + dash);

        return helpMap;
    }
}
