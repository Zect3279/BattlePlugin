package com.zect.battleplugin;

import dev.jorel.commandapi.*;
import dev.jorel.commandapi.arguments.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

import java.util.*;


public final class BattlePlugin extends JavaPlugin {

    @Override
    public void onLoad() {
        CommandAPI.onLoad(true);

        String[] subCommandList = new String[] {"start", "check", "FightTeam", "WatcherTeam", "SetCorner", "SetTimeLimit"};

        List<Argument> teamArgument = new ArrayList<>();
        teamArgument.add(new TeamArgument("team").safeOverrideSuggestions(s ->
            Bukkit.getScoreboardManager().getMainScoreboard().getTeams().toArray(new Team[0]))
        );


        new CommandAPICommand("siege")
                .withSubcommand(new CommandAPICommand("start")
                        .executes((sender, args) -> {
                            //perm group remove code
//                            sender.sendMessage("開始");
                            GameStart(sender, args);
                        })
                )
                .withSubcommand(new CommandAPICommand("check")
                        .executes((sender, args) -> {
                            //perm group remove code
                            sender.sendMessage("設定確認");
                            CheckSettings(sender, args);
                        })
                )
                .withSubcommand(new CommandAPICommand("FightTeam")
                        .withSubcommand(new CommandAPICommand("add")
                                .withArguments(teamArgument)
                                .executes((sender, args) -> {
                                    //perm group add code
//                                    sender.sendMessage("戦闘チームを追加");
                                    AddFighters(sender,args);
                                })
                        )
                        .withSubcommand(new CommandAPICommand("remove")
                                .withArguments(teamArgument)
                                .executes((sender, args) -> {
                                    //perm group remove code
                                    sender.sendMessage("戦闘チームを削除");
                                })
                        )
                )
                .withSubcommand(new CommandAPICommand("WatchTeam")
                        .withSubcommand(new CommandAPICommand("add")
                                .withArguments(teamArgument)
                                .executes((sender, args) -> {
                                    //perm group add code
//                    case args[0]
                                    sender.sendMessage("観覧チームを追加");
                                    AddFighters(sender,args);
                                })
                        )
                        .withSubcommand(new CommandAPICommand("remove")
                                .withArguments(teamArgument)
                                .executes((sender, args) -> {
                                    //perm group remove code
                                    sender.sendMessage("観覧チームを削除");
                                })
                        )
                )
                .withSubcommand(new CommandAPICommand("SetCorner")
                        .withArguments(new LocationArgument("location"))
                        .executes((sender, args) -> {
                            //perm group remove code
                            sender.sendMessage("座標を登録");
                        })
                )
                .withSubcommand(new CommandAPICommand("SetTimeLimit")
                        .withArguments(new IntegerArgument("second"))
                        .executes((sender, args) -> {
                            //perm group remove code
                            sender.sendMessage("時間制限を追加");
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

    Map<String, Location> TeamRes = new HashMap<>();
    Map<String, String> TeamName = new HashMap<>();

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
            sender.sendMessage(ChatColor.AQUA + "[攻城戦支援プラグイン]\n"
                    + ChatColor.RED + checking + "\nが設定できていないため、ゲームを開始できません。"
            );

        }
        // ゲーム開始
        GameController.start();
    }
    public void AddFighters(CommandSender sender, Object[] args) {
        // 戦闘チーム追加
        // 既に２つあったら、エラー吐かせる
        String fighter = (String) args[0];
        Server server = sender.getServer();
        Team Fighter = server.getScoreboardManager().getMainScoreboard().getTeam(fighter);

        String Team1 = TeamName.get("Team1");
        String Team2 = TeamName.get("Team2");
        String FighterName = Fighter.getName();

        if (Team1 == null) {
            if (FighterName.equals(Team2)) {
                sender.sendMessage(ChatColor.AQUA + "[攻城戦支援プラグイン]\n"
                        + Fighter.getColor() + "[" + Fighter.getName() + "]\n"
                        + ChatColor.GREEN + "は既に戦闘チームに追加されています。"
                );
            } else {
                TeamName.put("Team1", FighterName);
                sender.sendMessage(ChatColor.AQUA + "[攻城戦支援プラグイン]\n"
                        + Fighter.getColor() + "[" + Fighter.getName() + "]\n"
                        + ChatColor.GREEN + "を戦闘チームに追加しました。"
                );
            }
        } else if (Team2 == null) {
            if (FighterName.equals(Team1)) {
                sender.sendMessage(ChatColor.AQUA + "[攻城戦支援プラグイン]\n"
                        + Fighter.getColor() + "[" + Fighter.getName() + "]\n"
                        + ChatColor.GREEN + "は既に戦闘チームに追加されています。"
                );
            } else {
                TeamName.put("Team2", FighterName);
                sender.sendMessage(ChatColor.AQUA + "[攻城戦支援プラグイン]\n"
                        + Fighter.getColor() + "[" + Fighter.getName() + "]\n"
                        + ChatColor.GREEN + "を戦闘チームに追加しました。"
                );
            }
        } else {
            sender.sendMessage(ChatColor.AQUA + "[攻城戦支援プラグイン]\n"
                    + ChatColor.RED + "二チーム登録されているため、\nこれ以上のチームを登録することはできません。"
                    + "\n別チームを登録したい場合は、\n"
                    + ChatColor.GREEN + "/siege FightTeam remove <Team>\n"
                    + ChatColor.RED + "でremoveできます。"

            );
        }

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
        if (TeamRes.get("Team1") == null) {
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
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ("/tp ") + TeamRes.get("Team1")))
                .create();
        }
        if (TeamRes.get("Team2") == null) {
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
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ("/tp ") + TeamRes.get("Team2")))
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
