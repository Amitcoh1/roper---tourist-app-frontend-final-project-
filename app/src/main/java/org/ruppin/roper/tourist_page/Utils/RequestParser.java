package org.ruppin.roper.tourist_page.Utils;//package com.example.refael.swaptest.com.example.refael.swaptest.Utils;
//
//import com.example.refael.swaptest.model.Business;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//public class RequestParser {
//
//    private List<HashMap<String,String>> getBusiness(JSONArray jsonArray)
//    {
//        int count = jsonArray.length();
//        List<Business> businesses = new ArrayList<>();
//       Business business = null;
//
//        for(int i = 0; i<count; i++) {
//            try {
//                business = getPlace((JSONObject) jsonArray.get(i));
//                businesses.add(business);
//            }
//            catch(JSONException e)
//            {
//                e.printStackTrace();
//            }
//        }
//        return placesList;
//    }
//    public List<HashMap<String,String>> parse(String jsonData)
//    {
//        JSONArray jsonArray = null;
//        JSONObject jsonObject;
//
//        try {
//            jsonObject = new JSONObject(jsonData);
//            jsonArray = jsonObject.getJSONArray("results");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return getPlaces(jsonArray);
//    }
//}
