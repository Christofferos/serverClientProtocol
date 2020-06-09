import java.io.*;
import java.net.*;
import java.awt.event.*;

public class ClientThread extends Thread {
    private DataInputStream dataIn;
    private Game game;
    private Player player;
    private Socket sckt;

    public ClientThread(DataInputStream dataIn, Game game, Player player, Socket sckt) {
        this.dataIn = dataIn;
        this.game = game;
        this.player = player;
        this.sckt = sckt;
    }

    /* Reads and handles input from client.
    */
    public void run() {
        String in = "";
        while (game.isOn()) {
            in = recieve();
            game = updateGame(in);
            game.setUpdated(true);
        }
    }

    /* Reads input from client.
    */
    public String recieve() {
        String input = "";
        try {
			if (game.isOn()) {
				input = (String) dataIn.readUTF();
			}
		} catch (IOException ioe) {
			System.out.println("något IO fel intraffade!" + ioe);

        }
		return input;
    }
    
    /* Updates game-object according to key input from a client.
    */
    public Game updateGame(String in) {
        int keyCode = 0;
        try {
            if (containsOnlyDigits(in)) // Ignore input if it is not an integer in string format
                keyCode = Integer.parseInt(in);
        } catch (Exception e) {
            System.out.println("Illegal argument exception.");
        }
        if (keyCode == KeyEvent.VK_UP) {
            player.changeDir(Player.Direction.UP);
            game.movePlayer(player.getID());
        } else if (keyCode == KeyEvent.VK_DOWN) {
            player.changeDir(Player.Direction.DOWN);
            game.movePlayer(player.getID());
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            player.changeDir(Player.Direction.RIGHT);
            game.movePlayer(player.getID());
        } else if (keyCode == KeyEvent.VK_LEFT) {
            player.changeDir(Player.Direction.LEFT);
            game.movePlayer(player.getID());
        } else if (keyCode == KeyEvent.VK_SPACE) {
            game.throwBall(player);
        }
        
        return game;
    }


    /* Returns true if a string only contains digits.
    */
    public boolean containsOnlyDigits(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i)))
                return false;
        } 
        return true;
    }
}