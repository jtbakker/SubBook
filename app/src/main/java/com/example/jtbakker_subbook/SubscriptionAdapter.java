/**
 * Classname: SubscriptionAdapter
 *
 * Date: February 4, 2018
 *
 * Copyright (c) 2018 - CMPUT 301 All Rights Reserved
 */

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
import java.util.Locale;

/**
 * This adapter formats each Subscription object for display in MainActivity
 * list's rows.
 */
public class SubscriptionAdapter extends BaseAdapter {
    private Context subContext;
    private LayoutInflater subInflater;
    private ArrayList<Subscription> subSource;

    /**
     * This constructor was adapted from the "Android ListView Tutorial" by Odie Edo-Osagie
     * created on May 4, 2016.
     * Link at "https://www.raywenderlich.com/124438/android-listview-tutorial"
     * @param context
     * @param subs
     */
    public SubscriptionAdapter(Context context, ArrayList<Subscription> subs) {
        subContext = context;
        subSource = subs;
        subInflater = (LayoutInflater) subContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * This method was adapted from the "Android ListView Tutorial" by Odie Edo-Osagie created
     * on May 4, 2016.
     * Link at "https://www.raywenderlich.com/124438/android-listview-tutorial"
     *
     * Returns the View object for the Subscription at an index. Each attribute aside from
     * the comment is paired with a string denoting the attribute name followed by the
     * attribute itself (e.g. "Date created" is paired with "2017-01-01").
     *
     * @param index Index of the Subscription in both the ListView and the ArrayList in
     *              the MainActivity class.
     * @param convertView
     * @param parent
     * @return
     */
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
        String costText = subAtIndex.costToString();
        String rowCostText = subContext.getResources().getString(R.string.main_individual_cost, costText);
        text.setText(rowCostText);

        // Get date for subscription, convert to string, and set.
        text = (TextView) rowView.findViewById(R.id.subscription_date);
        String dateText = subAtIndex.dateToString();
        String rowDateText = subContext.getResources().getString(R.string.main_date_started, dateText);
        text.setText(rowDateText);

        /**
         * This code for alternating row colors was taken from an answer by Suraj Buraj on
         * Oct. 28, 2012 to a StackOverflow post.
         * Link at "https://stackoverflow.com/a/13109854"
         *
         * Row colors alternate from light gray to white based on index
         */
        if (index % 2 == 0) {
            rowView.setBackgroundColor(Color.LTGRAY);
        } else {
            rowView.setBackgroundColor(Color.WHITE);
        }
        return rowView;
    }

    // Implement methods defined in abstract class BaseAdapter.
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
}
