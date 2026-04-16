package plugin.oremining.command;

import java.time.format.DateTimeFormatter;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import plugin.oremining.DBManager;
import plugin.oremining.mapper.data.PlayerScore;

public class ViewScoreCommand extends BaseCommand {

  private final DBManager dbManager;

  public ViewScoreCommand(DBManager dbManager) {
    this.dbManager = dbManager;
  }

  @Override
  public boolean onExecutePlayerCommand(Player player, Command command, String label,
      String[] args) {

    List<PlayerScore> playerScoreList = dbManager.selectList();
    sendScoreList(player, playerScoreList);

    return true;
  }

  @Override
  public boolean onExecuteConsoleCommand(CommandSender sender, Command command, String label,
      String[] args) {
    return false;
  }

  /**
   * スコア一覧をフォーマットしてプレイヤーに送信する。
   *
   * @param player          コマンドを実行したプレイヤー
   * @param playerScoreList DBから取得したプレイヤースコア情報を入れたリスト
   */
  private void sendScoreList(Player player, List<PlayerScore> playerScoreList) {
    for (int i = 0; i < playerScoreList.size(); i++) {
      PlayerScore playerScore = playerScoreList.get(i);
      player.sendMessage(
          (i + 1) + "位" + " | "
              + playerScore.getPlayerName() + " | "
              + playerScore.getScore() + " | "
              + playerScore.getRegisteredAt()
              .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
  }
}

