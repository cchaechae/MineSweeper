package hu.ait.minesweeper;

/**
 * Created by chaelimseo on 3/5/17.
 */

public class GameSquare {

    boolean isMine;
    boolean isRevealed;
    boolean isFlag;
    int numAdjMine; //-1 if it's a mine

    public GameSquare(){

        isRevealed = false;
        isMine = false;
        isFlag = false;
    }

    public void setMine(){

        isMine = true;
        numAdjMine = -1;
    }

    public boolean isBomb(){

        return isMine;
    }

    public boolean isFlag(){
        System.out.println("checking flag");

        return isFlag;
    }

    public void setFlag(){

        System.out.println("setting flag");
        isFlag = true;
    }

    public void setNumAdjMine(int numAdjMine){

        this.numAdjMine = numAdjMine;
    }

    public int getNumAdjMine(){

        return numAdjMine;
    }

    public boolean flipOver(){

        isRevealed = true;

        return isRevealed;
    }


}
