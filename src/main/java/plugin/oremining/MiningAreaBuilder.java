package plugin.oremining;

import java.util.List;
import java.util.SplittableRandom;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class MiningAreaBuilder {

  private final World world;

  private final List<Material> oreList = List.of(Material.DIAMOND_ORE, Material.GOLD_ORE,
      Material.IRON_ORE);

  private final SplittableRandom random = new SplittableRandom();

  private static final int AREA_WIDTH = 40;
  private static final int AREA_HEIGHT = 5;
  private static final int AREA_DEPTH = 20;

  private static final int CORRIDOR_WIDTH = 2;
  private static final int CORRIDOR_HEIGHT = 3;

  private static final int BRANCH_COUNT = 6;
  private static final int BRANCH_INTERVAL = 2;
  private static final int BRANCH_START_Z = 7;

  private static final int START_LOCATION_Z = 5;

  private static final int ORE_COUNT = 100;

  public MiningAreaBuilder(World world) {
    this.world = world;
  }

  public void buildMiningArea() {

    Location basePosition = world.getSpawnLocation();

    fillStoneBlocks(basePosition);
    fillBedRocks(basePosition);

    digMainCorridor(basePosition);
    digBranches(basePosition);

    placeOres(basePosition);
  }


  /**
   * 採掘エリアの石ブロックを配置します。
   *
   * @param basePosition プレイヤーがスポーンするロケーション。
   */
  private void fillStoneBlocks(Location basePosition) {
    for (int x = -AREA_WIDTH / 2; x < AREA_WIDTH / 2; x++) {
      for (int y = 0; y < AREA_HEIGHT; y++) {
        for (int z = 0; z < AREA_DEPTH; z++) {

          new Location(basePosition.getWorld(),
              basePosition.getX() + x,
              basePosition.getY() + y,
              basePosition.getZ() + z + START_LOCATION_Z)
              .getBlock().setType(Material.STONE_BRICKS);
        }
      }

    }
  }

  /**
   * 採掘エリアに岩盤を配置します。
   *
   * @param basePosition プレイヤーがスポーンするロケーション。
   */
  private void fillBedRocks(Location basePosition) {
    for (int x = -AREA_WIDTH / 2; x < AREA_WIDTH / 2; x++) {
      for (int z = 0; z < AREA_DEPTH; z++) {

        new Location(basePosition.getWorld(),
            basePosition.getX() + x,
            basePosition.getY() - 1,
            basePosition.getZ() + z)
            .getBlock().setType(Material.BEDROCK);
      }
    }
  }

  /**
   * 採掘エリアのメイン廊下を作成します。
   *
   * @param basePosition プレイヤーがスポーンするロケーション。
   */
  private void digMainCorridor(Location basePosition) {
    for (int x = -CORRIDOR_WIDTH / 2; x < CORRIDOR_WIDTH / 2; x++) {
      for (int y = 1; y < 1 + CORRIDOR_HEIGHT; y++) {
        for (int z = 0; z < AREA_DEPTH; z++) {

          new Location(basePosition.getWorld(),
              basePosition.getX() + x,
              basePosition.getY() + y,
              basePosition.getZ() + z + START_LOCATION_Z)
              .getBlock().setType(Material.AIR);
        }
      }
    }
  }

  /**
   * 採掘エリアに枝分かれした通路を作成します。
   *
   * @param basePosition プレイヤーがスポーンするロケーション。
   */
  private void digBranches(Location basePosition) {
    for (int x = -AREA_WIDTH / 2; x < AREA_WIDTH / 2; x++) {
      for (int y = 1; y < 1 + CORRIDOR_HEIGHT; y++) {
        for (int z = BRANCH_START_Z; z < BRANCH_START_Z + BRANCH_COUNT * BRANCH_INTERVAL;
            z += BRANCH_INTERVAL) {

          new Location(basePosition.getWorld(),
              basePosition.getX() + x,
              basePosition.getY() + y,
              basePosition.getZ() + z)
              .getBlock().setType(Material.AIR);
        }
      }
    }
  }

  /**
   * 採掘エリアの石ブロックをランダムで鉱石に変換します。
   *
   * @param basePosition プレイヤーのスポーンするロケーション。
   */
  private void placeOres(Location basePosition) {
    for (int i = 0; i < ORE_COUNT; i++) {

      Material ore = oreList.get(random.nextInt(oreList.size()));

      Location location = new Location(basePosition.getWorld(),
          basePosition.getX() + random.nextInt(AREA_WIDTH) - (double) AREA_WIDTH / 2,
          basePosition.getY() + random.nextInt(AREA_HEIGHT),
          basePosition.getZ() + random.nextInt(AREA_DEPTH) + START_LOCATION_Z);

      if (location.getBlock().getType() == Material.STONE_BRICKS) {
        location.getBlock().setType(ore);

      }
    }
  }
}
