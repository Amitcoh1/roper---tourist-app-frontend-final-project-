package org.ruppin.roper.quest_page;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.ruppin.roper.R;
import org.ruppin.roper.Utils.Utils;
import org.ruppin.roper.quest_page.Models.editquestModel;
import org.ruppin.roper.quest_page.Models.newQuestModel;
import org.ruppin.roper.tourist_page.Models.Quest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditQuest extends AppCompatActivity {
    private EditText editQuestDesc;
    private EditText editQuestName;
    private EditText editQuestStatus;
    private EditText editQuestDiff;
    //
    private EditText _editQuestDesc;
    private EditText _editQuestName;
    private EditText _editQuestStatus;
    private EditText _editQuestDiff;
    private Button update;
    private String IP_ADD;
    private JsonQuestPlaceHolder jsonQuestPlaceHolder;
    private newQuestModel newQuestmodel;
    public Quest model;
    public Quest _model = new Quest();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_quest_activity);
        editQuestDesc = (EditText)findViewById(R.id.editQueast_Desc);
        editQuestDiff = (EditText)findViewById(R.id.editQueast_Diff);
        editQuestName = (EditText)findViewById(R.id.editQueast_Name);
        editQuestStatus = (EditText)findViewById(R.id.editQueast_Status);
        //
        _editQuestDesc = (EditText)findViewById(R.id.editQueast_Desc);
        _editQuestDiff = (EditText)findViewById(R.id.editQueast_Diff);
        _editQuestName = (EditText)findViewById(R.id.editQueast_Name);
        _editQuestStatus = (EditText)findViewById(R.id.editQueast_Status);
        //
        update = (Button)findViewById(R.id.saveQuest);
        IP_ADD = getResources().getString(R.string.IP);
        String userName = getIntent().getStringExtra("USER_NAME").toString();
        String userPass = getIntent().getStringExtra("PASSWORD").toString();
        String selection = getIntent().getStringExtra("SELECTION").toString();

        Retrofit retrofit  = Utils.retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"),userName,userPass);
        jsonQuestPlaceHolder  = retrofit.create(JsonQuestPlaceHolder.class);
        Call call = jsonQuestPlaceHolder.getQuest("http://"+IP_ADD+":8080/WebService/webapi/quest_service/quest?name="+selection);
        call.enqueue(new Callback<Quest>() {
            @Override
            public void onResponse(Call<Quest> call, Response<Quest> response) {
                model = response.body();
                editQuestDesc.setText(model.getDescription().toString());
                editQuestDiff.setText(model.getDifficulty().toString());
                editQuestName.setText(model.getName().toString());
                editQuestStatus.setText(model.getStatus().toString());
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        _editQuestDesc.setText(editQuestDesc.getText().toString());
                        _editQuestDiff.setText(editQuestDiff.getText().toString());
                        _editQuestName.setText(editQuestName.getText().toString());
                        _editQuestStatus.setText(editQuestStatus.getText().toString());
                        //
                        _model.setMissions(model.getMissions());
                        _model.setDescription(_editQuestDesc.getText().toString());
                        _model.setId(model.getId());
                        _model.setStatus(_editQuestStatus.getText().toString());
                        _model.setDifficulty(_editQuestDiff.getText().toString());
                        _model.setName(_editQuestName.getText().toString());
                        //
                        Retrofit retrofit  = Utils.retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"),userName,userPass);
                        jsonQuestPlaceHolder  = retrofit.create(JsonQuestPlaceHolder.class);
                        Call call = jsonQuestPlaceHolder.updateQuest("http://"+IP_ADD+":8080/WebService/webapi/quest_service/quest",_model);
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
