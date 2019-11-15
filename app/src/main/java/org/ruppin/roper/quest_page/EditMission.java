package org.ruppin.roper.quest_page;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ruppin.roper.R;
import org.ruppin.roper.Utils.Utils;
import org.ruppin.roper.quest_page.Models.newQuestModel;
import org.ruppin.roper.tourist_page.Models.Mission;
import org.ruppin.roper.tourist_page.Models.MissionNoBS;
import org.ruppin.roper.tourist_page.Models.Quest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditMission extends AppCompatActivity {
    private EditText editMissDesc;
    private EditText editMissName;
    private EditText editMissStatus;
    private EditText editMissDiff;
    //
    private EditText _editMissDesc;
    private EditText _editMissName;
    private EditText _editMissStatus;
    private EditText _editMissDiff;
    private Button update;
    private String IP_ADD;
    private JsonQuestPlaceHolder jsonQuestPlaceHolder;
    private newQuestModel newQuestmodel;
    public MissionNoBS model;
    public MissionNoBS _model = new MissionNoBS();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_mission_activity);
        editMissDesc = (EditText)findViewById(R.id.editMiss_Desc);
        editMissDiff = (EditText)findViewById(R.id.editMiss_Diff);
        editMissName = (EditText)findViewById(R.id.editMiss_Name);
        editMissStatus = (EditText)findViewById(R.id.editMiss_Status);
        //
        _editMissDesc = (EditText)findViewById(R.id.editMiss_Desc);
        _editMissDiff = (EditText)findViewById(R.id.editMiss_Diff);
        _editMissName = (EditText)findViewById(R.id.editMiss_Name);
        _editMissStatus = (EditText)findViewById(R.id.editMiss_Status);
        //
        update = (Button)findViewById(R.id.saveMission);
        IP_ADD = getResources().getString(R.string.IP);
        String userName = getIntent().getStringExtra("USER_NAME").toString();
        String userPass = getIntent().getStringExtra("PASSWORD").toString();
        String selection = getIntent().getStringExtra("SELECTION").toString();

        Retrofit retrofit  = Utils.retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"),userName,userPass);
        jsonQuestPlaceHolder  = retrofit.create(JsonQuestPlaceHolder.class);
        Call call = jsonQuestPlaceHolder.getMission("http://"+IP_ADD+":8080/WebService/webapi/mission_service/mission?name="+selection);
        call.enqueue(new Callback<MissionNoBS>() {
            @Override
            public void onResponse(Call<MissionNoBS> call, Response<MissionNoBS> response) {
                model = response.body();
                editMissDesc.setText(model.getDescription().toString());
                editMissDiff.setText(model.getDifficulty().toString());
                editMissName.setText(model.getName().toString());
                editMissStatus.setText(model.getStatus().toString());
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        _editMissDesc.setText(editMissDesc.getText().toString());
                        _editMissDiff.setText(editMissDiff.getText().toString());
                        _editMissName.setText(editMissName.getText().toString());
                        _editMissStatus.setText(editMissStatus.getText().toString());
                        //
                        //_model.setMissions(model.getMissions());
                        _model.setDescription(_editMissDesc.getText().toString());
                        _model.setId(model.getId());
                        _model.setStatus(_editMissStatus.getText().toString());
                        _model.setDifficulty(_editMissDiff.getText().toString());
                        _model.setName(_editMissName.getText().toString());
                        //
                        Retrofit retrofit  = Utils.retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"),userName,userPass);
                        jsonQuestPlaceHolder  = retrofit.create(JsonQuestPlaceHolder.class);
                        Call call = jsonQuestPlaceHolder.updateMission("http://"+IP_ADD+":8080/WebService/webapi/mission_service/mission",_model);
                        call.enqueue(new Callback<Quest>() {
                            @Override
                            public void onResponse(Call<Quest> call, Response<Quest> response) {
                                Context context = getApplicationContext();
                                CharSequence text = "info has been updated.";
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
}
