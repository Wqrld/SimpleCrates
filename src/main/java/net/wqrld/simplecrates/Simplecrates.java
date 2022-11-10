package net.wqrld.simplecrates;

import org.bukkit.plugin.java.JavaPlugin;

public final class Simplecrates extends JavaPlugin{

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new EventListener(), this);
        this.getCommand("key").setExecutor(new KeyCommand());
    }
}
