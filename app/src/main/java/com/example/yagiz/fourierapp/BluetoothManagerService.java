package com.example.yagiz.fourierapp;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class BluetoothManagerService extends Service {
    static private String address = null;
    //   static private ProgressDialog progress;
    static private BluetoothAdapter myBluetooth = null;
    static private BluetoothSocket btSocket = null;
    static private boolean isBtConnected = false;
    //SPP UUID. Look for it
    static private final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    static private String connectionStatus = "NotConnected";


    static Dictionary dictionary = new Dictionary();


    public BluetoothManagerService(){}

    public BluetoothManagerService(String address){

        this.address=address;

        new BluetoothManagerService.ConnectBT().execute();

    }

    public static synchronized void sendData(String pseudo)
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write((byte) dictionary.translateString(pseudo) );
            }
            catch (IOException e)
            {

            }
        }
    }

    public static synchronized void sendData(char pseudoAsChar){

        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write((byte) pseudoAsChar);
            }
            catch (IOException e)
            {

            }
        }

    }


    public static synchronized void sendCodes(String codesAsString){

        if (btSocket != null){

            for(int i=0; i<codesAsString.length(); i++){

                sendData(codesAsString.charAt(i));

                //Put some delay to ensure that the robot process the data
                try{
                    TimeUnit.SECONDS.sleep((long) 0.1);
                }catch (InterruptedException e){

                }

            }

        }

    }

    public static synchronized String getConnectionStatus() {
        return connectionStatus;
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        //@Override
        //protected void onPreExecute() {
        //progress = ProgressDialog.show(BluetoothMaster.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        //}

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket == null || !isBtConnected) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            } catch (IOException e) {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                connectionStatus="NotConnected";
                // finish(); commented out by Yagiz 28/2/2019
            } else {
                connectionStatus="Connected";
                isBtConnected = true;
            }
//            progress.dismiss();

        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
