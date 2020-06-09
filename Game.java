import java.util.*;

class Game {
	private static final long serialVersionUID = 1L;

	private char[][] gameField;
	private final static int SIZE = 20; // Width and height of game field
	private boolean gameIsOn = true;
	private String winner = "Ingen";
	private Player player1;
	private Player player2;
	private Ball ball;
	private boolean updated = true;
	private Timer timer;

	public Game() {
		player1 = new Player(10, 10, 'X', 1);
		player2 = new Player(12, 12, 'Z', 2);
		ball = new Ball(11, 11, 'O');
		gameField = new char[SIZE][SIZE];
		initGameField();
	}

	public void initGameField() {
		// Initialise matrix
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				gameField[x][y] = ' ';
			}
		}

		// Add border
		char border = '#';
		for (int i = 0; i < SIZE; i++) {
			gameField[0][i] = border;
			gameField[SIZE - 1][i] = border;
			gameField[i][0] = border;
			gameField[i][SIZE - 1] = border;
		}

		// Add players
		gameField[player1.getX()][player1.getY()] = player1.getSign();
		gameField[player2.getX()][player2.getY()] = player2.getSign();

		// Add ball
		gameField[ball.getX()][ball.getY()] = ball.getSign();

		// Add obstacles
		char obstacle = '#';
		for (int i = 5; i < SIZE - 6; i++) {
			gameField[i][5] = obstacle;
			gameField[i][SIZE - 6] = obstacle;
		}
	}

	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

	public String getWinner() {
		return winner;
	}

	public String toString() {
		String s = "";
		if (!player1.isDefeated() && !player2.isDefeated()) {
			for (int y = 0; y < SIZE; y++) {
				for (int x = 0; x < SIZE; x++) {
					s += Character.toString(gameField[x][y]);
				}
				s += "\n";
			}
		}
		return s;
	}

	public boolean collision(int x, int y) {
		if (gameField[x][y] == ' ' || gameField[x][y] == ball.getSign()) {
			return false;
		}
		return true;
	}

	public boolean draw(char sign, int x, int y) {
		if (x >= 0 && x < SIZE && y >= 0 && y < SIZE && (!collision(x, y) || sign == ' ')) {
			gameField[x][y] = sign;
			return true;
		}
		return false;
	}

	public void gameOver() {
		gameIsOn = false;
	}

	public boolean isOn() {
		return gameIsOn;
	}

	public Player getPlayer(int player) throws IllegalArgumentException {
		if (player == 1)
			return player1;
		else if (player == 2)
			return player2;
		throw new IllegalArgumentException();
	}

	public void pickUpBall(Player player) {
		if (ball.getX() == player.getX() && ball.getY() == player.getY()) {
			ball.setCarriedBy(player.getID());
			player.setCarriesBall(true);
		}
	}

	// Check that player is carrying a ball, check that the next position in front
	// of player is empty - before throwing the ball.
	public void throwBall(Player p) {
		if (p.carriesBall()) {
			if (!collision(nextX(p.getDir(), p.getX()), nextY(p.getDir(), p.getY()))) {
				p.setCarriesBall(false);
				ball.setXpos(nextX(p.getDir(), p.getX()));
				ball.setYpos((nextY(p.getDir(), p.getY())));
				ball.changeDir(p.getDir());
				draw(ball.getSign(), ball.getX(), ball.getY());
				animateBall();
			}
		}
	}

	public void animateBall() {
		TimerTask task = new Task();
		timer = new Timer();
		timer.schedule(task, 50, 50);

	}

	public int nextX(Player.Direction dir, int x) {
		if (dir == Player.Direction.LEFT) {
			return x - 1;
		} else if (dir == Player.Direction.RIGHT) {
			return x + 1;
		}
		return x;
	}

	public int nextY(Player.Direction dir, int y) {
		if (dir == Player.Direction.UP) {
			return y - 1;
		} else if (dir == Player.Direction.DOWN) {
			return y + 1;
		}
		return y;
	}

	// Move player and clean up the space behind the player to empty place.
	public void movePlayer(int player) {
		Player p = (player == 1) ? player1 : player2;
		if (p.getDir() == Player.Direction.UP) {
			if (!collision(p.getX(), p.getY() - 1)) {
				draw(' ', p.getX(), p.getY());
				p.moveY(false);
				pickUpBall(p);
				draw(p.getSign(), p.getX(), p.getY());
			}
		} else if (p.getDir() == Player.Direction.DOWN) {
			if (!collision(p.getX(), p.getY() + 1)) {
				draw(' ', p.getX(), p.getY());
				p.moveY(true);
				pickUpBall(p);
				draw(p.getSign(), p.getX(), p.getY());
			}
		} else if (p.getDir() == Player.Direction.RIGHT) {
			if (!collision(p.getX() + 1, p.getY())) {
				draw(' ', p.getX(), p.getY());
				p.moveX(true);
				pickUpBall(p);
				draw(p.getSign(), p.getX(), p.getY());
			}
		} else if (p.getDir() == Player.Direction.LEFT) {
			if (!collision(p.getX() - 1, p.getY())) {
				draw(' ', p.getX(), p.getY());
				p.moveX(false);
				pickUpBall(p);
				draw(p.getSign(), p.getX(), p.getY());
			}
		}
	}

	// Check if the player will be hit by the ball otherwise move ball in given
	// direction.
	class Task extends TimerTask {
		public void run() {
			if (collision(nextX(ball.getDir(), ball.getX()), nextY(ball.getDir(), ball.getY()))) {
				int nextX = nextX(ball.getDir(), ball.getX());
				int nextY = nextY(ball.getDir(), ball.getY());
				if (nextX == player1.getX() && nextY == player1.getY()) {
					player1.hit();
				} else if (nextX == player2.getX() && nextY == player2.getY()) {
					player2.hit();
				}
				if (player1.isDefeated() || player2.isDefeated()) {
					gameOver();
				}
				timer.cancel();
			} else {
				draw(' ', ball.getX(), ball.getY());
				ball.setXpos(nextX(ball.getDir(), ball.getX()));
				ball.setYpos(nextY(ball.getDir(), ball.getY()));
				draw(ball.getSign(), ball.getX(), ball.getY());
			}
			setUpdated(true);
		}
	}
}
