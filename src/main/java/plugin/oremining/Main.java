package plugin.oremining;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.oremining.command.GameSetupCommand;
import plugin.oremining.command.ViewScoreCommand;
import plugin.oremining.listener.GameInterruptListener;
import plugin.oremining.listener.PlayerScoreListener;
import plugin.oremining.listener.WorldTeleportListener;

public final class Main extends JavaPlugin {

  @Override
  public void onEnable() {

    PlayerScoreListener playerScoreListener = new PlayerScoreListener();
    Bukkit.getPluginManager().registerEvents(playerScoreListener, this);

    DBManager dbManager = new DBManager();

    GameStateManager gameStateManager = new GameStateManager(this, playerScoreListener, dbManager);
    playerScoreListener.setGameStateSupplier(gameStateManager::getState);

    GameSetupCommand gameSetupCommand = new GameSetupCommand(gameStateManager);
    getCommand("gameSetup").setExecutor(gameSetupCommand);
    getCommand("viewScore").setExecutor(new ViewScoreCommand());

    WorldTeleportListener worldTeleportListener = new WorldTeleportListener(gameStateManager);
    Bukkit.getPluginManager().registerEvents(worldTeleportListener, this);

    GameInterruptListener gameInterruptListener = new GameInterruptListener(gameStateManager);
    Bukkit.getPluginManager().registerEvents(gameInterruptListener, this);


  }

}



