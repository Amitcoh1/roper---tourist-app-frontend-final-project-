package org.ruppin.roper.quest_page;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.ruppin.roper.R;
import org.ruppin.roper.Utils.Utils;
import org.ruppin.roper.login_page.loginPage;
import org.ruppin.roper.quest_page.Models.editquestModel;
import org.ruppin.roper.tourist_page.MapsActivity;
import org.ruppin.roper.tourist_page.Models.Categories;
import org.ruppin.roper.tourist_page.Models.Quest;
import org.ruppin.roper.tourist_page.Models.Tourist;
import org.ruppin.roper.tourist_page.Utils.JsonCtgHolderApi;
import org.ruppin.roper.tourist_page.Utils.JsonPlaceHolderApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

//content_finish_quest_page.xml
public class FinishQuestPage extends AppCompatActivity {
    Tourist touristDto = null;
    String stringTourist = "";
    String IP_ADD = "";
    TextView questNameTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_quest_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        questNameTextView = (TextView)findViewById(R.id.QuestName);
        IP_ADD = getResources().getString(R.string.IP);
        stringTourist = getIntent().getStringExtra("TOURIST").toString();
        JsonParser parser = new JsonParser();
        JsonElement mJson =  parser.parse(stringTourist);
        Gson gson = new Gson();
        touristDto = gson.fromJson(mJson, Tourist.class);
        Quest q = new Quest();
        updateQuestTextField(touristDto, q);
        updateQuestStatus(q);



    }

    @Override
    public void onBackPressed() {

    }

    public void questManag(View view) {
        Intent intent = new Intent(FinishQuestPage.this, QuestManagement.class);
        intent.putExtra("TOURIST",stringTourist);
        startActivity(intent);
    }

    public void touristPage(View view) {
        Intent intent = new Intent(FinishQuestPage.this, MapsActivity.class);
        intent.putExtra("TOURIST",stringTourist);
        startActivity(intent);
    }

    public void login(View view) {
        startActivity(new Intent(FinishQuestPage.this, loginPage.class));
    }

    public void showFinishQuests(View view) {
        Intent intent = new Intent(FinishQuestPage.this, FinishQuest.class);
        intent.putExtra("USER_NAME",touristDto.getLoginName());
        intent.putExtra("PASSWORD",touristDto.getLoginName());

        startActivity(intent);
    }

    private String FindFinishQuestName(Tourist tourist, Quest quest)
    {
        for(Quest q :tourist.getQuests())
        {
            return q.getName();
        }
        return "Error";
    }

    private void updateQuestTextField(Tourist tourist, Quest q)
    {
        String tmp = questNameTextView.getText().toString().replaceAll(",QUEST,",  FindFinishQuestName(tourist, q));
        questNameTextView.setText(tmp);
    }

    private void updateQuestStatus(Quest q)
    {
        Retrofit retrofit = Utils.retrofitInit("http://" + IP_ADD + ":8080/WebService/webapi/", touristDto.getLoginName(), touristDto.getPassword());
        JsonQuestPlaceHolder jsonQuestPlaceHolder = retrofit.create(JsonQuestPlaceHolder.class);
        Quest questPayload = new Quest();
        questPayload.setId(q.getId());
        questPayload.setStatus("Done");
        Call call = jsonQuestPlaceHolder.updateQuest("http://" + IP_ADD + ":8080/WebService/webapi/quest_service/quest",questPayload);
        call.enqueue(new Callback<Quest>() {
            @Override
            public void onResponse(Call<Quest> call, Response<Quest> response) {

            }

            @Override
            public void onFailure(Call<Quest> call, Throwable t) {

            }
        });

    }
}
