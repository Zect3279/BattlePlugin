package io.github.sas08.battleplugin;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Collection;

public class Util {

    // タイトルバー
    public static void setTitle(String title, String subTitle, Integer time) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendTitle(title, subTitle,0,time,0);
        });
    }
    public static void setTeamTitle(String title, String RedTitle, String BlueTitle, Integer time) {
        Server server = Bukkit.getServer();
        Scoreboard score = server.getScoreboardManager().getMainScoreboard();
        Bukkit.getOnlinePlayers().forEach(player -> {
            Team team = score.getPlayerTeam(player);
            switch (team.getName()) {
                case "Red":
                    player.sendTitle(title, RedTitle, 0, time, 0);
                case "Blue":
                    player.sendTitle(title, BlueTitle, 0, time, 0);
                default:
                    break;
            }
        });
    }
    // アクションバー
    public static void setActionBar(String message) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendActionBar(message);
        });
    }
    // 一斉送信
    public static void sendAll(String message) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendMessage(message);
        });
    }
    // 音送信
    public static void sendSound(Collection<Player> players, Sound sound) {
        players.forEach(player -> {
            if (player != null) {
                player.playSound(player.getLocation(),sound,0.1f,1);
            }
        });
    }
    // 一斉TP
    public static void TeamTeleport(Location redLocation, Location blueLocation) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            Server server = Bukkit.getServer();
            Scoreboard score = server.getScoreboardManager().getMainScoreboard();
            Team team = score.getPlayerTeam(player);
            switch (team.getName()) {
                case "Red":
                    player.teleport(redLocation);
                    break;
                case "Blue":
                    player.teleport(blueLocation);
                    break;
                default:
                    break;
            }
        });
    }

    public static void giveLeather() {
        ItemStack RedLeather = createLeather(Material.LEATHER_CHESTPLATE, Color.RED);
        ItemStack BlueLeather = createLeather(Material.LEATHER_CHESTPLATE, Color.BLUE);
        Server server = Bukkit.getServer();
        Scoreboard score = server.getScoreboardManager().getMainScoreboard();
        Bukkit.getOnlinePlayers().forEach(player -> {
            Team team = score.getPlayerTeam(player);
            switch (team.getName()) {
                case "Red":
                    player.getInventory().setChestplate(RedLeather);
                    break;
                case "Blue":
                    player.getInventory().setChestplate(BlueLeather);
                    break;
                default:
                    break;
            }
        });
    }

    public static void giveItem(ItemStack item, Integer many, Integer index) {
        item.setAmount(many);
        Bukkit.getOnlinePlayers().forEach(player ->  {
            player.getInventory().setItem(index, item);
        });

    }

    public static ItemStack createLeather(Material leatherPiece, Color color) {
        ItemStack item = new ItemStack(leatherPiece);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(Color.fromRGB(color.asRGB()));
        meta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
        item.setItemMeta(meta);
        return item;
    }


}
