import java.net.*;

public class GameException extends Exception{
    private String errorMsg;
    private Socket causingSckt;

    public GameException(String msg, Socket sckt){
	errorMsg=msg;
	causingSckt=sckt;
    }

    public Socket getCausingSocket(){
	return causingSckt;
    }
    
}
