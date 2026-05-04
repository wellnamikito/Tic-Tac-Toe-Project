package Backend;

public class GameSession {

    private CLientHandler player1;
    private CLientHandler player2;

    public GameSession(CLientHandler player1, CLientHandler player2){
        this.player1 = player1;
        this.player2 = player2;

        player1.setSession(this);
        player2.setSession(this);

        startGame();
    }

    private void startGame(){
        player1.sendMessage("START X");
        player2.sendMessage("START 0");

        player1.sendMessage("YOUR TURN");
        player2.sendMessage("WAIT");
    }

    //пока просто пересылаем ходы
    public void handleMessage(CLientHandler sender, String message){
        if(sender == player1){
            player2.sendMessage(message);
        } else {
            player1.sendMessage(message);
        }
    }
}
