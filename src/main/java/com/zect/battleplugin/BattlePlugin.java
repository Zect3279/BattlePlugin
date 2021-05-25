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
    public void onEnable() {
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    // onCommand は plugin.yml に記載されたコマンドが呼ばれた時に実行
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        // argsが無かったら、切り返す
        if (args.length == 0) {
            return false;
        }
        String output;
        switch (args[0]) {
            case "start":
                // output = "攻城戦を開始します";
                GameStart(sender,cmd,args);
                break;
            case "check":
                // output = "設定を表示します";
                CheckSettings(sender,cmd,args);
                break;
            case "setfightteam":
                // output = "参加チームに追加";
                AddFighters(sender,cmd,args);
                break;
            case "setwatchteam":
                // output = "観覧チームに追加";
                SetWatcher(sender,cmd,args);
                break;
            case "setspawn":
                output = "スポーン地点設定";
                break;
            case "setborder":
                output = "範囲指定";
                break;
            case "settimelimit":
                output = "制限自邸を設定";
                break;
            default:
                return false;
        }
        // sender.sendMessage(output);
        return true;
    }
    public void GameStart(CommandSender sender, Command cms, String[] args) {
        // ゲーム開始
        GameController.start();
    }
    public void AddFighters(CommandSender sender, Command cmd, String[] args) {
        // 戦闘チーム追加
        // 既に２つあったら、古いやつを消す
    }
    public void SetWatcher(CommandSender sender, Command cmd, String[] args) {
        // 観覧チームを指定
        // 一つのみ指定可能
    }
    public void CheckSettings(CommandSender sender, Command cmd, String[] args) {
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
                    .append("クリックでチーム1リス地へTP")
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
