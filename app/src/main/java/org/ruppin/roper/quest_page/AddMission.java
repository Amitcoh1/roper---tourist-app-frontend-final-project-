package org.ruppin.roper.quest_page;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.ruppin.roper.R;
import org.ruppin.roper.Utils.Utils;
import org.ruppin.roper.quest_page.Models.newQuestModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddMission extends AppCompatActivity {
    private TextView newMissionDesc;
    private TextView newMissionName;
    private TextView newMissionStatus;
    private TextView newMissionDiff;
    private Button save;
    private String IP_ADD;
    private JsonQuestPlaceHolder jsonQuestPlaceHolder;
    private newQuestModel newQuestmodel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_mission_activity);
        newMissionDesc = (TextView)findViewById(R.id.addMis_Desc);
        newMissionDiff = (TextView)findViewById(R.id.addMis_Diff);
        newMissionName = (TextView)findViewById(R.id.addMis_Name);
        newMissionStatus = (TextView)findViewById(R.id.addMis_Status);
        save = (Button)findViewById(R.id.saveMission);
        IP_ADD = getResources().getString(R.string.IP);
        String userName = getIntent().getStringExtra("USER_NAME").toString();
        String userPass = getIntent().getStringExtra("PASSWORD").toString();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit  = Utils.retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"),userName,userPass);
                jsonQuestPlaceHolder  = retrofit.create(JsonQuestPlaceHolder.class);
                newQuestmodel = new newQuestModel();
                newQuestmodel.setDescription(newMissionDesc.getText().toString());
                newQuestmodel.setDifficulty(newMissionDiff.getText().toString());
                newQuestmodel.setName(newMissionName.getText().toString());
                newQuestmodel.setStatus(newMissionStatus.getText().toString());

                Call call = jsonQuestPlaceHolder.addQuest("http://"+IP_ADD+":8080/WebService/webapi/mission_service/mission?id="+getIntent().getStringExtra("SELECTION_ID"),newQuestmodel);
                call.enqueue(new Callback<newQuestModel>() {
                    @Override
                    public void onResponse(Call<newQuestModel> call, Response<newQuestModel> response) {
                        Context context = getApplicationContext();
                        CharSequence text = "mission has added..";
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
}
