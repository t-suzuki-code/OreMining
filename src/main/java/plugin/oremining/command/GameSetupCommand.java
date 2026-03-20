package plugin.oremining.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import plugin.oremining.Main;
import plugin.oremining.PlayerUtils;

public class GameSetupCommand extends BaseCommand {

  private final Main main;
  private boolean isGameReady = false;
  private BukkitTask task;

  public GameSetupCommand(Main main) {
    this.main = main;
  }


  @Override
  public boolean onExecutePlayerCommand(Player player, Command command, String label,
      String[] args) {
    if (!isGameReady) {

      isGameReady = true;
      PlayerUtils.resetPlayerStatus(player);
      PlayerUtils.generatePortal(player);
      player.sendMessage("ゲーム準備状態になりました。ポータルに入りゲームを開始してください！");
      player.sendMessage("ポータルに入らなかった場合、ゲーム準備状態は30秒で終了します。");

      task = new BukkitRunnable() {
        @Override
        public void run() {
          player.sendMessage("30秒経過したため、ゲーム準備状態を終了しました。");
          resetGameSetup();
          PlayerUtils.removePortal();
        }
      }.runTaskLater(main, 20 * 30);

    } else {
      player.sendMessage("ゲーム準備状態のため、コマンドを実行できません。");
    }
    return true;
  }

  @Override
  public boolean onExecuteConsoleCommand(CommandSender sender, Command command, String label,
      String[] args) {
    return false;
  }

  public void resetGameSetup() {
    task.cancel();
    isGameReady = false;
  }


}

