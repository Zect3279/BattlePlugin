package com.zect.battleplugin;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;


public final class BattlePlugin extends JavaPlugin {

    private String ResTeam1;
    private String ResTeam2;

    @Override
    public void onLoad() {
        // プラグインが動いたときに実行
        new CommandAPICommand("battle")
        .withArguments(new StringArgument("start"))
        .executes((sender, args) -> {
            GameStart(sender,args);
        })
        .withArguments(new StringArgument("check"))
        .withSubcommand(new CommandAPICommand("FightTeam")
            .withSubCommand(new CommandAPICommand("add"))
                .withArguments(new TeamArgument("team"))
                .executes((sender, args) -> {
                    AddFighters(sender,args);
                })
            .withSubCommand(new CommandAPICommand("remove"))
                .withArguments(new TeamArgument("team"))
                .executes((sender, args) -> {
                    RemoveFighters(sender,args);
                })
        )
        .withSubcommand(new CommandAPICommand("WatcherTeam")
            .withSubCommand(new CommandAPICommand("add"))
                .withArguments(new TeamArgument("team"))
                .executes((sender, args) -> {
                    AddWatcher(sender,args);
                })
            .withSubCommand(new CommandAPICommand("remove"))
                .withArguments(new TeamArgument("team"))
                .executes((sender, args) -> {
                    RemoveWatcher(sender,args);
                })
        )
        .withSubcommand(new CommandAPICommand("SetSpawn")
            .withSubcommand(new TeamArgument("team"))
                .withArguments(new LocationArgument("location"))
                .executes((sender, args) -> {
                    // 場所を保存してあげる
                })
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

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void GameStart(CommandSender sender, String[] args) {
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
    public void CheckSettings(CommandSender sender, String[] args) {
        // 設定一覧を表示
        // チーム1のリス地へTP
        // チーム2のリス地へTP
        // ゲームスタートボタン
        BaseComponent[] check = new ComponentBuilder(
            new TextComponent(new ComponentBuilder()
                .append("[攻城戦支援プラグイン] ").color(ChatColor.LIGHT_PURPLE)
                .append("設定一覧を表示予定").color(ChatColor.GREEN)
                .create())
        ).create();
        BaseComponent[] TP1 = new ComponentBuilder(
                new TextComponent(new ComponentBuilder()
                        .append("[Team1リス地へTP] ").color(ChatColor.GOLD)
                        .create())
        )
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder()
                    .append("クリックでチーム1のリス地へTP")
                    .create()
                ))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ("/tp ") + ResTeam1))
                .create();
        BaseComponent[] TP2 = new ComponentBuilder(
                new TextComponent(new ComponentBuilder()
                        .append("[Team2リス地へTP] ").color(ChatColor.GOLD)
                        .create())
        )
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder()
                    .append("クリックでチーム2リス地へTP")
                    .create()
                ))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ("/tp ") + ResTeam2))
                .create();
        BaseComponent[] starting = new ComponentBuilder(
                new TextComponent(new ComponentBuilder()
                        .append("[クリックで対戦を開始] ").color(ChatColor.GOLD)
                        .create())
        )
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder()
                    .append("クリックで攻城戦を開始")
                    .create()
                ))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/battle start"))
                .create();
        sender.sendMessage(check);
        sender.sendMessage(TP1);
        sender.sendMessage(TP2);
        sender.sendMessage(starting);
    }
    
}
