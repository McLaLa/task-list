/*
Alemed Muganlinsky
* */

package com.mclala.tasklist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.mclala.tasklist.R;

import java.util.ArrayList;

public class MainActivityAdapter extends ArrayAdapter<List> {

    public MainActivityAdapter(Context context, ArrayList<List> list) {
        super(context, 0, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        List list = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_row_main, parent, false);
        }

        TextView rowTextView = (TextView) convertView.findViewById(R.id.rowTextView);
        rowTextView.setText(list.getListName());

        TextView rowTextView2 = (TextView) convertView.findViewById(R.id.rowTextView2);
        rowTextView2.setText("(" + list.getItemsStroked() + "/" + list.getItemsToBuy() + ")");

        if(list.isDone()){
            TextView doneView = (TextView) convertView.findViewById(R.id.doneView);
            doneView.setBackgroundColor(Color.rgb(0, 100, 0));
        }else{
            TextView doneView = (TextView) convertView.findViewById(R.id.doneView);
            doneView.setBackgroundColor(Color.rgb(200,0,0));
        }
        return convertView;
    }
}