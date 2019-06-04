package com.samuelpuchala.corvus;

import android.content.Intent;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ImageView imgCoverR, imgCoin1X, imgCoin2X, imgCoin3X, imgCoin4X, imgCoin5X, imgCoin6X;
    private TextView txtPicMaskX;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgCoverR = findViewById(R.id.imgCover);
        txtPicMaskX = findViewById(R.id.txtPicMask);

        txtPicMaskX.animate().alpha(0f).setDuration(4500);

        imgCoin1X = findViewById(R.id.imgCoin1);
        imgCoin2X = findViewById(R.id.imgCoin2);
        imgCoin3X = findViewById(R.id.imgCoin3);
        imgCoin4X = findViewById(R.id.imgCoin4);
        imgCoin5X = findViewById(R.id.imgCoin5);
        imgCoin6X = findViewById(R.id.imgCoin6);

        imgCoin1X.animate().rotation(1440).setDuration(2000);
        imgCoin2X.animate().rotation(1440).setDuration(2500);
        imgCoin3X.animate().rotation(1440).setDuration(3000);
        imgCoin4X.animate().rotation(1440).setDuration(3500);
        imgCoin5X.animate().rotation(1440).setDuration(4000);
        imgCoin6X.animate().rotation(1440).setDuration(4500);


        new CountDownTimer(5000, 500) {

            public void onTick(long millisUntilFinished) {
               // imgCoverR.animate().rotation(360).setDuration(500); // why only turned once?
            }

            public void onFinish() {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        }.start();

    }


}
