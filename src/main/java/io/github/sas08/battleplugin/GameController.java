package io.github.sas08.battleplugin;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Collection;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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
        PlaceBeacon(Beacon1);
        PlaceBeacon(Beacon2);

        // バリアブロックでの隔離準備
        PlaceBarrier(Spawn1, "red");
        PlaceBarrier(Spawn2, "blue");

        // 赤チームテレポート先
        int yRed = Spawn1.getBlockY();
        Location RedWait = Spawn1;
        RedWait.setY(yRed+2);

        // 青チームテレポート先
        int yBlue = Spawn2.getBlockY();
        Location BlueWait = Spawn2;
        BlueWait.setY(yBlue+2);

        // 参加者全員隔離する
        Util.TeamTeleport(RedWait, BlueWait);

        // カウントダウン
        Count("敵のビーコンを破壊しろ！");

        // 服配布
        //
        Util.giveLeather();

        // ゲーム開始
        Control("survival");
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

        
        // バリアブロックでの隔離準備
        PlaceBarrier(Spawn1, "red");
        PlaceBarrier(Spawn2, "blue");
        
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
        
        ItemStack meat = new ItemStack(Material.COOKED_BEEF);
        ItemStack sword = new ItemStack(Material.STONE_SWORD);
        ItemStack bow = new ItemStack(Material.BOW);
        ItemStack arrow = new ItemStack(Material.ARROW);
        Util.giveLeather();
        Util.giveItem(meat, 64, 0);
        Util.giveItem(sword, 1, 1);
        Util.giveItem(bow, 1, 2);
        Util.giveItem(arrow, 64, 3);
        
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
        
        
        // バリアブロックでの隔離準備
        PlaceBarrier(Spawn1, "red");
        PlaceBarrier(Spawn2, "blue");
        
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
        ItemStack meat = new ItemStack(Material.COOKED_BEEF);
        ItemStack sword = new ItemStack(Material.STONE_SWORD);
        ItemStack bow = new ItemStack(Material.BOW);
        ItemStack arrow = new ItemStack(Material.ARROW);
        Util.giveLeather();
        Util.giveItem(meat, 64, 0);
        Util.giveItem(sword, 1, 1);
        Util.giveItem(bow, 1, 2);
        Util.giveItem(arrow, 64, 3);
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

    public static void PlaceBeacon(Location location) {
        int x = location.getBlockX();
        int y = location.getBlockY() - 1;
        int z = location.getBlockZ();

        World world = location.getWorld();

        for (int xPoint = x-1; xPoint <= x+1 ; xPoint++) {
            for (int zPoint = z-1 ; zPoint <= z+1; zPoint++) {
                world.getBlockAt(xPoint, y, zPoint).setType(Material.BEDROCK);
                world.getBlockAt(xPoint, y-1, zPoint).setType(Material.IRON_BLOCK);
                world.getBlockAt(xPoint, y-2, zPoint).setType(Material.BEDROCK);
            }
        }
        for (int xPoint = x-1; xPoint <= x+1; xPoint++) {
            world.getBlockAt(xPoint, y-1, z-2).setType(Material.BEDROCK);
            world.getBlockAt(xPoint, y-1, z+2).setType(Material.BEDROCK);
        }
        for (int zPoint = z-1; zPoint <= z+1; zPoint++) {
            world.getBlockAt(x-2, y-1, zPoint).setType(Material.BEDROCK);
            world.getBlockAt(x+2, y-1, zPoint).setType(Material.BEDROCK);
        }
        world.getBlockAt(x, y, z).setType(Material.BEACON);
    }

    public static void PlaceBarrier(Location location, String type) {

        World world = location.getWorld();

        // バリアブロック設置
        int x = location.getBlockX();
        int y = location.getBlockY() + 1;
        int z = location.getBlockZ();
        world.getBlockAt(x, y, z).setType(Material.BARRIER);
        for (int yPoint = y+1; yPoint <= y+3; yPoint++) {
            world.getBlockAt(x-1, yPoint, z).setType(Material.BARRIER);
            world.getBlockAt(x+1, yPoint, z).setType(Material.BARRIER);
            world.getBlockAt(x, yPoint, z+1).setType(Material.BARRIER);
            world.getBlockAt(x, yPoint, z-1).setType(Material.BARRIER);
        }

        // コンクリート設置
        int X = location.getBlockX();
        int Y = location.getBlockY() - 1;
        int Z = location.getBlockZ();
        world.getBlockAt(X-1, Y, Z).setType(Material.BEDROCK);
        world.getBlockAt(X+1, Y, Z).setType(Material.BEDROCK);
        world.getBlockAt(X, Y, Z+1).setType(Material.BEDROCK);
        world.getBlockAt(X, Y, Z-1).setType(Material.BEDROCK);
        world.getBlockAt(X, Y-1, Z).setType(Material.BEDROCK);

        switch (type) {
            case "red":
                world.getBlockAt(x, y+4, z).setType(Material.RED_CONCRETE);
                world.getBlockAt(X, Y, Z).setType(Material.RED_CONCRETE);
                break;
            case "blue":
                world.getBlockAt(x, y+4, z).setType(Material.BLUE_CONCRETE);
                world.getBlockAt(X, Y, Z).setType(Material.BLUE_CONCRETE);
                break;
            default:
                break;
        }

    }

    static Timer SctionTask = new Timer();

    public static void Control(String type) {
        /*
         * ボーダー処理・殺害処理・勝敗判定
         * の関数を発火
         */
        GamePlaying = true;

        switch (type) {
            case "survival":
                SctionTask.scheduleAtFixedRate(survivalActionBar,0,1000);
                break;
            case "king":
            case "simple":
                SctionTask.scheduleAtFixedRate(kingActionBar,0,1000);
                break;
            default:
                break;
        }
    }

    private static final TimerTask survivalActionBar = new TimerTask() {
        public void run() {
            // 定期的に実行したい処理
            Map<String, Integer> mems = Util.PlayerNumber();
            Integer all = mems.get("All");
            Integer red = mems.get("Red");
            Integer blue = mems.get("Bleu");
            Util.setActionBar("こんにちは");

            SctionTask.cancel();
//            Util.setActionBar("<" + all.toString() + "人が参加中 赤チーム:" + red.toString() + "人 青チーム:" + blue.toString() + "人>\n赤:枚 青:枚");
        }
    };
    private static final TimerTask kingActionBar = new TimerTask() {
        public void run() {
            // 定期的に実行したい処理
            Map<String, Integer> mems = Util.PlayerNumber();
            Integer all = mems.get("All");
            Integer red = mems.get("Red");
            Integer blue = mems.get("Bleu");
            Util.setActionBar("<" + all.toString() + "人が参加中 赤チーム:" + red.toString() + "人 青チーム:" + blue.toString() + "人>");
        }
    };
}

//public class WinChecker implements Runnable {
//
//    public void run() {
//        // 勝敗判定
//    }
//}


