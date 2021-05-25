package com.zect.battleplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class BattlePlugin extends JavaPlugin {

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
                GameStart(sender,cmd,args)
                break;
            case "check":
                // output = "設定を表示します";
                CheckSettings(sender,cmd,args);
                break;
            case "setfightteam":
                output = "参加チームに追加";
                break;
            case "setwatchteam":
                output = "観覧チームに追加";
                break;
            case "setborder":
                output = "範囲指定";
                break;
            case "setspawn":
                output = "スポーン地点設定";
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
        GameController.start();
    }
    public void CheckSettings(CommandSender sender, Command cmd, String[] args) {
        BaseComponent[] check = new ComponentBuilder(
                new TextComponent(new ComponentBuilder()
                        .append("[攻城戦支援プラグイン] ").color(ChatColor.LIGHT_PURPLE)
                        .append("設定一覧を表示予定").color(ChatColor.GREEN)
                        .append("[クリックで対戦を開始]").color(ChatColor.GOLD)
                        .create())
        )
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder()
                        .append("クリックで攻城戦を開始")
                        .create()
                ))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ("/battle start") + token))
                .create();
        sender.sendMessage(check);
    }
    
}
