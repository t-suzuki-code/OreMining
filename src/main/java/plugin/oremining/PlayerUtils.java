package plugin.oremining;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

public class PlayerUtils {

  private static final List<Location> portalLocationList = new ArrayList<>();

  /**
   * ゲームを始める前にプレイヤーの状態を設定する。 体力・空腹値・状態異常を初期化 装備をネザライト一式にし、インベントリに火打石を追加。
   *
   * @param player コマンドを実行したプレイヤー
   */

  public static void resetPlayerStatus(Player player) {
    player.setHealth(20);
    player.setFoodLevel(20);
    removePotionEffect(player);

    PlayerInventory inventory = player.getInventory();
    inventory.clear();
    inventory.setItem(0, new ItemStack(Material.FLINT_AND_STEEL));
    inventory.setItem(1, new ItemStack(Material.NETHERITE_PICKAXE));
    inventory.setItem(2, new ItemStack(Material.TORCH, 20));
    inventory.setHelmet(new ItemStack(Material.NETHERITE_HELMET));
    inventory.setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE));
    inventory.setLeggings(new ItemStack(Material.NETHERITE_LEGGINGS));
    inventory.setBoots(new ItemStack(Material.NETHERITE_BOOTS));
  }

  /**
   * プレイヤーに設定されている特殊効果を除外する
   *
   * @param player コマンドを実行したプレイヤー
   */
  public static void removePotionEffect(Player player) {
    player.getActivePotionEffects().stream()
        .map(PotionEffect::getType)
        .forEach(player::removePotionEffect);
  }

  /**
   * ポータル生成のための黒曜石を配置します。
   *
   * @param location 黒曜石を配置するロケーション。
   */
  public static void placeObsidian(Location location) {
    location.getBlock().setType(Material.OBSIDIAN);
  }

  /**
   * テレポートするためのポータルを作ります
   *
   * @param player コマンドを実行したプレイヤー
   */
  public static void generatePortal(Player player) {
    Location playerLocation = player.getLocation();
    double z = playerLocation.getZ() + 5;

    for (int i = 0; i < 4; i++) {

      double x = playerLocation.getX() + i;

      Location bottomLocation = new Location(player.getWorld(), x, playerLocation.getY(), z);
      Location topLocation = new Location(player.getWorld(), x, playerLocation.getY() + 4, z);

      placeObsidian(bottomLocation);
      placeObsidian(topLocation);
      portalLocationList.add(bottomLocation);
      portalLocationList.add(topLocation);

    }
    for (int i = 0; i < 5; i++) {

      double y = playerLocation.getY() + i;

      Location leftLocation = new Location(player.getWorld(), playerLocation.getX(), y, z);
      Location rightLocation = new Location(player.getWorld(), playerLocation.getX() + 3, y, z);

      placeObsidian(leftLocation);
      placeObsidian(rightLocation);
      portalLocationList.add(leftLocation);
      portalLocationList.add(rightLocation);

    }
  }

  /**
   * ポータルを削除します
   */
  public static void removePortal() {
    List<Location> copy = new ArrayList<>(portalLocationList);
    portalLocationList.clear();

    for (Location removeLocation : copy) {
      removeLocation.getBlock().setType(Material.AIR);

    }
  }

}
