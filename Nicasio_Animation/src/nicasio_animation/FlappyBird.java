/*
Mark Joseph C. Nicasio
4CSC
Final Project Animation
 */
package nicasio_animation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird extends JPanel implements ActionListener {

    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private final int PIPE_WIDTH = 80;
    private final int PIPE_GAP = 150;
    private final int GRAVITY = 2;
    private final int LIFT = 20;

    private int birdX = WIDTH / 4; // Bird's X position
    private int birdY = HEIGHT / 2; // Bird's Y position
    private int birdVelocity = 0;
    private int score = 0;
    private boolean gameOver = false;

    private Timer timer;
    private ArrayList<Rectangle> pipes;
    private Image birdImage;

    public FlappyBird() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.CYAN);
        setFocusable(true);

        // Load and scale the bird image
        birdImage = new ImageIcon("C:\\Users\\Mark Joseph Nicasio\\Desktop\\S.Y. 2024-2025 (4CSC CHAPTER)\\1st Sem\\CS 26115 (GRAPHICS COMPUTING AND MULTIMEDIA TECHNOLOGY)\\Graphics_Java\\Nicasio_Animation\\src\\birdy.png")
                .getImage()
                .getScaledInstance(20, 20, Image.SCALE_SMOOTH);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE && !gameOver) {
                    birdVelocity = -LIFT;
                } else if (gameOver && e.getKeyCode() == KeyEvent.VK_R) {
                    resetGame();
                }
            }
        });

        pipes = new ArrayList<>();
        timer = new Timer(20, this);
        timer.start();
        spawnPipe();
    }

    private void resetGame() {
        birdY = HEIGHT / 2;
        birdVelocity = 0;
        score = 0;
        pipes.clear();
        gameOver = false;
        spawnPipe();
        timer.restart();
    }

    private void spawnPipe() {
        Random rand = new Random();
        int pipeHeight = rand.nextInt(HEIGHT - PIPE_GAP - 50) + 50;
        pipes.add(new Rectangle(WIDTH, 0, PIPE_WIDTH, pipeHeight));
        pipes.add(new Rectangle(WIDTH, pipeHeight + PIPE_GAP, PIPE_WIDTH, HEIGHT - pipeHeight - PIPE_GAP));
    }

    private void updateBird() {
        birdVelocity += GRAVITY;
        birdY += birdVelocity;
        if (birdY < 0) {
            birdY = 0;
        }
        if (birdY > HEIGHT - birdImage.getHeight(null)) {
            gameOver = true;
            timer.stop();
        }
    }

    private void updatePipes() {
        for (int i = 0; i < pipes.size(); i++) {
            Rectangle pipe = pipes.get(i);
            pipe.x -= 5;
            if (pipe.x + PIPE_WIDTH < 0) {
                pipes.remove(i);
                i--;
                score++;
            }
        }

        if (pipes.isEmpty() || pipes.get(pipes.size() - 1).x < WIDTH - 300) {
            spawnPipe();
        }
    }

    private boolean checkCollision() {
        Rectangle bird = new Rectangle(birdX, birdY, birdImage.getWidth(null), birdImage.getHeight(null));
        for (Rectangle pipe : pipes) {
            if (pipe.intersects(bird)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            updateBird();
            updatePipes();
            if (checkCollision()) {
                gameOver = true;
                timer.stop();
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw bird
        g.drawImage(birdImage, birdX, birdY, null);

        // Draw pipes
        g.setColor(Color.GREEN);
        for (Rectangle pipe : pipes) {
            g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
        }

        // Draw score
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 20);

        // Game over message
        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Game Over", WIDTH / 2 - 100, HEIGHT / 2);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Press 'R' to Restart", WIDTH / 2 - 100, HEIGHT / 2 + 50);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");
        FlappyBird game = new FlappyBird();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
