package net.xtitova.simbeacon.ui;

import java.awt.*;
import javax.swing.*;
import net.xtitova.simbeacon.simulation.Simulation;
import net.xtitova.simbeacon.simulation.SimulationUpdateCallback;

public class SimpleGUi extends JFrame implements SimulationUpdateCallback {
    private JButton button = new JButton("Random Beacon"); // Unnecessary button
    private final DrawPanel drawPanel;

    // Two radio buttons to define whether to add or delete circles
    private JRadioButton radioAdd = new JRadioButton("Add Beacon");
    private JRadioButton radioDelete = new JRadioButton("Delete Beacon");

    private JCheckBox checkGried = new JCheckBox("Drawing the grid", false);

    private Simulation simulation = new Simulation();

    // Three buttons currently inactive to manage simulation
    JButton playButton = new JButton("PLAY");
    JButton pauseButton = new JButton("PAUSE");
    JButton stopButton = new JButton("STOP");

    public SimpleGUi() {
        // Creating a window
        super("Simple Example");

        simulation.addUpdateCallback(this);

        this.drawPanel = new DrawPanel(simulation,false); // Drawing area for drawing circles
        this.setBounds(100,100,500,650);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container container = this.getContentPane();

        // Main panel containing all the elements
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Vertical panel to hold all the buttons
        JPanel verticalBoxPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        verticalBoxPanel.setPreferredSize(new Dimension(500, 75));

        // Panel to select drawing mode
        JPanel drawingModePanel = new JPanel(new GridLayout(1, 3, 5, 5));
        drawingModePanel.setPreferredSize(new Dimension(500, 75));

        // Panel to manage simulation (play/stop)
        JPanel simulationPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        drawingModePanel.setPreferredSize(new Dimension(500, 75));

        //Filling the drawing mode panel
        drawingModePanel.add(radioAdd);
        drawingModePanel.add(radioDelete);
        drawingModePanel.add(checkGried);

        //Filling the manage simulation panel
        simulationPanel.add(playButton);
        simulationPanel.add(pauseButton);
        simulationPanel.add(stopButton);

        //Filling the buttons panel
        verticalBoxPanel.add(drawingModePanel);
        verticalBoxPanel.add(simulationPanel);
        verticalBoxPanel.add(button, BorderLayout.CENTER);

        // Connect radio buttons
        ButtonGroup group = new ButtonGroup();
        group.add(radioAdd);
        group.add(radioDelete);
        radioAdd.setSelected(true);

        // Added action on all buttons
        radioAdd.addActionListener(e -> changeClickMode(DrawPanel.ClickMode.ADD));
        radioDelete.addActionListener(e -> changeClickMode(DrawPanel.ClickMode.DELETE));

        checkGried.addActionListener(e -> changeGried());

        button.addActionListener(e -> funBouttonPress());
        playButton.addActionListener(e -> playAction());
        pauseButton.addActionListener(e -> pauseAction());
        stopButton.addActionListener(e -> stopAction());


        // Filling the main panel and window
        drawPanel.setPreferredSize(new Dimension(500, 500));
        mainPanel.add(drawPanel);
        mainPanel.add(verticalBoxPanel,BorderLayout.SOUTH);

        container.add(mainPanel);
        this.pack(); // To set the dimantion
    }

    @Override
    public void onUpdate() {
        // Called when simulation updates its state
        repaint();
    }

    private void changeGried() {
        drawPanel.setDrawGrid(checkGried.isSelected());
    }

    private void changeClickMode(DrawPanel.ClickMode clickMode){
        drawPanel.setClickMode(clickMode);
    }

    // Function to be completed
    private void playAction() {
        System.out.println("PLAY");

        simulation.start();
    }

    private void pauseAction() {
        simulation.pause();
        System.out.println("PAUSE");
    }

    private void stopAction() {
        System.out.println("STOP");

        simulation.stop();
    }

    // Function not used but good for testing
    private void funBouttonPress() {
        // Ajouter un cercle à la liste des cercles
        int x = (int) (Math.random() * drawPanel.getWidth());
        int y = (int) (Math.random() * drawPanel.getHeight());
        int r = 10;
        int r2 = 30;
        drawPanel.addCercle(x, y, r, r2, Color.RED, Color.BLUE);

    }
    
    // Not used but may be useful soon
    /*// Method for creating a button with a specified icon
    private static JButton createIconButton(String iconName) {
        // Load icon from file
        ImageIcon icon = new ImageIcon(iconName);
        // Create a button with the icon
        JButton button = new JButton(icon);
        // Disable button edge
        button.setBorderPainted(false);
        // Disable button filling
        button.setContentAreaFilled(false);
        // Enable button hover effect
        button.setRolloverEnabled(true);
        // Define alternative text for accessibility
        button.setToolTipText(iconName);
        return button;
    }*/
}
