package plugin.oremining;

import java.util.function.Supplier;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class SchedulerManager {

  private final Main main;
  private final Player player;
  private final Runnable onGameTimeUp;
  private final Supplier<Integer> scoreSupplier;
  private int gameTime = 60;
  private BukkitTask gameTask;


  public SchedulerManager(Main main, Player player, Runnable onGameTimeUp,
      Supplier<Integer> scoreSupplier) {
    this.main = main;
    this.player = player;
    this.onGameTimeUp = onGameTimeUp;
    this.scoreSupplier = scoreSupplier;
  }

  public void gameStart() {

    gameTask = Bukkit.getScheduler().runTaskTimer(main, () -> {
      if (gameTime <= 0) {
        gameTask.cancel();
        onGameTimeUp.run();
        return;
      }

      player.spigot().sendMessage(
          ChatMessageType.ACTION_BAR,
          new TextComponent(
              "現在のスコアは" + scoreSupplier.get() + "点！"
                  + "｜"
                  + "残り時間は" + gameTime + "秒です！"));

      gameTime -= 1;
    }, 0, 20);
  }

  /**
   * ゲームの制限時間を止めます。
   */
  public void cancelGameTask() {
    gameTask.cancel();
  }
}

