package io.github.takorojo.hyrax.utils;

import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

import static io.github.takorojo.hyrax.Hyrax.core;

public class HyraxWorldUtils {
    /**
     * Get the name of the world the given player is currently in.
     *
     * @param player player to check world name for
     *
     * @return Name of the world
     */
    public static String getWorldName(Player player) {
        return player.getWorld().getName();
    }

    /**
     * Get the world the given player is currently in.
     *
     * @param player player to get world for
     *
     * @return world the player is currently in
     */
    public static MultiverseWorld getWorld(Player player) {
        MVWorldManager worldManager = core.getMVWorldManager();
        return worldManager.getMVWorld(getWorldName(player));
    }
}
