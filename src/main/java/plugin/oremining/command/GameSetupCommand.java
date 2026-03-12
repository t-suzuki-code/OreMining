package plugin.oremining.command;

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
      PlayerUtils.generatePortal(player);

    }
    return true;
  }


}

