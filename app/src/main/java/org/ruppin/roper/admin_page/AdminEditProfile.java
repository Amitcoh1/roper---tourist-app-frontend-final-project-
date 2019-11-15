package org.ruppin.roper.admin_page;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.ruppin.roper.R;
import org.ruppin.roper.Utils.Utils;
import org.ruppin.roper.admin_page.Utils.JsonAdminHolderApi;
import org.ruppin.roper.bussines_owner_page.Models.BusinessBO;
import org.ruppin.roper.bussines_owner_page.Models.BusinessOwner;
import org.ruppin.roper.bussines_owner_page.Utils.JsonBusinessOwnerHolder;
import org.ruppin.roper.bussines_owner_page.add_business_page.AddBusinessToBO;
import org.ruppin.roper.tourist_page.Models.Categories;
import org.ruppin.roper.tourist_page.Utils.JsonCtgHolderApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AdminEditProfile extends AppCompatActivity {
    private EditText adminFN;
    private EditText adminLN;
    private EditText adminEmail;
    private EditText adminLoginName;
    private EditText adminPassword;
    private EditText adminDOB;
    private Button adminSaveCngs;
    private String IP_ADD;
    private JsonAdminHolderApi jsonAdminHolderApi;
    private Person person;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_profile);
        adminDOB = (EditText)findViewById(R.id.adminDOBId);
        adminEmail = (EditText)findViewById(R.id.adminEmailId);
        adminFN = (EditText)findViewById(R.id.adminFNId);
        adminLN = (EditText)findViewById(R.id.adminLNId);
        adminLoginName = (EditText)findViewById(R.id.AdminUNId);
        adminPassword = (EditText)findViewById(R.id.AdminPASSId);
        adminSaveCngs = (Button)findViewById(R.id.SaveCngsAdminPflId);
        IP_ADD = getResources().getString(R.string.IP);
        //
        String userName = getIntent().getStringExtra("USER_NAME").toString();
        String userPass = getIntent().getStringExtra("PASSWORD").toString();
        Retrofit retrofit = Utils.retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"),userName,userPass);
        jsonAdminHolderApi = retrofit.create(JsonAdminHolderApi.class);
        String url = "http://"+IP_ADD+":8080/WebService/webapi/person_service/login?loginName="+userName;
        Call call = jsonAdminHolderApi.getUser(url);
        call.enqueue(new Callback<Person>() {
            @Override
            public void onResponse(Call<Person> call, Response<Person> response) {
                person = response.body();
                adminDOB.setText(person.getDateOfBirthStr().toString());
                adminEmail.setText(person.getEmail().toString());
                adminFN.setText(person.getFirstName().toString());
                adminLN.setText(person.getLastName().toString());
                adminLoginName.setText(person.getLoginName().toString());
                adminPassword.setText(person.getPassword().toString());
                //
                adminSaveCngs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Person personToUpdt = new Person();
                        personToUpdt.setId(person.getId());
                        personToUpdt.setDateOfBirthStr(person.getDateOfBirthStr());
                        if(!person.getEmail().equals(adminEmail.getText().toString()))
                            personToUpdt.setEmail(adminEmail.getText().toString());

                        if(!person.getFirstName().equals(adminFN.getText().toString()))
                            personToUpdt.setFirstName(adminFN.getText().toString());
                        else
                            personToUpdt.setFirstName(person.getFirstName().toString());

                        if(!person.getLastName().equals(adminLN.getText().toString()))
                            personToUpdt.setLastName(adminLN.getText().toString());
                        else
                            personToUpdt.setLastName(person.getLastName().toString());

                        if(!person.getLoginName().equals(adminLoginName.getText().toString()))
                            personToUpdt.setLoginName(adminLoginName.getText().toString());

                        if(!person.getPassword().equals(adminPassword.getText().toString()))
                            personToUpdt.setPassword(adminPassword.getText().toString());
                        else
                            personToUpdt.setPassword(person.getPassword().toString());
                        //
                        Retrofit retrofit  = Utils.retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"),userName,userPass);
                        jsonAdminHolderApi = retrofit.create(JsonAdminHolderApi.class);
                        Call call = jsonAdminHolderApi.updtAdminProfile(personToUpdt);
                        call.enqueue(new Callback<Person>() {
                            @Override
                            public void onResponse(Call<Person> call, Response<Person> response)
                            {
                                Context context = getApplicationContext();
                                CharSequence text = "profile has updated..";
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {

                            }
                        });
                    }
                });
            }

            @Override
            public void onFailure(Call<Person> call, Throwable t) {

            }
        });

    }
}
