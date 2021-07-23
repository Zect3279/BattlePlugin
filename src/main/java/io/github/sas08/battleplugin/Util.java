package io.github.sas08.battleplugin;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

public class Util {

    // タイトルバー
    public static void setTitle(String title, String subTitle, Integer time) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendTitle(title, subTitle,0,time,0);
        });
    }
    public static void setTeamTitle(Map<String, TeamUtil> Teams,
                                    String title,
                                    String RedTitle, String BlueTitle,
                                    Integer time) {

        List<Player> Team1 = Teams.get("Team1").getMember();
        List<Player> Team2 = Teams.get("Team2").getMember();
        for (Player p : Team1) {
            p.sendTitle(title, RedTitle, 0, time, 0);
        }
        for (Player p : Team2) {
            p.sendTitle(title, BlueTitle, 0, time, 0);
        }
    }
    // アクションバー
    public static TimerTask setActionBar(String message) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendActionBar(message);
        });
        return null;
    }
    // 一斉送信
    public static void sendAll(String message) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendMessage(message);
        });
    }
    // 音送信
    public static void sendSound(Sound sound) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.playSound(player.getLocation(),sound,0.1f,1);
        });
    }
    // 一斉TP
    public static void TeamTeleport(Map<String, TeamUtil> Teams, Location Location1, Location Location2) {
        List<Player> Team1 = Teams.get("Team1").getMember();
        List<Player> Team2 = Teams.get("Team2").getMember();
        for (Player p : Team1) {
            p.teleport(Location1);
        }
        for (Player p : Team2) {
            p.teleport(Location2);
        }
    }

    public static void giveLeather(Map<String, TeamUtil> Teams) {
        ItemStack RedLeather = createLeather(Material.LEATHER_CHESTPLATE, Color.RED);
        ItemStack BlueLeather = createLeather(Material.LEATHER_CHESTPLATE, Color.BLUE);
        List<Player> Team1 = Teams.get("Team1").getMember();
        List<Player> Team2 = Teams.get("Team2").getMember();
        for (Player p : Team1) {
            p.getInventory().setChestplate(RedLeather);
        }
        for (Player p : Team2) {
            p.getInventory().setChestplate(BlueLeather);
        }
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

    public static Map<String,String> PlayerNumber(Map<String, TeamUtil> Teams) {
        Map<String, String> members = new HashMap<>();
        Integer team0 = Bukkit.getOnlinePlayers().size();
        Integer team1 = Teams.get("Team1").getMember().size();
        Integer team2 = Teams.get("Team2").getMember().size();

        members.put("All", String.valueOf(team0));
        members.put("Red", String.valueOf(team1));
        members.put("Blue", String.valueOf(team2));
        return members;
    }

    public static BaseComponent[] createBaseComponent(String MainMsg, String SubMsg, String Command, ChatColor Color) {
        BaseComponent[] Block = new ComponentBuilder(
                new TextComponent(new ComponentBuilder()
                        .append(MainMsg).color(Color)
                        .create())
        )
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder()
                        .append(SubMsg)
                        .create()
                ))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, Command))
                .create();
        return Block;
    }

    public static void sendMessage(Player player, String Message) {
        player.sendMessage(net.md_5.bungee.api.ChatColor.AQUA + "[攻城戦支援プラグイン]\n" + Message
        );
    }

    public static void sendBaseActionBar(Player player, BaseComponent[] message) {
        player.sendActionBar(message);
    }


}
