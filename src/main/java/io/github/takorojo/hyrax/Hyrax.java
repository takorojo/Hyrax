package io.github.takorojo.hyrax;

import com.onarandombox.MultiverseCore.MultiverseCore;
import io.github.takorojo.hyrax.commands.BackupWorldCommand;
import io.github.takorojo.hyrax.commands.RestoreWorldCommand;
import io.github.takorojo.hyrax.listeners.BackupListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Hyrax extends JavaPlugin {
    private final BackupListener backupListener = new BackupListener(this);
    public static final MultiverseCore core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");

    @Override
    public void onEnable() {
        // Plugin startup logic
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(backupListener, this);

        getCommand("backup-world").setExecutor(new BackupWorldCommand());
        getCommand("restore-world").setExecutor(new RestoreWorldCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
