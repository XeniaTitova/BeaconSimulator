import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BeaconMessage {

    private final String messagePattern = "Beacon (.*), (\\d+) tic ago, send message : (.*)";
    private List<BeaconInfo> detectedBeacons = new LinkedList<>();

    public void afficherElementsDeLaListe() {
        System.out.println("Éléments de la liste :");
        for (BeaconInfo element : detectedBeacons) {
            System.out.println(element.strCreation());
        }

    }
    public String createMessage(){
        String msg = "";
        for (BeaconInfo beacon : detectedBeacons) {
            msg += beacon.strCreation();
        }
        return  msg;
    }

    public void decodeMessage(String msg){
        try (Scanner scanner = new Scanner(msg)) {
            // Définir le délimiteur comme le début ou la fin de ligne
            scanner.useDelimiter("(\\n)");

            // Itérer sur chaque ligne
            while (scanner.hasNext()) {
                String ligne = scanner.next();
                System.out.println("Ligne : " + ligne);
                BeaconInfo newBeaconInfo = decodeLigne(ligne);
                updateBeaconList(newBeaconInfo);
            }
        }
    }

    private BeaconInfo decodeLigne(String msgLigne){
        Pattern regex = Pattern.compile(messagePattern);
        Matcher matcher = regex.matcher(msgLigne);

        // Récupérer les groupes capturés
        matcher.find();
        String id = matcher.group(1);
        long timeSinceReception = Long.parseLong(matcher.group(2));
        String message = matcher.group(3);

        // Afficher les résultats
        System.out.println("ID : " + id);
        System.out.println("Time Since Reception : " + timeSinceReception);
        System.out.println("Message : " + message);


        BeaconInfo decodedBeacon = new BeaconInfo(id, message, timeSinceReception);
        return decodedBeacon;
    }

    public void updateBeaconList(BeaconInfo newBeacon){
        String newId = newBeacon.getId();
        boolean isNew = true;
        for(BeaconInfo beacon : detectedBeacons){
            if(newId.equals(beacon.getId())){
                beacon.updateBeaconInfo(newBeacon);
                isNew = false;
                break;
            }
        }
        if(isNew){
            detectedBeacons.add(newBeacon);
        }
    }

}
