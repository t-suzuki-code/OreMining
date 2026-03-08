package plugin.oremining;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class SchedulerManager {

  private Player player;
  private Location location;
  private World world;

  public SchedulerManager(Player player, Location location, World world) {
    this.player = player;
    this.location = location;
    this.world = world;
  }

  public void gameStart() {

  }


}
