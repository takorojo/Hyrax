package io.github.takorojo.hyrax.commands;

import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import io.github.takorojo.hyrax.utils.HyraxPermissions;
import io.github.takorojo.hyrax.utils.ZipUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static io.github.takorojo.hyrax.Hyrax.core;

public class BackupWorldCommand implements CommandExecutor {
    private Player player;
    private Path working_directory = Paths.get(System.getProperty("user.dir"));
    private Path backup_directory = Paths.get(working_directory.toString(), "backups");

    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command (if defined) will be sent to the
     * player.
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
            Bukkit.getLogger().info("World name: " + getWorldName());

            backupCurrentWorld();
        }

        return true;
    }

    private void backupCurrentWorld() {
        createBackupDirectory();
        createBackupFile();
    }

    private void createBackupFile() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime now = LocalDateTime.now();
        String backup_filename = getWorldName() + "_" + dtf.format(now) + ".zip";
        Path backup_file_path = Paths.get(backup_directory.toString(), backup_filename);

        Bukkit.getLogger().info("Backing up to: " + backup_file_path);

        ZipUtils.createZipFile(backup_file_path, getWorld());
    }

    private void createBackupDirectory() {
        if (!backup_directory.toFile().exists()) {
            Bukkit.getLogger().info("Backup directory does not exist.  Creating...");

            if (backup_directory.toFile().mkdir()) {
                Bukkit.getLogger().info("Backup directory created!");
                createBackupDirectory();
            } else {
                Bukkit.getLogger().info("Backup directory not created.  This should never happen.");
            }
        }
    }

    private MultiverseWorld getWorld() {
        MVWorldManager worldManager = core.getMVWorldManager();
        return worldManager.getMVWorld(getWorldName());
    }

    private String getWorldName() {
        return player.getWorld().getName();
    }

    /**
     * Check if the given sender is a Player
     *
     * @param sender sender to check
     * @return True if a player, else false.
     */
    private boolean isPlayer(@NotNull CommandSender sender) {
        return sender instanceof Player;
    }
}
