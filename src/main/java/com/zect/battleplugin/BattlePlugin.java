package com.zect.battleplugin;

import dev.jorel.commandapi.*;
import dev.jorel.commandapi.arguments.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public final class BattlePlugin extends JavaPlugin {

    @Override
    public void onLoad() {
        CommandAPI.onLoad(true);

        String[] subCommandList = new String[] {"start", "check", "FightTeam", "WatcherTeam", "SetCorner", "SetTimeLimit"};

        CommandAPICommand start = new CommandAPICommand("start")
                .executes((sender, args) -> {
                    //perm group remove code
                    sender.sendMessage("開始");
                    GameStart(sender, args);
                });

        CommandAPICommand check = new CommandAPICommand("check")
                .executes((sender, args) -> {
                    //perm group remove code
                    sender.sendMessage("設定確認");
                    CheckSettings(sender, args);
                });

        CommandAPICommand teamAdd = new CommandAPICommand("add")
                .withArguments(new StringArgument("groupName"))
                .executes((sender, args) -> {
                    //perm group add code
//                    case args[2]
                    sender.sendMessage("チームを追加");
                    AddFighters(sender,args);
                });

        CommandAPICommand teamRemove = new CommandAPICommand("remove")
                .withArguments(new StringArgument("groupName"))
                .executes((sender, args) -> {
                    //perm group remove code
                    sender.sendMessage("チームを削除");
                });

        CommandAPICommand fight = new CommandAPICommand("FightTeam")
                .withSubcommand(teamAdd)
                .withSubcommand(teamRemove);

        CommandAPICommand watch = new CommandAPICommand("WatchTeam")
                .withSubcommand(teamAdd)
                .withSubcommand(teamRemove);

        CommandAPICommand corner = new CommandAPICommand("SetCorner")
                .withArguments(new LocationArgument("location"))
                .executes((sender, args) -> {
                    //perm group remove code
                    sender.sendMessage("座標を登録");
                });

        CommandAPICommand limit = new CommandAPICommand("SetTimeLimit")
                .withArguments(new IntegerArgument("second"))
                .executes((sender, args) -> {
                    //perm group remove code
                    sender.sendMessage("時間制限を追加");
                });

        new CommandAPICommand("siege")
                .withSubcommand(start)
                .withSubcommand(check)
                .withSubcommand(fight)
                .withSubcommand(watch)
                .withSubcommand(corner)
                .withSubcommand(limit)
                .register();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        CommandAPI.onEnable(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private Location ResTeam1;
    private Location ResTeam2;

//    public String CheckCanPlay() {
//        // ゲーム開始可能か確認する
//        // 開始できなかったら、reasonを返す
//
//        // ゲームが開始できるなら、nullを返す
//    }

    public void GameStart(CommandSender sender, Object[] args) {
        // ゲーム開始できるか判定する
        String checking = "ああ";
//        String checking = CheckCanPlay();
        if (checking != null) {
            // ゲームが開始できない
            // 設定一覧を表示
            BaseComponent[] check = SettingList();
            sender.sendMessage("[攻城戦支援プラグイン]\n" + checking + "\nが設定できていません\n" + check);

        }
        // ゲーム開始
        GameController.start();
    }
    public void AddFighters(CommandSender sender, Object[] args) {
        // 戦闘チーム追加
        // 既に２つあったら、エラー吐かせる
    }
    public void RemoveFighters(CommandSender sender, Object[] args) {
        // 戦闘チーム撤去
        // 0個だったら、エラー吐かせる
    }
    public void AddWatcher(CommandSender sender, Object[] args) {
        // 観覧チームを追加
        // 既にあったら、エラー吐く
    }
    public void RemoveWatcher(CommandSender sender, Object[] args) {
        // 観覧チームを削除
        // 0個だったら、エラー吐く
    }
    public BaseComponent[] SettingList() {
        BaseComponent[] settings = new ComponentBuilder(
                new TextComponent(new ComponentBuilder()
                    .append("[攻城戦支援プラグイン]\n").color(ChatColor.LIGHT_PURPLE)
                    .append("設定一覧を表示予定").color(ChatColor.GREEN)
                    .create())
            ).create();
        return settings;
    }
    public void CheckSettings(CommandSender sender, Object[] args) {
        // 設定一覧を表示
        // チーム1のリス地へTP
        // チーム2のリス地へTP
        // ゲームスタートボタン
        BaseComponent[] check = SettingList();
        if (ResTeam1 == null) {
//        if (ResTeam1 != null) {
            BaseComponent[] TP1 = new ComponentBuilder(
                new TextComponent(new ComponentBuilder()
                        .append("[Team1リス地へTP]").color(ChatColor.GOLD)
                        .create())
            )
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder()
                    .append("クリックでチーム1のリス地へTP")
                    .create()
                ))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ("/tp ") + ResTeam1))
                .create();
        }
        if (ResTeam2 == null) {
//        if (ResTeam2 != null) {
            BaseComponent[] TP2 = new ComponentBuilder(
                    new TextComponent(new ComponentBuilder()
                            .append("[Team2リス地へTP]").color(ChatColor.GOLD)
                            .create())
            )
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder()
                        .append("クリックでチーム2リス地へTP")
                        .create()
                    ))
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ("/tp ") + ResTeam2))
                    .create();
        }
        String checking = "ああ";
//        String checking = CheckCanPlay();
        if (checking == null) {
            BaseComponent[] starting = new ComponentBuilder(
                    new TextComponent(new ComponentBuilder()
                            .append("[クリックで対戦を開始]").color(ChatColor.GOLD)
                            .create())
            )
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder()
                        .append("クリックで攻城戦を開始")
                        .create()
                    ))
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/battle start"))
                    .create();
        }

//        if (TP1 != null && TP2 != null && starting != null) {
//            sender.sendMessage(check + "\n" + TP1 + "\n" + TP2 + "\n" + starting);
//        }else if (TP1) {
//            sender.sendMessage(check + "\n" + TP1);
//        }else if (TP2) {
//            sender.sendMessage(check + "\n" + TP2);
//        }
    }

}
