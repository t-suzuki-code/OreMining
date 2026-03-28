package plugin.oremining;

import java.io.File;
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

    File worldFolder = world.getWorldFolder();
    deleteDirectory(worldFolder);
  }

  private void deleteDirectory(File dir) {
    if (dir.isDirectory()) {
      File[] files = dir.listFiles();
      if (files != null) {
        for (File file : files) {
          deleteDirectory(file);
        }
      }
    }
    dir.delete();
  }
}
