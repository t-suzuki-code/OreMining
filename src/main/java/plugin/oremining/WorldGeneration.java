package plugin.oremining;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

public class WorldGeneration {


  private final World world;

  public WorldGeneration() {
    WorldCreator creator = new WorldCreator("OreMiningWorld");
    this.world = creator.createWorld();
  }

  public World getWorld() {
    return world;
  }

  public void removeWorld() {
    Bukkit.unloadWorld(world, false);
  }
}
