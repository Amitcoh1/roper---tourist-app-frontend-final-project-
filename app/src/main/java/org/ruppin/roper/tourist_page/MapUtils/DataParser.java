package org.ruppin.roper.tourist_page.MapUtils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser
{
    private HashMap<String,String> getDuration(JSONArray googleDirectionJson)
    {
        HashMap<String,String> googleDirectionsMap = new HashMap<>();

        String duration = "";
        String distance = "";

        Log.d("json response", googleDirectionJson.toString());
        try {
            duration = googleDirectionJson.getJSONObject(0).getJSONObject("duration").getString("text");
            distance = googleDirectionJson.getJSONObject(0).getJSONObject("distance").getString("text");

            googleDirectionsMap.put("duration", duration);
            googleDirectionsMap.put("distance", distance);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return googleDirectionsMap;
    }


    private HashMap<String, String> getPlace(JSONObject googlePlaceJson)
    {
        HashMap<String,String> googlePlacesMap = new HashMap<>();
        String placeName = "-NA_";
        String vicinity = "-NA-";
        String latitude = "-NA-";
        String longitude = "-NA-";
        String reference = "-NA-";
        String icon = "-NA-";
        String isOpen = "-NA-";
        String priceLevel = "-NA-";
        String rating = "-NA-";
        String type = "-NA-";
        String userRatingTotal = "-NA-";
        String nextPage = "false";
        JSONArray tmp;



        try {
            if(!googlePlaceJson.isNull("name")){placeName = googlePlaceJson.getString("name");}
            if(!googlePlaceJson.isNull("vicinity")) {vicinity = googlePlaceJson.getString("vicinity");}
            if(!googlePlaceJson.isNull("icon")){ icon = googlePlaceJson.getString("icon");}
            if(!googlePlaceJson.isNull("geometry")){latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");}
            if(!googlePlaceJson.isNull("geometry")){longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");}
            if(!googlePlaceJson.isNull("reference")){reference =  googlePlaceJson.getString("reference");}
            if(!googlePlaceJson.isNull("opening_hours")){isOpen =  String.valueOf(googlePlaceJson.getJSONObject("opening_hours").getBoolean("open_now"));}
            if(!googlePlaceJson.isNull("price_level")){priceLevel =  String.valueOf(googlePlaceJson.getInt("price_level"));}
            if(!googlePlaceJson.isNull("rating")){rating =  String.valueOf(googlePlaceJson.getDouble("rating"));}
            if(!googlePlaceJson.isNull("user_ratings_total")){userRatingTotal = String.valueOf(googlePlaceJson.getInt("user_ratings_total"));}
            if(!googlePlaceJson.isNull("types")) {
                tmp = googlePlaceJson.getJSONArray("types");
                for (int i = 0; i < tmp.length(); i++) {
                    if (type == null) {
                        type = tmp.get(i).toString();
                    } else {
                        type = type + "," + tmp.get(i).toString();
                    }
                }
            }
            googlePlacesMap.put("place_name", placeName);
            googlePlacesMap.put("vicinity", vicinity);
            googlePlacesMap.put("lat", latitude);
            googlePlacesMap.put("lng", longitude);
            googlePlacesMap.put("reference", reference);
            googlePlacesMap.put("icon", icon);
            googlePlacesMap.put("is_open", isOpen);
            googlePlacesMap.put("price_level", priceLevel);
            googlePlacesMap.put("rating", rating);
            googlePlacesMap.put("user_rating_total", userRatingTotal);
            googlePlacesMap.put("type", type);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return googlePlacesMap;
    }

    private List<HashMap<String,String>> getPlaces(JSONArray jsonArray)
    {
        int count = jsonArray.length();
        List<HashMap<String,String>> placesList = new ArrayList<>();
        HashMap<String,String> placeMap = null;

        for(int i = 0; i<count; i++) {
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placesList.add(placeMap);
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }
        }
        return placesList;
    }

    public List<HashMap<String,String>> parse(String jsonData)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return getPlaces(jsonArray);
    }

    public String[] parseDirections(String jsonData)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPathes(jsonArray);
    }

    public String getPath(JSONObject googlePathJson)
    {
        String polyline = "";
        try {
            polyline = googlePathJson.getJSONObject("polyline").getString("points");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return polyline;
    }

    public String[] getPathes(JSONArray googleStepsJson)
    {
        int count = googleStepsJson.length();
        String[] polylines = new String[count];
        for(int i=0; i<count; i++)
        {
            try {
                polylines[i] = getPath(googleStepsJson.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return polylines;
    }
}
