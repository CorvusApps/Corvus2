package com.samuelpuchala.corvus;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.facebook.login.LoginManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.shashank.sony.fancytoastlib.FancyToast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;

import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CollectionAdd extends AppCompatActivity implements View.OnClickListener {


    private ImageView imgCollectionImageX;
    private FloatingActionButton fbtnPopUpMenuX;
    private FloatingActionButton fbtnSaveCollectionX;
    private FirebaseAuth collAddFirebaseAuth;
    private CoordinatorLayout loutCollectionAddActLOX;
    private Bitmap recievedCollectionImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_add);

        imgCollectionImageX = findViewById(R.id.imgCollectionImage);
        imgCollectionImageX.setOnClickListener(this);

        fbtnPopUpMenuX = findViewById(R.id.fbtnPopUpMenu);
        fbtnSaveCollectionX = findViewById(R.id.fbtnSaveCollection);

        fbtnPopUpMenuX.setOnClickListener(this);
        fbtnSaveCollectionX.setOnClickListener(this);

        collAddFirebaseAuth = FirebaseAuth.getInstance();

        loutCollectionAddActLOX = findViewById(R.id.loutCollectionAddActLO);


//        FloatingActionButton fab = findViewById(R.id.fbtnSaveCollection);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.imgCollectionImage:

                if(ActivityCompat.checkSelfPermission(CollectionAdd.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]
                                    {Manifest.permission.READ_EXTERNAL_STORAGE},
                            1000);
                }else {

                    getChosenImage();

                }

                break;

            case R.id.fbtnPopUpMenu:

                showPopupMenu(view, true, R.style.MyPopupOtherStyle);


                break;

            case R.id.fbtnSaveCollection:

                Toast.makeText(CollectionAdd.this, "save", Toast.LENGTH_SHORT).show();

                break;



        }

    }

    public void loginLayoutTapped (View view) {

        try { // we need this because if you tap with no keyboard up the app will crash

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    private void showPopupMenu(View anchor, boolean isWithIcons, int style) {
        //init the wrapper with style
        Context wrapper = new ContextThemeWrapper(this, style);

        //init the popup
        PopupMenu popup = new PopupMenu(wrapper, anchor);

        /*  The below code in try catch is responsible to display icons*/
        if (isWithIcons) {
            try {
                Field[] fields = popup.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if ("mPopup".equals(field.getName())) {
                        field.setAccessible(true);
                        Object menuPopupHelper = field.get(popup);
                        Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                        Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                        setForceIcons.invoke(menuPopupHelper, true);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //inflate menu
        popup.getMenuInflater().inflate(R.menu.actions, popup.getMenu());

        //implement click events
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.popMenuLogout:

                        collAddFirebaseAuth.signOut();
                        LoginManager.getInstance().logOut();
                        logoutSnackbar();

                        transitionBackToLogin ();




                        return true;

                    case R.id.popMenuFAQ:

                        FancyToast.makeText(CollectionAdd.this, "FAQ", FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show();

                        return true;

                }

                return false;
            }
        });
        popup.show();

    }

    private void logoutSnackbar(){


        Snackbar snackbar;

        snackbar = Snackbar.make(loutCollectionAddActLOX, "Good bye", Snackbar.LENGTH_INDEFINITE);

        // snackbar.setActionTextColor(getResources().getColor(R.color.pdlg_color_blue));

        View snackbarView = snackbar.getView();
        //snackbarView.setBackgroundColor(getColor(R.color.colorAccent));

        snackbar.show();

        // THE COLOR SET BELOW WORKS but the default is white which is what we want; keeping code for reference
        int snackbarTextId = com.google.android.material.R.id.snackbar_text;
        TextView textView = (TextView)snackbarView.findViewById(snackbarTextId);
        textView.setTextSize(18);
//       textView.setTextColor(getResources().getColor(R.color.com_facebook_blue));

    }

    private void transitionBackToLogin () {

        new CountDownTimer(1000, 500) {


            public void onTick(long millisUntilFinished) {
                // imgCoverR.animate().rotation(360).setDuration(500); // why only turned once?
            }

            public void onFinish() {
                Intent intent = new Intent(CollectionAdd.this, Login.class);
                startActivity(intent);
                finish();
            }
        }.start();

    }

    private void getChosenImage() {

        // FancyToast.makeText(getContext(), "we can access images", FancyToast.LENGTH_LONG,
        // FancyToast.SUCCESS, true).show();

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2000);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1000) {

            if(grantResults.length >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){

                getChosenImage();

            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2000) {

            if(resultCode == Activity.RESULT_OK) {

                //Do something with captured image

                try {

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    recievedCollectionImageBitmap = BitmapFactory.decodeFile(picturePath);

                    imgCollectionImageX.setImageBitmap(recievedCollectionImageBitmap);


                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

        }

    }
}
