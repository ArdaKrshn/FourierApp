package com.example.yagiz.fourierapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class QRReaderActivity extends AppCompatActivity {

    private Button scanButton;
    private Button sendButton;
    private Dictionary dictionary = new Dictionary();
    ListView codesList;
    ArrayList<String> codes;
    ArrayAdapter<String> arrayAdapter;
    // SharedPreferences sharedPreferences=this.getSharedPreferences("com.example.yagiz.fourierapp", Context.MODE_PRIVATE);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrreader);

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

        scanButton = (Button) findViewById(R.id.scanButton);
        sendButton = (Button) findViewById(R.id.sendButton);
        codesList = findViewById(R.id.codesList);
        codes = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,codes);
        codesList.setAdapter(arrayAdapter);
        final Activity activity = this;
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Tara");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new CodesSender().execute();

            }
        });

        codesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder adb= new AlertDialog.Builder(QRReaderActivity.this);
                adb.setTitle("Silmek istediginize emin misiniz?");
                final int positionToRemove = position;
                adb.setNegativeButton("Iptal",null);
                adb.setPositiveButton("Evet",new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which){

                        codes.remove(positionToRemove);
                        arrayAdapter.notifyDataSetChanged();

                    }
                });
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result != null){

            if(result.getContents()==null){
                Toast.makeText(this,"Taramayı durdurdunuz!", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                codes.add(result.getContents());
                arrayAdapter.notifyDataSetChanged();
            }

        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    protected void onDestroy() {

        //Let the robot know that the user exited the mode
        BluetoothManagerService.sendData("OutOfMode");
        super.onDestroy();

    }

    private class CodesSender{

        String codesAsString = dictionary.translateCodes(codes);

        protected void execute(){

            if(isRunnable())
                sendCodes();
            //Implement the user feedback

        }

        protected boolean isRunnable(){

            int numberOfStartingBrackets = 0;
            int numberOfEndingBrackets=0;

            for(int i=0; i<codesAsString.length(); i++){

                char pseudoAsChar = codesAsString.charAt(i);

                if(pseudoAsChar == dictionary.translateString("While")||pseudoAsChar == dictionary.translateString("If")){

                    char nextChar;

                    try{

                        nextChar=codesAsString.charAt(i+1);

                    }catch (NullPointerException e){

                        msg("Code cannot end with a while or if");
                        return false;

                    }

                    if(isCondition(nextChar)){

                        try{

                            nextChar=codesAsString.charAt(i+2);

                        }catch (NullPointerException e){

                            msg("Code cannot end with a condition");
                            return false;

                        }


                        if(nextChar == dictionary.translateString("StartOfBlock")){

                        }
                        else{

                            msg("There should be a starting block after the condition");
                            return false;
                        }


                    }

                    else

                        msg("Condition missing after a while or if");
                    return false;



                }

                if(pseudoAsChar == dictionary.translateString("Repeat2")||pseudoAsChar == dictionary.translateString("Repeat3")||pseudoAsChar == dictionary.translateString("Repeat4")||pseudoAsChar == dictionary.translateString("Forever")){

                    char nextChar;

                    try{

                        nextChar=codesAsString.charAt(i+1);

                    }catch (NullPointerException e){

                        msg("Code cannot end with a Repeat command");
                        return false;

                    }

                    if(nextChar == dictionary.translateString("StartOfBlock")){
                        //Do nothing
                    }
                    else{

                        msg("Missing start of block after Repeat");
                        return false;
                    }


                }

                if(pseudoAsChar == dictionary.translateString("StartOfBlock"))
                    numberOfStartingBrackets++;

                if(pseudoAsChar == dictionary.translateString("EndOfBlock"))
                    numberOfEndingBrackets++;

                if(numberOfEndingBrackets>numberOfStartingBrackets) {
                    msg("Excess number of EndOfBlock");
                    return false;
                }


            }

            if(numberOfEndingBrackets==numberOfStartingBrackets)
                return true;
            else {
                msg("Excess number of StartOfBlock");
                return false;
            }
        }

        protected void sendCodes(){

            BluetoothManagerService.sendCodes(codesAsString);

        }

        protected boolean isCondition(char pseudoAsChar){

            if(pseudoAsChar==dictionary.translateString("Dark")||pseudoAsChar==dictionary.translateString("Light")||pseudoAsChar==dictionary.translateString("Near")||pseudoAsChar==dictionary.translateString("Quiet")||pseudoAsChar==dictionary.translateString("Loud"))
                return true;

            else
                return false;

        }

    }

    // fast way to call Toast
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

}
