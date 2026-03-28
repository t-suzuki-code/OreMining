package plugin.oremining;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class SchedulerManager {

  private final Main main;
  private final Player player;
  private final Runnable onGameTimeUp;
  private int gameTime = 60;
  private BukkitTask gameTask;

  public SchedulerManager(Main main, Player player, Runnable onGameTimeUp) {
    this.main = main;
    this.player = player;
    this.onGameTimeUp = onGameTimeUp;
  }

  public void gameStart() {

    gameTask = Bukkit.getScheduler().runTaskTimer(main, () -> {
      if (gameTime <= 0) {

        gameTask.cancel();
        onGameTimeUp.run();

        return;
      }
      player.sendTitle("残り時間は" + gameTime + "秒です！", "", 0, 40, 0);
      gameTime -= 20;
    }, 0, 20 * 20);
  }

  /**
   * ゲームプレイ用の制限時間を止めます。
   */
  public void cancelGameTask() {
    gameTask.cancel();
  }
}

