package plugin.oremining.command;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.jspecify.annotations.NonNull;

public class GameSetupCommand implements CommandExecutor {

  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command,
      @NonNull String label, @NonNull String @NonNull [] args) {
    if (sender instanceof Player player) {

      resetPlayerStatus(player);
      generatePortal(player);

    }
    return true;
  }


  /**
   * ゲームを始める前にプレイヤーの状態を設定する。 体力・空腹値・状態異常を初期化 装備をネザライト一式にし、インベントリに火打石を追加。
   *
   * @param player コマンドを実行したプレイヤー
   */

  private void resetPlayerStatus(Player player) {
    player.setHealth(20);
    player.setFoodLevel(20);
    removePotionEffect(player);

    PlayerInventory inventory = player.getInventory();
    inventory.clear();
    inventory.setItem(0, new ItemStack(Material.FLINT_AND_STEEL));
    inventory.setItem(1, new ItemStack(Material.NETHERITE_PICKAXE));
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
  private void removePotionEffect(Player player) {
    player.getActivePotionEffects().stream()
        .map(PotionEffect::getType)
        .forEach(player::removePotionEffect);
  }

  /**
   * ポータル生成のための黒曜石を配置します。
   *
   * @param location 黒曜石を配置するロケーション。
   */
  private void placeObsidian(Location location) {
    location.getBlock().setType(Material.OBSIDIAN);
  }

  /**
   * テレポートするためのポータルを作ります
   *
   * @param player コマンドを実行したプレイヤー
   */
  private void generatePortal(Player player) {
    Location playerLocation = player.getLocation();
    double z = playerLocation.getZ() + 5;

    for (int i = 0; i < 4; i++) {

      double x = playerLocation.getX() + i;

      placeObsidian(new Location(player.getWorld(), x, playerLocation.getY(), z));
      placeObsidian(new Location(player.getWorld(), x, playerLocation.getY() + 4, z));

    }
    for (int i = 0; i < 5; i++) {

      double y = playerLocation.getY() + i;

      placeObsidian(new Location(player.getWorld(), playerLocation.getX(), y, z));
      placeObsidian(new Location(player.getWorld(), playerLocation.getX() + 3, y, z));

    }
  }
}

