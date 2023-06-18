package ww1.plugin.pack;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;


public class myListener implements Listener{
	
	Location spawnLocation = new Location(Bukkit.getServer().getWorld("world"),-90,233,-166);
	//occurs when something is hit
    @EventHandler
    public void somethingHit​(EntityDamageByEntityEvent e) {
    	//Bukkit.broadcastMessage(e.getCause().toString());
    	if(e.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
    		Vector target = e.getEntity().getLocation().getDirection();
    		Vector arrow = e.getDamager().getVelocity();
    		target = target.normalize();
    		arrow = arrow.normalize();
    		boolean facing = target.getX() * arrow.getX() + target.getZ() * arrow.getZ() < 0.45;
    		//Damageable target = (Damageable)e.getEntity();
    		boolean cancel = false;
    		if(e.getEntityType() == EntityType.PLAYER) {
    			Player p = (Player)e.getEntity();
    			if(p.isBlocking() && facing) {
    				cancel = true;
    			}
    		}
    		if(!cancel) {
    			e.setDamage(22);
    		}
    		//((Damageable)e.getEntity()).setHealth(0);
    	}
    }
    //occurs when a projectile hits
    @EventHandler
    public void arrowMiss(ProjectileHitEvent e) {
    	//Bukkit.broadcastMessage(e.getHitBlock().toString());
    	if(e.getHitBlock() != null) {
    		e.getEntity().remove();
    	}
    }
    
    
    @EventHandler
    public void onPlayerDeathDrop(PlayerDeathEvent e) {
    	e.getDrops().clear();
          
    }
    
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event){
    	String typeNameString = event.getItemDrop().getItemStack().getType().name();
    	if (typeNameString.endsWith("_HELMET")
                || typeNameString.endsWith("_CHESTPLATE")
                || typeNameString.endsWith("_LEGGINGS")
                || typeNameString.endsWith("_BOOTS")) {
    		event.getItemDrop().remove();
    		//event.setCancelled(true);
        }
    }
    
    //occurs when someone shoots an arrow
    @EventHandler
    public void shootArrow(EntityShootBowEvent e) {
    	if(e.getBow().getType() == Material.BOW) {
        	Vector projectileVector = e.getProjectile().getVelocity();
        	projectileVector.setX(projectileVector.getX() * 5);
        	projectileVector.setY(projectileVector.getY() * 5);
        	projectileVector.setZ(projectileVector.getZ() * 5);
        	e.getProjectile().setVelocity(projectileVector);
    	}
    	else {
        	//Bukkit.broadcastMessage(e.getHitBlock().toString());
        	Vector projectileVector = e.getProjectile().getVelocity();
        	projectileVector.setX(projectileVector.getX() * 5);
        	projectileVector.setY(projectileVector.getY() * 5);
        	projectileVector.setZ(projectileVector.getZ() * 5);
        	e.getProjectile().setVelocity(projectileVector);
    	}

    }
    //occurs when something is damaged, aka left click
    @EventHandler
    public void itemFrameItemRemoval(EntityDamageEvent e) {
        if (e.getEntity() instanceof ItemFrame) {
            e.setCancelled(true);
        }
    }
    //occurs when the player enter the bed
    @EventHandler
    public void bedEnter(PlayerBedEnterEvent e) {
    	e.setCancelled(true);
    	e.getPlayer().setBedSpawnLocation(spawnLocation);
    }
    //occurs when the player logs in
    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
    	Player newPlayer = e.getPlayer();
    	newPlayer.sendMessage("Welcome to our server!");
    	newPlayer.sendMessage("This is a WW1 themed server with the TDM gamemode.");
    	newPlayer.sendMessage(ChatColor.RED.toString() + "TLDR: Kill the enemy team");
    	newPlayer.sendMessage("We recommend you install the 'World War 1 Resource Pack' by Sachosenior.");
    	newPlayer.sendMessage(ChatColor.GREEN.toString() + "Have Fun!");
    	newPlayer.teleport(spawnLocation);
    	newPlayer.getInventory().clear();
    	//Message sent, player teleported and his inventory is cleared. Job done
    }
    //occurs when the player interacts with a block
    @EventHandler
    public void buttonInteract(PlayerInteractEvent e) {
    	Block clickedBlock = e.getClickedBlock();
    	//if player clicked the trapdoor with rightclick 
    	if (clickedBlock.getType().toString().contains("TRAPDOOR") && e.getAction().toString() == "RIGHT_CLICK_BLOCK" && e.getPlayer().getGameMode() != GameMode.CREATIVE) {
    		e.setCancelled(true);
    	}
    	//else if player clicked dispenser with rightclick
    	else if (clickedBlock.getType() == Material.DISPENSER && e.getAction().toString() == "RIGHT_CLICK_BLOCK" && e.getPlayer().isSneaking() == false) {
    		Dispenser clickedDispenser = (Dispenser) clickedBlock.getState();
    		clickedDispenser.dispense();
    		e.setCancelled(true);
    	}
    	
    }
    //occurs when the player respawn
    @EventHandler
    public void respawnTeleport(PlayerRespawnEvent e) {
    	Player newPlayer = e.getPlayer();
    	Location spawnLocation = new Location(newPlayer.getWorld(),-90,233,-166);
    	newPlayer.teleport(spawnLocation);
    }
    //occurs when a dispenser fires
    @EventHandler
    public void shootDispenser(BlockDispenseEvent e) {
    	if(e.getItem().getType() == Material.ARROW) {
        	Vector projectileVector = e.getVelocity();
        	//Bukkit.broadcastMessage(Double.toString(projectileVector.getY()));
        	projectileVector.setX(projectileVector.getX() * 30);
        	projectileVector.setY(3);
        	projectileVector.setZ(projectileVector.getZ() * 30);
        	e.setVelocity(projectileVector);
    	}

    }
    
    @EventHandler
    public void ignoreGas(EntityPotionEffectEvent e) {
    	if(e.getEntityType() == EntityType.PLAYER) {
    		Player p = (Player)e.getEntity();
    		if(p.getInventory().getHelmet().getType() == Material.CARVED_PUMPKIN) {
    			e.setCancelled(true);
    		}
    	}
    	
    }
    
}
