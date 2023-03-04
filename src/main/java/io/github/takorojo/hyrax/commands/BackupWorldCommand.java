package io.github.takorojo.hyrax.commands;

import io.github.takorojo.hyrax.utils.HyraxPermissions;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;

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
        }

        return true;
    }

    private String getWorldName() {
        return player.getWorld().getName();
    }

    private boolean isPlayer(@NotNull CommandSender sender) {
        return sender instanceof Player;
    }
}
