import net.xtitova.simbeacon.simulation.Simulation;
import net.xtitova.simbeacon.ui.SimpleGUi;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class MainGui {
    public static void main(String[] args) {
        SimpleGUi app = new SimpleGUi(new Simulation());
        app.setVisible(true);
    }
}