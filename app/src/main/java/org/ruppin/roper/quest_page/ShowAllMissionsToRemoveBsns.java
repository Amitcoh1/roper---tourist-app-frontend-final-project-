package org.ruppin.roper.quest_page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ruppin.roper.R;
import org.ruppin.roper.Utils.Utils;
import org.ruppin.roper.admin_page.AdminEditDefaultData;
import org.ruppin.roper.admin_page.AdminMainPage;
import org.ruppin.roper.bussines_owner_page.Models.BusinessBO;
import org.ruppin.roper.bussines_owner_page.Utils.JsonBusinessOwnerHolder;
import org.ruppin.roper.tourist_page.Models.Business;
import org.ruppin.roper.tourist_page.Models.Mission;
import org.ruppin.roper.tourist_page.Models.MissionNoBS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ShowAllMissionsToRemoveBsns extends AppCompatActivity
{
    private Activity activity;
    private Spinner showAllMissions;
    private Spinner showAllBsns;
    private Button next;
    private String IP_ADD;
    private JsonQuestPlaceHolder jsonQuestPlaceHolder;
    private JsonBusinessOwnerHolder jsonBusinessOwnerHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_all_missions5);
        this.activity = this;
        IP_ADD = getResources().getString(R.string.IP);
        showAllMissions = (Spinner) findViewById(R.id.showAllQmsnsSpinner1232);
        showAllBsns = (Spinner) findViewById(R.id.showAllQBsnAmitSpinner);
        next = (Button)findViewById(R.id.rm);
        String userName = getIntent().getStringExtra("USER_NAME").toString();
        String userPass = getIntent().getStringExtra("PASSWORD").toString();
        String missionsAsString = getIntent().getStringExtra("MISSIONS");
        ArrayList<String> namesOfMissions = new ArrayList<String>();
        ArrayList<String> ids = new ArrayList<String>();
        ArrayList<String> longi = new ArrayList<String>();
        ArrayList<String> lat = new ArrayList<String>();
        try {
            JSONArray missionsArr = new JSONArray(missionsAsString);
            for (int i = 0; i < missionsArr.length(); i++)
            {
                JSONObject jsonObj = missionsArr.getJSONObject(i);
                namesOfMissions.add(jsonObj.get("name").toString());
            }

            ArrayAdapter<String> adapterNames = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item,namesOfMissions);
            showAllMissions.setAdapter(adapterNames);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //
        Retrofit retrofit1  = Utils.retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"),userName,userPass);
        jsonQuestPlaceHolder  = retrofit1.create(JsonQuestPlaceHolder.class);
        Call call2 = jsonQuestPlaceHolder.getMission("http://"+IP_ADD+":8080/WebService/webapi/mission_service/mission?name=" + showAllMissions.getSelectedItem().toString());
        call2.enqueue(new Callback<Mission>() {
            @Override
            public void onResponse(Call<Mission> call, Response<Mission> response) {
                if(response.body().getBusiness() != null)
                {
                String name = response.body().getBusiness().getName().toString();
                String id1 = response.body().getBusiness().getId().toString();
                ArrayList<String> array = new ArrayList<String>();
                array.add(name);
                ArrayAdapter<String> adapterNames1 = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, array);
                showAllBsns.setAdapter(adapterNames1);

                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Retrofit retrofit22 = Utils.retrofitInit(String.format("http://" + IP_ADD + ":8080/WebService/webapi/"), userName, userPass);
                        jsonBusinessOwnerHolder = retrofit22.create(JsonBusinessOwnerHolder.class);


                        Retrofit retrofit1 = Utils.retrofitInit(String.format("http://" + IP_ADD + ":8080/WebService/webapi/"), userName, userPass);
                        jsonQuestPlaceHolder = retrofit1.create(JsonQuestPlaceHolder.class);
                        Call call2222 = jsonQuestPlaceHolder.getMission("http://" + IP_ADD + ":8080/WebService/webapi/mission_service/mission?name=" + showAllMissions.getSelectedItem().toString());
                        call2222.enqueue(new Callback<Mission>() {
                            @Override
                            public void onResponse(Call<Mission> call, Response<Mission> response) {
                                String id = response.body().getId().toString();
                                String s = "{" + "\"id\":" + id + "}";


                                MissionNoBS missionNoBS = new MissionNoBS(id);

                                Retrofit retrofit1 = Utils.retrofitInit(String.format("http://" + IP_ADD + ":8080/WebService/webapi/"), userName, userPass);
                                jsonQuestPlaceHolder = retrofit1.create(JsonQuestPlaceHolder.class);
                                Call call2 = jsonQuestPlaceHolder.removeBsns("http://" + IP_ADD + ":8080/WebService/webapi/mission_service/business?id=" + id1, missionNoBS);
                                call2.enqueue(new Callback<MissionNoBS>() {
                                    @Override
                                    public void onResponse(Call<MissionNoBS> call, Response<MissionNoBS> response) {
                                        Context context = getApplicationContext();
                                        CharSequence text = "bussiness has been removed.";
                                        int duration = Toast.LENGTH_SHORT;
                                        Toast toast = Toast.makeText(context, text, duration);
                                        toast.show();
                                    }

                                    @Override
                                    public void onFailure(Call call, Throwable t) {

                                    }
                                });


                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {

                            }
                        });


                    }
                });
            }
            else
                {
                    Context context = getApplicationContext();
                    CharSequence text = "no businesses to show..";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    Intent intent = new Intent(ShowAllMissionsToRemoveBsns.this, ShowAllQuestsToRemoveBsns.class);
                    intent.putExtra("USER_NAME",userName);
                    intent.putExtra("PASSWORD",userPass);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
        //

            }


}
