package plugin.oremining;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

public class PlayerUtils {

  /**
   * ゲームを始める前にプレイヤーの状態を設定します。 体力・空腹値・状態異常を初期化、インベントリに火打石とツルハシを追加します。
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
  }

  /**
   * プレイヤーに設定されている特殊効果を除外します。
   *
   * @param player コマンドを実行したプレイヤー。
   */
  private static void removePotionEffect(Player player) {
    player.getActivePotionEffects().stream()
        .map(PotionEffect::getType)
        .forEach(player::removePotionEffect);
  }
}
