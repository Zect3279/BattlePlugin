# BattlePlugin
**50人戦争支援プラグイン**  
Javaを勉強しながら作成しているため、遅い

## usage
- /siege start -> 開始

## コマンド詳細
### start
- ゲームを開始する

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

## 開始するためのコマンド
1. /siege nav

## 使用ライブラリ
- CommandAPI Copyright (c) 2020 Jorel Ali https://github.com/JorelAli/CommandAPI/blob/master/LICENSE
