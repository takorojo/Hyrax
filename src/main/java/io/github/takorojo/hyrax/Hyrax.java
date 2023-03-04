package io.github.takorojo.hyrax;

import io.github.takorojo.hyrax.commands.BackupWorldCommand;
import io.github.takorojo.hyrax.listeners.BackupListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Hyrax extends JavaPlugin {
    private final BackupListener backupListener = new BackupListener(this);

    @Override
    public void onEnable() {
        // Plugin startup logic
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(backupListener, this);

        getCommand("backup-world").setExecutor(new BackupWorldCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
