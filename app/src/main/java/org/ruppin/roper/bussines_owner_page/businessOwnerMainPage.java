package org.ruppin.roper.bussines_owner_page;
//

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.ruppin.roper.Utils.Utils;
import org.ruppin.roper.bussines_owner_page.Models.BusinessBO;
import org.ruppin.roper.bussines_owner_page.Models.BusinessOwner;
import org.ruppin.roper.bussines_owner_page.Utils.JsonBusinessOwnerHolder;
import org.ruppin.roper.bussines_owner_page.delete_business.DeleteBusiness;
import org.ruppin.roper.bussines_owner_page.edit_bussineses_page.EditBusinessOwnesBusinesses;
import org.ruppin.roper.bussines_owner_page.show_bussineses_page.ShowBusinessOwnesBusinesses;

import java.util.ArrayList;

import org.ruppin.roper.R;
import org.ruppin.roper.login_page.loginPage;
import org.ruppin.roper.tourist_page.MapsActivity;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class businessOwnerMainPage extends AppCompatActivity {

    private String IP_ADD;
    private Button editBusinessOnerInfo;
    private Button editBusinesses;
    private Button addBusiness;
    private Button showBusinesses;
    private Button deleteBusiness;
    private Button logout;
    String id;
    ArrayList<BusinessBO> boBsns;
    private JsonBusinessOwnerHolder jsonBusinessOwnerHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_owner_main_page);
        //buttons from the layout
        editBusinessOnerInfo = (Button) findViewById(R.id.editBusinessOwnerInfoBtn);
        editBusinesses = (Button) findViewById(R.id.editBusinessBtn);
        addBusiness = (Button) findViewById(R.id.addBusinessBtn);
        showBusinesses = (Button) findViewById(R.id.showBusinessBtn);
        deleteBusiness = (Button) findViewById(R.id.deleteBusinessBtn);
        logout = (Button)findViewById(R.id.LogoutBOId);
        IP_ADD = getResources().getString(R.string.IP);
        //OPTION 1
        editBusinessOnerInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = getIntent().getExtras();
                String userName = extras.getString("USER_NAME");
                String password = extras.getString("PASSWORD");
                Retrofit retrofit  = Utils.retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"),userName,password);
                jsonBusinessOwnerHolder = retrofit.create(JsonBusinessOwnerHolder.class);
                Call call = jsonBusinessOwnerHolder.getBusinessOwner("http://"+IP_ADD+":8080/WebService/webapi/business_owner_service/businessOwner?loginName="+userName);
                call.enqueue(new Callback<BusinessOwner>() {
                public void onResponse(Call<BusinessOwner> call, Response<BusinessOwner> response) {
                    BusinessOwner bo = response.body();
                    //start the new intent of the edit page
                    Intent intent = new Intent(businessOwnerMainPage.this,org.ruppin.roper.bussines_owner_page.editBOpage.EditBusinessOwnerpage.class);
                    intent.putExtra("USER_NAME", userName.toString());
                    intent.putExtra("PASSWORD",password.toString());
                    intent.putExtra("FIRST_NAME",bo.getFirstName().toString());
                    intent.putExtra("LAST_NAME",bo.getLastName().toString());
                    intent.putExtra("EMAIL",bo.getEmail().toString());
                    intent.putExtra("DOB",bo.getDateOfBirthStr().toString());
                    intent.putExtra("CREATION_DATE",bo.getCreationDate().toString());
                    intent.putExtra("ID",bo.getId().toString());
                    startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<BusinessOwner> call, Throwable t) {
                        Log.e("ERROR", String.format("Error: %s", t.getMessage()));
                    }
                });
            }
        });
        //OPTION 2
        showBusinesses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = getIntent().getExtras();
                String userName = extras.getString("USER_NAME");
                String password = extras.getString("PASSWORD");
                Retrofit retrofit  = Utils.retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"),userName,password);
                jsonBusinessOwnerHolder = retrofit.create(JsonBusinessOwnerHolder.class);
                Call call = jsonBusinessOwnerHolder.getBusinessOwner("http://"+IP_ADD+":8080/WebService/webapi/business_owner_service/businessOwner?loginName="+userName);
                call.enqueue(new Callback<BusinessOwner>() {
                    @Override
                    public void onResponse(Call<BusinessOwner> call, Response<BusinessOwner> response)
                    {
                        BusinessOwner bo = response.body();
                        boBsns = bo.getBusinesses();
                        Gson boBsnsJson = new Gson();
                        String boBsnsString = boBsnsJson.toJson(boBsns);
                        Intent intent = new Intent(businessOwnerMainPage.this, ShowBusinessOwnesBusinesses.class);
                        intent.putExtra("BUSINESSES",boBsnsString);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                    }
                });
            }
        });
        //OPTION 3
        editBusinesses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = getIntent().getExtras();
                String userName = extras.getString("USER_NAME");
                String password = extras.getString("PASSWORD");
                Retrofit retrofit  = Utils.retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"),userName,password);
                jsonBusinessOwnerHolder = retrofit.create(JsonBusinessOwnerHolder.class);
                Call call = jsonBusinessOwnerHolder.getBusinessOwner("http://"+IP_ADD+":8080/WebService/webapi/business_owner_service/businessOwner?loginName="+userName);
                call.enqueue(new Callback<BusinessOwner>() {
                    @Override
                    public void onResponse(Call<BusinessOwner> call, Response<BusinessOwner> response)
                    {
                        BusinessOwner bo = response.body();
                        boBsns = bo.getBusinesses();
                        Gson boBsnsJson = new Gson();
                        String boBsnsString = boBsnsJson.toJson(boBsns);
                        Intent intent = new Intent(businessOwnerMainPage.this, EditBusinessOwnesBusinesses.class);
                        intent.putExtra("BUSINESSES",boBsnsString);
                        intent.putExtra("USERNAME",userName);
                        intent.putExtra("PASSWORD",password);
                        intent.putExtra("ID",bo.getId());
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {

                    }
                });
            }
        });
        //OPTION 4
        addBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = getIntent().getExtras();
                String userName = extras.getString("USER_NAME");
                String password = extras.getString("PASSWORD");
                Retrofit retrofit  = Utils.retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"),userName,password);
                jsonBusinessOwnerHolder = retrofit.create(JsonBusinessOwnerHolder.class);
                Call call = jsonBusinessOwnerHolder.getBusinessOwner("http://"+IP_ADD+":8080/WebService/webapi/business_owner_service/businessOwner?loginName="+userName);
                call.enqueue(new Callback<BusinessOwner>() {
                    @Override
                    public void onResponse(Call<BusinessOwner> call, Response<BusinessOwner> response)
                    {
                        BusinessOwner bo = response.body();
                        id = bo.getId();

                        Intent intent = new Intent(businessOwnerMainPage.this,org.ruppin.roper.bussines_owner_page.add_business_page.AddBusinessToBO.class);
                        intent.putExtra("ID",id);
                        intent.putExtra("USERNAME",userName);
                        intent.putExtra("PASSWORD",password);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {

                    }
                });
            }
        });
        //OPTION 5
        deleteBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Bundle extras = getIntent().getExtras();
                String userName = extras.getString("USER_NAME");
                String password = extras.getString("PASSWORD");
                Retrofit retrofit  = Utils.retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"),userName,password);
                jsonBusinessOwnerHolder = retrofit.create(JsonBusinessOwnerHolder.class);
                Call call = jsonBusinessOwnerHolder.getBusinessOwner("http://"+IP_ADD+":8080/WebService/webapi/business_owner_service/businessOwner?loginName="+userName);
                call.enqueue(new Callback<BusinessOwner>() {
                    @Override
                    public void onResponse(Call<BusinessOwner> call, Response<BusinessOwner> response)
                    {
                        BusinessOwner bo = response.body();
                        boBsns = bo.getBusinesses();
                        Gson boBsnsJson = new Gson();
                        String boBsnsString = boBsnsJson.toJson(boBsns);
                        Intent intent = new Intent(businessOwnerMainPage.this, DeleteBusiness.class);
                        intent.putExtra("BUSINESSES",boBsnsString);
                        intent.putExtra("USERNAME",userName);
                        intent.putExtra("PASSWORD",password);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {

                    }
                });
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(businessOwnerMainPage.this, loginPage.class));
            }
        });
    }
    @Override
    public void onBackPressed() {
        Context context = getApplicationContext();
        CharSequence text = "Use the logout button to go back to login page";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}

