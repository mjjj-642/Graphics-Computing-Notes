import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TetrisAnimation extends JPanel implements ActionListener {

    private final int GRID_WIDTH = 10;
    private final int GRID_HEIGHT = 20;
    private final int BLOCK_SIZE = 30;
    private Timer gameTimer;  
    private Timer confettiTimer; 
    private int[][] grid;
    private int[][] currentPiece;
    private int currentX, currentY;
    private int pieceCounter = 0; 
    private boolean rotatedTShape = false; 
    private boolean rotatedBlueRicky = false; 
    private int fallCounter = 0; 
    private List<Confetti> confettiList = new ArrayList<>();
    private boolean showConfetti = false; 
    private final int CONFETTI_COUNT = 100;

    public TetrisAnimation() {
        setPreferredSize(new Dimension(GRID_WIDTH * BLOCK_SIZE, GRID_HEIGHT * BLOCK_SIZE));
        setBackground(Color.BLACK);
        gameTimer = new Timer(200, this);  
        confettiTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                
                for (Confetti c : confettiList) {
                    c.move();
                }
                repaint();  
            }
        });
        grid = new int[GRID_HEIGHT][GRID_WIDTH];
        preFillLine();
        spawnPiece();
        gameTimer.start();
    }

    private void spawnPiece() {
        fallCounter = 0; 
        switch (pieceCounter % 11) { 
            case 0:
                currentPiece = new int[][]{
                    {1, 1, 1},
                    {0, 1, 0}
                };
                currentX = GRID_WIDTH / 2 - currentPiece[0].length / 2; 
                break;
            case 1:
                currentPiece = new int[][]{
                    {1, 1},
                    {1, 1}
                };
                currentX = GRID_WIDTH / 2 - currentPiece[0].length / 2 + 3; 
                break;
            case 2:
                currentPiece = new int[][]{
                    {1},
                    {1},
                    {1},
                    {1}
                };
                currentX = GRID_WIDTH - 1; 
                break;
            case 3:
                currentPiece = new int[][]{
                    {0, 1, 1},
                    {1, 1, 0}
                };
                currentX = 5; 
                break;
            case 4:
                if (!rotatedTShape) {
                    currentPiece = new int[][]{
                        {1, 1, 1},
                        {0, 1, 0}
                    };
                    currentX = GRID_WIDTH / 2 - currentPiece[0].length / 2; 
                } else {
                    currentPiece = new int[][]{
                        {1, 0},
                        {1, 1},
                        {1, 0}
                    };
                    currentX = 3;
                }
                break;
            case 5:
                currentPiece = new int[][]{
                    {1, 1},
                    {1, 1}
                };
                currentX = 1;
                break;
            case 6:
                if (!rotatedBlueRicky) {
                    currentPiece = new int[][]{
                        {1, 0, 0},
                        {1, 1, 1}
                    };
                    currentX = GRID_WIDTH / 2 - currentPiece[0].length / 2;
                } else {
                    currentPiece = new int[][]{
                        {1, 1, 1},
                        {0, 0, 1}
                    };
                    currentX = 6; 
                }
                break;
            case 7:
                currentPiece = new int[][]{
                    {0, 1, 1},
                    {1, 1, 0}
                };
                currentX = 1;
                break;
            case 8:
                currentPiece = new int[][]{
                    {1, 1},
                    {1, 1}
                };
                currentX = 4; 
                break;
            case 9:
                currentPiece = new int[][]{
                    {1}
                };
                currentX = 1; 
                break;
            case 10:
                currentPiece = new int[][]{
                    {1},
                    {1},
                    {1},
                    {1}
                };
                currentX = 0; 
                break;
        }
        currentY = 0; 
    }

    private void preFillLine() {
        for (int x = 0; x < GRID_WIDTH; x++) {
            grid[GRID_HEIGHT - 1][x] = 1;
        }
        grid[GRID_HEIGHT - 1][GRID_WIDTH / 2] = 0; 
    }

    private boolean canMove(int newX, int newY) {
        for (int y = 0; y < currentPiece.length; y++) {
            for (int x = 0; x < currentPiece[y].length; x++) {
                if (currentPiece[y][x] == 1) {
                    int gridX = newX + x;
                    int gridY = newY + y;
                    if (gridX < 0 || gridX >= GRID_WIDTH || gridY >= GRID_HEIGHT || grid[gridY][gridX] == 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void lockPiece() {
        for (int y = 0; y < currentPiece.length; y++) {
            for (int x = 0; x < currentPiece[y].length; x++) {
                if (currentPiece[y][x] == 1) {
                    grid[currentY + y][currentX + x] = 1;
                }
            }
        }

        if (pieceCounter % 8 == 4) {
            rotatedTShape = !rotatedTShape;
        }

        if (pieceCounter % 9 == 6) {
            rotatedBlueRicky = !rotatedBlueRicky;
        }

        pieceCounter++;
        clearLines();
        spawnPiece();
    }

    private void clearLines() {
        for (int y = 0; y < GRID_HEIGHT; y++) {
            boolean fullLine = true;
            for (int x = 0; x < GRID_WIDTH; x++) {
                if (grid[y][x] == 0) {
                    fullLine = false;
                    break;
                }
            }
            if (fullLine) {
                for (int row = y; row > 0; row--) {
                    System.arraycopy(grid[row - 1], 0, grid[row], 0, GRID_WIDTH);
                }
                for (int x = 0; x < GRID_WIDTH; x++) {
                    grid[0][x] = 0;
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        for (int y = 0; y < GRID_HEIGHT; y++) {
            for (int x = 0; x < GRID_WIDTH; x++) {
                if (grid[y][x] == 1) {
                    g2d.setColor(Color.CYAN);
                    g2d.fillRect(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }
        
        g2d.setColor(pieceCounter % 3 == 1 ? Color.YELLOW : pieceCounter % 3 == 2 ? Color.GREEN : Color.RED);
        for (int y = 0; y < currentPiece.length; y++) {
            for (int x = 0; x < currentPiece[y].length; x++) {
                if (currentPiece[y][x] == 1) {
                    g2d.fillRect((currentX + x) * BLOCK_SIZE, (currentY + y) * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect((currentX + x) * BLOCK_SIZE, (currentY + y) * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                    g2d.setColor(pieceCounter % 3 == 1 ? Color.YELLOW : pieceCounter % 3 == 2 ? Color.GREEN : Color.RED);
                }
            }
        }
  
        if (showConfetti) {
            Iterator<Confetti> iterator = confettiList.iterator();
            while (iterator.hasNext()) {
                Confetti c = iterator.next();
                g2d.setColor(c.twinkle ? c.color : Color.WHITE);
                g2d.fillRect(c.x, c.y, c.size, c.size);
                if (c.isOutOfBounds(getWidth(), getHeight())) {
                    iterator.remove();
                }
            }
            
            String message = "Tetris!";
            Font font = new Font("Arial", Font.BOLD, 48);
            FontMetrics metrics = g2d.getFontMetrics(font);
            int x = (getWidth() - metrics.stringWidth(message)) / 2;
            int y = (getHeight() - metrics.getHeight()) / 2 + metrics.getAscent(); 

            g2d.setColor(Color.WHITE);
            g2d.setFont(font);
            g2d.drawString(message, x, y);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (pieceCounter > 10) {
            gameTimer.stop();
            if (!showConfetti) {
                showConfetti = true;
                for (int i = 0; i < CONFETTI_COUNT; i++) {
                    confettiList.add(new Confetti(
                            (int) (Math.random() * getWidth()),
                            (int) (Math.random() * getHeight() / 2),
                            5 + (int) (Math.random() * 10),
                            -3 + (int) (Math.random() * 7),
                            2 + (int) (Math.random() * 5),
                            new Color(
                                    (int) (Math.random() * 255),
                                    (int) (Math.random() * 255),
                                    (int) (Math.random() * 255)
                            )
                    ));
                }
                confettiTimer.start();  
            }
            repaint();
            return;
        }

        if (canMove(currentX, currentY + 1)) {
            currentY++;
            if (pieceCounter % 8 == 4) {
                fallCounter++;
                if (fallCounter == 3 && !rotatedTShape) {
                    currentPiece = new int[][]{
                        {1, 0},
                        {1, 1},
                        {1, 0}
                    };
                    currentX = 3;
                    rotatedTShape = true;
                }
            }

            if (pieceCounter % 9 == 6) {
                fallCounter++;
                if (fallCounter == 3 && !rotatedBlueRicky) {
                    currentPiece = new int[][]{
                        {1, 1, 1},
                        {0, 0, 1}
                    };
                    currentX = 6;
                    rotatedBlueRicky = true;
                }
            }
        } else {
            lockPiece();
        }
        repaint();
    }
   
    class Confetti {

        int x, y;
        int size;
        int dx, dy;
        Color color;
        boolean twinkle;

        public Confetti(int x, int y, int size, int dx, int dy, Color color) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.dx = dx;
            this.dy = dy;
            this.color = color;
            this.twinkle = true;
        }

        public void move() {
            x += dx;
            y += dy;
            twinkle = !twinkle;
        }

        public boolean isOutOfBounds(int width, int height) {
            return y > height;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris Animation");
        TetrisAnimation tetris = new TetrisAnimation();
        frame.add(tetris);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}