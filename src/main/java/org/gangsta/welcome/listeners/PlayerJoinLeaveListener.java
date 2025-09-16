package org.gangsta.welcome.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.gangsta.welcome.WelcomeMSGS;

public class PlayerJoinLeaveListener implements Listener {

    private final WelcomeMSGS plugin;

    public PlayerJoinLeaveListener(WelcomeMSGS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Suppress default join message
        event.setJoinMessage(null);

        boolean isFirstJoin = !event.getPlayer().hasPlayedBefore();

        // Delay execution slightly to ensure player is fully loaded
        new BukkitRunnable() {
            @Override
            public void run() {
                if (isFirstJoin) {
                    handleFirstJoin(event);
                } else {
                    handleRegularJoin(event);
                }
            }
        }.runTaskLater(plugin, 10L); // 0.5 second delay
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Suppress default quit message
        event.setQuitMessage(null);

        // Send custom leave message
        plugin.getMessageManager().sendLeaveMessage(event.getPlayer());
    }

    private void handleFirstJoin(PlayerJoinEvent event) {
        // Send first join message
        plugin.getMessageManager().sendFirstJoinMessage(event.getPlayer());

        // Send title
        plugin.getMessageManager().sendTitle(event.getPlayer(), true);

        // Play effects
        plugin.getEffectManager().playJoinEffects(event.getPlayer(), true);
    }

    private void handleRegularJoin(PlayerJoinEvent event) {
        // Send regular join message
        plugin.getMessageManager().sendJoinMessage(event.getPlayer());

        // Send title
        plugin.getMessageManager().sendTitle(event.getPlayer(), false);

        // Play effects
        plugin.getEffectManager().playJoinEffects(event.getPlayer(), false);
    }
}