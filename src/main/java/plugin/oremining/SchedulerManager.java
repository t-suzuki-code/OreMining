package plugin.oremining;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class SchedulerManager {

  private final Player player;
  private final Location location;
  private final World world;
  private final Main main;
  private int gameTime = 60;

  public SchedulerManager(Player player, Location location, World world, Main main) {
    this.player = player;
    this.location = location;
    this.world = world;
    this.main = main;

  }

  public void gameStart() {

    Bukkit.getScheduler().runTaskTimer(main, task -> {
      if (gameTime <= 0) {
        task.cancel();
        player.teleport(location);
        Bukkit.unloadWorld(world, false);
        PlayerUtils.resetPlayerStatus(player);
        return;
      }
      player.sendTitle("残り時間は" + gameTime + "秒です！", "", 0, 40, 0);
      gameTime -= 20;
    }, 0, 20 * 20);
  }


}
