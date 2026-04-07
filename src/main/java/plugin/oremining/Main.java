package plugin.oremining;

import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.oremining.command.GameSetupCommand;
import plugin.oremining.command.ViewScoreCommand;
import plugin.oremining.listener.GameInterruptListener;
import plugin.oremining.listener.GameScoreListener;
import plugin.oremining.listener.WorldTeleportListener;

public final class Main extends JavaPlugin {

  @Override
  public void onEnable() {

    GameScoreListener gameScoreListener = new GameScoreListener();
    Bukkit.getPluginManager().registerEvents(gameScoreListener, this);

    DBManager dbManager = new DBManager();

    GameStateManager gameStateManager = new GameStateManager(this, gameScoreListener, dbManager);
    gameScoreListener.setGameStateSupplier(gameStateManager::getState);

    Objects.requireNonNull(getCommand("gameSetup"))
        .setExecutor(new GameSetupCommand(gameStateManager));
    Objects.requireNonNull(getCommand("viewScore")).setExecutor(new ViewScoreCommand(dbManager));

    Bukkit.getPluginManager().registerEvents(new WorldTeleportListener(gameStateManager), this);
    Bukkit.getPluginManager().registerEvents(new GameInterruptListener(gameStateManager), this);


  }

}



