package com.ahmed.martin.bingo_game;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

class Adapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> name;
    private final ArrayList<String> result;


    public Adapter(Activity context, ArrayList<String> name, ArrayList<String> result) {
        super(context, R.layout.list_show_player, result);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.name = name;
        this.result = result;
    }


    handler hand ;
    public View getView( int position, View view, final ViewGroup parent) {


        if(view==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_show_player, null);
            hand =new handler(view);
            view.setTag(hand);
        }
        hand = (handler) view.getTag();

        hand.getsize().setText(name.get(position));
        hand.getprice().setText(result.get(position));

        return view;
    }

    public class handler {

        private View v;
        private TextView size;
        private TextView price;




        public handler(View v) {
            this.v = v;
        }

        public TextView getsize() {
            if(size ==null)
                size =v.findViewById(R.id.player_name);
            return size;
        }

        public TextView getprice() {
            if(price ==null)
                price =v.findViewById(R.id.result);
            return price;
        }

        public void setsize(TextView tv) {
            this.size = tv;
        }

        public void setprice(TextView iv) {
            this.price = iv;
        }

    }
}

