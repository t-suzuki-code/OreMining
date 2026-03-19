package plugin.oremining.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import plugin.oremining.PlayerUtils;

public class GameSetupCommand extends BaseCommand {

  @Override
  public boolean onExecutePlayerCommand(Player player, Command command, String label,
      String[] args) {
    PlayerUtils.resetPlayerStatus(player);
    PlayerUtils.generatePortal(player);
    return true;
  }

  @Override
  public boolean onExecuteConsoleCommand(CommandSender sender, Command command, String label,
      String[] args) {
    return false;
  }


}

