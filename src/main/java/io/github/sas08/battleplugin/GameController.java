package io.github.sas08.battleplugin;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
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
                                     Map<String, Location> Beacon,
                                     Integer ticketLimit,
                                     Integer BeaconLimit) {
        // 使うであろう変数を用意
        Team Team1 = MainBoard.getTeam(TeamName.get("Team1"));
        Team Team2 = MainBoard.getTeam(TeamName.get("Team2"));
        Team Team3 = MainBoard.getTeam(TeamName.get("Team3"));
        Location Spawn1 = TeamRes.get("Team1");
        Location Spawn2 = TeamRes.get("Team2");
        Location Beacon1 = Beacon.get("Team1");
        Location Beacon2 = Beacon.get("Team2");

        // ビーコンの設置
        // バリアブロックでの隔離
        World world = Beacon1.getWorld();
        Location loc = new Location(world, Beacon1.getX(), Beacon1.getX(), Beacon1.getZ());
        loc.getBlock().setType(Material.BEACON);


        // カウントダウン
        Count("敵のビーコンを破壊しろ！");

        // 服配布
        //

        // ゲーム開始
//         Controll();
    }

    public static void KingStart(Server server,
                                 Scoreboard MainBoard,
                                 Map<String, String> TeamName,
                                 Map<String, Location> TeamRes,
                                 Map<String, Player> King,
                                 Integer phase) {
        // 使うであろう変数を用意
        Team Team1 = MainBoard.getTeam(TeamName.get("Team1"));
        Team Team2 = MainBoard.getTeam(TeamName.get("Team2"));
        Team Team3 = MainBoard.getTeam(TeamName.get("Team3"));
        Location Spawn1 = TeamRes.get("Team1");
        Location Spawn2 = TeamRes.get("Team2");
        Player King1 = King.get("Team1");
        Player King2 = King.get("Team2");

        // ゲーム開始機構
        switch (phase) {
            case 1:
                KingCount("大将: " + Team1.getColor() + King1.getName()
                                + ChatColor.WHITE + "を守れ！",
                        "大将: " + Team1.getColor() + King1.getName()
                                + ChatColor.WHITE + "を殺せ！");
                break;
            case 2:
                KingCount("大将: " + Team2.getColor() + King2.getName()
                                + ChatColor.WHITE + "を殺せ！",
                        "大将: " + Team2.getColor() + King2.getName()
                                + ChatColor.WHITE + "を守れ！");
                break;
            default:
                break;

        }
        // ゲーム開始
//         Controll();
    }
    public static void SimpleStart(Server server,
                                   Scoreboard MainBoard,
                                   Map<String, String> TeamName,
                                   Map<String, Location> TeamRes,
                                   Map<String, Player> King,
                                   Integer timeLimit,
                                   Integer phase) {
        // 使うであろう変数を用意
        Team Team1 = MainBoard.getTeam(TeamName.get("Team1"));
        Team Team2 = MainBoard.getTeam(TeamName.get("Team2"));
        Team Team3 = MainBoard.getTeam(TeamName.get("Team3"));
        Location Spawn1 = TeamRes.get("Team1");
        Location Spawn2 = TeamRes.get("Team2");
        Player King1 = King.get("Team1");
        Player King2 = King.get("Team2");

        // ゲーム開始機構
        switch (phase) {
            case 1:
                KingCount("大将: " + Team1.getColor() + King1.getName()
                                + ChatColor.WHITE + "を守れ！",
                        "大将: " + Team1.getColor() + King1.getName()
                                + ChatColor.WHITE + "を殺せ！");
                break;
            case 2:
                KingCount("大将: " + Team2.getColor() + King2.getName()
                                + ChatColor.WHITE + "を殺せ！",
                        "大将: " + Team2.getColor() + King2.getName()
                                + ChatColor.WHITE + "を守れ！");
                break;
            default:
                break;
        }
        // ゲーム開始
//         Controll();
    }

    public static void KingCount(String redsub, String bluesub) {
        Collection<Player> players = (Collection<Player>) Bukkit.getOnlinePlayers();
        // titleでカウントダウン
        try {
            Util.setTeamTitle("開始まで 5秒前", redsub, bluesub, 100);
            Util.sendSound(players, Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTeamTitle("開始まで 4秒前", redsub, bluesub, 100);
            Util.sendSound(players, Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTeamTitle("開始まで 3秒前", redsub, bluesub, 100);
            Util.sendSound(players, Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTeamTitle("開始まで 2秒前", redsub, bluesub, 100);
            Util.sendSound(players, Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTeamTitle("開始まで 1秒前", redsub, bluesub, 100);
            Util.sendSound(players, Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTitle("ゲーム開始！", "50人マイクラ戦争", 30);
            Util.sendSound(players, Sound.BLOCK_ANVIL_PLACE);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void Count(String subtitle) {
        Collection<Player> players = (Collection<Player>) Bukkit.getOnlinePlayers();
        // titleでカウントダウン
        try {
            Util.setTitle("開始まで 5秒前", subtitle, 100);
            Util.sendSound(players, Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTitle("開始まで 4秒前", subtitle, 100);
            Util.sendSound(players, Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTitle("開始まで 3秒前", subtitle, 100);
            Util.sendSound(players, Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTitle("開始まで 2秒前", subtitle, 100);
            Util.sendSound(players, Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTitle("開始まで 1秒前", subtitle, 100);
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


