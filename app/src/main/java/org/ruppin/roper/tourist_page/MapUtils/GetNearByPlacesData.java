package org.ruppin.roper.tourist_page.MapUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.koushikdutta.ion.Ion;

import org.ruppin.roper.tourist_page.Models.Business;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class GetNearByPlacesData extends AsyncTask<Object, String, String>
{
    String googlePlacesData;
    GoogleMap mMap;
    String url;
    String nextPageUrl;
    List<String> businessNames;
    List<Business> businesses;
    Context context;
    String nextPageToken;

    public GetNearByPlacesData(List<String> businessNames, List<Business> businesses, Context context) {
        this.businessNames = businessNames;
        this.businesses = businesses;
        this.context = context;
    }


    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap)objects[0];
        url = (String)objects[1];
        nextPageUrl = (String)objects[2];
        DownloadUrl downloadUrl = new DownloadUrl();

        try {
            googlePlacesData = downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String,String>> nearbyPlaceList = null;
        DataParser parser = new DataParser();
        nearbyPlaceList = parser.parse(s);
        showNearbyPlaces(nearbyPlaceList);
//        if(googlePlacesData.contains("next_page_token"))
//        {
//            JSONObject jsonObject = null;
//            try {
//                jsonObject = new JSONObject(googlePlacesData);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            try {
//                nextPageToken = jsonObject.getString("next_page_token");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            nextPageUrl = nextPageUrl.replace("pagetoken=", "pagetoken=" + nextPageToken);
//            DownloadUrl downloadUrl = new DownloadUrl();
//            try {
//                googlePlacesData = downloadUrl.readUrl(nextPageUrl);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            onPostExecute(googlePlacesData);
//        }
        if(businesses != null){showNearbyBusinesses(businesses);}
    }

    private void showNearbyPlaces(List<HashMap<String,String>> nearbyPlaceList) {
        Map<String, Integer> duplicates = new HashMap<>();
        if (businesses != null) {
            for (int i = 0; i < nearbyPlaceList.size(); i++) {
                HashMap<String, String> googlePlace = nearbyPlaceList.get(i);
                if (!businessNames.contains(googlePlace.get("place_name").toLowerCase())) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    String placeName = googlePlace.get("place_name");
                    String vicinity = googlePlace.get("vicinity");
                    double lat = Double.parseDouble(googlePlace.get("lat"));
                    double lng = Double.parseDouble(googlePlace.get("lng"));
                    String icon = googlePlace.get("icon");
                    try {
                        Bitmap bmImg = Ion.with(context).load(icon).asBitmap().get();
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bmImg));
                    } catch (ExecutionException e) {
                        Log.e("Error","Cant download icon.");
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.e("Error","Cant download icon.");
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                    }

                    LatLng latLng = new LatLng(lat, lng);
                    markerOptions.position(latLng);
                    markerOptions.title(placeName + " : " + vicinity);
                    // markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));


                    mMap.addMarker(markerOptions);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                } else {
                    duplicates.put(googlePlace.get("place_name").toLowerCase(), i);
                }
            }

            for (Business b : businesses) {
                if (duplicates.containsKey(b.getName().toLowerCase())) {
                    b.setAddress(nearbyPlaceList.get(duplicates.get(b.getName().toLowerCase())).get("vicinity"));
                }
            }
        } else {
            for (int i = 0; i < nearbyPlaceList.size(); i++) {
                HashMap<String, String> googlePlace = nearbyPlaceList.get(i);
                MarkerOptions markerOptions = new MarkerOptions();
                String placeName = googlePlace.get("place_name");
                String vicinity = googlePlace.get("vicinity");
                double lat = Double.parseDouble(googlePlace.get("lat"));
                double lng = Double.parseDouble(googlePlace.get("lng"));

                LatLng latLng = new LatLng(lat, lng);
                markerOptions.position(latLng);
                markerOptions.title(placeName + " : " + vicinity);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));

                mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
            }
        }
    }
    private void showNearbyBusinesses(List<Business> nearbyPlacebusinessesList)
    {
        for(int i=0; i<nearbyPlacebusinessesList.size(); i++)
        {
            MarkerOptions markerOptions = new MarkerOptions();
            Business business = nearbyPlacebusinessesList.get(i);

            String title;
            if(business.getAddress() != null) {title = String.format("%s : %s",business.getName(), business.getAddress());}
            else {title = String.format("%s",business.getName());}
            double lat = Double.parseDouble(business.getLatitude());
            double lng = Double.parseDouble(business.getLongitude());

            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(title);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        }
    }


}
