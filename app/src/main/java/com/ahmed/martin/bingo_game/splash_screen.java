package com.ahmed.martin.bingo_game;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

public class splash_screen extends AppCompatActivity {
    LinearLayout l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        TextView b = findViewById(R.id.b);
        b.startAnimation(AnimationUtils.loadAnimation(this,R.anim.translate1));
        TextView i = findViewById(R.id.i);
        i.startAnimation(AnimationUtils.loadAnimation(this,R.anim.translate2));
        TextView n = findViewById(R.id.n);
        n.startAnimation(AnimationUtils.loadAnimation(this,R.anim.translate3));
        TextView g = findViewById(R.id.g);
        g.startAnimation(AnimationUtils.loadAnimation(this,R.anim.translate4));
        TextView o = findViewById(R.id.o);
        o.startAnimation(AnimationUtils.loadAnimation(this,R.anim.translate5));



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                 show_word();
            }
        },5000);
    }

    void show_word(){
        l = findViewById(R.id.world_layout);
        l.startAnimation(AnimationUtils.loadAnimation(this,R.anim.alpha));
        l.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(splash_screen.this,sighn_in.class));
                finish();
            }
        },1000);
    }
}
