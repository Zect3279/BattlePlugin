package io.github.sas08.battleplugin;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventListener implements Listener {


    // プレイヤーが死んだらインスタントが呼び出される
    @EventHandler
    public void onPlayerDead(PlayerDeathEvent event) {
        boolean Now = GameController.GamePlaying;
        if (!Now) {
            return;
        }
        if (event.getEntity() instanceof Player) {
            // 死んだ人の処理
        }

        if (event.getEntity().getKiller() instanceof Player) {
            // 殺した人の処理
        }
    }
    @EventHandler
    public void makeSpec(PlayerDeathEvent event) {
        Boolean Spec = BattlePlugin.system.getSpec();
        if (!Spec) {
            return;
        }
        Player player = event.getEntity();
        player.setGameMode(GameMode.SPECTATOR);
    }
//    @EventHandler
//    public void beaconCoter(PlayerDeathEvent event) {
//        Boolean Now = GameController.GamePlaying;
//        if (!Now) {
//            return;
//        }
//        String rule = Util.GameType;
//        if (rule != "survival") {
//            return;
//        }
//        // チケット処理
//        Util.ticketCounter(event);
//    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // 攻城戦中に入ってくたら、攻城戦に強制参加
        // チームに参加していなかったら、人数が少ない方のチームに入れる
        // チームリス地に強制的にTPする
        boolean Now = GameController.GamePlaying;
        if (!Now) {
            return;
        }
        /*
         * 『あなたは、[チーム名] に所属しました。』
         * 『後5秒でリス地にTPします。』
         * 『3』
         * 『2』
         * 『1』
         * 『TP完了』
         */
    }
    Integer BrokenCounter = 0;
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        boolean Now = GameController.GamePlaying;
        if (!Now) {
            return;
        }
        Block block = event.getBlock();
        if (block.getType() != Material.BEACON) {
            return;
        }

//        if (block.getLocation() != Beacon1 && block.getLocation() != Beacon2) {
//            event.getPlayer().sendMessage("これは壊すビーコンじゃないよ");
//        }

        event.getPlayer().sendMessage("ビーコン壊したよ");
        // 破壊を無かったことに
        event.setCancelled(true);

        // カウンター処理
        BrokenCounter += 1;
        String Counter = String.valueOf(BrokenCounter);
        event.getPlayer().sendTitle(Counter, "");

        // ボスバーの処理
    }
}
