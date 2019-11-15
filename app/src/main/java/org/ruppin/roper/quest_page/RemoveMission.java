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
import org.ruppin.roper.quest_page.Models.editquestModel;
import org.ruppin.roper.tourist_page.Models.Quest;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RemoveMission extends AppCompatActivity {
    private Activity activity;
    private Spinner showAll;
    private Button remove;
    private String IP_ADD;
    private JsonQuestPlaceHolder jsonQuestPlaceHolder;
    public Quest quest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_all_missions_to_remove);
        this.activity = this;
        IP_ADD = getResources().getString(R.string.IP);
        showAll = (Spinner) findViewById(R.id.showAllMissionsSpinnerToRemove);
        remove = (Button)findViewById(R.id.removeMission);
        //
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
                Retrofit retrofit = Utils.retrofitInit(String.format("http://" + IP_ADD + ":8080/WebService/webapi/"), userName, userPass);
                jsonQuestPlaceHolder = retrofit.create(JsonQuestPlaceHolder.class);
                Call call2 = jsonQuestPlaceHolder.getQuest("http://" + IP_ADD + ":8080/WebService/webapi/quest_service/quest?name="+showAll.getSelectedItem().toString());
                call2.enqueue(new Callback<Quest>() {
                    @Override
                    public void onResponse(Call<Quest> call, Response<Quest> response) {
                        remove.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                quest = response.body();
                                editquestModel model = new editquestModel(quest.getId().toString());
                                //
                                Retrofit retrofit = Utils.retrofitInit(String.format("http://" + IP_ADD + ":8080/WebService/webapi/"), userName, userPass);
                                jsonQuestPlaceHolder = retrofit.create(JsonQuestPlaceHolder.class);
                                Call call = jsonQuestPlaceHolder.removeQuest("http://" + IP_ADD + ":8080/WebService/webapi/quest_service/quest",model);
                                call.enqueue(new Callback() {
                                    @Override
                                    public void onResponse(Call call, Response response) {
                                        Context context = getApplicationContext();
                                        CharSequence text = "removed quest..";
                                        int duration = Toast.LENGTH_SHORT;
                                        Toast toast = Toast.makeText(context, text, duration);
                                        toast.show();
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
            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }
}
