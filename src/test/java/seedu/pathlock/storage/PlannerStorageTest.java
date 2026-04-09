package seedu.pathlock.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import seedu.pathlock.module.Module;
import seedu.pathlock.planner.PlannerList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlannerStorageTest {

    private final String username = "testUser";

    @AfterEach
    public void tearDown() {
        deleteRecursively(new File("data/users/" + username));
    }

    @Test
    public void load_emptyFile_returnsEmptyPlanner() throws IOException {
        PlannerStorage storage = new PlannerStorage(username);

        PlannerList planner = new PlannerList();
        storage.save(planner);

        PlannerList loaded = storage.load();

        assertNotNull(loaded);
        assertEquals(0, loaded.getAllModules().size());
    }

    @Test
    public void save_thenLoad_returnsSamePlannerModules() throws IOException {
        PlannerStorage storage = new PlannerStorage(username);

        PlannerList planner = new PlannerList();

        Module cs2103 = new Module("CS2103", 4);
        cs2103.setSemester("y2s1");

        Module ee2026 = new Module("EE2026", 4);
        ee2026.setSemester("y2s2");

        planner.addModule(cs2103);
        planner.addModule(ee2026);

        storage.save(planner);
        PlannerList loaded = storage.load();

        assertEquals(2, loaded.getAllModules().size());

        Module first = loaded.getAllModules().get(0);
        Module second = loaded.getAllModules().get(1);

        assertEquals("CS2103", first.getModuleCode());
        assertEquals("y2s1", first.getSemester());
        assertEquals(4, first.getModularCredits());

        assertEquals("EE2026", second.getModuleCode());
        assertEquals("y2s2", second.getSemester());
        assertEquals(4, second.getModularCredits());
    }

    @Test
    public void setPlannerName_changesFilePath() {
        PlannerStorage storage = new PlannerStorage(username);

        storage.setPlannerName("plan2");

        assertTrue(storage.getFilePath().endsWith("data/users/" + username + "/plans/plan2.txt"));
        assertEquals("plan2", storage.getPlannerName());
    }

    @Test
    public void listPlannerNames_returnsExistingPlannerFiles() throws IOException {
        PlannerStorage plan1Storage = new PlannerStorage(username, "plan1");
        PlannerStorage plan2Storage = new PlannerStorage(username, "plan2");

        plan1Storage.save(new PlannerList());
        plan2Storage.save(new PlannerList());

        PlannerStorage storage = new PlannerStorage(username);
        ArrayList<String> plannerNames = storage.listPlannerNames();

        assertEquals(2, plannerNames.size());
        assertTrue(plannerNames.contains("plan1"));
        assertTrue(plannerNames.contains("plan2"));
    }

    @Test
    public void load_ignoresBlankLines() throws IOException {
        PlannerStorage storage = new PlannerStorage(username, "plan1");
        File file = new File(storage.getFilePath());
        file.getParentFile().mkdirs();

        FileWriter writer = new FileWriter(file);
        writer.write(System.lineSeparator());
        writer.write("CS1010|y1s1|4" + System.lineSeparator());
        writer.write(System.lineSeparator());
        writer.write("CS2113|y2s1|4" + System.lineSeparator());
        writer.close();

        PlannerList loaded = storage.load();

        assertEquals(2, loaded.getAllModules().size());
        assertEquals("CS1010", loaded.getAllModules().get(0).getModuleCode());
        assertEquals("y1s1", loaded.getAllModules().get(0).getSemester());
        assertEquals("CS2113", loaded.getAllModules().get(1).getModuleCode());
        assertEquals("y2s1", loaded.getAllModules().get(1).getSemester());
    }

    private void deleteRecursively(File file) {
        if (file == null || !file.exists()) {
            return;
        }

        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    deleteRecursively(child);
                }
            }
        }

        file.delete();
    }
}
