package org.ruppin.roper.bussines_owner_page.delete_business;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.ruppin.roper.R;
import org.ruppin.roper.Utils.Utils;
import org.ruppin.roper.bussines_owner_page.Models.BusinessBO;
import org.ruppin.roper.bussines_owner_page.Models.BusinessOwner;
import org.ruppin.roper.bussines_owner_page.Utils.JsonBusinessOwnerHolder;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

//TO DO
//to search in the list if there are same names and case it is then count how many then do - name(1 of 2) / name(2 of 2)
public class DeleteBusiness extends AppCompatActivity {
    private String IP_ADD;
    private Spinner nameData;
    private Spinner addData;
    private Button saveBtn;
    private JsonBusinessOwnerHolder jsonBusinessOwnerHolder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_business);
        nameData = (Spinner)findViewById(R.id.BsnsNameDltId);
        addData = (Spinner)findViewById(R.id.bsnsAddDltId);
        saveBtn = (Button)findViewById(R.id.saveCngsBsnsId);

        IP_ADD = getResources().getString(R.string.IP);
        //
        String bsns = getIntent().getExtras().getString("BUSINESSES");
        String userName = getIntent().getStringExtra("USERNAME").toString();
        String userPass = getIntent().getStringExtra("PASSWORD").toString();
        JsonParser parser = new JsonParser();
        JsonElement mJson =  parser.parse(bsns);
        JsonArray jsonArray = mJson.getAsJsonArray();
        Gson gson = new Gson();
        BusinessBO[] listBO = gson.fromJson(jsonArray, BusinessBO[].class);
        //
        ArrayList<String> names = new ArrayList<String>();
        ArrayList<String> add = new ArrayList<String>();
        LinkedHashMap mapData = new LinkedHashMap<String, String>();
        for(int i=0;i<listBO.length;i++)
        {
            StringBuilder sbNames = new StringBuilder();
            sbNames.append(listBO[i].getName().toString());
            names.add(sbNames.toString());

            //
            StringBuilder sbAdd = new StringBuilder();
            sbAdd.append(listBO[i].getAddress().toString());
            add.add(sbAdd.toString());
            mapData.put(names.get(i),add.get(i));
        }
        ArrayAdapter<String> adapterNames = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,names );
        nameData.setAdapter(adapterNames);
        nameData.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<String> addToArray = new ArrayList<String>();
                addToArray.add(add.get(position));
                ArrayAdapter<String> adapterAddress =
                        new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,addToArray);
                addData.setAdapter(adapterAddress);
                Hashtable<String, String> NameId = new Hashtable<String, String>();
                saveBtn.setOnClickListener(new View.OnClickListener() {
                    String idToDlt;
                    @Override
                    public void onClick(View v) {
                        for(int i=0;i<listBO.length;i++)
                        {
                            if(listBO[i].getName().equals(names.get(position).toString()))
                                idToDlt = listBO[i].getId().toString();
                        }
                        Retrofit retrofit = Utils.retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"), userName.toString(), userPass.toString());
                        jsonBusinessOwnerHolder = retrofit.create(JsonBusinessOwnerHolder.class);
                        BusinessBO bsnsToDlt = new BusinessBO(idToDlt);
                        Call call = jsonBusinessOwnerHolder.deleteBusiness(bsnsToDlt);
                        call.enqueue(new Callback<BusinessBO>() {
                            @Override
                            public void onResponse(Call<BusinessBO> call, Response<BusinessBO> response) {
                                Context context = getApplicationContext();
                                CharSequence text = "business has deleted..";
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }

                            @Override
                            public void onFailure(Call<BusinessBO> call, Throwable t) {
                                Log.e("ERROR", String.format("Error: %s", t.getMessage()));
                            }
                        });
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}

