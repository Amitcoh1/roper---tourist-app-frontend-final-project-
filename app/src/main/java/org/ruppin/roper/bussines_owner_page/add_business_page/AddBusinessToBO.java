package org.ruppin.roper.bussines_owner_page.add_business_page;
//
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.ruppin.roper.Utils.Utils;
import org.ruppin.roper.bussines_owner_page.Models.BusinessBO;
import org.ruppin.roper.bussines_owner_page.Models.BusinessOwner;
import org.ruppin.roper.bussines_owner_page.Utils.JsonBusinessOwnerHolder;
import org.ruppin.roper.tourist_page.Models.Categories;
import org.ruppin.roper.tourist_page.Utils.JsonCtgHolderApi;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.ruppin.roper.R;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//
public class AddBusinessToBO extends AppCompatActivity {
    private TextView bsnsName;
    private TextView bsnsAdd;
    private Spinner bsnsType;
    private TextView promotInfo;
    private Switch onPromot;
    private TextView token;
    String onPromotData;
    private Button save;
    private String IP_ADD;
    private JsonBusinessOwnerHolder jsonBusinessOwnerHolder;
    private JsonCtgHolderApi jsonCtgHolderApi;
    //private PlaceAutocompleteFragment placeAutocompleteFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
       // placeAutocompleteFragment = (PlaceAutocompleteFragment)getFragmentManager().findFragmentById(R.id.BsnsAddId);
        setContentView(R.layout.activity_add_business);
        bsnsName = (TextView)findViewById(R.id.BsnsNameId);
        bsnsAdd = (TextView)findViewById(R.id.BsnsAddId);
        bsnsType = (Spinner)findViewById(R.id.bsnsTypeList);
        onPromot = (Switch)findViewById(R.id.onPromotionId);
        promotInfo = (TextView)findViewById(R.id.bsnsPromotInfoId);
        token = (TextView)findViewById(R.id.addBsnsToken);
        save = (Button)findViewById(R.id.saveCngsBsnsId);
        IP_ADD = getResources().getString(R.string.IP);
        String userName = getIntent().getStringExtra("USERNAME").toString();
        String userPass = getIntent().getStringExtra("PASSWORD").toString();
        Retrofit retrofit  = Utils.retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"),userName,userPass);
        jsonCtgHolderApi = retrofit.create(JsonCtgHolderApi.class);
        Call call = jsonCtgHolderApi.getCtgrs("http://"+IP_ADD+":8080/WebService/webapi/category_service/categories");
        call.enqueue(new Callback<List<Categories>>() {
            @Override
            public void onResponse(Call<List<Categories>> call, Response<List<Categories>> response)
            {
                List<Categories> categories = response.body();
                List<String> ctgrsName= new ArrayList<String>();
                for(int i=0;i<categories.size();i++)
                    if(categories.get(i).getType().equals("place"))ctgrsName.add(categories.get(i).getDisplayName());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddBusinessToBO.this, android.R.layout.simple_spinner_dropdown_item, ctgrsName);
                bsnsType.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit_add = Utils.retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"),userName,userPass);
                jsonBusinessOwnerHolder = retrofit_add.create(JsonBusinessOwnerHolder.class);
                Bundle extras = getIntent().getExtras();
                String id = extras.getString("ID");
                LatLng latLng = getLocationFromAddress(getApplicationContext(),bsnsAdd.getText().toString());
                BusinessBO businessBO = new BusinessBO(
                        token.getText().toString(),
                        id.toString(),
                        Double.toString(latLng.latitude),
                        Double.toString(latLng.longitude),
                        bsnsName.getText().toString(),
                        onPromot.isChecked()? "true" : "false",
                        bsnsAdd.getText().toString(),
                        promotInfo.getText().toString(),
                        bsnsType.getSelectedItem().toString());
                Call call_add = jsonBusinessOwnerHolder.addBusiness("http://"+IP_ADD+":8080/WebService/webapi/business_service/business?id="+id,businessBO);
                call_add.enqueue(new Callback<BusinessOwner>() {
                    @Override
                    public void onResponse(Call<BusinessOwner> call, Response<BusinessOwner> response) {
                        Context context = getApplicationContext();
                        CharSequence text = "business has added.";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }

                    @Override
                    public void onFailure(Call<BusinessOwner> call, Throwable t) {

                    }
                });
            }
        });


    }
    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }
}

