package plugin.oremining;

import java.io.File;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

public class WorldGeneration {

  @Getter
  private final World world;

  public WorldGeneration() {
    WorldCreator creator = new WorldCreator("OreMiningWorld");
    VoidChunkGenerator voidChunkGenerator = new VoidChunkGenerator();
    this.world = creator.generator(voidChunkGenerator).createWorld();
  }

  /**
   * ワールドを削除します。
   */
  public void removeWorld() {
    Bukkit.unloadWorld(world, false);
    deleteDirectory(world.getWorldFolder());
  }

  /**
   * ワールドのファイルを削除します。
   *
   * @param dir 生成されたファイル。
   */
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
