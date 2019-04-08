package com.ahmed.martin.bingo_game;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class resault extends AppCompatActivity {

    TextView[][] tex =new TextView[5][5];
    static String play_numbers[][] = new String [5][5];

    ArrayList<String> numbers = new ArrayList<>();
    ArrayAdapter <String> adapter ;

    ListView players;
    ArrayList <String> players_score=new ArrayList<>();
    static ArrayList<String> players_name = new ArrayList<>();
    Adapter player_adapter;

    SharedPreferences sharref ;
    static String gam_id ;
    String uid;
    String player_name;



    DatabaseReference ref;
    ValueEventListener listen;

    InterstitialAd mInterstitialAd;
    AdView mAdView;
    private RewardedVideoAd mRewardedVideoAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resault);
        MobileAds.initialize(this, "ca-app-pub-9940348601431996~3658002235");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        MobileAds.initialize(this, "ca-app-pub-9940348601431996~3658002235");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9940348601431996/4916201442");// realy code
       // mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");// test code
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        sharref = getSharedPreferences("file", 0);
        gam_id = sharref.getString("game_id", "");
        player_name = sharref.getString("player_name","");
        for(int i=1;i<=25;i++){
            numbers.add(i+"");
        }
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,numbers);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        players=findViewById(R.id.players);
        player_adapter=new Adapter(this,players_name,players_score);
        players.setAdapter(player_adapter);

        fill_data();
    }

    @Override
    protected void onStart() {
        super.onStart();



        play_numbers=new String[5][5];
        ref= FirebaseDatabase.getInstance().getReference().child(gam_id);
        listen=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                players_score.clear();
                players_name.clear();

                for(DataSnapshot data : dataSnapshot.child("players").getChildren()){

                    players_name.add(data.getValue().toString());
                    String  key = data.getKey().toString();

                    if(dataSnapshot.child("results").child(key).getValue()!=null) {
                        players_score.add(dataSnapshot.child("results").child(key).getValue().toString());
                        player_adapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        ref.addValueEventListener(listen);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ref.removeEventListener(listen);
    }

    void fill_data(){
        tex[0][0]=findViewById(R.id.tex11);
        tex[0][1]=findViewById(R.id.tex12);
        tex[0][2]=findViewById(R.id.tex13);
        tex[0][3]=findViewById(R.id.tex14);
        tex[0][4]=findViewById(R.id.tex15);
        tex[1][0]=findViewById(R.id.tex21);
        tex[1][1]=findViewById(R.id.tex22);
        tex[1][2]=findViewById(R.id.tex23);
        tex[1][3]=findViewById(R.id.tex24);
        tex[1][4]=findViewById(R.id.tex25);
        tex[2][0]=findViewById(R.id.tex31);
        tex[2][1]=findViewById(R.id.tex32);
        tex[2][2]=findViewById(R.id.tex33);
        tex[2][4]=findViewById(R.id.tex35);
        tex[2][3]=findViewById(R.id.tex34);
        tex[3][0]=findViewById(R.id.tex41);
        tex[3][1]=findViewById(R.id.tex42);
        tex[3][2]=findViewById(R.id.tex43);
        tex[3][3]=findViewById(R.id.tex44);
        tex[3][4]=findViewById(R.id.tex45);
        tex[4][0]=findViewById(R.id.tex51);
        tex[4][1]=findViewById(R.id.tex52);
        tex[4][2]=findViewById(R.id.tex53);
        tex[4][3]=findViewById(R.id.tex54);
        tex[4][4]=findViewById(R.id.tex55);
    }

    public void tex11(View view) {
        if(TextUtils.isEmpty(tex[0][0].getText().toString()))
            choose_number(tex[0][0]);
        else
            remove_number(tex[0][0]);

    }


    public void tex12(View view) {
        if(TextUtils.isEmpty(tex[0][1].getText().toString()))
            choose_number(tex[0][1]);
        else
            remove_number(tex[0][1]);
    }

    public void tex13(View view) {
        if(TextUtils.isEmpty(tex[0][2].getText().toString()))
            choose_number(tex[0][2]);
        else
            remove_number(tex[0][2]);
    }

    public void tex14(View view) {
        if(TextUtils.isEmpty(tex[0][3].getText().toString()))
            choose_number(tex[0][3]);
        else
            remove_number(tex[0][3]);
    }

    public void tex15(View view) {
        if(TextUtils.isEmpty(tex[0][4].getText().toString()))
            choose_number(tex[0][4]);
        else
            remove_number(tex[0][4]);
    }

    public void tex21(View view) {
        if(TextUtils.isEmpty(tex[1][0].getText().toString()))
            choose_number(tex[1][0]);
        else
            remove_number(tex[1][0]);
    }

    public void tex22(View view) {
        if(TextUtils.isEmpty(tex[1][1].getText().toString()))
            choose_number(tex[1][1]);
        else
            remove_number(tex[1][1]);
    }

    public void tex23(View view) {
        if(TextUtils.isEmpty(tex[1][2].getText().toString()))
            choose_number(tex[1][2]);
        else
            remove_number(tex[1][2]);
    }

    public void tex24(View view) {
        if(TextUtils.isEmpty(tex[1][3].getText().toString()))
            choose_number(tex[1][3]);
        else
            remove_number(tex[1][3]);
    }

    public void tex25(View view) {

        if(TextUtils.isEmpty(tex[1][4].getText().toString()))
            choose_number(tex[1][4]);
        else
            remove_number(tex[1][4]);
    }

    public void tex31(View view) {

        if(TextUtils.isEmpty(tex[2][0].getText().toString()))
            choose_number(tex[2][0]);
        else
            remove_number(tex[2][0]);
    }

    public void tex32(View view) {

        if(TextUtils.isEmpty(tex[2][1].getText().toString()))
            choose_number(tex[2][1]);
        else
            remove_number(tex[2][1]);
    }

    public void tex33(View view) {

        if(TextUtils.isEmpty(tex[2][2].getText().toString()))
            choose_number(tex[2][2]);
        else
            remove_number(tex[2][2]);
    }

    public void tex34(View view) {

        if(TextUtils.isEmpty(tex[2][3].getText().toString()))
            choose_number(tex[2][3]);
        else
            remove_number(tex[2][3]);
    }

    public void tex35(View view) {

        if(TextUtils.isEmpty(tex[2][4].getText().toString()))
            choose_number(tex[2][4]);
        else
            remove_number(tex[2][4]);
    }

    public void tex41(View view) {

        if(TextUtils.isEmpty(tex[3][0].getText().toString()))
            choose_number(tex[3][0]);
        else
            remove_number(tex[3][0]);
    }

    public void tex42(View view) {

        if(TextUtils.isEmpty(tex[3][1].getText().toString()))
            choose_number(tex[3][1]);
        else
            remove_number(tex[3][1]);
    }

    public void tex43(View view) {

        if(TextUtils.isEmpty(tex[3][2].getText().toString()))
            choose_number(tex[3][2]);
        else
            remove_number(tex[3][2]);
    }

    public void tex44(View view) {

        if(TextUtils.isEmpty(tex[3][3].getText().toString()))
            choose_number(tex[3][3]);
        else
            remove_number(tex[3][3]);
    }

    public void tex45(View view) {

        if(TextUtils.isEmpty(tex[3][4].getText().toString()))
            choose_number(tex[3][4]);
        else
            remove_number(tex[3][4]);
    }

    public void tex51(View view) {

        if(TextUtils.isEmpty(tex[4][0].getText().toString()))
            choose_number(tex[4][0]);
        else
            remove_number(tex[4][0]);
    }

    public void tex52(View view) {

        if(TextUtils.isEmpty(tex[4][1].getText().toString()))
            choose_number(tex[4][1]);
        else
            remove_number(tex[4][1]);
    }

    public void tex53(View view) {

        if(TextUtils.isEmpty(tex[4][2].getText().toString()))
            choose_number(tex[4][2]);
        else
            remove_number(tex[4][2]);
    }

    public void tex54(View view) {

        if(TextUtils.isEmpty(tex[4][3].getText().toString()))
            choose_number(tex[4][3]);
        else
            remove_number(tex[4][3]);
    }

    public void tex55(View view) {

        if(TextUtils.isEmpty(tex[4][4].getText().toString()))
            choose_number(tex[4][4]);
        else
            remove_number(tex[4][4]);
    }

        String number = "";
    void choose_number(final TextView text){
        Spinner s = new Spinner(this);
        s.setAdapter(adapter);
        AlertDialog.Builder alart = new AlertDialog.Builder(this);
        alart.setView(s);
        alart.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                text.setText(number);
                numbers.remove(number);
                adapter.notifyDataSetChanged();
            }
        });
        alart.show();
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               number = numbers.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void remove_number(final TextView text) {
        AlertDialog.Builder alart = new AlertDialog.Builder(this);
        alart.setMessage("Do You Need Remove The Number From THis Rectangle");
        alart.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                numbers.add(text.getText().toString());
                text.setText("");
                adapter.notifyDataSetChanged();
            }
        });
        alart.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }});
        alart.show();
    }

    public void start_game(View view) {
        if(numbers.size()==0) {
            for(int i=0;i<5;i++){
                for(int j=0;j<5;j++){
                   play_numbers[i][j]=tex[i][j].getText().toString();
                }
            }
            ref.child("go_out").setValue("");
           /* final DatabaseReference r = ref.child("players_number");
            r.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        r.setValue(Integer.parseInt(dataSnapshot.getValue().toString()) + 1);
                    }else
                        r.setValue(1);

                    /*if(playr_count==players_score.size())
                        ref.child("state").setValue("play");
                    r.removeEventListener(this);
                }

                public void onCancelled(DatabaseError databaseError) {
                }
            });*/
           final DatabaseReference state =  ref.child("state");
           state.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                   if(dataSnapshot.exists()){
                       if(dataSnapshot.getValue().toString().equals("play")){
                           Toast.makeText(resault.this, "Sorry Players In Game Wait Next Level", Toast.LENGTH_SHORT).show();
                       }else {
                           DatabaseReference who_play = ref.child("who_play");
                           who_play.child(uid).setValue(player_name).addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {
                                   Intent t = new Intent(resault.this, game.class);
                                   t.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                   finish();
                                   startActivity(t);
                               }
                           });
                       }
                   }
                   state.removeEventListener(this);
               }

               @Override
               public void onCancelled(DatabaseError databaseError) {

               }
           });




        }else
            Toast.makeText(this, "Enter All Number", Toast.LENGTH_SHORT).show();
    }

    public void leave(View view) {

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        SharedPreferences.Editor editor =sharref.edit();
        editor.putBoolean("in_a_game",false);
        editor.putString("game_id","");
        editor.putString("player_name","");
        editor.commit();


        if(players_name.size()==1){
            ref.removeValue();
        } else {
            ref.child("players").child(uid).removeValue();
            ref.child("results").child(uid).removeValue();
        }

        Intent in=new Intent(resault.this,Main.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(in);

    }


    public void auto_fill(View view) {
        final ProgressDialog prog = new ProgressDialog(this);
        prog.setMessage("Wait");
        prog.show();

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.loadAd("ca-app-pub-9940348601431996/5918168211",
                new AdRequest.Builder().build());  // real
       // mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
       //         new AdRequest.Builder().build());  // test

        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                mRewardedVideoAd.show();
            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {
                prog.dismiss();
                int k=25;
                if(numbers.size()==0 || numbers.size()<25) {
                    numbers.clear();
                    for (int i = 1; i <= 25; i++) {
                        numbers.add(i + "");
                    }
                }
                for(int i=0;i<5;i++){
                    for(int j=0;j<5;j++){
                        int x = (int)((Math.random()*k));
                        tex [i][j].setText(numbers.remove(x));
                        k--;
                    }
                }
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {

            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int ix) {
              /*  prog.dismiss();
                int k=25;
                if(numbers.size()==0 || numbers.size()<25) {
                    numbers.clear();
                    for (int i = 1; i <= 25; i++) {
                        numbers.add(i + "");
                    }
                }
                for(int i=0;i<5;i++){
                    for(int j=0;j<5;j++){
                        int x = (int)((Math.random()*k));
                        tex [i][j].setText(numbers.remove(x));
                        k--;
                    }
                }*/

            }

        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }else
            finish();
    }
}
