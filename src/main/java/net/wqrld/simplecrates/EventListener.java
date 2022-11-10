package net.wqrld.simplecrates;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static net.wqrld.simplecrates.AuthHelper.isAuthorized;

public class EventListener implements Listener {

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        if (e.getLine(0).equals("[SC]")) {
            if (isAuthorized(e.getPlayer())) {
                e.getPlayer().sendMessage("No perms to make crates");
                e.setCancelled(true);
                return;
            }
            if (e.getLine(1).equalsIgnoreCase("")) {
                e.getPlayer().sendMessage("must specify crate name");
                e.setCancelled(true);
                return;
            }


            e.getPlayer().sendMessage("A crate was successfully created with type " + e.getLine(0));
            e.getPlayer().sendMessage("Grab a key with /key " + e.getLine(0));
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (e.getBlock().getType() == Material.TRIPWIRE_HOOK) {
            if (e.getItemInHand().getItemMeta().getDisplayName().contains(ChatColor.WHITE + "Key")) {
                e.setCancelled(true);
                return;
            }

        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.CHEST) {
            Block crateBlock = e.getClickedBlock();
            for (int y = 1; y < 5; y++) {
                if (crateBlock.getRelative(BlockFace.DOWN, y).getType() == Material.CHEST) {
                    String crateType = "";

                    e.setCancelled(true);
                    Block storageBlock = crateBlock.getRelative(BlockFace.DOWN, y);
                    Chest storageChest = (Chest) storageBlock.getState();

                    if (storageChest.getBlockInventory().isEmpty() && isAuthorized(e.getPlayer())){
                        e.getPlayer().sendMessage("No rewards have been configured yet");
                        return;
                    }

                    if (storageBlock.getRelative(BlockFace.UP).getType() == Material.OAK_SIGN) {
                        Sign s = (Sign) storageBlock.getRelative(BlockFace.UP).getState();
                        if (s.getLine(0).equals("[SC]")) {
                            crateType = s.getLine(1);
                        }
                    }
                    if (crateType.equalsIgnoreCase("")) {
                        e.getPlayer().sendMessage("Crate type not defined yet");
                        return;
                    }


                    boolean haskey = false;
                    for (ItemStack is : e.getPlayer().getInventory().getContents()) {
                        if (is != null && is.getType() == Material.TRIPWIRE_HOOK && is.getItemMeta().getDisplayName().equals(ChatColor.AQUA + crateType + " " + ChatColor.WHITE + "Key")) {
                            haskey = true;
                            is.setAmount(1);
                            e.getPlayer().getInventory().removeItem(is);
                            break;
                        }
                    }
                    if (!haskey) {
                        e.getPlayer().sendMessage("You do not have a crate key");
                        return;
                    }


                    int len = storageChest.getBlockInventory().getStorageContents().length;

                    int index;
                    ItemStack prize;
                    do {
                        index = (int) Math.floor(Math.random() * len);
                        prize = storageChest.getBlockInventory().getItem(index);
                    } while (prize == null);
                    if (e.getPlayer().getInventory().firstEmpty() == -1) {
                        e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), prize);
                    } else {
                        e.getPlayer().getInventory().addItem(prize);
                    }


                    e.getPlayer().sendMessage(ChatColor.AQUA + "You won " + prize.getType().name().replace("_", " ").toLowerCase());
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ITEM_GOAT_HORN_SOUND_1, 100f, 1f);
                    e.getPlayer().spawnParticle(Particle.DRAGON_BREATH, e.getClickedBlock().getLocation(), 200);


                    break;
                }
            }

        }

    }

}
