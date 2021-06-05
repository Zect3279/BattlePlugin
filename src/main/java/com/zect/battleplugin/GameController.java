package com.zect.battleplugin;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameController extends JavaPlugin implements Listener {

    public static boolean GamePlaying = false;



    public static void start(Server server, Scoreboard MainBoard, Map<String, String> TeamName, ArrayList<Location> Corner, Map<String, Location> TeamRes, Integer timeLimit) {
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
        Count(server);
        // ゲーム開始
//         Controll();
    }

    public static void Count(Server server) {
        /* titleでカウントダウン
         * 『ゲーム開始まで 5秒前』
         * 『3』
         * 『2』
         * 『1』
         * 『ゲーム開始』
         */
    }

    public void Controll() throws Exception {
        /*
         * ボーダー処理・殺害処理・勝敗判定
         * の関数を発火
         */
        GamePlaying = true;
//        Integer num = 5;
//        ExecutorService exec = Executors.newCachedThreadPool();
//        while (num > 0) {
//            exec.submit(new Bording());
//            exec.submit(new WinChecker());
//        }
    }
}

//public class Bording implements Runnable {
//
//    public void run() {
//        // ボーダー処理
//    }
//}
//public class WinChecker implements Runnable {
//
//    public void run() {
//        // 勝敗判定
//    }
//}


