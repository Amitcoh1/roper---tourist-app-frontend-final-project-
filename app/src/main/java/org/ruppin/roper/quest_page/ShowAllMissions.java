package org.ruppin.roper.quest_page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ruppin.roper.R;
import org.ruppin.roper.Utils.Utils;
import org.ruppin.roper.tourist_page.Models.Quest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ShowAllMissions extends AppCompatActivity  {
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
        setContentView(R.layout.show_all_missions);
        this.activity = this;
        IP_ADD = getResources().getString(R.string.IP);
        showAll = (Spinner) findViewById(R.id.showAllQMissionsSpinner);
        next = (Button)findViewById(R.id.moveToMissionSelected);
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
                        Retrofit retrofit  = Utils.retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"),userName,userPass);
                        jsonQuestPlaceHolder  = retrofit.create(JsonQuestPlaceHolder.class);
                        Call call = jsonQuestPlaceHolder.getQuest("http://"+IP_ADD+":8080/WebService/webapi/mission_service/mission?name="+showAll.getSelectedItem().toString());
                        call.enqueue(new Callback<Quest>() {
                            @Override
                            public void onResponse(Call<Quest> call, Response<Quest> response) {
                                Intent intent = new Intent(ShowAllMissions.this, EditMission.class);
                                intent.putExtra("USER_NAME",userName);
                                intent.putExtra("PASSWORD",userPass);
                                intent.putExtra("SELECTION",showAll.getSelectedItem().toString());
                                intent.putExtra("SELECTION_ID",response.body().getId().toString());
                                activity.startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {

                            }
                        });
                    }
                });
            }
    }

