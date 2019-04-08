package com.ahmed.martin.bingo_game;


import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> name;
    private final ArrayList<String> staties;
    private final ArrayList<String> statuies;


    public CustomListAdapter(Activity context, ArrayList<String> name, ArrayList<String> staties , ArrayList<String> statuies) {
        super(context, R.layout.list_show, name);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.name = name;
        this.staties = staties;
        this.statuies=statuies;
    }


    handler hand ;
    public View getView(final int position, View view, final ViewGroup parent) {


        if(view==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_show, null);
            hand =new handler(view);
            view.setTag(hand);
        }
        hand = (handler) view.getTag();


        hand.getsize().setText(name.get(position));
        hand.getprice().setText(staties.get(position));
        if(TextUtils.isEmpty(statuies.get(position))){
            hand.getstatus().setImageResource(R.drawable.open);
        }else
            hand.getstatus().setImageResource(R.drawable.close);

        return view;


    }

    public class handler {

        private View v;
        private TextView size;
        private TextView price;
        private ImageView statuse;



        public handler(View v) {
            this.v = v;
        }

        public TextView getsize() {
            if(size ==null)
                size =v.findViewById(R.id.game_name);
            return size;
        }

        public TextView getprice() {
            if(price ==null)
                price =v.findViewById(R.id.state);
            return price;
        }

        public void setsize(TextView tv) {
            this.size = tv;
        }

        public void setprice(TextView iv) {
            this.price = iv;
        }

        public ImageView getstatus(){
            if(statuse==null)
                statuse=v.findViewById(R.id.status_img);
            return statuse;
        }

        public void setRadioButton(ImageView rb){ this.statuse = rb;}

    }
}

