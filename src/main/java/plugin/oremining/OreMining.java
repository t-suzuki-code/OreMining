package plugin.oremining;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.oremining.command.GameSetupCommand;
import plugin.oremining.listener.WorldTeleportListener;

public final class OreMining extends JavaPlugin {

  @Override
  public void onEnable() {
    getCommand("gameSetup").setExecutor(new GameSetupCommand());
    WorldTeleportListener worldTeleportListener = new WorldTeleportListener();
    Bukkit.getPluginManager().registerEvents(worldTeleportListener, this);
  }
}



