package com.andreicarlopapuc.huntinglupus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;


public class UserAllProductsEXPANDABLElist extends Activity {
    HashMap<String, List<String>> Thing_category;
    List<String> Thing_list;
    ExpandableListView Exp_list;
    ProductsCategoryList adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_all_products);
        Exp_list = (ExpandableListView) findViewById(R.id.exp_list);
        Thing_category = DataProvider.getInfo();
        Thing_list = new ArrayList<String>(Thing_category.keySet());
        adapter = new ProductsCategoryList(this, Thing_category, Thing_list);
        Exp_list.setAdapter(adapter);
    }
}