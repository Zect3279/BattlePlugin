package com.zect.battleplugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameController extends JavaPlugin implements Listener {

    public static boolean GamePlaying = false;



    public static void start(Server server, Scoreboard MainBoard, Map<String, String> TeamName, Map<String, Location> TeamRes, Integer timeLimit) {
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
//        Team Team1 = MainBoard.getTeam(TeamName.get("Team1"));
//        Team Team2 = MainBoard.getTeam(TeamName.get("Team2"));
//        Team Team3 = MainBoard.getTeam(TeamName.get("Team3"));
//        Location Corner1 = Corner.get(0);
//        Location Corner2 = Corner.get(1);
//        Location Spawn1 = TeamRes.get("Team1");
//        Location Spawn2 = TeamRes.get("Team2");

        // ゲーム開始機構
        Count(server);
        // ゲーム開始
//         Controll();
    }

    public static void Count(Server server) {
        Collection<Player> players = (Collection<Player>) Bukkit.getOnlinePlayers();
        // titleでカウントダウン
        try {
            Util.setTitle("開始まで 5秒前", "マイクラ戦争が始まるよ", 100);
            Util.sendSound(players, Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTitle("開始まで 4秒前", "マイクラ戦争が始まるよ", 100);
            Util.sendSound(players, Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTitle("開始まで 3秒前", "マイクラ戦争が始まるよ", 100);
            Util.sendSound(players, Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTitle("開始まで 2秒前", "マイクラ戦争が始まるよ", 100);
            Util.sendSound(players, Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTitle("開始まで 1秒前", "マイクラ戦争が始まるよ", 100);
            Util.sendSound(players, Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTitle("ゲーム開始！", "50人マイクラ戦争", 30);
            Util.sendSound(players, Sound.BLOCK_ANVIL_PLACE);


        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void Controll() throws Exception {
        /*
         * ボーダー処理・殺害処理・勝敗判定
         * の関数を発火
         */
        GamePlaying = true;
        ExecutorService exec = Executors.newCachedThreadPool();
        while (true) {
            exec.submit(new People());
        }
    }
}
class People implements Runnable {
    public void run() {
        // 下に表示するやつ
        // <__人が参加中 赤チーム:__ 青チーム:__>
    }
}

//public class WinChecker implements Runnable {
//
//    public void run() {
//        // 勝敗判定
//    }
//}


