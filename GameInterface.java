import java.io.*;

interface GameInterface extends Serializable{

    public boolean isOn();
    public void movePlayer();
    public String getWinner();
    public void gameOver();
    public String toString();   
}