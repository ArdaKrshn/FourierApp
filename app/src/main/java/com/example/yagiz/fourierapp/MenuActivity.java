package com.example.yagiz.fourierapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
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

    }


    public void goToQR(View view){

        Log.i("MainActivityButton", "goToQR button is pressed");

        BluetoothManagerService.sendData("ProgrammingMode");

        Intent i = new Intent(MenuActivity.this, QRReaderActivity.class);

        //Change the activity.
       // i.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity
        startActivity(i);
    }

    public void goToManualDrive(View view){

        Log.i("MainActivityButton", "goToManualDrive button is pressed");

        BluetoothManagerService.sendData("ManualControlMode");

        Intent i = new Intent(MenuActivity.this, ManualControlActivity.class);

        //Change the activity.
     //   i.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity
        startActivity(i);
    }

    public void goToLineFollowing(View view){


        BluetoothManagerService.sendData("LineFollowingMode");


        Intent i = new Intent(MenuActivity.this, LineFollowingActivity.class);

        //Change the activity.
     //   i.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity
        startActivity(i);

    }

    public void goToRoomba(View view){

        BluetoothManagerService.sendData("RoombaMode");

        Intent i = new Intent(MenuActivity.this, RoombaActivity.class);

        //Change the activity.
      //  i.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity
        startActivity(i);

    }


    private void checkConnectionStatus(View view){
        msg(BluetoothManagerService.getConnectionStatus());
    }

  //  private void checkConnectionStatus(){
  //      msg(BluetoothManagerService.getConnectionStatus());
    //}

    // fast way to call Toast
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }




}
