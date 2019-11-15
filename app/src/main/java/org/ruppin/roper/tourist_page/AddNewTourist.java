package org.ruppin.roper.tourist_page;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;
import org.ruppin.roper.Utils.Utils;
import org.ruppin.roper.tourist_page.Models.Categories;
import org.ruppin.roper.tourist_page.Utils.JsonCtgHolderApi;
import org.ruppin.roper.tourist_page.Utils.MultiSelectSpinner;
import org.ruppin.roper.users.usersEntity;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.ruppin.roper.R;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddNewTourist extends AppCompatActivity {

    private String IP_ADD;

    private TextView register1;
    private TextView register2;
    private EditText nFname;
    private EditText nLname;
    private EditText nUsername;
    private EditText nPassword;
    private EditText nDOB;
    private EditText nEmail;
    private Spinner nType;
    private CheckBox cbMoreInfo;
    private String selectedItemType;
    private String responseResult;
    private int counter = 1;
    private Spinner nGender;
    private String selectGenderType;
    private EditText age;
    private Spinner nSpokenLen;
    private Spinner nVicType;
    private String selectSpokenLanguage;
    private String selectVicType;
    private CheckBox nMeetPeople;
    private EditText nradiusTH;
    private MultiSelectSpinner selectCtgrsType;
    JsonCtgHolderApi jsonCtgHolderApi;
    JSONObject jsonAdd = new JSONObject();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_tourist);
        nFname = (EditText) findViewById(R.id.firstName);
        nLname = (EditText) findViewById(R.id.lastName);
        nDOB = (EditText) findViewById(R.id.DOB);
        nEmail = (EditText) findViewById(R.id.email);
        nPassword = (EditText) findViewById(R.id.password);
        nUsername = (EditText) findViewById(R.id.userName);
        register1 = (TextView) findViewById(R.id.register);
        register2 = (TextView) findViewById(R.id.register);
        register1.setText("Register");
        cbMoreInfo = (CheckBox) findViewById(R.id.moreInfo);
        age = (EditText) findViewById(R.id.age);
        nSpokenLen = (Spinner) findViewById(R.id.spokenLen);
        nGender = (Spinner) findViewById(R.id.gender);
        nMeetPeople = (CheckBox) findViewById(R.id.meetPeople);
        nVicType = (Spinner) findViewById(R.id.vicationType);
        nradiusTH = (EditText) findViewById(R.id.radiousTH);
        selectCtgrsType = (MultiSelectSpinner) findViewById(R.id.CtgrsType);


        nGender.setAlpha(0.0f);
        nGender.setEnabled(false);
        age.setAlpha(0.0f);
        age.setEnabled(false);
        nSpokenLen.setAlpha(0.0f);
        nSpokenLen.setEnabled(false);
        nVicType.setAlpha(0.0f);
        nVicType.setEnabled(false);
        nMeetPeople.setAlpha(0.0f);
        nMeetPeople.setEnabled(false);
        nradiusTH.setAlpha(0.0f);
        nradiusTH.setEnabled(false);
        selectCtgrsType.setAlpha(0.0f);
        selectCtgrsType.setEnabled(false);
        IP_ADD = getResources().getString(R.string.IP);

        cbMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nGender.setAlpha(1.0f);
                nGender.setEnabled(true);
                age.setAlpha(1.0f);
                age.setEnabled(true);
                nSpokenLen.setAlpha(1.0f);
                nSpokenLen.setEnabled(true);
                nVicType.setAlpha(1.0f);
                nVicType.setEnabled(true);
                nMeetPeople.setAlpha(1.0f);
                nMeetPeople.setEnabled(true);
                nradiusTH.setAlpha(1.0f);
                nradiusTH.setEnabled(true);
                selectCtgrsType.setAlpha(1.0f);
                selectCtgrsType.setEnabled(true);
                if(cbMoreInfo.isChecked()==false)
                {
                    nGender.setAlpha(0.0f);
                    nGender.setEnabled(false);
                    age.setAlpha(0.0f);
                    age.setEnabled(false);
                    nSpokenLen.setAlpha(0.0f);
                    nSpokenLen.setEnabled(false);
                    nVicType.setAlpha(0.0f);
                    nVicType.setEnabled(false);
                    nMeetPeople.setAlpha(0.0f);
                    nMeetPeople.setEnabled(false);
                    nradiusTH.setAlpha(0.0f);
                    nradiusTH.setEnabled(false);
                    selectCtgrsType.setAlpha(0.0f);
                    selectCtgrsType.setEnabled(false);
                }
            }
        });

        register1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()!=false) {
                    try {
                        createUser();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(responseResult.equals("412"))
                    {
                        Context context = getApplicationContext();
                        CharSequence text = "fields must be unique!";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                    else {
                        Context context = getApplicationContext();
                        CharSequence text = "user created!";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        register1.setText("");
                        register1.setText("Login Page");
                        register2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(AddNewTourist.this, org.ruppin.roper.login_page.loginPage.class);
                                startActivity(intent);
                            }
                        });
                    }
                }
                else if(!register1.getText().toString().equals("Login Page"))
                {
                    Context context = getApplicationContext();
                    CharSequence text = "field is missing!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(AddNewTourist.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Gender));
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nGender.setAdapter(spinnerArrayAdapter);
        nGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        selectGenderType = "Male";
                        break;
                    case 1:
                        selectGenderType = "Female";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(AddNewTourist.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Languages));
        spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nSpokenLen.setAdapter(spinnerArrayAdapter1);
        nSpokenLen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        selectSpokenLanguage = "Hebrew";
                        break;
                    case 1:
                        selectSpokenLanguage = "Arabic";
                        break;
                    case 2:
                        selectSpokenLanguage = "English";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(AddNewTourist.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.vicType));
        spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nVicType.setAdapter(spinnerArrayAdapter2);
        nVicType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        selectVicType = "business";
                        break;
                    case 1:
                        selectVicType = "pleasure";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Retrofit retrofit  = Utils.retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"),nUsername.getText().toString(),nPassword.getText().toString());
        jsonCtgHolderApi = retrofit.create(JsonCtgHolderApi.class);
        Call call = jsonCtgHolderApi.getCtgrs("http://"+IP_ADD+":8080/WebService/webapi/category_service/categories");
        call.enqueue(new Callback<List<Categories>>() {
            @Override
            public void onResponse(Call<List<Categories>> call, Response<List<Categories>> response)
            {
                List<Categories> categories = response.body();
                List<String> ctgrsName= new ArrayList<String>();
                for(int i=0;i<categories.size();i++)
                    ctgrsName.add(categories.get(i).getName());
                selectCtgrsType.setItems(ctgrsName);
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }
    private Retrofit retrofitInit(String url)
    {
        Gson gson = new GsonBuilder().serializeNulls().create();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    public boolean validate()
    {
        if(nFname.getText().toString().equals("")||nLname.getText().equals("")||nDOB.getText().equals("")||nEmail.getText().equals("")||nPassword.getText().equals("")||nUsername.getText().equals(""))
        {
            return false;
        }
        return true;
    }
    public void createUser() throws ExecutionException, InterruptedException {
        String answerFromCeration  = new BackgroundTaskPost().execute().get();
    }
    class BackgroundTaskPost extends AsyncTask<Void, Void, String> {
        String json_url;
        @Override
        protected void onPreExecute() {
            json_url = "http://"+IP_ADD+":8080/WebService/webapi/tourist_service/tourist";
        }
        @Override
        protected String doInBackground(Void... voids) {
            try {
                try {
                    URL url = new URL(json_url);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setDoOutput(true);
                    conn.setRequestProperty ("Authorization", Utils.BasicAuthorizationEncoder(nUsername.getText().toString(),nPassword.getText().toString()));
                    conn.setDoInput(true);
                    usersEntity.addUserEntity addu = new usersEntity.addUserEntity();
                    addu.setDOB(nDOB.getText().toString());
                    addu.setEmail(nEmail.getText().toString());
                    addu.setfName(nFname.getText().toString());
                    addu.setlName(nLname.getText().toString());
                    addu.setUserName(nUsername.getText().toString());
                    addu.setPassword(nPassword.getText().toString());
                    addu.setMoreInfo(cbMoreInfo.isChecked());
                    addu.setAge(age.getText().toString());
                    addu.setGender(selectGenderType.toString());
                    addu.setSpokenLen(selectSpokenLanguage.toString());
                    addu.setVicType(selectVicType.toString());
                    addu.setMeetPeople(nMeetPeople.isChecked());
                    addu.setRadiusThreshold(nradiusTH.getText().toString());
                    addu.setType("Tourist");
                    addu.setCtgrsTypes(selectCtgrsType.getSelectedItemsAsString());
                    jsonAdd.put("email", addu.getEmail());
                    jsonAdd.put("firstName",addu.getfName());
                    jsonAdd.put("lastName",addu.getlName());
                    jsonAdd.put("loginName",addu.getUserName());
                    jsonAdd.put("dateOfBirthStr",addu.getDOB());
                    jsonAdd.put("password",addu.getPassword());
                    jsonAdd.put("type", addu.getType());
                    jsonAdd.put("moreInformation",addu.isMoreInfo());
                    jsonAdd.put("preferences",addu.getCtgrsTypes());
                    if(!addu.isMoreInfo())
                    {
                        jsonAdd.put("age", 0);
                        jsonAdd.put("gender", "Hidden");
                        jsonAdd.put("spokenLanguages", "Hidden");
                        jsonAdd.put("vicationType", "Hidden");
                        jsonAdd.put("meetPeople", "false");
                        jsonAdd.put("radiusThreshold", 5.0);
                        jsonAdd.put("preferences","Hidden");
                        setPostRequestContent(conn, jsonAdd);
                        conn.connect();
                        responseResult = conn.getResponseMessage();
                        if((conn.getResponseMessage()).equals("Precondition Failed"))
                        {
                            responseResult = "412";
                            conn.disconnect();
                        }
                        else
                        {
                            responseResult = "200";
                        }
                        return responseResult;
                    }
                    else {
                        jsonAdd.put("age", addu.getAge());
                        jsonAdd.put("gender", addu.getGender());
                        jsonAdd.put("spokenLanguages", addu.getSpokenLen());
                        jsonAdd.put("vicationType", addu.getVicType());
                        jsonAdd.put("meetPeople", addu.isMeetPeople());
                        jsonAdd.put("radiusThreshold", addu.getRadiusThreshold());
                        jsonAdd.put("preferences",addu.getCtgrsTypes());
                        setPostRequestContent(conn, jsonAdd);
                        //
//                        Intent intent = new Intent(AddNewTourist.this,org.ruppin.roper.login_page.loginPage.class);
//                        intent.putExtra("TOURIST_OBJECT", (Parcelable) jsonAdd);
                        //
                        conn.connect();
                        responseResult = conn.getResponseMessage();
                        if ((conn.getResponseMessage()).equals("Precondition Failed")) {
                            responseResult = "412";
                            conn.disconnect();
                        } else {
                            responseResult = "200";
                        }
                        return conn.getResponseMessage() + "";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }


        private void setPostRequestContent(HttpURLConnection conn,
                                           JSONObject jsonObject) throws IOException {

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();
            os.close();
        }

        @Override
        protected void onProgressUpdate(Void... values)
        {
            super.onProgressUpdate(values);
        }


    }
}
