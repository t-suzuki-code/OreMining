package plugin.oremining.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import plugin.oremining.GameStateManager;

public class WorldTeleportListener implements Listener {

  private final GameStateManager gameStateManager;

  public WorldTeleportListener(GameStateManager gameStateManager) {
    this.gameStateManager = gameStateManager;
  }

  /**
   * プレイヤーポータル通過イベントを状態管理クラスに通知します。
   *
   * @param e イベント情報
   */
  @EventHandler
  public void onPlayerPortal(PlayerPortalEvent e) {
    gameStateManager.onPlayerPortal(e);
  }
}
