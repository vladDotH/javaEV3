package c.vlad.serialtest2;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN";

    private EV3 bot;

    private int speed = 30;

    private SeekBar speedBar;

    private Button Aup, Adown,
            Bup, Bdown,
            Cup, Cdown,
            Dup, Ddown;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "Programme launching...");

        bot = new EV3();


        Aup = (Button) findViewById(R.id.Aup);
        Adown = (Button) findViewById(R.id.Adown);

        Bup = (Button) findViewById(R.id.Bup);
        Bdown = (Button) findViewById(R.id.Bdown);

        Cup = (Button) findViewById(R.id.Cup);
        Cdown = (Button) findViewById(R.id.Cdown);

        Dup = (Button) findViewById(R.id.Dup);
        Ddown = (Button) findViewById(R.id.Ddown);

        speedBar = (SeekBar) findViewById(R.id.speed);

        speedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speed = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        Aup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        bot.A.setSpeed(speed);
                        bot.A.start();
                        break;

                    case MotionEvent.ACTION_UP:
                        bot.A.setSpeed(0);
                        bot.A.stopFloat();
                        break;
                }
                return true;
            }
        });

        Adown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        bot.A.setSpeed(-speed);
                        bot.A.start();
                        break;

                    case MotionEvent.ACTION_UP:
                        bot.A.setSpeed(0);
                        bot.A.stopFloat();
                        break;
                }
                return true;
            }
        });

        Bup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        bot.B.setSpeed(speed);
                        bot.B.start();
                        break;

                    case MotionEvent.ACTION_UP:
                        bot.B.setSpeed(0);
                        bot.B.stopFloat();
                        break;
                }
                return true;
            }
        });

        Bdown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        bot.B.setSpeed(-speed);
                        bot.B.start();
                        break;

                    case MotionEvent.ACTION_UP:
                        bot.B.setSpeed(0);
                        bot.B.stopFloat();
                        break;
                }
                return true;
            }
        });

        Cup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        bot.C.setSpeed(speed);
                        bot.C.start();
                        break;

                    case MotionEvent.ACTION_UP:
                        bot.C.setSpeed(0);
                        bot.C.stopFloat();
                        break;
                }
                return true;
            }
        });

        Cdown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        bot.C.setSpeed(-speed);
                        bot.C.start();
                        break;

                    case MotionEvent.ACTION_UP:
                        bot.C.setSpeed(0);
                        bot.C.stopFloat();
                        break;
                }
                return true;
            }
        });

        Dup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        bot.D.setSpeed(speed);
                        bot.D.start();
                        break;

                    case MotionEvent.ACTION_UP:
                        bot.D.setSpeed(0);
                        bot.D.stopFloat();
                        break;
                }
                return true;
            }
        });

        Ddown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        bot.D.setSpeed(-speed);
                        bot.D.start();
                        break;

                    case MotionEvent.ACTION_UP:
                        bot.D.setSpeed(0);
                        bot.D.stopFloat();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bot.close();
    }
}

