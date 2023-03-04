package io.github.takorojo.hyrax.utils;

import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtils {
    /**
     * Create a zip file at the given path for the given world.
     *
     * @param zip_file_path path to the zip file to create
     * @param world         world to derive the name from
     */
    public static void createZipFile(Path zip_file_path, MultiverseWorld world) {
        final Map<String, String> env = new HashMap<>();
        Path root_directory = Paths.get(zip_file_path.getParent().getParent().toString(), world.getName());

        env.put("create", "true");

        final URI uri = URI.create("jar:file:" + zip_file_path);

        try (final FileSystem zipFileSystem = FileSystems.newFileSystem(uri, env);
             final Stream<Path> files = Files.walk(root_directory)) {
            final Path root = zipFileSystem.getPath("/");

            files.forEach(file -> {
                copyToZip(root, root_directory, file);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void extractZipFile(Path zip_file_path, Path destination_directory) {
        Bukkit.getLogger().info("Extracting " + zip_file_path + " to " + destination_directory);

        try (ZipInputStream zip = new ZipInputStream(new FileInputStream(zip_file_path.toFile()))) {
            ZipEntry zipEntry = zip.getNextEntry();

            while (zipEntry != null) {
                boolean isDirectory = false;

                if (zipEntry.getName().endsWith(File.separator)) {
                    isDirectory = true;
                }

                Path newPath = zipSlipProtect(zipEntry, destination_directory);

                if (isDirectory) {
                    Files.createDirectories(newPath);
                } else {
                    if (newPath.getParent() != null) {
                        if (Files.notExists(newPath.getParent())) {
                            Files.createDirectories(newPath.getParent());
                        }
                    }

                    Files.copy(zip, newPath, StandardCopyOption.REPLACE_EXISTING);
                }

                zipEntry = zip.getNextEntry();
            }

            zip.closeEntry();
        } catch (FileNotFoundException e) {
            Bukkit.getLogger().severe(zip_file_path + "not found.");
        } catch (IOException e) {
            Bukkit.getLogger().severe(e.getLocalizedMessage());
        }
    }

    /**
     * Protect against the "Zip Slip" vulnerability by normalizing the path to
     * the given ZipEntry.
     *
     * @param zipEntry             entry to check
     * @param destinationDirectory destination to normalize against
     *
     * @return normalized path to the destination directory.
     *
     * @throws IOException
     */
    private static Path zipSlipProtect(ZipEntry zipEntry, Path destinationDirectory) throws IOException {
        Path destinationDirResolved = destinationDirectory.resolve(zipEntry.getName());

        Path normalizePath = destinationDirResolved.normalize();

        if (!normalizePath.startsWith(destinationDirectory)) {
            throw new IOException("Bad zip entry: " + zipEntry.getName());
        }

        return normalizePath;
    }

    /**
     * Copy all files from the given directory into a zip file.
     *
     * @param root          Root of the zip file
     * @param rootDirectory Root directory to copy
     * @param file          File to copy
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
