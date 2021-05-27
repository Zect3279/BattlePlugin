package com.zect.battleplugin;

import dev.jorel.commandapi.*;
import dev.jorel.commandapi.arguments.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public final class BattlePlugin extends JavaPlugin {

    @Override
    public void onLoad() {
        CommandAPI.onLoad(true);

        new CommandAPICommand("battle")
                .withArguments(new StringArgument("start"))
                .executes((sender, args) -> {
                    // `/battle start`を実行したらゲームを開始する
                    sender.sendMessage("開始");
//                    GameStart(sender, args);
                })
                .withArguments(new StringArgument("check"))
                .executes((sender, args) -> {
                    // `/battle check`を実行したら設定一覧を表示する
                    sender.sendMessage("設定確認");
//                    CheckSettings(sender, args);
                })
                .withSubcommand(new CommandAPICommand("FightTeam")
                                .withSubcommand(new CommandAPICommand("add")
                                                .withArguments(new TeamArgument("team"))
                                                .executes((sender, args) -> {
                                                    // `/battle FightTeam add [team]`を実行したら参加チームに追加する
//                                    AddFighters(sender,args);
                                                })
                                )
                                .withSubcommand(new CommandAPICommand("remove")
                                                .withArguments(new TeamArgument("team"))
                                                .executes((sender, args) -> {
                                                    // `/battle FightTeam add [team]`を実行したら参加チームに追加する
//                                    AddFighters(sender,args);
                                                })
                                )
                )
                .withSubcommand(new CommandAPICommand("WatcherTeam")
                        .withSubcommand(new CommandAPICommand("add")
                                .withArguments(new TeamArgument("team"))
                                .executes((sender, args) -> {
                                    // 場所を保存してあげる
                                })
                        )
                        .withSubcommand(new CommandAPICommand("remove")
                                .withArguments(new TeamArgument("team"))
                                .executes((sender, args) -> {
                                    // 場所を保存してあげる
                                })
                        )
                )
                .withSubcommand(new CommandAPICommand("SetCorner")
                        .withArguments(new LocationArgument("location"))
                        .executes((sender, args) -> {
                            // 場所を保存してあげる
                        })
                )
                .withSubcommand(new CommandAPICommand("SetTimeLimit")
                        .withArguments(new IntegerArgument("second"))
                        .executes((sender, args) -> {
                            // 時間を保存してあげる
                        })
                )
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
    public void AddFighters(CommandSender sender, String[] args) {
        // 戦闘チーム追加
        // 既に２つあったら、エラー吐かせる
    }
    public void RemoveFighters(CommandSender sender, String[] args) {
        // 戦闘チーム撤去
        // 0個だったら、エラー吐かせる
    }
    public void AddWatcher(CommandSender sender, String[] args) {
        // 観覧チームを追加
        // 既にあったら、エラー吐く
    }
    public void RemoveWatcher(CommandSender sender, String[] args) {
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
