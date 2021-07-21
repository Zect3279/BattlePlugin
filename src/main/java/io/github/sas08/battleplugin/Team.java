
public class ConfigTeam<T> {
  
    private final String name;
    private final Location respawn;
    private final Location beacon;
    private final Integer ticket;
    private final Player king;
    private final Integer time;
  
  
    public ConfigTeam(String TeamName, Location RespawnPoint, Location BeaconPoint, Integer MaxTicket, Player King, Integer MaxTime) {
        this.name = TeamName;
        this.respawn = RespawnPoint;
        this.beacon = BeaconPoint;
        this.ticket = MaxTicket;
        this.king = King;
        this.time = MaxTime
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
        return ticket;
    }
  
    public Player getKing() {
        return king;
    }
  
  
    public Integer lostTicket() {
        ticket--;
    }
  
    public List<Player> getMember() {
        List<Player> MemberList = new ArrayList<Player>();
        Scoreboard score = Bukkit.getServer().getScoreboardManager().getMainScoreboard();
        Bukkit.getOnlinePlayers().forEach(player -> {
            Team team = score.getPlayerTeam(player);
            if (team.getName() == this.getame()) {
                MemberList.add(player);
            }
        });
        return MemberList
    }
    
    // 毎秒timeをへらす機構
}
