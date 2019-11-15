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
import org.ruppin.roper.tourist_page.Models.MissionNoBS;
import org.ruppin.roper.tourist_page.Models.Quest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ShowAllMissionsToRemove extends AppCompatActivity  {
    private Activity activity;
    private Spinner showAll;
    private Button next;
    private String IP_ADD;
    private JsonQuestPlaceHolder jsonQuestPlaceHolder;
    private JsonQuestPlaceHolder jsonQuestPlaceHolder2;
    private String selection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_all_missions_to_remove);
        this.activity = this;
        IP_ADD = getResources().getString(R.string.IP);
        showAll = (Spinner) findViewById(R.id.showAllQMissionsSpinnerToRemove);
        next = (Button)findViewById(R.id.moveToMissionSelectedToRemove);
        String userName = getIntent().getStringExtra("USER_NAME").toString();
        String userPass = getIntent().getStringExtra("PASSWORD").toString();
        String missionsAsString = getIntent().getStringExtra("MISSIONS");
        ArrayList<String> namesOfMissions = new ArrayList<String>();
        try {
            JSONArray missionsArr = new JSONArray(missionsAsString);
            for (int i = 0; i < missionsArr.length(); i++)
            {
                JSONObject jsonObj = missionsArr.getJSONObject(i);
                namesOfMissions.add(jsonObj.get("name").toString());
            }
            ArrayAdapter<String> adapterNames = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item,namesOfMissions);
            showAll.setAdapter(adapterNames);
        } catch (JSONException e) {
            e.printStackTrace();
        }
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //
                        Retrofit retrofit1  = Utils.retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"),userName,userPass);
                        jsonQuestPlaceHolder  = retrofit1.create(JsonQuestPlaceHolder.class);
                        Call call2 = jsonQuestPlaceHolder.getMission("http://"+IP_ADD+":8080/WebService/webapi/mission_service/mission?name=" + showAll.getSelectedItem().toString());
                        call2.enqueue(new Callback<MissionNoBS>() {
                            @Override
                            public void onResponse(Call<MissionNoBS> call, Response<MissionNoBS> response) {
                                String id = response.body().getId().toString();
                                String s = "{" + "\"id\":" + id + "}";

                                    MissionNoBS missionNoBS = new MissionNoBS(id);
                                    Retrofit retrofit  = Utils.retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"),userName,userPass);
                                    jsonQuestPlaceHolder  = retrofit.create(JsonQuestPlaceHolder.class);
                                    Call call1 = jsonQuestPlaceHolder.removeMission("http://"+IP_ADD+":8080/WebService/webapi/mission_service/mission",missionNoBS);
                                    call1.enqueue(new Callback<Quest>() {
                                        @Override
                                        public void onResponse(Call<Quest> call, Response<Quest> response) {
                                            Context context = getApplicationContext();
                                            CharSequence text = "info has been updated.";
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
    }

