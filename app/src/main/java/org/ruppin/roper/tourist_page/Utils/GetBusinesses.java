package org.ruppin.roper.tourist_page.Utils;//package com.example.refael.swaptest.com.example.refael.swaptest.Utils;
//
//import com.example.refael.swaptest.com.example.refael.swaptest.MapUtils.DataParser;
//import com.example.refael.swaptest.com.example.refael.swaptest.MapUtils.DownloadUrl;
//
//public class GetBusinesses
//{
//    String googlePlacesData;
//    GoogleMap mMap;
//    String url;
//
//
//    @Override
//    protected String doInBackground(Object... objects) {
//        mMap = (GoogleMap)objects[0];
//        url = (String)objects[1];
//        DownloadUrl downloadUrl = new DownloadUrl();
//
//        try {
//            googlePlacesData = downloadUrl.readUrl(url);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return googlePlacesData;
//    }
//
//    @Override
//    protected void onPostExecute(String s) {
//        List<HashMap<String,String>> nearbyPlaceList = null;
//        DataParser parser = new DataParser();
//        nearbyPlaceList = parser.parse(s);
//        showNearbyPlaces(nearbyPlaceList);
//    }
//
//    private void showNearbyPlaces(List<HashMap<String,String>> nearbyPlaceList)
//    {
//        for(int i=0; i<nearbyPlaceList.size(); i++)
//        {
//            MarkerOptions markerOptions = new MarkerOptions();
//            HashMap<String,String> googlePlace = nearbyPlaceList.get(i);
//
//            String placeName = googlePlace.get("place_name");
//            String vicinity = googlePlace.get("vicinity");
//            double lat = Double.parseDouble(googlePlace.get("lat"));
//            double lng = Double.parseDouble(googlePlace.get("lng"));
//
//            LatLng latLng = new LatLng(lat, lng);
//            markerOptions.position(latLng);
//            markerOptions.title(placeName + " : " +vicinity);
//            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
//
//            mMap.addMarker(markerOptions);
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
//        }
//    }
//}
