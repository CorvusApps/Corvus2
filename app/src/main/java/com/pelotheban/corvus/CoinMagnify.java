package com.pelotheban.corvus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class CoinMagnify extends AppCompatActivity {

   TextView txtCoinMagImageLinkX;
   String CoinMagImageLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_magnify);

        CoinMagImageLink = getIntent().getStringExtra("imagelink");

        txtCoinMagImageLinkX = findViewById(R.id.txtCoinMagImageLink);

        txtCoinMagImageLinkX.setText(CoinMagImageLink);

    }
}
