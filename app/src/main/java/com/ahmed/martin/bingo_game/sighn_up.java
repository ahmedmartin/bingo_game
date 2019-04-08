package com.ahmed.martin.bingo_game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class sighn_up extends AppCompatActivity {

    EditText email;
    EditText password;
    InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sighn_up);

        MobileAds.initialize(this, "ca-app-pub-9940348601431996~3658002235");
        mInterstitialAd = new InterstitialAd(this);
        // mInterstitialAd.setAdUnitId("ca-app-pub-9940348601431996/4916201442");// realy code
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");// test code
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
    }

    public void sighn_up(View view) {

        String Email = email.getText().toString();
        String Password = password.getText().toString();
        if(TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password)){
            Toast.makeText(this,"should enter all data please",Toast.LENGTH_LONG).show();
        }else {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        finish();
                        startActivity(new Intent(sighn_up.this,Main.class));
                    } else {
                        Toast.makeText(sighn_up.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }

    public void sighn_in(View view) {
        finish();
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
