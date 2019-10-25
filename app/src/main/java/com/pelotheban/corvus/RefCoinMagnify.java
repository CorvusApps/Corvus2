package com.pelotheban.corvus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class RefCoinMagnify extends AppCompatActivity {

    ImageView imgRefCoinMagCoinImageX;
    String RefCoinMagImageLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ref_coin_magnify);

        RefCoinMagImageLink = getIntent().getStringExtra("imagelink");


        imgRefCoinMagCoinImageX = findViewById(R.id.imgRefCoinMagCoinImage);


        Picasso.get().load(RefCoinMagImageLink).into(imgRefCoinMagCoinImageX); //tutorial had with which got renamed to get but with took ctx as parameter...

    }
}