package com.example.track_trigger;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

//import android.support.v4.content.ContextCompat;

public class RowAdapter extends ArrayAdapter<Commentr> {

    //Comment est la liste des models à afficher
    public RowAdapter(Context context, List<Commentr> tweets) {
        super(context, 0, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_list,parent, false);
        }

        TweetViewHolder viewHolder = (TweetViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new TweetViewHolder();
            viewHolder.pseudo = (TextView) convertView.findViewById(R.id.pseudo);
            viewHolder.text = (TextView) convertView.findViewById(R.id.text);
            viewHolder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            convertView.setTag(viewHolder);
        }

        Commentr tweet = getItem(position);

        if(tweet.getImportant().equals("y")) {
            viewHolder.text.setTextColor(Color.parseColor("#FF0000"));
        }

        if(tweet.getImportant().equals("n")) {
            viewHolder.text.setTextColor(Color.parseColor("#000000"));
        }

        viewHolder.pseudo.setText(tweet.getPseudo());
        viewHolder.text.setText(tweet.getText());
        viewHolder.avatar.setImageDrawable(new ColorDrawable(tweet.getColor()));

        return convertView;
    }

    private class TweetViewHolder{
        public TextView pseudo;
        public TextView text;
        public ImageView avatar;
    }
}

class Commentr {
    private int color;
    private String pseudo;
    private String text;
    private String important;


    public Commentr(int color, String pseudo, String text, String important) {
        this.color = color;
        this.pseudo = pseudo;
        this.text = text;
        this.important = important;
    }

    public String getPseudo() {
        return this.pseudo;
    }

    public String getText() {
        return this.text;
    }

    public int getColor() {
        return this.color;
    }

    public String getImportant() { return this.important; }
}

