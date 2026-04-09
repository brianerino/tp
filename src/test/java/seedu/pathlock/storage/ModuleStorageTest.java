package seedu.pathlock.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import seedu.pathlock.module.Module;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ModuleStorageTest {

    private final String username = "testUser_" + UUID.randomUUID();

    @AfterEach
    public void tearDown() {
        deleteRecursively(new File("data/users/" + username));
    }

    @Test
    public void load_emptyFile_returnsEmptyList() throws IOException {
        ModStorage storage = new ModStorage(username);

        storage.save(List.of());
        List<Module> modules = storage.load();

        assertNotNull(modules);
        assertEquals(0, modules.size());
    }

    @Test
    public void save_thenLoad_returnsSameModules() throws IOException {
        ModStorage storage = new ModStorage(username);

        Module cs1010 = new Module("CS1010", 4);
        Module cs2113 = new Module("CS2113", 4);

        storage.save(List.of(cs1010, cs2113));
        List<Module> loaded = storage.load();

        assertEquals(2, loaded.size());
        assertEquals("CS1010", loaded.get(0).getModuleCode());
        assertEquals(4, loaded.get(0).getModularCredits());
        assertEquals("CS2113", loaded.get(1).getModuleCode());
        assertEquals(4, loaded.get(1).getModularCredits());
    }

    @Test
    public void save_createsFileAndParentDirectories() throws IOException {
        ModStorage storage = new ModStorage(username);

        storage.save(List.of());

        File file = new File(storage.getFilePath());
        assertTrue(file.exists());
        assertTrue(file.getParentFile().exists());
    }

    @Test
    public void load_ignoresBlankLines() throws IOException {
        ModStorage storage = new ModStorage(username);
        File file = new File(storage.getFilePath());
        file.getParentFile().mkdirs();

        FileWriter writer = new FileWriter(file);
        writer.write(System.lineSeparator());
        writer.write("CS1010|4" + System.lineSeparator());
        writer.write(System.lineSeparator());
        writer.write("CS2040C|4" + System.lineSeparator());
        writer.close();

        List<Module> loaded = storage.load();

        assertEquals(2, loaded.size());
        assertEquals("CS1010", loaded.get(0).getModuleCode());
        assertEquals("CS2040C", loaded.get(1).getModuleCode());
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
