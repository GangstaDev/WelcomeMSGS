package org.gangsta.welcome.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.gangsta.welcome.WelcomeMSGS;

import java.util.List;

public class ConfigManager {

    private final WelcomeMSGS plugin;
    private FileConfiguration config;

    public ConfigManager(WelcomeMSGS plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }

    // Getter methods
    public boolean isFirstJoinEnabled() {
        return config.getBoolean("first-join.enabled");
    }

    public List<String> getFirstJoinMessages() {
        return config.getStringList("first-join.messages");
    }

    public boolean isJoinEnabled() {
        return config.getBoolean("join.enabled");
    }

    public List<String> getJoinMessages() {
        return config.getStringList("join.messages");
    }

    public boolean isLeaveEnabled() {
        return config.getBoolean("leave.enabled");
    }

    public List<String> getLeaveMessages() {
        return config.getStringList("leave.messages");
    }

    public boolean isTitlesEnabled() {
        return config.getBoolean("titles.enabled");
    }

    public String getFirstJoinTitle() {
        return config.getString("titles.first-join.title");
    }

    public String getFirstJoinSubtitle() {
        return config.getString("titles.first-join.subtitle");
    }

    public int getFirstJoinTitleFadeIn() {
        return config.getInt("titles.first-join.fade-in");
    }

    public int getFirstJoinTitleStay() {
        return config.getInt("titles.first-join.stay");
    }

    public int getFirstJoinTitleFadeOut() {
        return config.getInt("titles.first-join.fade-out");
    }

    public String getRegularJoinTitle() {
        return config.getString("titles.regular-join.title");
    }

    public String getRegularJoinSubtitle() {
        return config.getString("titles.regular-join.subtitle");
    }

    public int getRegularJoinTitleFadeIn() {
        return config.getInt("titles.regular-join.fade-in");
    }

    public int getRegularJoinTitleStay() {
        return config.getInt("titles.regular-join.stay");
    }

    public int getRegularJoinTitleFadeOut() {
        return config.getInt("titles.regular-join.fade-out");
    }

    public boolean isFireworksEnabled() {
        return config.getBoolean("fireworks.enabled");
    }

    public boolean isFireworksFirstJoinOnly() {
        return config.getBoolean("fireworks.first-join-only");
    }

    public String getFireworkType() {
        return config.getString("fireworks.type");
    }

    public List<String> getFireworkColors() {
        return config.getStringList("fireworks.colors");
    }

    public List<String> getFireworkFadeColors() {
        return config.getStringList("fireworks.fade-colors");
    }

    public boolean isFireworkFlicker() {
        return config.getBoolean("fireworks.flicker");
    }

    public boolean isFireworkTrail() {
        return config.getBoolean("fireworks.trail");
    }

    public int getFireworkPower() {
        return config.getInt("fireworks.power");
    }

    public boolean isSoundsEnabled() {
        return config.getBoolean("sounds.enabled");
    }

    public String getFirstJoinSound() {
        return config.getString("sounds.first-join");
    }

    public String getRegularJoinSound() {
        return config.getString("sounds.regular-join");
    }

    public float getSoundVolume() {
        return (float) config.getDouble("sounds.volume");
    }

    public float getSoundPitch() {
        return (float) config.getDouble("sounds.pitch");
    }

    // Setter methods for in-game configuration
    public void setValue(String path, Object value) {
        config.set(path, value);
        plugin.saveConfig();
    }

    public FileConfiguration getConfig() {
        return config;
    }
}