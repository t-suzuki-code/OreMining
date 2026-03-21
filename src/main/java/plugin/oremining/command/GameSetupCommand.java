package plugin.oremining.command;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import plugin.oremining.Main;
import plugin.oremining.PlayerUtils;
import plugin.oremining.SchedulerManager;

public class GameSetupCommand extends BaseCommand {

  private final Main main;
  private SchedulerManager schedulerManager;

  @Getter
  private boolean isGameReady = false;
  @Getter
  private boolean isGamePlay = false;
  @Getter
  private Location gameStartLocation;

  private BukkitTask gameReadyTask;

  public GameSetupCommand(Main main) {
    this.main = main;
  }


  @Override
  public boolean onExecutePlayerCommand(Player player, Command command, String label,
      String[] args) {
    if (!isGameReady) {

      isGameReady = true;
      gameStartLocation = player.getLocation();
      PlayerUtils.resetPlayerStatus(player);
      PlayerUtils.generatePortal(player);
      player.sendMessage("ゲーム準備状態になりました。ポータルに入りゲームを開始してください！");
      player.sendMessage("ポータルに入らなかった場合、ゲーム準備状態は30秒で終了します。");

      gameReadyTask = new BukkitRunnable() {
        @Override
        public void run() {
          player.sendMessage("30秒経過したため、ゲーム準備状態を終了しました。");
          resetGameSetup();
          PlayerUtils.removePortal();
        }
      }.runTaskLater(main, 20 * 30);

    } else {
      player.sendMessage("ゲームプレイ中のため、コマンドの処理を実行できません。");
    }
    return true;
  }

  @Override
  public boolean onExecuteConsoleCommand(CommandSender sender, Command command, String label,
      String[] args) {
    return false;
  }

  public void resetGameReadyTask() {
    gameReadyTask.cancel();
  }

  public void resetIsGameReady() {
    isGameReady = false;
  }

  public void resetGameSetup() {
    gameReadyTask.cancel();
    isGameReady = false;
  }

  public void startIsGamePlay() {
    isGamePlay = true;
  }

  public void resetIsGamePlay() {
    isGamePlay = false;
  }

  public void setSchedulerManager(SchedulerManager schedulerManager) {
    this.schedulerManager = schedulerManager;
  }

  public void cancelGameTask() {
    schedulerManager.cancelGameTask();
  }

  public void unloadGameWorld() {
    schedulerManager.unloadGameWorld();
  }

  public void cleanupGame() {
    schedulerManager.cleanupGame();
  }

}

