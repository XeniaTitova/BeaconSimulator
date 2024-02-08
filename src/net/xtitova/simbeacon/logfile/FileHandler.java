package net.xtitova.simbeacon.logfile;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.time.LocalDateTime;
//System.out.println("Current Time: " + LocalDateTime.now());
public class FileHandler {

    public static void writeToFile(String fileName, String phrase) {
        try {
            // Check if the file exists, if not, create it
            if (!Files.exists(Paths.get(fileName))) {
                Files.createFile(Paths.get(fileName));
            }

            // Write the phrase to the end of the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.append("\n" + LocalDateTime.now() + ": \t " + phrase);
            writer.newLine();
            writer.close();
//            System.out.println("The phrase has been written to the file successfully!");
        } catch (IOException e) {
            System.err.println("Error writing to the file: " + e.getMessage());
        }
    }

    public static void deleteFilesByPattern(String directoryPath, String pattern) {
        File directory = new File(directoryPath);
        System.out.println(Arrays.toString(directory.listFiles()));
        File[] files = directory.listFiles((dir, name) -> name.matches(pattern));

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    if (file.delete()) {
                        System.out.println("File " + file.getName() + " deleted successfully!");
                    } else {
                        System.err.println("Failed to delete file: " + file.getName());
                    }
                }
            }
        }
    }

    public static String readTextFile(String filePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}

