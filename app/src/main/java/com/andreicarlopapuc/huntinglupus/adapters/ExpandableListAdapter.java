package com.andreicarlopapuc.huntinglupus.adapters;

import android.widget.BaseExpandableListAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andreicarlopapuc.huntinglupus.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrei on 7/24/2015.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context ctx;
    private HashMap<String, List<String>> productCollection;
    private List<String> products;

    public ExpandableListAdapter(Context ctx, List<String> products,
                                 HashMap<String, List<String>> productCollection) {
        this.ctx = ctx;
        this.productCollection = productCollection;
        this.products = products;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return productCollection.get(products.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        //final String products = (String) getChild(groupPosition, childPosition);
        //LayoutInflater inflater = context.getLayoutInflater();
        LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        final String products = (String)getChild(groupPosition, childPosition);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_item,null);
        }

        TextView item = (TextView) convertView.findViewById(R.id.product);
        item.setText(products);


        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return productCollection.get(products.get(groupPosition)).size();
    }

    public Object getGroup(int arg0) {
        return products.get(arg0);
    }

    public int getGroupCount() {
        return products.size();
    }

    public long getGroupId(int arg0) {
        return arg0;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String group_title = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_item, parent, false);
        }
        TextView item = (TextView) convertView.findViewById(R.id.product);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(group_title);
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int ar0, int arg1) {
        return true;
    }

}
