package org.ruppin.roper.quest_page;

import android.app.Activity;
import android.content.Context;
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
import org.ruppin.roper.bussines_owner_page.Models.BusinessBO;
import org.ruppin.roper.bussines_owner_page.Utils.JsonBusinessOwnerHolder;
import org.ruppin.roper.tourist_page.Models.Mission;
import org.ruppin.roper.tourist_page.Models.MissionNoBS;
import org.ruppin.roper.tourist_page.Models.Quest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ShowAllMissionsToAddBsns extends AppCompatActivity {
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
        setContentView(R.layout.activity_add_business_to_mission);
        this.activity = this;
        IP_ADD = getResources().getString(R.string.IP);
        showAllMissions = (Spinner) findViewById(R.id.showAllQmsnsSpinner);
        showAllBsns = (Spinner) findViewById(R.id.showAllQBsns111Spinner);
        next = (Button)findViewById(R.id.addedB2M);
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
        Retrofit retrofit = Utils.retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"),userName,userPass);
        jsonBusinessOwnerHolder  = retrofit.create(JsonBusinessOwnerHolder.class);
        Call call2 = jsonBusinessOwnerHolder.getBussinesses("http://"+IP_ADD+":8080/WebService/webapi/business_service/businesses");
        call2.enqueue(new Callback<ArrayList<BusinessBO>>() {
            @Override
            public void onResponse(Call<ArrayList<BusinessBO>> call, Response<ArrayList<BusinessBO>> response) {
                ArrayList<String> namesOfBsnses = new ArrayList<String>();
                ArrayList<BusinessBO> bsnsFromRes = response.body();
                Map<String, String> map = new HashMap<>();

                    for (int i = 0; i < bsnsFromRes.size(); i++)
                    {
                        namesOfBsnses.add(bsnsFromRes.get(i).getName().toString());
                        StringBuilder data = new StringBuilder();
                        data.append(bsnsFromRes.get(i).getId().toString()).append(",").append(bsnsFromRes.get(i).getLongitude().toString()).append(",").append(bsnsFromRes.get(i).getLatitude().toString());

                        map.put(bsnsFromRes.get(i).getName().toString(),data.toString());
                    }

                    ArrayAdapter<String> adapterNames1 = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item,namesOfBsnses);
                    showAllBsns.setAdapter(adapterNames1);

                //
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Retrofit retrofit22  = Utils.retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"),userName,userPass);
                        jsonBusinessOwnerHolder  = retrofit22.create(JsonBusinessOwnerHolder.class);

                        String name = map.get(showAllBsns.getSelectedItem().toString()).split(",")[0].toString();
                        String longi = map.get(showAllBsns.getSelectedItem().toString()).split(",")[1].toString();
                        String lat = map.get(showAllBsns.getSelectedItem().toString()).split(",")[2].toString();

                        Call call23 = jsonBusinessOwnerHolder.
                                getBsns("http://"+IP_ADD+":8080/WebService/webapi" +
                                        "/business_service/business?name="+ showAllBsns.getSelectedItem().toString() +
                                        "&latitude=" + lat +
                                        "&longitude=" +longi );
                        call23.enqueue(new Callback<BusinessBO>() {
                            @Override
                            public void onResponse(Call<BusinessBO> call, Response<BusinessBO> response) {

                                String d = response.body().getId();


                                        Retrofit retrofit1  = Utils.retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"),userName,userPass);
                                        jsonQuestPlaceHolder  = retrofit1.create(JsonQuestPlaceHolder.class);
                                        Call call2222 = jsonQuestPlaceHolder.getMission("http://"+IP_ADD+":8080/WebService/webapi/mission_service/mission?name=" + showAllMissions.getSelectedItem().toString());
                                        call2222.enqueue(new Callback<Mission>() {
                                            @Override
                                            public void onResponse(Call<Mission> call, Response<Mission> response) {
                                                String id = response.body().getId().toString();
                                                String s = "{" + "\"id\":" + id + "}";

                                                MissionNoBS missionNoBS = new MissionNoBS(id);

                                                Retrofit retrofit1  = Utils.retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"),userName,userPass);
                                                jsonQuestPlaceHolder  = retrofit1.create(JsonQuestPlaceHolder.class);
                                                Call call2 = jsonQuestPlaceHolder.addMissionToBsns("http://"+IP_ADD+":8080/WebService/webapi/mission_service/business?id=" + d,missionNoBS);
                                                call2.enqueue(new Callback<MissionNoBS>() {
                                                    @Override
                                                    public void onResponse(Call<MissionNoBS> call, Response<MissionNoBS> response) {
                                                        Context context = getApplicationContext();
                                                        CharSequence text = "bussiness has been added.";
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

                            @Override
                            public void onFailure(Call call, Throwable t) {

                            }
                        });




                    }
                });
            }

            @Override
            public void onFailure(Call call, Throwable t) {
            }
        });
    }
}
