package org.ruppin.roper.quest_page;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.ruppin.roper.quest_page.Models.newQuestModel;
import org.ruppin.roper.R;
import org.ruppin.roper.Utils.Utils;
import org.ruppin.roper.quest_page.Models.newQuestModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NewQuest extends AppCompatActivity {
    private TextView newQuestDesc;
    private TextView newQuestName;
    private TextView newQuestStatus;
    private TextView newQuestDiff;
    private Button save;
    private String IP_ADD;
    private JsonQuestPlaceHolder jsonQuestPlaceHolder;
    private newQuestModel newQuestmodel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_quest);
        newQuestDesc = (TextView)findViewById(R.id.addQueast_Desc);
        newQuestDiff = (TextView)findViewById(R.id.addQueast_Diff);
        newQuestName = (TextView)findViewById(R.id.addQueast_Name);
        newQuestStatus = (TextView)findViewById(R.id.addQueast_Status);
        save = (Button)findViewById(R.id.SaveCngsBtn);
        IP_ADD = getResources().getString(R.string.IP);
        String userName = getIntent().getStringExtra("USER_NAME").toString();
        String userPass = getIntent().getStringExtra("PASSWORD").toString();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit  = Utils.retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"),userName,userPass);
                jsonQuestPlaceHolder  = retrofit.create(JsonQuestPlaceHolder.class);
                newQuestmodel = new newQuestModel();
                newQuestmodel.setDescription(newQuestDesc.getText().toString());
                newQuestmodel.setDifficulty(newQuestDiff.getText().toString());
                newQuestmodel.setName(newQuestName.getText().toString());
                newQuestmodel.setStatus(newQuestStatus.getText().toString());

                Call call = jsonQuestPlaceHolder.addQuest("http://"+IP_ADD+":8080/WebService/webapi/quest_service/quest",newQuestmodel);
                call.enqueue(new Callback<newQuestModel>() {
                    @Override
                    public void onResponse(Call<newQuestModel> call, Response<newQuestModel> response) {
                        Context context = getApplicationContext();
                        CharSequence text = "quest has added..";
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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

}
