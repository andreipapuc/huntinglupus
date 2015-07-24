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

import java.util.List;
import java.util.Map;

/**
 * Created by Andrei on 7/24/2015.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Activity context;
    private Map<String, List<String>> productCollections;
    private List<String> products;

    public ExpandableListAdapter(Activity context, List<String> products,
                                 Map<String, List<String>> productCollections) {
        this.context = context;
        this.productCollections = productCollections;
        this.products = products;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return productCollections.get(products.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String products = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_item, null);
        }

        TextView item = (TextView) convertView.findViewById(R.id.product);


        item.setText(products);
        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return productCollections.get(products.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition) {
        return products.get(groupPosition);
    }

    public int getGroupCount() {
        return products.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String productName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_item,
                    null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.product);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(productName);
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
