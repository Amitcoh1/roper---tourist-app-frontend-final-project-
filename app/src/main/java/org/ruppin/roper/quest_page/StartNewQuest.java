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

import org.ruppin.roper.R;
import org.ruppin.roper.Utils.Utils;
import org.ruppin.roper.tourist_page.Models.Quest;
import org.ruppin.roper.tourist_page.Models.Tourist;
import org.ruppin.roper.tourist_page.Models.questNoBS;
import org.ruppin.roper.tourist_page.Utils.JsonTouristHolder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class StartNewQuest extends AppCompatActivity
{
    private Activity activity;
    private Spinner showAll;
    private Button next;
    private String IP_ADD;
    private JsonQuestPlaceHolder jsonQuestPlaceHolder;
    private JsonTouristHolder jsonTouristHolder;
    private String id = new String();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_new_quest_btn_activity);
        this.activity = this;
        IP_ADD = getResources().getString(R.string.IP);
        showAll = (Spinner) findViewById(R.id.showAllQuestsFromTrst);
        next = (Button)findViewById(R.id.selectQuestToApply);
        String userName = getIntent().getStringExtra("USER_NAME").toString();
        String userPass = getIntent().getStringExtra("PASSWORD").toString();
        Retrofit retrofit = Utils.retrofitInit(String.format("http://" + IP_ADD + ":8080/WebService/webapi/"), userName, userPass);
        jsonQuestPlaceHolder = retrofit.create(JsonQuestPlaceHolder.class);
        Call call = jsonQuestPlaceHolder.getAllQuests("http://" + IP_ADD + ":8080/WebService/webapi/quest_service/quests");
        call.enqueue(new Callback<ArrayList<Quest>>() {
            @Override
            public void onResponse(Call<ArrayList<Quest>> call, Response<ArrayList<Quest>> response) {
                ArrayList<String> questsNamesArray = new ArrayList<String>();
                ArrayList<Quest> quests = response.body();
                for(int i=0;i<quests.size();i++)
                {
                    questsNamesArray.add(quests.get(i).getName().toString());
                }
                ArrayAdapter<String> adapterNames = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item,questsNamesArray);
                showAll.setAdapter(adapterNames);
                //
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Retrofit retrofit = Utils.retrofitInit(String.format("http://" + IP_ADD + ":8080/WebService/webapi/"), userName, userPass);
                        jsonQuestPlaceHolder = retrofit.create(JsonQuestPlaceHolder.class);
                        Call call = jsonQuestPlaceHolder.getQuestNoBS("http://"+IP_ADD+":8080/WebService/webapi/quest_service/quest?name="+showAll.getSelectedItem().toString());
                        call.enqueue(new Callback<questNoBS>() {
                            @Override
                            public void onResponse(Call<questNoBS> call, Response<questNoBS> response) {
                                Tourist tourist = new Tourist(getIntent().getStringExtra("TOURIST_ID").toString());
                                Retrofit retrofit = Utils.retrofitInit(String.format("http://" + IP_ADD + ":8080/WebService/webapi/"), userName, userPass);
                                jsonTouristHolder = retrofit.create(JsonTouristHolder.class);
                                //
                                try
                                {
                                    id = getIntent().getStringExtra("QUEST_ID_CURRENT").toString();
                                }
                                catch (Exception e)
                                {
                                    if(id.equals(""))
                                    {
                                        Call touristCall = jsonTouristHolder.addQuestToTourist("http://"+IP_ADD+":8080/WebService/webapi/tourist_service/quest?id=" + response.body().getId().toString() , tourist);
                                        touristCall.enqueue(new Callback() {
                                            @Override
                                            public void onResponse(Call call, Response response) {
                                                Context context = getApplicationContext();
                                                CharSequence text = "info updated..";
                                                int duration = Toast.LENGTH_SHORT;
                                                Toast toast = Toast.makeText(context, text, duration);
                                                toast.show();
                                                Intent intent = new Intent(StartNewQuest.this, org.ruppin.roper.quest_page.MissionMapPage.class);
                                                intent.putExtra("TOURIST",getIntent().getStringExtra("TOURIST"));
                                                startActivity(intent);
                                            }

                                            @Override
                                            public void onFailure(Call call, Throwable t) {

                                            }
                                        });
                                    }
                                    else
                                    {
                                        Call touristCallDelete = jsonTouristHolder.deleteQuestFromTourist("http://"+IP_ADD+":8080/WebService/webapi/tourist_service/quest?id=" + getIntent().getStringExtra("QUEST_ID_CURRENT").toString() , tourist);
                                        touristCallDelete.enqueue(new Callback<Tourist>() {
                                            @Override
                                            public void onResponse(Call<Tourist> call, Response<Tourist> response) {
                                                Call touristCall = jsonTouristHolder.addQuestToTourist("http://"+IP_ADD+":8080/WebService/webapi/tourist_service/quest?id=" + response.body().getId().toString() , tourist);
                                                touristCall.enqueue(new Callback() {
                                                    @Override
                                                    public void onResponse(Call call, Response response) {
                                                        Context context = getApplicationContext();
                                                        CharSequence text = "info updated..";
                                                        int duration = Toast.LENGTH_SHORT;
                                                        Toast toast = Toast.makeText(context, text, duration);
                                                        toast.show();
                                                        Intent intent = new Intent(StartNewQuest.this, org.ruppin.roper.quest_page.MissionMapPage.class);
                                                        intent.putExtra("TOURIST",getIntent().getStringExtra("TOURIST"));
                                                        startActivity(intent);
                                                    }

                                                    @Override
                                                    public void onFailure(Call call, Throwable t) {

                                                    }
                                                });
                                            }

                                            @Override
                                            public void onFailure(Call<Tourist> call, Throwable t) {

                                            }
                                        });
                                    }
                                }
                                if(!id.equals(""))
                                {
                                    Call touristCallDelete = jsonTouristHolder.deleteQuestFromTourist("http://" + IP_ADD + ":8080/WebService/webapi/tourist_service/quest?id=" + getIntent().getStringExtra("QUEST_ID_CURRENT").toString(), tourist);
                                    touristCallDelete.enqueue(new Callback<Tourist>() {
                                        @Override
                                        public void onResponse(Call<Tourist> call, Response<Tourist> response) {
                                        }

                                        @Override
                                        public void onFailure(Call<Tourist> call, Throwable t) {

                                        }
                                    });
                                    Call touristCall = jsonTouristHolder.addQuestToTourist("http://" + IP_ADD + ":8080/WebService/webapi/tourist_service/quest?id=" + response.body().getId().toString(), tourist);
                                    touristCall.enqueue(new Callback() {
                                        @Override
                                        public void onResponse(Call call, Response response) {
                                            Context context = getApplicationContext();
                                            CharSequence text = "info updated..";
                                            int duration = Toast.LENGTH_SHORT;
                                            Toast toast = Toast.makeText(context, text, duration);
                                            toast.show();
                                            Intent intent = new Intent(StartNewQuest.this, org.ruppin.roper.quest_page.MissionMapPage.class);
                                            intent.putExtra("TOURIST", getIntent().getStringExtra("TOURIST"));
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onFailure(Call call, Throwable t) {

                                        }
                                    });
                                }
                                //
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
