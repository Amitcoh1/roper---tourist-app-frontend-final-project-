//package org.ruppin.roper.admin_page;
//
//import android.content.Context;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.CheckBox;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import org.json.JSONObject;
//import org.ruppin.roper.users.usersEntity;
//
//import java.io.BufferedWriter;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.io.OutputStreamWriter;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.concurrent.ExecutionException;
//
//import org.ruppin.roper.R;
//
//public class AddNewAdmin extends AppCompatActivity {
//
//    private String IP_ADD;
//
//    private TextView register1;
//    private TextView register2;
//    private EditText nFname;
//    private EditText nLname;
//    private EditText nUsername;
//    private EditText nPassword;
//    private EditText nDOB;
//    private EditText nEmail;
//    private Spinner nType;
//    private CheckBox cbMoreInfo;
//    private String selectedItemType;
//    private String responseResult;
//    private int counter = 1;
//    JSONObject jsonAdd = new JSONObject();
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register_admin);
//        nFname = (EditText) findViewById(R.id.firstName);
//        nLname = (EditText) findViewById(R.id.lastName);
//        nDOB = (EditText) findViewById(R.id.DOB);
//        nEmail = (EditText) findViewById(R.id.email);
//        nPassword = (EditText) findViewById(R.id.password);
//        nUsername = (EditText) findViewById(R.id.userName);
//        register1 = (TextView) findViewById(R.id.register);
//        register2 = (TextView) findViewById(R.id.register);
//        register1.setText("Register");
//
//         IP_ADD = getResources().getString(R.string.IP);
//
//        register1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(validate()!=false) {
//                    try {
//                        createUser();
//                    } catch (ExecutionException e) {
//                        e.printStackTrace();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    if(responseResult.equals("412"))
//                    {
//                        Context context = getApplicationContext();
//                        CharSequence text = "fields must be unique!";
//                        int duration = Toast.LENGTH_SHORT;
//                        Toast toast = Toast.makeText(context, text, duration);
//                        toast.show();
//                    }
//                    else {
//                        Context context = getApplicationContext();
//                        CharSequence text = "user created!";
//                        int duration = Toast.LENGTH_SHORT;
//                        Toast toast = Toast.makeText(context, text, duration);
//                        toast.show();
//                        register1.setText("");
//                        register1.setText("Login Page");
//                        register2.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
////                                Intent intent = new Intent(AddNewAdmin.this, MainActivity.class);
////                                startActivity(intent);
//                            }
//                        });
//                    }
//                }
//                else if(!register1.getText().toString().equals("Login Page"))
//                {
//                    Context context = getApplicationContext();
//                    CharSequence text = "field is missing!";
//                    int duration = Toast.LENGTH_SHORT;
//                    Toast toast = Toast.makeText(context, text, duration);
//                    toast.show();
//                }
//            }
//        });
//
//    }
//    public boolean validate()
//    {
//        if(nFname.getText().toString().equals("")||nLname.getText().equals("")||nDOB.getText().equals("")||nEmail.getText().equals("")||nPassword.getText().equals("")||nUsername.getText().equals(""))
//        {
//            return false;
//        }
//        return true;
//    }
//    public void createUser() throws ExecutionException, InterruptedException {
//
//        String answerFromCeration  = new BackgroundTaskPost().execute().get();
//    }
//    class BackgroundTaskPost extends AsyncTask<Void, Void, String> {
//        String json_url;
//        @Override
//        protected void onPreExecute() {
//            json_url = "http://"+IP_ADD+":8080/WebService/webapi/PersonService/user";
//        }
//        @Override
//        protected String doInBackground(Void... voids) {
//            try {
//                try {
//                    URL url = new URL(json_url);
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                    conn.setRequestMethod("POST");
//                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
//                    conn.setDoOutput(true);
//                    conn.setDoInput(true);
//                    usersEntity.addUserEntity addu = new usersEntity.addUserEntity();
//                    addu.setDOB(nDOB.getText().toString());
//                    addu.setEmail(nEmail.getText().toString());
//                    addu.setfName(nFname.getText().toString());
//                    addu.setlName(nLname.getText().toString());
//                    addu.setUserName(nUsername.getText().toString());
//                    addu.setPassword(nPassword.getText().toString());
//                    addu.setType("Admin");
//                    jsonAdd.put("email", addu.getEmail());
//                    jsonAdd.put("firstName",addu.getfName());
//                    jsonAdd.put("lastName",addu.getlName());
//                    jsonAdd.put("loginName",addu.getUserName());
//                    jsonAdd.put("dateOfBirthStr",addu.getDOB());
//                    jsonAdd.put("password",addu.getPassword());
//                    jsonAdd.put("type",addu.getType());
//                    setPostRequestContent(conn, jsonAdd);
//                    conn.connect();
//                    responseResult = conn.getResponseMessage();
//                    if((conn.getResponseMessage()).equals("Precondition Failed"))
//                    {
//                        responseResult = "412";
//                        conn.disconnect();
//                    }
//                    else
//                    {
//                        responseResult = "200";
//                    }
//                    return conn.getResponseMessage()+"";
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            } catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//
//        private void setPostRequestContent(HttpURLConnection conn,
//                                           JSONObject jsonObject) throws IOException {
//
//            OutputStream os = conn.getOutputStream();
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
//            writer.write(jsonObject.toString());
////            Log.i(MainActivity.class.toString(), jsonObject.toString());
//            writer.flush();
//            writer.close();
//            os.close();
//        }
//
//        @Override
//        protected void onProgressUpdate(Void... values)
//        {
//            super.onProgressUpdate(values);
//        }
//    }
//}
