package seedu.pathlock;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.LogManager;

import seedu.pathlock.appstate.AppState;
import seedu.pathlock.command.Command;
import seedu.pathlock.exception.DuplicateException;
import seedu.pathlock.module.Module;
import seedu.pathlock.module.ModuleList;
import seedu.pathlock.parser.Parser;
import seedu.pathlock.planner.PlannerList;
import seedu.pathlock.profile.UserProfile;
import seedu.pathlock.storage.ModStorage;
import seedu.pathlock.storage.ProfileStorage;
import seedu.pathlock.ui.UI;

public class PathLock {
    /**
     * Main entry-point for the PathLock application.
     */
    public static void main(String[] args) {
        setupLogging();

        Scanner scanner = new Scanner(System.in);
        PlannerList course = new PlannerList();

        UI.opening();

        UserProfile profile = getOrCreateProfile(scanner);
        ModuleList modules = getModuleList(profile.getName());
        AppState appState = new AppState(modules, course, profile, profile.getName());

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

                    String result = command.execute(appState);
                    UI.dash();
                    System.out.println(result);
                    UI.dash();
                } catch (Exception e) {
                    UI.dash();
                    System.out.println(e.getMessage());
                    UI.dash();
                }
            }
        }
        scanner.close();
    }

    private static void setupLogging() {
        new File("data").mkdirs();
        try (InputStream config = PathLock.class.getResourceAsStream("/logging.properties")) {
            if (config != null) {
                LogManager.getLogManager().readConfiguration(config);
            }
        } catch (IOException e) {
            // fall back to default logging if config cannot be loaded
        }
    }

    private static ModuleList getModuleList(String username) {
        ModStorage storage = new ModStorage(username);
        ModuleList modules = new ModuleList();

        try {
            List<Module> savedModules = storage.load();
            for (Module saved : savedModules) {
                try {
                    String code = saved.getModuleCode();
                    int mc = saved.getModularCredits();

                    if (modules.isRecognisedModule(code)) {
                        modules.addModule(code);
                    } else {
                        modules.addExternalModule(code, mc);
                    }
                } catch (DuplicateException | IllegalArgumentException e) {
                    // skip invalid or duplicate entries
                }
            }
        } catch (IOException e) {
            // no saved data
        }

        return modules;
    }

    private static UserProfile getOrCreateProfile(Scanner scanner) {

        System.out.print("Enter your name: ");
        if (!scanner.hasNextLine()) {
            throw new NoSuchElementException("No input provided for name.");
        }

        String name = scanner.nextLine().trim();

        while (name.isEmpty()) {
            System.out.print("Name cannot be empty. Enter your name: ");
            name = scanner.nextLine().trim();
        }
        ProfileStorage profileStorage = new ProfileStorage(name);
        try {

            UserProfile savedProfile = profileStorage.load(); //
            if (savedProfile != null) {
                System.out.println("Welcome back, " + savedProfile.getName() + "!");
                System.out.println("Saved GPA: " + String.format("%.2f", savedProfile.getGpa()));
                System.out.println("Recommended maximum semester workload: "
                        + savedProfile.getRecommendedMaxWorkload() + " MCs");
                UI.dash();
                return savedProfile;
            }
        } catch (IOException e) {
            System.out.println("Could not load saved profile. Creating a new one.");
            UI.dash();
        }

        double gpa = promptForGpa(scanner);

        UserProfile profile = new UserProfile(name, gpa);

        try {
            profileStorage.save(profile);
        } catch (IOException e) {
            System.out.println("Warning: profile could not be saved.");
            UI.dash();
        }

        System.out.println("Profile saved for " + profile.getName() + ".");
        System.out.println("Recommended max semester workload: "
                + profile.getRecommendedMaxWorkload() + " MCs");
        UI.dash();

        return profile;
    }

    private static double promptForGpa(Scanner scanner) {
        while (true) {
            System.out.print("Enter your GPA (2.0 to 5.0): ");

            if (!scanner.hasNextLine()) {  // ← ADD THIS
                throw new NoSuchElementException("No input provided for GPA.");
            }

            String input = scanner.nextLine().trim();
            try {
                double gpa = Double.parseDouble(input);
                if (gpa < 2.0 || gpa > 5.0) {
                    System.out.println("GPA must be between 2.0 and 5.0.");
                    continue;
                }
                return gpa;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid GPA.");
            }
        }
    }
}
