package com.zivdekel.baloss.events;

import com.zivdekel.baloss.Baloss;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathEvent implements Listener {
    private final Baloss loss;

    public DeathEvent(Baloss loss) {
        this.loss = loss;
    }

    @EventHandler
    public void onDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();
        Economy economy = Baloss.getEconomy();


        double balance = economy.getBalance(player);
        double config = loss.getConfig().getDouble("lost-balance");
        double actualtakeaway = config / 10;
        double takeaway = balance / actualtakeaway;

        String msg = loss.getConfig().getString("death-message");

        if (player != null) {
            if (msg != null) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg) + economy.format(takeaway));
                economy.withdrawPlayer(player, takeaway);
            }
        }
    }
}
