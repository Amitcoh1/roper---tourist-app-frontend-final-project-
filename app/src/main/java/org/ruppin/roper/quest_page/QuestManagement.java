package org.ruppin.roper.quest_page;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.ruppin.roper.tourist_page.Models.Tourist;

import org.ruppin.roper.R;

public class QuestManagement extends AppCompatActivity {
    private Button newQuest;
    private Button continueQuest;
    private Button loginPage;
    private Button TouristMap;
    private  Tourist touristDto = new Tourist();
    String stringTourist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_managment);
        /*Getting the tourist dto from the intent*/


        stringTourist = getIntent().getStringExtra("TOURIST").toString();
        JsonParser parser = new JsonParser();
        JsonElement mJson =  parser.parse(stringTourist);
        Gson gson = new Gson();
        touristDto = gson.fromJson(mJson, Tourist.class);

        //Setting up the buttons
        newQuest = (Button) findViewById(R.id.newQuestId);
        continueQuest = (Button) findViewById(R.id.continueQuestId);
        loginPage = (Button) findViewById(R.id.loginPageId);
        TouristMap = (Button) findViewById(R.id.TouristMapId);
    }


    @Override
    public void onBackPressed() {

    }

    /*Click functions*/
    public void backToLoginPageClick(View v)
    {
        startActivity(new Intent(QuestManagement.this, org.ruppin.roper.login_page.loginPage.class));
    }

    public void backToTouristPageClick(View v)
    {
        Intent intent = new Intent(QuestManagement.this, org.ruppin.roper.tourist_page.MapsActivity.class);
        intent.putExtra("TOURIST",stringTourist);
        startActivity(intent);
    }

    public void newQuestClick(View v)
    {
        Intent intent = new Intent(QuestManagement.this, org.ruppin.roper.quest_page.StartNewQuest.class);
        intent.putExtra("TOURIST_ID",touristDto.getId().toString());
        if(touristDto.getQuests().size()!=0)
            intent.putExtra("QUEST_ID_CURRENT",touristDto.getQuests().get(0).getId().toString());
        intent.putExtra("TOURIST",stringTourist.toString());
        intent.putExtra("USER_NAME",touristDto.getLoginName().toString());
        intent.putExtra("PASSWORD",touristDto.getPassword().toString());
        startActivity(intent);
    }

    public void continueQuestClick(View v)
    {
        Intent intent = new Intent(QuestManagement.this, org.ruppin.roper.quest_page.MissionMapPage.class);
        intent.putExtra("TOURIST",stringTourist);
        startActivity(intent);
    }

}
