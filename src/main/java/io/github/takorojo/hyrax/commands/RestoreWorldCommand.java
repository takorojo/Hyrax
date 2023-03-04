package io.github.takorojo.hyrax.commands;

import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import io.github.takorojo.hyrax.utils.HyraxConstants;
import io.github.takorojo.hyrax.utils.HyraxDate;
import io.github.takorojo.hyrax.utils.HyraxPermissions;
import io.github.takorojo.hyrax.utils.ZipUtils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.github.takorojo.hyrax.Hyrax.core;
import static io.github.takorojo.hyrax.utils.HyraxPlayerUtils.isPlayer;
import static io.github.takorojo.hyrax.utils.HyraxPlayerUtils.kickAllPlayers;
import static io.github.takorojo.hyrax.utils.HyraxWorldUtils.getWorld;
import static io.github.takorojo.hyrax.utils.HyraxWorldUtils.getWorldName;

public class RestoreWorldCommand implements CommandExecutor {
    private Player player;

    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     *
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!isPlayer(sender)) {
            Bukkit.getLogger().info("Command used by non-player " + sender.getName());
            return false;
        }

        player = (Player) sender;

        if (player.hasPermission(HyraxPermissions.HYRAX_BACKUP_WORLD)) {
            restoreCurrentWorld();
        }

        return true;
    }

    private void restoreCurrentWorld() {
        Path copy_path = getLatestBackupFilePath();
        Path paste_path = Paths.get(HyraxConstants.WORKING_DIRECTORY.toString(), getWorldName(player));

        Bukkit.getLogger().info("Copy path: " + copy_path);
        Bukkit.getLogger().info("Paste path: " + paste_path);

        kickAllPlayers(getWorld(player));
        ZipUtils.extractZipFile(copy_path, paste_path);
        core.getServer().shutdown();
    }

    private Path getLatestBackupFilePath() {
        Set<File> files = getBackedUpFiles(getWorld(player));
        HyraxDate latest_date = new HyraxDate("0");

        // Get the latest file
        for (File file : files) {
            String filename = file.getName();
            Pattern pattern = Pattern.compile("^(?<worldname>(.+))_(?<date>\\d+).zip");
            Matcher matcher = pattern.matcher(filename);

            if (matcher.matches()) {
                String world_name = matcher.group("worldname");
                HyraxDate date = new HyraxDate(matcher.group("date"));

                if (Objects.equals(world_name, getWorldName(player))) {
                    Bukkit.getLogger().info("File: " + file);

                    if (date.isLaterThan(latest_date)) {
                        Bukkit.getLogger().info("Date " + date + " is later than " + latest_date + "!");
                        latest_date = date;
                    }
                }

            }

        }

        String filename = getWorldName(player) + "_" + latest_date + ".zip";

        return Paths.get(HyraxConstants.BACKUP_DIRECTORY.toString(), filename);
    }

    private Set<File> getBackedUpFiles(MultiverseWorld world) {
        Set<File> files = new HashSet<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(HyraxConstants.BACKUP_DIRECTORY.toString()))) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    Bukkit.getLogger().info("Getting backed up file " + path);
                    files.add(path.toFile());
                }
            }
        } catch (IOException e) {
            Bukkit.getLogger().severe(e.getLocalizedMessage());
        }

        return files;
    }
}
