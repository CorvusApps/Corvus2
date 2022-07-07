package com.pelotheban.corvus;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
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
import com.google.android.material.button.MaterialButton;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
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


public class RefCoinList extends AppCompatActivity implements RewardedVideoAdListener {


    // UI and data components for transfering collection ID from Homepage through here to AddCoin and ShowCoin activities - as well as getting back to right RefCollections sort
    private TextView txtRefCListCollUIDX;
    private String cRefListuid;
    private int era;
     // temp note2
    // Firebase related
    private FirebaseAuth firebaseAuthRefCoins;
    private DatabaseReference coinRefDatabase;

    //UI Components
    private RecyclerView rcvRefCoinsX;

    private int cardRefToggle; // allows for onclick expansion and deflation of coin cards

    private Dialog dialog; //universal dialog instance variable used for most dialogs in the activity
    private CoordinatorLayout loutRefCoinListActLOX; //primarily used for snackbars

    private ProgressDialog pd; // universal progress dialog used in this activity

    private FloatingActionButton fbtnPopUpMenuRefCoinListX;

    // components for new FAB based pop-up menu
    private FloatingActionButton fbtnPopUp2RefCoinsX, fbtnMiniMyCollectionsRefCoinsX, fbtnMiniFAQRefCoinsX, fbtnMiniLogoutRefCoinsX;
    private TextView txtRefCoinsButtonRefCoinsX, txtFAQButtonRefCoinsX, txtLogoutButtonRefCoinsX;
    private String popupMenuToggle; // need this to know menu state so things like back press and card press buttons do their regular function or toggle the menu

    // custom view to use as a shade behind custom dialogs
    private View shadeX;

    //creating instance variables that can be used to pass info to the coin modify screen

        //TODO - verify and delete this whole group
    private Bitmap coinBitmap;
    private Drawable coinImageY;

    private String coinPersonageY, coinDenominationY, coinMintY, coinRICvarY, coinWeightY, coinDiameterY, coinObvDescY, coinObvLegY, coinRevDescY
            , coinRevLegY, coinProvenanceY, coinNotesY, coinImageLinkY, coinUIDY, coinSortRicY;
    private int coinRICY,  coinValueY, coinSortRicYint;

    //need to get imageLink before removing the values so can delete it later
    //TODO - verify and delete this whole group
    private String deleteImageLink;

    //For Sorting
    //TODO - verify and delete this whole group NEED THE LAYOUT MANAGER
    private LinearLayoutManager layoutManagerCoins;
    private SharedPreferences sortRefSharedPrefCoins;

    private Query sortRefQueryCoins;

    // card elements for on-click functionality

    LinearLayout loutRefCoinObvDescX;

    TextView txtRefObvDescX;

    // in-card buttons for replicate and duplicate examples
    //TextView txtCardRefReplicateBtnX; no longer using this but keeping for now
    MaterialButton btnDuplicatesX , btnReplicateX;
    LinearLayout loutDupAndRepX;

    // adMob

    InterstitialAd mInterstitialAdRefCoinList;
    int mAdvertCounterRefCoinList;
    private SharedPreferences sharedAdvertCounterRefCoinList;

    RewardedVideoAd mRewardedAdRefCoinList;
    TextView txtAdMessageX;
    int adMobToggle;
    private int adRewFailedToggle;


    @Override
    @SuppressLint("RestrictedApi") // suppresses the issue with not being able to use visibility with the FAB
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ref_coin_list);

        //To be shown first time only as intro info
        if (isFirstTime()) {
            oneTimeInfoLogin();
        }

        //adMob
        sharedAdvertCounterRefCoinList = getSharedPreferences("adSettingRefCoinList", MODE_PRIVATE);
        mAdvertCounterRefCoinList = sharedAdvertCounterRefCoinList.getInt("CounterRefCoinList", 0); // where if no settings

        MobileAds.initialize(this, "ca-app-pub-1744081621312112~1448123556");

        adRewFailedToggle = 0;

        mRewardedAdRefCoinList = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedAdRefCoinList.setRewardedVideoAdListener(this);
       // mRewardedAdRefCoinList.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build()); // TEST
        mRewardedAdRefCoinList.loadAd("ca-app-pub-1744081621312112/8222753841", new AdRequest.Builder().build()); // REAL

        if (mAdvertCounterRefCoinList > 10) {

            Log.i("REWARDEDLOG", "in counter if");

            if (mRewardedAdRefCoinList.isLoaded()) {
                mRewardedAdRefCoinList.show();

                Log.i("REWARDEDLOG", "loadedif");
            } else {
                Log.i("REWARDEDLOG", "in counter if 2");

                CountDownTimer adtimer = new CountDownTimer(3000, 500) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        Log.i("REWARDEDLOG", "in finished first timer");

                        if (mRewardedAdRefCoinList.isLoaded()) {
                            mRewardedAdRefCoinList.show();
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
                                    if (mRewardedAdRefCoinList.isLoaded()) {
                                        mRewardedAdRefCoinList.show();
                                        Log.i("REWARDEDLOG", "in show after second timer");
                                    }

                                }
                            }.start();

                        }

                    }
                }.start();

            }
        }

        mInterstitialAdRefCoinList = new InterstitialAd(RefCoinList.this);
        mInterstitialAdRefCoinList.setAdUnitId(getString(R.string.test_interstitial_ad));
        //mInterstitialAdRefCoinList.setAdUnitId(getString(R.string.refcoinlist_interstitial_ad));

        mInterstitialAdRefCoinList.loadAd(new AdRequest.Builder().build());

        //Toast.makeText(RefCoinList.this, mAdvertCounterRefCoinList + "", Toast.LENGTH_SHORT).show();

        mInterstitialAdRefCoinList.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.

                if (mAdvertCounterRefCoinList > 14) {

                    mInterstitialAdRefCoinList.show();
                    SharedPreferences.Editor editor = sharedAdvertCounterRefCoinList.edit();
                    editor.putInt("CounterRefCoinList", 0); // this only kicks in on next on create so need to set actual mAdvertCounter to 0 below so the add does not loop
                    editor.apply(); // saves the value
                    mAdvertCounterRefCoinList = 0;
                }
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                mInterstitialAdRefCoinList.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                mInterstitialAdRefCoinList.loadAd(new AdRequest.Builder().build());

            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                mInterstitialAdRefCoinList.loadAd(new AdRequest.Builder().build());
            }
        });

        // FABs and TXTs for new pop up menu components

        popupMenuToggle = "Not";

        fbtnPopUp2RefCoinsX = findViewById(R.id.fbtnPopUp2RefCoins);

        fbtnMiniMyCollectionsRefCoinsX = findViewById(R.id.fbtnMiniMyCollectionsRefCoins);
        fbtnMiniFAQRefCoinsX = findViewById(R.id.fbtnMiniFAQRefCoins);
        fbtnMiniLogoutRefCoinsX = findViewById(R.id.fbtnMiniLogoutRefCoins);

        txtRefCoinsButtonRefCoinsX = findViewById(R.id.txtMyCollectionsButtonRefCoins);
        txtFAQButtonRefCoinsX = findViewById(R.id.txtFAQButtonRefCoins);
        txtLogoutButtonRefCoinsX = findViewById(R.id.txtLogoutButtonRefCoins);



        //FAB for popup menu

        fbtnPopUpMenuRefCoinListX = findViewById(R.id.fbtnPopUpMenuRefCoinList);
        fbtnPopUpMenuRefCoinListX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //showPopupMenuRefCols(view, true, R.style.MyPopupOtherStyle);
                showNewFABbasedMenuRefCoins();

            }
        });

        // UI and data components for transfering collection ID from Homepage through here to AddCoin and ShowCoin activities - as well as getting back to the right RefCollections
        txtRefCListCollUIDX = findViewById(R.id.txtRefCListCollUID);
        cRefListuid = getIntent().getStringExtra("coluid");
        txtRefCListCollUIDX.setText(cRefListuid);
        era = getIntent().getIntExtra("era", 0);

        //setting the title to the coinlist which is the collection name
        TextView txtRefCollectionNameCoinList = findViewById(R.id.txtRefCoinListCollectionName);
        String cListRefColName = getIntent().getStringExtra("title");
        txtRefCollectionNameCoinList.setText("REF. COLLECTION " + cListRefColName);



        // Firebase related
        firebaseAuthRefCoins = FirebaseAuth.getInstance();

        coinRefDatabase = FirebaseDatabase.getInstance().getReference().child("my_users").child("vPlrYZXdGHgRotLma4OVopIRKY02")
                .child("collections").child(cRefListuid).child("coins");
        coinRefDatabase.keepSynced(true);


        //Shared preferences for sorting
        sortRefSharedPrefCoins = getSharedPreferences("SortSetting3", MODE_PRIVATE);
        String mSorting2 = sortRefSharedPrefCoins.getString("Sort2", "sortric"); // where if no settings - so here in refcoins it defaults to this

        if(mSorting2.equals("sortric")) {

            sortRefQueryCoins = coinRefDatabase.orderByChild("sortric").endAt(999999999);
            layoutManagerCoins = new LinearLayoutManager(this);
            layoutManagerCoins.setReverseLayout(false);

        }

        //UI Components
        rcvRefCoinsX = findViewById(R.id.rcvRefCoins);
        rcvRefCoinsX.setHasFixedSize(true); //Not sure this applies or why it is here
        rcvRefCoinsX.setLayoutManager(layoutManagerCoins);

        cardRefToggle = 0; // allows for onclick expansion and deflation of coin cards

        loutRefCoinListActLOX = findViewById(R.id.loutRefCoinListActLO); //primarily used for snackbars); //primarily used for snackbars

        // custom view to use as a shade behind custom dialogs
        shadeX = findViewById(R.id.shade);

        //setting this up so when we have our FAB popup menu clicking anywhere on the scrreen will turn it off
        shadeX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fbtnPopUpMenuRefCoinListX.setVisibility(View.VISIBLE);
                fbtnPopUp2RefCoinsX.setVisibility(View.GONE);

                fbtnMiniMyCollectionsRefCoinsX.setVisibility(View.GONE);
                fbtnMiniFAQRefCoinsX.setVisibility(View.GONE);
                fbtnMiniLogoutRefCoinsX.setVisibility(View.GONE);

                txtRefCoinsButtonRefCoinsX.setVisibility(View.GONE);
                txtFAQButtonRefCoinsX.setVisibility(View.GONE);
                txtLogoutButtonRefCoinsX.setVisibility(View.GONE);

                shadeX.setVisibility(View.GONE);
                popupMenuToggle = "Not";

            }
        });

// The Code setting out recycler view /////////////////////////////////////////////////////////////////
// The tutorial had this section of code through to setAdapter in separate on Start Method but for StaggeredGrid that seemed to cause the recycler view to be destroyed and not come back once we moved off the screen works fine here


        final FirebaseRecyclerAdapter<ZZZjcRefCoins, ZZZjcRefCoinsViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<ZZZjcRefCoins, ZZZjcRefCoinsViewHolder>
                (ZZZjcRefCoins.class,R.layout.yyy_card_ref_coin, ZZZjcRefCoinsViewHolder.class,sortRefQueryCoins) {


            @Override
            protected void populateViewHolder(ZZZjcRefCoinsViewHolder viewHolder, ZZZjcRefCoins model, int position) {
                viewHolder.setPersonage(model.getPersonage());
                viewHolder.setDenomination(model.getDenomination());
                viewHolder.setImage(getApplicationContext(),model.getImageLink());
                viewHolder.setRicvar(model.getRicvar());
                viewHolder.setId(model.getId());
                viewHolder.setWeight(model.getWeight());
                viewHolder.setDiameter(model.getDiameter());

                viewHolder.setMint(model.getMint());
                viewHolder.setObvDesc(model.getObvdesc());
                viewHolder.setObvLeg(model.getObvleg());
                viewHolder.setRevDesc(model.getRevdesc());
                viewHolder.setRevLeg(model.getRevleg());
                viewHolder.setProvenance(model.getProvenance());
                viewHolder.setValue(model.getValue());
                viewHolder.setNotes(model.getNotes());

                viewHolder.setCoinuid(model.getCoinuid()); //so ridiculous the get and set functions have to be the same name as the variable like coluid = setColuid wtf
                viewHolder.setImageLink(model.getImageLink());

                viewHolder.setLayouts();

                viewHolder.setRICLabel(model.getId(), model.getRicvar());

                viewHolder.setSortric(model.getSortric());
            }

            // The Code setting out recycler view /////////////////////////////////////////////////////////////////

            // This method is part of the onItemClick AND onLongItem Click code NOT to populate the recycler view /////////////////////////////////////////////////////
            @Override
            public ZZZjcRefCoinsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                ZZZjcRefCoinsViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);

                viewHolder.setOnItemClickListener(new ZZZjcRefCoinsViewHolder.OnItemClickListener() {


                    @Override
                    public void onItemLongClick(View view, int position) {


                    }

                    // inflates card from recycler view to see fields not visible in base view
                    @Override
                    public void onItemClick(View view, int position) {

                          // layouts that toggle from GONE to INVISIBLE to expand card
                        loutRefCoinObvDescX = view.findViewById(R.id.loutRefCoinObvDesc);
                        LinearLayout loutRefCoinObvLegX = view.findViewById(R.id.loutRefCoinObvLeg);
                        LinearLayout loutRefCoinRevLegX = view.findViewById(R.id.loutRefCoinRevLeg);
                        LinearLayout loutRefCoinProvenanceX = view.findViewById(R.id.loutRefCoinProvenance);
                        LinearLayout loutRefCoinNotesX = view.findViewById(R.id.loutRefCoinNotes);


                        txtRefObvDescX = view.findViewById(R.id.txtRefObvDesc);
                        TextView txtRefObvLegX = view.findViewById(R.id.txtRefObvLeg);
                        TextView txtRefRevLegX = view.findViewById(R.id.txtRefRevLeg);
                        TextView txtRefProvenanceX = view.findViewById(R.id.txtRefProvenance);
                        //TextView txtRefValueX = view.findViewById(R.id.txtValue); // need to use the one below which is unformatted because can't turn it integer with comas
                        TextView txtRefCardValueUnformattedX = view.findViewById(R.id.txtRefCardValueUnformatted);

                        TextView txtRefNotesX = view.findViewById(R.id.txtRefNotes);

                        //Bellow getting more views from card to feed replicate as needed but can skip the items already initialized above (e.g. obvleg)

                        //Pulling views from the card
                        TextView txtRefPersonageX = view.findViewById(R.id.txtRefCardPersonage);
                        TextView txtRefDenominationX = view.findViewById(R.id.txtRefCardDenomination);
                        TextView txtRefMintX = view.findViewById(R.id.txtRefCardMint);
                        TextView txtRefRICX = view.findViewById(R.id.txtRefCardRIC);
                        TextView txtRefRICvarX = view.findViewById(R.id.txtRefCardRICvar);

                        TextView txtRefRevDescX = view.findViewById(R.id.txtRefRevDesc);

                        TextView txtRefCardImgLinkX = view.findViewById(R.id.txtRefCardImgLink);

                        // To feed duplicate
                        TextView txtRefCardSortricX = view.findViewById(R.id.txtRefCardSortric);

                        //get data from views to feed replicate

                        coinPersonageY = txtRefPersonageX.getText().toString();
                        coinDenominationY = txtRefDenominationX.getText().toString();
                        coinMintY = txtRefMintX.getText().toString();

                        coinRICvarY = txtRefRICvarX.getText().toString();

                        coinObvDescY = txtRefObvDescX.getText().toString();
                        coinObvLegY = txtRefObvLegX.getText().toString();
                        coinRevLegY = txtRefRevLegX.getText().toString();
                        coinRevDescY = txtRefRevDescX.getText().toString();

                        coinNotesY = txtRefNotesX.getText().toString();

                        //get data from views to duplicates (some gets pulled in from replicate as well)
                        //keeping sortric as string and then manipulating it below OR in duplicates

                        coinSortRicY = txtRefCardSortricX.getText().toString();
                        coinSortRicYint = Integer.parseInt(coinSortRicY);

                        coinImageLinkY = txtRefCardImgLinkX.getText().toString();



                        //the RIC and Value have to be converted to an int before being put to coinadd class
                        final String coinRICYpre = txtRefRICX.getText().toString();
                        coinRICY = Integer.parseInt(coinRICYpre);


                        if (coinObvDescY.equals("BREAKER")) {


                        } else {

                            if (cardRefToggle != 1) {

                                cardRefToggle = 1;


                                //bring out replicate function once the card is clicked the first time
                                // txtCardRefReplicateBtnX = view.findViewById(R.id.txtRefCardReplicateBtn);
                                // txtCardRefReplicateBtnX.setVisibility(View.VISIBLE); // This is the old button which should go away replaced by buttons below

                                loutDupAndRepX = view.findViewById(R.id.loutDupAndRep);
                                loutDupAndRepX.setVisibility(View.VISIBLE);

                                btnDuplicatesX = view.findViewById(R.id.btnDuplicates);
                                btnDuplicatesX.setVisibility(View.VISIBLE);

                                btnReplicateX = view.findViewById(R.id.btnReplicate);
                                btnReplicateX.setVisibility(View.VISIBLE);

                                btnReplicateX.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(RefCoinList.this, ReplicationColList.class);
                                        //intent.putExtra("coluid", cListuid); May need to get col ID through the model even that may not work

//                                    //we don't have any of this stuff coming from RefCoin List but will need to grab it from the ListView so keeping here as reminder
//                                    intent.putExtra("coluid", cListuid);
//                                    intent.putExtra("title", cListColName);
//                                    intent.putExtra("coincount", coinListItemCountInt);
//                                    intent.putExtra("colvalue", coinListColValueInt);
//                                    intent.putExtra("standardref", cListStandardRef);

                                        intent.putExtra("personage", coinPersonageY);
                                        intent.putExtra("denomination", coinDenominationY);
                                        intent.putExtra("mint", coinMintY);
                                        intent.putExtra("ricvar", coinRICvarY);
                                        intent.putExtra("obvdesc", coinObvDescY);
                                        intent.putExtra("obvleg", coinObvLegY);
                                        intent.putExtra("revdesc", coinRevDescY);
                                        intent.putExtra("revleg", coinRevLegY);
                                        intent.putExtra("notes", coinNotesY);

                                        intent.putExtra("id", coinRICY);


                                        intent.putExtra("replicate", "yes");

                                        startActivity(intent);
                                    }
                                });

                                btnDuplicatesX.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        //Toast.makeText(RefCoinList.this, coinPersonageY + coinRICY + " " + coinSortRicYint, Toast.LENGTH_LONG).show();

                                        Intent intent = new Intent(RefCoinList.this, RefDupCoinList.class);

                                        intent.putExtra("personage", coinPersonageY);
                                        intent.putExtra("id", coinRICY); // integer
                                        intent.putExtra("sortric", coinSortRicYint); //integer
                                        intent.putExtra("coluid", cRefListuid);
                                        intent.putExtra("ricvar", coinRICvarY);

                                        // extra stuff for static source coin

                                        intent.putExtra("obvdesc", coinObvDescY);
                                        intent.putExtra("obvleg", coinObvLegY);
                                        intent.putExtra("revdesc", coinRevDescY);
                                        intent.putExtra("revleg", coinRevLegY);

                                        intent.putExtra("imageLink", coinImageLinkY);

                                        startActivity(intent);

                                    }
                                });

                                // checking to see if there are values in the various fields before inflating them
//                            if (txtRefObvDescX.getText().toString().isEmpty()) {
//                            } else {
                                loutRefCoinObvDescX.setVisibility(View.VISIBLE);
//                            }

                                if (txtRefObvLegX.getText().toString().isEmpty()) {
                                } else {
                                    loutRefCoinObvLegX.setVisibility(View.VISIBLE);
                                }

                                if (txtRefRevLegX.getText().toString().isEmpty()) {
                                } else {
                                    loutRefCoinRevLegX.setVisibility(View.VISIBLE);
                                }

                                if (txtRefProvenanceX.getText().toString().isEmpty() && Integer.parseInt(txtRefCardValueUnformattedX.getText().toString()) == 0) {
                                } else {
                                    loutRefCoinProvenanceX.setVisibility(View.VISIBLE);
                                }

                                if (txtRefNotesX.getText().toString().isEmpty()) {
                                } else {
                                    loutRefCoinNotesX.setVisibility(View.VISIBLE);
                                }

                            } else if (cardRefToggle == 1) {

                                cardRefToggle = 0;

                                loutRefCoinObvDescX.setVisibility(View.GONE);
                                loutRefCoinObvLegX.setVisibility(View.GONE);
                                loutRefCoinRevLegX.setVisibility(View.GONE);
                                loutRefCoinProvenanceX.setVisibility(View.GONE);
                                loutRefCoinNotesX.setVisibility(View.GONE);

                                btnReplicateX.setVisibility(View.GONE);
                                loutDupAndRepX.setVisibility(View.GONE);

                            }
                        }

                        //logic for popping up intestitial adds every x clicks on a reference collection

                        mAdvertCounterRefCoinList = sharedAdvertCounterRefCoinList.getInt("CounterRefCoinList", 0); // where if no settings
                        SharedPreferences.Editor editor = sharedAdvertCounterRefCoinList.edit();
                        editor.putInt("CounterRefCoinList", mAdvertCounterRefCoinList + 1);
                        editor.apply(); // saves the value
                        mAdvertCounterRefCoinList = mAdvertCounterRefCoinList + 1;

                        if (mAdvertCounterRefCoinList > 10) {

                            Log.i("REWARDEDLOG", "in counter if");

                            if (mRewardedAdRefCoinList.isLoaded()) {
                                mRewardedAdRefCoinList.show();
                                Log.i("REWARDEDLOG", "loadedif");
                            }
                        }

                        if (mAdvertCounterRefCoinList > 14) {

                            mInterstitialAdRefCoinList.show();
                            editor = sharedAdvertCounterRefCoinList.edit();
                            editor.putInt("CounterRefCoinList", 0); // this only kicks in on next on create so need to set actual mAdvertCounter to 0 below so the add does not loop
                            editor.apply(); // saves the value
                            mAdvertCounterRefCoinList = 0;

                            mInterstitialAdRefCoinList.setAdListener(new AdListener() {
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
                                    mInterstitialAdRefCoinList.loadAd(new AdRequest.Builder().build());
                                }

                                @Override
                                public void onAdClicked() {
                                    // Code to be executed when the user clicks on an ad.
                                    mInterstitialAdRefCoinList.loadAd(new AdRequest.Builder().build());

                                }

                                @Override
                                public void onAdLeftApplication() {
                                    // Code to be executed when the user has left the app.
                                }

                                @Override
                                public void onAdClosed() {
                                    // Code to be executed when the interstitial ad is closed.
                                    mInterstitialAdRefCoinList.loadAd(new AdRequest.Builder().build());
                                }
                            });


                        }


                        ///////////////// end of ad Mob on item click ////////////////////

                    }

                });

                return viewHolder;
            }

        };

        ///////////////////////////////////////////////////////

        // The onclick methods were in the broader recycler view methods - this calls for the adapter on everything
        rcvRefCoinsX.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

        //mRewardedAdRefCoinList.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build()); // TEST
        mRewardedAdRefCoinList.loadAd("ca-app-pub-1744081621312112/8222753841", new AdRequest.Builder().build()); // REAL
        adMobToggle = 1;

        SharedPreferences.Editor editor = sharedAdvertCounterRefCoinList.edit();
        editor = sharedAdvertCounterRefCoinList.edit();
        editor.putInt("CounterRefCoinList", 0); // this only kicks in on next on create so need to set actual mAdvertCounter to 0 below so the add does not loop
        editor.apply(); // saves the value
        mAdvertCounterRefCoinList = 0;

    }

    @Override
    public void onRewardedVideoAdClosed() {

        Log.i("REWARDEDLOG", "in closed");

        if (mRewardedAdRefCoinList.isLoaded() && adMobToggle == 1) {
            mRewardedAdRefCoinList.show();
            Log.i("REWARDEDLOG", "in closed and loaded   " + adMobToggle );
        } else if (adMobToggle == 1){

            CountDownTimer adtimer = new CountDownTimer(3000, 500) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {

                    Log.i("REWARDEDLOG", "in timer");
                    if (mRewardedAdRefCoinList.isLoaded()) {
                        mRewardedAdRefCoinList.show();
                        Log.i("REWARDEDLOG", "in closed after timer");
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
    public static class ZZZjcRefCoinsViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public ZZZjcRefCoinsViewHolder(View itemView) {

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

        //setting all the info from collection add to cardview;some will be hidden and passed on to expanded coin view or other activities


        // Recycler view reuses inflaed layouts; so when you click on a card and scroll down that layout is reused and if you clicked it the card 6 or 7 places down will be inflated
        // So First had to put the set layouts function in the populate view holder method - this way this method gets called for each coins card; then here reset the layouts to gone
        // that way any inflated cards get snapped back before they are shown
        public void setLayouts(){

            LinearLayout loutRefCoinObvDescX = (LinearLayout) mView.findViewById(R.id.loutRefCoinObvDesc);  // layouts that toggle from GONE to INVISIBLE to expand card
            LinearLayout loutRefCoinObvLegX = (LinearLayout) mView.findViewById(R.id.loutRefCoinObvLeg);
            LinearLayout loutRefCoinRevLegX = (LinearLayout) mView.findViewById(R.id.loutRefCoinRevLeg);
            LinearLayout loutRefCoinProvenanceX = (LinearLayout) mView.findViewById(R.id.loutRefCoinProvenance);
            LinearLayout loutRefCoinNotesX = (LinearLayout) mView.findViewById(R.id.loutRefCoinNotes);

            TextView txtRefCardReplicateBtnX = (TextView) mView.findViewById(R.id.txtRefCardReplicateBtn);

            LinearLayout loutDupAndRepX = (LinearLayout) mView.findViewById(R.id.loutDupAndRep);

            loutRefCoinObvDescX.setVisibility(View.GONE);
            loutRefCoinObvLegX.setVisibility(View.GONE);
            loutRefCoinRevLegX.setVisibility(View.GONE);
            loutRefCoinProvenanceX.setVisibility(View.GONE);
            loutRefCoinNotesX.setVisibility(View.GONE);

            txtRefCardReplicateBtnX.setVisibility(View.GONE);

            loutDupAndRepX.setVisibility(View.GONE);



        }

        // getting rid of RIC label only if both RIC and RIC var empty so something like Unlisted or Ves281 still get RIC in front
        public void setRICLabel (int id, String ricvar){

            TextView txtRefLabelRICX = (TextView)mView.findViewById(R.id.txtRefLabelRIC);
            txtRefLabelRICX.setVisibility(View.VISIBLE);

            if (id == 0 && ricvar.equals("")) {
                txtRefLabelRICX.setVisibility(View.GONE);

            }

        }

        public void setPersonage(String personage){

            TextView txtRefCardPersonageX = (TextView)mView.findViewById(R.id.txtRefCardPersonage);
            txtRefCardPersonageX.setText(personage);

        }

        public void setDenomination(String denomination){

            TextView txtRefCardDenominationX = (TextView)mView.findViewById(R.id.txtRefCardDenomination);
            txtRefCardDenominationX.setText(denomination);

        }

        public void setImage(Context ctx, final String imageLink){

            ImageView imgRefCardCoinAddX = (ImageView) mView.findViewById(R.id.imgRefCardCoinAdd);
            Picasso.get().load(imageLink).into(imgRefCardCoinAddX); //tutorial had with which got renamed to get but with took ctx as parameter...

            TextView txtRefCardImgLinkX = (TextView) mView.findViewById(R.id.txtRefCardImgLink);
            txtRefCardImgLinkX.setText(imageLink); // This will provide the image link to card view so it can be passed to duplicate screen for base coin

            imgRefCardCoinAddX.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(itemView.getContext(), RefCoinMagnify.class);

                    intent.putExtra("imagelink", imageLink);

                    itemView.getContext().startActivity(intent);
                }
            });

        }

        public void setRicvar(String ricvar) {
            TextView txtRefCardRICvarX = (TextView)mView.findViewById(R.id.txtRefCardRICvar);
            txtRefCardRICvarX.setText(ricvar);

        }

        public void setId(int id) {

            TextView txtRefCardRICX = (TextView)mView.findViewById(R.id.txtRefCardRIC);
            String id2 = String.valueOf(id);
            txtRefCardRICX.setText(id2);

            txtRefCardRICX.setVisibility(View.VISIBLE);

            try{ // wierd null poing exception on swipe delete only and only diameter but doing try catch for all ifs
                if (id == 0){


                    txtRefCardRICX.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        public void setCoinuid(String coinuid) {
            TextView txtRefCardCoinUidX = (TextView)mView.findViewById(R.id.txtRefCardCoinUid);
            txtRefCardCoinUidX.setText(coinuid);
        }

        public void setImageLink(String imageLink) {
            TextView txtRefCardImgLinkX = (TextView)mView.findViewById(R.id.txtRefCardImgLink);
            txtRefCardImgLinkX.setText(imageLink);
        }

        public void setWeight(String weight) {
            TextView txtRefCardWeightX = (TextView)mView.findViewById(R.id.txtRefCardWeight);
            txtRefCardWeightX.setText(weight);
            TextView txtRefLabelWeightX = (TextView)mView.findViewById(R.id.txtRefLabelWeight);
            txtRefLabelWeightX.setVisibility(View.VISIBLE);

            try{ // wierd null poing exception on swipe delete only and only diameter but doing try catch for all ifs
                if (weight.isEmpty()) {

                    txtRefLabelWeightX.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public void setDiameter(String diameter) {


            TextView txtRefLabelDiameterX = (TextView)mView.findViewById(R.id.txtRefLabelDiameter);
            txtRefLabelDiameterX.setVisibility(View.VISIBLE);

            try{ // wierd null poing exception on swipe delete only and only diameter but doing try catch for all ifs
                if (diameter.isEmpty()) {

                    txtRefLabelDiameterX.setVisibility(View.GONE);
                    TextView txtRefCardDiameterX = (TextView)mView.findViewById(R.id.txtRefCardDiameter);
                    txtRefCardDiameterX.setText("");
                } else {

                    TextView txtRefCardDiameterX = (TextView)mView.findViewById(R.id.txtRefCardDiameter);
                    float floatdiameter = Float.parseFloat(diameter);
                    int intdiameter = Math.round (floatdiameter);
                    txtRefCardDiameterX.setText(String.valueOf(intdiameter));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void setMint(String mint) {
            TextView txtRefCardMintX = (TextView)mView.findViewById(R.id.txtRefCardMint);
            txtRefCardMintX.setText(mint);
        }

        public void setObvDesc(String obvdesc) {


            TextView txtRefObvDescX = (TextView) mView.findViewById(R.id.txtRefObvDesc);
            txtRefObvDescX.setText(obvdesc);

            try {

               if (obvdesc.equals("BREAKER")) {

                    ImageView imgRefCardCoinAddX = mView.findViewById(R.id.imgRefCardCoinAdd);
                    imgRefCardCoinAddX.setVisibility(View.GONE);

                    LinearLayout loutRefCoinFirstLineX = mView.findViewById(R.id.loutRefCoinFirstLine);
                    loutRefCoinFirstLineX.setVisibility(View.GONE);

                    LinearLayout loutRefCoinSecondLineX = mView.findViewById(R.id.loutRefCoinSecondLine);
                    loutRefCoinSecondLineX.setVisibility(View.GONE);

                    LinearLayout loutRefCoinRevDescX = mView.findViewById(R.id.loutRefCoinMain);
                    loutRefCoinRevDescX.setBackgroundColor(Color.parseColor("#D81B60"));

                    TextView txtRefRevDescX = mView.findViewById(R.id.txtRefRevDesc);
                    txtRefRevDescX.setTextColor(Color.parseColor("#FFFFFF"));
                    txtRefRevDescX.setTextSize(20);
                    txtRefRevDescX.setGravity(Gravity.CENTER_HORIZONTAL);
                    txtRefRevDescX.setTypeface(null, Typeface.BOLD_ITALIC);

                } else {

                   ImageView imgRefCardCoinAddX = mView.findViewById(R.id.imgRefCardCoinAdd);
                   imgRefCardCoinAddX.setVisibility(View.VISIBLE);

                   LinearLayout loutRefCoinFirstLineX = mView.findViewById(R.id.loutRefCoinFirstLine);
                   loutRefCoinFirstLineX.setVisibility(View.VISIBLE);

                   LinearLayout loutRefCoinSecondLineX = mView.findViewById(R.id.loutRefCoinSecondLine);
                   loutRefCoinSecondLineX.setVisibility(View.VISIBLE);

                   LinearLayout loutRefCoinRevDescX = mView.findViewById(R.id.loutRefCoinMain);
                   loutRefCoinRevDescX.setBackgroundColor(Color.parseColor("#FFFFFF"));

                   TextView txtRefRevDescX = mView.findViewById(R.id.txtRefRevDesc);
                   txtRefRevDescX.setTextColor(Color.parseColor("#FC0E0D0D"));
                   txtRefRevDescX.setTextSize(15);
                   txtRefRevDescX.setGravity(Gravity.LEFT);
                   txtRefRevDescX.setTypeface(null, Typeface.NORMAL);

               }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public void setObvLeg(String obvleg) {
            TextView txtRefObvLegX = (TextView)mView.findViewById(R.id.txtRefObvLeg);
            txtRefObvLegX.setText(obvleg);
        }

        public void setRevDesc(String revdesc) {
            TextView txtRefRevDescX = (TextView)mView.findViewById(R.id.txtRefRevDesc);
            txtRefRevDescX.setText(revdesc);
        }

        public void setRevLeg(String revleg) {
            TextView txtRefRevLegX = (TextView)mView.findViewById(R.id.txtRefRevLeg);
            txtRefRevLegX.setText(revleg);
        }

        public void setProvenance(String provenance) {
            TextView txtRefProvenanceX = (TextView)mView.findViewById(R.id.txtRefProvenance);
            txtRefProvenanceX.setText(provenance);
        }
        public void setValue(int value) {
            TextView txtRefValueX = (TextView)mView.findViewById(R.id.txtRefValue);
           // String value2 = String.valueOf(value);
            String value2 = NumberFormat.getNumberInstance(Locale.US).format(value);
            txtRefValueX.setText(value2);

            //setting up an unformatted value so when the view is picked up later as int it doesn't crap out
            TextView txtRefCardValueUnformattedX = (TextView)mView.findViewById(R.id.txtRefCardValueUnformatted);
            String value2uf = String.valueOf(value);
            txtRefCardValueUnformattedX.setText(value2uf);

            TextView txtRefLabelValueX = (TextView)mView.findViewById(R.id.txtRefLabelValue);
            txtRefLabelValueX.setVisibility(View.VISIBLE);

            try{ // wierd null poing exception on swipe delete only and only diameter but doing try catch for all ifs
                if (value == 0){

                    txtRefLabelValueX.setVisibility(View.GONE);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public void setNotes(String notes) {
            TextView txtRefNotesX = (TextView)mView.findViewById(R.id.txtRefNotes);
            txtRefNotesX.setText(notes);

        }

        public void setSortric(int sortric) {

            TextView txtRefCardSortRicX = (TextView)mView.findViewById(R.id.txtRefCardSortric);
            String sortric2 = String.valueOf(sortric);
            txtRefCardSortRicX.setText(sortric2);



        }

        // Custom built onItemClickListener for the recycler view; seems to cover LongClick as well
        ////////////////////////////////////////////////////////////////////////////////////////
        //Listen to the video as this is a bit confusing

        private ZZZjcRefCoinsViewHolder.OnItemClickListener hpListener;

        public interface OnItemClickListener {

            void onItemClick(View view, int position);
            void onItemLongClick (View view, int position);
        }

        public void setOnItemClickListener(ZZZjcRefCoinsViewHolder.OnItemClickListener listener) {

            hpListener = listener;
        }

        ///////////////////////////////////////////////////////////////////////////////////////

    }

    //////////////////////// END -------->>> RECYCLER VIEW COMPONENTS /////////////////////////////////////////////////////
    /////////////////// includes: viewholder, sort, expoloding card view dialog, functions from dialog //////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////// START ----->>> POP-UP ////////////////////////////////////////////////////////////////////

    /////////////////// Start-New Pop up Version //////////////////////////////////////////////////////////////////////

    @SuppressLint("RestrictedApi") // suppresses the issue with not being able to use visibility with the FAB
    private void showNewFABbasedMenuRefCoins() {

        popupMenuToggle = "pressed";

        fbtnPopUpMenuRefCoinListX.setVisibility(View.GONE);
        fbtnPopUp2RefCoinsX.setVisibility(View.VISIBLE);

        fbtnMiniMyCollectionsRefCoinsX.setVisibility(View.VISIBLE);
        fbtnMiniFAQRefCoinsX.setVisibility(View.VISIBLE);
        fbtnMiniLogoutRefCoinsX.setVisibility(View.VISIBLE);

        txtRefCoinsButtonRefCoinsX.setVisibility(View.VISIBLE);
        txtFAQButtonRefCoinsX.setVisibility(View.VISIBLE);
        txtLogoutButtonRefCoinsX.setVisibility(View.VISIBLE);

        shadeX.setVisibility(View.VISIBLE);

        fbtnPopUp2RefCoinsX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fbtnPopUpMenuRefCoinListX.setVisibility(View.VISIBLE);
                fbtnPopUp2RefCoinsX.setVisibility(View.GONE);

                fbtnMiniMyCollectionsRefCoinsX.setVisibility(View.GONE);
                fbtnMiniFAQRefCoinsX.setVisibility(View.GONE);
                fbtnMiniLogoutRefCoinsX.setVisibility(View.GONE);

                txtRefCoinsButtonRefCoinsX.setVisibility(View.GONE);
                txtFAQButtonRefCoinsX.setVisibility(View.GONE);
                txtLogoutButtonRefCoinsX.setVisibility(View.GONE);

                shadeX.setVisibility(View.GONE);
                popupMenuToggle = "Not";

            }
        });

        fbtnMiniMyCollectionsRefCoinsX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RefCoinList.this, HomePage.class);
                startActivity(intent);
                popupMenuToggle = "Not";
            }
        });

        fbtnMiniFAQRefCoinsX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fbtnPopUpMenuRefCoinListX.setVisibility(View.VISIBLE);
                fbtnPopUp2RefCoinsX.setVisibility(View.GONE);

                fbtnMiniMyCollectionsRefCoinsX.setVisibility(View.GONE);
                fbtnMiniFAQRefCoinsX.setVisibility(View.GONE);
                fbtnMiniLogoutRefCoinsX.setVisibility(View.GONE);

                txtRefCoinsButtonRefCoinsX.setVisibility(View.GONE);
                txtFAQButtonRefCoinsX.setVisibility(View.GONE);
                txtLogoutButtonRefCoinsX.setVisibility(View.GONE);

                shadeX.setVisibility(View.GONE);
                popupMenuToggle = "Not";

                faqDialogView();

            }
        });

        fbtnMiniLogoutRefCoinsX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fbtnPopUpMenuRefCoinListX.setVisibility(View.VISIBLE);
                fbtnPopUp2RefCoinsX.setVisibility(View.GONE);

                fbtnMiniMyCollectionsRefCoinsX.setVisibility(View.GONE);
                fbtnMiniFAQRefCoinsX.setVisibility(View.GONE);
                fbtnMiniLogoutRefCoinsX.setVisibility(View.GONE);

                txtRefCoinsButtonRefCoinsX.setVisibility(View.GONE);
                txtFAQButtonRefCoinsX.setVisibility(View.GONE);
                txtLogoutButtonRefCoinsX.setVisibility(View.GONE);

                shadeX.setVisibility(View.GONE);
                popupMenuToggle = "Not";

                //Confirm the user wants to logout and execute
                alertDialogLogOut();

            }
        });

    }

    /////////////////// END-New Pop up Version //////////////////////////////////////////////////////////////////////

    /////////////////// Start-OLD Pop up Version //////////////////////////////////////////////////////////////////////


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

                        Intent intent = new Intent(RefCoinList.this, HomePage.class);
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

        snackbar = Snackbar.make(loutRefCoinListActLOX, "Good bye", Snackbar.LENGTH_SHORT);

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

        // taking this out for now as it's overkill and if we want to bring it back need to actually develop one for refcoin list

        //Everything in this method is code for a custom dialog
//        LayoutInflater inflater = LayoutInflater.from(this);
//        View view = inflater.inflate(R.layout.zzzz_otinfo_coinlist, null);
//
//        dialog = new AlertDialog.Builder(this)
//                .setView(view)
//                .create();
//
//        dialog.show();
//
//        Button btnOKoneTimeCLX = view.findViewById(R.id.btnOKoneTimeCL);
//        btnOKoneTimeCLX.setOnClickListener(new View.OnClickListener() {
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

                firebaseAuthRefCoins.signOut();
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
                Intent intent = new Intent(RefCoinList.this, Login.class);
                startActivity(intent);
                finish();
            }
        }.start();

    }

    @Override
    @SuppressLint("RestrictedApi") // suppresses the issue with not being able to use visibility with the FAB
    public void onBackPressed() {

        if (popupMenuToggle.equals("pressed")) {

            fbtnPopUpMenuRefCoinListX.setVisibility(View.VISIBLE);
            fbtnPopUp2RefCoinsX.setVisibility(View.GONE);

            fbtnMiniMyCollectionsRefCoinsX.setVisibility(View.GONE);
            fbtnMiniFAQRefCoinsX.setVisibility(View.GONE);
            fbtnMiniLogoutRefCoinsX.setVisibility(View.GONE);

            txtRefCoinsButtonRefCoinsX.setVisibility(View.GONE);
            txtFAQButtonRefCoinsX.setVisibility(View.GONE);
            txtLogoutButtonRefCoinsX.setVisibility(View.GONE);

            shadeX.setVisibility(View.GONE);
            popupMenuToggle = "Not";


        }else {

            Intent intent = new Intent(RefCoinList.this, RefCollections.class);
            intent.putExtra("era", era);
            startActivity(intent);
            finish();

        }

    }
}
