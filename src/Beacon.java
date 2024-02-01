import java.util.List;

public class Beacon {
    public enum Mode {
        TRANSMISSION,
        RECEPTION
    }

    private String id;

    private String message = "";
    private Coord position = new Coord(0, 0);
    private BeaconMessage beaconMessage = new BeaconMessage();
    private long txPeriod;
    private long rxtxDuration;
    private long rxDelays;
    private long internalTime = (long) (Math.random() * 100);
    private Mode trMode = Mode.RECEPTION;

    private  boolean receptionInProcess = false;

    private long reseptionCount = 0;

    private String receivedMessage = "";

//    private List<BeaconTransmission> transmissions;

    public Beacon(String id, long txPeriod, long rxtxDuration, long rxDelays) {
        this.id = id;
        this.txPeriod = txPeriod;
        this.rxtxDuration = rxtxDuration;
        this.rxDelays = rxDelays;
        beaconMessage.updateBeaconList(new BeaconInfo(id,message,0));
        System.out.println(internalTime);
    }
    public Mode getTrMode() {
        return trMode;
    }

    public boolean isReceiving() {
        return trMode == Mode.RECEPTION;
    }

    public boolean isTransmitting() {
        return trMode == Mode.TRANSMISSION;
    }

    public void tick() {
        internalTime ++ ;
//        System.out.println("The internal time of beacon " + id + " is " + internalTime);

        //  At the start of each period, information is transmitted, then the beacon receives the information.
        if (internalTime == rxtxDuration){ trMode = Mode.RECEPTION;
            System.out.println("The internal time of beacon " + id + " is in RECEPTION mode");
        }
        if (internalTime >= txPeriod && !receptionInProcess){
            internalTime = 0;
            trMode = Mode.TRANSMISSION;
            receivedMessage = "";
            System.out.println("The internal time of beacon " + id + " is in TRANSMISSION mode");
        }
    }

    public void transmitMessage(String msg){
        if (!msg.equals(receivedMessage)){
            if (!receivedMessage.isEmpty() && reseptionCount != 0){
                System.out.println("The message : '" + receivedMessage + "' could not be received correctly");
            }
            receivedMessage = msg;
            reseptionCount = 0;
        } else {
            reseptionCount ++;
            if(reseptionCount == rxDelays){
                // the message has been detected, the beacon will wait for the end of the transmission before switching to transmission mode.
                receptionInProcess = true;
            }
            if(reseptionCount == rxtxDuration){
                System.out.println("The message : '" + receivedMessage + "' has been received");
                receptionInProcess = false;
                beaconMessage.decodeMessage(receivedMessage);
                receivedMessage = "";
            }
        }

    }

    public String requestMessage(){
        return beaconMessage.createMessage();
    }

}
