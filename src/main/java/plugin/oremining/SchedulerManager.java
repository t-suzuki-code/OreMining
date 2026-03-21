package plugin.oremining;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import plugin.oremining.command.GameSetupCommand;
import plugin.oremining.listener.PlayerScoreListener;
import plugin.oremining.mapper.data.PlayerScore;

public class SchedulerManager {

  private final Player player;
  private final World world;
  private final Main main;
  private final PlayerScoreListener playerScoreListener;
  private final DBManager dbManager = new DBManager();
  private final GameSetupCommand gameSetupCommand;
  private int gameTime = 60;
  private BukkitTask gameTask;


  public SchedulerManager(Player player, World world, Main main,
      PlayerScoreListener playerScoreListener, GameSetupCommand gameSetupCommand) {
    this.player = player;
    this.world = world;
    this.main = main;
    this.playerScoreListener = playerScoreListener;
    this.gameSetupCommand = gameSetupCommand;

  }

  public void gameStart() {

    gameTask = Bukkit.getScheduler().runTaskTimer(main, () -> {
      if (gameTime <= 0) {

        dbManager.insert(new PlayerScore(player.getName(),
            playerScoreListener.getPlayerScore()));

        gameTask.cancel();
        player.teleport(gameSetupCommand.getGameStartLocation());

        endGame();

        return;
      }
      player.sendTitle("残り時間は" + gameTime + "秒です！", "", 0, 40, 0);
      gameTime -= 20;
    }, 0, 20 * 20);
  }

  public void resetGameTask() {
    gameTask.cancel();
  }

  public void endGame() {
    Bukkit.unloadWorld(world, false);
    PlayerUtils.resetPlayerStatus(player);
    gameSetupCommand.resetIsGameReady();
    gameSetupCommand.resetIsGamePlay();
    playerScoreListener.reset();
  }


}
