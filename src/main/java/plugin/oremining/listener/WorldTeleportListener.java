package plugin.oremining.listener;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import plugin.oremining.OrePlacement;
import plugin.oremining.SchedulerManager;
import plugin.oremining.WorldGeneration;

public class WorldTeleportListener implements Listener {


  @EventHandler
  public void onPlayerPortal(PlayerPortalEvent e) {
    Player player = e.getPlayer();
    Location location = player.getLocation();
    e.setCancelled(true);

    WorldGeneration worldGeneration = new WorldGeneration();
    World teleportWorld = worldGeneration.getWorld();

    OrePlacement orePlacement = new OrePlacement(teleportWorld);
    orePlacement.placeRandomOres();

    player.teleport(teleportWorld.getSpawnLocation());

    SchedulerManager schedulerManager = new SchedulerManager(player, location, teleportWorld);
    schedulerManager.gameStart();
  }
}
