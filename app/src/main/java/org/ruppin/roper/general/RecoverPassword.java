package org.ruppin.roper.general;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.ruppin.roper.users.parseUsers;
import org.ruppin.roper.users.usersEntity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.ruppin.roper.R;

public class RecoverPassword extends AppCompatActivity {

    private String IP_ADD;

    private TextView recover1;
    private EditText nFname;
    private EditText nLname;
    private EditText nUsername;
    private EditText nPassword;
    private EditText nDOB;
    private EditText nEmail;
    private String responseText;
    JSONObject jsonRecoverPassword = new JSONObject();
    parseUsers pu = new parseUsers();
    List<usersEntity> usersToPass = new ArrayList<>();
    private  boolean noContentRes=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);
        nFname = (EditText) findViewById(R.id.firstName);
        nLname = (EditText) findViewById(R.id.lastName);
        nDOB = (EditText) findViewById(R.id.DOB);
        nEmail = (EditText) findViewById(R.id.email);
        nPassword = (EditText) findViewById(R.id.password);
        nUsername = (EditText) findViewById(R.id.userName);
        recover1 = (TextView) findViewById(R.id.recover);

        IP_ADD = getResources().getString(R.string.IP);

        recover1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(validate()!=false) {
                recoverPassword();
                if(usersToPass.size()!=0)
                {
                    Context context = getApplicationContext();
                    CharSequence text = "recovered!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else
                {
                    Context context = getApplicationContext();
                    CharSequence text = "incorrect info!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
            else if(validate()==false)
            {
                Context context = getApplicationContext();
                CharSequence text = "field is missing!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    });
}

    public List<usersEntity> parseUsers(String s) {
        try {
            List<usersEntity> users = new ArrayList<>();
            usersEntity userToAdd = new usersEntity();
            JSONObject jsnObj = new JSONObject(s);
            String password = jsnObj.getString("password");
            String user = jsnObj.getString("loginName");
            userToAdd.setPassword(password);
            userToAdd.setUsername(user);
            users.add(userToAdd);
            pu.setListOfUsers(users);
            return pu.getListOfUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean validate()
    {
        if(nFname.getText().toString().equals("")||nLname.getText().equals("")||nDOB.getText().equals("")||nEmail.getText().equals("")||nUsername.getText().equals(""))
        {
            return false;
        }
        return true;
    }
    public void recoverPassword()
    {

        try {
            String s = new RecoverPassword.BackgroundTaskRecoverPassword().execute().get();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    class BackgroundTaskRecoverPassword extends AsyncTask<Void, Void, String> {
        String json_url;
        @Override
        protected void onPreExecute() {
            json_url = "http://"+IP_ADD+":8080/WebService/webapi/person_service/password";
        }
        @Override
        protected String doInBackground(Void... voids) {
            try {
                try {
                    URL url = new URL(json_url);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("PUT");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    usersEntity.addUserEntity addu = new usersEntity.addUserEntity();
                    addu.setDOB(nDOB.getText().toString());
                    addu.setEmail(nEmail.getText().toString());
                    addu.setfName(nFname.getText().toString());
                    addu.setlName(nLname.getText().toString());
                    addu.setUserName(nUsername.getText().toString());
                    jsonRecoverPassword.put("email", addu.getEmail());
                    jsonRecoverPassword.put("firstName",addu.getfName());
                    jsonRecoverPassword.put("lastName",addu.getlName());
                    jsonRecoverPassword.put("loginName",addu.getUserName());
                    jsonRecoverPassword.put("dateOfBirthStr",addu.getDOB());
                    setGetRequestContent(conn, jsonRecoverPassword);
                    conn.connect();
                    if(conn.getResponseMessage().equals("OK"))
                    {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line+"\n");
                        }
                        br.close();
                        responseText = sb.toString();
                        usersToPass = parseUsers(responseText);
                        if (usersToPass.size() != 0)
                        {
                            for (int i = 0; i < usersToPass.size(); i++)
                            {
                                nPassword.setText(usersToPass.get(i).getPassword().toString());
                            }
                        }
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


        private void setGetRequestContent(HttpURLConnection conn,
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