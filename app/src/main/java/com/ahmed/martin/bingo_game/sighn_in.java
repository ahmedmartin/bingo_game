package com.ahmed.martin.bingo_game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class sighn_in extends AppCompatActivity {

    EditText email;
    EditText password;
    InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sighn_in);

        MobileAds.initialize(this, "ca-app-pub-9940348601431996~3658002235");
        mInterstitialAd = new InterstitialAd(this);
        // mInterstitialAd.setAdUnitId("ca-app-pub-9940348601431996/4916201442");// realy code
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");// test code
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser!=null){//&&currentUser.isEmailVerified()) {
            finish();
            startActivity(new Intent(sighn_in.this,Main.class));
        }


    }

    public void sighn_up(View view) {
        startActivity(new Intent(sighn_in.this,sighn_up.class));
    }

    public void sighn_in(View view) {

        String Email = email.getText().toString();
        String Password = password.getText().toString();
        if(TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password)){
            Toast.makeText(this,"should enter all data please",Toast.LENGTH_LONG).show();
        }else {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                finish();
                                startActivity(new Intent(sighn_in.this,Main.class));

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(sighn_in.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

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
