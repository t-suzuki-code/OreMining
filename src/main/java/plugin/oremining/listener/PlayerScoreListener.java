package plugin.oremining.listener;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import plugin.oremining.Main;

public class PlayerScoreListener implements Listener {

  private final Main main;
  private int count;
  private int playerScore;
  private Material oreType;
  private World teleportWorld;


  public PlayerScoreListener(Main main) {
    this.main = main;
  }

  public void setTeleportWorld(World teleportWorld) {
    this.teleportWorld = teleportWorld;
  }

  public int getPlayerScore() {
    return playerScore;
  }

  public void reset() {
    count = 0;
    playerScore = 0;
    oreType = null;
  }

  @EventHandler
  public void onBlockBreak(BlockBreakEvent e) {

    Player player = e.getPlayer();

    if (e.getPlayer().getWorld() == teleportWorld) {

      switch (e.getBlock().getType()) {
        case DIAMOND_ORE, GOLD_ORE, IRON_ORE -> {

          if (oreType == e.getBlock().getType()) {
            count = count + 1;
          } else {
            count = 1;
          }

          if (count == 3) {
            playerScore = playerScore + 30;
            count = 0;
          }

          oreType = e.getBlock().getType();

          switch (oreType) {
            case DIAMOND_ORE -> playerScore = playerScore + 30;
            case GOLD_ORE -> playerScore = playerScore + 20;
            case IRON_ORE -> playerScore = playerScore + 10;
            default -> {
              return;
            }
          }

          player.sendMessage("現在のスコアは" + playerScore + "点です！");

        }
      }
    }
  }
}

