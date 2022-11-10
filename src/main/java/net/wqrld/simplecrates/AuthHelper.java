package net.wqrld.simplecrates;

import org.bukkit.command.CommandSender;

public class AuthHelper {
    public static boolean isAuthorized(CommandSender p){
        return (p.isOp() || p.hasPermission("simpleCrates.key"));
    }
}
