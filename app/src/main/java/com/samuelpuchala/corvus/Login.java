package com.samuelpuchala.corvus;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth firebaseAuth;
    CallbackManager callbackManager;

    LoginButton btnFBLoginX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_login);
       // printKeyHash();

        firebaseAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();

        btnFBLoginX = findViewById(R.id.btnFBLogin);
        btnFBLoginX.setReadPermissions("email");
        btnFBLoginX.setOnClickListener(this);


    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        // updateUI(currentUser); // not working and morteza doesn't even mention it

        if (currentUser != null){

            Intent intent = new Intent(Login.this, Home.class);
            startActivity(intent);
            finish();
        }


    }

    @Override
    public void onClick(View view) {
       // Toast.makeText(Login.this, "button works", Toast.LENGTH_LONG).show();

        switch (view.getId()){

            case R.id.btnFBLogin:

                signIn();

                break;

        }

    }

    private void signIn() {

        btnFBLoginX.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                handleFacebookAccessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


    }

    private void handleFacebookAccessToken(AccessToken accessToken) {

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("ERROR_COMT", ""+e.getMessage());

            }
        }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                String email = authResult.getUser().getEmail();
                Toast.makeText(Login.this, "You are signed in with email: " + email, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(Login.this, Home.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //    private void printKeyHash() {
//
//        try {
//
//            PackageInfo info = getPackageManager().getPackageInfo("com.samuelpuchala.corvus",
//                    PackageManager.GET_SIGNATURES);
//
//            for (Signature signature:info.signatures) {
//
//                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
//                messageDigest.update(signature.toByteArray());
//                Log.e("KEYHASH", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
//
//            }
//
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//
//    }
}
