package org.ruppin.roper.bussines_owner_page.show_bussineses_page;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.ruppin.roper.bussines_owner_page.Models.BusinessBO;

import org.ruppin.roper.R;

public class ShowBusinessOwnesBusinesses extends AppCompatActivity
{
    TableLayout table;
    TableRow trEmpty,tr,tr2,tr3,tr4,tr5,tr6,tr7,tr8,tr9,tr10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_owner_show_businesses);
        String bsns = getIntent().getExtras().getString("BUSINESSES");
        JsonParser parser = new JsonParser();
        JsonElement mJson =  parser.parse(bsns);
        JsonArray jsonArray = mJson.getAsJsonArray();
        Gson gson = new Gson();
        BusinessBO[] listBO = gson.fromJson(jsonArray,BusinessBO[].class);
        table = (TableLayout)findViewById(R.id.ShowBSNSpage);
        table.setColumnStretchable(0,true);
        table.setColumnStretchable(1,true);
        if(listBO.length==0)
        {
            TextView emptyPage = new TextView(getApplicationContext());
            emptyPage.setGravity(Gravity.CENTER);
            emptyPage.setText("No businesses to show");
            trEmpty = new TableRow(this);
            trEmpty.addView(emptyPage);
            table.addView(trEmpty);
        }
        for (int i=0;i<listBO.length;i++) {
            //11
            TextView bsnsToken = new TextView(this);
            bsnsToken.setText("Token");
            bsnsToken.setGravity(Gravity.CENTER);
            tr10 = new TableRow(this);
            tr10.addView(bsnsToken);
            EditText bsnsTokeneData = new EditText(this);
            bsnsTokeneData.setText(listBO[i].getMissionToken());
            bsnsTokeneData.setGravity(Gravity.CENTER);
            bsnsTokeneData.setEnabled(false);
            tr10.addView(bsnsTokeneData);
            table.addView(tr10);
            //1
            TextView bsnsName = new TextView(this);
            bsnsName.setText("Name");
            bsnsName.setGravity(Gravity.CENTER);
            tr = new TableRow(this);
            tr.addView(bsnsName);
            EditText bsnsNameData = new EditText(this);
            bsnsNameData.setText(listBO[i].getName());
            bsnsNameData.setGravity(Gravity.CENTER);
            bsnsNameData.setEnabled(false);
            tr.addView(bsnsNameData);
            table.addView(tr);
            //2
            TextView bsnsAdd = new TextView(this);
            bsnsAdd.setText("Address");
            bsnsAdd.setGravity(Gravity.CENTER);
            tr2 = new TableRow(this);
            tr2.addView(bsnsAdd);
            EditText bsnsAddData = new EditText(this);
            bsnsAddData.setText(listBO[i].getAddress());
            bsnsAddData.setGravity(Gravity.CENTER);
            bsnsAddData.setEnabled(false);
            tr2.addView(bsnsAddData);
            table.addView(tr2);
            //3
            TextView promot = new TextView(this);
            promot.setText("Promot");
            promot.setGravity(Gravity.CENTER);
            tr3 = new TableRow(this);
            tr3.addView(promot);
            ToggleButton promotData = new ToggleButton(this);
            if (listBO[i].getOnPromotion().equals("false"))
                promotData.setText("OFF");
            else
                promotData.setText("ON");
            promotData.setEnabled(false);
            promotData.setGravity(Gravity.CENTER);
            tr3.addView(promotData);
            table.addView(tr3);
            //4
            TextView bsnsType = new TextView(this);
            bsnsType.setText("Type");
            bsnsType.setGravity(Gravity.CENTER);
            tr4 = new TableRow(this);
            tr4.addView(bsnsType);
            EditText bsnsTypeData = new EditText(this);
            if(listBO[i].getType()!=null) {
                bsnsTypeData.setText(listBO[i].getType().replace("_", " "));
                String typeCap = bsnsTypeData.getText().toString().substring(0,1).toUpperCase() + bsnsTypeData.getText().toString().substring(1);
                bsnsTypeData.setText(typeCap.replace("_", " "));
            }
            else
                bsnsTypeData.setText(listBO[i].getType());
            bsnsTypeData.setGravity(Gravity.CENTER);
            bsnsTypeData.setEnabled(false);
            tr4.addView(bsnsTypeData);
            table.addView(tr4);
            //5
            TextView bsnsOnPromotionInfo = new TextView(this);
            bsnsOnPromotionInfo.setText("Promotion Info");
            bsnsOnPromotionInfo.setGravity(Gravity.CENTER);
            tr5 = new TableRow(this);
            tr5.addView(bsnsOnPromotionInfo);
            EditText bsnsOnPromotionInfoData = new EditText(this);
            bsnsOnPromotionInfoData.setText(listBO[i].getPromotionInfo());
            bsnsOnPromotionInfoData.setGravity(Gravity.CENTER);
            bsnsOnPromotionInfoData.setEnabled(false);
            tr5.addView(bsnsOnPromotionInfoData);
            table.addView(tr5);
            //6
            TextView line = new TextView(this);
            line.setText("      ");
            line.setGravity(Gravity.CENTER);
            tr6 = new TableRow(this);
            tr6.addView(line);
            table.addView(tr6);
            TextView line2 = new TextView(this);
            line2.setText("      ");
            line2.setGravity(Gravity.CENTER);
            tr7 = new TableRow(this);
            tr7.addView(line2);
            table.addView(tr7);
            TextView line3 = new TextView(this);
            line3.setText("      ");
            line3.setGravity(Gravity.CENTER);
            tr8 = new TableRow(this);
            tr8.addView(line3);
            table.addView(tr8);
            TextView line4 = new TextView(this);
            line4.setText("      ");
            line4.setGravity(Gravity.CENTER);
            tr9 = new TableRow(this);
            tr9.addView(line4);
            table.addView(tr9);
        }

    }
}
