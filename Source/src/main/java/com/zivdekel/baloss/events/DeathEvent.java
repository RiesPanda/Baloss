package com.zivdekel.baloss.events;

import com.zivdekel.baloss.Baloss;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.List;

public class DeathEvent implements Listener {
    private final Baloss loss;

    public DeathEvent(final Baloss loss) {
        this.loss = loss;
    }

    @EventHandler
    public void onDeathEvent(PlayerDeathEvent event) {
        // Grabbing the player
        final Player player = event.getEntity().getPlayer();
        // Grabbing the economy
        final Economy economy = Baloss.getEconomy();

        // Getting the permission the player has, which will change the percentage taken.
        int lostPercentage = getLossPercentage(player);

        // Getting the player's balance
        double balance = economy.getBalance(player);

        // Getting config stuff
        String msg = loss.getConfig().getString("death-message");
        String bypassmsg = loss.getConfig().getString("bypass-sent-message");
        boolean bypass = loss.getConfig().getBoolean("bypass-msg");
        List<String> worlds = loss.getConfig().getStringList("disabled-worlds");
        double defaultPercentage = loss.getConfig().getDouble("lost-balance");
        //

        double lostPer = (double) lostPercentage / 100;
        double lostAmount = balance * lostPer;

        //

        // Calculating default takeaway
        double lostDefaultPer = defaultPercentage / 100;
        double defaultAmount = balance * lostDefaultPer;


        // Getting player's world
        String world = player.getWorld().getName();


        // If the player does not have any permissions but has the bypass permission, ask if he has the bypass message allowed.
        // If he does, send him a message.
        if (lostPercentage == -1) {
            if (player.hasPermission("Baloss.bypass")) {
                if (bypass) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', bypassmsg));
                }
                // Otherwise, if the player doesn't have a permission and doesn't have the bypass permission, takeaway the default amount.
            } else {
                if (!worlds.contains(world) && (msg != null)) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg) + economy.format(defaultAmount));
                    economy.withdrawPlayer(player, defaultAmount);
                }
            }
            // Otherwise, if he does have a permission, take away the correct amount.
        } else {
            if (!worlds.contains(world) && !player.hasPermission("Baloss.bypass") && player.hasPermission("Baloss.lose." + getLossPercentage(player))) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg) + economy.format(lostAmount));
                economy.withdrawPlayer(player, lostAmount);
            } else if (player.hasPermission("Baloss.bypass")) {
                if (bypass) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', bypassmsg));
                }
            }
        }

    }

    private int getLossPercentage(Player player) {
        for (int i = 1; i <= 100; i++) {
            if (player.hasPermission("Baloss.lose." + i)) {
                return i;
            }
        }
        return -1;
    }

}
