package org.gangsta.welcome.managers;

import org.bukkit.entity.Player;
import org.gangsta.welcome.WelcomeMSGS;
import org.gangsta.welcome.util.ColorUtil;

import java.util.List;
import java.util.Random;

public class MessageManager {

    private final WelcomeMSGS plugin;
    private final Random random;

    public MessageManager(WelcomeMSGS plugin) {
        this.plugin = plugin;
        this.random = new Random();
    }

    public String getRandomMessage(List<String> messages, Player player) {
        if (messages == null || messages.isEmpty()) {
            return "";
        }

        String message = messages.get(random.nextInt(messages.size()));
        message = message.replace("%player%", player.getName());
        message = message.replace("%displayname%", player.getDisplayName());

        return ColorUtil.translateColors(message);
    }

    public void sendFirstJoinMessage(Player player) {
        ConfigManager config = plugin.getConfigManager();

        if (!config.isFirstJoinEnabled()) {
            return;
        }

        List<String> messages = config.getFirstJoinMessages();
        String message = getRandomMessage(messages, player);

        if (!message.isEmpty()) {
            plugin.getServer().broadcastMessage(message);
        }
    }

    public void sendJoinMessage(Player player) {
        ConfigManager config = plugin.getConfigManager();

        if (!config.isJoinEnabled()) {
            return;
        }

        List<String> messages = config.getJoinMessages();
        String message = getRandomMessage(messages, player);

        if (!message.isEmpty()) {
            plugin.getServer().broadcastMessage(message);
        }
    }

    public void sendLeaveMessage(Player player) {
        ConfigManager config = plugin.getConfigManager();

        if (!config.isLeaveEnabled()) {
            return;
        }

        List<String> messages = config.getLeaveMessages();
        String message = getRandomMessage(messages, player);

        if (!message.isEmpty()) {
            plugin.getServer().broadcastMessage(message);
        }
    }

    public void sendTitle(Player player, boolean isFirstJoin) {
        ConfigManager config = plugin.getConfigManager();

        if (!config.isTitlesEnabled()) {
            return;
        }

        String title, subtitle;
        int fadeIn, stay, fadeOut;

        if (isFirstJoin) {
            title = config.getFirstJoinTitle();
            subtitle = config.getFirstJoinSubtitle();
            fadeIn = config.getFirstJoinTitleFadeIn();
            stay = config.getFirstJoinTitleStay();
            fadeOut = config.getFirstJoinTitleFadeOut();
        } else {
            title = config.getRegularJoinTitle();
            subtitle = config.getRegularJoinSubtitle();
            fadeIn = config.getRegularJoinTitleFadeIn();
            stay = config.getRegularJoinTitleStay();
            fadeOut = config.getRegularJoinTitleFadeOut();
        }

        // Replace placeholders and translate colors
        title = title.replace("%player%", player.getName()).replace("%displayname%", player.getDisplayName());
        subtitle = subtitle.replace("%player%", player.getName()).replace("%displayname%", player.getDisplayName());

        title = ColorUtil.translateColors(title);
        subtitle = ColorUtil.translateColors(subtitle);

        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }
}