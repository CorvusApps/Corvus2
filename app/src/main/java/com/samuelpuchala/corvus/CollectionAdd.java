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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.UUID;

public class CollectionAdd extends AppCompatActivity implements View.OnClickListener {


    private ImageView imgCollectionImageX;
    private FloatingActionButton fbtnPopUpMenuX;
    private FloatingActionButton fbtnSaveCollectionX;
    private FirebaseAuth collAddFirebaseAuth;
    private CoordinatorLayout loutCollectionAddActLOX;
    private Bitmap recievedCollectionImageBitmap;
    private String imageIdentifier;
    private String imageLink;
    private EditText edtCollectionNameX, edtCollectionDescX;



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

        edtCollectionNameX = findViewById(R.id.edtCollectionName);
        edtCollectionDescX = findViewById(R.id.edtCollectionDesc);


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

                // prevent uploading collections without names
                if(edtCollectionNameX.getText().toString().equals("")) {

                    Toast.makeText(CollectionAdd.this, "You must enter a Collection Name", Toast.LENGTH_SHORT).show();
                } else {

                    // allows the collection to be uploaded without the pic while making sure pic uploaded first if there to first generate the imageLink
                    if (recievedCollectionImageBitmap == null) {

                        uploadCollection();
                    } else {

                        uploadImageToServer();

                    }
                }

                break;

        }

    }
    //onClick set up in XML; gets rid of keyboard when background tapped
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


    // Start of the save collection function go with pic first if pic selected; if not start with uploadCollection()
    private void uploadImageToServer () {

        if (recievedCollectionImageBitmap != null) {

            // Get the data from an ImageView as bytes
            imgCollectionImageX.setDrawingCacheEnabled(true);
            imgCollectionImageX.buildDrawingCache();
            //Bitmap bitmap = ((BitmapDrawable) imgPicX.getDrawable()).getBitmap(); // we already have the bitmap
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            recievedCollectionImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();

            imageIdentifier = UUID.randomUUID() + ".png";   //initialized here because needs to be unique for each image but is random = unique??

            UploadTask uploadTask = FirebaseStorage.getInstance().getReference().child("myImages")
                    .child(imageIdentifier).putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads

                    Toast.makeText(CollectionAdd.this, exception.toString(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.


                    // get the download link of the image uploaded to server
                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        // apparently the onCompleteListener is to allow this to happen in the backround vs. UI thread
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            if (task.isSuccessful()) {

                                imageLink = task.getResult().toString();

                                // setting up as separate method to let image upload finish before calling the put function which requires the imageLink
                                uploadCollection ();
                            }
                        }
                    });
                }
            });

        }
    }

    private void uploadCollection (){

        HashMap<String, String> dataMap = new HashMap<>();

        dataMap.put("title", edtCollectionNameX.getText().toString());
        dataMap.put("imageIdentifier", imageIdentifier);
        dataMap.put("imageLink", imageLink);
        dataMap.put("des", edtCollectionDescX.getText().toString());
        String uid = FirebaseAuth.getInstance().getUid();

        FirebaseDatabase.getInstance().getReference().child("my_users").child(uid)
                .child("collections").push().setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    Toast.makeText(CollectionAdd.this, "Upload complete", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CollectionAdd.this, CoinList.class);
                    startActivity(intent);
                    finish();

                }

            }
        });

    }
}
