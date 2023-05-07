package com.pelotheban.corvus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.facebook.login.LoginManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.Locale;

public class RefCollections extends AppCompatActivity implements RewardedVideoAdListener {

    private FloatingActionButton fbtnPopUpMenuRefColsX;
    private AlertDialog dialog;
    private FirebaseAuth firebaseAuthRefCollections;
    private CoordinatorLayout loutRefCollectionsActLOX;
    private RecyclerView rcvRefCollectionsX;
    private DatabaseReference mDatabase;

    // information from collection directory to limit which collections we show - goes into sort function
    private int era, eraBack;
    private Query sortQuery;

    // components for new FAB based pop-up menu
    private FloatingActionButton fbtnPopUp2RefCollectionsX, fbtnMiniMyCollectionsRefCollectionsX, fbtnMiniFAQRefCollectionsX, fbtnMiniLogoutRefCollectionsX;
    private TextView txtRefCoinsButtonRefCollectionsX, txtFAQButtonRefCollectionsX, txtLogoutButtonRefCollectionsX;
    private String popupMenuToggle; // need this to know menu state so things like back press and card press buttons do their regular function or toggle the menu

    //creating instance variables that can be used to pass info to the collection detail dialog
    private Bitmap colBitmap;
    private String colTitleY;
    private String colDesY;
    private String colNotesY;
    private int colCoinCountY;
    private int colCoinCountallY;
    private String colStandardRefY;

    private Drawable colImageY;

    // creating instance variables to be passed to the CoinList Activity or Delete from Storage activity
    private String colUIDY;
    private String colImageLinkY;

    // custom view to use as a shade behind custom dialogs
    private View shadeX;

    // adMob

    InterstitialAd mInterstitialAd;
    int mAdvertCounter;
    private SharedPreferences sharedAdvertCounter;

    RewardedVideoAd mRewardedAdRefCollections;
    TextView txtAdMessageX;
    int adMobToggle;
    private int adRewFailedToggle;

    @Override
    @SuppressLint("RestrictedApi") // suppresses the issue with not being able to use visibility with the FAB
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ref_collections);

        //To be shown first time only as intro info

//        if (isFirstTime()) {
//            oneTimeInfoLogin();
//        }


        //adMob
        sharedAdvertCounter = getSharedPreferences("adSetting", MODE_PRIVATE);
        mAdvertCounter = sharedAdvertCounter.getInt("Counter", 0); // where if no settings


        MobileAds.initialize(this, "ca-app-pub-1744081621312112~1448123556");

        adRewFailedToggle = 0;

        mRewardedAdRefCollections = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedAdRefCollections.setRewardedVideoAdListener(this);
        //mRewardedAdRefCollections.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build()); // TEST
        mRewardedAdRefCollections.loadAd("ca-app-pub-1744081621312112/5372943816", new AdRequest.Builder().build()); // REAL

        if (mAdvertCounter > 4) {

            Log.i("REWARDEDLOG", "in counter if");

            if (mRewardedAdRefCollections.isLoaded()) {
                mRewardedAdRefCollections.show();

                Log.i("REWARDEDLOG", "loadedif 1");
            } else {
                Log.i("REWARDEDLOG", "in counter if 2");

                CountDownTimer adtimer = new CountDownTimer(3000, 500) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        Log.i("REWARDEDLOG", "in finished first timer");

                        if (mRewardedAdRefCollections.isLoaded()) {
                            mRewardedAdRefCollections.show();
                            Log.i("REWARDEDLOG", "in showing after first timer");
                        } else {

                            Log.i("REWARDEDLOG", "before second timer");

                            CountDownTimer adtimer = new CountDownTimer(3000, 500) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {

                                    Log.i("REWARDEDLOG", "in finished second timer");
                                    if (mRewardedAdRefCollections.isLoaded()) {
                                        mRewardedAdRefCollections.show();
                                        Log.i("REWARDEDLOG", "in show after second timer");
                                    }

                                }
                            }.start();

                        }

                    }
                }.start();

            }
        }


        mInterstitialAd = new InterstitialAd(RefCollections.this);
        mInterstitialAd.setAdUnitId(getString(R.string.test_interstitial_ad));
        //mInterstitialAd.setAdUnitId(getString(R.string.refcollections_interstitial_ad));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

       // Toast.makeText(RefCollections.this, mAdvertCounter + "", Toast.LENGTH_SHORT).show();

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.

                if (mAdvertCounter > 8) {

                    mInterstitialAd.show();
                    SharedPreferences.Editor editor = sharedAdvertCounter.edit();
                    editor.putInt("Counter", 0); // this only kicks in on next on create so need to set actual mAdvertCounter to 0 below so the add does not loop
                    editor.apply(); // saves the value
                    mAdvertCounter = 0;
                }
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

        // FABs and TXTs for new pop up menu components

        popupMenuToggle = "Not";

        fbtnPopUp2RefCollectionsX = findViewById(R.id.fbtnPopUp2RefCollections);

        fbtnMiniMyCollectionsRefCollectionsX = findViewById(R.id.fbtnMiniMyCollectionsRefCollections);
        fbtnMiniFAQRefCollectionsX = findViewById(R.id.fbtnMiniFAQRefCollections);
        fbtnMiniLogoutRefCollectionsX = findViewById(R.id.fbtnMiniLogoutRefCollections);

        txtRefCoinsButtonRefCollectionsX = findViewById(R.id.txtMyCollectionsButtonRefCollections);
        txtFAQButtonRefCollectionsX = findViewById(R.id.txtFAQButtonRefCollections);
        txtLogoutButtonRefCollectionsX = findViewById(R.id.txtLogoutButtonRefCollections);


        loutRefCollectionsActLOX = findViewById(R.id.loutRefCollectionsActLO);
        firebaseAuthRefCollections = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("my_users");
        mDatabase.keepSynced(true);

        // custom view to use as a shade behind custom dialogs
        shadeX = findViewById(R.id.shade);

        // information from collection directory to limit which collections we show - goes into sort function
        era = getIntent().getIntExtra("era",0);

        // setting up eraBack to return to EmperorDir
        if( era < 13) {

            eraBack = 1;
        } else if (era < 24) {

            eraBack = 2;
        } else if (era < 31) {

            eraBack = 3;
        } else {

            eraBack = 4;
        }


        //setting this up so when we have our FAB popup menu clicking anywhere on the scrreen will turn it off
        shadeX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fbtnPopUpMenuRefColsX.setVisibility(View.VISIBLE);
                fbtnPopUp2RefCollectionsX.setVisibility(View.GONE);

                fbtnMiniMyCollectionsRefCollectionsX.setVisibility(View.GONE);
                fbtnMiniFAQRefCollectionsX.setVisibility(View.GONE);
                fbtnMiniLogoutRefCollectionsX.setVisibility(View.GONE);

                txtRefCoinsButtonRefCollectionsX.setVisibility(View.GONE);
                txtFAQButtonRefCollectionsX.setVisibility(View.GONE);
                txtLogoutButtonRefCollectionsX.setVisibility(View.GONE);

                shadeX.setVisibility(View.GONE);
                popupMenuToggle = "Not";

            }
        });

        //query for sorting; unlike in home page this will be hardwired for only sort by collection number; and the database ref is already hardcoded for my facebook account which will store the reference collections
        DatabaseReference sortReference = mDatabase.child("vPlrYZXdGHgRotLma4OVopIRKY02")
                .child("collections");

        // setting up query to be dependent on selection in RefCollectionDir - which is limiting collections to an era
        // usually era matces the sortric except for postumus who is era 51

        int start = era * 1000;
        int end = start + 999;
        if (era == 51) {

            sortQuery = sortReference.orderByChild("id").startAt(52000).endAt(52999);

        } else {
            // setting up exception for the emperors added after the number system was locked in AND for a few emperors pre-existin but affected by this
            if (era == 38) {

                sortQuery = sortReference.orderByChild("id").startAt(38000).endAt(38499);

            } else {

                if (era == 53) {

                    sortQuery = sortReference.orderByChild("id").startAt(38500).endAt(38799);

                } else {

                    if (era == 54) {

                        sortQuery = sortReference.orderByChild("id").startAt(38800).endAt(38999);

                    } else {

                        if (era == 46) {

                            sortQuery = sortReference.orderByChild("id").startAt(46000).endAt(46499);

                        } else {

                            if (era == 55) {

                                sortQuery = sortReference.orderByChild("id").startAt(46500).endAt(46999);

                            } else {

                                sortQuery = sortReference.orderByChild("id").startAt(start).endAt(end);
                            }
                        }
                    }

                }
            }



        }

//        if (era == 1) {
//
//            sortQuery = sortReference.orderByChild("id").startAt(1).endAt(12999);
//
//        } else if (era == 2){
//
//            sortQuery = sortReference.orderByChild("id").startAt(13000).endAt(23999);
//
//        } else if (era == 3){
//
//            sortQuery = sortReference.orderByChild("id").startAt(24000).endAt(30999);
//
//        } else if (era == 50){
//
//            sortQuery = sortReference.orderByChild("id").startAt(50000).endAt(50999);
//
//        } else if (era == 49){
//
//            sortQuery = sortReference.orderByChild("id").startAt(49000).endAt(49999);
//
//        } else if (era == 50){
//
//            sortQuery = sortReference.orderByChild("id").startAt(50000).endAt(50999);
//
//
//        } else if (era == 51){
//
//            sortQuery = sortReference.orderByChild("id").startAt(52000).endAt(52999);
//
//        } else {
//
//            sortQuery = sortReference.orderByChild("id").startAt(31000).endAt(59999);
//
//
//        }

        rcvRefCollectionsX = findViewById(R.id.rcvRefCollections);
        rcvRefCollectionsX.setHasFixedSize(true); //Not sure this applies or why it is here
        // rcvCollectionsX.setLayoutManager(new LinearLayoutManager(this));    // different layout options - use 1 of the 3
        // rcvCollectionsX.setLayoutManager(new GridLayoutManager(this, 2));    // different layout options - use 1 of the 3
        rcvRefCollectionsX.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));  // different layout options - use 1 of the 3


        //FAB for popup menu
        fbtnPopUpMenuRefColsX = findViewById(R.id.fbtnPopUpMenuRefCols);
        fbtnPopUpMenuRefColsX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // showPopupMenuRefCols(view, true, R.style.MyPopupOtherStyle);
                showNewFABbasedMenuRefCollections();

            }
        });


         // The Code setting out recycler view /////////////////////////////////////////////////////////////////
         // The tutorial had this section of code through to setAdapter in separate on Start Method but for StaggeredGrid that seemed to cause the recycler view to be destroyed and not come back once we moved off the screen works fine here
        final FirebaseRecyclerAdapter<ZZZjcRefCollections, RefCollections.ZZZjcRefCollectionsViewHolder> firebaseRecyclerAdapter
            = new FirebaseRecyclerAdapter<ZZZjcRefCollections, RefCollections.ZZZjcRefCollectionsViewHolder>
            (ZZZjcRefCollections.class,R.layout.yyy_card_ref_collections, RefCollections.ZZZjcRefCollectionsViewHolder.class,sortQuery) {
            @Override
             protected void populateViewHolder(RefCollections.ZZZjcRefCollectionsViewHolder viewHolder, ZZZjcRefCollections model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDes(model.getDes());
                viewHolder.setImage(getApplicationContext(), model.getImageLink());
                viewHolder.setNotes(model.getNotes());

                viewHolder.setColuid(model.getColuid()); //so ridiculous the get and set functions have to be the same name as the variable like coluid = setColuid wtf
                viewHolder.setImageLink(model.getImageLink());
                viewHolder.setCoincount(model.getCoincount());
                viewHolder.setCoincountall(model.getCoincountall());

                viewHolder.setStandardref(model.getStandardref());

                }

               // The Code setting out recycler view /////////////////////////////////////////////////////////////////

              // This method is part of the onItemClick AND onLongItem Click code NOT to populate the recycler view /////////////////////////////////////////////////////
             @Override
             public ZZZjcRefCollectionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                ZZZjcRefCollectionsViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);

                viewHolder.setOnItemClickListener(new ZZZjcRefCollectionsViewHolder.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //Pulling data from view on card

                        //logic for popping up intestitial adds every x clicks on a reference collection

                        mAdvertCounter = sharedAdvertCounter.getInt("Counter", 0); // where if no settings
                        SharedPreferences.Editor editor = sharedAdvertCounter.edit();
                        editor.putInt("Counter", mAdvertCounter + 1);
                        editor.apply(); // saves the value

                        mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdLoaded() {
                                // Code to be executed when an ad finishes loading.
                            }

                            @Override
                            public void onAdFailedToLoad(int errorCode) {
                                // Code to be executed when an ad request fails.
                            }

                            @Override
                            public void onAdOpened() {
                                // Code to be executed when the ad is displayed.
                                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                            }

                            @Override
                            public void onAdClicked() {
                                // Code to be executed when the user clicks on an ad.
                            }

                            @Override
                            public void onAdLeftApplication() {
                                // Code to be executed when the user has left the app.
                            }

                            @Override
                            public void onAdClosed() {
                                // Code to be executed when the interstitial ad is closed.
                                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                            }
                        });

                        ////////////////// end of Ad Mob for on click ////////////////////////


                        TextView colUID = view.findViewById(R.id.crdRefTxtCollectionUID);
                        TextView colTitle = view.findViewById(R.id.crdRefTxtCollectionTitle);

                        TextView colStandardRef = view.findViewById(R.id.crdRefTxtStandardRef);

                        //get data from views

                        colUIDY = colUID.getText().toString();
                        colTitleY = colTitle.getText().toString();
                        colStandardRefY = colStandardRef.getText().toString();

                        // no need to pass the image to the CoinList Class but keeping the lines below as example code
                        Intent intent = new Intent(view.getContext(), RefCoinList.class);
//                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                        colBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                        byte [] bytes = stream.toByteArray();
//                        intent.putExtra("image", bytes); // put bitmap image as array of bytes
                        intent.putExtra("coluid", colUIDY);
                        intent.putExtra("title", colTitleY);
                        intent.putExtra("era", era);

                        intent.putExtra("standardref", colStandardRefY); // for Reference type - so RIC or Gnechi for example
                        startActivity(intent);

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                        //Pulling views from the card?
                        TextView colTitle = view.findViewById(R.id.crdRefTxtCollectionTitle);
                        TextView colDes = view.findViewById(R.id.crdRefTxtCollectionDes);
                        ImageView colImage = view.findViewById(R.id.crdRefImgCollectionImage);
                        TextView colNotes = view.findViewById(R.id.crdRefTxtCollectionNotes);
                        TextView colCoinCount = view.findViewById(R.id.crdRefCoinCount);
                        TextView colCoinCountall = view.findViewById(R.id.crdRefCoinCountall);


                    //get data from views

                    String colCoinCountY2 = colCoinCount.getText().toString();
                    colCoinCountY = Integer.parseInt(colCoinCountY2);

                    String colCoinCountallY2 = colCoinCountall.getText().toString();
                    colCoinCountallY = Integer.parseInt(colCoinCountallY2);

                    colTitleY = colTitle.getText().toString();
                    colDesY = colDes.getText().toString();
                    colImageY = colImage.getDrawable();

                    if (colImageY != null) {

                        colBitmap = ((BitmapDrawable) colImageY).getBitmap();
                    }

                    colNotesY = colNotes.getText().toString();


                    //getting these so we can use downstream in delete and modify methods
                    TextView colUID = view.findViewById(R.id.crdRefTxtCollectionUID);
                    colUIDY = colUID.getText().toString();

                    TextView colImageLink = view.findViewById(R.id.crdRefTxtCollectionImgLink);
                    colImageLinkY = colImageLink.getText().toString();


                    collectionDialogView();

                }
            });

            return viewHolder;
        }
    };

    ///////////////////////////////////////////////////////

    // The onclick methods were in the broader recycler view methods - this calls for the adapter on everything
        rcvRefCollectionsX.setAdapter(firebaseRecyclerAdapter);

    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

       //mRewardedAdRefCollections.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build()); // TEST
        mRewardedAdRefCollections.loadAd("ca-app-pub-1744081621312112/5372943816", new AdRequest.Builder().build()); // REAL
        adMobToggle = 1;

        SharedPreferences.Editor editor = sharedAdvertCounter.edit();
        editor = sharedAdvertCounter.edit();
        editor.putInt("Counter",0);
        editor.apply(); // saves the value
        mAdvertCounter = 0;

    }

    @Override
    public void onRewardedVideoAdClosed() {

        Log.i("REWARDEDLOG", "in closed");

        if (mRewardedAdRefCollections.isLoaded() && adMobToggle == 1) {
            mRewardedAdRefCollections.show();
            Log.i("REWARDEDLOG", "in closed and loaded   " + adMobToggle );
        } else if (adMobToggle == 1){

            CountDownTimer adtimer = new CountDownTimer(3000, 500) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {

                    Log.i("REWARDEDLOG", "in timer");
                    if (mRewardedAdRefCollections.isLoaded()) {
                        mRewardedAdRefCollections.show();
                        Log.i("REWARDEDLOG", "in closed after timer");
                    } else if (adMobToggle == 1){

                        CountDownTimer adtimer = new CountDownTimer(3000, 500) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {

                                Log.i("REWARDEDLOG", "in timer");
                                if (mRewardedAdRefCollections.isLoaded()) {
                                    mRewardedAdRefCollections.show();
                                    Log.i("REWARDEDLOG", "in closed after timer");
                                }

                            }
                        }.start();

                    }

                }
            }.start();

        }

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

        adMobToggle = 2;

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }

    ///////////////////////// END -------> ON-CREATE ////////////////////////////////////////////////////////////////////

    //////////////////////// START ------> RECYCLER VIEW COMPONENTS /////////////////////////////////////////////////////
    /////////////////// includes: viewholder, sort, expoloding card view dialog, functions from dialog //////////////////


    // View holder for the recycler view
    public static class ZZZjcRefCollectionsViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public ZZZjcRefCollectionsViewHolder(View itemView) {

            super(itemView);
            mView = itemView;

            // Custom built onItemClickListener for the recycler view
            ////////////////////////////////////////////////////////////////////////////////////////
            //Listen to the video as this is a bit confusing - also added the OnTimeclick listener above to the parameters NOT

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    hpListener.onItemClick(view, getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    hpListener.onItemLongClick(view, getAdapterPosition());

                    return true;
                }
            });

            ////////////////////////////////////////////////////////////////////////////////////////

        }

        public void setTitle(String title){

            TextView crdRefTxtCollectionTitleX = (TextView)mView.findViewById(R.id.crdRefTxtCollectionTitle);
            crdRefTxtCollectionTitleX.setText(title);

        }

        public void setDes(String des){

            TextView crdRefTxtCollectionDesX = (TextView)mView.findViewById(R.id.crdRefTxtCollectionDes);
            crdRefTxtCollectionDesX.setText(des);

        }

        public void setImage(Context ctx, String imageLink){

            ImageView crdRefImgCollectionsImgX = (ImageView) mView.findViewById(R.id.crdRefImgCollectionImage);
            Picasso.get().load(imageLink).into(crdRefImgCollectionsImgX); //tutorial had with which got renamed to get but with took ctx as parameter...


        }

        public void setNotes(String notes) {
            TextView crdRefTxtCollectionNotesX = (TextView)mView.findViewById(R.id.crdRefTxtCollectionNotes);
            crdRefTxtCollectionNotesX.setText(notes);

        }

        public void setCoincount(int coincount) {

            TextView crdRefCoinCountX = (TextView)mView.findViewById(R.id.crdRefCoinCount);
            String coincount2 = String.valueOf(coincount);
            crdRefCoinCountX.setText(coincount2);

        }

        public void setCoincountall(int coincountall) {

            TextView crdRefCoinCountallX = (TextView)mView.findViewById(R.id.crdRefCoinCountall);
            String coincountall2 = String.valueOf(coincountall);
            crdRefCoinCountallX.setText(coincountall2);

        }

        // these are setting hiddent textviews in cardView which can then passvalues to child views like expanded collectio or delete method

        public void setColuid(String coluid) {
            TextView crdRefTxtCollectionUIDX = (TextView)mView.findViewById(R.id.crdRefTxtCollectionUID);
            crdRefTxtCollectionUIDX.setText(coluid);
        }

        public void setImageLink(String imageLink) {
            TextView crdRefTxtCollectionImgLinkX = (TextView)mView.findViewById(R.id.crdRefTxtCollectionImgLink);
            crdRefTxtCollectionImgLinkX.setText(imageLink);
        }

        public void setStandardref(String standardref) {
            TextView crdRefTxtCollectionStandardRefX = (TextView)mView.findViewById(R.id.crdRefTxtStandardRef);
            crdRefTxtCollectionStandardRefX.setText(standardref);
        }


        // Custom built onItemClickListener for the recycler view; seems to cover LongClick as well
        ////////////////////////////////////////////////////////////////////////////////////////
        //Listen to the video as this is a bit confusing

        private OnItemClickListener hpListener;

        public interface OnItemClickListener {

            void onItemClick(View view, int position);
            void onItemLongClick (View view, int position);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {

            hpListener = listener;
        }

        ///////////////////////////////////////////////////////////////////////////////////////


    } /////////////// end of viewHolder

    private void collectionDialogView() {

        shadeX.setVisibility(View.VISIBLE);

        //Everything in this method is code for a custom dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.zzx_dia_view_ref_collection, null);

        dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.show();
        dialog.getWindow().setAttributes(lp);


        if (colImageY != null) {
            ImageView imgColDetailImageX = view.findViewById(R.id.imgColDetailImage);
            imgColDetailImageX.setImageBitmap(colBitmap);
        }

        TextView txtColDetailCoinCountX = view.findViewById(R.id.txtColDetailCoinCount);
       //String colCoinCountY3 = String.valueOf(colCoinCountY); version below gets it to good numbers format with ","
        String colCoinCountY3 = NumberFormat.getNumberInstance(Locale.US).format(colCoinCountY);
        txtColDetailCoinCountX.setText(colCoinCountY3);

        TextView txtColDetailCoinCountallX = view.findViewById(R.id.txtColDetailCoinCountall);
        //String colCoinCountY3 = String.valueOf(colCoinCountY); version below gets it to good numbers format with ","
        String colCoinCountallY3 = NumberFormat.getNumberInstance(Locale.US).format(colCoinCountallY);
        txtColDetailCoinCountallX.setText(colCoinCountallY3);

        TextView txtColDetailTitleX = view.findViewById(R.id.txtColDetailTitle);
        txtColDetailTitleX.setText(colTitleY);
        TextView txtColDetailDesX = view.findViewById(R.id.txtColDetailDesc);
        txtColDetailDesX.setText(colDesY);


        TextView txtColDetailNotesX = view.findViewById(R.id.txtColDetailNotes);
        txtColDetailNotesX.setText(colNotesY);


        ImageView imgColDetBackX = view.findViewById(R.id.imgColDetBack);
        imgColDetBackX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                shadeX.setVisibility(View.INVISIBLE);
                dialog.dismiss();

            }

        });

        ImageView imgColDetEnterX = view.findViewById(R.id.imgColDetEnter);
        imgColDetEnterX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), RefCoinList.class);
//
                intent.putExtra("coluid", colUIDY);
                intent.putExtra("title", colTitleY);
                startActivity(intent);
                shadeX.setVisibility(View.INVISIBLE);
                dialog.dismiss();
            }
        });



        //if dismissed in any way like a backbutton resets the view on RefCollections to normal
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                shadeX.setVisibility(View.INVISIBLE);
            }
        });

    }

    //////////////////////// END -------->>> RECYCLER VIEW COMPONENTS /////////////////////////////////////////////////////
    /////////////////// includes: viewholder, sort, expoloding card view dialog, functions from dialog //////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////// START ----->>> POP-UP ////////////////////////////////////////////////////////////////////

    /////////////////// Start-New Pop up Version //////////////////////////////////////////////////////////////////////

    @SuppressLint("RestrictedApi") // suppresses the issue with not being able to use visibility with the FAB
    private void showNewFABbasedMenuRefCollections() {

        popupMenuToggle = "pressed";

        fbtnPopUpMenuRefColsX.setVisibility(View.GONE);
        fbtnPopUp2RefCollectionsX.setVisibility(View.VISIBLE);

        fbtnMiniMyCollectionsRefCollectionsX.setVisibility(View.VISIBLE);
        fbtnMiniFAQRefCollectionsX.setVisibility(View.VISIBLE);
        fbtnMiniLogoutRefCollectionsX.setVisibility(View.VISIBLE);

        txtRefCoinsButtonRefCollectionsX.setVisibility(View.VISIBLE);
        txtFAQButtonRefCollectionsX.setVisibility(View.VISIBLE);
        txtLogoutButtonRefCollectionsX.setVisibility(View.VISIBLE);

        shadeX.setVisibility(View.VISIBLE);

        fbtnPopUp2RefCollectionsX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fbtnPopUpMenuRefColsX.setVisibility(View.VISIBLE);
                fbtnPopUp2RefCollectionsX.setVisibility(View.GONE);

                fbtnMiniMyCollectionsRefCollectionsX.setVisibility(View.GONE);
                fbtnMiniFAQRefCollectionsX.setVisibility(View.GONE);
                fbtnMiniLogoutRefCollectionsX.setVisibility(View.GONE);

                txtRefCoinsButtonRefCollectionsX.setVisibility(View.GONE);
                txtFAQButtonRefCollectionsX.setVisibility(View.GONE);
                txtLogoutButtonRefCollectionsX.setVisibility(View.GONE);

                shadeX.setVisibility(View.GONE);
                popupMenuToggle = "Not";

            }
        });

        fbtnMiniMyCollectionsRefCollectionsX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RefCollections.this, HomePage.class);
                startActivity(intent);
                popupMenuToggle = "Not";
            }
        });

        fbtnMiniFAQRefCollectionsX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fbtnPopUpMenuRefColsX.setVisibility(View.VISIBLE);
                fbtnPopUp2RefCollectionsX.setVisibility(View.GONE);

                fbtnMiniMyCollectionsRefCollectionsX.setVisibility(View.GONE);
                fbtnMiniFAQRefCollectionsX.setVisibility(View.GONE);
                fbtnMiniLogoutRefCollectionsX.setVisibility(View.GONE);

                txtRefCoinsButtonRefCollectionsX.setVisibility(View.GONE);
                txtFAQButtonRefCollectionsX.setVisibility(View.GONE);
                txtLogoutButtonRefCollectionsX.setVisibility(View.GONE);

                shadeX.setVisibility(View.GONE);
                popupMenuToggle = "Not";

                faqDialogView();

            }
        });

        fbtnMiniLogoutRefCollectionsX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fbtnPopUpMenuRefColsX.setVisibility(View.VISIBLE);
                fbtnPopUp2RefCollectionsX.setVisibility(View.GONE);

                fbtnMiniMyCollectionsRefCollectionsX.setVisibility(View.GONE);
                fbtnMiniFAQRefCollectionsX.setVisibility(View.GONE);
                fbtnMiniLogoutRefCollectionsX.setVisibility(View.GONE);

                txtRefCoinsButtonRefCollectionsX.setVisibility(View.GONE);
                txtFAQButtonRefCollectionsX.setVisibility(View.GONE);
                txtLogoutButtonRefCollectionsX.setVisibility(View.GONE);

                shadeX.setVisibility(View.GONE);
                popupMenuToggle = "Not";

                //Confirm the user wants to logout and execute
                alertDialogLogOut();

            }
        });

    }

    /////////////////// END-New Pop up Version //////////////////////////////////////////////////////////////////////

    /////////////////// Start-OLD Pop up Version //////////////////////////////////////////////////////////////////////

    // Main popup menu functionality called when popup menu FAB pressed
    private void showPopupMenuRefCols(View anchor, boolean isWithIcons, int style) {
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
        popup.getMenuInflater().inflate(R.menu.ref_collections_menu, popup.getMenu());

        //implement click events
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.popMenuMyCollections:

                        Intent intent = new Intent(RefCollections.this, HomePage.class);
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

    private void logoutSnackbar(){


        Snackbar snackbar;

        snackbar = Snackbar.make(loutRefCollectionsActLOX, "Good bye", Snackbar.LENGTH_SHORT);


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

    private void oneTimeInfoLogin() {

        // taking this out as we're presenting in ref collection Dir now
        //Everything in this method is code for a custom dialog
//        LayoutInflater inflater = LayoutInflater.from(this);
//        View view = inflater.inflate(R.layout.zzzz_otinfo_refcollections, null);
//
//        dialog = new AlertDialog.Builder(this)
//                .setView(view)
//                .create();
//
//        dialog.show();
//
//        Button btnOKoneTimeHPX = view.findViewById(R.id.btnOKoneTimeRC);
//        btnOKoneTimeHPX.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                dialog.dismiss();
//
//            }
//        });
    }

    // checks this is the app is run first time which we use to decide whether to show the one time info dialogs
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

    private void faqDialogView() {

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

    //Dialog that comes up when user chooses logout from the popup menu; strange legacy menu name but standard yes no dialog
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

                firebaseAuthRefCollections.signOut();
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
    //Method called from LogOut to get us back to Login screen
    private void transitionBackToLogin () {

        new CountDownTimer(1000, 500) {


            public void onTick(long millisUntilFinished) {
                // imgCoverR.animate().rotation(360).setDuration(500); // why only turned once?
            }

            public void onFinish() {
                Intent intent = new Intent(RefCollections.this, Login.class);
                startActivity(intent);
                finish();
            }
        }.start();

    }

    @Override
    @SuppressLint("RestrictedApi") // suppresses the issue with not being able to use visibility with the FAB
    public void onBackPressed(){

        if (popupMenuToggle.equals("pressed")) {

            fbtnPopUpMenuRefColsX.setVisibility(View.VISIBLE);
            fbtnPopUp2RefCollectionsX.setVisibility(View.GONE);

            fbtnMiniMyCollectionsRefCollectionsX.setVisibility(View.GONE);
            fbtnMiniFAQRefCollectionsX.setVisibility(View.GONE);
            fbtnMiniLogoutRefCollectionsX.setVisibility(View.GONE);

            txtRefCoinsButtonRefCollectionsX.setVisibility(View.GONE);
            txtFAQButtonRefCollectionsX.setVisibility(View.GONE);
            txtLogoutButtonRefCollectionsX.setVisibility(View.GONE);

            shadeX.setVisibility(View.GONE);
            popupMenuToggle = "Not";

        } else {

            Intent intent = new Intent(RefCollections.this, RefEmperorDir.class); // needs passback of era
            intent.putExtra("era", eraBack);
            startActivity(intent);
            finish();

        }

    }

    //////////////////////// END ------->>> LOGOUT AND ON-BACK PRESS  /////////////////////////////////////////////////////

    ///////////////////////// START ----->>> AD MOB /////////////////////////////////////////////////////

    public void initAdverts() {

        MobileAds.initialize(this, getString(R.string.test_banner_ad));

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.test_interstitial_ad));

        mInterstitialAd.setAdListener(new AdListener());






    }

}
