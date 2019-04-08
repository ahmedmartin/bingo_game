package com.ahmed.martin.bingo_game;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.renderscript.Sampler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class game extends AppCompatActivity {

    String my_name;
    String uid;
    TextView tex [][] = new TextView[5][5];
    TextView player_name;
    TextView B,I,N,g,O;
    TextView count;
    long counter ;

    DatabaseReference ref;
    DatabaseReference last_ref;
    DatabaseReference state_ref;
    DatabaseReference go_ref;
    DatabaseReference next_ref;
    DatabaseReference names_ref;

    ValueEventListener next_listener;
    ValueEventListener last_play_listen;
    ValueEventListener names_listener;

    ArrayList <String> players_name=new ArrayList<>();

    CountDownTimer counter_time;
    CountDownTimer wait_counter;
    InterstitialAd mInterstitialAd;
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);




        player_name = findViewById(R.id.player_name);
        B=findViewById(R.id.b);
        I=findViewById(R.id.i);
        N=findViewById(R.id.n);
        g=findViewById(R.id.g);
        O=findViewById(R.id.o);

        count=findViewById(R.id.count);

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
        tex[2][3]=findViewById(R.id.tex34);
        tex[2][4]=findViewById(R.id.tex35);
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

        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();

        for(int i=0;i<5;i++)
            for(int j=0;j<5;j++)
                tex[i][j].setText(resault.play_numbers[i][j]);




        wait_counter = new CountDownTimer(100*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                count.setText(millisUntilFinished/1000+"");
            }

            @Override
            public void onFinish() {

                if(players_name.size()==1){
                    AlertDialog.Builder alart = new AlertDialog.Builder(game.this);
                    alart.setMessage("No Player Enter The Game Do You Need Wait Or Leave");
                    alart.setPositiveButton("Leave", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            leave_game();
                        }
                    });
                    alart.setNegativeButton("ReTry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            wait_counter.start();
                        }
                    });
                    alart.show();
                }else
                    state_ref.setValue("play");
            }
        };
        wait_counter.start();
    }

    @Override
    protected void onStart() {
        super.onStart();

        ref= FirebaseDatabase.getInstance().getReference().child(resault.gam_id);
        ref.child("last play").setValue("");

        wait_counter.start();

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9940348601431996/4916201442");// realy code
       // mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");// test code
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        names_listener=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                players_name.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    players_name.add(data.getValue().toString());
                }
                /*if(numbers==players_name.size()){
                    state_ref.setValue("play");
                }*/
                if(resault.players_name.size()==players_name.size()){
                    state_ref.setValue("play");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        names_ref = ref.child("who_play");
        names_ref.addValueEventListener(names_listener);
       last_play_listen= new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 if(dataSnapshot.exists()){
                     for(int i=0;i<5;i++)
                         for(int j=0;j<5;j++)
                             if(resault.play_numbers[i][j].equals(dataSnapshot.getValue().toString())){
                                     tex[i][j].setBackgroundResource(R.drawable.selected);
                                     resault.play_numbers[i][j]="0";
                                     check_for_finish();
                                     break;
                             }
                 }
            }
            public void onCancelled(DatabaseError databaseError) {}
        };

        last_ref=ref.child("last play");
        last_ref.addValueEventListener(last_play_listen);

        final DatabaseReference name =ref.child("players").child(uid);
        name.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                    my_name=dataSnapshot.getValue().toString();
                name.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        state_ref=ref.child("state");
        state_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue().toString().equals("play")) {
                    next_ref.setValue(players_name.get(0));
                    wait_counter.cancel();
                    state_ref.removeEventListener(this);
                    // counter start when play start
                    counter_time = new CountDownTimer(60*1000,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            counter=millisUntilFinished;
                            update();
                        }

                        @Override
                        public void onFinish() {
                            count.setText("");
                            int index =players_name.indexOf(player_name.getText().toString());
                            if(index == players_name.size()-1)
                                index=0;
                            else
                                index++;
                            next_ref.setValue(players_name.get(index));
                        }
                    };
                    counter_time.start();
                }else
                    next_ref.setValue("wait other players");
            }

            public void onCancelled(DatabaseError databaseError) {

            }
        });

        go_ref=ref.child("go_out");
        go_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.getValue().toString().equals("win")){
                        startActivity(new Intent(game.this,resault.class));
                        finish();
                        names_ref.removeValue();
                        state_ref.setValue("join");
                        go_ref.removeEventListener(this);
                    }
                }
            }

            public void onCancelled(DatabaseError databaseError) {

            }
        });



        next_listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    player_name.setText(dataSnapshot.getValue().toString());
                    if(counter_time!=null) {
                        counter_time.cancel();
                        restart();
                        counter_time.start();
                    }
                }
            }

            public void onCancelled(DatabaseError databaseError) {}
        };
        next_ref=ref.child("next player");
        next_ref.addValueEventListener(next_listener);


         /*  DatabaseReference num_ref = ref.child("players_number");
           num_ref.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                   if (dataSnapshot.exists()){
                       numbers=Integer.parseInt(dataSnapshot.getValue().toString());
                       if(numbers==players_name.size()) {
                           state_ref.setValue("play");
                       }
                   }
               }

               @Override
               public void onCancelled(DatabaseError databaseError) {

               }
           });*/



    }
    int numbers = 0;

    void restart(){
        counter=60*1000;
        update();
    }
    void update(){
        count.setText(counter/1000+"");
    }

    @Override
    protected void onStop() {
        super.onStop();
        next_ref.removeEventListener(next_listener);
        last_ref.removeEventListener(last_play_listen);
        names_ref.removeEventListener(names_listener);
        if(counter_time!=null) {
            counter_time.cancel();
            counter_time = null;
        }
        if (wait_counter!=null)
            wait_counter.cancel();
    }

    private void check_for_finish() {
        int count = 0;
        for(int i=0;i<5;i++) {
            boolean b=true;
            for (int j = 0; j < 5; j++)
                if(! resault.play_numbers[i][j].equals("0")) {
                    b = false;
                    break;
                }
            if(b) {
                count++;
                put_sighn(count);
            }
        }

        for(int i=0;i<5;i++){
            boolean b =true;
            for(int j=0;j<5;j++)
                if(! resault.play_numbers[j][i].equals("0")){
                    b=false;
                    break;
                }
             if(b) {
                 count++;
                 put_sighn(count);
             }

        }


        boolean lcross =true;
        boolean rcross=true;
        for(int i=0,j=4;i<5;i++,j--){
            if(! resault.play_numbers[i][j].equals("0")) {
                lcross = false;
            }
            if(! resault.play_numbers[i][i].equals("0")){
                rcross=false;
            }
        }
        if(lcross) {
            count++;
            put_sighn(count);
        }
        if(rcross) {
            count++;
            put_sighn(count);
        }

        if(count>=5){
           final DatabaseReference r= ref.child("results").child(uid);
           r.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                   if(dataSnapshot.exists()){
                       int result =Integer.parseInt(dataSnapshot.getValue().toString());
                       r.setValue(++result);
                       ref.child("go_out").setValue("win");
                      // ref.child("players_number").setValue("0");
                       r.removeEventListener(this);
                   }
               }

               public void onCancelled(DatabaseError databaseError) {}
           });

        }

    }

    void put_sighn(int count){
        switch (count){
            case 1: {B.setBackgroundResource(R.drawable.cross);
                     break;}
            case 2: {I.setBackgroundResource(R.drawable.cross);
                    break;}
            case 3: { N.setBackgroundResource(R.drawable.cross);
                     break; }
            case 4: {g.setBackgroundResource(R.drawable.cross);
                     break;}
            case 5: {O.setBackgroundResource(R.drawable.cross);
                     break;}
        }
    }

    public void tex11(View view) {
         pressed(0,0);
    }

    public void tex12(View view) {
        pressed(0,1);
    }

    public void tex13(View view) {
        pressed(0,2);
    }

    public void tex14(View view) {
        pressed(0,3);
    }

    public void tex15(View view) {
        pressed(0,4);
    }

    public void tex21(View view) {
        pressed(1,0);
    }

    public void tex22(View view) {
        pressed(1,1);
    }

    public void tex23(View view) {
        pressed(1,2);
    }

    public void tex24(View view) {
        pressed(1,3);
    }

    public void tex25(View view) {
        pressed(1,4);
    }

    public void tex31(View view) {
        pressed(2,0);
    }

    public void tex32(View view) {
        pressed(2,1);
    }

    public void tex33(View view) {
        pressed(2,2);
    }

    public void tex34(View view) {
        pressed(2,3);
    }

    public void tex35(View view) {
        pressed(2,4);
    }

    public void tex41(View view) {
        pressed(3,0);
    }

    public void tex42(View view) {
        pressed(3,1);
    }

    public void tex43(View view) {
        pressed(3,2);
    }

    public void tex44(View view) {
        pressed(3,3);
    }

    public void tex45(View view) {
        pressed(3,4);
    }

    public void tex51(View view) {
        pressed(4,0);
    }

    public void tex52(View view) {
        pressed(4,1);
    }

    public void tex53(View view) {
        pressed(4,2);
    }

    public void tex54(View view) {
        pressed(4,3);
    }

    public void tex55(View view) {
        pressed(4,4);
    }

    void pressed(int i , int j){
        if(player_name.getText().toString().equals(my_name)){
            if(resault.play_numbers[i][j].equals("0")){
                Toast.makeText(this, "Item Selected Try Another One", Toast.LENGTH_LONG).show();
            }else {
                player_name.setText("Wait");
               // counter_time.cancel();
                ref.child("last play").setValue(resault.play_numbers[i][j]);
                player_name.setText("wait");
                int index =players_name.indexOf(my_name);
                if(index == players_name.size()-1)
                    index=0;
                else
                    index++;
                ref.child("next player").setValue(players_name.get(index));
            }
        }else
            Toast.makeText(this, "Wait Your Order", Toast.LENGTH_SHORT).show();
    }

    public void leave(View view) {
           leave_game();
    }

    void leave_game (){



            if(player_name.getText().toString().equals(my_name)||player_name.getText().toString().equals("wait")
                    ||player_name.getText().toString().equals("wait other players")){
                names_ref.child(uid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if(players_name.size()>1) {
                            int index = players_name.indexOf(my_name);
                            if (index == players_name.size() - 1)
                                index = 0;
                            else
                                index++;
                            next_ref.setValue(players_name.get(index));
                        }else
                            state_ref.setValue("join");

                        Intent in=new Intent(game.this,resault.class);
                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        finish();
                        startActivity(in);
                    }
                });

            }


    }

    @Override
    public void onBackPressed() {

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
                AlertDialog.Builder alart = new AlertDialog.Builder(game.this);
                alart.setMessage("Do You Wanna Leave The Game ?!");
                alart.setTitle("LEAVE GAME");
                alart.setPositiveButton("Leave", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        leave_game();
                    }
                });
                alart.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alart.show();
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.

            }
        });

        if (mInterstitialAd.isLoaded())
            mInterstitialAd.show();
        else {
            AlertDialog.Builder alart = new AlertDialog.Builder(this);
            alart.setMessage("Do You Wanna Leave The Game ?!");
            alart.setTitle("LEAVE GAME");
            alart.setPositiveButton("Leave", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                        leave_game();

                }
            });
            alart.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alart.show();
        }
    }


}
