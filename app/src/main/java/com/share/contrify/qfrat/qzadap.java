package com.share.contrify.qfrat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class qzadap extends ArrayAdapter<fieldsinfo> {
    public qzadap(Context context, ArrayList<fieldsinfo> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        fieldsinfo user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview, parent, false);
        }
        // Lookup view for data population
        TextView qzname =  convertView.findViewById(R.id.qzname);
        TextView qzprz =  convertView.findViewById(R.id.totprz);
        TextView qzstdat = convertView.findViewById(R.id.startdt);
        TextView totmin = convertView.findViewById(R.id.totmn);
        TextView totques = convertView.findViewById(R.id.totques);
        TextView accbut = convertView.findViewById(R.id.accbut);
        // Populate the data into the template view using the data object
        qzname.setText(user.qname);
        qzprz.setText(user.prz);
        qzstdat.setText(user.stdt);
        totmin.setText(user.totmin);
        totques.setText(user.totques);
        accbut.setText(user.accbut);
        // Return the completed view to render on screen
        return convertView;
    }
}