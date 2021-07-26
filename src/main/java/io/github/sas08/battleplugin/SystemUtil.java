package io.github.sas08.battleplugin;

public class SystemUtil<T> {

    //    // デフォルトの秒数を300秒に
//    Integer timeLimit = 300;
//

    // 処理に使うためのゲームタイプを用意
    public String gameType;

    // 死んだらスペクテーターにするかの設定
    public Boolean DoSpectator;

    // 制限時間
    private Integer times;
    
    // チケット上限
    private Integer ticket;


    public void setTimes(Integer MaxTime) {
        this.times = MaxTime;
    }

    public void setSpec(Boolean DoSpec) {
        this.DoSpectator = DoSpec;
    }

    public void setType(String GameType) {
        this.gameType = GameType;
    }

    public void setTicket(Integer MaxTicket) {
        this.ticket = MaxTicket;
    }
    

    public Integer getTimes() {
        return times;
    }

    public Boolean getSpec() {
        return DoSpectator;
    }

    public String getType() {
        return gameType;
    }
    
    public Integer getTicket() {
        return ticket;
    }


    // 毎秒timeをへらす機構
}
