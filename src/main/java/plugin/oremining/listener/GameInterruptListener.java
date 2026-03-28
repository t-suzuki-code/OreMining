package plugin.oremining.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import plugin.oremining.GameStateManager;

public class GameInterruptListener implements Listener {

  private final GameStateManager gameStateManager;

  public GameInterruptListener(GameStateManager gameStateManager) {
    this.gameStateManager = gameStateManager;
  }

  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent e) {
    gameStateManager.onPlayerDeath();
  }

  @EventHandler
  public void onPlayerRespawn(PlayerRespawnEvent e) {
    gameStateManager.onPlayerRespawn(e);
  }

  @EventHandler
  public void onPlayerLogout(PlayerQuitEvent e) {
    gameStateManager.onPlayerQuit();
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent e) {
    gameStateManager.onPlayerJoin(e);
  }
}
