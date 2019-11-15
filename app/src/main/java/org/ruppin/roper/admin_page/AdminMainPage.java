package org.ruppin.roper.admin_page;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.koushikdutta.ion.builder.Builders;

import org.ruppin.roper.R;
import org.ruppin.roper.bussines_owner_page.businessOwnerMainPage;
import org.ruppin.roper.login_page.loginPage;
import org.ruppin.roper.quest_page.AddBussinessToMission;
import org.ruppin.roper.quest_page.AddMission;
import org.ruppin.roper.quest_page.EditMission;
import org.ruppin.roper.quest_page.EditQuest;
import org.ruppin.roper.quest_page.NewQuest;
import org.ruppin.roper.quest_page.RemoveMission;
import org.ruppin.roper.quest_page.RemoveQuest;
import org.ruppin.roper.quest_page.ShowAllMissions;
import org.ruppin.roper.quest_page.ShowAllMissionsToRemove;
import org.ruppin.roper.quest_page.ShowAllQstsToAddBsns;
import org.ruppin.roper.quest_page.ShowAllQuests;
import org.ruppin.roper.quest_page.ShowAllQuests2;
import org.ruppin.roper.quest_page.ShowAllQuests3;
import org.ruppin.roper.quest_page.ShowAllQuestsToMissionRemove;
import org.ruppin.roper.quest_page.ShowAllQuestsToRemoveBsns;

public class AdminMainPage extends AppCompatActivity {

    private Button editProfile;
    private Button editDfltstngs;
    private Button logout;
    private Button addNewQuestBtn;
    private Button editquest;
    private Button removeQuest;
    private Button addMission;
    private Button editMission;
    private Button removeMission;
    private Button addB2M;
    private Button rmB2M;
    private String IP_ADD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_page);
        //
        editDfltstngs = (Button)findViewById(R.id.editDflsStAdId);
        editProfile = (Button)findViewById(R.id.EditPrflAdId);
        addNewQuestBtn = (Button)findViewById(R.id.addnewQuest);
        logout = (Button)findViewById(R.id.logOutAdId);
        editquest = (Button)findViewById(R.id.editQuest);
        removeQuest = (Button)findViewById(R.id.removeQuestAdmin);
        addMission = (Button)findViewById(R.id.addMission) ;
        editMission = (Button)findViewById(R.id.editMission) ;
        removeMission = (Button)findViewById(R.id.removeMissionAdminPage);
        addB2M = (Button)findViewById(R.id.addMissionToBussiness);
        rmB2M = (Button) findViewById(R.id.rmMissionToBussiness2);
        IP_ADD = getResources().getString(R.string.IP);
        String userName = getIntent().getStringExtra("USER_NAME").toString();
        String userPass = getIntent().getStringExtra("PASSWORD").toString();
        //
        editDfltstngs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainPage.this, AdminEditDefaultData.class);
                intent.putExtra("USER_NAME",userName);
                intent.putExtra("PASSWORD",userPass);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMainPage.this, loginPage.class));
            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainPage.this, AdminEditProfile.class);
                intent.putExtra("USER_NAME",userName);
                intent.putExtra("PASSWORD",userPass);
                startActivity(intent);
            }
        });
        addNewQuestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainPage.this, NewQuest.class);
                intent.putExtra("USER_NAME",userName);
                intent.putExtra("PASSWORD",userPass);
                intent.putExtra("TYPE_OF_BTN_ADDQ","addQuest");
                startActivity(intent);
            }
        });
        editquest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainPage.this, ShowAllQuests.class);
                intent.putExtra("USER_NAME",userName);
                intent.putExtra("PASSWORD",userPass);
                startActivity(intent);
            }
        });
        removeQuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainPage.this, RemoveQuest.class);
                intent.putExtra("USER_NAME",userName);
                intent.putExtra("PASSWORD",userPass);
                startActivity(intent);
            }
        });
        addMission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainPage.this, ShowAllQuests2.class);
                intent.putExtra("USER_NAME",userName);
                intent.putExtra("PASSWORD",userPass);
                intent.putExtra("TYPE_OF_BTN_ADDM","addMission");
                startActivity(intent);
            }
        });
        editMission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainPage.this, ShowAllQuests3.class);
                intent.putExtra("USER_NAME",userName);
                intent.putExtra("PASSWORD",userPass);
                startActivity(intent);
            }
        });
        removeMission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainPage.this, ShowAllQuestsToMissionRemove.class);
                intent.putExtra("USER_NAME",userName);
                intent.putExtra("PASSWORD",userPass);
                startActivity(intent);
            }
        });
        addB2M.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainPage.this, ShowAllQstsToAddBsns.class);
                intent.putExtra("USER_NAME",userName);
                intent.putExtra("PASSWORD",userPass);
                startActivity(intent);
            }
        });
        rmB2M.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainPage.this, ShowAllQuestsToRemoveBsns.class);
                intent.putExtra("USER_NAME",userName);
                intent.putExtra("PASSWORD",userPass);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        Context context = getApplicationContext();
        CharSequence text = "Use the logout button to go back to login page";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}
