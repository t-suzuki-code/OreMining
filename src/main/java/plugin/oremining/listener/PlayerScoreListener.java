package plugin.oremining.listener;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class PlayerScoreListener implements Listener {


  private int count;
  private Material oreType;

  @Getter
  private int playerScore;
  @Setter
  private World teleportWorld;


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
            player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "★ ボーナス！+30点 ★");
          }

          oreType = e.getBlock().getType();

          switch (oreType) {
            case DIAMOND_ORE -> {
              playerScore = playerScore + 30;
              player.sendMessage(ChatColor.AQUA + "[+30] ダイヤモンド鉱石を採掘！");
            }
            case GOLD_ORE -> {
              playerScore = playerScore + 20;
              player.sendMessage(ChatColor.GOLD + "[+20] 金鉱石を採掘！");
            }
            case IRON_ORE -> {
              playerScore = playerScore + 10;
              player.sendMessage(ChatColor.GRAY + "[+10] 鉄鉱石を採掘！");
            }
            default -> {
            }
          }
        }
      }
    }
  }

  /**
   * PlayerScoreListenerクラスのフィールド情報をリセットします。
   */
  public void resetPlayerScore() {
    playerScore = 0;
    count = 0;
    oreType = null;
  }
}

