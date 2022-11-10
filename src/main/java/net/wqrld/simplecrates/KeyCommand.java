package net.wqrld.simplecrates;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static net.wqrld.simplecrates.AuthHelper.isAuthorized;

public class KeyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player) && !isAuthorized(sender)) {
            sender.sendMessage("You do not have permission to execute this command");
            return false;
        }
        if (args.length != 1 && args.length != 2) {
            sender.sendMessage("/key <name> [player]");
            return false;
        }


        ItemStack key = new ItemStack(Material.TRIPWIRE_HOOK);
        ItemMeta im = key.getItemMeta();
        im.setDisplayName(ChatColor.AQUA + args[0] + " " + ChatColor.WHITE + "Key");
        key.setItemMeta(im);
        if (args.length == 1 && sender instanceof Player) {
            Player p = (Player) sender;
            p.getInventory().addItem(key);
            p.sendMessage("You have received a key");
        } else if (args.length == 2) {
            Player p = sender.getServer().getOnlinePlayers().stream().filter(player -> player.getName().equals(args[1])).findFirst().orElse(null);
            if (p == null) {
                sender.sendMessage("Not found");
                return false;
            }
            p.getInventory().addItem(key);
            sender.sendMessage("The key has been given.");

        }

        return true;


    }
}
