package io.github.sas08.battleplugin;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
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
    // 音送信
    public static void sendSound(Collection<Player> players, Sound sound) {
        players.forEach(player -> {
            if (player != null) {
                player.playSound(player.getLocation(),sound,0.1f,1);
            }
        });
    }

}
