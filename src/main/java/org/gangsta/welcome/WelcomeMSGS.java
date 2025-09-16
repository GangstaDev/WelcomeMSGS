package org.gangsta.welcome;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.gangsta.welcome.commands.WelcomeCommand;
import org.gangsta.welcome.listeners.PlayerJoinLeaveListener;
import org.gangsta.welcome.managers.ConfigManager;
import org.gangsta.welcome.managers.MessageManager;
import org.gangsta.welcome.managers.EffectManager;

public class WelcomeMSGS extends JavaPlugin implements Listener {

    private static WelcomeMSGS instance;
    private ConfigManager configManager;
    private MessageManager messageManager;
    private EffectManager effectManager;

    @Override
    public void onEnable() {
        instance = this;

        this.configManager = new ConfigManager(this);
        this.messageManager = new MessageManager(this);
        this.effectManager = new EffectManager(this);

        configManager.loadConfig();

        getServer().getPluginManager().registerEvents(new PlayerJoinLeaveListener(this), this);

        WelcomeCommand welcomeCommand = new WelcomeCommand(this);
        getCommand("welcome").setExecutor(welcomeCommand);
        getCommand("welcome").setTabCompleter(welcomeCommand);

        getLogger().info("WelcomeMSGS v" + getDescription().getVersion() + " has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("WelcomeMSGS has been disabled!");
    }

    public static WelcomeMSGS getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public EffectManager getEffectManager() {
        return effectManager;
    }

    public void reloadPlugin() {
        configManager.loadConfig();
        getLogger().info("WelcomeMSGS configuration reloaded!");
    }
}