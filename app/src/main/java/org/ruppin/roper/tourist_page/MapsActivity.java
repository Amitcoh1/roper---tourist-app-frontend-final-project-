package org.ruppin.roper.tourist_page;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.ruppin.roper.R;
import org.ruppin.roper.Utils.Utils;
import org.ruppin.roper.login_page.loginPage;
import org.ruppin.roper.quest_page.QuestManagement;
import org.ruppin.roper.tourist_page.Models.Business;
import org.ruppin.roper.tourist_page.Models.GooglePlace;
import org.ruppin.roper.tourist_page.Models.Results;
import org.ruppin.roper.tourist_page.Models.Tourist;
import org.ruppin.roper.tourist_page.Utils.ApiCallback;
import org.ruppin.roper.tourist_page.Utils.Constants;
import org.ruppin.roper.tourist_page.Utils.GoogleCallback;
import org.ruppin.roper.tourist_page.Utils.JsonPlaceHolderApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMarkerDragListener,
        NavigationView.OnNavigationItemSelectedListener{

    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentLocationMarker;
    public static final int REQUEST_LOCATION_CODE = 99;
    private double radius = 50; // MAX IS 50
    private Context context;
    private Resources r;
    private String ip,port,key,stringTourist;
    private double latitude,longitude;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private List<String> types = new ArrayList<>();
    private Location lastGetLocation,currentLocation;
    private float pullDistance = 500;
    private Integer zoom = 13;
    Tourist touristDto = null;
    boolean firstTime = true;
    private DrawerLayout drawer;
    private NavigationView navigationView = null;
    private FrameLayout fragmentContainer;
    private EditText editText;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps_tourist_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.googleActivity);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        toggle.syncState();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        stringTourist = getIntent().getStringExtra("TOURIST").toString();
        JsonParser parser = new JsonParser();
        JsonElement mJson =  parser.parse(stringTourist);
        Gson gson = new Gson();
        touristDto = gson.fromJson(mJson, Tourist.class);
        setTypes(touristDto,types);
        key = touristDto.getGooglePlacesKey();
        if(touristDto.getRadiusThreshold() != null && !touristDto.getRadiusThreshold().isEmpty())
        {radius = Double.parseDouble(touristDto.getRadiusThreshold());}
        else{radius = Double.parseDouble(touristDto.getDefaultRadius());}

        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        Context context = MapsActivity.this;
        ip = context.getResources().getString(R.string.IP);
        port = context.getResources().getString(R.string.PORT);
        fragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);
    }



    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){drawer.closeDrawer(GravityCompat.START);}
    }

    private void setTypes(Tourist touristDto, List<String> types)
    {
        boolean flag = false;
        String[] tmp = {""};
        if(touristDto.getPreferences() != null && !touristDto.getPreferences().isEmpty() && !touristDto.getPreferences().equals("Hidden")) {
            tmp = touristDto.getPreferences().split(",");
            for(String i : tmp)
            {
                types.add(i.replaceAll("^\\s+|\\s+$",""));
            }
        }
        else if(touristDto.getDefalutTypes() != null && !touristDto.getDefalutTypes().isEmpty() && touristDto.getPreferences().equals("Hidden")) {
            tmp = touristDto.getDefalutTypes().split(",");
            for(String i : tmp)
            {
                types.add(i.replaceAll("^\\s+|\\s+$",""));
            }
        }
        else {
            types.add("restaurant");
            types.add("subway station");
            types.add("taxi stand");
            types.add("cafe");
            types.add("airport");
            types.add("shopping mall");
            types.add("taxi stand");
            flag = true;
        }

//        if(!flag)
//        {
//            for(String s : tmp)
//            {
//                types.add(s.trim());
//            }
//        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new test()).commit();
        if(menuItem.getItemId() == R.id.nbLogout) {
            startActivity(new Intent(MapsActivity.this, loginPage.class));
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(menuItem.getItemId() == R.id.nbRefresh) {
            refreshMap();
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(menuItem.getItemId() == R.id.nbQuest) {
            Intent intent = new Intent(MapsActivity.this, QuestManagement.class);
            intent.putExtra("TOURIST",stringTourist);
            drawer.closeDrawer(GravityCompat.START);
            startActivity(intent);
        }
        else if(menuItem.getItemId() == R.id.nbProfile) {
            Intent intent = new Intent(MapsActivity.this,org.ruppin.roper.tourist_page.EditTourist.class);
            intent.putExtra("TOURIST",stringTourist);
            drawer.closeDrawer(GravityCompat.START);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode)
        {
            case REQUEST_LOCATION_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //permission is granted
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        if(client == null)
                        {
                            buildGoogleApiClient();
                        }

                        mMap.setMyLocationEnabled(true);
                    }
                }
                else //permission is denied
                {
                    Toast.makeText(this,"Permission denied!", Toast.LENGTH_LONG).show();
                }
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap = googleMap;
        // mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            buildGoogleApiClient();
            // ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            // ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
            // ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_NETWORK_STATE},1);
            //  ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET},1);
            mMap.setMyLocationEnabled(true);
            // mMap.setOnMarkerDragListener(this);
            mMap.setOnMarkerClickListener(this);



        }


    }

    protected synchronized void buildGoogleApiClient()
    {
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        client.connect();

    }


    @Override
    public void onLocationChanged(Location location) {
        this.currentLocation = location;
        if(firstTime)
        {
            firstTime = false;
            lastGetLocation = location;
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mMap.addCircle(new CircleOptions()
                    .center(latLng)
                    .radius(radius)
                    .strokeWidth(0f)
                    .strokeColor(Color.parseColor("#3371cce7"))
                    .fillColor(Color.parseColor("#3371cce7")));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
            refreshMap();
        }
        else if(lastGetLocation != null && currentLocation.distanceTo(lastGetLocation) >= pullDistance)
        {
            lastGetLocation = location;
            refreshMap();
        }
    }


    private void mapInit(LatLng latLng , String type, Object dataTransfer[])
    {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        currentLocationMarker = mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));

        if(client != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
        }

        latitude = lastLocation.getLatitude();
        longitude = lastLocation.getLongitude();
        //String url = getUrl(latitude, longitude, key, false);
        // String nextPageUrl =  getUrl(latitude, longitude, key, true);


        dataTransfer[0] = mMap;
        // dataTransfer[1] = url;
        // dataTransfer[2] = nextPageUrl;
    }

//    private Retrofit retrofitInit(String url)
//    {
//        Gson gson = new GsonBuilder().serializeNulls().create();
//
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(loggingInterceptor)
//                .build();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(url)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .client(okHttpClient)
//                .build();
//
//        return retrofit;
//
//    }


    public void onClick(View v)
    {
        refreshMap();
    }

    private void refreshMap()
    {

        zoom = getZoom(radius);
        //mMap.clear();
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude())));
        //  mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
        if(ip == null || port == null)
        {
            Log.e("Connection", "Port and ip are needed please fix them.");
            Toast.makeText(MapsActivity.this, "Connection error, check for ip or port.", Toast.LENGTH_LONG).show();
        }
        else {
            lastLocation = currentLocation;
            Retrofit retrofit = Utils.retrofitInit(String.format("http://%s:%s/WebService/webapi/", ip, port),touristDto.getLoginName(),touristDto.getPassword());
            jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
            findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
            for(String s : types) {
                getBusinesses(s, new ApiCallback() {
                    @Override
                    public void onGetItem(List<Business> list)
                    {
                        List<String> businessesNames = new ArrayList<>();
                        for(Business b : list)
                        {
                            businessesNames.add(b.getName().toLowerCase());
                        }
                        getPlaces(s, new GoogleCallback() {
                            @Override
                            public void onGetPlaces(List<GooglePlace> placesList) {
                                if(!s.equals("Point of interest")){showNearbyPlaces(placesList, businessesNames, list,s);}
                            }

                            @Override
                            public void onError(Throwable t) {
                            }
                        });
                        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                        if(list != null || list.isEmpty()==false){showNearbyBusinesses(list);}
                        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Toast.makeText(MapsActivity.this, String.format("Error, could not connect to roper server, error massege: %s",t.getMessage()), Toast.LENGTH_LONG).show();

                        for(String s : types) {
                            getPlaces(s, new GoogleCallback() {
                                @Override
                                public void onGetPlaces(List<GooglePlace> placesList) {
                                    showNearbyPlaces(placesList, null, null,s);
                                    //progressDoalog.dismiss();
                                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                }

                                @Override
                                public void onError(Throwable t) {
                                    //progressDoalog.dismiss();
                                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                }
                            });

                        }
                        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    }
                });
            }}
        //mMap.clear();
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude())));
        //  mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
        mMap.clear();
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(radius*1000)
                .strokeWidth(0f)
                .strokeColor(Color.parseColor("#3371cce7"))
                .fillColor(Color.parseColor("#3371cce7")));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
    }


    private String getUrl(double latitude, double longitude, String key, double radius, String type)
    {
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location=" + latitude + "," + longitude);
        googlePlaceUrl.append("&radius=" + String.valueOf(radius * Constants.METER_TO_KILOMETER));
        googlePlaceUrl.append("&type=" + type);
        googlePlaceUrl.append("&key=" + key);
        return googlePlaceUrl.toString();
    }

    private String getUrl(String key, String nextPage)
    {
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("pagetoken=" + nextPage);
        googlePlaceUrl.append("&key=" + key);
        return googlePlaceUrl.toString();
    }


    private List<Business> getBusinesses(String type, final ApiCallback callback)
    {

        List<Business> businessList = new ArrayList<Business>();

        Call<List<Business>> call = jsonPlaceHolderApi.getBusinesses(radius, String.valueOf(lastLocation.getLongitude()), String.valueOf(lastLocation.getLatitude()),type);

        call.enqueue(new Callback<List<Business>>() {
            @Override
            public void onResponse(Call<List<Business>> call, Response<List<Business>> response) {
                if (!response.isSuccessful()) {
                    Log.e("ERROR", String.format("Error respone code: %s.", response.code()));
                    Exception t = new Exception(String.format("Error respone code: %s, response message: %s", response.code(), response.message()));
                    callback.onError(t);
                }
                List<Business> businesses = response.body();
                List<String> businessesNames = new ArrayList<>();
                callback.onGetItem(businesses);
            }

            @Override
            public void onFailure(Call<List<Business>> call, Throwable t) {
                Log.e("ERROR", String.format("Error: %s", t.getMessage()));
                callback.onError(t);
            }
        });
        return businessList;
    }




    /* Can get a maximun of 60 results 20 result per 3 pages. */
    private void getPlaces(String type, final GoogleCallback callback)
    {
        type = type.toLowerCase();
        String url = getUrl(lastLocation.getLatitude(), lastLocation.getLongitude(),key, radius, type);
        Call<GooglePlace> call = jsonPlaceHolderApi.getPlaces(url);
        List<GooglePlace> googlePlaceList = new ArrayList<>();
        final boolean flag[] = {true};

        call.enqueue(new Callback<GooglePlace>() {
            @Override
            public void onResponse(Call<GooglePlace> call, Response<GooglePlace> response) {
                if (!response.isSuccessful()) {
                    Log.e("ERROR", String.format("Error respone code: %s.", response.code()));
                    //Toast.makeText(this,"Cant load interest points.", Toast.LENGTH_LONG).show();
                    return;
                }
                GooglePlace places = response.body();
                googlePlaceList.add(places);
                if(places.getNext_page_token() != null)
                {
                    String url = getUrl(key, places.getNext_page_token());
                    call = jsonPlaceHolderApi.getPlaces(url);

                    call.enqueue(new Callback<GooglePlace>() {
                        @Override
                        public void onResponse(Call<GooglePlace> call, Response<GooglePlace> response) {
                            if (!response.isSuccessful()) {
                                Log.e("ERROR", String.format("Error respone code: %s.", response.code()));
                                return;
                            }
                            GooglePlace places = response.body();
                            googlePlaceList.add(places);
                            if(places.getNext_page_token() != null)
                            {
                                String url = getUrl(key, places.getNext_page_token());
                                call = jsonPlaceHolderApi.getPlaces(url);

                                call.enqueue(new Callback<GooglePlace>() {
                                    @Override
                                    public void onResponse(Call<GooglePlace> call, Response<GooglePlace> response) {
                                        if (!response.isSuccessful()) {
                                            Log.e("ERROR", String.format("Error respone code: %s.", response.code()));
                                            return;
                                        }
                                        GooglePlace places = response.body();
                                        googlePlaceList.add(places);
                                        if(places.getNext_page_token() == null && flag[0] == true)
                                        {
                                            flag[0] = false;
                                            callback.onGetPlaces(googlePlaceList);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<GooglePlace> call, Throwable t) {
                                    }
                                });
                            }
                            else{callback.onGetPlaces(googlePlaceList);}
                        }

                        @Override
                        public void onFailure(Call<GooglePlace> call, Throwable t) {
                        }
                    });
                }
                else{callback.onGetPlaces(googlePlaceList);}

            }

            @Override
            public void onFailure(Call<GooglePlace> call, Throwable t) {
                Log.e("ERROR", String.format("Error: %s", t.getMessage()));
                callback.onError(t);
            }
        });
    }

    private int getZoom(double radius)
    {
        if(radius > 0 && radius <3){return 13;}
        else if(radius > 2 && radius < 5){return 12;}
        else if(radius > 8 && radius < 15){return 11;}
        else if(radius > 15 && radius < 25){return 10;}
        else if(radius > 25 && radius < 35){return 9;}
        else if(radius > 35 && radius < 51){return 8;}
        else return 11;
    }

    private void showNearbyPlaces(List<GooglePlace> nearbyPlaceList, List<String> businessNames, List<Business> businesses, String type) {
        Map<String, String> duplicates = new HashMap<>();
        if (businesses != null) {
            for(int j =0; j<nearbyPlaceList.size(); j++) {
                for (int i = 0; i < nearbyPlaceList.get(j).getResults().length; i++) {
                    Results results[] = nearbyPlaceList.get(j).getResults();
                    if (!businessNames.contains(results[i].getName().toLowerCase())) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        String icon = results[i].getIcon();
                        String[] types = results[i].getTypes();
                        iconTypeBusiness(markerOptions,types[0],false);
                        LatLng latLng = new LatLng(Double.parseDouble(results[i].getGeometry().getLocation().getLat()), Double.parseDouble(results[i].getGeometry().getLocation().getLng()));
                        markerOptions.position(latLng);
                        markerOptions.title(results[i].getName() + " : " + results[i].getVicinity());


                        mMap.addMarker(markerOptions);
                    } else {
                        duplicates.put(results[i].getName().toLowerCase(), String.valueOf(j) + "," + String.valueOf(i));
                    }
                }
            }
            String[] tmp;
            for (Business b : businesses) {
                if (duplicates.containsKey(b.getName().toLowerCase()))
                {
                    tmp = duplicates.get(b.getName().toLowerCase()).split(",");
                    b.setAddress(nearbyPlaceList.get(Integer.parseInt(tmp[0])).getResults()[Integer.parseInt(tmp[1])].getVicinity());
                }
            }
        }
        else {
            for(int j =0; j<nearbyPlaceList.size(); j++) {
                for (int i = 0; i < nearbyPlaceList.get(j).getResults().length; i++) {
                    Results results[] = nearbyPlaceList.get(j).getResults();
                    MarkerOptions markerOptions = new MarkerOptions();
                    String icon = results[i].getIcon();
                    iconTypeBusiness(markerOptions,type,false);
                    LatLng latLng = new LatLng(Double.parseDouble(results[i].getGeometry().getLocation().getLat()), Double.parseDouble(results[i].getGeometry().getLocation().getLng()));
                    markerOptions.position(latLng);
                    markerOptions.title(results[i].getName() + " : " + results[i].getVicinity());

                    mMap.addMarker(markerOptions);
                }
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
            if(business.getOnPromotion()){ markerOptions.snippet(String.format("Promotion info:%s", business.getPromotionInfo()));}
            if(business.getOnPromotion() == null && business.getType() == null){iconTypeBusiness(markerOptions,"",false);}
            else if(business.getOnPromotion() == null && business.getType() != null){iconTypeBusiness(markerOptions,business.getType(),false);}
            else if(business.getOnPromotion() != null && business.getType() == null){iconTypeBusiness(markerOptions,"",business.getOnPromotion());}
            else {iconTypeBusiness(markerOptions,business.getType(),business.getOnPromotion());}
            mMap.addMarker(markerOptions);
        }
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void iconTypeBusiness(MarkerOptions markerOptions, String type, boolean promotion)
    {
        switch(type.toLowerCase())
        {
            case "airport":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.airport_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.airport_blue));}
                break;

            case "amusement park":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.amusement_park_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.amusement_park_blue));}
                break;

            case "aquarium":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.aquarium_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.aquarium_blue));}
                break;

            case "art gallery":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.art_gallery_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.art_gallery_blue));}
                break;

            case "atm":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.atm_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.atm_blue));}
                break;

            case "bakery":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.bakery_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.bakery_blue));}
                break;

            case "bank":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.bank_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.bank_blue));}
                break;

            case "bar":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.bar_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.bar_blue));}
                break;

            case "campground":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.campground_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.campground_blue));}
                break;

            case "beauty salon":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.beauty_salon_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.beauty_salon_blue));}
                break;

            case "bicycle store":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.bicycle_store_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.bicycle_store_blue));}
                break;

            case "book store":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.book_store_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.book_store_blue));}
                break;

            case "bowling alley":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.bowling_alley_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.bowling_alley_blue));}
                break;

            case "bus station":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.bus_station_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.airport_blue));}
                break;

            case "electronics store":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.electronics_store_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.electronics_store_blue));}
                break;

            case "cafe":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.cafe_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.cafe_blue));}
                break;

            case "car rental":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.car_rental_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.car_rental_blue));}
                break;

            case "casino":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.casino_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.casino_blue));}
                break;

            case "church":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.church_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.church_blue));}
                break;

            case "clothing store":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.clothing_store_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.clothing_store_blue));}
                break;

            case "convenience store":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.convenience_store_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.convenience_store_blue));}
                break;

            case "embassy":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.embassy_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.embassy_blue));}
                break;

            case "gas station":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.gas_station_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.gas_station_blue));}
                break;

            case "gym":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.gym_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.gym_blue));}
                break;

            case "hair care":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.hair_care_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.hair_care_blue));}
                break;

            case "hindu temple":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.hindu_temple_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.hindu_temple_blue));}
                break;

            case "hospital":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.hospital_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.hospital_blue));}
                break;

            case "jewelry store":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.jewelry_store_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.jewelry_store_blue));}
                break;

            case "laundry":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.laundry_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.laundry_blue));}
                break;

            case "library":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.library_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.library_blue));}
                break;

            case "liquor store":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.liquor_store_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.liquor_store_blue));}
                break;

            case "lodging":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.lodging_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.lodging_blue));}
                break;

            case "meal delivery":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.meal_delivery_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.meal_delivery_blue));}
                break;

            case "meal takeaway":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.meal_delivery_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.meal_delivery_blue));}
                break;

            case "mosque":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.mosque_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.mosque_blue));}
                break;

            case "movie theater":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.movie_theater_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.movie_theater_blue));}
                break;

            case "museum":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.museum_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.museum_blue));}
                break;

            case "night club":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.night_club_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.night_club_blue));}
                break;

            case "park":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.park_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.park_blue));}
                break;

            case "parking":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.parking_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.parking_blue));}
                break;

            case "pharmacy":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.pharmacy_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.pharmacy_blue));}
                break;

            case "point of interest":
               markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.point_of_interest_blue));
                break;

            case "police":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.police_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.police_blue));}
                break;

            case "post office":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.post_office_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.post_office_blue));}
                break;

            case "restaurant":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.restaurant_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.restaurant_blue));}
                break;

            case "rv park":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.rv_park_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.rv_park_blue));}
                break;

            case "shoe store":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.shoe_store_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.shoe_store_blue));}
                break;

            case "shopping mall":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.shopping_mall_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.shopping_mall_blue));}
                break;

            case "spa":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.spa_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.spa_blue));}
                break;

            case "stadium":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.stadium_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.stadium_blue));}
                break;

            case "store":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.store_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.store_blue));}
                break;

            case "subway station":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.subway_station_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.subway_station_blue));}
                break;

            case "supermarket":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.supermarket_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.supermarket_blue));}
                break;

            case "synagogue":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.synagogue_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.synagogue_blue));}
                break;

            case "taxi stand":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.taxi_stand_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.taxi_stand_blue));}
                break;

            case "train station":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.train_station_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.train_station_blue));}
                break;

            case "transit station":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.transit_station_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.transit_station_blue));}
                break;

            case "travel agency":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.travel_agency_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.travel_agency_blue));}
                break;

            case "zoo":
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.zoo_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.zoo_blue));}
                break;

            default:
                if(promotion){markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.generic_star));}
                else{markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.generic_blue));}
                break;
        }
    }

    /* private void iconTypeGoogle(MarkerOptions markerOptions, String type, boolean promotion) {

         if (type.contains("airport")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.airport_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.airport_blue));
             }
         } else if (type.contains("amusement_park")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.amusement_park_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.amusement_park_blue));
             }
         } else if (type.contains("aquarium")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.aquarium_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.aquarium_blue));
             }
         } else if (type.contains("art_gallery")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.art_gallery_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.art_gallery_blue));
             }
         } else if (type.contains("atm")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.atm_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.atm_blue));
             }
         } else if (type.contains("bakery")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.bakery_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.bakery_blue));
             }
         } else if (type.contains("bank")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.bank_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.bank_blue));
             }
         } else if (type.contains("bar")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.bar_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.bar_blue));
             }
         } else if (type.contains("campground")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.campground_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.campground_blue));
             }
         } else if (type.contains("beauty_salon")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.beauty_salon_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.beauty_salon_blue));
             }
         } else if (type.contains("bicycle_store")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.bicycle_store_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.bicycle_store_blue));
             }
         } else if (type.contains("book_store")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.book_store_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.book_store_blue));
             }
         } else if (type.contains("bowling_alley")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.bowling_alley_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.bowling_alley_star));
             }
         } else if (type.contains("bus_station")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.bus_station_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.airport_blue));
             }
         } else if (type.contains("electronics_store")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.electronics_store_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.electronics_store_blue));
             }
         } else if (type.contains("cafe")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.cafe_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.cafe_blue));
             }
         } else if (type.contains("car_rental")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.car_rental_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.car_rental_blue));
             }
         } else if (type.contains("casino")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.casino_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.casino_blue));
             }
         } else if (type.contains("church")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.church_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.church_blue));
             }
         } else if (type.contains("clothing_store")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.clothing_store_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.clothing_store_blue));
             }
         } else if (type.contains("convenience_store")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.convenience_store_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.convenience_store_blue));
             }
         } else if (type.contains("embassy")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.embassy_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.embassy_blue));
             }
         } else if (type.contains("gas_station")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.gas_station_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.gas_station_blue));
             }
         } else if (type.contains("gym")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.gym_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.gym_blue));
             }
         } else if (type.contains("hair_care")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.hair_care_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.hair_care_blue));
             }
         } else if (type.contains("hindu_temple")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.hindu_temple_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.hindu_temple_blue));
             }
         } else if (type.contains("hospital")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.hospital_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.hospital_blue));
             }
         } else if (type.contains("jewelry_store")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.jewelry_store_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.jewelry_store_blue));
             }
         } else if (type.contains("laundry")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.laundry_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.laundry_blue));
             }
         } else if (type.contains("library")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.library_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.library_blue));
             }
         } else if (type.contains("liquor_store")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.liquor_store_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.liquor_store_blue));
             }
         } else if (type.contains("lodging")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.lodging_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.lodging_blue));
             }
         } else if (type.contains("meal_delivery")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.meal_delivery_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.meal_delivery_blue));
             }
         } else if (type.contains("meal_takeaway")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.meal_delivery_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.meal_delivery_blue));
             }
         } else if (type.contains("mosque")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.mosque_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.mosque_blue));
             }
         } else if (type.contains("movie_theater")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.movie_theater_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.movie_theater_blue));
             }
         } else if (type.contains("museum")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.museum_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.museum_blue));
             }
         } else if (type.contains("night_club")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.night_club_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.night_club_blue));
             }
         } else if (type.contains("park")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.park_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.park_blue));
             }
         } else if (type.contains("parking")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.parking_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.parking_blue));
             }
         } else if (type.contains("pharmacy")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.pharmacy_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.pharmacy_blue));
             }
         } else if (type.contains("police")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.police_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.police_blue));
             }
         } else if (type.contains("post_office")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.post_office_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.post_office_blue));
             }
         } else if (type.contains("restaurant")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.restaurant_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.restaurant_blue));
             }
         } else if (type.contains("rv_park")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.rv_park_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.rv_park_blue));
             }
         } else if (type.contains("shoe_store")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.shoe_store_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.shoe_store_blue));
             }
         } else if (type.contains("shopping_mall")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.shopping_mall_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.shopping_mall_blue));
             }
         } else if (type.contains("spa")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.spa_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.spa_blue));
             }
         } else if (type.contains("stadium")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.stadium_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.stadium_blue));
             }
         } else if (type.contains("store")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.store_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.store_blue));
             }
         } else if (type.contains("subway_station")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.subway_station_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.subway_station_blue));
             }
         } else if (type.contains("supermarket")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.supermarket_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.supermarket_blue));
             }
         } else if (type.contains("synagogue")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.synagogue_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.synagogue_blue));
             }
         } else if (type.contains("taxi_stand")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.taxi_stand_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.taxi_stand_blue));
             }
         } else if (type.contains("train_station")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.train_station_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.train_station_blue));
             }
         } else if (type.contains("transit_station")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.transit_station_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.transit_station_blue));
             }
         } else if (type.contains("travel_agency")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.travel_agency_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.travel_agency_blue));
             }
         } else if (type.contains("zoo")) {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.zoo_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.zoo_blue));
             }
         } else {
             if (promotion) {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.generic_star));
             } else {
                 markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.mipmap.generic_blue));
             }
         }
     }
 */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if(ContextCompat.checkSelfPermission(MapsActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, MapsActivity.this);
        }

    }

    public boolean checkLocationPermission()
    {
        if(ContextCompat.checkSelfPermission(MapsActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(MapsActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
            }

            else
            {
                ActivityCompat.requestPermissions(MapsActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
            }
            return false;
        }


        return true;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // marker.setDraggable(true);
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        // end_lantitude = marker.getPosition().latitude;
        //  end_longitude = marker.getPosition().longitude;
    }


}
