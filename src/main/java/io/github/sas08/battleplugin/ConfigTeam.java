package io.github.sas08.battleplugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class ConfigTeam<T> {
  
    private final String name;
    private Location respawn;
    private Location beacon;
    private Integer tickets;
    private Player king;
    private Integer times;
  
  
    public ConfigTeam(String TeamName) {
        this.name = TeamName;
    }
    

    public void setRespawn(Location Respawn) {
        this.respawn = Respawn;
    }

    public void setBeacon(Location Beacon) {
        this.beacon = Beacon;
    }

    public void setMaxTicket(Integer MaxTicket) {
        this.tickets = MaxTicket;
    }

    public void setKing(Player King) {
        this.king = King;
    }

    public void setTimes(Integer MaxTime) {
        this.times = MaxTime;
    }

  
    public String getName() {
        return name;
    }
  
    public Location getRespawn() {
        return respawn;
    }
  
    public Location getBeacon() {
        return beacon;
    }
  
    public Integer getTicket() {
        return tickets;
    }
  
    public Player getKing() {
        return king;
    }

    public Integer getTimes() {
        return times;
    }
  
    public void lostTicket() {
        tickets--;
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


    // 毎秒timeをへらす機構
}
