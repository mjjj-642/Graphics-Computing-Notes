import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import javax.swing.Timer;



public class IntricateBirdAnimation extends Frame {
    private int x = 0; // Bird's horizontal position
    private int y = 250; // Bird's vertical position
    private double wingAngle = 0; // Wing rotation angle
    private double scale = 1.0; // Bird's scale factor
    private Image offscreenImage;
    private Graphics2D offscreenGraphics;
    private Timer timer;

    public IntricateBirdAnimation() {
        // Set window properties
        setTitle("Intricate Bird Animation");
        setSize(800, 600);
        setVisible(true);

        // Set up double buffering
        offscreenImage = createImage(getWidth(), getHeight());
        offscreenGraphics = (Graphics2D) offscreenImage.getGraphics();
        offscreenGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Timer for animation updates
        timer = new Timer(20, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                x += 2; // Move the bird horizontally
                if (x > getWidth()) x = -100; // Reset position once bird reaches the edge
                wingAngle = Math.sin(System.currentTimeMillis() * 0.005) * 30; // Flap wings
                scale = 1 + Math.cos(x * 0.02) * 0.2; // Bird scales based on its position
                repaint(); // Repaint the window for each frame
            }
        });
        timer.start();
    }

    // Override the update method to use double buffering
    @Override
    public void update(Graphics g) {
        paint(g); // Call paint to perform double buffering
    }

    // Method to handle the drawing of the components
    @Override
    public void paint(Graphics g) {
        // Clear the offscreen image
        offscreenGraphics.clearRect(0, 0, getWidth(), getHeight());

        // Draw background (sky)
        offscreenGraphics.setColor(Color.CYAN);
        offscreenGraphics.fillRect(0, 0, getWidth(), getHeight());

        // Draw clouds (static for simplicity)
        offscreenGraphics.setColor(Color.WHITE);
        offscreenGraphics.fillOval(100, 50, 200, 80);
        offscreenGraphics.fillOval(400, 100, 250, 100);

        // Draw the sun (for day)
        offscreenGraphics.setColor(Color.YELLOW);
        offscreenGraphics.fillOval(600, 50, 100, 100);

        // Apply transformations to the bird (Translation, Rotation, Scaling)
        offscreenGraphics.translate(x, y); // Move the bird based on x, y coordinates
        offscreenGraphics.scale(scale, scale); // Scale the bird
        
        // Draw the intricate bird (Head, Body, Wings, Eyes, Beak)
        
        // Bird body (rounded oval with feather effect)
        offscreenGraphics.setColor(new Color(255, 204, 0)); // Light yellow for the body
        offscreenGraphics.fillOval(-50, -30, 100, 60); // Body

        // Bird head (smaller circle)
        offscreenGraphics.setColor(new Color(255, 204, 0)); // Same color as body
        offscreenGraphics.fillOval(-40, -60, 50, 50); // Head

        // Bird eyes (two small black circles)
        offscreenGraphics.setColor(Color.BLACK);
        offscreenGraphics.fillOval(-25, -50, 10, 10); // Left eye
        offscreenGraphics.fillOval(5, -50, 10, 10); // Right eye

        // Bird beak (triangle)
        offscreenGraphics.setColor(Color.ORANGE);
        int[] xPoints = {-10, 0, 10};
        int[] yPoints = {-35, -30, -35};
        offscreenGraphics.fillPolygon(xPoints, yPoints, 3); // Beak

        // Left wing (rotate and shear with feather details)
        offscreenGraphics.rotate(Math.toRadians(wingAngle), 0, 0); // Rotate wings
        offscreenGraphics.setColor(Color.DARK_GRAY);
        offscreenGraphics.fillPolygon(new int[]{-50, -90, -50}, new int[]{-10, 10, 30}, 3); // Outer wing feather
        offscreenGraphics.setColor(Color.GRAY);
        offscreenGraphics.fillPolygon(new int[]{-50, -85, -50}, new int[]{5, 15, 25}, 3); // Inner wing feather

        // Right wing (rotate and shear with feather details)
        offscreenGraphics.rotate(Math.toRadians(-2 * wingAngle), 0, 0); // Rotate opposite direction for right wing
        offscreenGraphics.setColor(Color.DARK_GRAY);
        offscreenGraphics.fillPolygon(new int[]{50, 90, 50}, new int[]{-10, 10, 30}, 3); // Outer wing feather
        offscreenGraphics.setColor(Color.GRAY);
        offscreenGraphics.fillPolygon(new int[]{50, 85, 50}, new int[]{5, 15, 25}, 3); // Inner wing feather
        
        // Reset rotation for other components
        offscreenGraphics.setTransform(new AffineTransform()); // Reset transform

        // Draw the offscreen image onto the screen
        g.drawImage(offscreenImage, 0, 0, this);
    }

    // Main method to run the program
    public static void main(String[] args) {
        IntricateBirdAnimation animation = new IntricateBirdAnimation();
        // Add WindowListener to close the window
        animation.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}
