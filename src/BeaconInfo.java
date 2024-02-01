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
        return "Beacon " + id + ", " + timeSinceReception + " tic ago, send message : " + message;
    }

    public void addTime(long time){
        timeSinceReception += time;
    }
}
