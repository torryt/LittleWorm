import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

class SnakeFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	int xSize = 600;
	int ySize = 400;

	public SnakeFrame() {
		Container cp = getContentPane();
		cp.add(new SnakePanel());
		// cp.add(new MainMenu());

		setSize(xSize, ySize);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
	}
}

/*--------------------------------------------------------------------------------*/
class SnakePanel extends JPanel implements Runnable, KeyListener {

	private static final long serialVersionUID = 1L;

	Snakeelement headRect = initSnake();
	Snakeelement tailRect = new Snakeelement();
	Eple ep = new Eple();
	int score = 0;
	String scoreString = "Score: " + score;
	int difficultyList[];
	int diffPointer;
	long pauseTime;

	boolean isCrashed = false;
	int growthRate = 10;

	static final int MOVING_LEFT = 0;
	static final int MOVING_RIGHT = 1;
	static final int MOVING_UP = 2;
	static final int MOVING_DOWN = 3;

	public static int direction = MOVING_RIGHT;

	public SnakePanel() {
		initDifficulty();
		addKeyListener(this);
		setVisible(true);
		setFocusable(true);
		boolean focus = requestFocusInWindow();
		System.out.println(focus);

		new Thread(this).start();
	}

	private void initDifficulty() {
		difficultyList = new int[6];
		difficultyList[1] = 40;
		difficultyList[2] = 30;
		difficultyList[3] = 20;
		difficultyList[4] = 10;
		difficultyList[5] = 5;
		
		diffPointer = 3;
		pauseTime = difficultyList[diffPointer];
	}

	public void paintComponent(Graphics g) {
		if (isCrashed) {
			endGame(g);
		}

		else {
			g.clearRect(0, 0, this.getWidth(), this.getHeight());
			checkAppleEaten();
			checkCollision();
			g.setColor(Color.black);
			g.drawString(scoreString, 600 - 70, 30);
			g.drawRect(5, 5, 590, 365);
			setBackground(Color.white);

			Snakeelement snakeTraverser = headRect;
			// Linked-list traverser

			while (snakeTraverser != null) {
				snakeTraverser.drawMe(g);
				snakeTraverser = snakeTraverser.tail;
			}

			ep.drawMe(g);
		}
	}

	
	private void endGame(Graphics g) {
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		g.drawImage(loadImage(), 20, 20, null);

		g.drawString("Score: " + score, 270, 300);
		g.drawString("Press \"R\" to play again!", 230, 320);
		g.drawString("Difficulty: " + diffPointer
				+ " (press 'n' or 'm' to adjust)", 180, 340);
	}

	private Image loadImage() {

		BufferedImage img = null;

		try {
			img = ImageIO.read(new File("crash.png"));
		} catch (IOException e) {
		}

		return img;
	}

	private void checkCollision() {
		// Checking wall collision
		if (headRect.x <= 5 || headRect.x >= 590 || headRect.y <= 5
				|| headRect.y >= 365) {
			isCrashed = true;
		}

		// Checking self collision
		checkSelfCollision();
	}

	private void checkSelfCollision() {
		Rectangle r1 = new Rectangle(headRect.x, headRect.y, headRect.bredde,
				headRect.hoyde);
		Snakeelement next = headRect.getNext();

		// Traverse the snake and compare each element with the head
		while (next != null) {
			Rectangle r2 = new Rectangle(next.x, next.y, next.bredde,
					next.hoyde);

			if (r1.intersects(r2)) {
				isCrashed = true;
				break;
			}
			next = next.getNext();
		}

	}

	private void checkAppleEaten() {
		Rectangle r1 = new Rectangle(headRect.x, headRect.y, headRect.bredde,
				headRect.hoyde);
		Rectangle r2 = new Rectangle(ep.x, ep.y, ep.bredde, ep.hoyde);

		if (r1.intersects(r2)) {

			moveApple();
			score++;
			scoreString = "Score: " + score;

			// Gets the last element of the snake and appends a tail to it
			headRect.addTail(growthRate);
			tailRect = headRect.getLastElement(headRect);
			tailRect.tail = new Snakeelement(tailRect);
			tailRect.tail.tail = new Snakeelement(tailRect.tail);
			tailRect.tail.tail.tail = new Snakeelement(tailRect.tail.tail);

		}
	}

	private void moveApple() {
		int randomX = 10 + (int) ((Math.random() * 575.0));
		int randomY = 10 + (int) ((Math.random() * 345.0));
		ep.setPos(randomX, randomY);
	}

	// The run() method.
	public void run() {

		/*
		 * The algorithm for MOVING the snake is as follows: 1. Move the head 2.
		 * Insert new element in heads old place 3. New elements tail is heads
		 * old tail 4. Heads new tail is the new element 5. Remove the last
		 * element of the snake
		 */
		while (!isCrashed) {

			// Adds new element in the same position as headRect
			Snakeelement insertSnake = new Snakeelement(headRect);

			// Moves the headRect in the right direction
			switch (direction) {
			case MOVING_LEFT:
				headRect.x -= 5;
				repaint();
				break;
			case MOVING_RIGHT:
				headRect.x += 5;
				repaint();
				break;
			case MOVING_UP:
				headRect.y -= 5;
				repaint();
				break;
			case MOVING_DOWN:
				headRect.y += 5;
				repaint();
				break;
			}

			// Organizing the tails
			insertSnake.tail = headRect.tail;
			headRect.tail = insertSnake;

			// Removing the last element
			headRect.removeLastElement(headRect);

			try {
				Thread.sleep(pauseTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		repaint();
	}

	/**
	 * Initializes the snake with three tails
	 * 
	 * @return Snakeelement Returns with the head element
	 */
	private Snakeelement initSnake() {

		Snakeelement snake = new Snakeelement();
		snake.tail = new Snakeelement(snake);
		snake.tail.x -= 5;
		snake.tail.tail = new Snakeelement(snake.tail);
		snake.tail.tail.x -= 5;

		return snake;
	}

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		
		//Cases UP, DOWN, LEFT, RIGHT controls direction of the snake.
		case KeyEvent.VK_UP:
			if (direction != MOVING_DOWN) {
				direction = MOVING_UP;
			}
			break;

			
		case KeyEvent.VK_DOWN:
			if (direction != MOVING_UP) {
				direction = MOVING_DOWN;
			}
			break;

			
		case KeyEvent.VK_LEFT:
			if (direction != MOVING_RIGHT) {
				direction = MOVING_LEFT;
			}
			break;

			
		case KeyEvent.VK_RIGHT:
			if (direction != MOVING_LEFT) {
				direction = MOVING_RIGHT;
			}
			break;
		
		/*
		 * Cases M and N controls difficulty level.
		 * Difficulty can only be adjusted when snake is dead.
		 */
		case KeyEvent.VK_M:
			if (isCrashed && diffPointer < 5) {
				pauseTime = difficultyList[++diffPointer];
				repaint();
			}
			break;
		
		case KeyEvent.VK_N:
			if (isCrashed && diffPointer > 1) {
				pauseTime = difficultyList[--diffPointer];
				repaint();
			}
			break;

		//Key R triggers game reset
		case KeyEvent.VK_R:
			if (isCrashed) {
				isCrashed = false;
				reset();

				new Thread(this).start();
			}
		}
	}

	private void reset() {
		headRect = initSnake();
		score = 0;
		scoreString = "Score: " + score;
		direction = MOVING_RIGHT;
		moveApple();
	}

	public void keyReleased(KeyEvent arg0) {
	}

	public void keyTyped(KeyEvent e) {
	}
}

/*--------------------------------------------------------------------------------*/

public class Snake {
	public static void main(String[] arg0) {
		new SnakeFrame();
	}
}
