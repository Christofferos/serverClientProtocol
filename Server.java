import java.io.*;
import java.net.*;

public class Server {
	public static void main(String[] args) throws Exception {
		ServerSocket serverSckt = new ServerSocket(1234);
		while (true) {
			Game game = new Game();
			String input;
			Socket sckt1 = serverSckt.accept();
			Socket sckt2 = serverSckt.accept();
			DataInputStream dataIn1 = new DataInputStream(sckt1.getInputStream());
			DataOutputStream dataOut1 = new DataOutputStream(sckt1.getOutputStream());
			DataInputStream dataIn2 = new DataInputStream(sckt2.getInputStream());
			DataOutputStream dataOut2 = new DataOutputStream(sckt2.getOutputStream());

			// Start two new threads that will handle input from each client.
			ClientThread cThread1 = new ClientThread(dataIn1, game, game.getPlayer(1), sckt1);
			ClientThread cThread2 = new ClientThread(dataIn2, game, game.getPlayer(2), sckt2);
			cThread1.start();
			cThread2.start();

			while (game.isOn()) {
				try {
					if (game.isUpdated()) {
						if (game.isOn()) {

							send(sckt1, game.toString() + "\nEgna liv: " + game.getPlayer(1).getLives()
									+ "\nMotståndarens liv: " + game.getPlayer(2).getLives(), dataOut1);
							send(sckt2, game.toString() + "\nEgna liv: " + game.getPlayer(2).getLives()
									+ "\nMotståndarens liv: " + game.getPlayer(1).getLives(), dataOut2);
						}

						game.setUpdated(false);
					}
					// If it is not possible to play anymore
					// send the game-string to the first client.
					if (!game.isOn())
						send(sckt1, game.toString(), dataOut1);

					// Send end-game message to each client.
					if (game.getPlayer(2).isDefeated()) {
						send(sckt1, "You win!", dataOut1);
						send(sckt2, "You lose!", dataOut2);
					} else if (game.getPlayer(1).isDefeated()) {
						send(sckt1, "You lose!", dataOut1);
						send(sckt2, "You win!", dataOut2);
					}

				} catch (GameException ge) {
					Socket causingSckt = ge.getCausingSocket();
					if (causingSckt == sckt1)
						send(sckt1, game.toString(), dataOut2);
					else
						send(sckt2, game.toString(), dataOut1);
					break;
				}
			}
			sckt1.close();
			sckt2.close();
		}
	}

	/* Sends a game-string to a client.
	*/
	private static void send(Socket sckt, String game, DataOutputStream dataOut) throws GameException {
		try {
			dataOut.writeUTF(game);

		} catch (IOException ioe) {
			System.out.println("något fel intraffade!" + ioe);

		}
	}
}
