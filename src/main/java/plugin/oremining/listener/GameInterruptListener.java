package plugin.oremining.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import plugin.oremining.Main;
import plugin.oremining.PlayerUtils;
import plugin.oremining.command.GameSetupCommand;

public class GameInterruptListener implements Listener {

  private final Main main;
  private final GameSetupCommand gameSetupCommand;

  public GameInterruptListener(Main main, GameSetupCommand gameSetupCommand) {
    this.main = main;
    this.gameSetupCommand = gameSetupCommand;
  }

  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent e) {

    if (gameSetupCommand.isGamePlay() && gameSetupCommand.isGameReady()) {

      gameSetupCommand.resetGameTask();

    } else if (gameSetupCommand.isGameReady()) {

      gameSetupCommand.resetGameReadyTask();

    }
  }

  @EventHandler
  public void onPlayerRespawn(PlayerRespawnEvent e) {
    if (gameSetupCommand.isGameReady() && gameSetupCommand.isGamePlay()) {
      e.setRespawnLocation(gameSetupCommand.getGameStartLocation());
      gameSetupCommand.endGame();

    } else if (gameSetupCommand.isGameReady()) {
      e.setRespawnLocation(gameSetupCommand.getGameStartLocation());
      gameSetupCommand.resetIsGameReady();
      PlayerUtils.removePortal();
    }
  }
}
