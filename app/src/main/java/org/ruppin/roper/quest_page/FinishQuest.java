package org.ruppin.roper.quest_page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.ruppin.roper.R;
import org.ruppin.roper.Utils.Utils;
import org.ruppin.roper.tourist_page.Models.Quest;
import org.ruppin.roper.tourist_page.Models.Tourist;
import org.ruppin.roper.tourist_page.Utils.JsonTouristHolder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FinishQuest extends AppCompatActivity {
    private Activity activity;
    private Spinner showAll;
    private Button next;
    private String IP_ADD;
    private JsonTouristHolder jsonTouristHolder;
    private JsonQuestPlaceHolder jsonQuestPlaceHolder2;
    private String selection;
    private TableLayout table;
    private TableRow trEmpty,tr,trSaveBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_finished_quests);
        this.activity = this;
        IP_ADD = getResources().getString(R.string.IP);
        String userName = getIntent().getStringExtra("USER_NAME").toString();
        String userPass = getIntent().getStringExtra("PASSWORD").toString();
        Retrofit retrofit = Utils.retrofitInit(String.format("http://" + IP_ADD + ":8080/WebService/webapi/"), userName, userPass);
        jsonTouristHolder = retrofit.create(JsonTouristHolder.class);
        Call call = jsonTouristHolder.getTourist("http://" + IP_ADD + ":8080/WebService/webapi/tourist_service/tourist?loginName=" + userName);
        call.enqueue(new Callback<Tourist>() {
            @Override
            public void onResponse(Call<Tourist> call, Response<Tourist> response) {
                ArrayList<String> questsNamesArray = new ArrayList<String>();
                ArrayList<Quest> quests = response.body().getQuests();
                ArrayList<Quest> questsFinished = new ArrayList<Quest>();
                for(Quest q : quests)
                {
                    if(q.getStatus().toLowerCase().equals("done"))
                    {
                        questsFinished.add(q);
                    }

                }
                table = (TableLayout)findViewById(R.id.FinishQuestsTable);
                table.setColumnStretchable(0,true);
                table.setColumnStretchable(1,true);
                if(questsFinished.size()==0)
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
                    for (Quest q : questsFinished) {
                        TextView questName = new TextView(getApplicationContext());
                        questName.setText(q.getName().toString());
                        questName.setGravity(Gravity.CENTER);
                        tr = new TableRow(getApplicationContext());
                        tr.addView(questName);

                        table.addView(tr);

                    }
                }
            }
            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }
}
