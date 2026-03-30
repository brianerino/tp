package seedu.duke.storage;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;

import seedu.duke.module.Module;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StorageTest {

    @Test
    public void load_emptyFile_returnsEmptyList() throws IOException {
        Storage storage = new Storage("data/test.txt");

        // ensure empty file
        storage.save(List.of());

        List<Module> modules = storage.load();

        assertNotNull(modules);
        assertEquals(0, modules.size());
    }
}
