package net.xtitova.simbeacon.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BeaconMessage {

    private static final String MESSAGE_PATTERN = "Beacon (.*), (\\d+) tic ago, send message : (.*)";

    public static String encodeMessage(List<BeaconInfo> beaconInfos) {
        String msg = "";

        for (BeaconInfo beacon : beaconInfos) {
            msg += beacon.strCreation();
        }
        return  msg;
    }

    public static List<BeaconInfo> decodeMessage(String msg){
        List<BeaconInfo> beaconInfos = new ArrayList<>();

        try (Scanner scanner = new Scanner(msg)) {
            // Définir le délimiteur comme le début ou la fin de ligne
            scanner.useDelimiter("(\\n)");

            // Itérer sur chaque ligne
            while (scanner.hasNext()) {
                String ligne = scanner.next();
//                System.out.println("Ligne : " + ligne);
                beaconInfos.add(decodeLigne(ligne));
            }
        }
        return beaconInfos;
    }

    private static BeaconInfo decodeLigne(String msgLigne){
        Pattern regex = Pattern.compile(MESSAGE_PATTERN);
        Matcher matcher = regex.matcher(msgLigne);

        // Récupérer les groupes capturés
        matcher.find();
        String id = matcher.group(1);
        long timeSinceReception = Long.parseLong(matcher.group(2));
        String message = matcher.group(3);

        // Afficher les résultats
//        System.out.println("ID : " + id);
//        System.out.println("Time Since Reception : " + timeSinceReception);
//        System.out.println("Message : " + message);

        return new BeaconInfo(id, message, timeSinceReception);
    }


}
