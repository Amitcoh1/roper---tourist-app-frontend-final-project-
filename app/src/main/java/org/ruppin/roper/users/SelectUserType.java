package org.ruppin.roper.users;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

//import org.ruppin.roper.admin_page.AddNewAdmin;
import org.ruppin.roper.bussines_owner_page.AddNewBO;
import org.ruppin.roper.tourist_page.AddNewTourist;

import org.ruppin.roper.R;

public class SelectUserType extends AppCompatActivity {
    private Spinner nType;
    private String selectedItemType;
    private TextView nextPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage_regiset);

        nType = (Spinner) findViewById(R.id.type);
        String[] type = {"business owner","tourist"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(SelectUserType.this, android.R.layout.simple_list_item_1, type);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nType.setAdapter(spinnerArrayAdapter);
        nType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        selectedItemType = "business owner";
                        break;
                    case 1:
                        selectedItemType = "tourist";
                        break;
//                    case 2:
//                        selectedItemType = "Admin";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        nextPage = (TextView) findViewById(R.id.nextStep1);
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedItemType == "business owner")
                {
                    Intent intent = new Intent(SelectUserType.this, AddNewBO.class);
                    startActivity(intent);
                }
                else if(selectedItemType == "tourist")
                {
                    Intent intent1 = new Intent(SelectUserType.this, AddNewTourist.class);
                    startActivity(intent1);
                }
//                else if(selectedItemType == "Admin")
//                {
//
//                    Intent intent2 = new Intent(SelectUserType.this, AddNewAdmin.class);
//                    startActivity(intent2);
//                }
            }
        });
    }
}
