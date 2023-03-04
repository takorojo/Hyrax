package io.github.takorojo.hyrax.utils;

import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static io.github.takorojo.hyrax.Hyrax.core;

public class HyraxPlayerUtils {
    /**
     * Check if the given entity is a Player.
     *
     * @param entity entity to check
     *
     * @return True if entity is a Player, else false
     */
    public static boolean isPlayer(@NotNull Entity entity) {
        return entity instanceof Player;
    }

    /**
     * Check if the given sender is a Player.
     *
     * @param sender sender to check
     *
     * @return True if sender is a Player, else false
     *
     * @see HyraxPlayerUtils#isPlayer(Entity)
     */
    public static boolean isPlayer(@NotNull CommandSender sender) {
        return sender instanceof Player;
    }

    public static void kickAllPlayers(MultiverseWorld world) {
        var players = core.getServer().getOnlinePlayers();

        for (Player p : players) {
            if (playerInWorld(p, world)) {
                p.kickPlayer("World is being restored.  Please wait patiently while the server is restarted.");
            }
        }
    }

    public static boolean playerInWorld(Player player, MultiverseWorld world) {
        return player.isOnline() && player.getWorld().getName().equals(world.getName());
    }
}
