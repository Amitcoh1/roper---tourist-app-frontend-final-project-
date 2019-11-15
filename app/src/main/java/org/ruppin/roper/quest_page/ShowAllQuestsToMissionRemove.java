package org.ruppin.roper.quest_page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.ruppin.roper.R;
import org.ruppin.roper.Utils.Utils;
import org.ruppin.roper.tourist_page.Models.MissionNoBS;
import org.ruppin.roper.tourist_page.Models.Quest;
import org.ruppin.roper.tourist_page.Models.questNoBS;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ShowAllQuestsToMissionRemove extends AppCompatActivity {
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
        setContentView(R.layout.show_all_quests4);
        this.activity = this;
        IP_ADD = getResources().getString(R.string.IP);
        showAll = (Spinner) findViewById(R.id.showAllQuestsSpinner4);
        next = (Button)findViewById(R.id.moveToNextPage4);
        String userName = getIntent().getStringExtra("USER_NAME").toString();
        String userPass = getIntent().getStringExtra("PASSWORD").toString();
        Retrofit retrofit = Utils.retrofitInit(String.format("http://" + IP_ADD + ":8080/WebService/webapi/"), userName, userPass);
        jsonQuestPlaceHolder = retrofit.create(JsonQuestPlaceHolder.class);
        Call call = jsonQuestPlaceHolder.getAllQuests("http://" + IP_ADD + ":8080/WebService/webapi/quest_service/quests");
        call.enqueue(new Callback<ArrayList<Quest>>() {
            @Override
            public void onResponse(Call<ArrayList<Quest>> call, Response<ArrayList<Quest>> response) {
                ArrayList<String> questsNamesArray = new ArrayList<String>();
                ArrayList<String> questsIdsArray = new ArrayList<String>();
                ArrayList<Quest> quests = response.body();
                for(int i=0;i<quests.size();i++)
                {
                    questsNamesArray.add(quests.get(i).getName().toString());
                    questsIdsArray.add(quests.get(i).getId().toString());
                }
                ArrayAdapter<String> adapterNames = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item,questsNamesArray);
                showAll.setAdapter(adapterNames);
                //
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //
                        Retrofit retrofit  = Utils.retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"),userName,userPass);
                        jsonQuestPlaceHolder  = retrofit.create(JsonQuestPlaceHolder.class);
                        Call call = jsonQuestPlaceHolder.getQuestNoBS("http://"+IP_ADD+":8080/WebService/webapi/quest_service/quest?name="+showAll.getSelectedItem().toString());
                        call.enqueue(new Callback<questNoBS>() {
                            @Override
                            public void onResponse(Call<questNoBS> call, Response<questNoBS> response) {
                                questNoBS quest = response.body();
                                ArrayList<MissionNoBS> missions = new ArrayList<MissionNoBS>();
                                missions = quest.getMissions();
                                Intent intent = new Intent(ShowAllQuestsToMissionRemove.this, ShowAllMissionsToRemove.class);
                                intent.putExtra("USER_NAME",userName);
                                intent.putExtra("PASSWORD",userPass);
                                intent.putExtra("SELECTION",showAll.getSelectedItem().toString());
                                intent.putExtra("SELECTION_ID",response.body().getId().toString());
                                intent.putExtra("MISSIONS", missions.toString());
                                activity.startActivity(intent);
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
