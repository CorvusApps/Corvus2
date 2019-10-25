package com.pelotheban.corvus;


import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;


public class Login extends AppCompatActivity implements View.OnClickListener {

    ConstraintLayout loutLoginX;
    FirebaseAuth firebaseAuth;

    CallbackManager callbackManager;

    LoginButton btnFBLoginX;
    Button btnFBLoginOverlayX;

    GoogleSignInClient mGoogleSignInClient;

    SignInButton btnGoogleLoginX;
    Button btnGoogleLoginOverlayX;

    ImageView imgLoginCrowX;

    int signInProvider; // 0 initial, 1 = google, 2 = facebook

    String email;

    TextView txtShieldX;
    ProgressBar pbFBLoginX;

    private AlertDialog dialog;
    TextView txtOneTimeInfoLoginX;

    //// TEST SHIT /////

    EditText edtEmailX, edtPassX;
    Button btnSignUpX;



    //// TEST SHIT ////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
       // printKeyHash();

       loutLoginX = findViewById(R.id.loutLogin);

        firebaseAuth = FirebaseAuth.getInstance();

        callbackManager = CallbackManager.Factory.create();

        btnFBLoginX = findViewById(R.id.btnFBLogin);
        btnFBLoginX.setReadPermissions("email");
        btnFBLoginX.setOnClickListener(this);

        btnFBLoginOverlayX = findViewById(R.id.btnFBLoginOverlay);
        btnFBLoginOverlayX.setOnClickListener(this);

        btnGoogleLoginX = (SignInButton) findViewById(R.id.btnGoogleLogin);
        btnGoogleLoginX.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) //getString(R.string.default_web_client_id
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        btnGoogleLoginOverlayX = findViewById(R.id.btnGoogleLoginOverlay);
        btnGoogleLoginOverlayX.setOnClickListener(this);

        signInProvider = 0;

        imgLoginCrowX = findViewById(R.id.imgLoginCrow);
        imgLoginCrowX.setAlpha(0.5f);

        txtShieldX = findViewById(R.id.txtShield);
        txtShieldX.setAlpha(0f);

        pbFBLoginX = findViewById(R.id.pbFBLogin);
        pbFBLoginX.setAlpha(0f);

        txtOneTimeInfoLoginX = findViewById(R.id.txtOneTimeInfoLogin);
        txtOneTimeInfoLoginX.setOnClickListener(this);


        //// TEST SHIT /////

       edtEmailX = findViewById(R.id.edtEmail);
       edtPassX = findViewById(R.id.edtPass);
       btnSignUpX = findViewById(R.id.btnSingUp);
       btnSignUpX.setOnClickListener(this);

        //// TEST SHIT ////


    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null){

           // Toast.makeText(Login.this, currentUser.toString(), Toast.LENGTH_LONG).show();

            Intent intent = new Intent(Login.this, HomePage.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onClick(View view) {


        switch (view.getId()){

            case R.id.btnFBLoginOverlay:

                btnFBLoginX.performClick();
                signInProvider = 2;
                fBsignIn();


                break;

            case R.id.btnGoogleLoginOverlay:

                btnGoogleLoginX.performClick();
                signInProvider = 1;
                googleSignIn();

                break;

            case R.id.txtOneTimeInfoLogin:

                oneTimeInfoLogin();

                break;

            case R.id.btnSingUp:

                Toast.makeText(Login.this, "button pressed", Toast.LENGTH_SHORT).show();

                signUp();


                break;

        }

    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 101);
    }

    private void fBsignIn() {

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

        txtShieldX.setAlpha(1f);
        pbFBLoginX.setAlpha(1f);

        firebaseAuth.signInWithCredential(credential).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(loutLoginX, e.getMessage(), Snackbar.LENGTH_INDEFINITE).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                email = authResult.getUser().getEmail();

                loginSnackbar();

                transitionToHome();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (signInProvider == 2) {

            callbackManager.onActivityResult(requestCode, resultCode, data);

        } else if (signInProvider == 1){


            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == 101) {


                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount acct = task.getResult(ApiException.class);

                    firebaseAuthWithGoogle(acct);
                } catch (ApiException e) {


                    Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        Log.d("GOOGLE", "firebaseAuthWithGoogle:" + acct.getId());

        txtShieldX.setAlpha(1f);
        pbFBLoginX.setAlpha(1f);

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // Sign in success, update I with the signed-in user's information

                        FirebaseUser user = firebaseAuth.getCurrentUser();

                        email = authResult.getUser().getEmail();

                        loginSnackbar();

                        transitionToHome();

                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Snackbar.make(loutLoginX, e.getMessage(), Snackbar.LENGTH_INDEFINITE).show();
            }
        });

    }

    private void transitionToHome () {

        new CountDownTimer(2000, 500) {

            public void onTick(long millisUntilFinished) {
                // imgCoverR.animate().rotation(360).setDuration(500); // why only turned once?
            }

            public void onFinish() {
                Intent intent = new Intent(Login.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        }.start();

    }

    private void loginSnackbar(){

       Snackbar snackbar;

        snackbar = Snackbar.make(loutLoginX, "Welcome: " + email, Snackbar.LENGTH_INDEFINITE);

        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getColor(R.color.colorAccent));

        snackbar.show();

        // THE COLOR SET BELOW WORKS but the default is white which is what we want; keeping code for reference
        int snackbarTextId = com.google.android.material.R.id.snackbar_text;
        TextView textView = (TextView)snackbarView.findViewById(snackbarTextId);
        textView.setTextSize(18);

    }

    private void oneTimeInfoLogin(){

        //Everything in this method is code for a custom dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.zzzz_otinfo_login, null);

        dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        dialog.show();

        Button btnOKoneTimeLoginX = view.findViewById(R.id.btnOKoneTimeLogin);
        btnOKoneTimeLoginX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();

    }

// This is the the code for generating the key hash required to link project to facebook; saving code for next app

    //    private void printKeyHash() {
//
//        try {
//
//            PackageInfo info = getPackageManager().getPackageInfo("com.pelotheban.corvus",
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

    private void signUp () {

        Toast.makeText(Login.this, "in sign up", Toast.LENGTH_SHORT).show();

        firebaseAuth.createUserWithEmailAndPassword(edtEmailX.getText().toString(),
                edtPassX.getText().toString()).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()) {

                            Toast.makeText(Login.this, "Authentication was successful",
                                    Toast.LENGTH_SHORT).show();

                            FirebaseDatabase.getInstance().getReference().child("my_users").
                                    child(task.getResult().getUser().getUid()).child("username").
                                    setValue(edtEmailX.getText().toString());

                            // BELOW to get display name (which will be the same as username but live in the Auth - this modifies the Auth

//                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                                    .setDisplayName(edtUserNameX.getText().toString()).build();
//
//                            FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates)
//                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//
//                                            if (task.isSuccessful()) {
//
//                                                Toast.makeText(MainActivity.this, "Display name added",
//                                                        Toast.LENGTH_SHORT).show();
//                                            }
//
//                                        }
//                                    });



                            Intent intent = new Intent(Login.this, HomePage.class);
                            startActivity(intent);

                        }else{

                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }


}
