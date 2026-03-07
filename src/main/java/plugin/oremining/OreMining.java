package plugin.oremining;

import org.bukkit.plugin.java.JavaPlugin;
import plugin.oremining.command.GameSetupCommand;

public final class OreMining extends JavaPlugin {

  @Override
  public void onEnable() {
    getCommand("gameSetup").setExecutor(new GameSetupCommand());
  }
}



