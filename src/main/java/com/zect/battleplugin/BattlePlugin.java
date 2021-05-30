package com.zect.battleplugin;

import dev.jorel.commandapi.*;
import dev.jorel.commandapi.arguments.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

import java.util.*;


public final class BattlePlugin extends JavaPlugin {

    @Override
    public void onLoad() {
        // おまじない
        CommandAPI.onLoad(true);

        // サブコマンドリスト　使うかもしれない
//        String[] subCommandList = new String[] {"start", "check", "FightTeam", "WatcherTeam",　"Respawn", "SetCorner", "SetTimeLimit"};

        // チーム一覧をコマンド入力時に出力するための変数
        List<Argument> teamArgument = new ArrayList<>();
        teamArgument.add(new TeamArgument("team").safeOverrideSuggestions(s ->
            Bukkit.getScoreboardManager().getMainScoreboard().getTeams().toArray(new Team[0]))
        );

        // コマンドを設定する
        new CommandAPICommand("siege")
                .withSubcommand(new CommandAPICommand("start")
                        .executes(this::GameStart)
                )
                .withSubcommand(new CommandAPICommand("check")
                        .executes(this::CheckSettings)
                )
                .withSubcommand(new CommandAPICommand("FightTeam")
                        .withSubcommand(new CommandAPICommand("list")
                                .executes(this::ShowFighters)
                        )
                        .withSubcommand(new CommandAPICommand("add")
                                .withArguments(teamArgument)
                                .executes(this::AddFighters)
                        )
                        .withSubcommand(new CommandAPICommand("remove")
                                .withArguments(teamArgument)
                                .executes(this::RemoveFighters)
                        )
                )
                .withSubcommand(new CommandAPICommand("WatchTeam")
                        .withSubcommand(new CommandAPICommand("list")
                                .executes(this::ShowWatchers)
                        )
                        .withSubcommand(new CommandAPICommand("add")
                                .withArguments(teamArgument)
                                .executes(this::AddWatcher)
                        )
                        .withSubcommand(new CommandAPICommand("remove")
                                .withArguments(teamArgument)
                                .executes(this::RemoveWatcher)
                        )
                )
                .withSubcommand(new CommandAPICommand("Respawn")
                        .withArguments(teamArgument)
                        .withArguments(new LocationArgument("SpawnPoint"))
                        .executes(this::SpawnPoint)
                )
                .withSubcommand(new CommandAPICommand("SetCorner")
                        .withArguments(new LocationArgument("location"))
                        .executes(this::AddCorner)
                )
                .withSubcommand(new CommandAPICommand("SetTimeLimit")
                        .withArguments(new TimeArgument("second"))
                        .executes(this::SetTimeLimit)
                )
                .register();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        // おまじない
        CommandAPI.onEnable(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    // リスポーン地点を保存
    Map<String, Location> TeamRes = new HashMap<>();

    // <Team, Team.getName()> チーム番号と名前を保存
    Map<String, String> TeamName = new HashMap<>();

    // 戦闘範囲の角
    ArrayList<Location> Corner = new ArrayList<>();

    // デフォルトの秒数を300秒に
    public Integer timeLimit = 300;

    public void CheckCanPlay() {
        // ゲーム開始可能か確認する
        // 開始できなかったら、reasonを返す

        // ゲームが開始できるなら、nullを返す
    }

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
    public void SetTimeLimit(CommandSender sender, Object[] args) {
        // 時間制限を追加
        Integer before = timeLimit;
        timeLimit = (Integer) args[0];
        sender.sendMessage(ChatColor.AQUA + "[攻城戦支援プラグイン]\n"
                + ChatColor.GREEN + "攻城戦の制限時間を\n"
                + ChatColor.YELLOW + "[" + before + "]"
                + ChatColor.GREEN + "から"
                + ChatColor.YELLOW + "[" + timeLimit + "]\n"
                + ChatColor.GREEN + "に設定しました。"
        );
    }
    public void AddCorner(CommandSender sender, Object[] args) {
        // 角を追加
        Location corner = (Location) args[0];

        Location Corner1 = Corner.get(0);
        Location Corner2 = Corner.get(1);

        Integer x = corner.getBlockX();
        Integer y = corner.getBlockY();
        Integer z = corner.getBlockZ();

        // クリックしたらTPする不思議なブロックを生成
        BaseComponent[] TP = new ComponentBuilder(
                new TextComponent(new ComponentBuilder()
                        .append("[設定した地点へTP]").color(ChatColor.GOLD)
                        .create())
        )
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder()
                        .append("クリックで設定した角へTP")
                        .create()
                ))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + x + " " + y + " " + z))
                .create();

        if (corner == Corner1 || corner == Corner2) {
            sender.sendMessage(ChatColor.AQUA + "[攻城戦支援プラグイン]\n"
                    + ChatColor.RED + "この座標は既に登録されています。\n"
                    + "座標を確認して、再度実行してください。"
            );
        } else if (Corner.size() < 3) {
            Corner.add(corner);
            sender.sendMessage(ChatColor.AQUA + "[攻城戦支援プラグイン]\n"
                    + ChatColor.GREEN + "範囲の角を一つ指定しました。\n"
                    + "以下のブロックからTPできます。"
            );
            sender.sendMessage(TP);
        } else if (Corner.size() > 2){
            Corner.remove(0);
            Corner.add(corner);
            sender.sendMessage(ChatColor.AQUA + "[攻城戦支援プラグイン]\n"
                    + ChatColor.GREEN + "範囲の角を一つ指定しました。\n"
                    + "以下のブロックからTPできます。"
            );
            sender.sendMessage(TP);
        } else {
            sender.sendMessage(ChatColor.AQUA + "[攻城戦支援プラグイン]\n"
                    + ChatColor.RED + "エラーが発生しました。\n入力したコマンドを確認して、もう一度試してください。"
            );
        }
    }
    public void AddFighters(CommandSender sender, Object[] args) {
        // 戦闘チーム追加

        // コマンド入力時に取得したチーム名からチームオブジェクトを持ってくる
        String fighter = (String) args[0];
        Server server = sender.getServer();
        Team Fighter = server.getScoreboardManager().getMainScoreboard().getTeam(fighter);

        // 登録されてるかもしれないチーム名を取得
        // 1,2は戦闘、3は観覧
        String Team1 = TeamName.get("Team1");
        String Team2 = TeamName.get("Team2");
        String Team3 = TeamName.get("Team3");
        // 登録しようとしてるチームの名前を取得
        String FighterName = Fighter.getName();

        if (FighterName == Team3) {
            sender.sendMessage(ChatColor.AQUA + "[攻城戦支援プラグイン]\n"
                    + Fighter.getColor() + "[" + Fighter.getName() + "]\n"
                    + ChatColor.GREEN + "は観覧チームに参加しているため、攻城戦には参加できません。"
            );
        } else if (Team1 == null) {
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
                    + ChatColor.RED + "でremoveした後に、もう一度試してください。"

            );
        }

    }
    public void RemoveFighters(CommandSender sender, Object[] args) {
        // 戦闘チーム撤去

        // 細かい説明は、AddFightersを見てね
        String fighter = (String) args[0];
        Server server = sender.getServer();
        Team Fighter = server.getScoreboardManager().getMainScoreboard().getTeam(fighter);

        String Team1 = TeamName.get("Team1");
        String Team2 = TeamName.get("Team2");
        String FighterName = Fighter.getName();

        if (FighterName == Team1) {
            TeamName.put("Team1", null);
            sender.sendMessage(ChatColor.AQUA + "[攻城戦支援プラグイン]\n"
                    + Fighter.getColor() + "[" + Fighter.getName() + "]\n"
                    + ChatColor.GREEN + "を戦闘チームから削除しました。"
            );
        } else if (FighterName == Team2) {
            TeamName.put("Team2", null);
            sender.sendMessage(ChatColor.AQUA + "[攻城戦支援プラグイン]\n"
                    + Fighter.getColor() + "[" + Fighter.getName() + "]\n"
                    + ChatColor.GREEN + "を戦闘チームから削除しました。"
            );
        } else if (Team1 == null && Team2 == null) {
            sender.sendMessage(ChatColor.AQUA + "[攻城戦支援プラグイン]\n"
                    + ChatColor.RED + "チームリストに何も入っていないため、\nこれ以上チームを削除することはできません。"
                    + "\nチームを登録したい場合は、\n"
                    + ChatColor.GREEN + "/siege FightTeam add <Team>\n"
                    + ChatColor.RED + "で追加できます。"

            );

        } else {
            sender.sendMessage(ChatColor.AQUA + "[攻城戦支援プラグイン]\n"
                    + ChatColor.RED + "指定されたチームは、戦闘チームに設定されていません。\n"
                    + "チーム名を確認して、もう一度試してください。"
            );
        }

    }
    public void ShowFighters(CommandSender sender, Object[] args) {
        // チーム一覧を表示
        // 設定されてなかったら、nullが返る
        sender.sendMessage(ChatColor.AQUA + "[攻城戦支援プラグイン]"
                + ChatColor.GREEN
                + "\n戦闘チーム1 : " + TeamName.get("Team1")
                + "\n戦闘チーム2 : " + TeamName.get("Team2")
        );
    }
    public void AddWatcher(CommandSender sender, Object[] args) {
        // 観覧チームを追加

        // 細かい説明は、AddFightersに書いてある
        String watcher = (String) args[0];
        Server server = sender.getServer();
        Team Watcher = server.getScoreboardManager().getMainScoreboard().getTeam(watcher);

        String Team1 = TeamName.get("Team1");
        String Team2 = TeamName.get("Team2");
        String Team3 = TeamName.get("Team3");
        String FighterName = Watcher.getName();

        if (FighterName == Team1 || FighterName == Team2) {
            sender.sendMessage(ChatColor.AQUA + "[攻城戦支援プラグイン]\n"
                    + Watcher.getColor() + "[" + Watcher.getName() + "]\n"
                    + ChatColor.GREEN + "は戦闘チームに参加しているため、攻城戦の観覧はできません。"
            );
        } else if (Team3 == null) {
            if (FighterName.equals(Team3)) {
                sender.sendMessage(ChatColor.AQUA + "[攻城戦支援プラグイン]\n"
                        + Watcher.getColor() + "[" + Watcher.getName() + "]\n"
                        + ChatColor.GREEN + "は既に観覧チームに追加されています。"
                );
            } else {
                TeamName.put("Team3", FighterName);
                sender.sendMessage(ChatColor.AQUA + "[攻城戦支援プラグイン]\n"
                        + Watcher.getColor() + "[" + Watcher.getName() + "]\n"
                        + ChatColor.GREEN + "を観覧チームに追加しました。"
                );
            }
        } else {
            sender.sendMessage(ChatColor.AQUA + "[攻城戦支援プラグイン]\n"
                    + ChatColor.RED + "既に登録されているため、\nこれ以上のチームを登録することはできません。"
                    + "\n別チームを登録したい場合は、\n"
                    + ChatColor.GREEN + "/siege FightTeam remove <Team>\n"
                    + ChatColor.RED + "でremoveした後に、もう一度試してください。"

            );
        }

    }
    public void RemoveWatcher(CommandSender sender, Object[] args) {
        // 観覧チームを削除

        // 細かい説明は、AddFightersに書いてある
        String watcher = (String) args[0];
        Server server = sender.getServer();
        Team Watcher = server.getScoreboardManager().getMainScoreboard().getTeam(watcher);

        String Team3 = TeamName.get("Team3");
        String FighterName = Watcher.getName();

        if (FighterName == Team3) {
            TeamName.put("Team3", null);
            sender.sendMessage(ChatColor.AQUA + "[攻城戦支援プラグイン]\n"
                    + Watcher.getColor() + "[" + Watcher.getName() + "]\n"
                    + ChatColor.GREEN + "を観覧チームから削除しました。"
            );
        } else if (Team3 == null) {
            sender.sendMessage(ChatColor.AQUA + "[攻城戦支援プラグイン]\n"
                    + ChatColor.RED + "チームリストに何も入っていないため、\nこれ以上チームを削除することはできません。"
                    + "\nチームを登録したい場合は、\n"
                    + ChatColor.GREEN + "/siege WatchTeam add <Team>\n"
                    + ChatColor.RED + "で追加できます。"

            );
        } else {
            sender.sendMessage(ChatColor.AQUA + "[攻城戦支援プラグイン]\n"
                    + ChatColor.RED + "指定されたチームは、観覧チームに設定されていません。\n"
                    + "チーム名を確認して、もう一度試してください。"
            );
        }
    }
    public void ShowWatchers(CommandSender sender, Object[] args) {
        // 観覧チームを表示
        sender.sendMessage(ChatColor.AQUA + "[攻城戦支援プラグイン]"
                + ChatColor.GREEN
                + "\n観覧チーム : " + TeamName.get("Team3")
        );
    }
    public void SpawnPoint(CommandSender sender, Object[] args) {
        // スポーン地点設定
        String fighter = (String) args[0];
        Server server = sender.getServer();
        Team Fighter = server.getScoreboardManager().getMainScoreboard().getTeam(fighter);

        String Team1N = TeamName.get("Team1");
        String Team2N = TeamName.get("Team2");
        String FighterName = Fighter.getName();
        Location fighterRes = (Location) args[1];

        if (!FighterName.equals(Team1N) && !FighterName.equals(Team2N)) {
            sender.sendMessage(ChatColor.AQUA + "[攻城戦支援プラグイン]\n"
                    + ChatColor.RED + "指定されたチームは、戦闘チームに設定されていません。\n"
                    + "チーム名を確認して、もう一度試してください。"
            );
        } else if (FighterName.equals(Team1N)) {
            TeamRes.put("Team1", fighterRes);
            // 設定したリスポーン地点の座標をx,y,zに保存
            Integer x = fighterRes.getBlockX();
            Integer y = fighterRes.getBlockY();
            Integer z = fighterRes.getBlockZ();

            // クリックしたらTPする不思議なブロックを生成
            BaseComponent[] TP1 = new ComponentBuilder(
                    new TextComponent(new ComponentBuilder()
                            .append("[Team1リス地へTP]").color(ChatColor.GOLD)
                            .create())
            )
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder()
                            .append("クリックでチーム1のリス地へTP")
                            .create()
                    ))
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + x + " " + y + " " + z))
                    .create();
            sender.sendMessage(ChatColor.AQUA + "[攻城戦支援プラグイン]\n"
                    + Fighter.getColor() + "[" + Fighter.getName() + "]\n"
                    + ChatColor.GREEN + "のリス地を設定しました。\n以下のブロックから設定場所にTPできます。"
            );
            sender.sendMessage(TP1);
        } else if (FighterName.equals(Team2N)) {
            TeamRes.put("Team2", fighterRes);
            Integer x = fighterRes.getBlockX();
            Integer y = fighterRes.getBlockY();
            Integer z = fighterRes.getBlockZ();
            BaseComponent[] TP2 = new ComponentBuilder(
                    new TextComponent(new ComponentBuilder()
                            .append("[Team2リス地へTP]").color(ChatColor.GOLD)
                            .create())
            )
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder()
                            .append("クリックでチーム2のリス地へTP")
                            .create()
                    ))
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + x + " " + y + " " + z))
                    .create();
            sender.sendMessage(ChatColor.AQUA + "[攻城戦支援プラグイン]\n"
                    + Fighter.getColor() + "[" + Fighter.getName() + "]\n"
                    + ChatColor.GREEN + "のリス地を設定しました。\n以下のブロックから設定場所にTPできます。"
            );
            sender.sendMessage(TP2);
        } else {
            sender.sendMessage(ChatColor.AQUA + "[攻城戦支援プラグイン]\n"
                    + ChatColor.RED + "エラーが発生しました。\n"
                    + "引数等を確認して、再度試してください。"
            );
        }

    }
    public BaseComponent[] SettingList() {
        // 設定一覧を作成して、返す
        BaseComponent[] settings = new ComponentBuilder(
                new TextComponent(new ComponentBuilder()
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
//        if (TeamRes.get("Team1") == null) {
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
//        }
//        if (TeamRes.get("Team2") == null) {
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
//        }
//        String checking = "ああ";
//        String checking = CheckCanPlay();
//        if (checking == null) {
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
//        }

        sender.sendMessage(check + "\n" + TP1 + "\n" + TP2 + "\n" + starting);
    }

}
