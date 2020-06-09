import java.net.*;
import java.io.*;

public class Client {
	public static void main(String[] args) {
		String game = "";
		DataOutputStream dataOut = null;
		DataInputStream dataIn = null;
		try {
			System.out.println("Väntar på annan spelare!");
			Socket sckt = new Socket("localhost", 1234);
			dataOut = new DataOutputStream(sckt.getOutputStream());
			dataIn = new DataInputStream(sckt.getInputStream());

			// Start a new thread that will listen for key input and send it to the server.
			new KeyEventListener(dataOut);

			do {
				// Print game-string from the server.
				game = (String) dataIn.readUTF();
				System.out.println(game);

			} while (game.length() > "Player1 wins!".length());

		} catch (IOException ioe) {
			System.out.println("nagot IO fel intraffade!" + ioe);
		}
	}
}
