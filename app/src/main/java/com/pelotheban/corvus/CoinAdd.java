package com.pelotheban.corvus;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;

import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.UUID;


public class CoinAdd extends AppCompatActivity {

    // UI and data components for transfering collection ID and collection Title from Homepage through here to AddCoin and ShowCoin activities
    private TextView txtHiddenCoinAddColIdX;
    private String cAdduidX; //collection UID

    private TextView txtHiddenCoinAddColTitleX;
    private String cAddColTitleX; // collection Title

    private String cAddStandardrefX; // standard ref passed from coinlist

    // data components for manipulating collection item count and value

    private int cAddItemCountX;
    private int cAddItemCountallX;
    private int cAddColValueX;

    // components for new FAB based pop-up menu
    private FloatingActionButton fbtnPopUp2CoinAddX, fbtnMiniRefCollectionsCoinAddX, fbtnMiniFAQCoinAddX, fbtnMiniLogoutCoinAddX;

    private String popupMenuToggle; // need this to know menu state so things like back press and card press buttons do their regular function or toggle the menu


    // UI components and intermediate variables to manipulate them

    private FloatingActionButton fabCoinSaveX, fabCoinPopUpMenuX ;

    private EditText edtPersonageX, edtRICX, edtDenominationX, edtRICvarX, edtWeightX, edtDiamaterX, edtMintX, edtObvDescX
            , edtObvLegendX, edtRevDescX, edtRevLegendX, edtProvenanceX, edtValueX, edtNotesX, edtSortRICX;

    private TextInputLayout txtLOedtRICX, txtLOedtRICvarX; // need these to programatically set hint for the edt nested in them

    private ImageView imgCoinAddX;
    private CoordinatorLayout loutCoinAddActLOX; // the layout needed as context by snackbars

    private Dialog dialog; // universal dialog for most dialogs created in this activity
    private ProgressDialog pd; // universal progress dialog used in this activity
    private String exceptions; // universal variable for various errors that can then be passed to alert dialogs

    private Bitmap coinBitmap; //For getting image from gallery

    int RIC3; // interim variable used to manipulate the RIC as integer before loading to Firebase
    int Value3; // same as above but for Value
    int sortRIC3; // same as above but for sortRIC

    private ImageView imgRICvarInfoX; // clickable image to open info on RICvar

    // custom view to use as a shade behind custom dialogs
    private View shadeX;

    //Firebase and Firebase related
    private String imageIdentifier; // the unique image identifier created in the app for Firebase
    private String imageLink; // created by Firebase when image uploaded
    private FirebaseAuth coinAddFirebaseAuth;

    //variables to recieve inputs from modify coin method from CoinList
    private String coinUIDRec, coinPersonageRec, coinDenominationRec, coinMintRec, coinRICvarRec, coinWeightRec, coinDiameterRec, coinObvDescRec
            ,coinObvLegRec, coinRevDescRec, coinRevLegRec, coinProvenanceRec, coinNotesRec, coinImageLinkRec;

    private String colUIDRec; //col uid we get from coin list versus homepage
    private String colTitleRec; //col title we get from coin list versus homepage
    private int colValueRec; // col value we get from coin list
    private int coinCountRec, coinCountallRec;
    private int coinRICRec, coinValueRec, coinSortRICRec;
    private String modify; // toggle to whether we are saving a new collection or modifying existing

    // need thsese in modify inside if statements so have to make them instance here; used to make sure 0 values go to firebase if RIC or value are ""
    int updateRIC2;
    int updateValue2;
    int updateSortRIC2;

    @Override
    @SuppressLint("RestrictedApi") // suppresses the issue with not being able to use visibility with the FAB
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_add);

        modify = "no"; // toggle to whether we are saving a new collection or modifying existing

        //To be shown first time only as intro info
        if (isFirstTime()) {
            oneTimeInfoCoinAdd();
        }

        // UI and data components for transfering collection ID and collection Title from Homepage through here to AddCoin and ShowCoin activities
        txtHiddenCoinAddColIdX = findViewById(R.id.txtHiddenCoinAddColId);
        cAdduidX = getIntent().getStringExtra("coluid");
        txtHiddenCoinAddColIdX.setText(cAdduidX);

        txtHiddenCoinAddColTitleX = findViewById(R.id.txtHiddenCoinAddColTitle);
        cAddColTitleX = getIntent().getStringExtra("title");
        txtHiddenCoinAddColTitleX.setText(cAddColTitleX);

        cAddStandardrefX = getIntent().getStringExtra("standardref");


        // data components for manipulating collection item count and value in coin add function

        cAddItemCountX = getIntent().getIntExtra("coincount", 0);
        cAddItemCountallX = getIntent().getIntExtra("coincountall",0);
        cAddColValueX = getIntent().getIntExtra("colvalue", 0);


        //Firebase related
        coinAddFirebaseAuth = FirebaseAuth.getInstance();

        // UI components and intermediate variables to manipulate them
        edtPersonageX = findViewById(R.id.edtPersonage);
        edtPersonageX.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edtPersonageX.setRawInputType(InputType.TYPE_CLASS_TEXT);// this plus textMultiline in XML allows for wraping text but provides a next button on keyboard to tab over


        edtDenominationX = findViewById(R.id.edtDenomination);
        edtDenominationX.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edtDenominationX.setRawInputType(InputType.TYPE_CLASS_TEXT);

        edtRICX = findViewById(R.id.edtRIC);
        edtRICX.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edtRICX.setRawInputType(InputType.TYPE_CLASS_NUMBER);

        txtLOedtRICX = findViewById(R.id.txtLOedtID);
        txtLOedtRICX.setHint(cAddStandardrefX + "#");

        edtRICvarX = findViewById(R.id.edtRICvar);
        edtRICvarX.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edtRICvarX.setRawInputType(InputType.TYPE_CLASS_TEXT);

        txtLOedtRICvarX = findViewById(R.id.txtLOedtRICvar);
        txtLOedtRICvarX.setHint(cAddStandardrefX + "var");

        edtWeightX = findViewById(R.id.edtWeight);
        edtWeightX.setImeOptions(EditorInfo.IME_ACTION_NEXT);


        edtDiamaterX = findViewById(R.id.edtDiameter);


        edtMintX = findViewById(R.id.edtMint);
        edtMintX.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edtMintX.setRawInputType(InputType.TYPE_CLASS_TEXT);

        edtObvDescX = findViewById(R.id.edtObvDesc);
        edtObvDescX.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edtObvDescX.setRawInputType(InputType.TYPE_CLASS_TEXT);

        edtObvLegendX = findViewById(R.id.edtObLegend);
        edtObvLegendX.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edtObvLegendX.setRawInputType(InputType.TYPE_CLASS_TEXT);

        edtRevDescX = findViewById(R.id.edtRevDesc);
        edtRevDescX.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edtRevDescX.setRawInputType(InputType.TYPE_CLASS_TEXT);

        edtRevLegendX = findViewById(R.id.edtRevLegend);
        edtRevLegendX.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edtRevLegendX.setRawInputType(InputType.TYPE_CLASS_TEXT);

        edtProvenanceX = findViewById(R.id.edtProvenance);
        edtProvenanceX.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edtProvenanceX.setRawInputType(InputType.TYPE_CLASS_TEXT);

        edtValueX = findViewById(R.id.edtValue);
        edtRICX.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edtRICX.setRawInputType(InputType.TYPE_CLASS_NUMBER);


        edtNotesX = findViewById(R.id.edtNotes);
        edtNotesX.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edtNotesX.setRawInputType(InputType.TYPE_CLASS_TEXT);

        //putting the sortRIC outside of the tabbing logic above because only for admin vs. users
        edtSortRICX = findViewById(R.id.edtSortRIC);

        loutCoinAddActLOX = findViewById(R.id.loutCoinAddActLO); // the layout needed as context by snackbars

        // custom view to use as a shade behind custom dialogs
        shadeX = findViewById(R.id.shade);

        // onclick to get coin from gallery starting with permissions
        imgCoinAddX = findViewById(R.id.imgCoinAdd);
        imgCoinAddX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ActivityCompat.checkSelfPermission(CoinAdd.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]
                                    {Manifest.permission.READ_EXTERNAL_STORAGE},
                            3000);
                }else {

                    getChosenCoinImage();

                }

            }
        });

        //clickable info for RICvar
        imgRICvarInfoX = findViewById(R.id.imgRICvarInfo);
        imgRICvarInfoX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                inputRICvarInfo();

            }
        });

        // FABs and TXTs for new pop up menu components

        popupMenuToggle = "Not";

        fbtnPopUp2CoinAddX = findViewById(R.id.fbtnPopUp2CoinAdd);

        fbtnMiniRefCollectionsCoinAddX = findViewById(R.id.fbtnMiniRefCollectionsCoinAdd);
        fbtnMiniFAQCoinAddX = findViewById(R.id.fbtnMiniFAQ2CoinAdd);
        fbtnMiniLogoutCoinAddX = findViewById(R.id.fbtnMiniLogout2CoinAdd);

        fabCoinPopUpMenuX = findViewById(R.id.fabCoinPopUpMenu);
        fabCoinPopUpMenuX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //showPopupMenu(view, true, R.style.MyPopupOtherStyle);

                showNewFABbasedMenuCoinAdd();

            }
        });

        fabCoinSaveX = findViewById(R.id.fabCoinSave);
        fabCoinSaveX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                coinSave();
            }
        });

        //setting this up so when we have our FAB popup menu clicking anywhere on the scrreen will turn it off
        shadeX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fabCoinPopUpMenuX.setVisibility(View.VISIBLE);
                fbtnPopUp2CoinAddX.setVisibility(View.GONE);

                fbtnMiniRefCollectionsCoinAddX.setVisibility(View.GONE);
                fbtnMiniFAQCoinAddX.setVisibility(View.GONE);
                fbtnMiniLogoutCoinAddX.setVisibility(View.GONE);

                shadeX.setVisibility(View.GONE);
                popupMenuToggle = "Not";

            }
        });

        //try to get data from intent if not null
        Bundle intent = getIntent().getExtras();
        if (getIntent().getStringExtra("personage") != null && getIntent().getStringExtra("replicate") == null){
            // we can come into this class from either add collection (home page) or modify collection (expanded collection window)
            // this code will be executed if we came in from modify and thus with push Extras; but because will have intent from Homepage need to be specific for coin intent
            // now also we can come in here through the replicate button from CoinList which will also generate an intent with personage but that should go to coinadd method - so adding the second if statment for the duplicate toggle

            //get and store data

            colUIDRec =  getIntent().getStringExtra("coluid"); // need collection because coming in from coinlist not home page
            coinUIDRec = getIntent().getStringExtra("coinuid");
            colTitleRec = getIntent().getStringExtra("title");

            coinPersonageRec = getIntent().getStringExtra("personage");
            coinDenominationRec = getIntent().getStringExtra("denomination");
            coinMintRec = getIntent().getStringExtra("mint");
            coinRICvarRec = getIntent().getStringExtra("ricvar");
            coinRICRec = getIntent().getIntExtra("id", 0);
            coinWeightRec = getIntent().getStringExtra("weight");
            coinDiameterRec = getIntent().getStringExtra("diameter");
            coinObvDescRec = getIntent().getStringExtra("obvdesc");
            coinObvLegRec = getIntent().getStringExtra("obvleg");
            coinRevDescRec = getIntent().getStringExtra("revdesc");
            coinRevLegRec = getIntent().getStringExtra("revleg");
            coinProvenanceRec = getIntent().getStringExtra("provenance");
            coinValueRec = getIntent().getIntExtra("value", 0);
            coinNotesRec = getIntent().getStringExtra("notes");

            coinSortRICRec = getIntent().getIntExtra("sortric", 0);

            coinImageLinkRec = getIntent().getStringExtra("imageLink");

            colValueRec = getIntent().getIntExtra("colvalue", 0);


            //populate the input views with existing value
            edtPersonageX.setText(coinPersonageRec);
            edtDenominationX.setText(coinDenominationRec);
            edtMintX.setText(coinMintRec);
            edtRICvarX.setText(coinRICvarRec);
            edtWeightX.setText(coinWeightRec);
            edtDiamaterX.setText(coinDiameterRec);
            edtObvDescX.setText(coinObvDescRec);
            edtObvLegendX.setText(coinObvLegRec);
            edtRevLegendX.setText(coinRevLegRec);
            edtRevDescX.setText(coinRevDescRec);
            edtProvenanceX.setText(coinProvenanceRec);
            edtNotesX.setText(coinNotesRec);

            // need to convert to string before putting into editText but want int in firbase for sorting
            String coinRICRec2 = String.valueOf(coinRICRec);
            //eliminate 0 entries
            if (coinRICRec2.equals("0")) {

                edtRICX.setText("");
            } else {

                edtRICX.setText(coinRICRec2);

            }

            String coinValueRec2 = String.valueOf(coinValueRec);
            if (coinValueRec2.equals("0"))  {
                edtValueX.setText("");
            } else {
                edtValueX.setText(coinValueRec2);
            }
            //will this give us a NULL pointer exception if there is NO value for sort ric?
            String coinSortRIC2 = String.valueOf(coinSortRICRec);
            if (coinSortRIC2.equals("0"))  {
                edtSortRICX.setText("");
            } else {
                edtSortRICX.setText(coinSortRIC2);
            }


            // executes only if there is an imageLink coming through to prevent crashing

            try {
                if (coinImageLinkRec.isEmpty()) {

                    //skip

                } else {
                    Picasso.get().load(coinImageLinkRec).into(imgCoinAddX);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            // toggles to this being modification input vs. new collection
            modify = "yes";

        } else if (getIntent().getStringExtra("personage") != null && getIntent().getStringExtra("replicate").equals("yes")) {


            // This prepopulates the fields if the replicate toggle is yes but doesn't send to modify methods so should proceed the same way to coin add when button is pressed as if nothing prepopulated
            colUIDRec =  getIntent().getStringExtra("coluid"); // need collection because coming in from coinlist not home page

            colTitleRec = getIntent().getStringExtra("title");

            coinPersonageRec = getIntent().getStringExtra("personage");
            coinDenominationRec = getIntent().getStringExtra("denomination");
            coinMintRec = getIntent().getStringExtra("mint");
            coinRICvarRec = getIntent().getStringExtra("ricvar");
            coinRICRec = getIntent().getIntExtra("id", 0);

            coinObvDescRec = getIntent().getStringExtra("obvdesc");
            coinObvLegRec = getIntent().getStringExtra("obvleg");
            coinRevDescRec = getIntent().getStringExtra("revdesc");
            coinRevLegRec = getIntent().getStringExtra("revleg");

            coinNotesRec = getIntent().getStringExtra("notes");

            coinSortRICRec = getIntent().getIntExtra("sortric", 0);

            colValueRec = getIntent().getIntExtra("colvalue", 0);
            cAddItemCountX = getIntent().getIntExtra("coincount", 0);
            cAddItemCountallX = getIntent().getIntExtra("coincountall", 0);


            //populate the input views with existing value
            edtPersonageX.setText(coinPersonageRec);
            edtDenominationX.setText(coinDenominationRec);
            edtMintX.setText(coinMintRec);
            edtRICvarX.setText(coinRICvarRec);

            edtObvDescX.setText(coinObvDescRec);
            edtObvLegendX.setText(coinObvLegRec);
            edtRevLegendX.setText(coinRevLegRec);
            edtRevDescX.setText(coinRevDescRec);

            edtNotesX.setText(coinNotesRec);

            // need to convert to string before putting into editText but want int in firbase for sorting
            String coinRICRec2 = String.valueOf(coinRICRec);
            //eliminate 0 entries
            if (coinRICRec2.equals("0")) {

                edtRICX.setText("");
            } else {

                edtRICX.setText(coinRICRec2);

            }

            String coinValueRec2 = String.valueOf(coinValueRec);
            if (coinValueRec2.equals("0"))  {
                edtValueX.setText("");
            } else {
                edtValueX.setText(coinValueRec2);
            }
            //will this give us a NULL pointer exception if there is NO value for sort ric?
            String coinSortRIC2 = String.valueOf(coinSortRICRec);
            if (coinSortRIC2.equals("0"))  {
                edtSortRICX.setText("");
            } else {
                edtSortRICX.setText(coinSortRIC2);
            }


        }

    }

    ///////////////////////// END -------> ON-CREATE ////////////////////////////////////////////////////////////////////

    //////////////////////// START ------> UI METHODS //////////////////////////////////////////////////////////////////

    //onClick set up in XML; gets rid of keyboard when background tapped
    public void loginLayoutTapped (View view) {

        try { // we need this because if you tap with no keyboard up the app will crash

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    //info for RICvar

    private void inputRICvarInfo(){

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
        txtAlertMsgX.setText(R.string.coinadd_05); // talks about RICvar; repeated in FAQ

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

    ///////////////////////// END -------> UI METHODS ////////////////////////////////////////////////////////////////////

    //////////////////////// START ------> ADD COIN  /////////////////////////////////////////////////////////////////////

    //land here from pressing coinSave fab to begin getting the new coin entry in firebase
    private void coinSave() {

        // hiding keyboard when save button pressed
        try { // we need this because if you tap with no keyboard up the app will crash

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        } catch (Exception e) {

            e.printStackTrace();
        }

        // prevent uploading coins without at least the Personage filled in
        if(edtPersonageX.getText().toString().equals("")) {

            alertDialogNoCoinPersonage();

        } else {

            if (modify.equals("no")) {


                //executes this if this is a new coin

                // allows the coin to be uploaded without the pic while making sure pic uploaded first if there to first generate the imageLink
                if (coinBitmap == null) {

                    alertDialogNoCoinPicture();
                } else {

                    uploadImageToServer();

                }

            } else {
                //executes this if it is an update

                beginUpdate();

            }
        }

    }

    //land here if trying to save coin without putting in the personage
    private void alertDialogNoCoinPersonage() {

        //Everything in this method is code for the universal alert dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.zzz_dialog_alert_universal, null);

        dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        dialog.show();

        TextView txtAlertMsgX = view.findViewById(R.id.txtAlertMsg);
        txtAlertMsgX.setText("You must enter the personage before you save the coin to your collection");

        Button btnOKdauX = view.findViewById(R.id.btnOKdau);
        btnOKdauX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });
    }

    // land here if press coinSave fab but have no collection picture; gives user options to get picture or go on
    private void alertDialogNoCoinPicture() {

        //Reuisg the no collection picture dialog from collectionAdd activity
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

                getChosenCoinImage();
                dialog.dismiss();

            }
        });

        Button btnSaveAsIsX = view.findViewById(R.id.btnSaveAsIs);
        btnSaveAsIsX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edtPersonageX.getText().toString().equals("")) {

                    dialog.dismiss();
                    alertDialogNoCoinPersonage();

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

    // land here after pressing coinSave and clearing the Personage and Picture present gates
    // Start of the save collection function go with pic first as we need the imageLink and identifier when uploading the collection
    private void uploadImageToServer () {

        pd = new ProgressDialog(CoinAdd.this,R.style.CustomAlertDialog);
        pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pd.show();

        // Get the data from an ImageView as bytes; does not crash when user does not select and image because takes default image provided by the app
        imgCoinAddX.setDrawingCacheEnabled(true);
        imgCoinAddX.buildDrawingCache();
        Bitmap bitmapCoinAdd = ((BitmapDrawable) imgCoinAddX.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // create this bitmap using recieved to upload stock image if user did not upload theirs; looks better in cardview and consistent with modify outcomes
        bitmapCoinAdd.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        imageIdentifier = UUID.randomUUID() + ".jpg";   //initialized here because needs to be unique for each image but is random = unique??

        UploadTask uploadTask = FirebaseStorage.getInstance().getReference().child("myImages").child("coinImages")
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
                            uploadCoin ();
                        }
                    }
                });
            }
        });

    }


    private void uploadCoin() {




        String uid = FirebaseAuth.getInstance().getUid();

        //////// this and the noted data put below gets the uid key for this snapshot so we can use it later on item click
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child("my_users").child(uid)
                .child("collections").child(cAdduidX).child("coins");
        DatabaseReference blankRecordReference = dbReference;
        DatabaseReference db_ref = blankRecordReference.push();
        String coinuidX = db_ref.getKey(); // this then is the key for the coin
        Long timestampX = System.currentTimeMillis() * -1; // make negative for sorting; using timestamp instead is giant pain in the ass as you can't make it a long value easily

        //////

        // converting RIC input into integer or setting to zero to avoid crash if left empty
        if(edtRICX.getText().toString().equals("")) {

            RIC3 = 0;

        } else {

            String RIC2 = edtRICX.getText().toString();
            RIC3 = Integer.parseInt(RIC2);// getting id to be an int before uploading so sorting works well

        }

        // converting Value input into integer or setting to zero to avoid crash if left empty
        if(edtValueX.getText().toString().equals("")) {

            Value3 = 0;

        } else {

            String Value2 = edtValueX.getText().toString();
            Value3 = Integer.parseInt(Value2);// getting Value to be an int before uploading so sorting works well

        }

        /// converting sortRIC input into integer or setting to zero to avoid crash if left empty

        if(edtSortRICX.getText().toString().equals("")) {

            sortRIC3 = 0;

        } else {

            String sortRIC2 = edtSortRICX.getText().toString();
            sortRIC3 = Integer.parseInt(sortRIC2);// getting sortRIC to be an int before uploading so sorting works well

        }


        HashMap<String, Object> dataMap = new HashMap<>();

        dataMap.put("personage", edtPersonageX.getText().toString());
        dataMap.put("imageIdentifier", imageIdentifier);
        dataMap.put("imageLink", imageLink);
        dataMap.put("id", RIC3);
        dataMap.put("ricvar", edtRICvarX.getText().toString());
        dataMap.put("denomination", edtDenominationX.getText().toString());
        dataMap.put("weight", edtWeightX.getText().toString());
        dataMap.put("diameter", edtDiamaterX.getText().toString());

        dataMap.put("mint", edtMintX.getText().toString());
        dataMap.put("obvdesc", edtObvDescX.getText().toString());
        dataMap.put("obvleg", edtObvLegendX.getText().toString());
        dataMap.put("revdesc", edtRevDescX.getText().toString());
        dataMap.put("revleg", edtRevLegendX.getText().toString());
        dataMap.put("provenance", edtProvenanceX.getText().toString());
        dataMap.put("value", Value3);
        dataMap.put("notes", edtNotesX.getText().toString());

        dataMap.put("timestamp", timestampX);

        dataMap.put("sortric",sortRIC3);

        //////
        dataMap.put("coinuid", coinuidX); // the unique coin UID which we can then use to link back to this coin

        /////

        // getting adjusted values for itemcount and collection value (nullifying add for itemcount if a dup AND nullifying all count adds if it is a breaker

        String BreakerCheck = edtObvDescX.getText().toString();

        if (BreakerCheck.equals("BREAKER")) {

        } else {

            cAddItemCountX = cAddItemCountX + 1;
            if (sortRIC3 > 1000000000) {
                cAddItemCountX = cAddItemCountX - 1;

            }

            cAddItemCountallX = cAddItemCountallX + 1;


            cAddColValueX = cAddColValueX + Value3;

        }

        db_ref.setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    pd.dismiss();
                    coinLoadSnackbar();

                    //once coin uploaded update collection timestamp, itemcount and vlaue///////////////
                    final Long timestampY = System.currentTimeMillis() * -1;
                    String uid = FirebaseAuth.getInstance().getUid();
                    DatabaseReference collectionReference = FirebaseDatabase.getInstance().getReference().child("my_users").child(uid)
                            .child("collections");

                    Query colTimeModQuery = collectionReference.orderByChild("coluid").equalTo(cAdduidX);

                    colTimeModQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            int sortCoinCount = -cAddItemCountX;
                            int sortColValue = -cAddColValueX;

                            for (DataSnapshot ds2: dataSnapshot.getChildren()) {

                                ds2.getRef().child("timestamp").setValue(timestampY);
                                ds2.getRef().child("coincount").setValue(cAddItemCountX);
                                ds2.getRef().child("coincountall").setValue(cAddItemCountallX);

                                ds2.getRef().child("colvalue").setValue(cAddColValueX);

                                ds2.getRef().child("sortcoincount").setValue(sortCoinCount);
                                ds2.getRef().child("sortcolvalue").setValue(sortColValue);

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    /////////////////////////////////////////////////////////

                    new CountDownTimer(3000, 500) {


                        public void onTick(long millisUntilFinished) {
                            // imgCoverR.animate().rotation(360).setDuration(500); // why only turned once?
                        }

                        public void onFinish() {
                            Intent intent = new Intent(CoinAdd.this, CoinList.class);
                            intent.putExtra("coluid", cAdduidX);
                            intent.putExtra("title", cAddColTitleX);
                            intent.putExtra("standardref", cAddStandardrefX);
                            startActivity(intent);
                            finish();
                        }
                    }.start();

                }

            }
        });

    }

    // universal dialog displaying error messages called from different parts of the activity
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

    //land here from pressing on the coin image view to get new coin picture
    private void getChosenCoinImage() {

        // gets image from internal storage - GALLERY
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 4000);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 3000) {

            if(grantResults.length >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){

                getChosenCoinImage();

            }
        }
    }

    ///////////////////////// END ------->>> ADD COIN /////////////////////////////////////////////////////////////////////

    ///////////////////////// START ----->>> UPDATE COIN ////////////////////////////////////////////////////////////


    //land here if press coinSave but modify is set to yes - ie. this is an update and not a new coin add
    private void beginUpdate() {

        //if the coin did not have any image to start with skip delete image to avoid a crash
        //the hard coded link is to ensure that on mass upload the place holder pic does not get deleted when one coin is modified since many coins share it
        if (coinImageLinkRec.isEmpty() | coinImageLinkRec.equals("")
                | coinImageLinkRec.equals("https://firebasestorage.googleapis.com/v0/b/corvus-e98f9.appspot.com/o/myImages%2FcoinImages%2F5d473542-87e1-4410-bbaf-eec7f48ee22c.jpg?alt=media&token=3f1769ec-4f31-49d2-a229-c472457ec99c")) {

            uploadNewImg();
        } else {

            //start by deleting existing pic from storage
            deletePreviousImage();
        }
    }

    //takes the image we got from the Gallery, manipulates it and sets it to the clickable imageView
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 4000) {

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
                    coinBitmap = BitmapFactory.decodeFile(picturePath);

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
                    coinBitmap = Bitmap.createScaledBitmap(coinBitmap, 600,300,true);

                    // correcting the rotation on the resized file using the degree variable of how much to fix we got above
                    Bitmap bitmap = coinBitmap;
                    Matrix matrix = new Matrix();
                    matrix.postRotate(degree);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0,bitmap.getWidth(), bitmap.getHeight(), matrix, true);


                    imgCoinAddX.setImageBitmap(bitmap);

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

        }

    }

    private void deletePreviousImage() {

        StorageReference mPictureReference = FirebaseStorage.getInstance().getReferenceFromUrl(coinImageLinkRec);

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

               // going to upload even if old not deleted to avoid getting trapped with a broken image link and no way to fix it
                uploadNewImg();

                Toast.makeText(CoinAdd.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    // this uses the image added into the image view which is either the one that comes from firebase or is added there even on modify with the collection add functionality which is active on the input page
    private void uploadNewImg() {

        pd = new ProgressDialog(CoinAdd.this,R.style.CustomAlertDialog);
        pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pd.show();

        // Get the data from an ImageView as bytes - this is identical to what we did for new collection except needing to get the bitmap here because not available from pic upload


        imgCoinAddX.setDrawingCacheEnabled(true);
        imgCoinAddX.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imgCoinAddX.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        imageIdentifier = UUID.randomUUID() + ".jpg";   //initialized here because needs to be unique for each image but is random = unique??

        UploadTask uploadTask = FirebaseStorage.getInstance().getReference().child("myImages").child("coinImages")
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
        final String updatePersonage = edtPersonageX.getText().toString();
        final String updateDenomination = edtDenominationX.getText().toString();
        final String updateMint = edtMintX.getText().toString();

        final String updateRIC = edtRICX.getText().toString();
        if (updateRIC.equals("")) {
             updateRIC2 = 0;
        } else {
             updateRIC2 = Integer.parseInt(updateRIC);// getting id to be an int before uploading so sorting work well
        }
        // to avoid 0 populating the edit text set it to "" if 0 comes down from firebase into modify; now need to put it back to 0 to load up so other if statements don't crash on null value

        final String updateRICvar = edtRICvarX.getText().toString();
        final String updateWeight = edtWeightX.getText().toString();
        final String updateDiameter = edtDiamaterX.getText().toString();
        final String updateObvDesc = edtObvDescX.getText().toString();
        final String updateObvLeg = edtObvLegendX.getText().toString();
        final String updateRevDesc = edtRevDescX.getText().toString();
        final String updateRevLeg = edtRevLegendX.getText().toString();
        final String updateProvenace = edtProvenanceX.getText().toString();

        final String updateValue = edtValueX.getText().toString();
        if (updateValue.equals("")) {
            updateValue2 = 0;
        } else {

            updateValue2 = Integer.parseInt(updateValue);// getting id to be an int before uploading so sorting work well
        }
        // to avoid 0 populating the edit text set it to "" if 0 comes down from firebase into modify; now need to put it back to 0 to load up so other if statements don't crash on null value

        final String updateNotes = edtNotesX.getText().toString();

        final String updateSortRIC = edtSortRICX.getText().toString();
        if (updateSortRIC.equals("")) {
            updateSortRIC2 = 0;
        } else {

            updateSortRIC2 = Integer.parseInt(updateSortRIC);// getting id to be an int before uploading so sorting work well
        }
        // to avoid 0 populating the edit text set it to "" if 0 comes down from firebase into modify; now need to put it back to 0 to load up so other if statements don't crash on null value



        //This points to what needs to be updated versus setting up a new upload

        String uid = FirebaseAuth.getInstance().getUid();

        DatabaseReference updateRef = FirebaseDatabase.getInstance().getReference().child("my_users").child(uid)
                .child("collections").child(colUIDRec).child("coins");

        Query query = updateRef.orderByChild("coinuid").equalTo(coinUIDRec);

        //updating timestamp to be able to sort on last updated
        final Long timestampCoinX = System.currentTimeMillis() * -1; // make negative for sorting; using timestamp instead is giant pain in the ass as you can't make it a long value easily

        //updating collection value to be able to net out any modifications (starting col value we pull in minus the old coinvalue plus updated coin value)
       colValueRec = colValueRec + updateValue2 - coinValueRec;


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds: dataSnapshot.getChildren()) {

                    //update data
                    ds.getRef().child("personage").setValue(updatePersonage);
                    ds.getRef().child("denomination").setValue(updateDenomination);
                    ds.getRef().child("mint").setValue(updateMint);
                    ds.getRef().child("id").setValue(updateRIC2);
                    ds.getRef().child("ricvar").setValue(updateRICvar);
                    ds.getRef().child("weight").setValue(updateWeight);
                    ds.getRef().child("diameter").setValue(updateDiameter);
                    ds.getRef().child("obvdesc").setValue(updateObvDesc);
                    ds.getRef().child("obvleg").setValue(updateObvLeg);
                    ds.getRef().child("revdesc").setValue(updateRevDesc);
                    ds.getRef().child("revleg").setValue(updateRevLeg);
                    ds.getRef().child("provenance").setValue(updateProvenace);
                    ds.getRef().child("value").setValue(updateValue2);
                    ds.getRef().child("notes").setValue(updateNotes);

                    ds.getRef().child("sortric").setValue(updateSortRIC2);

                    ds.getRef().child("timestamp").setValue(timestampCoinX);
                    ds.getRef().child("imageLink").setValue(imageLink);
                    ds.getRef().child("uid").setValue(imageIdentifier);

                    //once coin uploaded update collection timestamp///////////////
                    final Long timestampZ = System.currentTimeMillis() * -1;
                    String uid = FirebaseAuth.getInstance().getUid();
                    DatabaseReference collectionUpDateReference = FirebaseDatabase.getInstance().getReference().child("my_users").child(uid)
                            .child("collections");

                    Query colTimeModUpDateQuery = collectionUpDateReference.orderByChild("coluid").equalTo(colUIDRec);

                    colTimeModUpDateQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            int sortColValue = -colValueRec;

                            for (DataSnapshot ds3: dataSnapshot.getChildren()) {

                                ds3.getRef().child("timestamp").setValue(timestampZ);
                                ds3.getRef().child("colvalue").setValue(colValueRec); // only change coll value don't need item count because in modify not changing number of coins

                                ds3.getRef().child("sortcolvalue").setValue(sortColValue);

                                pd.dismiss();
                                coinLoadSnackbar();


                                new CountDownTimer(3000, 500) {

                                    public void onTick(long millisUntilFinished) {
                                        // imgCoverR.animate().rotation(360).setDuration(500); // why only turned once?
                                    }

                                    public void onFinish() {
                                        Intent intent = new Intent(CoinAdd.this, CoinList.class);
                                        intent.putExtra("coluid", colUIDRec);
                                        intent.putExtra("title", colTitleRec);
                                        intent.putExtra("standardref", cAddStandardrefX);
                                        startActivity(intent);
                                        finish();
                                    }
                                }.start();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            pd.dismiss();
                            Toast.makeText(CoinAdd.this, "Upload fail" + databaseError.toString(),Toast.LENGTH_LONG).show();

                        }
                    });

                    /////////////////////////////////////////////////////////



//                    new CountDownTimer(3000, 500) {
//
//                        public void onTick(long millisUntilFinished) {
//                            // imgCoverR.animate().rotation(360).setDuration(500); // why only turned once?
//                        }
//
//                        public void onFinish() {
//                            Intent intent = new Intent(CoinAdd.this, CoinList.class);
//                            intent.putExtra("coluid", colUIDRec);
//                            intent.putExtra("title", colTitleRec);
//                            startActivity(intent);
//                            finish();
//                        }
//                    }.start();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    ///////////////////////// End ------->>> UPDATE COIN ///////////////////////////////////////////////////////////////

    ///////////////////////// START ----->>> POP-UP ////////////////////////////////////////////////////////////////////

    /////////////////// Start-New Pop up Version //////////////////////////////////////////////////////////////////////

    @SuppressLint("RestrictedApi") // suppresses the issue with not being able to use visibility with the FAB
    private void showNewFABbasedMenuCoinAdd() {

        popupMenuToggle = "pressed";


        fabCoinPopUpMenuX.setVisibility(View.GONE);
        fabCoinSaveX.setVisibility(View.GONE);
        fbtnPopUp2CoinAddX.setVisibility(View.VISIBLE);

        fbtnMiniRefCollectionsCoinAddX.setVisibility(View.VISIBLE);
        fbtnMiniFAQCoinAddX.setVisibility(View.VISIBLE);
        fbtnMiniLogoutCoinAddX.setVisibility(View.VISIBLE);

        shadeX.setVisibility(View.VISIBLE);

        fbtnPopUp2CoinAddX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fabCoinPopUpMenuX.setVisibility(View.VISIBLE);
                fabCoinSaveX.setVisibility(View.VISIBLE);

                fbtnPopUp2CoinAddX.setVisibility(View.GONE);

                fbtnMiniRefCollectionsCoinAddX.setVisibility(View.GONE);
                fbtnMiniFAQCoinAddX.setVisibility(View.GONE);
                fbtnMiniLogoutCoinAddX.setVisibility(View.GONE);

                shadeX.setVisibility(View.GONE);
                popupMenuToggle = "Not";

            }
        });



        fbtnMiniRefCollectionsCoinAddX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CoinAdd.this, RefCollectionsDir.class);

                fabCoinPopUpMenuX.setVisibility(View.VISIBLE);
                fabCoinSaveX.setVisibility(View.VISIBLE);

                fbtnPopUp2CoinAddX.setVisibility(View.GONE);

                fbtnMiniRefCollectionsCoinAddX.setVisibility(View.GONE);
                fbtnMiniFAQCoinAddX.setVisibility(View.GONE);
                fbtnMiniLogoutCoinAddX.setVisibility(View.GONE);

                shadeX.setVisibility(View.GONE);
                startActivity(intent);

                popupMenuToggle = "Not";

            }
        });

        fbtnMiniFAQCoinAddX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fabCoinPopUpMenuX.setVisibility(View.VISIBLE);
                fabCoinSaveX.setVisibility(View.VISIBLE);

                fbtnPopUp2CoinAddX.setVisibility(View.GONE);

                fbtnMiniRefCollectionsCoinAddX.setVisibility(View.GONE);
                fbtnMiniFAQCoinAddX.setVisibility(View.GONE);
                fbtnMiniLogoutCoinAddX.setVisibility(View.GONE);

                shadeX.setVisibility(View.GONE);
                popupMenuToggle = "Not";

                faqDialogView();

            }
        });

        fbtnMiniLogoutCoinAddX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fabCoinPopUpMenuX.setVisibility(View.VISIBLE);
                fabCoinSaveX.setVisibility(View.VISIBLE);

                fbtnPopUp2CoinAddX.setVisibility(View.GONE);

                fbtnMiniRefCollectionsCoinAddX.setVisibility(View.GONE);
                fbtnMiniFAQCoinAddX.setVisibility(View.GONE);
                fbtnMiniLogoutCoinAddX.setVisibility(View.GONE);

                shadeX.setVisibility(View.GONE);
                popupMenuToggle = "Not";

                //Confirm the user wants to logout and execute
                alertDialogLogOut();

            }
        });


    }


    /////////////////// END-New Pop up Version //////////////////////////////////////////////////////////////////////

    /////////////////// Start-OLD Pop up Version //////////////////////////////////////////////////////////////////////


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

                        Intent intent = new Intent(CoinAdd.this, RefCollectionsDir.class);
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


    ///////////////////////// END -------> POP-UP MENU ///////////////////////////////////////////////////////////

    ///////////////////////// START ----->>> SNACKBARS ////////////////////////////////////////////////////////////

    private void coinLoadSnackbar() {

        Snackbar snackbar;

        snackbar = Snackbar.make(loutCoinAddActLOX, "Coin uploaded successfully", Snackbar.LENGTH_SHORT);

        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getColor(R.color.colorAccent));

        snackbar.show();


        int snackbarTextId = com.google.android.material.R.id.snackbar_text;
        TextView textView = (TextView)snackbarView.findViewById(snackbarTextId);
        textView.setTextSize(18);
        textView.setTextColor(getResources().getColor(R.color.lighttext));

    }

    private void logoutSnackbar(){


        Snackbar snackbar;

        snackbar = Snackbar.make(loutCoinAddActLOX, "Good bye", Snackbar.LENGTH_SHORT);


        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getColor(R.color.colorAccent));

        snackbar.show();


        int snackbarTextId = com.google.android.material.R.id.snackbar_text;
        TextView textView = (TextView)snackbarView.findViewById(snackbarTextId);
        textView.setTextSize(18);
        textView.setTextColor(getResources().getColor(R.color.lighttext));

    }



    ///////////////////////// END -------> SNACKBARS //////////////////////////////////////////////////////////////

    ///////////////////////// START ----->>> FAQ AND ONE TIME /////////////////////////////////////////////////////

    // One time page information to be shown when user accesses this page for the first time only
    private void oneTimeInfoCoinAdd() {

        //Everything in this method is code for a custom dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.zzzz_otinfo_coinadd, null);

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

    public void faqDialogView() {

        shadeX.setVisibility(View.VISIBLE);

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

                shadeX.setVisibility(View.INVISIBLE);
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

        final LinearLayout faqMassCoinsSetupX = view.findViewById(R.id.faqMassCoinsSetup);
        final TextView txtFAQMassCoinsSetupX = view.findViewById(R.id.txtFAQMassCoinsSetup);
        txtFAQMassCoinsSetupX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(faqMassCoinsSetupX.getVisibility() == View.GONE) {
                    faqMassCoinsSetupX.setVisibility(View.VISIBLE);
                    txtFAQMassCoinsSetupX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.collapse, 0);

                } else {

                    faqMassCoinsSetupX.setVisibility(View.GONE);
                    txtFAQMassCoinsSetupX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand, 0);
                }

            }
        });

        final LinearLayout faqReplicateCoinsX = view.findViewById(R.id.faqReplicateCoins);
        final TextView txtFAQReplicateCoinsX = view.findViewById(R.id.txtFAQReplicateCoins);
        txtFAQReplicateCoinsX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(faqReplicateCoinsX.getVisibility() == View.GONE) {
                    faqReplicateCoinsX.setVisibility(View.VISIBLE);
                    txtFAQReplicateCoinsX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.collapse, 0);

                } else {

                    faqReplicateCoinsX.setVisibility(View.GONE);
                    txtFAQReplicateCoinsX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand, 0);
                }

            }
        });

        final LinearLayout faqCoinListX = view.findViewById(R.id.faqCoinList2);
        final TextView txtFAQCoinListX = view.findViewById(R.id.txtFAQCoinList2);
        txtFAQCoinListX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(faqCoinListX.getVisibility() == View.GONE) {
                    faqCoinListX.setVisibility(View.VISIBLE);
                    txtFAQCoinListX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.collapse, 0);

                } else {

                    faqCoinListX.setVisibility(View.GONE);
                    txtFAQCoinListX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand, 0);
                }

            }
        });

        //if dismissed in any way like a backbutton resets the view on HomePage to normal
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                shadeX.setVisibility(View.INVISIBLE);
            }
        });

    }

    //////////////////////// END ------->>> FAQ & ONE TIME ////////////////////////////////////////////////////////////////

    ///////////////////////// START ----->>> LOGOUT AND ON-BACK PRESS /////////////////////////////////////////////////////
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

                coinAddFirebaseAuth.signOut();
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

    private void transitionBackToLogin () {

        new CountDownTimer(1000, 500) {


            public void onTick(long millisUntilFinished) {
                // imgCoverR.animate().rotation(360).setDuration(500); // why only turned once?
            }

            public void onFinish() {
                Intent intent = new Intent(CoinAdd.this, Login.class);
                startActivity(intent);
                finish();
            }
        }.start();

    }

    @Override
    @SuppressLint("RestrictedApi") // suppresses the issue with not being able to use visibility with the FAB
    public void onBackPressed() {

        // popup Menu Toggle

        if (popupMenuToggle.equals("pressed")) {

            fabCoinPopUpMenuX.setVisibility(View.VISIBLE);
            fabCoinSaveX.setVisibility(View.VISIBLE);

            fbtnPopUp2CoinAddX.setVisibility(View.GONE);

            fbtnMiniRefCollectionsCoinAddX.setVisibility(View.GONE);
            fbtnMiniFAQCoinAddX.setVisibility(View.GONE);
            fbtnMiniLogoutCoinAddX.setVisibility(View.GONE);

            shadeX.setVisibility(View.GONE);
            popupMenuToggle = "Not";

        } else {

//            Intent intent = new Intent(CoinAdd.this, CoinList.class);
//            startActivity(intent);
            finish();
        }

    }


}
