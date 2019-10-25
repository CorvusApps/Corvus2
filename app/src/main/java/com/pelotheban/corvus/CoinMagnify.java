package com.pelotheban.corvus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class CoinMagnify extends AppCompatActivity {


   ImageView imgCoinMagCoinImageX;
   String CoinMagImageLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_magnify);

        CoinMagImageLink = getIntent().getStringExtra("imagelink");


        imgCoinMagCoinImageX = findViewById(R.id.imgCoinMagCoinImage);



        Picasso.get().load(CoinMagImageLink).into(imgCoinMagCoinImageX); //tutorial had with which got renamed to get but with took ctx as parameter...

    }
}
