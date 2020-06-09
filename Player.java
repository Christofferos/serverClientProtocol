public class Player {
    private int xpos;
    private int ypos;
    private char sign;
    private Direction dir;
    private int lives;
    private int id;
    private boolean carriesBall;

    enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public Player(int x, int y, char c, int id) {
        xpos = x;
        ypos = y;
        sign = c;
        lives = 3;
        dir = Direction.RIGHT;
        this.id = id;
    }   

    public int getX() {
        return xpos;
    }

    public int getY() {
        return ypos;
    }

    public char getSign() {
        return sign;
    }

    public String getLives() {
        return Integer.toString(lives);
    }

    public Direction getDir() {
        return dir;
    }

    public int getID() {
        return id;
    }

    public boolean carriesBall() {
        return carriesBall;
    }

    public void setCarriesBall(boolean carriesBall) {
        this.carriesBall = carriesBall;
    }

    public void hit() {
        lives--;
    }

    public boolean isDefeated() {
        return (lives == 0);
    }

    public void moveX(boolean increment) {
        if (increment) 
            xpos++; 
        else 
            xpos--;
    }

    public void moveY(boolean increment) {
        if (increment)
            ypos++;
        else 
            ypos--;
    }

    public void changeDir(Direction direction) {
        dir = direction;
    }
}