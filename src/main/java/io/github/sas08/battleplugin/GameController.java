package io.github.sas08.battleplugin;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

public class GameController extends JavaPlugin implements Listener {

    public static boolean GamePlaying = false;

    private static Map<String, TeamUtil> Teams;

    private static SystemUtil system;

    public static void SurvivalStart(SystemUtil Systems,
                                     Map<String, TeamUtil> teams,
                                     Server server,
                                     Scoreboard MainBoard) {
        // 使うであろう変数を用意
        Teams = teams;
        system = Systems;
        TeamUtil team1 = Teams.get("Team1");
        TeamUtil team2 = Teams.get("Team2");
        TeamUtil team3 = Teams.get("Team3");
        Location Spawn1 = team1.getRespawn();
        Location Spawn2 = team2.getRespawn();
        Location Beacon1 = team1.getBeacon();
        Location Beacon2 = team2.getBeacon();
        

        // ビーコンの設置
        PlaceBeacon(Beacon1);
        PlaceBeacon(Beacon2);
        Bukkit.getLogger().info("ビーコン設置");

        // バリアブロックでの隔離準備
        PlaceBarrier(Spawn1, "red");
        PlaceBarrier(Spawn2, "blue");
        Bukkit.getLogger().info("バリア設置");

        // カウントダウン
        Count("敵のビーコンを破壊しろ！");
        Bukkit.getLogger().info("カウントダウン");

        // ゲームモードとかを設定
        Util.TeamTeleport(Teams, Spawn1, Spawn2);
        GameSet();
        Util.giveLeather(Teams);
        Bukkit.getLogger().info("アイテム・ゲームモード・TP");

        // ゲーム開始
        Control("survival");
        Bukkit.getLogger().info("アクションバー");
    }

    public static void KingStart(SystemUtil Systems,
                                 Map<String, TeamUtil> teams,
                                 Server server,
                                 Scoreboard MainBoard,
                                 Integer phase) {
        // 使うであろう変数を用意
        Teams = teams;
        system = Systems;
        TeamUtil team1 = Teams.get("Team1");
        TeamUtil team2 = Teams.get("Team2");
        TeamUtil team3 = Teams.get("Team3");
        Location Spawn1 = team1.getRespawn();
        Location Spawn2 = team2.getRespawn();
        Player King1 = team1.getKing();
        Player King2 = team2.getKing();


        // バリアブロックでの隔離準備
        PlaceBarrier(Spawn1, "red");
        PlaceBarrier(Spawn2, "blue");
        
        // ゲーム開始機構
        switch (phase) {
            case 1:
                KingCount("大将: " + team1.getColor() + King1.getName()
                                + ChatColor.WHITE + "を守れ！",
                        "大将: " + team1.getColor() + King1.getName()
                                + ChatColor.WHITE + "を殺せ！");
                break;
            case 2:
                KingCount("大将: " + team2.getColor() + King2.getName()
                                + ChatColor.WHITE + "を殺せ！",
                        "大将: " + team2.getColor() + King2.getName()
                                + ChatColor.WHITE + "を守れ！");
                break;
            default:
                break;
        }
        
        ItemStack meat = new ItemStack(Material.COOKED_BEEF);
        ItemStack sword = new ItemStack(Material.STONE_SWORD);
        ItemStack bow = new ItemStack(Material.BOW);
        ItemStack arrow = new ItemStack(Material.ARROW);
        Util.giveLeather(Teams);
        Util.giveItem(meat, 64, 0);
        Util.giveItem(sword, 1, 1);
        Util.giveItem(bow, 1, 2);
        Util.giveItem(arrow, 64, 3);
        
        // ゲーム開始
//         Controll();
    }

    public static void SimpleStart(SystemUtil Systems,
                                   Map<String, TeamUtil> teams,
                                   Server server,
                                   Scoreboard MainBoard,
                                   Integer phase) {
        // 使うであろう変数を用意
        Teams = teams;
        system = Systems;
        TeamUtil team1 = Teams.get("Team1");
        TeamUtil team2 = Teams.get("Team2");
        TeamUtil team3 = Teams.get("Team3");
        Location Spawn1 = team1.getRespawn();
        Location Spawn2 = team2.getRespawn();
        Player King1 = team1.getKing();
        Player King2 = team2.getKing();
        
        
        // バリアブロックでの隔離準備
        PlaceBarrier(Spawn1, "red");
        PlaceBarrier(Spawn2, "blue");
        
        // ゲーム開始機構
        switch (phase) {
            case 1:
                KingCount("大将: " + team1.getColor() + King1.getName()
                                + ChatColor.WHITE + "を守れ！",
                        "大将: " + team1.getColor() + King1.getName()
                                + ChatColor.WHITE + "を殺せ！");
                break;
            case 2:
                KingCount("大将: " + team2.getColor() + King2.getName()
                                + ChatColor.WHITE + "を殺せ！",
                        "大将: " + team2.getColor() + King2.getName()
                                + ChatColor.WHITE + "を守れ！");
                break;
            default:
                break;
        }
        ItemStack meat = new ItemStack(Material.COOKED_BEEF);
        ItemStack sword = new ItemStack(Material.STONE_SWORD);
        ItemStack bow = new ItemStack(Material.BOW);
        ItemStack arrow = new ItemStack(Material.ARROW);
        Util.giveLeather(Teams);
        Util.giveItem(meat, 64, 0);
        Util.giveItem(sword, 1, 1);
        Util.giveItem(bow, 1, 2);
        Util.giveItem(arrow, 64, 3);
        // ゲーム開始
//         Controll();
    }
    
    public static void GameSet() {
        // spec
        // HP full
        Server server = Bukkit.getServer();
        Scoreboard score = server.getScoreboardManager().getMainScoreboard();
        Collection<? extends Player> players = server.getOnlinePlayers();
        for (Player player : players) {
            if (score.getPlayerTeam(player).getName() == "Co") {
                player.setGameMode(GameMode.SPECTATOR);
            } else {
                player.setGameMode(GameMode.SURVIVAL);
                player.setHealth(player.getMaxHealth());
            }
        }
    }
    
    public static void KingCount(String redsub, String bluesub) {
        Collection<Player> players = (Collection<Player>) Bukkit.getOnlinePlayers();
        // titleでカウントダウン
        try {
            Util.setTeamTitle(Teams, "開始まで 5秒前", redsub, bluesub, 100);
            Util.sendSound(Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTeamTitle(Teams, "開始まで 4秒前", redsub, bluesub, 100);
            Util.sendSound(Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTeamTitle(Teams, "開始まで 3秒前", redsub, bluesub, 100);
            Util.sendSound(Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTeamTitle(Teams, "開始まで 2秒前", redsub, bluesub, 100);
            Util.sendSound(Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTeamTitle(Teams, "開始まで 1秒前", redsub, bluesub, 100);
            Util.sendSound(Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTitle("ゲーム開始！", "50人マイクラ戦争", 30);
            Util.sendSound(Sound.BLOCK_ANVIL_PLACE);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void Count(String subtitle) {
        Collection<Player> players = (Collection<Player>) Bukkit.getOnlinePlayers();
        // titleでカウントダウン
        try {
            Util.setTitle("開始まで 5秒前", subtitle, 100);
            Util.sendSound(Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTitle("開始まで 4秒前", subtitle, 100);
            Util.sendSound(Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTitle("開始まで 3秒前", subtitle, 100);
            Util.sendSound(Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTitle("開始まで 2秒前", subtitle, 100);
            Util.sendSound(Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTitle("開始まで 1秒前", subtitle, 100);
            Util.sendSound(Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTitle("ゲーム開始！", "50人マイクラ戦争", 30);
            Util.sendSound(Sound.BLOCK_ANVIL_PLACE);

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
                world.getBlockAt(X, Y, Z).setType(Material.RED_CONCRETE);
                break;
            case "blue":
                world.getBlockAt(X, Y, Z).setType(Material.BLUE_CONCRETE);
                break;
            default:
                break;
        }

    }


    static Timer ActionTask = new Timer();

    public static void Control(String type) {
        /*
         * ボーダー処理・殺害処理・勝敗判定
         * の関数を発火
         */
        GamePlaying = true;
        Map<String, String> mems = Util.PlayerNumber(Teams);
        String all = mems.get("All");
        String red = mems.get("Red");
        String blue = mems.get("Bleu");

        if (all == null) {
            all = "0";
        } else if (red == null) {
            red = "0";
        } else if (blue == null) {
            blue = "0";
        }

        switch (type) {
            case "survival":
                Util.setActionBar("< " + all + "人が参加中 " +
                                  "赤チーム:" + red + "人 " +
                                  "青チーム:" + blue + "人>\n" +
                                  "赤:" + Teams.get("Team1").getTicket() + "枚 " +
                                  "青:" + Teams.get("Team2").getTicket() + "枚 >");
                break;
            case "king":
            case "simple":
                Util.setActionBar("< " + all + "人が参加中 " +
                                  "赤チーム:" + red + "人 " +
                                  "青チーム:" + blue + "人 >");
                break;
            default:
                break;
        }
        ActionTask.cancel();
        Bukkit.getLogger().info("アクションバーキャンセル完了");
    }

}

//public class WinChecker implements Runnable {
//
//    public void run() {
//        // 勝敗判定
//    }
//}


