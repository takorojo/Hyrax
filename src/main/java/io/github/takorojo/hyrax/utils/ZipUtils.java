package io.github.takorojo.hyrax.utils;

import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class ZipUtils {
    /**
     * Create a zip file at the given path for the given world.
     *
     * @param zip_file_path path to the zip file to create
     * @param world world to derive the name from
     */
    public static void createZipFile(Path zip_file_path, MultiverseWorld world) {
        final Map<String, String> env = new HashMap<>();
        Path root_directory = Paths.get(zip_file_path.getParent().getParent().toString(), world.getName());

        env.put("create", "true");

        final URI uri = URI.create("jar:file:" + zip_file_path);

        try(final FileSystem zipFileSystem = FileSystems.newFileSystem(uri, env);
            final Stream<Path> files = Files.walk(root_directory)) {
            final Path root = zipFileSystem.getPath("/");

            files.forEach(file -> {
                copyToZip(root, root_directory, file);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Copy all files from the given directory into a zip file.
     *
     * @param root Root of the zip file
     * @param rootDirectory Root directory to copy
     * @param file File to copy
     */
    private static void copyToZip(Path root, Path rootDirectory, Path file) {
        final Path to = root.resolve(rootDirectory.relativize(file).toString());

        try {
            if (Files.isDirectory(file)) {
                Bukkit.getLogger().info("Creating directory " + file + " in " + to);
                Files.createDirectories(to);
            } else {
                Bukkit.getLogger().info("Copying " + file + " into " + to);
                Files.copy(file, to);
                Bukkit.getLogger().info("Successfully copied " + file + " into " + to);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
