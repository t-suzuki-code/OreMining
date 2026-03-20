package plugin.oremining.listener;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import plugin.oremining.Main;
import plugin.oremining.MiningAreaBuilder;
import plugin.oremining.PlayerUtils;
import plugin.oremining.SchedulerManager;
import plugin.oremining.WorldGeneration;
import plugin.oremining.command.GameSetupCommand;

public class WorldTeleportListener implements Listener {

  private final Main main;
  private final PlayerScoreListener playerScoreListener;
  private final GameSetupCommand gameSetupCommand;

  public WorldTeleportListener(Main main, PlayerScoreListener playerScoreListener,
      GameSetupCommand gameSetupCommand) {
    this.main = main;
    this.playerScoreListener = playerScoreListener;
    this.gameSetupCommand = gameSetupCommand;
  }

  @EventHandler
  public void onPlayerPortal(PlayerPortalEvent e) {
    Player player = e.getPlayer();
    Location location = player.getLocation();
    e.setCancelled(true);
    gameSetupCommand.resetGameSetup();

    WorldGeneration worldGeneration = new WorldGeneration();
    World teleportWorld = worldGeneration.getWorld();

    MiningAreaBuilder miningAreaBuilder = new MiningAreaBuilder(teleportWorld);
    miningAreaBuilder.buildMiningArea();

    player.teleport(teleportWorld.getSpawnLocation());

    playerScoreListener.setTeleportWorld(teleportWorld);

    PlayerUtils.removePortal();

    SchedulerManager schedulerManager = new SchedulerManager(player, location, teleportWorld, main,
        playerScoreListener);
    schedulerManager.gameStart();
  }

}
