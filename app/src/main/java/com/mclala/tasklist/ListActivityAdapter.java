/*
Alemed Muganlinsky
* */

package com.mclala.tasklist;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.mclala.tasklist.R;

import java.util.ArrayList;

public class ListActivityAdapter extends ArrayAdapter<ListItem> {
    //egen adapter som ärver från array adapter

    public ListActivityAdapter(Context context, ArrayList<ListItem> listItem) {
        super(context, 0, listItem);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // tar redan på listItem på positionen
        ListItem listItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_row_list, parent, false);
        }
        //kollar igenom om item är stryken
        if(listItem.isStroked()){
            TextView rowTextView = (TextView) convertView.findViewById(R.id.rowTextView);
            rowTextView.setText(listItem.getItemName());
            rowTextView.setPaintFlags(rowTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            ImageView checkBox = (ImageView)convertView.findViewById(R.id.checkBox);
            checkBox.setImageResource(R.drawable.ic_checked);
        }else{
            TextView rowTextView = (TextView) convertView.findViewById(R.id.rowTextView);
            rowTextView.setText(listItem.getItemName());
            rowTextView.setPaintFlags(rowTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            ImageView checkBox = (ImageView)convertView.findViewById(R.id.checkBox);
            checkBox.setImageResource(R.drawable.ic_unchecked);
        }

        //returnerar vyn
        return convertView;
    }
}
