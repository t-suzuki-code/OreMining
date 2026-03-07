package plugin.oremining.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.jspecify.annotations.NonNull;

public class GameSetupCommand implements CommandExecutor {

  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command,
      @NonNull String label, @NonNull String[] args) {
    if (sender instanceof Player player) {

      player.setHealth(20);
      player.setFoodLevel(20);
      removePotionEffect(player);

    }
    return false;
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
}

