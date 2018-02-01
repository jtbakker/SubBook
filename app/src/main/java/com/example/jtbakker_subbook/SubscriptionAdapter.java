package com.example.jtbakker_subbook;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Jacob Bakker on 1/29/2018.
 */

public class SubscriptionAdapter extends BaseAdapter {
    private Context subContext;
    private LayoutInflater subInflater;
    private ArrayList<Subscription> subSource;

    public SubscriptionAdapter(Context context, ArrayList<Subscription> subs) {
        subContext = context;
        subSource = subs;
        subInflater = (LayoutInflater) subContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return subSource.size();
    }
    @Override
    public Object getItem(int index) {
        return subSource.get(index);
    }
    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            rowView = subInflater.inflate(R.layout.subscription_row, null);
        }
        // Get name and set.
        TextView text = (TextView) rowView.findViewById(R.id.subscription_name);
        Subscription subAtIndex = subSource.get(index);
        text.setText(subAtIndex.getName());

        // Get cost for subscription, convert to string, and set.
        text = (TextView) rowView.findViewById(R.id.subscription_cost);
        String costText = String.format("%.02f", subAtIndex.getCost());
        String rowCostText = subContext.getResources().getString(R.string.individual_cost, costText);
        text.setText(rowCostText);

        // Get date for subscription, convert to string, and set.
        text = (TextView) rowView.findViewById(R.id.subscription_date);
        String dateText = new SimpleDateFormat("MM-dd-yyyy").format(subAtIndex.getDate());
        String rowDateText = subContext.getResources().getString(R.string.date_started, dateText);
        text.setText(rowDateText);

        if (index % 2 == 0) {
            rowView.setBackgroundColor(Color.LTGRAY);
        } else {
            rowView.setBackgroundColor(Color.WHITE);
        }

        return rowView;
    }

}
