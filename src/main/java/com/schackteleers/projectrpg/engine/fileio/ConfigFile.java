package com.schackteleers.projectrpg.engine.fileio;

import org.ini4j.Ini;

import java.io.File;
import java.io.IOException;

/**
 * @author Stijn Schack
 * @since 18/05/2017
 */
public class ConfigFile {
    private final Ini ini;
    private File file;
    private boolean isReadOnly;

    public ConfigFile(String fileName) throws IOException {
        this(fileName, new Ini());
    }

    public ConfigFile(String fileName, Ini ini) throws IOException {
        if (!fileName.endsWith(".ini"))
            fileName += ".ini";
        this.file = new File(fileName);

        if (!file.exists() && file.createNewFile()) {
            System.out.println(fileName + " created!");
        }

        file.setExecutable(false);
        isReadOnly = file.setReadOnly();

        this.ini = ini;
        ini.setFile(file);
        ini.load();
        store();
    }

    public void put(String section, String key, Object value) {
        ini.put(section, key, value);
    }

    public String get(String section, String key) {
        return ini.get(section, key);
    }

    public void store() throws IOException {
        if (isReadOnly) {
            isReadOnly = !file.setWritable(true);
        }
        ini.store();
        isReadOnly = file.setReadOnly();
    }

    public boolean isEmpty() {
        return ini.isEmpty();
    }

}
