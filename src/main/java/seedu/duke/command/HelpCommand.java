package seedu.duke.command;

import seedu.duke.appstate.AppState;
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
    public String execute(AppState appState) {
        ModuleList modules = appState.getModule();
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
        sb.append("  count                       - Show your MC progress\n");
        sb.append("  prereq MODULE_CODE          - Show prerequisites for a module\n");
        sb.append("  postreq MODULE_CODE         - Show modules unlocked by a module\n\n");

        // Module Planner
        sb.append("MODULE PLANNER COMMANDS\n");
        sb.append("  planner list                          - Show all modules in your planner\n");
        sb.append("  planner add MODULE_CODE SEMESTER      - Add a module to a semester\n");
        sb.append("  planner edit MODULE_CODE SEMESTER     - Move a module to a different semester\n");
        sb.append("  planner remove MODULE_CODE            - Remove a module from your planner\n");
        sb.append("  (Valid semesters: y1s1, y1s2, y2s1, y2s2, y3s1, y3s2, y4s1, y4s2)\n\n");

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

        switch (trimmed) {
        case "done":             
            return "done";
        case "remove":           
            return "remove";
        case "count":            
            return "count";
        case "list completed":   
            return "list completed";
        case "list incomplete":  
            return "list incomplete";
        case "list needed":      
            return "list needed";
        case "prereq":           
            return "prereq";
        case "postreq":          
            return "postreq";
        case "planner list":    
            return "planner list";
        case "planner add":      
            return "planner add";
        case "planner edit":    
            return "planner edit";
        case "planner remove":  
            return "planner remove";
        case "help":           
            return "help";
        case "exit":          
            return "exit";
        default:             
            return trimmed;
        }
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

        helpMap.put("prereq",
                dash + "\n"
                        + "COMMAND: prereq\n"
                        + dash + "\n"
                        + "Purpose:\n"
                        + "  Shows the prerequisites needed before taking a module.\n\n"
                        + "Usage:\n"
                        + "  prereq MODULE_CODE\n\n"
                        + "Example:\n"
                        + "  prereq CS2113\n\n"
                        + "Example output:\n"
                        + "  Prerequisites for CS2113: CS2040C\n"
                        + dash);

        helpMap.put("postreq",
                dash + "\n"
                        + "COMMAND: postreq\n"
                        + dash + "\n"
                        + "Purpose:\n"
                        + "  Shows what modules become available after completing a module.\n\n"
                        + "Usage:\n"
                        + "  postreq MODULE_CODE\n\n"
                        + "Example:\n"
                        + "  postreq CS2113\n\n"
                        + "Example output:\n"
                        + "  Modules unlocked by CS2113: CP3880, CP3200\n"
                        + dash);

        helpMap.put("planner list",
                dash + "\n"
                        + "COMMAND: planner list\n"
                        + dash + "\n"
                        + "Purpose:\n"
                        + "  Displays all modules currently in your semester planner.\n\n"
                        + "Usage:\n"
                        + "  planner list\n\n"
                        + "Example output:\n"
                        + "  Semester: 1\n"
                        + "  CS1010\n"
                        + "  Semester: 2\n"
                        + "  CS1231\n"
                        + "  ...\n"
                        + dash);

        helpMap.put("planner add",
                dash + "\n"
                        + "COMMAND: planner add\n"
                        + dash + "\n"
                        + "Purpose:\n"
                        + "  Adds a module to a specific semester in your planner.\n\n"
                        + "Usage:\n"
                        + "  planner add MODULE_CODE SEMESTER\n\n"
                        + "Valid semesters: y1s1, y1s2, y2s1, y2s2, y3s1, y3s2, y4s1, y4s2\n\n"
                        + "Example:\n"
                        + "  planner add CS2113 y2s1\n\n"
                        + "Example output:\n"
                        + "  Module CS2113 added to y2s1.\n"
                        + "  Current workload for y2s1: 4 MCs\n"
                        + "  [INFO] Maximum workload based on GPA 4.00: 28 MCs\n"
                        + dash);

        helpMap.put("planner edit",
                dash + "\n"
                        + "COMMAND: planner edit\n"
                        + dash + "\n"
                        + "Purpose:\n"
                        + "  Moves a module already in your planner to a different semester.\n\n"
                        + "Usage:\n"
                        + "  planner edit MODULE_CODE SEMESTER\n\n"
                        + "Valid semesters: y1s1, y1s2, y2s1, y2s2, y3s1, y3s2, y4s1, y4s2\n\n"
                        + "Example:\n"
                        + "  planner edit CS2113 y3s1\n\n"
                        + "Example output:\n"
                        + "  Edited CS2113 to be in y3s1\n"
                        + dash);

        helpMap.put("planner remove",
                dash + "\n"
                        + "COMMAND: planner remove\n"
                        + dash + "\n"
                        + "Purpose:\n"
                        + "  Removes a module from your semester planner.\n\n"
                        + "Usage:\n"
                        + "  planner remove MODULE_CODE\n\n"
                        + "Example:\n"
                        + "  planner remove CS2113\n\n"
                        + "Example output:\n"
                        + "  CS2113 has been removed from planner\n"
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
