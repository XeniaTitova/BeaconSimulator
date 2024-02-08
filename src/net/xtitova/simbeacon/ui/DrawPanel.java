package net.xtitova.simbeacon.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import net.xtitova.simbeacon.simulation.Beacon;
import net.xtitova.simbeacon.simulation.Coord;
import net.xtitova.simbeacon.simulation.Simulation;

class DrawPanel extends JPanel implements MouseListener {
    private static final int BEACON_SIZE = 10;
    public enum ClickMode {
        ADD,
        DELETE
    }

    private Simulation simulation;


    private int gridSize = 50;
    private boolean drawGrid; // Flag to draw or not draw the grid
    private ClickMode clickMode = ClickMode.ADD;

    public DrawPanel(Simulation simulation, boolean drawGrid) {
        this.simulation = simulation;
        this.drawGrid = drawGrid;
        addMouseListener(this);// Detection of mouse click interruption
    }

    public void setDrawGrid(boolean drawGrid) {
        this.drawGrid = drawGrid;
        repaint(); // Refresh the panel after adding a circle
    }

    public void setClickMode(ClickMode clickMode) {
        this.clickMode = clickMode;
    }

    public void addCercle(int xCenter, int yCenter, int r_int, int r_ext, Color color_int, Color color_ext) {
        simulation.addBeacon(xCenter, yCenter);
        repaint(); // Refresh the panel after adding a circle
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the grid if necessary
        if (drawGrid) {
            g.setColor(Color.LIGHT_GRAY); // Grid color
            // Vertical lines
            for (int i = 0; i < getWidth(); i += gridSize) {
                g.drawLine(i, 0, i, getHeight());
            }
            // Horizontal lines
            for (int i = 0; i < getHeight(); i += gridSize) {
                g.drawLine(0, i, getWidth(), i);
            }

            // Draw the coordinates
            g.setFont(new Font("Arial", Font.PLAIN, 8)); // Coordinate text font
            for (int x = 0; x < getWidth(); x += 5 * gridSize) {
                for (int y = 0; y < getHeight(); y += 5 * gridSize) {
                    g.drawString( x + "," + y, x + 2, y + 10); // Draw coordinates
                }
            }
        }

        simulation.getBeacons().forEach(beacon -> drawBeacon(beacon, g));
    }

    private void drawBeacon(Beacon beacon, Graphics g) {
        Beacon.Mode beaconMode = beacon.getTrMode();
        Color color;
        switch(beaconMode) {
            case Beacon.Mode.TRANSMISSION:
                color = Color.ORANGE;
                break;
            case Beacon.Mode.RECEPTION_IN_PROCESS:
                color = Color.GREEN;
                break;
            default:
                color = Color.RED;
        }
        String id = beacon.getId();
        Cercle circle = new Cercle((int)beacon.getX(), (int)beacon.getY(), BEACON_SIZE, 40, color, Color.BLUE, id);

        circle.draw(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Action to be executed on mouse click
        // Coordinate of click
        int x = e.getX();
        int y = e.getY();

        if(clickMode == ClickMode.ADD) {
            addCercle(x, y, 10, 30, Color.RED, Color.BLUE);
        } else {
            // Delete the circle on which you clicked
            Coord clickCoord = new Coord(x,y);
            List<Beacon> beacons = simulation.getBeacons();
            for (int i = 0; i < beacons.size(); i++) {
                Beacon beacon = beacons.get(i);
                double distance = clickCoord.distanceFrom(beacon.getPosition());
                if(distance <= (double) BEACON_SIZE /2+1){
                    simulation.deleteBeacon(beacon.getId());
                    repaint();
                }
            }
        }
    }

    // The other methods of the MouseListener interface must be implemented even if they are not used
    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
