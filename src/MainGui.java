import net.xtitova.simbeacon.logfile.FileHandler;
import net.xtitova.simbeacon.simulation.Simulation;
import net.xtitova.simbeacon.ui.SimpleGUi;

import java.time.LocalDateTime;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class MainGui {
    public static void main(String[] args) {
        SimpleGUi app = new SimpleGUi();
        app.setVisible(true);
        FileHandler.deleteFilesByPattern(".",".*\\_log.txt");
    }
}