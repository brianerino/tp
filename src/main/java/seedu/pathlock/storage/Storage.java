package seedu.pathlock.storage;

import java.io.File;
import java.io.IOException;

public abstract class Storage<T> {
    protected String filePath;

    protected Storage(String filePath) {
        assert filePath != null && !filePath.trim().isEmpty() : "File path cannot be empty";
        this.filePath = filePath;
    }

    protected File ensureFileExists() throws IOException {
        File file = new File(filePath);
        File parent = file.getParentFile();

        assert parent != null : "Parent directory should exist for file path";
        parent.mkdirs();

        if (!file.exists()) {
            file.createNewFile();
        }

        return file;
    }

    protected File ensureParentDirectoryExists() {
        File file = new File(filePath);
        File parent = file.getParentFile();

        assert parent != null : "Parent directory should exist for file path";
        parent.mkdirs();

        return file;
    }

    public String getFilePath() {
        return filePath;
    }

    public abstract T load() throws IOException;

    public abstract void save(T data) throws IOException;
}
