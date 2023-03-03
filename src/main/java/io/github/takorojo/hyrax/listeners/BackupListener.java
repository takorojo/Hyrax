package io.github.takorojo.hyrax.listeners;

import io.github.takorojo.hyrax.Hyrax;
import org.bukkit.event.Listener;

public class BackupListener implements Listener {
    private final Hyrax plugin;

    public BackupListener(Hyrax instance) {
        plugin = instance;
    }
}
