package plugin.oremining.command;

import java.time.format.DateTimeFormatter;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import plugin.oremining.DBManager;
import plugin.oremining.mapper.data.PlayerScore;

public class ViewScoreCommand implements CommandExecutor {

  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command,
      @NonNull String label, @NonNull String @NonNull [] args) {
    if (sender instanceof Player player) {
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
    }
    return true;
  }
}
