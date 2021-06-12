package com.zect.battleplugin;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Collection;

public class Util {

    // タイトルバー
    public static void setTitle(String title, String subTitle, Integer time) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendTitle(title, subTitle,0,time,0);
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
