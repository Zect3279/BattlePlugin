package com.zect.battleplugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

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
        if (block.getLocation != Beacon1 && block.getLocation != Beacon2) {
            event.getPlayer().sendMessage("これは壊すビーコンじゃないよ")
        }
        // ボスバーの処理
    }
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        // ゲーム開始待機中は動けない
        boolean Now = GameController.GamePlaying;
        if (!Now) {
            return;
        }
        event.setCancelled()
    }
}
