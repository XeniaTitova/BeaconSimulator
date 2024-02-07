import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final List<Coord> COORDS = List.of(
      new Coord(0, 3),
      new Coord(2, 2),
      new Coord(2, -2),
      new Coord(-2, -2),
      new Coord(-2, 2)
    );

    private static final double MAX_RECEPTION_DISTANCE = 5;
    private static final int NUMBER_OF_BEACONS = 5;
    private static boolean stopRequested = false;// TODO moche!

    public static void main(String[] args) {
//        List<Beacon> myBeacons1 = new List<>(); // QQ pourquoi ne marche pas?

        BeaconMessage a = new BeaconMessage();
//        a.decodeMessage("Beacon ABC123, 42 tic ago, send message : Hello World!\nBeacon 007, 33 tic ago, send message : Hi!!!");
        // Beacon creation / initalisation
        ArrayList<Beacon> myBeacons = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_BEACONS; i++) {
            myBeacons.add(new Beacon("" +  i, COORDS.get(i)));
        }

        //Endless loop (until a key is pressed)
        Thread actionsThread = new Thread(() -> {
            while (!stopRequested) {
                updateBeacons(myBeacons); // QQ ca se fait?

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Start action thread
        actionsThread.start();

        // Wait for key press to stop program
        System.out.println("Press a key to stop simulation");
        new java.util.Scanner(System.in).nextLine();
        stopRequested = true;

        // Wait for the action thread to end
        try {
            actionsThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Programme arrêté.");
    }
    private static void updateBeacons(ArrayList<Beacon> beacons){
        beacons.forEach(Beacon::tick);

        for(Beacon beaconTransmition: beacons){
            if (beaconTransmition.getTrMode() == Beacon.Mode.TRANSMISSION){
                for(Beacon beaconReception: beacons){
                    double distance = beaconReception.getPosition().distanceFrom(beaconTransmition.getPosition());

//                    System.out.println("Distance from " + beaconReception.getId() + " to "
//                        + beaconTransmition.getId() + " is " + distance);
                    if ((beaconReception.getTrMode() == Beacon.Mode.RECEPTION)
                      && (distance < MAX_RECEPTION_DISTANCE))  { //TODO distance check
                        beaconReception.transmitMessage(beaconTransmition.requestMessage());
                    }
                }
            }
        }
//        beacons.get(0).transmitMessage("Beacon ABC123, 42 tic ago, send message : Hello World!\nBeacon 007, 33 tic ago, send message : Hi!!!");

        System.out.println(beacons.get(0).requestMessage());
    }

}