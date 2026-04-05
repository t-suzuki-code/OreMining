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

  /**
   * GameStateManagerに死亡イベントを通知します。
   *
   * @param e イベント情報
   */
  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent e) {
    gameStateManager.onPlayerDeath();
  }

  /**
   * GameStateManagerにリスポーンイベントを通知します。
   *
   * @param e イベント情報
   */
  @EventHandler
  public void onPlayerRespawn(PlayerRespawnEvent e) {
    gameStateManager.onPlayerRespawn(e);
  }

  /**
   * GameStateManagerにログアウトイベントを通知します。
   *
   * @param e イベント情報
   */
  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent e) {
    gameStateManager.onPlayerQuit();
  }

  /**
   * GameStateManagerにジョインイベントを通知します。
   *
   * @param e イベント情報
   */
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent e) {
    gameStateManager.onPlayerJoin(e);
  }
}
