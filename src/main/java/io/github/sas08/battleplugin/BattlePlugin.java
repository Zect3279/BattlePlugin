package io.github.sas08.battleplugin;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;


public final class BattlePlugin extends JavaPlugin implements Listener {

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

        // ゲームルールのアーギュメントリスト
        List<Argument> gamemodeArgument = new ArrayList<>();
        String[] ruleList = new String[] {"survival", "simple", "king"};
        gamemodeArgument.add(new StringArgument("Rule").overrideSuggestions(ruleList));

        // コマンドを設定する
        new CommandAPICommand("siege")
                .withSubcommand(new CommandAPICommand("title")
                        .executes(this::TitleCall)
                )
                .withSubcommand(new CommandAPICommand("start")
                        .withArguments(new IntegerArgument("Phase"))
                        .executes(this::GameStart)
                )
                .withSubcommand(new CommandAPICommand("check")
                        .executes(this::CheckSettings)
                )
                .withSubcommand(new CommandAPICommand("simoru")
                        .executes(this::Simota)
                )
//                 .withSubcommand(new CommandAPICommand("Teaming")
//                         .executes(this::GiveTeam)
//                 )
                .withSubcommand(new CommandAPICommand("setKing")
                        .withArguments(new PlayerArgument("target"))
                        .executes(this::SetKings)
                )
                .withSubcommand(new CommandAPICommand("gamemode")
                        .withArguments(gamemodeArgument)
                        .executes(this::SetGameRule)
                )
                .withSubcommand(new CommandAPICommand("gamerule")
                        .withSubcommand(new CommandAPICommand("SpectatorAfterDeath")
                                .withArguments(new BooleanArgument("Bool"))
                                .executes(this::SpecAfterDeath)
                        )
                )
                .withSubcommand(new CommandAPICommand("Respawn")
                        .withArguments(teamArgument)
                        .withArguments(new LocationArgument("SpawnPoint"))
                        .executes(this::SpawnPoint)
                )
                .withSubcommand(new CommandAPICommand("Beacon")
                        .withArguments(teamArgument)
                        .withArguments(new LocationArgument("BeaconPosition"))
                        .executes(this::BeaconPosition)
                )
                .withSubcommand(new CommandAPICommand("SetTimeLimit")
                        .withArguments(new TimeArgument("second"))
                        .executes(this::SetTimeLimit)
                )
                .register();


        new CommandAPICommand("test")
                .withSubcommand(new CommandAPICommand("count")
                        .executes(this::toCount)
                )
                .withSubcommand(new CommandAPICommand("team")
                        .executes(this::GiveTeam)
                )
                .withSubcommand(new CommandAPICommand("start")
                        .withArguments(gamemodeArgument)
                        .withArguments(new StringArgument("Phase"))
                        .executes(this::TestStart)
                )
                .withSubcommand(new CommandAPICommand("seeActionBar")
                        .withArguments(gamemodeArgument)
                        .executes(this::toAction)
                )
                .register();
    }

    private void toAction(CommandSender sender, Object[] args) {
        String type = (String) args[0];
        GameController.Control(type);
    }

    private void SpecAfterDeath(CommandSender sender, Object[] args) {
        system.DoSpectator = (Boolean) args[0];
    }

    private void toCount(CommandSender sender, Object[] args) {
        GameController.Count("マイクラ戦争が始まるよ");
    }

    private void TestStart(CommandSender sender, Object[] args) {
        String Type = (String) args[0];
        switch (Type) {
            case "survival":
                GameController.Count("敵のビーコンを破壊しろ！");
                break;
            case "king":
            case "simple":
                String phase = (String) args[1];
                switch (phase) {
                    case "1":
                        GameController.KingCount("大将: " + ChatColor.RED + "A大将"
                                        + ChatColor.WHITE + "を守れ！",
                                "大将: " + ChatColor.RED + "A大将"
                                        + ChatColor.WHITE + "を殺せ！");
                        break;
                    case "2":
                        GameController.KingCount("大将: " + ChatColor.BLUE + "B大将"
                                        + ChatColor.WHITE + "を殺せ！",
                                "大将: " + ChatColor.BLUE + "B大将"
                                        + ChatColor.WHITE + "を守れ！");
                        break;
                    default:
                        sender.sendMessage("エラーが発生");
                        break;
                }
                break;
            default:
                sender.sendMessage("エラーが発生");
                break;
        }
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        // おまじない
        getServer().getPluginManager().registerEvents(new EventListener(), this);
        CommandAPI.onEnable(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    // チーム情報を作成
    Map<String, TeamUtil> Teams = new HashMap<>();

    static SystemUtil system = new SystemUtil();

    private void SetKings(CommandSender sender, Object[] args) {
        // Kingを追加
        Player king = (Player) args[0];
        Server server = sender.getServer();
        Scoreboard score = server.getScoreboardManager().getMainScoreboard();
        Team team = score.getPlayerTeam(king);

        if (team == null) {
            sender.sendMessage("チームに所属してないよ");
        } else if (team.getName() == "Red") {
            Teams.get("Team1").setKing(king);
            sender.sendMessage("[" + king.getName() + "]" + "を赤チームの大将を決定した");
        } else if (team.getName() == "Blue") {
            Teams.get("Team2").setKing(king);
            sender.sendMessage("[" + king.getName() + "]" + "を青チームの大将を決定した");
        }

    }

    public void SetGameRule(CommandSender sender, Object[] args) {
        String rule = (String) args[0];
        sender.sendMessage(rule);
        switch (rule) {
            case "survival":
                sender.sendMessage("サバイバル戦\n・時間制限なし\n・チケット0で終了\n・ビーコン有り");
                system.setType("survival");
                break;
            case "king":
                sender.sendMessage("大将戦\n・時間制限なし\n・大将を殺して終了");
                system.setType("king");
                break;
            case "simple":
                sender.sendMessage("シンプル戦\n・時間制限有り\n・大将を殺して終了");
                system.setType("simple");
                break;
            default:
                sender.sendMessage("エラーが発生");
                break;
        }
    }
    public void TitleCall(CommandSender sender, Object[] args) {
        Util.setTitle(ChatColor.GREEN + "マイクラ戦争" + ChatColor.RED + "プラグイン", ChatColor.GREEN + "制作:Zect", 500);
    }
    public void GameStart(CommandSender sender, Object[] args) {
        // ゲーム開始できるか判定する
//         String checking = "ああ";
        String checking = CheckCanPlay();
        if (checking != null) {
            // ゲームが開始できない
            sender.sendMessage(ChatColor.AQUA + "[攻城戦支援プラグイン]\n"
                    + ChatColor.RED + checking + "\nが設定できていないため、ゲームを開始できません。"
            );
            return;

        }
        // ゲーム開始
        sender.sendMessage("ゲームを開始");
        
        Server server = sender.getServer();
        Scoreboard MainBoard = server.getScoreboardManager().getMainScoreboard();

        // チーム割当
//        GiveTeam(sender, args);
        
        /* 引数
        * - [x] 戦闘チーム
        * - [x] 観覧チーム
        * - [x] リス地
        * - [x] 範囲
        * - [x] 制限時間
        * - [x] スコアボードobj
        * - [x] サーバーobj
        */
        Integer phase = (Integer) args[0];
        switch (system.getType()) {
            case "survival":
                GameController.SurvivalStart(system, Teams, server, MainBoard);
                break;
            case "king":
                GameController.KingStart(system, Teams, server, MainBoard, phase);
                break;
            case "simple":
                GameController.SimpleStart(system, Teams, server, MainBoard, phase);
                break;
            default:
                sender.sendMessage("エラーが発生");
                break;
        }
    }
    public void GiveTeam(CommandSender sender, Object[] args) {
        // チームに所属させる
        // チームカラーを取得して、その色で
        // [0 - 0]
        // みたいに表示する
        // 残り人数も表示する
        // チーム作成
        MakeTeams(sender);

        Server server = sender.getServer();
        Scoreboard score = server.getScoreboardManager().getMainScoreboard();

        TeamUtil Team1 = Teams.get("Team1");
        TeamUtil Team2 = Teams.get("Team2");

        String size1 = "0";
        String size2 = "0";

        try {
            Util.setTitle(size1 + " - " + size2, "チーム分けを開始", 100);

            Thread.sleep(200);

            Team team1 = score.getTeam(Teams.get("Team1").getName());
            Team team2 = score.getTeam(Teams.get("Team2").getName());
            Collection<? extends Player> players = server.getOnlinePlayers();
            Integer index = 0;

            for (Player player : players) {
                switch (index % 2) {
                    case 0:
                        team1.addPlayer(player);
                        index += 1;
                        break;
                    case 1:
                        team2.addPlayer(player);
                        index += 1;
                        break;
                    default:
                        break;
                }
                size1 = String.valueOf(team1.getSize());
                size2 = String.valueOf(team2.getSize());
                Integer Remainers = players.size() - index;
                String remainers = Remainers.toString();
                Util.setTitle(team1.getColor() + size1 + ChatColor.WHITE + " - " + team2.getColor() + size2, "チーム分け中\n残り:" + remainers, 100);
            }
            Thread.sleep(200);

            Util.setTitle(team1.getColor() + size1 + ChatColor.WHITE + " - " + team2.getColor() + size2, "チーム分け完了", 100);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void MakeTeams(CommandSender sender) {
        // チームを作成・登録

        // コマンド入力時に取得したチーム名からチームオブジェクトを持ってくる
        Server server = sender.getServer();
        Scoreboard Board = server.getScoreboardManager().getMainScoreboard();

        if (Board.getTeam("Red") != null) {
            Board.getTeam("Red").unregister();
        }
        if (Board.getTeam("Blue") != null) {
            Board.getTeam("Blue").unregister();
        }
        if (Board.getTeam("Co") != null) {
            Board.getTeam("Co").unregister();
        }

        Team team1 = Board.registerNewTeam("Red");
        Team team2 = Board.registerNewTeam("Blue");
        Team team3 = Board.registerNewTeam("Co");

        team1.setColor(org.bukkit.ChatColor.RED);
        team2.setColor(org.bukkit.ChatColor.BLUE);
        team3.setColor(org.bukkit.ChatColor.AQUA);
        
        team1.setAllowFriendlyFire(false);
        team2.setAllowFriendlyFire(false);
        team3.setAllowFriendlyFire(false);

        // チーム名を追加
        // 1,2は戦闘、3は観覧
        Teams.put("Team1", new TeamUtil("Red", org.bukkit.ChatColor.RED));
        Teams.put("Team2", new TeamUtil("Blue", org.bukkit.ChatColor.BLUE));
        Teams.put("Team3", new TeamUtil("Co", org.bukkit.ChatColor.AQUA));
        Teams.get("Team3").setOp(true);

    }
    public String CheckCanPlay() {
        // ゲーム開始可能か確認する
        // 開始できなかったら、reasonを返す
        // ゲームが開始できるなら、nullを返す
        
        /* ゲーム開始条件
        *  - 戦闘チームが２つある 
        *  - 範囲指定用の角が２つある
        *  - スポーン/リスポーン地点が２つ設定されてる
        */
        String cantStart = "";
        Integer can = 0;
        TeamUtil team1 = Teams.get("Team1");
        TeamUtil team2 = Teams.get("Team2");
        
        // 開始条件のどれか一つでも無かったら、開始できない
        switch (system.getType()) {
            case "survival":
//                sender.sendMessage("サバイバル戦\n・時間制限なし\n・チケット0で終了\n・ビーコン有り");
                if (team1.getBeacon() == null || team2.getBeacon() == null) {
                    cantStart += "\n- ビーコンの場所が指定されてない";
                    can += 1;
                }
                if (team1.getRespawn() == null || team2.getRespawn() == null) {
                    cantStart += "\n- リスポーン地点が指定されていない";
                    can += 1;
                }

                break;
            case "king":
            case "simple":
//                sender.sendMessage("大将戦\n・時間制限なし\n・大将を殺して終了");
//                sender.sendMessage("シンプル戦\n・時間制限有り\n・大将を殺して終了");
                if (team1.getKing() == null || team2.getKing() == null) {
                    cantStart += "\n- 大将が指定できてない";
                    can += 1;
                }
                if (team1.getRespawn() == null || team2.getRespawn() == null) {
                    cantStart += "\n- リスポーン地点が指定されていない";
                    can += 1;
                }

                break;
            default:
                break;
        }
        if (can == 0) {
            return null;
        } else {
            return cantStart;
        }
    }
    public void SetTimeLimit(CommandSender sender, Object[] args) {
        // 時間制限を追加
        Integer before = system.getTimes();
        system.setTimes((Integer) args[0]);
        sender.sendMessage(ChatColor.AQUA + "[攻城戦支援プラグイン]\n"
                + ChatColor.GREEN + "攻城戦の制限時間を\n"
                + ChatColor.YELLOW + "[" + before + "]"
                + ChatColor.GREEN + "から"
                + ChatColor.YELLOW + "[" + system.getTimes() + "]\n"
                + ChatColor.GREEN + "に設定しました。"
        );
    }
    public void SpawnPoint(CommandSender sender, Object[] args) {
        // スポーン地点設定
        String fighter = (String) args[0];
        Server server = sender.getServer();
        Team Fighter = server.getScoreboardManager().getMainScoreboard().getTeam(fighter);

        TeamUtil team1 = Teams.get("Team1");
        TeamUtil team2 = Teams.get("Team2");
        String FighterName = Fighter.getName();
        Location fighterRes = (Location) args[1];
        Player player = sender.getServer().getPlayer(sender.getName());

        if (!FighterName.equals(team1.getName()) && !FighterName.equals(team2.getName())) {
            sender.sendMessage(ChatColor.AQUA + "[攻城戦支援プラグイン]\n"
                    + ChatColor.RED + "指定されたチームは、戦闘チームに設定されていません。\n"
                    + "チーム名を確認して、もう一度試してください。"
            );
        } else if (FighterName.equals(team1.getName())) {
            team1.setRespawn(fighterRes);
            // 設定したリスポーン地点の座標をx,y,zに保存
            Integer x = fighterRes.getBlockX();
            Integer y = fighterRes.getBlockY();
            Integer z = fighterRes.getBlockZ();

            // クリックしたらTPする不思議なブロックを生成
            BaseComponent[] TP1 = Util.createBaseComponent("[Team1リス地へTP]",
                                                           "クリックでチーム1のリス地へTP",
                                                           "/tp " + x + " " + y + " " + z);
            Util.sendMessage(player, Fighter.getColor() + "[" + Fighter.getName() + "]\n"
                    + ChatColor.GREEN + "のリス地を設定しました。\n"
                    + "以下のブロックから設定場所にTPできます。");
            sender.sendMessage(TP1);
        } else if (FighterName.equals(team2.getName())) {
            team2.setRespawn(fighterRes);
            Integer x = fighterRes.getBlockX();
            Integer y = fighterRes.getBlockY();
            Integer z = fighterRes.getBlockZ();
            BaseComponent[] TP2 = Util.createBaseComponent("[Team2リス地へTP]",
                                                           "クリックでチーム2のリス地へTP",
                                                           "/tp " + x + " " + y + " " + z);
            Util.sendMessage(player, Fighter.getColor() + "[" + Fighter.getName() + "]\n"
                    + ChatColor.GREEN + "のリス地を設定しました。\n"
                    + "以下のブロックから設定場所にTPできます。"
            );
            sender.sendMessage(TP2);
        } else {
            Util.sendMessage(player, ChatColor.RED + "エラーが発生しました。\n"
                    + "引数等を確認して、再度試してください。"
            );
        }

    }
    private void BeaconPosition(CommandSender sender, Object[] args) {
        // スポーン地点設定
        String fighter = (String) args[0];
        Server server = sender.getServer();
        Team Fighter = server.getScoreboardManager().getMainScoreboard().getTeam(fighter);

        TeamUtil team1 = Teams.get("Team1");
        TeamUtil team2 = Teams.get("Team2");
        String FighterName = Fighter.getName();
        Location fighterBea = (Location) args[1];
        Player player = sender.getServer().getPlayer(sender.getName());

        if (!FighterName.equals(team1.getName()) && !FighterName.equals(team2.getName())) {
            Util.sendMessage(player, ChatColor.RED + "指定されたチームは、戦闘チームに設定されていません。\n"
                    + "チーム名を確認して、もう一度試してください。"
            );
        } else if (FighterName.equals(team1.getName())) {
            team1.setBeacon(fighterBea);
            // 設定したリスポーン地点の座標をx,y,zに保存
            Integer x = fighterBea.getBlockX();
            Integer y = fighterBea.getBlockY();
            Integer z = fighterBea.getBlockZ();

            // クリックしたらTPする不思議なブロックを生成
            BaseComponent[] TP1 = Util.createBaseComponent("[Team1ビーコンへTP]",
                    "クリックでチーム1のビーコンへTP",
                    "/tp " + x + " " + y + " " + z);
            Util.sendMessage(player, Fighter.getColor() + "[" + Fighter.getName() + "]\n"
                    + ChatColor.GREEN + "のビーコンを設定しました。\n"
                    + "以下のブロックから設定場所にTPできます。");
            sender.sendMessage(TP1);
        } else if (FighterName.equals(team2.getName())) {
            team2.setBeacon(fighterBea);
            Integer x = fighterBea.getBlockX();
            Integer y = fighterBea.getBlockY();
            Integer z = fighterBea.getBlockZ();

            BaseComponent[] TP2 = Util.createBaseComponent("[Team2ビーコンへTP]",
                    "クリックでチーム2のビーコンへTP",
                    "/tp " + x + " " + y + " " + z);
            Util.sendMessage(player, Fighter.getColor() + "[" + Fighter.getName() + "]\n"
                    + ChatColor.GREEN + "のビーコンを設定しました。\n"
                    + "以下のブロックから設定場所にTPできます。");
            sender.sendMessage(TP2);
        } else {
            Util.sendMessage(player, ChatColor.RED + "エラーが発生しました。\n"
                    + "引数等を確認して、再度試してください。"
            );
        }

    }
    public void CheckSettings(CommandSender sender, Object[] args) {
        // 設定一覧を表示
        // チーム1のリス地へTP
        // チーム2のリス地へTP
        // ゲームスタートボタン
        TeamUtil team1 = Teams.get("Team1");
        TeamUtil team2 = Teams.get("Team2");
        Integer cannot = 0;
        Player player = sender.getServer().getPlayer(sender.getName());
        switch (system.getType()) {
            case "survival":
//                sender.sendMessage("サバイバル戦\n・時間制限なし\n・チケット0で終了\n・ビーコン有り");
                if (team1.getBeacon() == null || team2.getBeacon() == null) {
                    Util.sendMessage(player, ChatColor.RED + "ビーコンの場所が指定されてない");
                    cannot += 1;
                }
                if (team1.getRespawn() == null || team2.getRespawn() == null) {
                    Util.sendMessage(player, ChatColor.RED + "リスポーン地点が指定されていない");
                    cannot += 1;
                }

                break;
            case "king":
            case "simple":
//                sender.sendMessage("大将戦\n・時間制限なし\n・大将を殺して終了");
//                sender.sendMessage("シンプル戦\n・時間制限有り\n・大将を殺して終了");
                if (team1.getKing() == null || team2.getKing() == null) {
                    Util.sendMessage(player, ChatColor.RED + "大将が指定できてない");
                    cannot += 1;
                }
                if (team1.getRespawn() == null || team2.getRespawn() == null) {
                    Util.sendMessage(player, ChatColor.RED + "リスポーン地点が指定されていない");
                    cannot += 1;
                }

                break;
            default:
                Util.sendMessage(player, ChatColor.RED + "ゲームモードが指定されていません。\n'/siege GameRule <type>' で指定してください。");
                break;
        }
        if (cannot != 0) {
            return;
        } else {
            // ゲーム開始ボタンを作成
            BaseComponent[] starting;
            BaseComponent[] substarting;
            if (system.getType() == "survival") {
                starting = Util.createBaseComponent("[クリックで対戦を開始]",
                        "クリックで攻城戦を開始",
                        "/siege start 0");
            } else {
                substarting = Util.createBaseComponent("[クリックでA攻めの対戦を開始]",
                        "クリックで攻城戦を開始",
                        "/siege start 1");
                starting = Util.createBaseComponent("[クリックでB攻めの対戦を開始]",
                        "クリックで攻城戦を開始",
                        "/siege start 2");
                sender.sendMessage(substarting);
            }
            sender.sendMessage(starting);
        }

        String checking = CheckCanPlay();
        if (checking == "") {
            sender.sendMessage(ChatColor.AQUA + "[攻城戦支援プラグイン]");
            sender.sendMessage(GetTeam());
            sender.sendMessage(GetRes1());
            sender.sendMessage(GetRes2());
            switch (system.getType()) {
                case "survival":
                    sender.sendMessage(GetBea1());
                    sender.sendMessage(GetBea2());
                    break;
                case "king":
                case "simple":
                    sender.sendMessage(GetKing());
                    break;
                default:
                    break;
            }
        }

    }
    public BaseComponent[] GetTeam() {
        // チームのリストを返す
        BaseComponent[] TeamList = new ComponentBuilder(
                new TextComponent(new ComponentBuilder()
                        .append("\n- 戦闘チーム1 : Red"
                                + "\n- 戦闘チーム2 : Blue"
                                + "\n- 観覧チーム : Co"
                        ).color(ChatColor.GREEN)
                        .create()
                )
        ).create();
        return TeamList;
    }
    public BaseComponent[] GetKing() {
        // チームのリストを返す
        BaseComponent[] KingList = new ComponentBuilder(
                new TextComponent(new ComponentBuilder()
                        .append("\n- 赤チーム : " + Teams.get("Team1").getKing().getName()
                                + "\n- 青チーム : " + Teams.get("Team2").getKing().getName()
                        ).color(ChatColor.GREEN)
                        .create()
                )
        ).create();
        return KingList;
    }
    public BaseComponent[] GetRes1() {
        Location Spawn1 = Teams.get("Team1").getRespawn();
        String spa1 = Spawn1.getBlockX() + " " + Spawn1.getBlockY() + " " + Spawn1.getBlockZ();

        BaseComponent[] TPRes1 = Util.createBaseComponent("\n- チーム1のリス地 : " + spa1,
                "クリックでチーム1のリス地へTP",
                "/tp " + spa1);
        return TPRes1;
    }
    public BaseComponent[] GetRes2() {
        Location Spawn2 = Teams.get("Team2").getRespawn();
        String spa2 = Spawn2.getBlockX() + " " + Spawn2.getBlockY() + " " + Spawn2.getBlockZ();

        BaseComponent[] TPRes2 = Util.createBaseComponent("\n- チーム2のリス地 : " + spa2,
                "クリックでチーム2のリス地へTP",
                "/tp " + spa2);
        return TPRes2;
    }
    public BaseComponent[] GetBea1() {
        Location Beacon1 = Teams.get("Team1").getBeacon();
        String bea1 = Beacon1.getBlockX() + " " + Beacon1.getBlockY() + " " + Beacon1.getBlockZ();

        BaseComponent[] TPBea1 = Util.createBaseComponent("\n- チーム1のビーコン : " + bea1,
                "クリックでチーム1のビーコンへTP",
                "/tp " + bea1);
        return TPBea1;
    }
    public BaseComponent[] GetBea2() {
        Location Beacon2 = Teams.get("Team2").getBeacon();
        String bea2 = Beacon2.getBlockX() + " " + Beacon2.getBlockY() + " " + Beacon2.getBlockZ();

        BaseComponent[] TPBea2 = Util.createBaseComponent("\n- チーム2のビーコン : " + bea2,
                "クリックでチーム2のビーコンへTP",
                "/tp " + bea2);
        return TPBea2;
    }
    public void Simota(CommandSender sender, Object[] args) {
        if (Teams.get("Team1") == null || Teams.get("Team2") == null) {
            sender.sendMessage("チームが設定できてないよ");
            return;
        }

        Server server = sender.getServer();
        GameController.Count("マイクラ戦争");
        Collection<Player> players = (Collection<Player>) Bukkit.getOnlinePlayers();
        Random random = new Random();
        Team team = null;
        Scoreboard MainBoard = server.getScoreboardManager().getMainScoreboard();

        Integer num = random.nextInt(2);
            if (num == 0) {
                team = MainBoard.getTeam(Teams.get("Team1").getName());
            } else if (num == 1) {
                team = MainBoard.getTeam(Teams.get("Team2").getName());
            } else {
                return;
            }

        try {
            Util.setTitle("開始まで 5秒前", "マイクラ戦争が始まるよ", 100);
            Util.sendSound(Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTitle("開始まで 4秒", "マイクラ戦争が始まるよ", 100);
            Util.sendSound(Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTitle("開始まで 3秒", "マイクラ戦争が始まるよ", 100);
            Util.sendSound(Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTitle("開始まで 2秒", "マイクラ戦争が始まるよ", 100);
            Util.sendSound(Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTitle("開始まで 1秒", "マイクラ戦争が始まるよ", 100);
            Util.sendSound(Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTitle("ゲーム開始！", "50人マイクラ戦争", 100);
            Util.sendSound(Sound.BLOCK_ANVIL_PLACE);

            Thread.sleep(700);
            Util.setTitle(team.getColor() + team.getName() + ChatColor.WHITE + "チームの勝利", "ゲーム終了", 150);
            for(Player player : players) {
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE,0.1f,1);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
