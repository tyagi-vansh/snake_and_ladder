import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JFrame implements ActionListener, KeyListener {
    private static final int GRID_SIZE = 20;
    private static final int CELL_SIZE = 20;
    private static final int GAME_SPEED = 150;

    private LinkedList<Point> snake;
    private Point food;
    private int direction; // 0: right, 1: down, 2: left, 3: up

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(GRID_SIZE * CELL_SIZE, GRID_SIZE * CELL_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        snake = new LinkedList<>();
        snake.add(new Point(2, 0));
        snake.add(new Point(1, 0));
        snake.add(new Point(0, 0));

        direction = 0; // initial direction is right

        spawnFood();

        Timer timer = new Timer(GAME_SPEED, this);
        timer.start();

        addKeyListener(this);
        setFocusable(true);
    }

    private void spawnFood() {
        Random rand = new Random();
        int x, y;

        do {
            x = rand.nextInt(GRID_SIZE);
            y = rand.nextInt(GRID_SIZE);
        } while (snake.contains(new Point(x, y)));

        food = new Point(x, y);
    }

    private void move() {
        Point head = snake.getFirst();

        switch (direction) {
            case 0:
                head = new Point((head.x + 1) % GRID_SIZE, head.y);
                break;
            case 1:
                head = new Point(head.x, (head.y + 1) % GRID_SIZE);
                break;
            case 2:
                head = new Point((head.x - 1 + GRID_SIZE) % GRID_SIZE, head.y);
                break;
            case 3:
                head = new Point(head.x, (head.y - 1 + GRID_SIZE) % GRID_SIZE);
                break;
        }

        if (head.equals(food)) {
            snake.addFirst(food);
            spawnFood();
        } else {
            snake.addFirst(head);
            snake.removeLast();
        }
    }

    private boolean checkCollision() {
        Point head = snake.getFirst();

        // Check collision with walls
        if (head.x < 0 || head.x >= GRID_SIZE || head.y < 0 || head.y >= GRID_SIZE) {
            return true;
        }

        // Check collision with itself
        return snake.indexOf(head) != snake.lastIndexOf(head);
    }

    private void draw(Graphics g) {
        // Draw snake
        g.setColor(Color.GREEN);
        for (Point point : snake) {
            g.fillRect(point.x * CELL_SIZE, point.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }

        // Draw food
        g.setColor(Color.RED);
        g.fillRect(food.x * CELL_SIZE, food.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();

        if (checkCollision()) {
            JOptionPane.showMessageDialog(this, "Game Over!");
            System.exit(0);
        }

        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        draw(g);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // Change direction based on arrow keys
        switch (key) {
            case KeyEvent.VK_RIGHT:
                direction = 0;
                break;
            case KeyEvent.VK_DOWN:
                direction = 1;
                break;
            case KeyEvent.VK_LEFT:
                direction = 2;
                break;
            case KeyEvent.VK_UP:
                direction = 3;
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SnakeGame game = new SnakeGame();
            game.setVisible(true);
        });
    }
}
