package org.gangsta.welcome.managers;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.gangsta.welcome.WelcomeMSGS;

import java.util.ArrayList;
import java.util.List;

public class EffectManager {

    private final WelcomeMSGS plugin;

    public EffectManager(WelcomeMSGS plugin) {
        this.plugin = plugin;
    }

    public void playJoinEffects(Player player, boolean isFirstJoin) {
        ConfigManager config = plugin.getConfigManager();

        // Play fireworks
        if (config.isFireworksEnabled()) {
            if (!config.isFireworksFirstJoinOnly() || isFirstJoin) {
                spawnFirework(player);
            }
        }

        // Play sounds
        if (config.isSoundsEnabled()) {
            playJoinSound(player, isFirstJoin);
        }
    }

    private void spawnFirework(Player player) {
        ConfigManager config = plugin.getConfigManager();
        Location location = player.getLocation().add(0, 1, 0);

        Firework firework = (Firework) player.getWorld().spawnEntity(location, EntityType.FIREWORK_ROCKET);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        // Create firework effect
        FireworkEffect.Builder effectBuilder = FireworkEffect.builder();

        // Set type
        try {
            FireworkEffect.Type type = FireworkEffect.Type.valueOf(config.getFireworkType().toUpperCase());
            effectBuilder.with(type);
        } catch (IllegalArgumentException e) {
            effectBuilder.with(FireworkEffect.Type.BALL);
        }

        // Set colors
        List<Color> colors = parseColors(config.getFireworkColors());
        if (!colors.isEmpty()) {
            effectBuilder.withColor(colors);
        }

        // Set fade colors
        List<Color> fadeColors = parseColors(config.getFireworkFadeColors());
        if (!fadeColors.isEmpty()) {
            effectBuilder.withFade(fadeColors);
        }

        // Set effects
        if (config.isFireworkFlicker()) {
            effectBuilder.flicker(true);
        }
        if (config.isFireworkTrail()) {
            effectBuilder.trail(true);
        }

        FireworkEffect effect = effectBuilder.build();
        fireworkMeta.addEffect(effect);
        fireworkMeta.setPower(config.getFireworkPower());

        firework.setFireworkMeta(fireworkMeta);
    }

    private List<Color> parseColors(List<String> colorNames) {
        List<Color> colors = new ArrayList<>();

        for (String colorName : colorNames) {
            try {
                switch (colorName.toUpperCase()) {
                    case "AQUA":
                        colors.add(Color.AQUA);
                        break;
                    case "BLACK":
                        colors.add(Color.BLACK);
                        break;
                    case "BLUE":
                        colors.add(Color.BLUE);
                        break;
                    case "FUCHSIA":
                        colors.add(Color.FUCHSIA);
                        break;
                    case "GRAY":
                        colors.add(Color.GRAY);
                        break;
                    case "GREEN":
                        colors.add(Color.GREEN);
                        break;
                    case "LIME":
                        colors.add(Color.LIME);
                        break;
                    case "MAROON":
                        colors.add(Color.MAROON);
                        break;
                    case "NAVY":
                        colors.add(Color.NAVY);
                        break;
                    case "OLIVE":
                        colors.add(Color.OLIVE);
                        break;
                    case "ORANGE":
                        colors.add(Color.ORANGE);
                        break;
                    case "PURPLE":
                        colors.add(Color.PURPLE);
                        break;
                    case "RED":
                        colors.add(Color.RED);
                        break;
                    case "SILVER":
                        colors.add(Color.SILVER);
                        break;
                    case "TEAL":
                        colors.add(Color.TEAL);
                        break;
                    case "WHITE":
                        colors.add(Color.WHITE);
                        break;
                    case "YELLOW":
                        colors.add(Color.YELLOW);
                        break;
                    default:
                        // Try parsing as hex color
                        if (colorName.startsWith("#")) {
                            java.awt.Color awtColor = java.awt.Color.decode(colorName);
                            colors.add(Color.fromRGB(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue()));
                        }
                        break;
                }
            } catch (Exception e) {
                plugin.getLogger().warning("Invalid color: " + colorName);
            }
        }

        return colors;
    }

    private void playJoinSound(Player player, boolean isFirstJoin) {
        ConfigManager config = plugin.getConfigManager();

        String soundName = isFirstJoin ? config.getFirstJoinSound() : config.getRegularJoinSound();
        float volume = config.getSoundVolume();
        float pitch = config.getSoundPitch();

        try {
            Sound sound = Sound.valueOf(soundName.toUpperCase());
            player.playSound(player.getLocation(), sound, volume, pitch);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid sound: " + soundName);
        }
    }
}