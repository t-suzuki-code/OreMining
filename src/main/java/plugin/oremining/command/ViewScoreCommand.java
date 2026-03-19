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

    DBManager dbManager = new DBManager();
    List<PlayerScore> playerScoreList = dbManager.selectList();

    for (PlayerScore playerScore : playerScoreList) {
      player.sendMessage(
          playerScore.getId() + " | "
              + playerScore.getPlayerName() + " | "
              + playerScore.getScore() + " | "
              + playerScore.getRegisteredAt()
              .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    return true;
  }

  @Override
  public boolean onExecuteConsoleCommand(CommandSender sender, Command command, String label,
      String[] args) {
    return false;
  }
}

