package org.ruppin.roper.quest_page;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.builder.Builders;

import org.ruppin.roper.R;
import org.ruppin.roper.Utils.Utils;
import org.ruppin.roper.tourist_page.Models.Mission;
import org.ruppin.roper.tourist_page.Models.Quest;
import org.ruppin.roper.tourist_page.Models.Tourist;
import org.ruppin.roper.tourist_page.Utils.JsonCtgHolderApi;
import org.ruppin.roper.tourist_page.Utils.JsonTouristHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

//content_finish_mission.xml
public class FinishMission extends AppCompatActivity {
    private TableLayout table;
    private TableRow trEmpty,tr,trSaveBtn;
    private String IP_ADD;
    private JsonTouristHolder jsonTouristHolder;
    private JsonQuestPlaceHolder jsonQuestPlaceHolder;
    private int mission_size = 0;
    private String name = new String();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_mission);
        IP_ADD = getResources().getString(R.string.IP);
        String userName = getIntent().getStringExtra("USER_NAME").toString();
        String userPass = getIntent().getStringExtra("PASSWORD").toString();
        Retrofit retrofit  = Utils.retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"),userName,userPass);
        jsonTouristHolder = retrofit.create(JsonTouristHolder.class);
        Call call  = jsonTouristHolder.getTourist("http://"+IP_ADD+":8080/WebService/webapi/tourist_service/tourist?loginName=" + userName);
        call.enqueue(new Callback<Tourist>() {
            @Override
            public void onResponse(Call<Tourist> call, Response<Tourist> response) {
                Tourist tourist = response.body();
                ArrayList<Quest> quests = tourist.getQuests();
                if(quests.size() == 0 )
                {
                    Context context = getApplicationContext();
                    CharSequence text = "No missions to show";
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    Intent intent = new Intent(getApplicationContext(), org.ruppin.roper.quest_page.MissionMapPage.class);
                    intent.putExtra("TOURIST", getIntent().getStringExtra("TOURIST"));
                    startActivity(intent);
                    return;
                }
                ArrayList<Mission> missions = quests.get(0).getMissions();
                Map<String,String> tokens = new HashMap<String, String>();
                for(Mission m : missions)
                {
                    tokens.put(m.getName().toString(),m.getBusiness().getMissionToken().toString());
                }
                mission_size = missions.size();
                table = (TableLayout)findViewById(R.id.FinishMissionTable);
                table.setColumnStretchable(0,true);
                table.setColumnStretchable(1,true);
                if(mission_size==0)
                {
                    TextView emptyPage = new TextView(getApplicationContext());
                    emptyPage.setGravity(Gravity.CENTER);
                    emptyPage.setText("No missions to show");
                    trEmpty = new TableRow(getApplicationContext());
                    trEmpty.addView(emptyPage);
                    table.addView(trEmpty);
                }
                else
                {
                    for(int i=0;i<mission_size;i++)
                    {
                        TextView missionName = new TextView(getApplicationContext());
                        missionName.setText(missions.get(i).getName().toString());
                        missionName.setGravity(Gravity.CENTER);
                        tr = new TableRow(getApplicationContext());
                        tr.addView(missionName);
                        EditText missionToken = new EditText(getApplicationContext());
                        missionToken.setHint("insert the mission token");
                        missionToken.setGravity(Gravity.CENTER);
                        missionToken.setEnabled(true);
                        tr.addView(missionToken);
                        table.addView(tr);
                    }
                    Button checkValid = new Button(getApplicationContext());
                    checkValid.setText("FINISH");
                    checkValid.setGravity(Gravity.CENTER);
                    trSaveBtn = new TableRow(getApplicationContext());
                    trSaveBtn.addView(checkValid);
                    table.addView(trSaveBtn);
                    checkValid.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int table_size = table.getChildCount()-1;
                            HashMap<String,Boolean> flags = new HashMap<String,Boolean>();
                            for(int i=0;i<table_size-1;i++)
                            {
                                TableRow tokenRaw = (TableRow) table.getChildAt(i);
                                EditText tokenFromUser = (EditText) tokenRaw.getChildAt(1);
                                TableRow nameRaw = (TableRow) table.getChildAt(i);
                                TextView nameOfMission = (TextView) nameRaw.getChildAt(0);
                                String tokenAsString = tokenFromUser.getText().toString();
                                String nameOfMissionAsString = nameOfMission.getText().toString();
                                String data = tokens.get(nameOfMissionAsString);
                                    if (!tokenAsString.equals(data))
                                    {
                                        flags.put(nameOfMissionAsString,false);
                                    }
                                    else {
                                        flags.put(nameOfMissionAsString,true);
                                    }
                                }
                                ArrayList<String> wrong = new ArrayList<String>();
                                StringBuilder stringBuilder = new StringBuilder();
                                for (Map.Entry<String, Boolean> entry : flags.entrySet())
                                {
                                    if(entry.getValue() == false)
                                    {
                                        stringBuilder.append(entry.getKey().toString());
                                        wrong.add(stringBuilder.toString());
                                    }
                                }
                                if(wrong.size()!=0)
                                {
                                    {
                                        Context context = getApplicationContext();

                                        CharSequence text = "wrong tokens for :  " + stringBuilder.toString();
                                        int duration = Toast.LENGTH_LONG;
                                        Toast toast = Toast.makeText(context, text, duration);
                                        toast.show();
                                    }

                                }
                                else
                                {
                                    Context context = getApplicationContext();
                                    CharSequence text = "Great!!";
                                    int duration = Toast.LENGTH_SHORT;
                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();

                                    //here to update all the missions to status finish
                                    for(int i = 0 ; i<mission_size ; i++)
                                    {
                                        Retrofit retrofit  = Utils.retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"),userName,userPass);
                                        jsonQuestPlaceHolder = retrofit.create(JsonQuestPlaceHolder.class);
                                        Call callFinish = jsonQuestPlaceHolder.finishMission("http://" + IP_ADD + ":8080/WebService/webapi/tourist_service/finishMission?id=" + missions.get(i).getId().toString(),tourist);
                                        callFinish.enqueue(new Callback() {
                                            @Override
                                            public void onResponse(Call call, Response response) {
                                                Intent intent = new Intent(getApplicationContext(), org.ruppin.roper.quest_page.FinishQuestPage.class);
                                                intent.putExtra("TOURIST", getIntent().getStringExtra("TOURIST"));
                                                startActivity(intent);
                                            }

                                            @Override
                                            public void onFailure(Call call, Throwable t) {

                                            }
                                        });
                                    }
//                                    //
//                                    Retrofit retrofit  = Utils.retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"),userName,userPass);
//                                    jsonQuestPlaceHolder = retrofit.create(JsonQuestPlaceHolder.class);
//                                    Call call  = jsonQuestPlaceHolder.getQuest("http://"+IP_ADD+":8080/WebService/webapi/quest_service/quest?name=" + tourist.getQuests().get(0).getName().toString());
//                                    call.enqueue(new Callback<Quest>() {
//                                        @Override
//                                        public void onResponse(Call<Quest> call, Response<Quest> response) {
//                                            Quest quest = response.body();
//                                            quest.setStatus("Done");
//                                            Retrofit retrofit  = Utils.retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"),userName,userPass);
//                                            jsonQuestPlaceHolder = retrofit.create(JsonQuestPlaceHolder.class);
//                                            Quest quest1 = new Quest();
//                                            Call call2  = jsonQuestPlaceHolder.updateQuest("http://"+IP_ADD+":8080/WebService/webapi/quest_service/quest", quest);
//                                            call2.enqueue(new Callback() {
//                                                @Override
//                                                public void onResponse(Call call, Response response) {
//
//                                                }
//
//                                                @Override
//                                                public void onFailure(Call call, Throwable t) {
//
//                                                }
//                                            });
//                                        }
//
//                                        @Override
//                                        public void onFailure(Call call, Throwable t) {
//
//                                        }
//                                    });
//                                    //
//                                    Intent intent = new Intent(getApplicationContext(), org.ruppin.roper.quest_page.FinishQuestPage.class);
//                                    intent.putExtra("TOURIST", getIntent().getStringExtra("TOURIST"));
//                                    startActivity(intent);
                                }
                        }

                    });
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

}
