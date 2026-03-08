package plugin.oremining;

import java.util.List;
import java.util.SplittableRandom;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class OrePlacement {

  private final World world;

  public OrePlacement(World world) {
    this.world = world;
  }

  public void placeRandomOres() {

    Location basePosition = world.getSpawnLocation();

    for (int x = -10; x < 10; x++) {
      for (int y = 0; y < 5; y++) {
        for (int z = 0; z < 10; z++) {
          double xx = basePosition.getX() + x;
          double yy = basePosition.getY() + y;
          double zz = basePosition.getZ() + z + 5;
          Location location = new Location(basePosition.getWorld(), xx, yy, zz);
          location.getBlock().setType(Material.STONE_BRICKS);
        }
      }

    }

    for (int x = -1; x < 1; x++) {
      for (int y = 1; y < 4; y++) {
        for (int z = 0; z < 10; z++) {
          double xx = basePosition.getX() + x;
          double yy = basePosition.getY() + y;
          double zz = basePosition.getZ() + z + 5;
          Location location = new Location(basePosition.getWorld(), xx, yy, zz);
          location.getBlock().setType(Material.AIR);
        }
      }
    }

    for (int x = -10; x < 10; x++) {
      for (int y = 1; y < 4; y++) {
        for (int z = 7; z < 12; z += 2) {
          double xx = basePosition.getX() + x;
          double yy = basePosition.getY() + y;
          double zz = basePosition.getZ() + z;
          Location location = new Location(basePosition.getWorld(), xx, yy, zz);
          location.getBlock().setType(Material.AIR);
        }
      }
    }
    List<Material> oreList = List.of(Material.DIAMOND_ORE, Material.GOLD_ORE, Material.IRON_ORE);
    SplittableRandom random = new SplittableRandom();

    for (int i = 0; i < 30; i++) {

      int randomX = random.nextInt(20) - 10;
      int randomY = random.nextInt(5);
      int randomZ = random.nextInt(10) + 5;
      int oreNumber = random.nextInt(3);

      double x = basePosition.getX() + randomX;
      double y = basePosition.getY() + randomY;
      double z = basePosition.getZ() + randomZ;

      Material ore = oreList.get(oreNumber);

      Location location = new Location(basePosition.getWorld(), x, y, z);

      if (location.getBlock().getType() == Material.STONE_BRICKS) {
        location.getBlock().setType(ore);

      }
    }


  }

}
