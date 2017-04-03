package hu.ait.minesweeper;

import hu.ait.minesweeper.GameSquare;

/**
 * Created by chaelimseo on 3/5/17.
 */

public class GameModel {

    private static GameModel instance;

    private GameSquare[][] board;
    public int level;
    private int numMines;
    private int placed;
    private int mineFree;

    private GameModel(){

        easyMode();
        mineFree = 0;

        board = new GameSquare[level][level];

        //create a 2D array board
        for (int i=0; i<level; i++){
            for (int j=0; j<level; j++){

                board[i][j]= new GameSquare();
            }
        }

        placeBomb();
    }

    private void easyMode(){

        level = 5;
        numMines = 3;
    }

    public static GameModel getInstance(){

        if (instance == null){
            instance = new GameModel();
        }

        return instance;
    }

    public void placeBomb(){

        while(placed<numMines) {

            int randi = (int) Math.floor(Math.random() * level);
            int randj = (int) Math.floor(Math.random() * level);

            if (!board[randi][randj].isBomb()) {
                board[randi][randj].setMine();
                placed++;
                System.out.println("bomb is: [" + randi + ", " + randj + "]");
            }
        }
    }

    public int getAdjMines(int i, int j) {

        //initialize the number of adjacent mines of the user guessed square as 0
        int numMine = 0;

        //if the user clicked square does not have mine,
        if (!board[i][j].isBomb()) {

            //for loop to go over the adjacent squares
            for (int r = i - 1; r < i + 2; r++) {

                for (int c = j - 1; c < j + 2; c++) {

                    //only applies to squares that are outside of the edges
                    if ((r > -1 && r < level) && (c > -1 && c < level)) {

                        //if the adjacent squares are in the board, check if the square has mines and if it has a bomb,
                        if (board[r][c].isBomb()) {
                            //increment the number of mines
                            numMine++;
                        }
                    }
                }
            }//end of the for loop
        }

        else if (board[i][j].isBomb()) {
            numMine = -1;
        }

        //set the number of adjacent mines equal to incremented numMines
        board[i][j].setNumAdjMine(numMine);

        //number of adjacent mines equals number of adjacent squares
        return board[i][j].getNumAdjMine();
    }

    public int getLevel(){

        return level;
    }

    public GameSquare getSquare(int x, int y){

        return board[x][y];
    }

    public void flip(int x, int y){

        board[x][y].flipOver();

        if (!board[x][y].isBomb()){
            mineFree++;
        }
    }

    public int getNumMines(){

        return numMines;
    }

    public void restartGame(){

        board = new GameSquare[level][level];

        //create a 2D array board
        for (int i=0; i<level; i++){
            for (int j=0; j<level; j++){

                board[i][j]= new GameSquare();
            }
        }

        placed = 0;
        placeBomb();
    }
}
