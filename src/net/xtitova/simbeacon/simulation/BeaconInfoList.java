package net.xtitova.simbeacon.simulation;

import java.util.LinkedList;
import java.util.List;

public class BeaconInfoList extends LinkedList<BeaconInfo> {
  private final String myId;

  public BeaconInfoList(String myId) {
    this.myId = myId;
  }

  public void updateBeaconList(List<BeaconInfo> newlyDetectedBeacons){
    for (BeaconInfo newBeacon : newlyDetectedBeacons) {
      String newId = newBeacon.getId();
      boolean isNew = true;
      for(BeaconInfo beacon : this){
        if(newId.equals(beacon.getId())){
          beacon.updateBeaconInfo(newBeacon);
          isNew = false;
          break;
        }
      }
      if(isNew){
        this.add(newBeacon);
      }
    }
  }

  public void updateTimeSinceLastReception(long timeSinceLastUpdate) {
    this.stream()
      .filter(beaconInfo -> !beaconInfo.getId().equals(myId))
      .forEach(beaconInfo -> beaconInfo.addTime(timeSinceLastUpdate));
  }
}
