package org.ruppin.roper.admin_page;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ruppin.roper.R;
import org.ruppin.roper.Utils.Utils;
import org.ruppin.roper.admin_page.Utils.JsonAdminHolderApi;
import org.ruppin.roper.bussines_owner_page.add_business_page.AddBusinessToBO;
import org.ruppin.roper.tourist_page.Models.Categories;
import org.ruppin.roper.tourist_page.Utils.JsonCtgHolderApi;
import org.ruppin.roper.tourist_page.Utils.MultiSelectSpinner;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AdminEditDefaultData extends AppCompatActivity {

    private EditText dfltRds;
    private EditText dfltApiKey;
    private MultiSelectSpinner dfltTypes;
    private Button saveCngs;
    private String IP_ADD;
    private JsonAdminHolderApi jsonAdminHolderApi;
    private JsonCtgHolderApi jsonCtgHolderApi;

    AdminDto adminDtoFromRet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_page);

        dfltApiKey = (EditText)findViewById(R.id.GglApiKId);
        dfltRds = (EditText)findViewById(R.id.DfltRdsId);
        dfltTypes = (MultiSelectSpinner)findViewById(R.id.DfltTypesId);
        saveCngs = (Button)findViewById(R.id.saveCngsAdminBtnId);
        IP_ADD = getResources().getString(R.string.IP);
        String userName = getIntent().getStringExtra("USER_NAME").toString();
        String userPass = getIntent().getStringExtra("PASSWORD").toString();
        Retrofit retrofit  = Utils.retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"),userName,userPass);
        jsonAdminHolderApi = retrofit.create(JsonAdminHolderApi.class);
        Call call = jsonAdminHolderApi.getSettings("http://"+IP_ADD+":8080/WebService/webapi/settings_service/settings");
        call.enqueue(new Callback<AdminDto>() {
            @Override
            public void onResponse(Call<AdminDto> call, Response<AdminDto> response)
            {
                adminDtoFromRet= response.body();
                dfltApiKey.setText(adminDtoFromRet.getGooglePlacesKey());
                dfltRds.setText(adminDtoFromRet.getDefaultRadius());
                //get the ctgrs
                Retrofit retrofit = Utils.retrofitInit(String.format("http://" + IP_ADD + ":8080/WebService/webapi/"), userName.toString(), userPass.toString());
                jsonCtgHolderApi = retrofit.create(JsonCtgHolderApi.class);
                Call callToGetCtgrs = jsonCtgHolderApi.getCtgrs("http://" + IP_ADD + ":8080/WebService/webapi/category_service/categories");
                callToGetCtgrs.enqueue(new Callback<List<Categories>>() {
                    @Override
                    public void onResponse(Call<List<Categories>> call, Response<List<Categories>> response) {
                        List<Categories> categories = response.body();
                        List<String> ctgrsName = new ArrayList<String>();
                        for (int i = 0; i < categories.size(); i++)
                            if(categories.get(i).getType().equals("place"))ctgrsName.add(categories.get(i).getDisplayName());
                        dfltTypes.setItems(ctgrsName);
                        String prefAsString = adminDtoFromRet.getDefalutTypes().toString();
                        String[] prefAsArray = prefAsString.split(",");
                        for(int i=0;i<prefAsArray.length;i++)
                            prefAsArray[i] = prefAsArray[i].replaceAll("^\\s+|\\s+$","");
                        dfltTypes.setSelection(prefAsArray);

                    }

                    @Override
                    public void onFailure(Call<List<Categories>> call, Throwable t) {

                    }

                });
                saveCngs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AdminDto adminDto = new AdminDto();
                        adminDto.setId(adminDtoFromRet.getId());
                        adminDto.setGooglePlacesKey(dfltApiKey.getText().toString());
                        adminDto.setDefaultRadius(dfltRds.getText().toString());
                        adminDto.setDefalutTypes(dfltTypes.getSelectedItemsAsString().replaceAll("^\\s+|\\s+$","".toString()));
                        Retrofit retrofit  = Utils.retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"),userName,userPass);
                        jsonAdminHolderApi = retrofit.create(JsonAdminHolderApi.class);
                        Call call = jsonAdminHolderApi.updtSettings(adminDto);
                        call.enqueue(new Callback<AdminDto>() {
                            @Override
                            public void onResponse(Call<AdminDto> call, Response<AdminDto> response)
                            {
                                Context context = getApplicationContext();
                                CharSequence text = "info has updated..";
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
        // here
    }
}
