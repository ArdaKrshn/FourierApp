package com.example.yagiz.fourierapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class ManualControlActivity extends AppCompatActivity {

    ImageButton startStop, forward, backward, leftward, rightward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manual_control);

        View decorView;
        decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

        startStop = (android.widget.ImageButton) findViewById(R.id.startStop);
        forward = (android.widget.ImageButton) findViewById(R.id.forward);
        backward = (android.widget.ImageButton) findViewById(R.id.backward);
        leftward = (android.widget.ImageButton) findViewById(R.id.leftward);
        rightward = (android.widget.ImageButton) findViewById(R.id.rightward);

        //commands to be sent to bluetooth
        startStop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startStop();      //method to turn on
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                forward();   //method to turn off
            }
        });

        backward.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backward(); //close connection
            }
        });
        leftward.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                leftward(); //close connection
            }
        });
        rightward.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                rightward(); //close connection
            }
        });


    }

    //private void Disconnect()
    //{
      //  if (btSocket != null) //If the btSocket is busy
        //{
          //  try {
            //    btSocket.close(); //close connection
            //} catch (IOException e) { msg("Error"); }
      //  }
      //  finish(); //return to the first layout

    //}


    private void startStop() { BluetoothManagerService.sendData("StartStop"); }

    private void forward()
    {
        BluetoothManagerService.sendData("GoForward");
    }

    private void backward()
    {
        BluetoothManagerService.sendData("GoBackward");
    }

    private void leftward()
    {
        BluetoothManagerService.sendData("GoLeft");
    }

    private void rightward()
    {
        BluetoothManagerService.sendData("GoRight");
    }


    // fast way to call Toast
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {

        //Let the robot know that the user exited the mode
        BluetoothManagerService.sendData("OutOfMode");
        super.onDestroy();

    }
}
