package plugin.oremining;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SplittableRandom;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class MiningAreaBuilder {

  private final World world;
  private Location basePosition;
  private final int random = new SplittableRandom().nextInt(100);

  // ===== エリアサイズ =====
  private static final int AREA_HALF = 49;

  // ===== Y座標 =====
  private static final int SAFETY_LAYER_Y = -1;
  private static final int FLOOR_Y = 0;
  private static final int TUNNEL_MIN_Y = 1;
  private static final int TUNNEL_MAX_Y = 4;
  private static final int CEILING_Y = 5;

  // ===== Z方向の枝通路 =====
  private static final int Z_BRANCH_OFFSET_NEAR = 8;
  private static final int Z_BRANCH_OFFSET_FAR = 32;

  // ===== 通路 =====
  private static final int TUNNEL_PERIOD = 3;
  private static final int BRANCH_START_OFFSET = 10;

  // ===== スポーン空間 =====
  private static final int SPAWN_HALF = 3;

  // ===== 鉱石 =====
  private static final int ORE_COUNT = 500;// 要調整

  // ===== 鉱石の出現率（%） =====
  private static final int IRON_ORE_RATE = 50;
  private static final int GOLD_ORE_RATE = 80;

  public MiningAreaBuilder(World world) {
    this.world = world;
  }

  /**
   * 採掘エリアを構築する。 石ブロックで全体を充填した後、岩盤の殻・通路・スポーン空間・鉱石の順に構築する。 ワールドのスポーン位置を通路空間のY座標に設定する。
   */
  public void build() {

    this.basePosition = world.getSpawnLocation();

    fillStoneBlocks();
    buildBedrockShell();
    digMainCorridors();
    digBranchTunnels();
    clearSpawnRoom();
    placeGlowstoneFloor();
    placeOres();

    world.setSpawnLocation(
        basePosition.getBlockX(),
        basePosition.getBlockY() + TUNNEL_MIN_Y,
        basePosition.getBlockZ()
    );
  }


  /**
   * 石ブロックで全エリアを充填する。Y0〜Y5の6層分を石ブロックで埋める。
   *
   */
  private void fillStoneBlocks() {
    for (int x = -AREA_HALF; x <= AREA_HALF; x++) {
      for (int z = -AREA_HALF; z <= AREA_HALF; z++) {
        for (int y = FLOOR_Y; y <= CEILING_Y; y++) {
          world.getBlockAt(
              basePosition.getBlockX() + x,
              basePosition.getBlockY() + y,
              basePosition.getBlockZ() + z
          ).setType(Material.STONE);
        }
      }
    }
  }

  /**
   * 岩盤の殻を構築する。 Y-1の安全層、Y5の天井、四方の外壁を岩盤で囲む。
   *
   */
  private void buildBedrockShell() {
    int baseX = basePosition.getBlockX();
    int baseY = basePosition.getBlockY();
    int baseZ = basePosition.getBlockZ();

    // Y-1（安全層）とY5（天井）
    for (int x = -AREA_HALF; x <= AREA_HALF; x++) {
      for (int z = -AREA_HALF; z <= AREA_HALF; z++) {
        world.getBlockAt(baseX + x, baseY + SAFETY_LAYER_Y, baseZ + z)
            .setType(Material.BEDROCK);
        world.getBlockAt(baseX + x, baseY + CEILING_Y, baseZ + z)
            .setType(Material.BEDROCK);
      }
    }

    // 四方の外壁
    for (int y = SAFETY_LAYER_Y; y <= CEILING_Y; y++) {
      for (int i = -AREA_HALF; i <= AREA_HALF; i++) {
        world.getBlockAt(baseX - AREA_HALF, baseY + y, baseZ + i)
            .setType(Material.BEDROCK);
        world.getBlockAt(baseX + AREA_HALF, baseY + y, baseZ + i)
            .setType(Material.BEDROCK);
        world.getBlockAt(baseX + i, baseY + y, baseZ - AREA_HALF)
            .setType(Material.BEDROCK);
        world.getBlockAt(baseX + i, baseY + y, baseZ + AREA_HALF)
            .setType(Material.BEDROCK);
      }
    }
  }

  /**
   * 十字型のメイン通路を掘削する。 X方向とZ方向に幅2マスの通路をエリア中央で交差させる。
   *
   */
  private void digMainCorridors() {
    int baseX = basePosition.getBlockX();
    int baseY = basePosition.getBlockY();
    int baseZ = basePosition.getBlockZ();

    for (int i = -AREA_HALF + 1; i < AREA_HALF; i++) {
      for (int y = TUNNEL_MIN_Y; y <= TUNNEL_MAX_Y; y++) {
        for (int offset = 0; offset < 2; offset++) {

          // X方向メイン通路
          world.getBlockAt(baseX + i, baseY + y, baseZ + offset)
              .setType(Material.AIR);

          // Z方向メイン通路
          world.getBlockAt(baseX + offset, baseY + y, baseZ + i)
              .setType(Material.AIR);
        }
      }
    }
  }

  /**
   * 枝通路を掘削する。 X方向：メイン通路からZ方向にBRANCH_START_OFFSETの位置から3マス周期で配置。 Z方向：固定4本（中心から±8, ±32の位置）。
   */
  private void digBranchTunnels() {
    int baseX = basePosition.getBlockX();
    int baseY = basePosition.getBlockY();
    int baseZ = basePosition.getBlockZ();

    // X方向の枝通路（正負両方向）
    int[] signs = {1, -1};
    for (int sign : signs) {
      for (int z = BRANCH_START_OFFSET; z < AREA_HALF; z += TUNNEL_PERIOD) {
        for (int x = -AREA_HALF + 1; x < AREA_HALF; x++) {
          for (int blockY = TUNNEL_MIN_Y; blockY <= TUNNEL_MAX_Y; blockY++) {
            world.getBlockAt(baseX + x, baseY + blockY, baseZ + z * sign)
                .setType(Material.AIR);
          }
        }
      }
    }

    // Z方向の枝通路（固定4本）
    int[] zBranchOffsets = {Z_BRANCH_OFFSET_NEAR, Z_BRANCH_OFFSET_FAR};
    for (int offset : zBranchOffsets) {
      for (int z = -AREA_HALF + 1; z < AREA_HALF; z++) {
        for (int blockY = TUNNEL_MIN_Y; blockY <= TUNNEL_MAX_Y; blockY++) {
          world.getBlockAt(baseX + offset, baseY + blockY, baseZ + z)
              .setType(Material.AIR);
          world.getBlockAt(baseX - offset, baseY + blockY, baseZ + z)
              .setType(Material.AIR);
        }
      }
    }
  }


  /**
   * スポーン空間を確保する。 メイン通路の交差部分を含む中央6×4×6の壁を取り除く。
   *
   */
  private void clearSpawnRoom() {
    int baseX = basePosition.getBlockX();
    int baseY = basePosition.getBlockY();
    int baseZ = basePosition.getBlockZ();

    for (int x = -SPAWN_HALF + 1; x <= SPAWN_HALF; x++) {
      for (int z = -SPAWN_HALF + 1; z <= SPAWN_HALF; z++) {
        for (int y = TUNNEL_MIN_Y; y <= TUNNEL_MAX_Y; y++) {
          world.getBlockAt(baseX + x, baseY + y, baseZ + z)
              .setType(Material.AIR);
        }
      }
    }
  }

  /**
   * 全通路の床をグローストーンに置換する。 Y0層で、直上（Y1）が空気ブロックの位置を通路と判定して置換する。
   *
   */
  private void placeGlowstoneFloor() {
    int baseX = basePosition.getBlockX();
    int baseY = basePosition.getBlockY();
    int baseZ = basePosition.getBlockZ();

    for (int x = -AREA_HALF + 1; x < AREA_HALF; x++) {
      for (int z = -AREA_HALF + 1; z < AREA_HALF; z++) {
        if (world.getBlockAt(baseX + x, baseY + TUNNEL_MIN_Y, baseZ + z)
            .getType() == Material.AIR) {
          world.getBlockAt(baseX + x, baseY + FLOOR_Y, baseZ + z)
              .setType(Material.GLOWSTONE);
        }
      }
    }
  }

  /**
   * 鉱石をランダムに配置する。 通路の壁（石ブロック）の中からランダムにORE_COUNT個を選んで鉱石に置換する。
   *
   */
  private void placeOres() {
    List<Block> stoneBlocks = collectStoneBlocks();
    Collections.shuffle(stoneBlocks);

    for (int i = 0; i < ORE_COUNT && i < stoneBlocks.size(); i++) {
      stoneBlocks.get(i).setType(getRandomOre());
    }
  }

  /**
   * 通路空間にある石ブロックを全て収集する。
   *
   * @return 石ブロックのリスト
   */
  private List<Block> collectStoneBlocks() {
    List<Block> stoneBlocks = new ArrayList<>();
    int baseX = basePosition.getBlockX();
    int baseY = basePosition.getBlockY();
    int baseZ = basePosition.getBlockZ();

    for (int x = -AREA_HALF + 1; x < AREA_HALF; x++) {
      for (int z = -AREA_HALF + 1; z < AREA_HALF; z++) {
        for (int y = TUNNEL_MIN_Y; y <= TUNNEL_MAX_Y; y++) {
          Block block = world.getBlockAt(baseX + x, baseY + y, baseZ + z);
          if (block.getType() == Material.STONE) {
            stoneBlocks.add(block);
          }
        }
      }
    }
    return stoneBlocks;
  }

  /**
   * 鉱石の種類をランダムに返す。
   *
   * @return 鉄鉱石、金鉱石、ダイヤモンド鉱石のいずれか
   */
  private Material getRandomOre() {

    if (random < IRON_ORE_RATE) {
      return Material.IRON_ORE;
    }
    if (random < GOLD_ORE_RATE) {
      return Material.GOLD_ORE;
    }
    return Material.DIAMOND_ORE;
  }
}
