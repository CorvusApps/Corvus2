package com.samuelpuchala.corvus;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;

import com.facebook.login.LoginManager;
import com.github.florent37.shapeofview.ShapeOfView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;

import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.transition.Explode;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.UUID;

public class CollectionAdd extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {



    private FloatingActionButton fbtnPopUpMenuX;
    private FloatingActionButton fbtnSaveCollectionX;
    private FirebaseAuth collAddFirebaseAuth;
    private CoordinatorLayout loutCollectionAddActLOX;
    private Bitmap recievedCollectionImageBitmap;
    private String imageIdentifier;
    private String imageLink;
    private EditText edtCollectionNameX, edtCollectionDescX, edtCollectionsNotesX, edtCollectionIDX;
    private AlertDialog dialog;
    private String exceptions;
    private ProgressDialog pd;
    private ImageView imgCollectionImageX;
    private ImageView imgInfoX;
    private int id3;


    // variables to be used in screen measurement methods needed to adjust UI for different screen sizes
    int height2;
    int fabheight2;

    //variables to recieve inputs from modify collection method from expanded collectio dialog in homepage
    String colUIDYRec, colTitleRec, colImageLinkRec, colDesRec, colNotesRec;
    int colIDYRec;
    String modify; // toggle to whether we are saving a new collection or modifying existing



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_add);

        //To be shown first time only as intro info

        if (isFirstTime()) {
            oneTimeInfoLogin();
        }

        imgCollectionImageX = findViewById(R.id.imgCollectionImage);
        imgCollectionImageX.setOnClickListener(this);

        fbtnPopUpMenuX = findViewById(R.id.fbtnPopUpMenu);
        fbtnSaveCollectionX = findViewById(R.id.fbtnSaveCollection);

        fbtnPopUpMenuX.setOnClickListener(this);
        fbtnSaveCollectionX.setOnClickListener(this);

        collAddFirebaseAuth = FirebaseAuth.getInstance();

        loutCollectionAddActLOX = findViewById(R.id.loutCollectionAddActLO);

        //setting onFocusChangeListeners to change UI when EditText is touched and keyboard comes up
        edtCollectionNameX = findViewById(R.id.edtCollectionName);
        edtCollectionNameX.setOnFocusChangeListener(this);

        edtCollectionDescX = findViewById(R.id.edtCollectionDesc);
        edtCollectionDescX.setOnFocusChangeListener(this);

        edtCollectionsNotesX = findViewById(R.id.edtCollectionNotes);
        edtCollectionsNotesX.setOnFocusChangeListener(this);

        edtCollectionIDX = findViewById(R.id.edtCollectionID);
        edtCollectionIDX.setOnFocusChangeListener(this);

        imgInfoX = findViewById(R.id.imgInfo);
        imgInfoX.setOnClickListener(this);

        modify = "no"; // toggle to whether we are saving a new collection or modifying existing

        colUIDYRec = getIntent().getStringExtra("coluid"); //getting this value in the onCreate so it can be passed on coinAdd to coinList


        //try to get data from intent if not null
        Bundle intent = getIntent().getExtras();
        if (intent != null){
            // we can come into this class from either add collection (home page) or modify collection (expanded collection window)
            // this code will be executed if we came in from modify and thus with push Extras

            //get and store data

            colTitleRec = getIntent().getStringExtra("title");
            colDesRec = getIntent().getStringExtra("des");
            colImageLinkRec = getIntent().getStringExtra("imageLink");
            colNotesRec = getIntent().getStringExtra("notes");
            colIDYRec = getIntent().getIntExtra("id", 0);
            //"coluid" set up in onCreate because needed both in Add and Modify


            //populate the input views with existing value
            edtCollectionNameX.setText(colTitleRec);
            edtCollectionDescX.setText(colDesRec);
            edtCollectionsNotesX.setText(colNotesRec);
            // need to convert to string before putting into editText but want int in firbase for sorting

            String colIDYRec2 = String.valueOf(colIDYRec);
            edtCollectionIDX.setText(colIDYRec2);


           // executes only if there is an imageLink coming through to prevent crashing
            if (colImageLinkRec.isEmpty()) {

                //skip

            } else {
                Picasso.get().load(colImageLinkRec).into(imgCollectionImageX);
            }
            // toggles to this being modification input vs. new collection
            modify = "yes";

        }


        //Getting the ArcView to which we are pegging the FAB to be midscreen so it oes not get hidden by keyboard
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        double height = size.y;

        // sets up the height variable to be used in ArcView and Button position to different screen sizes

        if (height<1300){

            height = height * .5;
            double fabheight = height - 125;

            height2 = (int) Math.round(height);
            fabheight2 = (int) Math.round(fabheight);

        } else if (height < 2000) {

            height = height * .6;
            double fabheight = height - 200;

            height2 = (int) Math.round(height);
            fabheight2 = (int) Math.round(fabheight);

        } else {

            height = height * .7;
            double fabheight = height - 250;

            height2 = (int) Math.round(height);
            fabheight2 = (int) Math.round(fabheight);

        }

        fbtnSaveCollectionX.setAlpha(0f);
        ShapeOfView arcViewX = findViewById(R.id.arcView);
        setArcViewDimensions(arcViewX, width/1, height2/1);
    }

    private void setArcViewDimensions(View view, int width, int height2){

        fbtnSaveCollectionX.animate().translationY(fabheight2).setDuration(1);
        fbtnSaveCollectionX.setAlpha(1f);

        android.view.ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = width;
        params.height = height2;
        view.setLayoutParams(params);

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

                // hiding keyboard when save button pressed
                try { // we need this because if you tap with no keyboard up the app will crash

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                } catch (Exception e) {

                    e.printStackTrace();
                }

                // prevent uploading collections without names
                if(edtCollectionNameX.getText().toString().equals("")) {

                    alertDialogNoCollectionName();

                } else {


                    if (modify.equals("no")) {

                        //executes this if this is a new collection

                        // allows the collection to be uploaded without the pic while making sure pic uploaded first if there to first generate the imageLink
                        if (recievedCollectionImageBitmap == null) {

                            alertDialogNoCollectionPicture();
                        } else {

                            uploadImageToServer();

                        }

                    } else {
                        //executes this if it is an update

                        beginUpdate();

                        }
                    }


                break;

            case R.id.imgInfo:

                collectionIDInfoDialog();


                break;

        }

    }

    private void collectionIDInfoDialog() {
        //Everything in this method is code for the universal alert dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.zzz_dialog_alert_universal, null);

        dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        dialog.show();

        TextView txtAlertTitleX = view.findViewById(R.id.txtAlertTitle);
        txtAlertTitleX.setText("Info");


        TextView txtAlertMsgX = view.findViewById(R.id.txtAlertMsg);
        txtAlertMsgX.setText(R.string.collectionadd_04); // talks about collection id; repeated in FAQ

        ImageView imgUniAlertX = view.findViewById(R.id.imgUniAlert);
        imgUniAlertX.setImageDrawable(getResources().getDrawable(R.drawable.info_white));

        Button btnOKdauX = view.findViewById(R.id.btnOKdau);
        btnOKdauX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });


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

                    case R.id.popMenuRefCollections:

                        Intent intent = new Intent(CollectionAdd.this, RefCollections.class);
                        startActivity(intent);


                        return true;

                    case R.id.popMenuLogout:

                        //Confirm the user wants to logout and execute
                        alertDialogLogOut();

                        return true;

                    case R.id.popMenuFAQ:

                        faqDialogView();

                        return true;

                }

                return false;
            }
        });
        popup.show();

    }

    private void logoutSnackbar(){


        Snackbar snackbar;

        snackbar = Snackbar.make(loutCollectionAddActLOX, "Good bye", Snackbar.LENGTH_SHORT);


        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getColor(R.color.colorAccent));

        snackbar.show();

        // THE COLOR SET BELOW WORKS but the default is white which is what we want; keeping code for reference
        int snackbarTextId = com.google.android.material.R.id.snackbar_text;
        TextView textView = (TextView)snackbarView.findViewById(snackbarTextId);
        textView.setTextSize(18);
        textView.setTextColor(getResources().getColor(R.color.lighttext));

    }

    private void collectionLoadSnackbar(){


        Snackbar snackbar;

        snackbar = Snackbar.make(loutCollectionAddActLOX, "Collection uploaded successfully", Snackbar.LENGTH_SHORT);


        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getColor(R.color.colorAccent));

        snackbar.show();

        // THE COLOR SET BELOW WORKS but the default is white which is what we want; keeping code for reference
        int snackbarTextId = com.google.android.material.R.id.snackbar_text;
        TextView textView = (TextView)snackbarView.findViewById(snackbarTextId);
        textView.setTextSize(18);
        textView.setTextColor(getResources().getColor(R.color.lighttext));

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

        // gets image from internal storage - GALLERY
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
                    String picturePath = cursor.getString(columnIndex); // path to the Bitmap created from pulled image
                    cursor.close();
                    recievedCollectionImageBitmap = BitmapFactory.decodeFile(picturePath);

                    // getting to the rotation wierdness from large files using the picturePath to id the file
                    int degree = 0;
                    ExifInterface exif = null;
                    try {
                        exif = new ExifInterface(picturePath);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    if (exif != null) {
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
                        if (orientation != -1) {
                            // We only recognise a subset of orientation tag values.
                            switch (orientation) {
                                case ExifInterface.ORIENTATION_ROTATE_90:
                                    degree = 90;
                                    break;
                                case ExifInterface.ORIENTATION_ROTATE_180:
                                    degree = 180;
                                    break;
                                case ExifInterface.ORIENTATION_ROTATE_270:
                                    degree = 270;
                                    break;
                            }

                        }
                    }

                    // resizing the image to a standard size that is easy on the storage
                    recievedCollectionImageBitmap = Bitmap.createScaledBitmap(recievedCollectionImageBitmap, 400,400,true);

                    // correcting the rotation on the resized file using the degree variable of how much to fix we got above
                    Bitmap bitmap = recievedCollectionImageBitmap;
                    Matrix matrix = new Matrix();
                    matrix.postRotate(degree);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0,bitmap.getWidth(), bitmap.getHeight(), matrix, true);


                    imgCollectionImageX.setImageBitmap(bitmap);

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

        }

    }


    // Start of the save collection function go with pic first as we need the imageLink and identifier when uploading the collection
    private void uploadImageToServer () {

        pd = new ProgressDialog(CollectionAdd.this,R.style.CustomAlertDialog);
        pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pd.show();

            // Get the data from an ImageView as bytes; does not crash when user does not select and image because takes default image provided by the app
            imgCollectionImageX.setDrawingCacheEnabled(true);
            imgCollectionImageX.buildDrawingCache();
            Bitmap bitmapColAdd = ((BitmapDrawable) imgCollectionImageX.getDrawable()).getBitmap(); // we already have the bitmap
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
           // create this bitmap using recieved to upload stock image if user did not upload theirs; looks better in cardview and consistent with modify outcomes
            bitmapColAdd.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            imageIdentifier = UUID.randomUUID() + ".jpg";   //initialized here because needs to be unique for each image but is random = unique??

            UploadTask uploadTask = FirebaseStorage.getInstance().getReference().child("myImages")
                    .child(imageIdentifier).putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads

                   //makes the exception message an instance variable string that can be used in a custom dialog below

                    exceptions = exception.toString();
                    alertDialogException();

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

    private void uploadCollection(){

        String uid = FirebaseAuth.getInstance().getUid();

        //////// this and the noted data put below gets the uid key for this snapshot so we can use it later on item click
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child("my_users").child(uid)
                .child("collections");
        DatabaseReference blankRecordReference = dbReference;
        DatabaseReference db_ref = blankRecordReference.push(); // this generates the uid
        final String coluidX = db_ref.getKey();
        Long timestampX = System.currentTimeMillis() * -1; // make negative for sorting; using timestamp instead is giant pain in the ass as you can't make it a long value easily

        //////

        if(edtCollectionIDX.getText().toString().equals("")) {

            id3 = 0;

        } else {

            String id2 = edtCollectionIDX.getText().toString();
            id3 = Integer.parseInt(id2);// getting id to be an int before uploading so sorting work well

        }

        HashMap<String, Object> dataMap = new HashMap<>();

        dataMap.put("title", edtCollectionNameX.getText().toString());
        dataMap.put("imageIdentifier", imageIdentifier);
        dataMap.put("imageLink", imageLink);
        dataMap.put("des", edtCollectionDescX.getText().toString());
        dataMap.put("notes", edtCollectionsNotesX.getText().toString());
        dataMap.put("timestamp", timestampX);
        dataMap.put("id", id3);



        //////
        dataMap.put("coluid", coluidX);

        /////
        db_ref.setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    pd.dismiss();
                    collectionLoadSnackbar();

                    new CountDownTimer(3000, 500) {


                        public void onTick(long millisUntilFinished) {
                            // imgCoverR.animate().rotation(360).setDuration(500); // why only turned once?
                        }

                        public void onFinish() {
                            Intent intent = new Intent(CollectionAdd.this, CoinList.class);
                            intent.putExtra("coluid", coluidX);
                            startActivity(intent);
                            finish();
                        }
                    }.start();

                }

            }
        });

    }

    private void alertDialogNoCollectionName() {

        //Everything in this method is code for the universal alert dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.zzz_dialog_alert_universal, null);

        dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        dialog.show();

        TextView txtAlertMsgX = view.findViewById(R.id.txtAlertMsg);
        txtAlertMsgX.setText("You must enter a collection name before you save it to Corvus");

        Button btnOKdauX = view.findViewById(R.id.btnOKdau);
        btnOKdauX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });
    }

    private void alertDialogException() {

        //Everything in this method is code for the universal alert dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.zzz_dialog_alert_universal, null);

        dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        dialog.show();

        TextView txtAlertMsgX = view.findViewById(R.id.txtAlertMsg);
        txtAlertMsgX.setText(exceptions);

        Button btnOKdauX = view.findViewById(R.id.btnOKdau);
        btnOKdauX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });
    }

    private void alertDialogLogOut() {

        //Everything in this method is code for a custom dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.zzz_dialog_addpic, null);

        dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        dialog.show();

        ImageView imgIconX = view.findViewById(R.id.imgIcon);
        imgIconX.setImageDrawable(getResources().getDrawable(R.drawable.logout));

        TextView txtTitleX = view.findViewById(R.id.txtTitle);
        txtTitleX.setText("Logout");

        TextView txtMsgX = view.findViewById(R.id.txtMsg);
        txtMsgX.setText("Do you really want to Logout from Corvus?");

        Button btnYesX = view.findViewById(R.id.btnYes);
        btnYesX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                collAddFirebaseAuth.signOut();
                LoginManager.getInstance().logOut();
                logoutSnackbar();
                transitionBackToLogin ();
            }
        });

        Button btnNoX = view.findViewById(R.id.btnNo);
        btnNoX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    private void alertDialogNoCollectionPicture() {

        //Everything in this method is code for the universal alert dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.zzz_dialog_setcolimage, null);

        dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        dialog.show();

        Button btnSetImageX = view.findViewById(R.id.btnSetImage);
        btnSetImageX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getChosenImage();
                dialog.dismiss();

            }
        });

        Button btnSaveAsIsX = view.findViewById(R.id.btnSaveAsIs);
        btnSaveAsIsX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edtCollectionNameX.getText().toString().equals("")) {

                    dialog.dismiss();
                    alertDialogNoCollectionName();

                } else {

                    uploadImageToServer();
                    dialog.dismiss();
                }

            }
        });

        Button btnCancelX = view.findViewById(R.id.btnCancel);
        btnCancelX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });


    }


    //Detects when user is on different edit texts and sends to methods that change position of arcview and fab when keyboard is up; differ by detected screen resolution
    @Override
    public void onFocusChange(View view, boolean hasFocus) {

        switch (view.getId()){

            case R.id.edtCollectionName:

                uiChangeWhenKeyboardUp();

                break;

            case R.id.edtCollectionDesc:

                uiChangeWhenKeyboardUp();

                break;

            case R.id.edtCollectionNotes:

                uiChangeWhenKeyboardUp();

                break;

            case R.id.edtCollectionID:

                uiChangeWhenKeyboardUp();

                break;

        }

    }

    private void uiChangeWhenKeyboardUp() {

        //This repeats the code from OnCreate with different params and sends the params back to ArcViewDimensions method which actually controls the UI

        //Getting the ArcView to which we are pegging the FAB to be midscreen so it oes not get hidden by keyboard
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        double height = size.y;

        // sets up the height variable to be used in ArcView and Button position to different screen sizes

        if (height < 1300) {

            height = height * .425;
            double fabheight = height - 125;

            height2 = (int) Math.round(height);
            fabheight2 = (int) Math.round(fabheight);

        } else if (height < 2000) {

            height = height * .45;
            double fabheight = height - 200;

            height2 = (int) Math.round(height);
            fabheight2 = (int) Math.round(fabheight);

        } else {

            height = height * .5;
            double fabheight = height - 250;

            height2 = (int) Math.round(height);
            fabheight2 = (int) Math.round(fabheight);
        }

        ShapeOfView arcViewX = findViewById(R.id.arcView);
        setArcViewDimensions(arcViewX, width / 1, height2 / 1);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(CollectionAdd.this, HomePage.class);
        startActivity(intent);
        finish();

    }

    private void oneTimeInfoLogin() {

        //Everything in this method is code for a custom dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.zzzz_otinfo_collectionadd, null);

        dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        dialog.show();

        Button btnOKoneTimeHPX = view.findViewById(R.id.btnOKoneTimeCA);
        btnOKoneTimeHPX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });
    }

   // Functionality for saving as an upate vs. as a new collection
    private void beginUpdate() {

        //if the collection did not have any image to start with skip delete image to avoid a crash

        if (colImageLinkRec.isEmpty() | colImageLinkRec.equals("")) {

            uploadNewImg();
        } else {

            //start by deleting existing pic from storage
            deletePreviousImage();
        }
    }

   // Deletes image previously associated with the collection being modified
    private void deletePreviousImage() {

        StorageReference mPictureReference = FirebaseStorage.getInstance().getReferenceFromUrl(colImageLinkRec);

        mPictureReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                uploadNewImg();

                //this deletes the image but until you close out of the app, that is not updated in the UI...
                //... which is OK because it also re-uploads the same image; but why?

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(CollectionAdd.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    // this uses the image added into the image view which is either the one that comes from firebase or is added there even on modify with the collection add functionality which is active on the input page
    private void uploadNewImg() {

        pd = new ProgressDialog(CollectionAdd.this,R.style.CustomAlertDialog);
        pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pd.show();

        // Get the data from an ImageView as bytes - this is identical to what we did for new collection except needing to get the bitmap here because not available from pic upload


            imgCollectionImageX.setDrawingCacheEnabled(true);
            imgCollectionImageX.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) imgCollectionImageX.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            imageIdentifier = UUID.randomUUID() + ".jpg";   //initialized here because needs to be unique for each image but is random = unique??

            UploadTask uploadTask = FirebaseStorage.getInstance().getReference().child("myImages")
                    .child(imageIdentifier).putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads

                    //makes the exception message an instance variable string that can be used in a custom dialog below

                    exceptions = exception.toString();
                    alertDialogException();

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
                                // sending to an update method seperate from the one used to upload a new collection
                                updateCollection();
                            }
                        }
                    });
                }
            });


    }

    // updates collection in Firebase in a similar was as for adding a new collection
    private void updateCollection() {

        //new values to update the previous record
        final String updateTitle = edtCollectionNameX.getText().toString();
        final String updateDes = edtCollectionDescX.getText().toString();
        final String updateNotes = edtCollectionsNotesX.getText().toString();
        final String updateID = edtCollectionIDX.getText().toString();

        final int updateID2 = Integer.parseInt(updateID);// getting id to be an int before uploading so sorting work well
                                                         // no need to do the if logic to see if it's blank because in modify value from firebase has to be something even if that is 0


        //This points to what needs to be updated versus setting up a new upload
        DatabaseReference updateRef = FirebaseDatabase.getInstance().getReference().child("my_users").child(collAddFirebaseAuth.getCurrentUser().getUid())
                .child("collections");

        Query query = updateRef.orderByChild("coluid").equalTo(colUIDYRec);

        //updating timestamp to be able to sort on last updated
        final Long timestampX = System.currentTimeMillis() * -1; // make negative for sorting; using timestamp instead is giant pain in the ass as you can't make it a long value easily

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds: dataSnapshot.getChildren()) {

                    //update data
                    ds.getRef().child("title").setValue(updateTitle);
                    ds.getRef().child("des").setValue(updateDes);
                    ds.getRef().child("notes").setValue(updateNotes);
                    ds.getRef().child("imageLink").setValue(imageLink);
                    ds.getRef().child("uid").setValue(imageIdentifier);
                    ds.getRef().child("timestamp").setValue(timestampX);
                    ds.getRef().child("id").setValue(updateID2);


                    pd.dismiss();
                    collectionLoadSnackbar();

                    new CountDownTimer(3000, 500) {


                        public void onTick(long millisUntilFinished) {
                            // imgCoverR.animate().rotation(360).setDuration(500); // why only turned once?
                        }

                        public void onFinish() {
                            Intent intent = new Intent(CollectionAdd.this, HomePage.class);
                            startActivity(intent);
                            finish();
                        }
                    }.start();


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void faqDialogView() {

        //Everything in this method is code for a custom dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.zzx_dia_view_faq, null);

        dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.show();
        dialog.getWindow().setAttributes(lp);

        ImageView btnFAQbackX = view.findViewById(R.id.btnFAQback);
        btnFAQbackX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });

        //Faq section layouts expandable onClick

        final LinearLayout faqSupportX = view.findViewById(R.id.faqSupport);
        final TextView txtFAQSupportX = view.findViewById(R.id.txtFAQSupport);
        txtFAQSupportX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(faqSupportX.getVisibility() == View.GONE) {
                    faqSupportX.setVisibility(View.VISIBLE);
                    txtFAQSupportX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.collapse, 0);

                } else {

                    faqSupportX.setVisibility(View.GONE);
                    txtFAQSupportX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand, 0);
                }

            }
        });

        final LinearLayout faqLoginLogoutX = view.findViewById(R.id.faqLoginLogout);
        final TextView txtFAQLoginLogoutX = view.findViewById(R.id.txtFAQLoginLogout);
        txtFAQLoginLogoutX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(faqLoginLogoutX.getVisibility() == View.GONE) {
                    faqLoginLogoutX.setVisibility(View.VISIBLE);
                    txtFAQLoginLogoutX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.collapse, 0);

                } else {

                    faqLoginLogoutX.setVisibility(View.GONE);
                    txtFAQLoginLogoutX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand, 0);
                }

            }
        });

        final LinearLayout faqCollectionsX = view.findViewById(R.id.faqCollections);
        final TextView txtFAQCollectionsX = view.findViewById(R.id.txtFAQCollections);
        txtFAQCollectionsX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(faqCollectionsX.getVisibility() == View.GONE) {
                    faqCollectionsX.setVisibility(View.VISIBLE);
                    txtFAQCollectionsX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.collapse, 0);

                } else {

                    faqCollectionsX.setVisibility(View.GONE);
                    txtFAQCollectionsX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand, 0);
                }

            }
        });

        final LinearLayout faqReferenceCollectionsX = view.findViewById(R.id.faqRefCollections);
        final TextView txtFAQReferenceCollectionsX = view.findViewById(R.id.txtFAQReferenceCollections);
        txtFAQReferenceCollectionsX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(faqReferenceCollectionsX.getVisibility() == View.GONE) {
                    faqReferenceCollectionsX.setVisibility(View.VISIBLE);
                    txtFAQReferenceCollectionsX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.collapse, 0);

                } else {

                    faqReferenceCollectionsX.setVisibility(View.GONE);
                    txtFAQReferenceCollectionsX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand, 0);
                }

            }
        });

        final LinearLayout faqCollectionsSetupX = view.findViewById(R.id.faqCollectionSetup);
        final TextView txtFAQCollectionSetupX = view.findViewById(R.id.txtFAQCollectionSetup);
        txtFAQCollectionSetupX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(faqCollectionsSetupX.getVisibility() == View.GONE) {
                    faqCollectionsSetupX.setVisibility(View.VISIBLE);
                    txtFAQCollectionSetupX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.collapse, 0);

                } else {

                    faqCollectionsSetupX.setVisibility(View.GONE);
                    txtFAQCollectionSetupX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand, 0);
                }

            }
        });

        final LinearLayout faqCoinsSetupX = view.findViewById(R.id.faqCoinsSetup);
        final TextView txtFAQCoinsSetupX = view.findViewById(R.id.txtFAQCoinsSetup);
        txtFAQCoinsSetupX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(faqCoinsSetupX.getVisibility() == View.GONE) {
                    faqCoinsSetupX.setVisibility(View.VISIBLE);
                    txtFAQCoinsSetupX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.collapse, 0);

                } else {

                    faqCoinsSetupX.setVisibility(View.GONE);
                    txtFAQCoinsSetupX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand, 0);
                }

            }
        });

    }

    // checks if the app has been run first time before showing activity
    private boolean isFirstTime()
    {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore) {
            // first time
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.commit();
        }
        return !ranBefore;
    }
}
