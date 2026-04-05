package plugin.oremining.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public abstract class BaseCommand implements CommandExecutor {

  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command,
      @NonNull String label, String @NonNull [] args) {

    if (sender instanceof Player player) {
      return onExecutePlayerCommand(player, command, label, args);
    } else {
      return onExecuteConsoleCommand(sender, command, label, args);
    }
  }

  /**
   * コマンド実行者がプレイヤーだった場合に実行します。
   *
   * @param player  コマンドを実行したプレイヤー
   * @param command コマンド
   * @param label   ラベル
   * @param args    コマンド引数
   * @return 処理の実行有無
   */
  public abstract boolean onExecutePlayerCommand(Player player, Command command,
      String label, String[] args);

  /**
   * コマンド実行者がプレイヤー以外だった場合に実行します。
   *
   * @param sender  コマンドを実行者
   * @param command コマンド
   * @param label   ラベル
   * @param args    コマンド引数
   * @return 処理の実行有無
   */
  public abstract boolean onExecuteConsoleCommand(CommandSender sender, Command command,
      String label, String[] args);

}
