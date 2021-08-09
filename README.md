# BattlePlugin
**50人戦争支援プラグイン**  
Javaを勉強しながら作成しているため、遅い

## え？？
配信見てるけど、アレ、多様すぎるだろ…（絶句）  
プラグインにコマンドブロックでextライブラリみたいなのを組み込めるようにしたい

## TODO
- [ ] コマンド処理機構を別ファイルに移行
- [ ] 指定したコマンドブロックと同期

## usage
### 推奨コマンド
- /siege nav -> ゲーム開始までのナビゲーション（開始までの設定を手伝います）
  
### 全ゲーム共通設定
- /siege gamemode -> ゲームの種類を指定
- /siege Respawn <Team> <Location> -> 指定したチームのリスポーン地点を設定
  
### サバイバルのみ使用
- /siege Beacon <Team> <Location> -> 指定したチームのビーコン地点を設定
- /siege setTicket <Team> <Location> -> 指定したチームのチケット最大数を設定

### 大将戦のみ使用
- /siege setTimeLimit <Integer> -> ゲームの制限時間を設定

## モード
### サバイバル (survival)
- サバイバル戦争
- スタート時にチーム色の革アーマーが支給される(壊れない)
- チームのビーコンが破壊されるとそのチームはチケット制に切り替わる
- ビーコンのHPは上部のBossBarで表示
- チケット数・参加人数は下のActionBarで表示
- 時間制限無し
- チケットが0になることで勝敗が確定する

### 大将戦 (king)
- 大将戦
- スタート時にチーム色の革アーマーが支給される
- 大将は黄色の革アーマーが支給される
- 大将のHPはBossBarで表示される
- 時間制限有り
- 大将が殺されると勝敗が確定する

### シンプル (simple)
- 大将戦と同じ

## 使用ライブラリ
- CommandAPI Copyright (c) 2020 Jorel Ali https://github.com/JorelAli/CommandAPI/blob/master/LICENSE
