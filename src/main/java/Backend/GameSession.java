package Backend;

public class GameSession {

    private CLientHandler player1;
    private CLientHandler player2;

    private char[][] board;
    private int size;

    private boolean isPlayer1Turn = true;
    private boolean gameOver = false;

    public GameSession(CLientHandler player1, CLientHandler player2, int size){
        this.player1 = player1;
        this.player2 = player2;
        this.size = size;

        board = new char[size][size];

        // заполнение поля
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = ' ';
            }
        }

        player1.setSession(this);
        player2.setSession(this);

        startGame();
    }

    private void startGame(){
        player1.sendMessage("START X");
        player2.sendMessage("START 0");

        sendTurn();
        sendState();
    }


    public void handleMessage(CLientHandler sender, String message){
        if(gameOver) return;

        if(!message.startsWith("MOVE")) return;
        String[] parts = message.split(" ");
        int x = Integer.parseInt(parts[1]);
        int y = Integer.parseInt(parts[2]);

        //проверка границ
        if(x < 0 || x >= size || y < 0 || y>= size){
            sender.sendMessage("INVALID");
            return;
        }
        // клетка занята
        if(board[x][y] != ' '){
            sender.sendMessage("INVALID");
            return;
        }

        // проверка очереди
        if(sender == player1 && !isPlayer1Turn ||
                sender == player2 && isPlayer1Turn){
            sender.sendMessage("NOT YOUR TURN");
            return;
        }

        // делаем ход
        if(sender == player1){
            board[x][y] = 'X';
            isPlayer1Turn = false;
        } else{
            board[x][y] = 'O';
            isPlayer1Turn = true;
        }

        sendState();

        // проверка победы
        if(checkWin(x,y)){
            sender.sendMessage("WIN");
            getOpponent(sender).sendMessage("LOSE");
            gameOver = true;
            return;
        }

        //проверка ничьи
        if(isDraw()){
            player1.sendMessage("DRAW");
            player2.sendMessage("DRAW");
            gameOver = true;
            return;
        }
    }

    // ЛОГИКА

    private boolean checkWin(int x, int y){
        char symbol = board[x][y];

        //строка
        boolean win = true;
        for (int i = 0; i < size; i++) {
            if(board[x][i] != symbol){
                win = false;
                break;
            }
        }
        if(win) return  true;

        //столбец
        win = true;
        for (int i = 0; i < size; i++) {
            if(board[i][y] != symbol){
                win = false;
                break;
            }
        }
        if(win) return  true;

        // главная диагональ
        if(x == y){
            win = true;
            for (int i = 0; i < size; i++) {
                if(board[i][i] != symbol){
                    win = false;
                    break;
                }
            }
            if (win) return true;
        }

        // побочная диагональ
        if (x + y == size - 1) {
            win = true;
            for (int i = 0; i < size; i++) {
                if (board[i][size - 1 - i] != symbol) {
                    win = false;
                    break;
                }
            }
            if (win) return true;

        }
        return false;
    }

    private boolean isDraw(){
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(board[i][j] == ' ') return false;
            }
        }
        return true;
    }

    // Сетевое взаимодействие

    private void sendState(){
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                sb.append(board[i][j] == ' ' ? '_' : board[i][j]);
            }
        }

        String state = "STATE" + sb;

        player1.sendMessage(state);
        player2.sendMessage(state);


    }
    private void sendTurn(){
        if(isPlayer1Turn){
            player1.sendMessage("YOUR TURN");
            player2.sendMessage("WAIT");
        } else{
            player2.sendMessage("YOUR TUNR");
            player1.sendMessage("WAIT");
        }
    }

    private  CLientHandler getOpponent(CLientHandler player){
        return  (player == player1) ? player2 : player1;
    }
}
