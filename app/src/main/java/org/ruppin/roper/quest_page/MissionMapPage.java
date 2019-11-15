package org.ruppin.roper.quest_page;

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
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.ruppin.roper.R;
import org.ruppin.roper.Utils.Utils;
import org.ruppin.roper.login_page.loginPage;
import org.ruppin.roper.tourist_page.MapsActivity;
import org.ruppin.roper.tourist_page.Models.Business;
import org.ruppin.roper.tourist_page.Models.GooglePlace;
import org.ruppin.roper.tourist_page.Models.Mission;
import org.ruppin.roper.tourist_page.Models.Quest;
import org.ruppin.roper.tourist_page.Models.Tourist;
import org.ruppin.roper.tourist_page.Utils.ApiCallback;
import org.ruppin.roper.tourist_page.Utils.GoogleCallback;
import org.ruppin.roper.tourist_page.Utils.JsonPlaceHolderApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class MissionMapPage extends AppCompatActivity implements OnMapReadyCallback,
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
        setContentView(R.layout.activity_quest);
        Toolbar toolbar = findViewById(R.id.toolbar_quest);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.quest_drawer);
        navigationView = findViewById(R.id.quest_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        toggle.syncState();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.quest_map);
        mapFragment.getMapAsync(this);

        stringTourist = getIntent().getStringExtra("TOURIST").toString();
        JsonParser parser = new JsonParser();
        JsonElement mJson =  parser.parse(stringTourist);
        Gson gson = new Gson();
        touristDto = gson.fromJson(mJson, Tourist.class);
        key = touristDto.getGooglePlacesKey();
        if(touristDto.getRadiusThreshold() != null && !touristDto.getRadiusThreshold().isEmpty())
        {radius = Double.parseDouble(touristDto.getRadiusThreshold());}
        else{radius = Double.parseDouble(touristDto.getDefaultRadius());}

        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        Context context = MissionMapPage.this;
        ip = context.getResources().getString(R.string.IP);
        port = context.getResources().getString(R.string.PORT);
        fragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);

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

    protected synchronized void buildGoogleApiClient()
    {
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        client.connect();

    }

    public boolean checkLocationPermission()
    {
        if(ContextCompat.checkSelfPermission(MissionMapPage.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(MissionMapPage.this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(MissionMapPage.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
            }

            else
            {
                ActivityCompat.requestPermissions(MissionMapPage.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
            }
            return false;
        }


        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap = googleMap;
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            mMap.setOnMarkerClickListener(this);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if(ContextCompat.checkSelfPermission(MissionMapPage.this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, MissionMapPage.this);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
    {
        if(menuItem.getItemId() == R.id.nbLogout) {
            startActivity(new Intent(MissionMapPage.this, loginPage.class));
            drawer.closeDrawer(GravityCompat.START);
        }

        if(menuItem.getItemId() == R.id.nbFinish) {
            Intent intent = new Intent(MissionMapPage.this, FinishMission.class);
            intent.putExtra("TOURIST",stringTourist);
            intent.putExtra("USER_NAME",touristDto.getLoginName().toString());
            intent.putExtra("PASSWORD" ,touristDto.getPassword().toString());
            drawer.closeDrawer(GravityCompat.START);
            startActivity(intent);
        }

        if(menuItem.getItemId() == R.id.nbRefresh) {
            refreshMapWithCheck();
            drawer.closeDrawer(GravityCompat.START);
        }

        if(menuItem.getItemId() == R.id.nbQuest) {
            Intent intent = new Intent(MissionMapPage.this, QuestManagement.class);
            intent.putExtra("TOURIST",stringTourist);
            drawer.closeDrawer(GravityCompat.START);
            startActivity(intent);
        }

        if(menuItem.getItemId() == R.id.nbTourist) {
            Intent intent = new Intent(MissionMapPage.this, MapsActivity.class);
            intent.putExtra("TOURIST",stringTourist);
            drawer.closeDrawer(GravityCompat.START);
            startActivity(intent);
        }

        return true;
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

    private void refreshMap()
    {

        zoom = getZoom(radius);
        if(ip == null || port == null)
        {
            Log.e("Connection", "Port and ip are needed please fix them.");
            Toast.makeText(MissionMapPage.this, "Connection error, check for ip or port.", Toast.LENGTH_LONG).show();
        }
        else {
            mMap.clear();
            findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
            lastLocation = currentLocation;
            MarkerOptions markerOptions = new MarkerOptions();
            double lat;
            double lng;
            String diff,status;

            for(Quest quest : touristDto.getQuests())
            {
                if(quest.getId().equals(touristDto.getOnGoingQuests()))
                {
                    for(Mission mission : quest.getMissions()) {
                        lat = Double.parseDouble(mission.getBusiness().getLatitude());
                        lng = Double.parseDouble(mission.getBusiness().getLongitude());

                        LatLng latLng = new LatLng(lat, lng);
                        markerOptions.position(latLng);
                        markerOptions.title(mission.getDescription());
                        markerOptions.snippet(mission.getBusiness().getName());

                        iconType(markerOptions, mission.getDifficulty(), mission.getStatus());
                        mMap.addMarker(markerOptions);
                    }
                }
            }

            }

        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(radius*1000)
                .strokeWidth(0f)
                .strokeColor(Color.parseColor("#3371cce7"))
                .fillColor(Color.parseColor("#3371cce7")));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
    }

    private boolean checkForActiveQuest()
    {
        for(Quest quest : touristDto.getQuests())
        {
            if(touristDto.getOnGoingQuests().contains(quest.getId()))
            {
                return true;
            }
        }
        return false;
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void iconType(MarkerOptions markerOptions, String diff, String status)
    {
        if(status.toLowerCase().equals("done"))
        {
            markerOptions.icon(bitmapDescriptorFromVector(MissionMapPage.this, R.mipmap.mission_tick_blue));
        }
        else if(diff.toLowerCase().equals("easy"))
        {
            if(status.toLowerCase().equals("new"))
            {
                markerOptions.icon(bitmapDescriptorFromVector(MissionMapPage.this, R.mipmap.mission_star_green));
            }
            if(status.toLowerCase().equals("active"))
            {
                markerOptions.icon(bitmapDescriptorFromVector(MissionMapPage.this, R.mipmap.mission_blank_green));
            }
        }
        else if(diff.toLowerCase().equals("medium"))
        {
            if(status.toLowerCase().equals("new"))
            {
                markerOptions.icon(bitmapDescriptorFromVector(MissionMapPage.this, R.mipmap.mission_star_yellow));
            }
            if(status.toLowerCase().equals("active"))
            {
                markerOptions.icon(bitmapDescriptorFromVector(MissionMapPage.this, R.mipmap.mission_blank_yellow));
            }
        }
        else if(diff.toLowerCase().equals("hard"))
        {
            if(status.toLowerCase().equals("new"))
            {
                markerOptions.icon(bitmapDescriptorFromVector(MissionMapPage.this, R.mipmap.mission_star_red));
            }
            if(status.toLowerCase().equals("active"))
            {
                markerOptions.icon(bitmapDescriptorFromVector(MissionMapPage.this, R.mipmap.mission_blank_red));
            }
        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
            refreshMapWithCheck();

        }
        else if(lastGetLocation != null && currentLocation.distanceTo(lastGetLocation) >= pullDistance)
        {
            lastGetLocation = location;
            refreshMapWithCheck();
        }
    }

    private void refreshMapWithCheck()
    {
        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        if(checkForActiveQuest()){refreshMap();}
        else
        {
            Context context = getApplicationContext();
            CharSequence text = "No active quest, please get a quest first.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            toast.show();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
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

    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){drawer.closeDrawer(GravityCompat.START);}
    }
}
