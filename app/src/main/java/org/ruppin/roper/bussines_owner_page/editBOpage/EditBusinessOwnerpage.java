package org.ruppin.roper.bussines_owner_page.editBOpage;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.ruppin.roper.Utils.Utils;
import org.ruppin.roper.bussines_owner_page.Models.BusinessOwner;
import org.ruppin.roper.bussines_owner_page.Utils.JsonBusinessOwnerHolder;

import org.ruppin.roper.R;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditBusinessOwnerpage extends AppCompatActivity
{

    private String IP_ADD;

    private EditText boFirstName;
    private EditText boLastName;
    private EditText boEmail;
    private EditText boLoginName;
    private EditText boPassword;
    private EditText boDOB;
    private Button saveChanges;

    private JsonBusinessOwnerHolder jsonBusinessOwnerHolder;

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_owner_edit_data_page);
        //
        boFirstName = (EditText) findViewById(R.id.BOFirstNameId);
        boLastName = (EditText) findViewById(R.id.BOLastNameId);
        boEmail = (EditText) findViewById(R.id.BOEmailId);
        boLoginName = (EditText) findViewById(R.id.BOLoginNameId);
        boPassword = (EditText) findViewById(R.id.BOPasswordId);
        boDOB = (EditText) findViewById(R.id.BO_DOBId);
        saveChanges = (Button) findViewById(R.id.BOSaveCngsBtn);

        IP_ADD = getResources().getString(R.string.IP);

        String userName = getIntent().getStringExtra("USER_NAME").toString();
        String userPass = getIntent().getStringExtra("PASSWORD").toString();
        String userFirstName = getIntent().getStringExtra("FIRST_NAME").toString();
        String userLastName = getIntent().getStringExtra("LAST_NAME").toString();
        String userEmail = getIntent().getStringExtra("EMAIL").toString();
        String userDOB = getIntent().getStringExtra("DOB").toString();
        String userCreationDate = getIntent().getStringExtra("CREATION_DATE").toString();
        String userId = getIntent().getStringExtra("ID").toString();
        boLoginName.setText(userName);
        boPassword.setText(userPass);
        boFirstName.setText(userFirstName);
        boLastName.setText(userLastName);
        boEmail.setText(userEmail);
        boDOB.setText(userDOB);

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = Utils.retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"), boLoginName.getText().toString(), boPassword.getText().toString());
                jsonBusinessOwnerHolder = retrofit.create(JsonBusinessOwnerHolder.class);
                BusinessOwner bo = new BusinessOwner(
                        userCreationDate,
                        userId,
                        boDOB.getText().toString(),
                        boEmail.getText().toString(),
                        boLoginName.getText().toString(),
                        boFirstName.getText().toString(),
                        boLastName.getText().toString(),
                        boPassword.getText().toString());
                Call call = jsonBusinessOwnerHolder.
                        updateBOinfo(bo);
                call.enqueue(new Callback<BusinessOwner>() {
                    @Override
                    public void onResponse(Call<BusinessOwner> call, Response<BusinessOwner> response) {
                        Context context = getApplicationContext();
                        CharSequence text = "info has updated..";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }

                    @Override
                    public void onFailure(Call<BusinessOwner> call, Throwable t) {
                            Log.e("ERROR", String.format("Error: %s", t.getMessage()));
                    }
                });
            }
        });

    }
}
