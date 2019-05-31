package com.samuelpuchala.corvus;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ImageView imgCoverR;
    private TextView txtPicMaskX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgCoverR = findViewById(R.id.imgCover);
        txtPicMaskX = findViewById(R.id.txtPicMask);

        txtPicMaskX.animate().alpha(0f).setDuration(3000);
        imgCoverR.animate().rotation(1440).setDuration(3000);

        new CountDownTimer(3500, 500) {

            public void onTick(long millisUntilFinished) {
               // imgCoverR.animate().rotation(360).setDuration(500); // why only turned once?
            }

            public void onFinish() {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                //finish();
            }
        }.start();




    }


}
