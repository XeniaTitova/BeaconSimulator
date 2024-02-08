package net.xtitova.simbeacon.simulation;
import net.xtitova.simbeacon.logfile.FileHandler;

public class Beacon {
    public enum Mode {
        TRANSMISSION,
        WAITING_RECEPTION,
        RECEPTION_IN_PROCESS
    }

    private String id;
    private String logFileName;
    private String message = "";
    private Coord position = new Coord(0, 0);
//    private net.xtitova.simbeacon.simulation.BeaconMessage beaconMessage = new net.xtitova.simbeacon.simulation.BeaconMessage();

    private long txPeriod = 100;
    private long rxtxDuration = 10;
    private long rxDelays = 3;
    private long timeSinceLastUpdate = 0;
    private long cycleTime = (long) (Math.random() * 100);
    private Mode trMode = Mode.WAITING_RECEPTION;
//    private  boolean receptionInProcess = false;

    private long receptionCount = 0;

    private  String receivedMessage = "";


    private BeaconInfoList detectedBeacons;

    public Beacon(String id, Coord coord) {
        this.id = id;
        this.position = new Coord(coord.getX(), coord.getY());
        this.detectedBeacons = new BeaconInfoList(id);
        logFileName = id + "_log.txt";
        detectedBeacons.add(new BeaconInfo(id, message,0));  // Add ouorselves to the list
        FileHandler.writeToFile(logFileName, "Beacon " + id + " a été créé \t inital cycleTime = " + cycleTime);
//        System.out.println(cycleTime);
    }
    public Mode getTrMode() {
        return trMode;
    }

    public boolean isTransimssion() {
        return trMode == Mode.TRANSMISSION;
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

    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }

    public void tick() {
        timeSinceLastUpdate++;
        if (trMode != Mode.RECEPTION_IN_PROCESS){
            cycleTime++;
        }

        //  At the start of each period, information is transmitted, then the beacon receives the information.
        if (cycleTime == rxtxDuration){
            trMode = Mode.WAITING_RECEPTION;
            FileHandler.writeToFile(logFileName, "Passage en mode ATTANTE DE RECEPTION");
//            System.out.println("The internal time of beacon " + id + " is in RECEPTION mode");
        }
        if (cycleTime >= txPeriod){
            cycleTime = 0;
            trMode = Mode.TRANSMISSION;
            FileHandler.writeToFile(logFileName, "Passage en mode TRANSMISSION");
            detectedBeacons.updateTimeSinceLastReception(timeSinceLastUpdate);
            timeSinceLastUpdate = 0;
            receivedMessage = "";
//            System.out.println("The internal time of beacon " + id + " is in TRANSMISSION mode");
        }
    }

    public void transmitMessage(String msg){
        FileHandler.writeToFile(logFileName, "Message recu :\n" + msg);
        if (!msg.equals(receivedMessage)){
            if (!receivedMessage.isEmpty() && receptionCount != 0){
//                System.out.println(id + ": The message : '" + receivedMessage + "' could not be received correctly");
                FileHandler.writeToFile(logFileName, "Resseption echoué. Message :\n" + msg);
            }
            receivedMessage = msg;
            receptionCount = 0;
        } else {
            if(receptionCount == rxDelays){
                // the message has been detected, the beacon will wait for the end of the transmission before switching to transmission mode.
                trMode = Mode.RECEPTION_IN_PROCESS;
                FileHandler.writeToFile(logFileName, "Passage en mode RESSEPTION DE MESSAGE");
            }
            if(receptionCount >= rxtxDuration-1){
                FileHandler.writeToFile(logFileName, "Message recu avec sucess. Message :\n" + msg);
//                System.out.println(id + ": The message : '" + receivedMessage + "' has been received");
                trMode = Mode.WAITING_RECEPTION;
                detectedBeacons.updateTimeSinceLastReception(timeSinceLastUpdate);
                timeSinceLastUpdate = 0;
                detectedBeacons.updateBeaconList(BeaconMessage.decodeMessage(msg));
                receivedMessage = "";

                FileHandler.writeToFile(logFileName, "Mise à jour de la liste des beacon :\n" + BeaconMessage.encodeMessage(detectedBeacons));
            }
        }
        receptionCount++;
    }

    public String requestMessage(){
        FileHandler.writeToFile(logFileName, "Message envoyer");
                return BeaconMessage.encodeMessage(detectedBeacons);
    }

}
