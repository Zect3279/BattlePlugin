package com.zect.battleplugin;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
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
        List<Argument> gameruleArgument = new ArrayList<>();
        String[] ruleList = new String[] {"survival", "simple"};
        gameruleArgument.add(new StringArgument("Rule").overrideSuggestions(ruleList));

        // コマンドを設定する
        new CommandAPICommand("siege")
                .withSubcommand(new CommandAPICommand("title")
                        .executes(this::TitleCall)
                )
                .withSubcommand(new CommandAPICommand("start")
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
                .withSubcommand(new CommandAPICommand("GameRule")
                        .withArguments(gameruleArgument)
                        .executes(this::SetGameRule)
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
                .register();

    }


    private void toCount(CommandSender sender, Object[] args) {
        Server server = sender.getServer();
        GameController.Count(server);
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

    // リスポーン地点を保存
    Map<String, Location> TeamRes = new HashMap<>();

    // <Team, Team.getName()> チーム番号と名前を保存
    Map<String, String> TeamName = new HashMap<>();

    // ビーコンの位置を設定（サバイバルのみ使用）
    Map<String, Location> Beacon = new HashMap<>();

    // デフォルトの秒数を300秒に
    public Integer timeLimit = 300;

    String gameType = null;


    public void SetGameRule(CommandSender sender, Object[] args) {
        String rule = (String) args[0];
        sender.sendMessage(rule);
        switch (rule) {
            case "survival":
                sender.sendMessage("サバイバル戦");
                gameType = "survival";
                break;
            case "king":
                sender.sendMessage("大将戦");
                gameType = "king";
            default:
                sender.sendMessage("エラーが発生");
                break;
        }
    }
    private void BeaconPosition(CommandSender sender, Object[] args) {
        // スポーン地点設定
        String fighter = (String) args[0];
        Server server = sender.getServer();
        Team Fighter = server.getScoreboardManager().getMainScoreboard().getTeam(fighter);

        String Team1N = TeamName.get("Team1");
        String Team2N = TeamName.get("Team2");
        String FighterName = Fighter.getName();
        Location fighterBea = (Location) args[1];

        if (!FighterName.equals(Team1N) && !FighterName.equals(Team2N)) {
            sender.sendMessage(ChatColor.AQUA + "[攻城戦支援プラグイン]\n"
                    + ChatColor.RED + "指定されたチームは、戦闘チームに設定されていません。\n"
                    + "チーム名を確認して、もう一度試してください。"
            );
        } else if (FighterName.equals(Team1N)) {
            Beacon.put("Team1", fighterBea);
            // 設定したリスポーン地点の座標をx,y,zに保存
            Integer x = fighterBea.getBlockX();
            Integer y = fighterBea.getBlockY();
            Integer z = fighterBea.getBlockZ();

            // クリックしたらTPする不思議なブロックを生成
            BaseComponent[] TP1 = new ComponentBuilder(
                    new TextComponent(new ComponentBuilder()
                            .append("[Team1ビーコンへTP]").color(ChatColor.GOLD)
                            .create())
            )
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder()
                            .append("クリックでチーム1のビーコンへTP")
                            .create()
                    ))
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + x + " " + y + " " + z))
                    .create();
            sender.sendMessage(ChatColor.AQUA + "[攻城戦支援プラグイン]\n"
                    + Fighter.getColor() + "[" + Fighter.getName() + "]\n"
                    + ChatColor.GREEN + "のビーコンを設定しました。\n以下のブロックから設定場所にTPできます。"
            );
            sender.sendMessage(TP1);
        } else if (FighterName.equals(Team2N)) {
            Beacon.put("Team2", fighterBea);
            Integer x = fighterBea.getBlockX();
            Integer y = fighterBea.getBlockY();
            Integer z = fighterBea.getBlockZ();
            BaseComponent[] TP2 = new ComponentBuilder(
                    new TextComponent(new ComponentBuilder()
                            .append("[Team2ビーコンへTP]").color(ChatColor.GOLD)
                            .create())
            )
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder()
                            .append("クリックでチーム2のビーコンへTP")
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
    public void TitleCall(CommandSender sender, Object[] args) {
        Util.setTitle("マイクラ戦争プラグイン", "企画:KUN(?) 制作:Zect 命名:nori", 500);
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

        }
        // ゲーム開始
        sender.sendMessage("ゲームを開始");
        
        Server server = sender.getServer();
        Scoreboard MainBoard = server.getScoreboardManager().getMainScoreboard();

        // チーム割当
        GiveTeam(sender, args);
        
        /* 引数
        * - [x] 戦闘チーム
        * - [x] 観覧チーム
        * - [x] リス地
        * - [x] 範囲
        * - [x] 制限時間
        * - [x] スコアボードobj
        * - [x] サーバーobj
        */
        GameController.start(server, MainBoard, TeamName, TeamRes, timeLimit);
    }
    public void GiveTeam(CommandSender sender, Object[] args) {
        // チームに所属させる
        // チームカラーを取得して、その色で
        // [0 - 0]
        // みたいに表示する
        Random random = new Random();
        // チーム作成
        AddTeams(sender);

        Server server = sender.getServer();
        Scoreboard score = server.getScoreboardManager().getMainScoreboard();
        String Team1 = TeamName.get("Team1");
        String Team2 = TeamName.get("Team2");

        String size1 = "0";
        String size2 = "0";

        try {
            Util.setTitle(size1 + " - " + size2, "チーム分けを開始", 100);

            Thread.sleep(200);

            Team team1 = score.getTeam(TeamName.get("Team1"));
            Team team2 = score.getTeam(TeamName.get("Team2"));
            Collection<? extends Player> players = server.getOnlinePlayers();

            for (Player player : players) {
                Integer num = random.nextInt(2);
                Team team = score.getPlayerTeam(player);
                if (team == null) {
                    if (num == 0) {
                        team1.addPlayer(player);
                    } else if (num == 1) {
                        team2.addPlayer(player);
                    } else {
                        return;
                    }
                } else {
                    if (team.getName() == TeamName.get("Team1") || team.getName() == TeamName.get("Team2") || team.getName() == TeamName.get("Team3")) {
                        return;
                    } else if (Team1 == null || Team2 == null) {
                        return;
                    } else {
                        if (team.getName() == TeamName.get("Team1")) {
                            team1.removePlayer(player);
                        }
                        if (team.getName() == TeamName.get("Team2")) {
                            team2.removePlayer(player);
                        }

                        if (num == 0) {
                            team1.addPlayer(player);
                        } else if (num == 1) {
                            team2.addPlayer(player);
                        } else {
                            return;
                        }
                    }
                }
                size1 = String.valueOf(team1.getSize());
                size2 = String.valueOf(team2.getSize());
                Util.setTitle(team1.getColor() + size1 + ChatColor.WHITE + " - " + team2.getColor() + size2, "チーム分け中", 100);
            }
            Thread.sleep(200);

            Util.setTitle(team1.getColor() + size1 + ChatColor.WHITE + " - " + team2.getColor() + size2, "チーム分け完了", 100);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void AddTeams(CommandSender sender) {
        // チームを作成・登録

        // コマンド入力時に取得したチーム名からチームオブジェクトを持ってくる
        Server server = sender.getServer();
        Scoreboard Board = server.getScoreboardManager().getMainScoreboard();

        Team team1 = Board.registerNewTeam("Red");
        Team team2 = Board.registerNewTeam("Blue");
        Team team3 = Board.registerNewTeam("Co");

        team1.setColor(org.bukkit.ChatColor.RED);
        team2.setColor(org.bukkit.ChatColor.BLUE);
        team3.setColor(org.bukkit.ChatColor.AQUA);

        // チーム名を追加
        // 1,2は戦闘、3は観覧
        TeamName.put("Team1", team1.getName());
        TeamName.put("Team2", team2.getName());
        TeamName.put("Team3", team3.getName());

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

        String Team1 = TeamName.get("Team1");
        String Team2 = TeamName.get("Team2");
        Location Spawn1 = TeamRes.get("Team1");
        Location Spawn2 = TeamRes.get("Team2");
        
        String cantStart = "";
        Integer can = 0;
        
        // 開始条件のどれか一つでも無かったら、開始できない
        if (Team1 == null || Team2 == null || Spawn1 == null || Spawn2 == null) {
            // 上のどれかがnullだったら実行
            if (Team1 == null || Team2 == null) {
                cantStart += "\n- 2つの戦闘チーム";
                can += 1;
            }
            if (Spawn1 == null || Spawn2 == null) {
                cantStart += "\n- 2つのスポーン地点";
                can += 1;
            }
        }
        if (can == 0) {
            return null;
        } else {
            return cantStart;
        }
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
    public void ShowFighters(CommandSender sender, Object[] args) {
        // チーム一覧を表示
        // 設定されてなかったら、nullが返る
        sender.sendMessage(ChatColor.AQUA + "[攻城戦支援プラグイン]"
                + ChatColor.GREEN
                + "\n戦闘チーム1 : " + TeamName.get("Team1")
                + "\n戦闘チーム2 : " + TeamName.get("Team2")
        );
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
    public void CheckSettings(CommandSender sender, Object[] args) {
        // 設定一覧を表示
        // チーム1のリス地へTP
        // チーム2のリス地へTP
        // ゲームスタートボタン
        BaseComponent[] starting;
        List<BaseComponent[]> check = SettingList(sender);
        String checking = CheckCanPlay();
        if (checking == null) {
            starting = new ComponentBuilder(
                    new TextComponent(new ComponentBuilder()
                            .append("[クリックで対戦を開始]").color(ChatColor.GOLD)
                            .create())
            )
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder()
                            .append("クリックで攻城戦を開始")
                            .create()
                    ))
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/siege start"))
                    .create();

            for (int i = 0; i >= check.size(); i++){
                sender.sendMessage(check.get(i));
            }
            sender.sendMessage("\n\n");
            sender.sendMessage(starting);
        }
    }
    public List<BaseComponent[]> SettingList(CommandSender sender) {
        Server server = sender.getServer();
        Scoreboard MainBoard = server.getScoreboardManager().getMainScoreboard();
        String Team1 = TeamName.get("Team1");
        String Team2 = TeamName.get("Team2");
        String Team3 = TeamName.get("Team3");
        Location Spawn1 = TeamRes.get("Team1");
        Location Spawn2 = TeamRes.get("Team2");
        // 設定一覧を作成して、返す
        BaseComponent[] TeamList = new ComponentBuilder(
                new TextComponent(new ComponentBuilder()
                    .append(
                          "\n- 戦闘チーム1 : " + Team1
                        + "\n- 戦闘チーム2 : " + Team2
                        + "\n- 観覧チーム : " + Team3
                    ).color(ChatColor.GREEN)
                    .create()
                )
        ).create();
        // 設定したリスポーン地点の座標をx,y,zに保存
        String spa1 = Spawn1.getBlockX() + " " + Spawn1.getBlockY() + " " + Spawn1.getBlockZ();
        String spa2 = Spawn2.getBlockX() + " " + Spawn2.getBlockY() + " " + Spawn2.getBlockZ();

        // クリックしたらTPする不思議なブロックを生成
        BaseComponent[] TP1 = new ComponentBuilder(
                new TextComponent(new ComponentBuilder()
                        .append("\n- チーム1のリス地 : " + spa1).color(ChatColor.GREEN)
                        .create())
        )
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder()
                        .append("クリックでチーム1のリス地へTP")
                        .create()
                ))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + spa1))
                .create();
        BaseComponent[] TP2 = new ComponentBuilder(
                new TextComponent(new ComponentBuilder()
                        .append("\n- チーム2のリス地 : " + spa2).color(ChatColor.GREEN)
                        .create())
        )
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder()
                        .append("クリックでチーム2のリス地へTP")
                        .create()
                ))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + spa2))
                .create();
        
        // 全部を結合

        return Arrays.asList(TeamList,TP1,TP2);
    }
    public void Simota(CommandSender sender, Object[] args) {
        if (TeamName.get("Team1") == null || TeamName.get("Team2") == null) {
            sender.sendMessage("チームが設定できてないよ");
        }

        Server server = sender.getServer();
        GameController.Count(server);
        Collection<Player> players = (Collection<Player>) Bukkit.getOnlinePlayers();
        Random random = new Random();
        Team team = null;
        Scoreboard MainBoard = server.getScoreboardManager().getMainScoreboard();

        Integer num = random.nextInt(2);
            if (num == 0) {
                team = MainBoard.getTeam(TeamName.get("Team1"));
            } else if (num == 1) {
                team = MainBoard.getTeam(TeamName.get("Team2"));
            } else {
                return;
            }

        try {
            Util.setTitle("開始まで 5秒前", "マイクラ戦争が始まるよ", 100);
            Util.sendSound(players, Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTitle("開始まで 4秒", "マイクラ戦争が始まるよ", 100);
            Util.sendSound(players, Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTitle("開始まで 3秒", "マイクラ戦争が始まるよ", 100);
            Util.sendSound(players, Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTitle("開始まで 2秒", "マイクラ戦争が始まるよ", 100);
            Util.sendSound(players, Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTitle("開始まで 1秒", "マイクラ戦争が始まるよ", 100);
            Util.sendSound(players, Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            Thread.sleep(1000);

            Util.setTitle("ゲーム開始！", "50人マイクラ戦争", 100);
            Util.sendSound(players, Sound.BLOCK_ANVIL_PLACE);

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
