package plugin.oremining.command;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import plugin.oremining.PlayerUtils;

public class GameSetupCommand implements CommandExecutor {

  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command,
      @NonNull String label, @NonNull String @NonNull [] args) {
    if (sender instanceof Player player) {

      PlayerUtils.resetPlayerStatus(player);
      generatePortal(player);

    }
    return true;
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

