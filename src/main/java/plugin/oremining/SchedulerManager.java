package plugin.oremining;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import plugin.oremining.listener.PlayerScoreListener;
import plugin.oremining.mapper.data.PlayerScore;

public class SchedulerManager {

  private final Player player;
  private final Location location;
  private final World world;
  private final Main main;
  private final PlayerScoreListener playerScoreListener;
  private final DBManager dbManager = new DBManager();
  private int gameTime = 60;

  public SchedulerManager(Player player, Location location, World world, Main main,
      PlayerScoreListener playerScoreListener) {
    this.player = player;
    this.location = location;
    this.world = world;
    this.main = main;
    this.playerScoreListener = playerScoreListener;

  }

  public void gameStart() {

    Bukkit.getScheduler().runTaskTimer(main, task -> {
      if (gameTime <= 0) {
        task.cancel();
        player.teleport(location);
        Bukkit.unloadWorld(world, false);
        PlayerUtils.resetPlayerStatus(player);

        dbManager.insert(new PlayerScore(player.getName(),
            playerScoreListener.getPlayerScore()));

        playerScoreListener.reset();
        return;
      }
      player.sendTitle("残り時間は" + gameTime + "秒です！", "", 0, 40, 0);
      gameTime -= 20;
    }, 0, 20 * 20);
  }


}
