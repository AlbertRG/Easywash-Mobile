package com.h2o.easywash;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TimerTask task = new TimerTask() {

            @Override
            public void run() {

                Intent intent;

                if(loginMetadata()){
                    //If you decided to save your login information
                    intent = new Intent(MainActivity.this, MenuActivity.class);
                } else {
                    //If you are not registered or simply did not want to save your login information
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                }
                //Run the corresponding Activity and delete the current Activity
                startActivity(intent);
                finish();
            }//run
        };//task

        //Object to set the timeout to 3 seconds
        Timer time = new Timer();
        time.schedule(task, 3000);
    } //onCreate

    private boolean loginMetadata(){
        SharedPreferences metadata = getSharedPreferences( "user.dat",MODE_PRIVATE);
        return metadata.getBoolean("register", false);
    }//loginMetadata

}