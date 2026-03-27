package seedu.duke;

import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import seedu.duke.command.Command;
import seedu.duke.module.Module;
import seedu.duke.module.ModuleList;
import seedu.duke.parser.Parser;
import seedu.duke.ui.UI;
import seedu.duke.storage.Storage;
import seedu.duke.exception.DuplicateException;


public class PathLock {
    /**
     * Main entry-point for the PathLock application.
     */
    @SuppressWarnings("checkstyle:RightCurly")
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ModuleList modules = getModuleList();

        UI.opening();
        while (true) {
            UI.userPrompt();

            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                UI.closing();
                break;
            } else {
                try {
                    Command command = Parser.parseCommand(input);

                    if (command == null) {
                        UI.unknownCommand();
                        continue;
                    }

                    String result = command.execute(modules);
                    System.out.println(result);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    UI.dash();
                }
            }
        }
        scanner.close();
    }

    private static ModuleList getModuleList() {
        Storage storage = new Storage();
        ModuleList modules = new ModuleList();

        try {
            List<Module> savedModules = storage.load();
            for (Module saved : savedModules) {
                try {
                    String code = saved.getModuleCode();
                    int mc = saved.getModularCredits();
                    if (modules.isRecognisedModule(code)){
                        modules.addModule(code);
                    } else {
                        modules.addExternalModule(code, mc);
                    }
                } catch (DuplicateException | IllegalArgumentException e) {
                    // skip invalid or duplicate entries from save file
                }
            }
        } catch (IOException e) {
            // no saved data, start fresh
        }
        return modules;
    }
}
