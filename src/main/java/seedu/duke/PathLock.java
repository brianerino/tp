package seedu.duke;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import seedu.duke.command.Command;
import seedu.duke.module.ModuleList;
import seedu.duke.parser.Parser;
import seedu.duke.ui.UI;
import seedu.duke.storage.Storage;
public class PathLock {
    /**
     * Main entry-point for the PathLock application.
     */
    @SuppressWarnings("checkstyle:RightCurly")
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Storage storage = new Storage();
        ModuleList modules;

        try {
            modules = new ModuleList(storage.load());
        } catch (IOException e) {
            modules = new ModuleList(new ArrayList<>());
        }

        UI.opening();
        while (true) {
            UI.userPrompt();

            if (!scanner.hasNextLine()){
                break;
            }

            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                UI.closing();
                break;
            } else if (input.equalsIgnoreCase("help")) {
                UI.help();
            } else {
                Command command = Parser.parseCommand(input);

                if (command == null) {
                    UI.unknownCommand();
                    continue;
                }

                try {
                    String result = command.execute(modules);
                    System.out.println(result);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        scanner.close();
    }
}
