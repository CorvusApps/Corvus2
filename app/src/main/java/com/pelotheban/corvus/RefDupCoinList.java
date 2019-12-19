package com.pelotheban.corvus;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.facebook.login.LoginManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
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
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.Locale;

public class RefDupCoinList extends AppCompatActivity {

    // UI and data components for transfering collection ID from RefCoinList through here to ShowCoin activities
    private TextView txtDupRefCListCollUIDX;
    private String cDupRefListuid;
    private int ccListDupRefSortric;


    // Firebase related
    private FirebaseAuth firebaseAuthDupRefCoins;
    private DatabaseReference coinDupRefDatabase;
    private LinearLayoutManager layoutManagerDupCoins;

    //UI Components
    private RecyclerView rcvDupRefCoinsX;

    private int cardDupRefToggle; // allows for onclick expansion and deflation of coin cards

    private Dialog dialog; //universal dialog instance variable used for most dialogs in the activity
    private CoordinatorLayout loutDupRefCoinListActLOX; //primarily used for snackbars

    private ProgressDialog pd; // universal progress dialog used in this activity


    // custom view to use as a shade behind custom dialogs
    private View shadeX;

    //creating instance variables that can be used to pass info to the coin modify screen


    private String coinDupPersonageY, coinDupDenominationY, coinDupMintY, coinDupRICvarY, coinDupWeightY, coinDupDiameterY, coinDupObvDescY, coinDupObvLegY, coinDupRevDescY
            , coinDupRevLegY, coinDupProvenanceY, coinDupNotesY, coinDupImageLinkY, coinDupUIDY, coinDupSortRicY;
    private int coinDupRICY,  coinDupValueY;


    private Query sortDupRefQueryCoins;


    // adMob

    InterstitialAd mInterstitialAdDupRefCoinList;
    int mAdvertCounterDupRefCoinList;
    private SharedPreferences sharedAdvertCounterDupRefCoinList;




    @Override
    @SuppressLint("RestrictedApi") // suppresses the issue with not being able to use visibility with the FAB
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ref_dup_coin_list);


        //adMob
        sharedAdvertCounterDupRefCoinList = getSharedPreferences("adSettingDupRefCoinList", MODE_PRIVATE);
        mAdvertCounterDupRefCoinList = sharedAdvertCounterDupRefCoinList.getInt("CounterDupRefCoinList", 0); // where if no settings


        MobileAds.initialize(this, "ca-app-pub-1744081621312112~1448123556");
        mInterstitialAdDupRefCoinList = new InterstitialAd(RefDupCoinList.this);
        mInterstitialAdDupRefCoinList.setAdUnitId(getString(R.string.test_interstitial_ad));
        //mInterstitialAdRefCoinList.setAdUnitId(getString(R.string.refcoinlist_interstitial_ad));
        //NEED TO GET ANOTHER AD MOB UNIT FOR THIS PAGE
        mInterstitialAdDupRefCoinList.loadAd(new AdRequest.Builder().build());

        //Toast.makeText(RefCoinList.this, mAdvertCounterRefCoinList + "", Toast.LENGTH_SHORT).show();

        mInterstitialAdDupRefCoinList.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.

                if (mAdvertCounterDupRefCoinList > 12) {

                    mInterstitialAdDupRefCoinList.show();
                    SharedPreferences.Editor editor = sharedAdvertCounterDupRefCoinList.edit();
                    editor.putInt("CounterDupRefCoinList", 0); // this only kicks in on next on create so need to set actual mAdvertCounter to 0 below so the add does not loop
                    editor.apply(); // saves the value
                    mAdvertCounterDupRefCoinList = 0;
                }
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                mInterstitialAdDupRefCoinList.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                mInterstitialAdDupRefCoinList.loadAd(new AdRequest.Builder().build());

            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                mInterstitialAdDupRefCoinList.loadAd(new AdRequest.Builder().build());
            }
        });


        // UI and data components for transfering collection ID from Refcoinlist through here to ShowCoin activities - as well as getting back to the right RefCollections
        txtDupRefCListCollUIDX = findViewById(R.id.txtDupRefCListCollUID);
        cDupRefListuid = getIntent().getStringExtra("coluid");
        txtDupRefCListCollUIDX.setText(cDupRefListuid);


        ccListDupRefSortric = getIntent().getIntExtra("sortric", 0);


        //setting the title to the coinlist which is the Personage and RIC number
        TextView txtDupCoinTypeX = findViewById(R.id.txtDupCoinType);
        String cListDupRefPersonage = getIntent().getStringExtra("personage");
        int cListDupRefRIC = getIntent().getIntExtra("id", 0);
        String cListDupRefRICvar = getIntent().getStringExtra("ricvar");
        txtDupCoinTypeX.setText(cListDupRefPersonage + " RIC " + cListDupRefRIC + cListDupRefRICvar);

        //setting rest of the static source coin

        TextView txtDubBaseObvDescX = findViewById(R.id.txtDupBaseObvDesc);
        TextView txtDubBaseObvLegX = findViewById(R.id.txtDupBaseObvLeg);
        TextView txtDubBaseRevDescX = findViewById(R.id.txtDupBaseRevDesc);
        TextView txtDubBaseRevLegX = findViewById(R.id.txtDupBaseRevLeg);

        ImageView imgDupBaseCoinX = findViewById(R.id.imgDupBaseCoin);

        String DubBaseObvDesc = getIntent().getStringExtra("obvdesc");
        String DubBaseObvLeg = getIntent().getStringExtra("obvleg");
        String DubBaseRevDesc = getIntent().getStringExtra("revdesc");
        String DubBaseRevLeg = getIntent().getStringExtra("revleg");

        String DubBaseImage = getIntent().getStringExtra("imageLink");

        txtDubBaseObvDescX.setText(DubBaseObvDesc);
        txtDubBaseObvLegX.setText(DubBaseObvLeg);
        txtDubBaseRevDescX.setText(DubBaseRevDesc);
        txtDubBaseRevLegX.setText(DubBaseRevLeg);

        Picasso.get().load(DubBaseImage).into(imgDupBaseCoinX); //tutorial had with which got renamed to get but with took ctx as parameter...


        // Firebase related
        firebaseAuthDupRefCoins = FirebaseAuth.getInstance();

        coinDupRefDatabase = FirebaseDatabase.getInstance().getReference().child("my_users").child("vPlrYZXdGHgRotLma4OVopIRKY02")
                .child("collections").child(cDupRefListuid).child("coins");
        coinDupRefDatabase.keepSynced(true);

        int dupSortric = ccListDupRefSortric + 1000000000;

        sortDupRefQueryCoins = coinDupRefDatabase.orderByChild("sortric").equalTo(dupSortric);
        layoutManagerDupCoins = new LinearLayoutManager(this);
        layoutManagerDupCoins.setReverseLayout(false);



        //UI Components
        rcvDupRefCoinsX = findViewById(R.id.rcvDupRefCoins);
        rcvDupRefCoinsX.setHasFixedSize(true); //Not sure this applies or why it is here
        rcvDupRefCoinsX.setLayoutManager(layoutManagerDupCoins);

        cardDupRefToggle = 0; // allows for onclick expansion and deflation of coin cards

        loutDupRefCoinListActLOX = findViewById(R.id.loutDupRefCoinListActLO); //primarily used for snackbars); //primarily used for snackbars

        // custom view to use as a shade behind custom dialogs
        shadeX = findViewById(R.id.shade);



// The Code setting out recycler view /////////////////////////////////////////////////////////////////
// The tutorial had this section of code through to setAdapter in separate on Start Method but for StaggeredGrid that seemed to cause the recycler view to be destroyed and not come back once we moved off the screen works fine here


        final FirebaseRecyclerAdapter<ZZZJcDupRefCoins, RefDupCoinList.ZZZJcDupRefCoinsViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<ZZZJcDupRefCoins, RefDupCoinList.ZZZJcDupRefCoinsViewHolder>
                (ZZZJcDupRefCoins.class,R.layout.yyy_card_dup_ref_coins, RefDupCoinList.ZZZJcDupRefCoinsViewHolder.class,sortDupRefQueryCoins) {


            @Override
            protected void populateViewHolder(RefDupCoinList.ZZZJcDupRefCoinsViewHolder viewHolder, ZZZJcDupRefCoins model, int position) {
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
            public RefDupCoinList.ZZZJcDupRefCoinsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                RefDupCoinList.ZZZJcDupRefCoinsViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);

                viewHolder.setOnItemClickListener(new RefDupCoinList.ZZZJcDupRefCoinsViewHolder.OnItemClickListener() {


                    @Override
                    public void onItemLongClick(View view, int position) {


                    }

                    // inflates card from recycler view to see fields not visible in base view
                    @Override
                    public void onItemClick(View view, int position) {  // taking away all the expandability for dups - keeping here just in case

//                        // layouts that toggle from GONE to INVISIBLE to expand card
//                        LinearLayout loutDupRefCoinObvDescX = view.findViewById(R.id.loutDupRefCoinObvDesc);
//                        LinearLayout loutDupRefCoinObvLegX = view.findViewById(R.id.loutDupRefCoinObvLeg);
//                        LinearLayout loutDupRefCoinRevLegX = view.findViewById(R.id.loutDupRefCoinRevLeg);
//                        LinearLayout loutDupRefCoinProvenanceX = view.findViewById(R.id.loutDupRefCoinProvenance);
//                        LinearLayout loutDupRefCoinNotesX = view.findViewById(R.id.loutDupRefCoinNotes);
//
//
//                        TextView txtDupRefObvDescX = view.findViewById(R.id.txtDupRefObvDesc);
//                        TextView txtDupRefObvLegX = view.findViewById(R.id.txtDupRefObvLeg);
//                        TextView txtDupRefRevLegX = view.findViewById(R.id.txtDupRefRevLeg);
//                        TextView txtDupRefProvenanceX = view.findViewById(R.id.txtDupRefProvenance);
//                        //TextView txtRefValueX = view.findViewById(R.id.txtValue); // need to use the one below which is unformatted because can't turn it integer with comas
//                        TextView txtDupRefCardValueUnformattedX = view.findViewById(R.id.txtDupRefCardValueUnformatted);
//
//                        TextView txtDupRefNotesX = view.findViewById(R.id.txtDupRefNotes);
//
//
//                        if (cardDupRefToggle != 1) {
//
//                            cardDupRefToggle = 1;
//
//
//
//
//
//                            // checking to see if there are values in the various fields before inflating them
////                            if (txtRefObvDescX.getText().toString().isEmpty()) {
////                            } else {
//                            loutDupRefCoinObvDescX.setVisibility(View.VISIBLE);
////                            }
//
//                            if(txtDupRefObvLegX.getText().toString().isEmpty()) {
//                            }else {
//                                loutDupRefCoinObvLegX.setVisibility(View.VISIBLE);
//                            }
//
//                            if (txtDupRefRevLegX.getText().toString().isEmpty()){
//                            } else {
//                                loutDupRefCoinRevLegX.setVisibility(View.VISIBLE);
//                            }
//
//                            if (txtDupRefProvenanceX.getText().toString().isEmpty() && Integer.parseInt(txtDupRefCardValueUnformattedX.getText().toString()) == 0){
//                            } else {
//                                loutDupRefCoinProvenanceX.setVisibility(View.VISIBLE);
//                            }
//
//                            if (txtDupRefNotesX.getText().toString().isEmpty()) {
//                            } else {
//                                loutDupRefCoinNotesX.setVisibility(View.VISIBLE);
//                            }
//
//                        } else if (cardDupRefToggle == 1){
//
//                            cardDupRefToggle = 0;
//
//                            loutDupRefCoinObvDescX.setVisibility(View.GONE);
//                            loutDupRefCoinObvLegX.setVisibility(View.GONE);
//                            loutDupRefCoinRevLegX.setVisibility(View.GONE);
//                            loutDupRefCoinProvenanceX.setVisibility(View.GONE);
//                            loutDupRefCoinNotesX.setVisibility(View.GONE);
//
//                        }
//
//                        //logic for popping up intestitial adds every x clicks on a reference collection
//
//                        mAdvertCounterDupRefCoinList = sharedAdvertCounterDupRefCoinList.getInt("CounterDupRefCoinList", 0); // where if no settings
//                        SharedPreferences.Editor editor = sharedAdvertCounterDupRefCoinList.edit();
//                        editor.putInt("CounterDupRefCoinList", mAdvertCounterDupRefCoinList + 1);
//                        editor.apply(); // saves the value
//                        mAdvertCounterDupRefCoinList = mAdvertCounterDupRefCoinList + 1;
//
//                        if (mAdvertCounterDupRefCoinList > 12) {
//
//                            mInterstitialAdDupRefCoinList.show();
//                            editor = sharedAdvertCounterDupRefCoinList.edit();
//                            editor.putInt("CounterDupRefCoinList", 0); // this only kicks in on next on create so need to set actual mAdvertCounter to 0 below so the add does not loop
//                            editor.apply(); // saves the value
//                            mAdvertCounterDupRefCoinList = 0;
//
//                            mInterstitialAdDupRefCoinList.setAdListener(new AdListener() {
//                                @Override
//                                public void onAdLoaded() {
//                                    // Code to be executed when an ad finishes loading.
//
//                                }
//
//                                @Override
//                                public void onAdFailedToLoad(int errorCode) {
//                                    // Code to be executed when an ad request fails.
//                                }
//
//                                @Override
//                                public void onAdOpened() {
//                                    // Code to be executed when the ad is displayed.
//                                    mInterstitialAdDupRefCoinList.loadAd(new AdRequest.Builder().build());
//                                }
//
//                                @Override
//                                public void onAdClicked() {
//                                    // Code to be executed when the user clicks on an ad.
//                                    mInterstitialAdDupRefCoinList.loadAd(new AdRequest.Builder().build());
//
//                                }
//
//                                @Override
//                                public void onAdLeftApplication() {
//                                    // Code to be executed when the user has left the app.
//                                }
//
//                                @Override
//                                public void onAdClosed() {
//                                    // Code to be executed when the interstitial ad is closed.
//                                    mInterstitialAdDupRefCoinList.loadAd(new AdRequest.Builder().build());
//                                }
//                            });
//
//
//                        }

                        ///////////////// end of ad Mob on item click ////////////////////

                    }

                });

                return viewHolder;
            }

        };

        ///////////////////////////////////////////////////////

        // The onclick methods were in the broader recycler view methods - this calls for the adapter on everything
        rcvDupRefCoinsX.setAdapter(firebaseRecyclerAdapter);
    }

    ///////////////////////// END -------> ON-CREATE ////////////////////////////////////////////////////////////////////

    //////////////////////// START ------> RECYCLER VIEW COMPONENTS /////////////////////////////////////////////////////
    /////////////////// includes: viewholder, sort, expoloding card view dialog, functions from dialog //////////////////


    // View holder for the recycler view
    public static class ZZZJcDupRefCoinsViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public ZZZJcDupRefCoinsViewHolder(View itemView) {

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

            LinearLayout loutDupRefCoinObvDescX = (LinearLayout) mView.findViewById(R.id.loutDupRefCoinObvDesc);  // layouts that toggle from GONE to INVISIBLE to expand card
            LinearLayout loutDupRefCoinObvLegX = (LinearLayout) mView.findViewById(R.id.loutDupRefCoinObvLeg);
            LinearLayout loutDupRefCoinRevLegX = (LinearLayout) mView.findViewById(R.id.loutDupRefCoinRevLeg);
            LinearLayout loutDupRefCoinProvenanceX = (LinearLayout) mView.findViewById(R.id.loutDupRefCoinProvenance);
            LinearLayout loutDupRefCoinNotesX = (LinearLayout) mView.findViewById(R.id.loutDupRefCoinNotes);


            loutDupRefCoinObvDescX.setVisibility(View.GONE);
            loutDupRefCoinObvLegX.setVisibility(View.GONE);
            loutDupRefCoinRevLegX.setVisibility(View.GONE);
           // loutDupRefCoinProvenanceX.setVisibility(View.GONE); // starting with this visible for dups
            loutDupRefCoinNotesX.setVisibility(View.GONE);
        }

        // getting rid of RIC label only if both RIC and RIC var empty so something like Unlisted or Ves281 still get RIC in front
        public void setRICLabel (int id, String ricvar){

            TextView txtDupRefLabelRICX = (TextView)mView.findViewById(R.id.txtDupRefLabelRIC);
            //txtDupRefLabelRICX.setVisibility(View.VISIBLE);

            if (id == 0 && ricvar.equals("")) {
                txtDupRefLabelRICX.setVisibility(View.GONE);

            }

        }

        public void setPersonage(String personage){

            TextView txtDupRefCardPersonageX = (TextView)mView.findViewById(R.id.txtDupRefCardPersonage);
            txtDupRefCardPersonageX.setText(personage);

        }

        public void setDenomination(String denomination){

            TextView txtDupRefCardDenominationX = (TextView)mView.findViewById(R.id.txtDupRefCardDenomination);
            txtDupRefCardDenominationX.setText(denomination);

        }

        public void setImage(Context ctx, final String imageLink){

            ImageView imgDupRefCardCoinAddX = (ImageView) mView.findViewById(R.id.imgDupRefCardCoinAdd);
            Picasso.get().load(imageLink).into(imgDupRefCardCoinAddX); //tutorial had with which got renamed to get but with took ctx as parameter...
            imgDupRefCardCoinAddX.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(itemView.getContext(), RefCoinMagnify.class);

                    intent.putExtra("imagelink", imageLink);

                    itemView.getContext().startActivity(intent);
                }
            });

        }

        public void setRicvar(String ricvar) {
            TextView txtDupRefCardRICvarX = (TextView)mView.findViewById(R.id.txtDupRefCardRICvar);
            txtDupRefCardRICvarX.setText(ricvar);

        }

        public void setId(int id) {

            TextView txtDupRefCardRICX = (TextView)mView.findViewById(R.id.txtDupRefCardRIC);
            String id2 = String.valueOf(id);
            txtDupRefCardRICX.setText(id2);

           // txtDupRefCardRICX.setVisibility(View.VISIBLE);

            try{ // wierd null poing exception on swipe delete only and only diameter but doing try catch for all ifs
                if (id == 0){

                    txtDupRefCardRICX.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        public void setCoinuid(String coinuid) {
            TextView txtDupRefCardCoinUidX = (TextView)mView.findViewById(R.id.txtDupRefCardCoinUid);
            txtDupRefCardCoinUidX.setText(coinuid);
        }

        public void setImageLink(String imageLink) {
            TextView txtDupRefCardImgLinkX = (TextView)mView.findViewById(R.id.txtDupRefCardImgLink);
            txtDupRefCardImgLinkX.setText(imageLink);
        }

        public void setWeight(String weight) {
            TextView txtDupRefCardWeightX = (TextView)mView.findViewById(R.id.txtDupRefCardWeight);
            txtDupRefCardWeightX.setText(weight);
            TextView txtDupRefLabelWeightX = (TextView)mView.findViewById(R.id.txtDupRefLabelWeight);
            txtDupRefLabelWeightX.setVisibility(View.VISIBLE);

            try{ // wierd null poing exception on swipe delete only and only diameter but doing try catch for all ifs
                if (weight.isEmpty()) {

                    txtDupRefLabelWeightX.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public void setDiameter(String diameter) {
            TextView txtDupRefCardDiameterX = (TextView)mView.findViewById(R.id.txtDupRefCardDiameter);
            txtDupRefCardDiameterX.setText(diameter);

            TextView txtDupRefLabelDiameterX = (TextView)mView.findViewById(R.id.txtDupRefLabelDiameter);
            txtDupRefLabelDiameterX.setVisibility(View.VISIBLE);

            try{ // wierd null poing exception on swipe delete only and only diameter but doing try catch for all ifs
                if (diameter.isEmpty()) {

                    txtDupRefLabelDiameterX.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void setMint(String mint) {
            TextView txtDupRefCardMintX = (TextView)mView.findViewById(R.id.txtDupRefCardMint);
            txtDupRefCardMintX.setText(mint);
        }

        public void setObvDesc(String obvdesc) {
            TextView txtDupRefObvDescX = (TextView)mView.findViewById(R.id.txtDupRefObvDesc);
            txtDupRefObvDescX.setText(obvdesc);
        }

        public void setObvLeg(String obvleg) {
            TextView txtDupRefObvLegX = (TextView)mView.findViewById(R.id.txtDupRefObvLeg);
            txtDupRefObvLegX.setText(obvleg);
        }

        public void setRevDesc(String revdesc) {
            TextView txtDupRefRevDescX = (TextView)mView.findViewById(R.id.txtDupRefRevDesc);
            txtDupRefRevDescX.setText(revdesc);
        }

        public void setRevLeg(String revleg) {
            TextView txtDupRefRevLegX = (TextView)mView.findViewById(R.id.txtDupRefRevLeg);
            txtDupRefRevLegX.setText(revleg);
        }

        public void setProvenance(String provenance) {
            TextView txtDupRefProvenanceX = (TextView)mView.findViewById(R.id.txtDupRefProvenance);
            txtDupRefProvenanceX.setText(provenance);
        }
        public void setValue(int value) {
            TextView txtDupRefValueX = (TextView)mView.findViewById(R.id.txtDupRefValue);
            // String value2 = String.valueOf(value);
            String value2 = NumberFormat.getNumberInstance(Locale.US).format(value);
            txtDupRefValueX.setText(value2);

            //setting up an unformatted value so when the view is picked up later as int it doesn't crap out
            TextView txtDupRefCardValueUnformattedX = (TextView)mView.findViewById(R.id.txtDupRefCardValueUnformatted);
            String value2uf = String.valueOf(value);
            txtDupRefCardValueUnformattedX.setText(value2uf);

            TextView txtDupRefLabelValueX = (TextView)mView.findViewById(R.id.txtDupRefLabelValue);
            txtDupRefLabelValueX.setVisibility(View.VISIBLE);

            try{ // wierd null poing exception on swipe delete only and only diameter but doing try catch for all ifs
                if (value == 0){

                    txtDupRefLabelValueX.setVisibility(View.GONE);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public void setNotes(String notes) {
            TextView txtDupRefNotesX = (TextView)mView.findViewById(R.id.txtDupRefNotes);
            txtDupRefNotesX.setText(notes);

        }

        public void setSortric(int sortric) {

            TextView txtDupRefCardSortRicX = (TextView)mView.findViewById(R.id.txtDupRefCardSortric);
            String sortric2 = String.valueOf(sortric);
            txtDupRefCardSortRicX.setText(sortric2);
        }

        // Custom built onItemClickListener for the recycler view; seems to cover LongClick as well
        ////////////////////////////////////////////////////////////////////////////////////////
        //Listen to the video as this is a bit confusing

        private RefDupCoinList.ZZZJcDupRefCoinsViewHolder.OnItemClickListener hpListener;

        public interface OnItemClickListener {

            void onItemClick(View view, int position);
            void onItemLongClick (View view, int position);
        }

        public void setOnItemClickListener(RefDupCoinList.ZZZJcDupRefCoinsViewHolder.OnItemClickListener listener) {

            hpListener = listener;
        }

        ///////////////////////////////////////////////////////////////////////////////////////

    }

    //////////////////////// END -------->>> RECYCLER VIEW COMPONENTS /////////////////////////////////////////////////////
    /////////////////// includes: viewholder, sort, expoloding card view dialog, functions from dialog //////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////// START ----->>> POP-UP ////////////////////////////////////////////////////////////////////

    /////////////////// Start-New Pop up Version //////////////////////////////////////////////////////////////////////



    /////////////////// END-New Pop up Version //////////////////////////////////////////////////////////////////////

    /////////////////// Start-OLD Pop up Version //////////////////////////////////////////////////////////////////////




    ///////////////////////// END -------> POP-UP MENU ///////////////////////////////////////////////////////////

    ///////////////////////// START ----->>> SNACKBARS ////////////////////////////////////////////////////////////




    ///////////////////////// END -------> SNACKBARS //////////////////////////////////////////////////////////////

    ///////////////////////// START ----->>> FAQ AND ONE TIME /////////////////////////////////////////////////////





}

