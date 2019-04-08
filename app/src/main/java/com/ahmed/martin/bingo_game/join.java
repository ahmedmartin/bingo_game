package com.ahmed.martin.bingo_game;

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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class join extends AppCompatActivity {

    private ArrayList <String> game_id = new ArrayList<>();
    private ArrayList <String> states = new ArrayList<>();
    private ArrayList <String> privat = new ArrayList<>();
    private ArrayList <String> game_name =new ArrayList<>();
    private CustomListAdapter adapter;
    private ListView game_list ;
    private DatabaseReference ref;
    private EditText player_name;
    private EditText password;
    private LinearLayout layout;
    private int position;

    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        player_name=findViewById(R.id.player_name);
        password=findViewById(R.id.password);
        layout=findViewById(R.id.layout);

        adapter = new CustomListAdapter(this,game_name,states,privat);
        game_list=findViewById(R.id.games_name);
        game_list.setAdapter(adapter);
        game_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int positio, long id) {
                        if(states.get(positio).equals("join")){

                             layout.setVisibility(View.VISIBLE);

                             if(TextUtils.isEmpty(privat.get(positio)))
                                 password.setVisibility(View.INVISIBLE);

                             game_list.setVisibility(View.INVISIBLE);
                             position=positio;
                        }
            }
        });


        ref = FirebaseDatabase.getInstance().getReference();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                game_id.clear();
                states.clear();
                privat.clear();
                game_name.clear();
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    game_id.add(data.getKey().toString());
                    states.add(data.child("state").getValue().toString());

                    if(data.hasChild("password")) {
                        privat.add(data.child("password").getValue().toString());
                    }else {
                        privat.add("");
                    }
                    game_name.add(data.child("game name").getValue().toString());
                    adapter.notifyDataSetChanged();
                }
                ref.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    void set_data(int position){

        String player_nam = player_name.getText().toString()+(int)(Math.random()*100);
         String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ref.child(game_id.get(position)).child("players").child(uid).setValue(player_nam);
        ref.child(game_id.get(position)).child("results").child(uid).setValue(0);



        SharedPreferences sharref =getSharedPreferences("file",0);
        SharedPreferences.Editor editor =sharref.edit();
        editor.putBoolean("in_a_game",true);
        editor.putString("game_id",game_id.get(position));
        editor.putString("player_name",player_nam);
        editor.commit();

        Intent join = new Intent(join.this,resault.class);
        join.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(join);
    }

    public void cancel(View view) {
        layout.setVisibility(View.INVISIBLE);
        game_list.setVisibility(View.VISIBLE);
    }

    public void ok(View view) {
        if(TextUtils.isEmpty(player_name.getText().toString()))
            Toast.makeText(join.this, "Enter Your Name ", Toast.LENGTH_SHORT).show();
        else {
            if(!TextUtils.isEmpty(privat.get(position))) {
                if (TextUtils.isEmpty(password.getText().toString()))
                    Toast.makeText(join.this, "Enter Password to can Play", Toast.LENGTH_SHORT).show();
                else {
                    if(password.getText().toString().equals(privat.get(position)))
                        set_data(position);
                    else
                        Toast.makeText(join.this, "Password Not Valid", Toast.LENGTH_SHORT).show();
                }
            }else
                set_data(position);
        }
    }

}
