package net.xtitova.simbeacon.simulation;

public class BeaconInfo {
    private String id;
    private String message;
    private long timeSinceReception;

    public BeaconInfo(String id, String message, long timeSinceReception) {
        this.id = id;
        this.message = message;
        this.timeSinceReception = timeSinceReception;
    }

    public String strCreation(){
        return "net.xtitova.simbeacon.simulation.Beacon " + id + ", " + timeSinceReception + " tic ago, send message : " + message + "\n";
    }

    public void addTime(long time){
        timeSinceReception += time;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public long getTimeSinceReception() {
        return timeSinceReception;
    }

    public void updateBeaconInfo(BeaconInfo newBeaconInfo) {
        if(newBeaconInfo.getTimeSinceReception() < timeSinceReception) {
            message = newBeaconInfo.getMessage();
            timeSinceReception = newBeaconInfo.getTimeSinceReception();
        }
    }
}
