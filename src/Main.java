import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final int NUMBER_OF_BEACONS = 2;
    private static volatile boolean stopRequested = false;// TODO moche!

    public static void main(String[] args) {
//        List<Beacon> myBeacons1 = new List<>(); // QQ pourquoi ne marche pas?

        BeaconMessage a = new BeaconMessage();
//        a.decodeMessage("Beacon ABC123, 42 tic ago, send message : Hello World!\nBeacon 007, 33 tic ago, send message : Hi!!!");
        // Beacon creation / initalisation
        ArrayList<Beacon> myBeacons = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_BEACONS; i++) {
            myBeacons.add(new Beacon("" +  i, 1000, 100, 3));
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
        for(Beacon beacon: beacons){
            beacon.tick();
        }
        beacons.get(0).sendMessage("AbraCadabra");
    }

}