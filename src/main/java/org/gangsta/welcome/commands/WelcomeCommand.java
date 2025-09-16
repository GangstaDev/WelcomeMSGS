package org.gangsta.welcome.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.gangsta.welcome.WelcomeMSGS;
import org.gangsta.welcome.util.ColorUtil;

import java.util.*;
import java.util.stream.Collectors;

public class WelcomeCommand implements CommandExecutor, TabCompleter {

    private final WelcomeMSGS plugin;

    // All configurable messages
    private final Map<String, String> messages = new HashMap<String, String>() {{
        put("no-permission", "&#FF6B6B&You don't have permission to use this command!");
        put("config-reloaded", "&#4ECDC4&Configuration reloaded successfully!");
        put("feature-enabled", "&#4ECDC4&%feature% has been enabled!");
        put("feature-disabled", "&#FF6B6B&%feature% has been disabled!");
        put("invalid-feature", "&#FF6B6B&Invalid feature! Available: firstjoin, join, leave, titles, fireworks, sounds");
        put("config-set", "&#4ECDC4&Set %path% to: %value%");
        put("message-added", "&#4ECDC4&Message added to %type%!");
        put("message-removed", "&#FF6B6B&Message %index% removed from %type%!");
        put("invalid-index", "&#FF6B6B&Invalid message index!");
        put("invalid-type", "&#FF6B6B&Invalid message type! Available: firstjoin, join, leave");
        put("no-messages", "&#FFEAA7&No messages found for %type%");
        put("test-executed", "&#4ECDC4&Test executed for %type%!");
        put("player-only", "&#FF6B6B&This command can only be used by players!");
        put("usage-toggle", "&#FF6B6B&Usage: /welcome toggle <feature>");
        put("usage-set", "&#FF6B6B&Usage: /welcome set <path> <value>");
        put("usage-add", "&#FF6B6B&Usage: /welcome add <type> <message>");
        put("usage-remove", "&#FF6B6B&Usage: /welcome remove <type> <index>");
        put("usage-list", "&#FF6B6B&Usage: /welcome list <type>");
        put("usage-test", "&#FF6B6B&Usage: /welcome test <type>");
        put("usage-setmsg", "&#FF6B6B&Usage: /welcome setmsg <type> <message>");
        put("help-header", "&#FF6B6B&=== WelcomeMSGS Commands ===");
        put("help-reload", "&#4ECDC4&/welcome reload &#FFFFFF&- Reload the configuration");
        put("help-toggle", "&#4ECDC4&/welcome toggle <feature> &#FFFFFF&- Toggle features on/off");
        put("help-set", "&#4ECDC4&/welcome set <path> <value> &#FFFFFF&- Set configuration values");
        put("help-add", "&#4ECDC4&/welcome add <type> <message> &#FFFFFF&- Add messages");
        put("help-remove", "&#4ECDC4&/welcome remove <type> <index> &#FFFFFF&- Remove messages");
        put("help-list", "&#4ECDC4&/welcome list <type> &#FFFFFF&- List messages");
        put("help-test", "&#4ECDC4&/welcome test <type> &#FFFFFF&- Test messages/effects");
        put("help-setmsg", "&#4ECDC4&/welcome setmsg <type> <message> &#FFFFFF&- Set command messages");
        put("help-features", "&#FFEAA7&Features: firstjoin, join, leave, titles, fireworks, sounds");
        put("help-types", "&#FFEAA7&Types: firstjoin, join, leave");
        put("help-msgtypes", "&#FFEAA7&Message types: no-permission, config-reloaded, feature-enabled, etc.");
        put("list-header", "&#4ECDC4&=== %type% Messages ===");
        put("list-item", "&#FFEAA7&%index%. %message%");
    }};

    public WelcomeCommand(WelcomeMSGS plugin) {
        this.plugin = plugin;
        loadMessagesFromConfig();
    }

    private void loadMessagesFromConfig() {
        // Load custom messages from config if they exist
        if (plugin.getConfig().contains("command-messages")) {
            for (String key : plugin.getConfig().getConfigurationSection("command-messages").getKeys(false)) {
                String value = plugin.getConfig().getString("command-messages." + key);
                if (value != null) {
                    messages.put(key, value);
                }
            }
        } else {
            // Save default messages to config
            for (Map.Entry<String, String> entry : messages.entrySet()) {
                plugin.getConfig().set("command-messages." + entry.getKey(), entry.getValue());
            }
            plugin.saveConfig();
        }
    }

    private String getMessage(String key, String... replacements) {
        String message = messages.getOrDefault(key, "&#FF6B6B&Message not found: " + key);

        // Replace placeholders
        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 < replacements.length) {
                message = message.replace("%" + replacements[i] + "%", replacements[i + 1]);
            }
        }

        return ColorUtil.translateColors(message);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("welcomemsgs.admin")) {
            sender.sendMessage(getMessage("no-permission"));
            return true;
        }

        if (args.length == 0) {
            showHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                handleReload(sender);
                break;
            case "toggle":
                handleToggle(sender, args);
                break;
            case "set":
                handleSet(sender, args);
                break;
            case "add":
                handleAdd(sender, args);
                break;
            case "remove":
                handleRemove(sender, args);
                break;
            case "list":
                handleList(sender, args);
                break;
            case "test":
                handleTest(sender, args);
                break;
            case "setmsg":
                handleSetMessage(sender, args);
                break;
            default:
                showHelp(sender);
                break;
        }
        return true;
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage(getMessage("help-header"));
        sender.sendMessage(getMessage("help-reload"));
        sender.sendMessage(getMessage("help-toggle"));
        sender.sendMessage(getMessage("help-set"));
        sender.sendMessage(getMessage("help-add"));
        sender.sendMessage(getMessage("help-remove"));
        sender.sendMessage(getMessage("help-list"));
        sender.sendMessage(getMessage("help-test"));
        sender.sendMessage(getMessage("help-setmsg"));
        sender.sendMessage("");
        sender.sendMessage(getMessage("help-features"));
        sender.sendMessage(getMessage("help-types"));
    }

    private void handleReload(CommandSender sender) {
        plugin.reloadPlugin();
        loadMessagesFromConfig();
        sender.sendMessage(getMessage("config-reloaded"));
    }

    private void handleToggle(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(getMessage("usage-toggle"));
            sender.sendMessage(getMessage("help-features"));
            return;
        }

        String feature = args[1].toLowerCase();
        boolean newValue;

        switch (feature) {
            case "firstjoin":
                newValue = !plugin.getConfigManager().isFirstJoinEnabled();
                plugin.getConfigManager().setValue("first-join.enabled", newValue);
                break;
            case "join":
                newValue = !plugin.getConfigManager().isJoinEnabled();
                plugin.getConfigManager().setValue("join.enabled", newValue);
                break;
            case "leave":
                newValue = !plugin.getConfigManager().isLeaveEnabled();
                plugin.getConfigManager().setValue("leave.enabled", newValue);
                break;
            case "titles":
                newValue = !plugin.getConfigManager().isTitlesEnabled();
                plugin.getConfigManager().setValue("titles.enabled", newValue);
                break;
            case "fireworks":
                newValue = !plugin.getConfigManager().isFireworksEnabled();
                plugin.getConfigManager().setValue("fireworks.enabled", newValue);
                break;
            case "sounds":
                newValue = !plugin.getConfigManager().isSoundsEnabled();
                plugin.getConfigManager().setValue("sounds.enabled", newValue);
                break;
            default:
                sender.sendMessage(getMessage("invalid-feature"));
                return;
        }

        String messageKey = newValue ? "feature-enabled" : "feature-disabled";
        sender.sendMessage(getMessage(messageKey, "feature", feature));
    }

    private void handleSet(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(getMessage("usage-set"));
            return;
        }

        String path = args[1];
        String value = String.join(" ", Arrays.copyOfRange(args, 2, args.length));

        Object parsedValue = parseValue(value);
        plugin.getConfigManager().setValue(path, parsedValue);
        sender.sendMessage(getMessage("config-set", "path", path, "value", value));
    }

    private Object parseValue(String value) {
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            return Boolean.parseBoolean(value);
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ignored) {
        }

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ignored) {
        }

        return value;
    }

    private void handleAdd(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(getMessage("usage-add"));
            sender.sendMessage(getMessage("help-types"));
            return;
        }

        String type = args[1].toLowerCase();
        String message = String.join(" ", Arrays.copyOfRange(args, 2, args.length));

        String configPath;
        switch (type) {
            case "firstjoin":
                configPath = "first-join.messages";
                break;
            case "join":
                configPath = "join.messages";
                break;
            case "leave":
                configPath = "leave.messages";
                break;
            default:
                sender.sendMessage(getMessage("invalid-type"));
                return;
        }

        List<String> messages = plugin.getConfig().getStringList(configPath);
        messages.add(message);
        plugin.getConfigManager().setValue(configPath, messages);

        sender.sendMessage(getMessage("message-added", "type", type));
    }

    private void handleRemove(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(getMessage("usage-remove"));
            return;
        }

        String type = args[1].toLowerCase();
        int index;

        try {
            index = Integer.parseInt(args[2]) - 1; // Convert to 0-based index
        } catch (NumberFormatException e) {
            sender.sendMessage(getMessage("invalid-index"));
            return;
        }

        String configPath;
        switch (type) {
            case "firstjoin":
                configPath = "first-join.messages";
                break;
            case "join":
                configPath = "join.messages";
                break;
            case "leave":
                configPath = "leave.messages";
                break;
            default:
                sender.sendMessage(getMessage("invalid-type"));
                return;
        }

        List<String> messages = plugin.getConfig().getStringList(configPath);
        if (index < 0 || index >= messages.size()) {
            sender.sendMessage(getMessage("invalid-index"));
            return;
        }

        messages.remove(index);
        plugin.getConfigManager().setValue(configPath, messages);

        sender.sendMessage(getMessage("message-removed", "index", String.valueOf(index + 1), "type", type));
    }

    private void handleList(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(getMessage("usage-list"));
            sender.sendMessage(getMessage("help-types"));
            return;
        }

        String type = args[1].toLowerCase();
        List<String> messages;

        switch (type) {
            case "firstjoin":
                messages = plugin.getConfigManager().getFirstJoinMessages();
                break;
            case "join":
                messages = plugin.getConfigManager().getJoinMessages();
                break;
            case "leave":
                messages = plugin.getConfigManager().getLeaveMessages();
                break;
            default:
                sender.sendMessage(getMessage("invalid-type"));
                return;
        }

        if (messages.isEmpty()) {
            sender.sendMessage(getMessage("no-messages", "type", type));
            return;
        }

        sender.sendMessage(getMessage("list-header", "type", type));
        for (int i = 0; i < messages.size(); i++) {
            sender.sendMessage(getMessage("list-item", "index", String.valueOf(i + 1), "message", messages.get(i)));
        }
    }

    private void handleTest(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getMessage("player-only"));
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(getMessage("usage-test"));
            sender.sendMessage(getMessage("help-types"));
            return;
        }

        Player player = (Player) sender;
        String type = args[1].toLowerCase();

        switch (type) {
            case "firstjoin":
                plugin.getMessageManager().sendFirstJoinMessage(player);
                plugin.getMessageManager().sendTitle(player, true);
                plugin.getEffectManager().playJoinEffects(player, true);
                break;
            case "join":
                plugin.getMessageManager().sendJoinMessage(player);
                plugin.getMessageManager().sendTitle(player, false);
                plugin.getEffectManager().playJoinEffects(player, false);
                break;
            case "leave":
                plugin.getMessageManager().sendLeaveMessage(player);
                break;
            default:
                sender.sendMessage(getMessage("invalid-type"));
                return;
        }

        sender.sendMessage(getMessage("test-executed", "type", type));
    }

    private void handleSetMessage(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(getMessage("usage-setmsg"));
            sender.sendMessage(getMessage("help-msgtypes"));
            return;
        }

        String messageKey = args[1];
        String newMessage = String.join(" ", Arrays.copyOfRange(args, 2, args.length));

        messages.put(messageKey, newMessage);
        plugin.getConfig().set("command-messages." + messageKey, newMessage);
        plugin.saveConfig();

        sender.sendMessage(getMessage("config-set", "path", "command-messages." + messageKey, "value", newMessage));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission("welcomemsgs.admin")) {
            return Collections.emptyList();
        }

        if (args.length == 1) {
            return Arrays.asList("reload", "toggle", "set", "add", "remove", "list", "test", "setmsg")
                    .stream().filter(s -> s.startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        }

        if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "toggle":
                    return Arrays.asList("firstjoin", "join", "leave", "titles", "fireworks", "sounds")
                            .stream().filter(s -> s.startsWith(args[1].toLowerCase())).collect(Collectors.toList());
                case "add":
                case "remove":
                case "list":
                case "test":
                    return Arrays.asList("firstjoin", "join", "leave")
                            .stream().filter(s -> s.startsWith(args[1].toLowerCase())).collect(Collectors.toList());
                case "setmsg":
                    return messages.keySet().stream()
                            .filter(s -> s.startsWith(args[1].toLowerCase())).collect(Collectors.toList());
            }
        }

        return Collections.emptyList();
    }
}