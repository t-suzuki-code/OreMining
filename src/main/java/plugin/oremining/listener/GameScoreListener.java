package plugin.oremining.listener;

import java.util.function.Supplier;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import plugin.oremining.GameState;

/**
 * 鉱石採掘を検知し、スコアの加算・ボーナス判定を行うリスナーです。
 */
public class GameScoreListener implements Listener {

  private int comboCount;
  private Material oreType;
  private static final int BONUS_REQUIRED_COMBO = 3;
  @Getter
  private int gameScore;
  @Setter
  private Supplier<GameState> gameStateSupplier;


  @EventHandler
  public void onBlockBreak(BlockBreakEvent e) {

    Player player = e.getPlayer();

    if (gameStateSupplier.get() == GameState.PLAYING) {

      switch (e.getBlock().getType()) {
        case DIAMOND_ORE, GOLD_ORE, IRON_ORE -> {
          updateComboCount(e);
          addBonusScore(player);
          oreType = e.getBlock().getType();
          addScore(player);
        }
      }
    }
  }

  /**
   * 同じ種類の鉱石の連続採掘回数をカウントします。
   *
   * @param e イベント情報。
   */
  private void updateComboCount(BlockBreakEvent e) {
    if (oreType == e.getBlock().getType()) {
      comboCount++;
    } else {
      comboCount = 1;
    }
  }

  /**
   * 同じ種類の鉱石が連続で規定回数採掘されたときボーナス点の処理を行います。
   *
   * @param player ゲーム中のプレイヤー
   */
  private void addBonusScore(Player player) {
    if (comboCount == BONUS_REQUIRED_COMBO) {
      gameScore += 30;
      comboCount = 0;
      player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "★ ボーナス！+30点 ★");
    }
  }

  /**
   * 採掘した鉱石の種類によってスコア加算を行います。
   *
   * @param player ゲーム中のプレイヤー
   */
  private void addScore(Player player) {
    switch (oreType) {
      case DIAMOND_ORE -> {
        gameScore += 30;
        player.sendMessage(ChatColor.AQUA + "[+30] ダイヤモンド鉱石を採掘！");
      }
      case GOLD_ORE -> {
        gameScore += 20;
        player.sendMessage(ChatColor.GOLD + "[+20] 金鉱石を採掘！");
      }
      case IRON_ORE -> {
        gameScore += 10;
        player.sendMessage(ChatColor.GRAY + "[+10] 鉄鉱石を採掘！");
      }
      default -> {
      }
    }
  }

  /**
   * GameScoreListenerクラスのフィールド情報をリセットします。
   */
  public void resetGameScore() {
    gameScore = 0;
    comboCount = 0;
    oreType = null;
  }
}

