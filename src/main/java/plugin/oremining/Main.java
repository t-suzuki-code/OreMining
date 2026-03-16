package plugin.oremining;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.oremining.command.GameSetupCommand;
import plugin.oremining.listener.PlayerScoreListener;
import plugin.oremining.listener.WorldTeleportListener;

public final class Main extends JavaPlugin {

  @Override
  public void onEnable() {
    getCommand("gameSetup").setExecutor(new GameSetupCommand());

    PlayerScoreListener playerScoreListener = new PlayerScoreListener(this);
    Bukkit.getPluginManager().registerEvents(playerScoreListener, this);

    WorldTeleportListener worldTeleportListener = new WorldTeleportListener(this,
        playerScoreListener);
    Bukkit.getPluginManager().registerEvents(worldTeleportListener, this);

  }
}



