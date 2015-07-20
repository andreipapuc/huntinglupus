package com.andreicarlopapuc.huntinglupus;

/**
 * Created by Alex on 7/20/2015.
 */


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

    public class DataProvider {

        public static HashMap<String, List<String>> getInfo()
        {
            HashMap<String, List<String>> ThingDetails = new HashMap<String, List<String>>();

            List<String> Tools = new ArrayList<String>();
            Tools.add("Ciocan");
            Tools.add("Surubelnita");


            List<String> Furniture = new ArrayList<String>();
            Furniture.add("Scaune");
            Furniture.add("Tabureti");


            List<String> Keys= new ArrayList<String>();
            Keys.add("Intrare");
            Keys.add("Baie");

            ThingDetails.put("Tools", Tools);
            ThingDetails.put("Furniture", Furniture);
            ThingDetails.put("Keys", Keys);

            return ThingDetails;

        }

    }



