package tech.itexpress.botattackanalyser;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class BotAttackAnalyserSpigot extends JavaPlugin implements Listener {

    private ConnectionLimiter connectionLimiter;
    private boolean attackInProgress = false;

    @Override
    public void onEnable() {
        connectionLimiter = new ConnectionLimiter(10);
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        if (!connectionLimiter.isAllowed()) {
            event.setKickMessage("");
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            if (!attackInProgress) {
                attackInProgress = true;
                Bukkit.broadcastMessage(ChatColor.RED + "Derzeit Unterliegt dem Server ein Botangriff! Einstweilig sind keine Loginversuche möglich bis Vollendung des Angriffes");
            }
        } else {
            attackInProgress = false;
        }
    }
}
