package plugin.oremining;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import plugin.oremining.listener.GameScoreListener;
import plugin.oremining.mapper.data.PlayerScore;

public class GameStateManager {

  private final Main main;
  private final GameScoreListener gameScoreListener;
  private final DBManager dbManager;

  private SchedulerManager schedulerManager;
  private WorldGeneration worldGeneration;
  private Player player;
  private Location gameStartLocation;
  private BukkitTask gameReadyTask;

  @Getter
  private GameState state = GameState.IDLE;


  private final List<Location> portalLocationList = new ArrayList<>();

  /**
   * GameStateManagerを生成します。
   *
   * @param main              タイマー登録用に使用するプラグインインスタンス
   * @param gameScoreListener スコアの取得・リセットを行うリスナー
   * @param dbManager         スコアのDB保存を行うマネージャー
   */
  public GameStateManager(Main main, GameScoreListener gameScoreListener, DBManager dbManager) {
    this.main = main;
    this.gameScoreListener = gameScoreListener;
    this.dbManager = dbManager;
  }

  /**
   * ゲーム開始の準備をします。
   *
   * @param player コマンドを実行したプレイヤー
   */
  public void onGameSetup(Player player) {
    if (state == GameState.IDLE) {
      state = GameState.READY;
      this.player = player;
      gameStartLocation = player.getLocation();

      PlayerUtils.resetPlayerStatus(player);
      generatePortal();

      player.sendMessage("ポータルに入って、ゲームを始めよう！");
      player.sendMessage("ポータルに入らなかった場合、30秒でポータルが削除されます");

      gameReadyTask = new BukkitRunnable() {
        @Override
        public void run() {
          onReadyTimeout();
        }
      }.runTaskLater(main, 20 * 30);

    } else {
      player.sendMessage(ChatColor.RED + "ゲーム中のため、コマンドの処理を実行できません。");
    }
  }

  /**
   * ゲーム準備状態が制限時間で終了した時の処理。
   */
  public void onReadyTimeout() {
    state = GameState.IDLE;
    removePortal();
    player.sendMessage(ChatColor.RED + "30秒経過したため、ポータルが削除されました。");
    resetToIdle();
  }

  /**
   * プレイヤーがポータルを通過した時の処理。
   *
   * @param e ポータルを通過した時のイベント情報。
   */
  public void onPlayerPortal(PlayerPortalEvent e) {
    if (state == GameState.READY) {
      gameReadyTask.cancel();
      state = GameState.PLAYING;
      e.setCancelled(true);

      worldGeneration = new WorldGeneration();
      World teleportWorld = worldGeneration.getWorld();

      MiningAreaBuilder miningAreaBuilder = new MiningAreaBuilder(teleportWorld);
      miningAreaBuilder.build();

      player.teleport(teleportWorld.getSpawnLocation());
      player.sendTitle(
          "§aGameStart！",
          "§a鉱石を採掘しよう!",
          10, 60, 10);

      removePortal();

      schedulerManager = new SchedulerManager(main, player, this::onGameTimeout,
          gameScoreListener::getGameScore);
      schedulerManager.startGame();
    }
  }

  /**
   * ゲーム終了時の処理。
   */
  public void onGameTimeout() {
    int finalScore = gameScoreListener.getGameScore();
    state = GameState.IDLE;

    player.teleport(gameStartLocation);
    player.sendTitle(
        "§6§lTimeUP!",
        "§6§lスコアは" + finalScore + "§6§l点！",
        10, 100, 10);
    PlayerUtils.resetPlayerStatus(player);
    worldGeneration.removeWorld();

    dbManager.insert(new PlayerScore(player.getName(), finalScore));
    gameScoreListener.resetGameScore();
    resetToIdle();
  }

  /**
   * プレイヤーが死亡した時の処理
   */
  public void onPlayerDeath() {
    switch (state) {
      case READY -> gameReadyTask.cancel();
      case PLAYING -> schedulerManager.cancelGameTask();
      case null, default -> {
      }
    }
  }

  /**
   * プレイヤーがゲーム中断後リスポーンした時の処理
   *
   * @param e イベント情報
   */
  public void onPlayerRespawn(PlayerRespawnEvent e) {
    switch (state) {
      case READY -> {
        e.setRespawnLocation(gameStartLocation);
        state = GameState.IDLE;
        removePortal();
        resetToIdle();
      }
      case PLAYING -> {
        e.setRespawnLocation(gameStartLocation);
        PlayerUtils.resetPlayerStatus(e.getPlayer());
        state = GameState.IDLE;
        worldGeneration.removeWorld();
        gameScoreListener.resetGameScore();
        resetToIdle();
      }
      case null, default -> {
      }
    }
  }

  /**
   * プレイヤーがログアウトした時のゲーム中断処理。
   */
  public void onPlayerQuit() {
    switch (state) {
      case READY -> gameReadyTask.cancel();

      case PLAYING -> {
        schedulerManager.cancelGameTask();
        worldGeneration.removeWorld();
      }
    }
  }

  /**
   * プレイヤーがゲームを中断しログインした時の処理。
   *
   * @param e ログインした時のイベント情報。
   */
  public void onPlayerJoin(PlayerJoinEvent e) {
    switch (state) {
      case READY -> {
        if (restorePlayerOnJoin(e)) {
          return;
        }
        removePortal();
      }
      case PLAYING -> {
        if (restorePlayerOnJoin(e)) {
          return;
        }
        gameScoreListener.resetGameScore();
      }
      case null, default -> {
      }
    }
  }

  /**
   * プレイヤーがログインした際の共通復帰処理を行います。 プレイヤーが死亡中の際は処理をスキップします。
   *
   * @param e イベント情報
   * @return 死亡中で処理をスキップした場合はtrue
   */
  private boolean restorePlayerOnJoin(PlayerJoinEvent e) {
    if (e.getPlayer().isDead()) {
      return true;
    }
    e.getPlayer().teleport(gameStartLocation);
    state = GameState.IDLE;
    PlayerUtils.resetPlayerStatus(e.getPlayer());
    resetToIdle();
    return false;
  }

  /**
   * 黒曜石を配置します。
   *
   * @param location 黒曜石を配置するロケーション。
   */
  private void placeObsidian(Location location) {
    location.getBlock().setType(Material.OBSIDIAN);
  }

  /**
   * テレポートするためのポータルを作ります
   *
   */
  private void generatePortal() {
    Location playerLocation = player.getLocation();
    double z = playerLocation.getZ() + 5;

    for (int i = 0; i < 4; i++) {

      double x = playerLocation.getX() + i;

      Location bottomLocation = new Location(player.getWorld(), x, playerLocation.getY(), z);
      Location topLocation = new Location(player.getWorld(), x, playerLocation.getY() + 4, z);

      placeObsidian(bottomLocation);
      placeObsidian(topLocation);
      portalLocationList.add(bottomLocation);
      portalLocationList.add(topLocation);

    }
    for (int i = 0; i < 5; i++) {

      double y = playerLocation.getY() + i;

      Location leftLocation = new Location(player.getWorld(), playerLocation.getX(), y, z);
      Location rightLocation = new Location(player.getWorld(), playerLocation.getX() + 3, y, z);

      placeObsidian(leftLocation);
      placeObsidian(rightLocation);
      portalLocationList.add(leftLocation);
      portalLocationList.add(rightLocation);

    }
  }

  /**
   * ポータルを削除します
   */
  private void removePortal() {
    List<Location> copy = new ArrayList<>(portalLocationList);
    portalLocationList.clear();

    for (Location removeLocation : copy) {
      removeLocation.getBlock().setType(Material.AIR);
    }
  }

  /**
   * 参照のクリアをします。
   */
  private void resetToIdle() {
    player = null;
    gameStartLocation = null;
    gameReadyTask = null;
    worldGeneration = null;
    schedulerManager = null;
  }
}
