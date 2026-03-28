package plugin.oremining.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import plugin.oremining.GameStateManager;

public class GameSetupCommand extends BaseCommand {

  private final GameStateManager gameStateManager;

  public GameSetupCommand(GameStateManager gameStateManager) {
    this.gameStateManager = gameStateManager;
  }

  @Override
  public boolean onExecutePlayerCommand(Player player, Command command, String label,
      String[] args) {
    gameStateManager.onGameSetup(player);
    return true;
  }

  @Override
  public boolean onExecuteConsoleCommand(CommandSender sender, Command command, String label,
      String[] args) {
    return false;
  }
}

