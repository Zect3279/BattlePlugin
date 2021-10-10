package io.github.sas08.battleplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamUtil {

    private static class ItemData {
        // 最後にのったブロックをメモっておく
        private ItemStack item;
        private Integer amount ;

        ItemData(ItemStack itemData, Integer number) {
            item = itemData;
            amount = number;
        }
    }

    // チーム名
    private final String name;

    // チーム色
    private final ChatColor color;

    // リスポーン地点
    private Location respawn;

    // ビーコン地点
    private Location beacon;

    // ビーコン数
    private Integer beaconNum = 20;

    // チケット数
    private Integer tickets = 200;

    // 大将
    private Player king;

    // 配布アイテムリスト
    private List<ItemData> items;

    // Coと一般を分ける
    private boolean op;


    public TeamUtil(String TeamName, ChatColor Color) {
        this.name = TeamName;
        this.color = Color;
    }


    public void addItem(ItemStack item, Integer number) {
        this.items.add(new ItemData(item, number));
    }

    public void removeItem(Integer index) {
        this.items.remove(index);
    }


    public void setRespawn(Location Respawn) {
        this.respawn = Respawn;
    }

    public void setBeacon(Location Beacon) {
        this.beacon = Beacon;
    }

    public void setBeaconNum(Integer MaxBeaconHP) {
        this.beaconNum = MaxBeaconHP;
    }

    public void setMaxTicket(Integer MaxTicket) {
        this.tickets = MaxTicket;
    }

    public void setKing(Player King) {
        this.king = King;
    }

    public void setOp(boolean Op) {
        this.op = Op;
    }


    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }
  
    public Location getRespawn() {
        return respawn;
    }
  
    public Location getBeacon() {
        return beacon;
    }

    public Integer getBeaconNum() {
        return beaconNum;
    }
  
    public Integer getTicket() {
        return tickets;
    }
  
    public Player getKing() {
        return king;
    }

    public boolean getOp() {
        return op;
    }


    public void lostTicket() {
        tickets--;
    }

    public List<ItemData> getItems() {
        return items;
    }

    public List<Player> getMember() {
        List<Player> MemberList = new ArrayList<Player>();
        Scoreboard score = Bukkit.getServer().getScoreboardManager().getMainScoreboard();
        Bukkit.getOnlinePlayers().forEach(player -> {
            Team team = score.getPlayerTeam(player);
            if (team.getName().equals(this.getName())) {
                MemberList.add(player);
            }
        });
        return MemberList;
    }


}
