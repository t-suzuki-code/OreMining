# 鉱石採掘ゲーム：クラス設計

## 正常フロー

### ゲーム準備（GameSetupCommand）
- プレイヤーがゲームスタートコマンドを実行する
- システムがプレイヤーのインベントリに必要なアイテムをセット
- システムがプレイヤーの少し前にポータルを生成
- システムがプレイヤーの体力、空腹値、状態異常を初期化

### ワールド移動（WorldTeleportListener）
- プレイヤーがポータルに入る
- システムが専用のワールドを生成
- システムがプレイヤーを専用のワールドにテレポートさせる

### ゲーム中の処理（PlayerScoreListener）
- システムが制限時間をスタートしゲーム開始
- プレイヤーが鉱石を採掘
- 鉱石が採掘された時点でシステムがスコアを加算（種類別に点数を指定、連続で同じ種類などのボーナスあり）

### ゲーム終了処理（SchedulerManager）
- システムが制限時間でゲーム終了
- システムがプレイヤーを元のワールドに戻す
- システムが専用のワールドを削除
- システムがプレイヤーのスコアをDBに保存
- システムがプレイヤーの体力、空腹値、状態異常を初期化

### スコア確認（ViewScoreCommand）
- プレイヤーがリストコマンドを実行
- システムが過去の記録をチャット欄に表示（点数が高い順に最大10件）

## クラス一覧

| クラス名 | 種類 | 役割 |
|---|---|---|
| OreMining | メインクラス | プラグイン全体の起動、コマンド・リスナーの登録 |
| GameSetupCommand | コマンド | ゲーム準備（アイテムセット、ポータル生成、プレイヤー初期化） |
| ViewScoreCommand | コマンド | スコアリストの表示 |
| WorldTeleportListener | リスナー | ポータル通過の検知、ワールド生成・テレポートの実行 |
| PlayerScoreListener | リスナー | 鉱石採掘の検知、スコア加算 |
| SchedulerManager | スケジューラー | 制限時間の管理、ゲーム終了処理の実行 |
| DBManager | DB関連 | スコアのDB保存・取得 |
| WorldGeneration | ワールド生成 | 採掘ワールドの生成・削除 |
| OrePlacement | 鉱石配置 | ワールド内の鉱石ランダム配置 |

## クラス間の呼び出し関係

- **OreMining（メイン）** → GameSetupCommand, WorldTeleportListener, PlayerScoreListener
- **WorldTeleportListener** → SchedulerManager, WorldGeneration, OrePlacement
- **SchedulerManager** → DBManager, WorldGeneration
- **ViewScoreCommand** → DBManager
