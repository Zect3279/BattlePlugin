package com.zect.battleplugin;

import org.bukkit.plugin.java.JavaPlugin;

public class GameController extends JavaPlugin {

    static void start(Server server, Scoreboard MainBoard, Map<String, String> TeamName, Location Corner, Map<String, Location> TeamRes, Integer timeLimit) {
        /* 引数
        * - [x] 戦闘チーム
        * - [x] 観覧チーム
        * - [x] リス地
        * - [x] 範囲
        * - [x] 制限時間
        * - [x] スコアボードobj
        * - [x] サーバーobj
        */
        
        // 使うであろう変数を用意
//         Server server = sender.getServer();
//         Scoreboard MainBoard = server.getScoreboardManager().getMainScoreboard();
        Team Team1 = MainBoard.getTeam(TeamName.get("Team1"));
        Team Team2 = MainBoard.getTeam(TeamName.get("Team2"));
        Team Team3 = MainBoard.getTeam(TeamName.get("Team3"));
        Location Corner1 = Corner.get(0);
        Location Corner2 = Corner.get(1);
        Location Spawn1 = TeamRes.get("Team1");
        Location Spawn2 = TeamRes.get("Team2");
        
        // ゲーム開始機構
        
        /* titleでカウントダウン
        * 『ゲーム開始まで 5秒前』
        * 『3』
        * 『2』
        * 『1』
        * 『ゲーム開始』
        */
        
        /*
        * ボーダー処理・殺害処理・勝敗判定
        * の関数を発火
        */
    }

}
