package hu.ait.minesweeper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    private ToggleButton toggle;
    private boolean toggleOn;
    private Button restart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final GameView gameView = (GameView) findViewById(R.id.gameView);

        toggle = (ToggleButton) findViewById(R.id.togglebt);

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Save the state here
                if (isChecked) {
                    // The toggle is enabled: flag
                    gameView.setToggleOn(true);
                    System.out.println("toggle on");

                } else {
                    // The toggle is disabled: try
                    gameView.setToggleOn(false);
                    toggle.setTextOff("Try");
                    System.out.println("toggle off");
                }
            }
        });

        restart = (Button) findViewById(R.id.restartbt);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gameView.restart();
            }
        });
    }
}
