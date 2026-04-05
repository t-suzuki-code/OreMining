package plugin.oremining.command;

import java.time.format.DateTimeFormatter;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import plugin.oremining.DBManager;
import plugin.oremining.mapper.data.PlayerScore;

public class ViewScoreCommand extends BaseCommand {


  @Override
  public boolean onExecutePlayerCommand(Player player, Command command, String label,
      String[] args) {

    //キャッシュが古いデータを返す問題を回避するため、インスタンスを生成。
    DBManager dbManager = new DBManager();

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
    for (PlayerScore playerScore : playerScoreList) {
      player.sendMessage(
          playerScore.getId() + " | "
              + playerScore.getPlayerName() + " | "
              + playerScore.getScore() + " | "
              + playerScore.getRegisteredAt()
              .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
  }
}

