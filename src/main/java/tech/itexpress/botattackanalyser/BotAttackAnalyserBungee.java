package tech.itexpress.botattackanalyser;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ChatColor;

public class BotAttackAnalyserBungee extends Plugin implements Listener {

    private ConnectionLimiter connectionLimiter;
    private boolean attackInProgress = false;

    @Override
    public void onEnable() {
        connectionLimiter = new ConnectionLimiter(10);
        getProxy().getPluginManager().registerListener(this, this);
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        if (!connectionLimiter.isAllowed()) {
            event.getPlayer().disconnect("Zu viele Verbindungen pro Sekunde.");
            if (!attackInProgress) {
                attackInProgress = true;
                ProxyServer.getInstance().broadcast(ChatColor.RED + "Derzeit Unterliegt dem Server ein Botangriff! Einstweilig sind keine Loginversuche möglich bis Vollendung des Angriffes");
            }
        } else {
            attackInProgress = false;
        }
    }
}
