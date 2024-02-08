package net.xtitova.simbeacon.simulation;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
  private static final double MAX_RECEPTION_DISTANCE = 40;

  private int simulationDelay = 100;
  private ArrayList<Beacon> beacons = new ArrayList<>();
  private int lastAddedBeaconNumber = 0;

  private  boolean pauseRequested = false;

  private SimulationUpdateCallback updateCallback = null;

  public void addUpdateCallback(SimulationUpdateCallback callback) {
    this.updateCallback = callback;
  }


  public String addBeacon(double x, double y) {
    String beaconId = "B" + (++lastAddedBeaconNumber);

    if (getBeaconById(beaconId) != null) {
      throw new RuntimeException("Unexpected problem happened: beacon " + beaconId + " already exists");
    }

    beacons.add(new Beacon(beaconId, new Coord(x, y)));
    return beaconId;
  }

  public void deleteBeacon(String beaconId) {
    Beacon beacon = getBeaconById(beaconId);

    if (beacon == null) {
      throw new RuntimeException("Cannot delete beacon " + beaconId + " as it does not exist");
    }
    beacons.remove(beacon);
  }

  public List<Beacon> getBeacons() {
    return beacons;
  }

  public Beacon getBeaconById(String beaconId) {
    return beacons.stream()
        .filter(beacon -> beacon.getId().equals(beaconId))
        .findFirst()
        .orElse(null);
  }


  public void start() {
    pauseRequested = false;

    new Thread(() -> {
      while (!pauseRequested) {
        updateBeacons();

        try {
          Thread.sleep(simulationDelay);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();
  }

  public void pause() {
    pauseRequested = true;
  }
  public void stop() {

    pauseRequested = true;
  }

  private void updateBeacons() {
    beacons.forEach(Beacon::tick);

    for(Beacon beaconTransmition: beacons){
      if (beaconTransmition.getTrMode() == Beacon.Mode.TRANSMISSION){
        for(Beacon beaconReception: beacons){
          double distance = beaconReception.getPosition().distanceFrom(beaconTransmition.getPosition());

          if ((beaconReception.getTrMode() != Beacon.Mode.TRANSMISSION)
              && (distance < MAX_RECEPTION_DISTANCE))  {
            beaconReception.transmitMessage(beaconTransmition.requestMessage());
          }
        }
      }
    }

    if (updateCallback != null) {
      updateCallback.onUpdate();
    }
  }
}
