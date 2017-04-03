package hu.ait.minesweeper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.icu.text.TimeZoneFormat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by chaelimseo on 3/5/17.
 */

public class GameView extends View {

    private int level;
    private Paint paintLine;
    private Paint paintNum;
    private Paint paintFlag;
    private Paint paintMine;
    private Paint paintSquare;
    private boolean isToggleOn;
    private Bitmap bitmapBG;
    private Bitmap bitmapBomb;
    private Bitmap bitmapFlag;
    private int foundMine;
    private int foundFlag;
    private String num;
    private int result;
    private boolean over;

    public GameView(Context context, AttributeSet attrs){

        super(context, attrs);

        num = "";
        level = GameModel.getInstance().getLevel();
        isToggleOn = false;

        foundMine = 0;
        foundFlag = 0;

        paintLine = new Paint();
        paintLine.setColor(Color.BLACK);
        paintLine.setStrokeWidth(5);

        paintSquare = new Paint();
        paintLine.setColor(Color.GRAY);

        paintMine = new Paint();
        paintMine.setColor(Color.RED);

        paintFlag = new Paint();
        paintFlag.setColor(Color.GREEN);

        paintNum = new Paint();
        paintNum.setColor(context.getResources().getColor(R.color.colorNumber));
        paintNum.setTextSize(45);

        bitmapBG = BitmapFactory.decodeResource(getResources(), R.drawable.naturebg);
        bitmapBomb = BitmapFactory.decodeResource(getResources(), R.drawable.bomb);
        bitmapFlag = BitmapFactory.decodeResource(getResources(), R.drawable.flag);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        bitmapBG= Bitmap.createScaledBitmap(bitmapBG, getWidth(), getHeight(), false);
        bitmapBomb= Bitmap.createScaledBitmap(bitmapBomb, getWidth()/level, getHeight()/level, false);
        bitmapFlag= Bitmap.createScaledBitmap(bitmapFlag, getWidth()/level, getHeight()/level, false);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmapBG, 0, 0, null);
        drawGameBoard(canvas);
        drawSquare(canvas);

        if(over) {
            drawBombs(canvas);
        }
    }


    private void drawGameBoard(Canvas canvas) {

        for (int i=1; i<level; i++){

            //four horizontal lines
            canvas.drawLine(0, i* getHeight()/level , getWidth(), i* getHeight()/level, paintLine);

            //four vertical lines
            canvas.drawLine(i* getWidth() / level , 0, i* getWidth() / level, getHeight(),paintLine);
        }

    }

    private void drawSquare(Canvas canvas){

        for (int i=0; i<level; i++){
            for (int j=0; j<level; j++){

                GameSquare current = GameModel.getInstance().getSquare(i,j);

                //if the square is revealed
                if (current.isRevealed){

                    //if it's a flag
                    if (current.isFlag()){
                        float centerX = i * getWidth() / level ;
                        float centerY = j * getHeight() / level ;

                        //canvas.drawCircle(centerX, centerY,50, paintFlag);

                        canvas.drawBitmap(bitmapFlag, centerX, centerY, null);
                        System.out.println("is flag: " + i + ", " + j);

                    }


                    //and if it's a mine
                    else if (current.isBomb()) {

                        float left = i * getWidth() / level ;
                        float top = j * getHeight() / level ;

                        System.out.println("is bomb, draw the bit map");

                        canvas.drawBitmap(bitmapBomb, left, top, null);
                    }


                    else{

                        //display the number
                        float centerX = i * getWidth() / level + getWidth() / (level*2);
                        float centerY = j * getHeight() / level + getHeight() / (level*2);

                        num = Integer.toString(GameModel.getInstance().getAdjMines(i, j));

                        canvas.drawText(num, centerX, centerY, paintNum);

                    }
                }

            }
        }
    }

    public void setToggleOn(boolean on){

        isToggleOn = on;
    }

    private void drawBombs(Canvas canvas){

        for (int i=0; i<level; i++){
            for (int j= 0; j<level; j++){

                GameSquare current = GameModel.getInstance().getSquare(i, j);
                if (current.isBomb() && !current.isFlag()){

                    float left = i * getWidth() / level ;
                    float top = j * getHeight() / level ;

                    System.out.println("is bomb, draw the bit map");
                    canvas.drawBitmap(bitmapBomb, left, top, null);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!over) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                int tx = ((int) event.getX()) / (getWidth() / level);
                int ty = ((int) event.getY()) / (getWidth() / level);

                GameSquare current = GameModel.getInstance().getSquare(tx, ty);

                GameModel.getInstance().flip(tx, ty);

                //Try mode
                if (!isToggleOn) {

                    if (!current.isBomb()) {

                        result = 0;

                    } else {

                        //game ends
                        result = -1;
                    }
                }

                //Flag field mode
                else {

                    current.setFlag();
                    System.out.println("flag: " + tx + ", " + ty);

                    //if there is mine,
                    if (current.isBomb() && current.isFlag()) {

                        foundMine++;

                        if (foundMine == GameModel.getInstance().getNumMines()) {

                            //game ends and the player wins
                            result = 1;
                        }
                    }

                    //if there is no mine,
                    else {

                        //game ends, user lost
                        result = -1;
                    }

                }

                invalidate();
                gameOver();
            }
        }

        return true;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int d = w == 0 ? h : h == 0 ? w : w < h ? w : h;
        setMeasuredDimension(d, d);
    }

    public void restart(){

        GameModel.getInstance().restartGame();
        over = false;
        result = 0;
        foundMine = 0;
        foundFlag = 0;
        invalidate();
    }

    public void gameOver(){

        switch (result){

            case -1: Toast.makeText(getContext(), "BOOM, You Lost", Toast.LENGTH_SHORT).show();
                over = true;
                break;

            case 1:  Toast.makeText(getContext(), "Congratulations, You Won!", Toast.LENGTH_SHORT).show();
                over = true;
                break;

            case 0: over = false;
                break;
        }
    }
}
