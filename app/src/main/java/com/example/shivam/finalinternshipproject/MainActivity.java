package com.example.shivam.finalinternshipproject;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.twitter.sdk.android.core.Twitter;

public class MainActivity extends AppCompatActivity {
//This is the Splash Screen for my Project
Handler handler;
    ProgressBar progressBar;
    int progressBarTimer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        handler=new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(progressBarTimer<100){
                    progressBarTimer+=5;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressBarTimer);
                        }
                    });
                    try{
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if(progressBarTimer==100){
                    Intent intent = new Intent(MainActivity.this,TwitterLoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

        }).start();

    }
}
