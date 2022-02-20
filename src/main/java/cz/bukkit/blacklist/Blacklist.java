package cz.bukkit.blacklist;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;


public final class Blacklist extends JavaPlugin implements Listener {

    private final List<String> blockedNames = new ArrayList<>();
    private String kickMessage;
    private String prefix;
    private boolean debugMode;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        blockedNames.addAll(getConfig().getStringList("blockedNames"));
        kickMessage = getConfig().getString("kickmessage", "Your nick is blacklisted.");
        prefix = getConfig().getString("prefix", "[Blacklist]");
        debugMode = getConfig().getBoolean("debug", false);
    }

    @Override
    public void onDisable() {
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerPreLoginEventLowest(AsyncPlayerPreLoginEvent event)
    {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            return;
        }
        final String name = event.getName().toLowerCase();
        blockedNames.forEach(test -> {
            if (name.contains(test)) {
                event.setKickMessage(prefix + kickMessage.replace("%blocked%", test));
                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                if (debugMode) {
                    getLogger().info("Player " + name + " was kicked because name contains " + test + " phrase.");
                }
            }
        });
    }

}
