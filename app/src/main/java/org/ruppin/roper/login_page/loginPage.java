package org.ruppin.roper.login_page;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;
import org.ruppin.roper.Utils.Utils;
import org.ruppin.roper.admin_page.AdminMainPage;
import org.ruppin.roper.general.RecoverPassword;
import org.ruppin.roper.tourist_page.Models.Tourist;
import org.ruppin.roper.tourist_page.Utils.JsonTouristHolder;
import org.ruppin.roper.users.SelectUserType;
import org.ruppin.roper.users.parseUsers;
import org.ruppin.roper.users.usersEntity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import org.ruppin.roper.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class loginPage extends AppCompatActivity {
    private EditText name;
    private EditText password;
    private TextView login;
    private EditText nFname;
    private EditText nLname;
    private EditText nUsername;
    private EditText nPassword;
    private EditText nDOB;
    private EditText nEmail;
    private TextView register;
    private TextView recoverPassword;
    private Boolean bool = false;
    public String json_string;
    private TextView nameOfUser;
    private String touristString;

    private String IP_ADD;
    parseUsers pu = new parseUsers();
    List<usersEntity> usersToPass = new ArrayList<>();
    HttpClient client;
    HttpResponse response;
    JSONObject jsonAdd = new JSONObject();
    String typeToInitPage;
    private JsonTouristHolder jsonTouristHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        name = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        login = (TextView) findViewById(R.id.login);
        nFname = (EditText) findViewById(R.id.firstName);
        nLname = (EditText) findViewById(R.id.lastName);
        nDOB = (EditText) findViewById(R.id.DOB);
        nEmail = (EditText) findViewById(R.id.email);
        nPassword = (EditText) findViewById(R.id.password);
        nUsername = (EditText) findViewById(R.id.userName);
        register = (TextView) findViewById(R.id.registerMain);
        recoverPassword = (TextView) findViewById(R.id.forgotPassword);

        IP_ADD = getResources().getString(R.string.IP);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                bool = validate(name.getText().toString(), password.getText().toString());
                if (bool == false) {
                    Context context = getApplicationContext();
                    CharSequence text = "wrong credentials,please try again";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    toast.show();
                } else
                {
                    if(typeToInitPage.equals("Tourist"))
                    {
                       // Retrofit retrofit  = retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"));
                        String n = nUsername.getText().toString();
                        String p = nPassword.getText().toString();
                        Retrofit retrofit  = Utils.retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"), n, p);
                        jsonTouristHolder = retrofit.create(JsonTouristHolder.class);
                        Bundle extras = getIntent().getExtras();
                        Call call = jsonTouristHolder.getTourist("http://"+IP_ADD+":8080/WebService/webapi/tourist_service/tourist?loginName="+n);
                        call.enqueue(new Callback<Tourist>() {
                            @Override
                            public void onResponse(Call<Tourist> call, Response<Tourist> response)
                            {
                                Tourist tourist = response.body();
                                Gson touristJson = new Gson();
                                touristString = touristJson.toJson(tourist);
                                Intent intent = new Intent(loginPage.this,org.ruppin.roper.tourist_page.MapsActivity.class);
                                intent.putExtra("TOURIST",touristString);
                                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {
                                Log.e("tourist error",t.toString());
                            }
                        });
                    }
                    if(typeToInitPage.equals("BusinessOwner"))
                    {
                        Intent intent = new Intent(loginPage.this,org.ruppin.roper.bussines_owner_page.businessOwnerMainPage.class);
                        intent.putExtra("USER_NAME", name.getText().toString());
                        intent.putExtra("PASSWORD",password.getText().toString());
                        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                        startActivity(intent);
                    }
                    if(typeToInitPage.equals("Admin"))
                    {
                        Intent intent = new Intent(loginPage.this, AdminMainPage.class);
                        intent.putExtra("USER_NAME", name.getText().toString());
                        intent.putExtra("PASSWORD",password.getText().toString());
                        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                        startActivity(intent);
                    }
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginPage.this, SelectUserType.class);
                startActivity(intent);
            }
        });

        recoverPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginPage.this, RecoverPassword.class);
                startActivity(intent);
            }
        });
    }


    String getName()
    {
        return name.getText().toString();
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String json_url;
        String STRING_JSON;

        @Override
        protected void onPreExecute() {
            json_url = "http://"+ IP_ADD+":8080/WebService/" +
                    "webapi/person_service/" +
                    "login?loginName=" + name.getText();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(json_url);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestProperty ("Authorization", Utils.BasicAuthorizationEncoder(nUsername.getText().toString(), nPassword.getText().toString()));
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while ((STRING_JSON = bufferedReader.readLine()) != null) {
                    stringBuilder.append(STRING_JSON + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                json_string = stringBuilder.toString().trim();
                if (json_string != "") {
                    usersToPass = parseUsers(json_string);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    public List<usersEntity> parseUsers(String s) {
        try {
            List<usersEntity> users = new ArrayList<>();
            usersEntity userToAdd = new usersEntity();
            JSONObject jsnObj = new JSONObject(s);
            String password = jsnObj.getString("password");
            String user = jsnObj.getString("loginName");
            typeToInitPage = jsnObj.getString("type");
            if(typeToInitPage.equalsIgnoreCase("Business owner"))
                typeToInitPage = "BusinessOwner";
            userToAdd.setPassword(password);
            userToAdd.setUsername(user);
            userToAdd.setType(typeToInitPage);
            users.add(userToAdd);
            pu.setListOfUsers(users);
            return pu.getListOfUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean validate(String userName, String userPassword)
    {
        try {
            String result = new BackgroundTask().execute().get();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if (usersToPass.size() != 0)
        {
            for (int i = 0; i < usersToPass.size(); i++)
            {
                if ((usersToPass.get(i).getUsername().equals(name.getText().toString())) && (usersToPass.get(i).getPassword().equals(password.getText().toString())))
                {
                    return true;
                }

            }

        }
        return false;
    }

    class BackgroundTaskPost extends AsyncTask<Void, Void, String> {
        String json_url;
        @Override
        protected void onPreExecute() {
            json_url = "http://"+IP_ADD+":8080/WebService/webapi/person_service/user";
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(Void... voids) {
            try {
                try {
                    URL url = new URL(json_url);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Authorization", Utils.BasicAuthorizationEncoder(nUsername.getText().toString(), nPassword.getText().toString()));
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    usersEntity.addUserEntity addu = new usersEntity.addUserEntity();
                    addu.setDOB(nDOB.getText().toString());
                    addu.setEmail(nEmail.getText().toString());
                    addu.setfName(nFname.getText().toString());
                    addu.setlName(nLname.getText().toString());
                    addu.setUserName(nUsername.getText().toString());
                    addu.setPassword(nPassword.getText().toString());
                    jsonAdd.put("email", addu.getEmail());
                    jsonAdd.put("firstName",addu.getfName());
                    jsonAdd.put("lastName",addu.getlName());
                    jsonAdd.put("loginName",addu.getUserName());
                    jsonAdd.put("dateOfBirthStr",addu.getDOB());
                    jsonAdd.put("password",addu.getPassword());
                    setPostRequestContent(conn, jsonAdd);
                    conn.connect();
                    return conn.getResponseMessage()+"";
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
            Log.i(loginPage.class.toString(), jsonObject.toString());
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
    public void createUser()
    {
        new BackgroundTaskPost().execute();
    }
    @Override

    public void onBackPressed() { }
//    private Retrofit retrofitInit(String url)
//    {
//        Gson gson = new GsonBuilder().serializeNulls().create();
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(loggingInterceptor)
//                .build();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(url)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .client(okHttpClient)
//                .build();
//        return retrofit;
//    }

}
