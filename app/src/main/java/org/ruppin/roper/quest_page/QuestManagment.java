package org.ruppin.roper.quest_page;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.ruppin.roper.tourist_page.Models.Tourist;

import org.ruppin.roper.R;

public class QuestManagment extends AppCompatActivity {
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
    /*Click functions*/
    public void backToLoginPageClick(View v)
    {
        startActivity(new Intent(QuestManagment.this, org.ruppin.roper.login_page.loginPage.class));
    }

    public void backToTouristPageClick(View v)
    {
        Intent intent = new Intent(QuestManagment.this, org.ruppin.roper.tourist_page.MapsActivity.class);
        intent.putExtra("TOURIST",stringTourist);
        startActivity(intent);
    }

    public void newQuestClick(View v)
    {
        Intent intent = new Intent(QuestManagment.this, org.ruppin.roper.quest_page.NewQuest.class);
        intent.putExtra("TOURIST",stringTourist);
        startActivity(intent);
    }

    public void continueQuestClick(View v)
    {
        Intent intent = new Intent(QuestManagment.this, org.ruppin.roper.quest_page.QuestActivity.class);
        intent.putExtra("TOURIST",stringTourist);
        startActivity(intent);
    }

}
