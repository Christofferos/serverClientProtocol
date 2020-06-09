public class Ball {
    private int xpos;
    private int ypos;
    private char sign;
    private Player.Direction dir;
    private int carriedBy;

    enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public Ball(int x, int y, char c) {
        xpos = x;
        ypos = y;
        sign = c;
        dir = Player.Direction.RIGHT;
        carriedBy = 0;
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

    public Player.Direction getDir() {
        return dir;
    }

    public void setXpos(int x) {
        xpos = x;
    }

    public void setYpos(int y) {
        ypos = y;
    }

    public void changeDir(Player.Direction direction) {
        dir = direction;
    }

    public void setCarriedBy(int carriedBy) {
        this.carriedBy = carriedBy;
    }
}