public class Beacon {
    public enum Mode {
        TRANSMISSION,
        RECEPTION
    }

    private String id;

    private String message = "";
    private Coord position = new Coord(0, 0);
//    private BeaconMessage beaconMessage = new BeaconMessage();

    private long txPeriod = 100;
    private long rxtxDuration = 10;
    private long rxDelays = 1;
    private long timeSinceLastUpdate = 0;
    private long cycleTime = (long) (Math.random() * 100);
    private Mode trMode = Mode.RECEPTION;

    private  boolean receptionInProcess = false;

    private long receptionCount = 0;

    private  String receivedMessage = "";

//    private List<BeaconTransmission> transmissions;

    private BeaconInfoList detectedBeacons;

    public Beacon(String id, Coord coord) {
        this.id = id;
        this.position = new Coord(coord.getX(), coord.getY());
        this.detectedBeacons = new BeaconInfoList(id);
        detectedBeacons.add(new BeaconInfo(id,message,0));  // Add ouorselves to the list
        System.out.println(cycleTime);
    }
    public Mode getTrMode() {
        return trMode;
    }

    public Coord getPosition() {
        return position;
    }

    public void setPosition(Coord position) {
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void tick() {
        timeSinceLastUpdate++;

        if (!receptionInProcess){
            cycleTime++;
        }
//        System.out.println("The internal time of beacon " + id + " is " + internalTime);

        //  At the start of each period, information is transmitted, then the beacon receives the information.
        if (cycleTime == rxtxDuration){ trMode = Mode.RECEPTION;
            System.out.println("The internal time of beacon " + id + " is in RECEPTION mode");
        }
        if (cycleTime >= txPeriod && !receptionInProcess){
            cycleTime = 0;
            trMode = Mode.TRANSMISSION;
            receivedMessage = "";
            System.out.println("The internal time of beacon " + id + " is in TRANSMISSION mode");
        }
    }

    public void transmitMessage(String msg){
        if (!msg.equals(receivedMessage)){
            if (!receivedMessage.isEmpty() && receptionCount != 0){
                System.out.println(id + ": The message : '" + receivedMessage + "' could not be received correctly");
            }
            receivedMessage = msg;
            receptionCount = 0;
        } else {
            receptionCount++;
            if(receptionCount == rxDelays){
                // the message has been detected, the beacon will wait for the end of the transmission before switching to transmission mode.
                receptionInProcess = true;
            }
            if(receptionCount == rxtxDuration){
                System.out.println(id + ": The message : '" + receivedMessage + "' has been received");
                receptionInProcess = false;
                detectedBeacons.updateTimeSinceLastReception(timeSinceLastUpdate);
                timeSinceLastUpdate = 0;
                detectedBeacons.updateBeaconList(BeaconMessage.decodeMessage(msg));
                receivedMessage = "";
            }
        }

    }

    public String requestMessage(){
        detectedBeacons.updateTimeSinceLastReception(timeSinceLastUpdate);
        timeSinceLastUpdate = 0;
        return BeaconMessage.encodeMessage(detectedBeacons);
    }

}
