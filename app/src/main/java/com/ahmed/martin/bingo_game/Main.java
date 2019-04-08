package com.ahmed.martin.bingo_game;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Main extends AppCompatActivity  {

    private EditText game_name;
    private EditText player_name;
    private EditText password;
    private RadioButton rd;
    private boolean checked;
    private LinearLayout layout;
    private LinearLayout but_layout;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, "ca-app-pub-9940348601431996~6061255942");
        mAdView = findViewById(R.id.adView);
        final AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                if(errorCode==3)
                    Toast.makeText(Main.this, "banner "+errorCode+" ERROR_CODE_NO_FILL", Toast.LENGTH_SHORT).show();
                if (errorCode==2)
                    Toast.makeText(Main.this, "banner "+errorCode+" ERROR_CODE_NETWORK_ERROR", Toast.LENGTH_SHORT).show();
                if (errorCode==1)
                    Toast.makeText(Main.this, "banner "+errorCode+" ERROR_CODE_INVALID_REQUEST", Toast.LENGTH_SHORT).show();
            }

        });
        game_name=findViewById(R.id.game_name);
        player_name=findViewById(R.id.player_name);
        password=findViewById(R.id.password);
        rd=findViewById(R.id.privat);
        layout=findViewById(R.id.layout);
        but_layout=findViewById(R.id.but_layout);

        SharedPreferences ref = getSharedPreferences("file", 0);
        if (ref.getBoolean("in_a_game", false)) {
            if (!TextUtils.isEmpty(ref.getString("game_id", ""))) {
                Intent t = new Intent(Main.this, resault.class);
                t.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                startActivity(t);
            }
        }


    }

    @Override
    protected void onStart() {
        super.onStart();






        final InterstitialAd mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9940348601431996/4916201442");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                mInterstitialAd.show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                mInterstitialAd.show();
                if(errorCode==3)
                Toast.makeText(Main.this, "image "+errorCode+" ERROR_CODE_NO_FILL", Toast.LENGTH_SHORT).show();
                if (errorCode==2)
                    Toast.makeText(Main.this, "image "+errorCode+" ERROR_CODE_NETWORK_ERROR", Toast.LENGTH_SHORT).show();
                if (errorCode==1)
                    Toast.makeText(Main.this, "image "+errorCode+" ERROR_CODE_INVALID_REQUEST", Toast.LENGTH_SHORT).show();
            }

        });

        /*}else {
            // save data in shared file check if user in a game
            SharedPreferences ref = getSharedPreferences("file", 0);
            if (ref.getBoolean("in_a_game", false)) {
                if (!TextUtils.isEmpty(ref.getString("game_id", ""))) {
                    Intent t = new Intent(Main.this, resault.class);
                    t.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();
                    startActivity(t);
                }
            }
        }*/

    }


    public void creat(View view) {

        layout.setVisibility(View.VISIBLE);
        but_layout.setVisibility(View.INVISIBLE);
        password.setVisibility(View.INVISIBLE);
        rd.setChecked(false);

        checked = true;

        rd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checked){
                    checked=false;
                    rd.setChecked(true);
                    password.setVisibility(View.VISIBLE);
                }else {
                    checked=true;
                    rd.setChecked(false);
                    password.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    void set_data(boolean privat){

        String player_nam = player_name.getText().toString()+(int)(Math.random()*100);
        final String uid =FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference ref =FirebaseDatabase.getInstance().getReference().push();
        ref.child("players").child(uid).setValue(player_nam); // put player name in data base
        ref.child("game name").setValue(game_name.getText().toString()); // put game name in data base
        ref.child("state").setValue("join");  //put state in data base
        ref.child("results").child(uid).setValue(0); // put players results in data bas

        if(privat)
            ref.child("password").setValue(password.getText().toString());


        //put data in a shared pref for the player
        SharedPreferences sharref =getSharedPreferences("file",0);
        SharedPreferences.Editor editor =sharref.edit();
        editor.putBoolean("in_a_game",true);
        editor.putString("game_id",ref.getKey());
        editor.putString("player_name",player_nam);
        editor.commit();

        //start result activity to begin game
        Intent in  =new Intent(Main.this,resault.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(in);
    }

    public void join(View view) {
        startActivity(new Intent(Main.this,join.class));
    }

    public void log_out(View view) {


        FirebaseAuth mauth=FirebaseAuth.getInstance();
        mauth.signOut();
        Intent sign_in=new Intent(Main.this,sighn_in.class);
        sign_in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(sign_in);
    }

    @Override
    public void onBackPressed() {
        Intent sign_in=new Intent(this,sighn_in.class);
        sign_in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
    }

    public void ok(View view) {
        if(TextUtils.isEmpty(game_name.getText().toString()))
            Toast.makeText(Main.this, "should enter game name", Toast.LENGTH_LONG).show();
        else {
            if (TextUtils.isEmpty(player_name.getText().toString()))
                Toast.makeText(Main.this, "should enter player name", Toast.LENGTH_SHORT).show();
            else {
                if (rd.isChecked()) {
                    if (TextUtils.isEmpty(password.getText().toString()))
                        Toast.makeText(Main.this, "if you wanna private game enter password", Toast.LENGTH_SHORT).show();
                    else
                        set_data(true);
                } else
                    set_data(false);
            }
        }
    }

    public void cancel(View view) {
        but_layout.setVisibility(View.VISIBLE);
        layout.setVisibility(View.INVISIBLE);
    }

    public void about_us(View view) {
        startActivity(new Intent(this,about_us.class));
    }
}
