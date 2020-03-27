package com.share.contrify.qfrat;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class newsadap extends ArrayAdapter<newsfield> {
    public newsadap(Context context, ArrayList<newsfield> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        newsfield user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_news, parent, false);
        }
        // Lookup view for data population
        TextView ntit =  convertView.findViewById(R.id.newstit);
        // Populate the data into the template view using the data object
        //ntit.setMovementMethod(LinkMovementMethod.getInstance());
        ntit.setText(user.comb);
        // Return the completed view to render on screen
        return convertView;
    }
}
