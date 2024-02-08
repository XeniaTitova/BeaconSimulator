package net.xtitova.simbeacon.ui;

import java.awt.*;

class Cercle { // A filled inner circle and an outer crown
    private int xCenter;
    private int yCenter;
    private int rayon_int;
    private int rayon_ext;
    private Color color_int;
    private Color color_ext;
    private String id;

    public Cercle(int xCenter, int yCenter, int rayon, int rayon2, Color color_int, Color color_ext, String id) {
        this.xCenter = xCenter;
        this.yCenter = yCenter;
        this.rayon_int = rayon;
        this.color_int = color_int;
        this.rayon_ext = rayon2;
        this.color_ext = color_ext;
        this.id = id;

    }

    public void draw(Graphics g) {
        // Draw the inner circle (filled)
        g.setColor(color_int);
        g.fillOval(xCenter - rayon_int, yCenter - rayon_int, rayon_int * 2, rayon_int * 2);

        // Draw the outer circle (contour)
        g.setColor(color_ext);
        g.drawOval(xCenter - rayon_ext, yCenter - rayon_ext, rayon_ext * 2, rayon_ext * 2);
        g.setColor(Color.BLACK);
        g.drawString( id, xCenter, yCenter);
    }

    public Boolean isInCercle(int x, int y){
        // Calculate the distance between the center of the circle and the given point (x, y)
        double distance = Math.sqrt(Math.pow(x - xCenter, 2) + Math.pow(y - yCenter, 2));

        // Check that the distance is less than or equal to the radius of the circle
        return distance <= rayon_int;
    }
}
