package com.zivdekel.baloss.commands;

import com.zivdekel.baloss.Baloss;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand implements CommandExecutor {

    private Baloss loss;

    public ReloadCommand(Baloss loss) {
        this.loss = loss;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String perm = loss.getConfig().getString("perm-name");

        Player player = (Player) sender;
        if (perm != null) {
            if (command.getName().equalsIgnoreCase("reload")) {
                if (player.hasPermission(perm)) {
                    loss.reloadConfig();
                    player.sendMessage(ChatColor.GREEN + "Baloss has been reloaded successfully!");
                } else {
                    player.sendMessage(ChatColor.RED + "You do not have permission to use this command! If you think this is an error, please contact a server administrator or the plugin developer!");
                }
            } else {
                player.sendMessage(ChatColor.GREEN + "Available commands: \n - '/reload'");
            }
        } else {
            return false;
        }


        return true;
    }
}
