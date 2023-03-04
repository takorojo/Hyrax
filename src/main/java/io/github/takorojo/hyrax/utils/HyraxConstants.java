package io.github.takorojo.hyrax.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class HyraxConstants {
    public static final Path WORKING_DIRECTORY = Paths.get(System.getProperty("user.dir"));
    public static final Path BACKUP_DIRECTORY = Paths.get(WORKING_DIRECTORY.toString(), "backups");
}
