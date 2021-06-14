package com.zect.battleplugin;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameController extends JavaPlugin implements Listener {

    public static boolean GamePlaying = false;

    public static void SurvivalStart(Server server,
                             Scoreboard MainBoard,
                             Map<String, String> TeamName,
                             Map<String, Location> TeamRes,
                             Map<String, Location> Beacon) {
        // 使うであろう変数を用意
        Team Team1 = MainBoard.getTeam(TeamName.get("Team1"));
        Team Team2 = MainBoard.getTeam(TeamName.get("Team2"));
        Team Team3 = MainBoard.getTeam(TeamName.get("Team3"));
        Location Spawn1 = TeamRes.get("Team1");
        Location Spawn2 = TeamRes.get("Team2");
        Location Beacon1 = Beacon.get("Team1");
        Location Beacon2 = Beacon.get("Team2");

        // ゲーム開始機構
        Count("サバイバル戦争");
        // ゲーム開始
//         Controll();
    }

    public static void KingStart(Server server,
                                 Scoreboard MainBoard,
                                 Map<String, String> TeamName,
                                 Map<String, Location> TeamRes,
                                 Map<String, Player> King) {
        // 使うであろう変数を用意
        Team Team1 = MainBoard.getTeam(TeamName.get("Team1"));
        Team Team2 = MainBoard.getTeam(TeamName.get("Team2"));
        Team Team3 = MainBoard.getTeam(TeamName.get("Team3"));
        Location Spawn1 = TeamRes.get("Team1");
        Location Spawn2 = TeamRes.get("Team2");
        Player King1 = King.get("Team1");
        Player King2 = King.get("Team2");

        // ゲーム開始機構
        Count("赤チーム大将:" + Team1.getColor() + King1.getName()
                + ChatColor.WHITE + "\n青チーム大将:" + Team2.getColor() + King2.getName()
                + ChatColor.WHITE + "\n大将戦");
        // ゲーム開始
//         Controll();
    }

    public static void Count(String type) {
        Collection<Player> players = (Collection<Player>) Bukkit.getOnlinePlayers();
        // titleでカウントダウン
        try {
            Util.setTitle("開始まで 5秒前", type + "が始まるよ", 100);
            Util.sendSound(players, Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTitle("開始まで 4秒前", type + "が始まるよ", 100);
            Util.sendSound(players, Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTitle("開始まで 3秒前", type + "が始まるよ", 100);
            Util.sendSound(players, Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTitle("開始まで 2秒前", type + "が始まるよ", 100);
            Util.sendSound(players, Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTitle("開始まで 1秒前", type + "が始まるよ", 100);
            Util.sendSound(players, Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTitle("ゲーム開始！", type, 30);
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
        // 大将戦：<__人が参加中 赤チーム:__ 青チーム:__>
        // サバイバル：<__人が参加中 赤チーム:__/<チケット数> 青チーム:__/<チケット数>>
    }
}

//public class WinChecker implements Runnable {
//
//    public void run() {
//        // 勝敗判定
//    }
//}


