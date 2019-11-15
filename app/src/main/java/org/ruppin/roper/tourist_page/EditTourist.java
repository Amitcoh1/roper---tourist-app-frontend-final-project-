package org.ruppin.roper.tourist_page;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.ruppin.roper.R;
import org.ruppin.roper.Utils.Utils;
import org.ruppin.roper.bussines_owner_page.Models.BusinessOwner;
import org.ruppin.roper.bussines_owner_page.Utils.JsonBusinessOwnerHolder;
import org.ruppin.roper.bussines_owner_page.businessOwnerMainPage;
import org.ruppin.roper.bussines_owner_page.delete_business.DeleteBusiness;
import org.ruppin.roper.login_page.loginPage;
import org.ruppin.roper.tourist_page.Models.Categories;
import org.ruppin.roper.tourist_page.Models.Tourist;
import org.ruppin.roper.tourist_page.Utils.JsonCtgHolderApi;
import org.ruppin.roper.tourist_page.Utils.JsonTouristHolder;
import org.ruppin.roper.tourist_page.Utils.MultiSelectSpinner;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class EditTourist extends AppCompatActivity {

    private Tourist touristDto;
    private EditText tourFName;
    private EditText tourLName;
    private EditText tourEmail;
    private EditText tourDOB;
    private EditText tourLoginName;
    private EditText tourPass;
    private Spinner tourGender;
    private EditText tourAge;
    private MultiSelectSpinner tourPref;
    private EditText tourRTH;
    private Spinner tourSpknLn;
    private Spinner tourVicType;
    //private Switch tourMtPpl;
    private Button save;
    private String IP_ADD;
    JsonCtgHolderApi jsonCtgHolderApi;
    JsonTouristHolder jsonTouristHolder;
    Tourist touristToUpdt = new Tourist();
    Tourist touristToPass;
    private String defaultRadius;
    private String apiKey;
    private String defaultTypes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tourist);
        //get the tourist json string and parse it into touristDto
        String stringTourist = getIntent().getStringExtra("TOURIST").toString();
        JsonParser parser = new JsonParser();
        JsonElement mJson = parser.parse(stringTourist);
        Gson gson = new Gson();
        touristDto = gson.fromJson(mJson, Tourist.class);
        defaultRadius = touristDto.getDefaultRadius();
        apiKey = touristDto.getGooglePlacesKey();
        defaultTypes = touristDto.getDefalutTypes();
        //catch the elements
        IP_ADD = getResources().getString(R.string.IP);
        tourFName = (EditText) findViewById(R.id.TourFirstNameId);
        tourLName = (EditText) findViewById(R.id.TourLastNameId);
        tourEmail = (EditText) findViewById(R.id.TourEmailId);
        tourDOB = (EditText) findViewById(R.id.Tour_DOBId);
        tourLoginName = (EditText) findViewById(R.id.TourLoginNameId);
        tourPass = (EditText) findViewById(R.id.TourPasswordId);
        tourGender = (Spinner) findViewById(R.id.TourGenderTypeId);
        tourAge = (EditText) findViewById(R.id.TourAgeId);
        tourPref = (MultiSelectSpinner) findViewById(R.id.TourPredId);
        tourRTH = (EditText) findViewById(R.id.TourRadiousTHId);
        tourSpknLn = (Spinner) findViewById(R.id.TourSpknId);
        tourVicType = (Spinner) findViewById(R.id.TourVicType);
        //tourMtPpl = (Switch) findViewById(R.id.meetPplId);
        save = (Button) findViewById(R.id.TourSaveCngsBtn);
        //
        tourFName.setText(touristDto.getFirstName().toString());

        tourLName.setText(touristDto.getLastName().toString());

        tourEmail.setText(touristDto.getEmail().toString());

        tourDOB.setText(touristDto.getDateOfBirthStr().toString());

        tourLoginName.setText(touristDto.getLoginName().toString());

        tourPass.setText(touristDto.getPassword());

        String[] gndrXml = getResources().getStringArray(R.array.Gender);
        ArrayAdapter<String> adapterGndrVls = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, gndrXml);
        tourGender.setAdapter(adapterGndrVls);
        if (touristDto.getGender().equalsIgnoreCase("male"))
            tourGender.setSelection(0);
        else if (touristDto.getGender().equalsIgnoreCase("female"))
            tourGender.setSelection(1);
        else
            tourGender.setSelection(2);

        tourAge.setText(touristDto.getAge().toString());

        Retrofit retrofit = Utils.retrofitInit(String.format("http://" + IP_ADD + ":8080/WebService/webapi/"), touristDto.getLoginName().toString(), touristDto.getPassword().toString());
        jsonCtgHolderApi = retrofit.create(JsonCtgHolderApi.class);
        Call call = jsonCtgHolderApi.getCtgrs("http://" + IP_ADD + ":8080/WebService/webapi/category_service/categories");
        call.enqueue(new Callback<List<Categories>>() {
            @Override
            public void onResponse(Call<List<Categories>> call, Response<List<Categories>> response) {
                List<Categories> categories = response.body();
                List<String> ctgrsName = new ArrayList<String>();
                for (int i = 0; i < categories.size(); i++)
                    if(categories.get(i).getType().equals("place"))
                    {
                        ctgrsName.add(categories.get(i).getDisplayName());
                    }
                ctgrsName.add("Hidden");
                tourPref.setItems(ctgrsName);
                String prefAsString = touristDto.getPreferences().toString();
                String[] prefAsArray = prefAsString.split(",");
                for (int i = 0; i < prefAsArray.length; i++)
                    prefAsArray[i] = prefAsArray[i].replaceAll("\\s+", "");
                if (touristDto.getPreferences().equals("Hidden")) {
                    tourPref.setSelection(54);
                } else
                    tourPref.setSelection(prefAsArray);
            }

            @Override
            public void onFailure(Call<List<Categories>> call, Throwable t) {

            }

        });

        tourRTH.setText(touristDto.getRadiusThreshold());

        String[] lngXml = getResources().getStringArray(R.array.Languages);
        ArrayAdapter<String> adapterLngVls = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, lngXml);
        tourSpknLn.setAdapter(adapterLngVls);
        if (touristDto.getSpokenLanguages().equalsIgnoreCase("hebrew"))
            tourSpknLn.setSelection(0);
        else if (touristDto.getSpokenLanguages().equalsIgnoreCase("arabic"))
            tourSpknLn.setSelection(1);
        else if (touristDto.getSpokenLanguages().equalsIgnoreCase("english"))
            tourSpknLn.setSelection(2);
        else
            tourSpknLn.setSelection(3);

        String[] vicTypeXml = getResources().getStringArray(R.array.vicType);
        ArrayAdapter<String> adapterVicTypeVls = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, vicTypeXml);
        tourVicType.setAdapter(adapterVicTypeVls);
        if (touristDto.getVicationType().equalsIgnoreCase("business"))
            tourVicType.setSelection(0);
        else if (touristDto.getVicationType().equalsIgnoreCase("pleasure"))
            tourVicType.setSelection(1);
        else
            tourVicType.setSelection(2);

//        String meetPplStt = touristDto.getMeetPeople().toString();
//        if (meetPplStt == "true")
//            tourMtPpl.setChecked(true);
//        else
//            tourMtPpl.setChecked(false);
        //send an update request of the tourist personal information
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = Utils.retrofitInit(String.format("http://" + IP_ADD + ":8080/WebService/webapi/"), touristDto.getLoginName().toString(), touristDto.getPassword().toString());
                jsonTouristHolder = retrofit.create(JsonTouristHolder.class);

                touristToUpdt.setId(touristDto.getId());
                touristToUpdt.setLastName(tourLName.getText().toString());
                if (!tourLoginName.getText().toString().equals(touristDto.getLoginName().toString()))
                    touristToUpdt.setLoginName(tourLoginName.getText().toString());
                if (!tourEmail.getText().toString().equals(touristDto.getEmail().toString()))
                    touristToUpdt.setEmail(tourEmail.getText().toString());
                touristToUpdt.setDateOfBirthStr(tourDOB.getText().toString());
                touristToUpdt.setPassword(tourPass.getText().toString());
                if (!tourGender.getSelectedItem().equals("Hidden"))
                    touristToUpdt.setGender(tourGender.getSelectedItem().toString());
                if (!tourAge.getText().equals(0))
                    touristToUpdt.setAge(tourAge.getText().toString());
                if (!tourRTH.getText().equals("Hidden")) {
                    touristToUpdt.setRadiusThreshold(tourRTH.getText().toString());
                } else {
                    touristToUpdt.setRadiusThreshold(touristDto.getDefaultRadius().toString());
                }
                if (!tourSpknLn.getSelectedItem().equals("Hidden"))
                    touristToUpdt.setSpokenLanguages(tourSpknLn.getSelectedItem().toString());
                if (!tourVicType.getSelectedItem().equals("Hidden"))
                    touristToUpdt.setVicationType(tourVicType.getSelectedItem().toString());
//                if (tourMtPpl.isChecked())
//                    touristToUpdt.setMeetPeople("true");
//                else
//                    touristToUpdt.setMeetPeople("false");
                if (!tourPref.getSelectedItemsAsString().equals("Hidden")) {
                    List<String> arr = tourPref.getSelectedStrings();
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < arr.size(); i++) {
                        if (!(arr.get(i).equals("Hidden"))) {
                            arr.set(i, arr.get(i).trim().toString());
                            sb.append(arr.get(i));
                            sb.append(",");
                        }
                    }
                    String sbAsString = sb.toString();
                    sbAsString = sbAsString.substring(0, sbAsString.length() - 1);
                    touristToUpdt.setPreferences(sbAsString);
                } else
                    touristToUpdt.setPreferences("Hidden");
                touristToUpdt.setMoreInformation("true");
                Call call = jsonTouristHolder.updateTouristInfo(touristToUpdt);
                call.enqueue(new Callback<Tourist>() {
                    @Override
                    public void onResponse(Call<Tourist> call, Response<Tourist> response) {
                        Context context = getApplicationContext();
                        CharSequence text = "info has been updated.";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        if (touristToUpdt.getLoginName() == null)
                            touristToUpdt.setLoginName(touristDto.getLoginName().toString());
                        if (touristToUpdt.getPassword() == null)
                            touristToUpdt.setPassword(touristDto.getPassword().toString());

                        Retrofit retrofit2 = Utils.retrofitInit(String.format("http://" + IP_ADD + ":8080/WebService/webapi/"), touristToUpdt.getLoginName().toString(), touristToUpdt.getPassword().toString());
                        jsonTouristHolder = retrofit.create(JsonTouristHolder.class);
                        Call call2 = jsonTouristHolder.getTourist("http://" + IP_ADD + ":8080/WebService/webapi/tourist_service/tourist?loginName=" + touristToUpdt.getLoginName().toString());
                        call2.enqueue(new Callback<Tourist>() {
                            @Override
                            public void onResponse(Call<Tourist> call, Response<Tourist> response) {
                                touristToPass = response.body();
                                touristToPass.setDefalutTypes(defaultTypes);
                                touristToPass.setDefaultRadius(defaultRadius);
                                touristToPass.setGooglePlacesKey(apiKey);
                                Intent intent = new Intent(EditTourist.this, MapsActivity.class);
                                Gson touristJson = new Gson();
                                String touristString = touristJson.toJson(touristToPass);
                                intent.putExtra("TOURIST", touristString);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call<Tourist> call, Throwable t) {

                            }

                        });
                    }

                    @Override
                    public void onFailure(Call<Tourist> call, Throwable t) {
                        Log.e("ERROR", String.format("Error: %s", t.getMessage()));
                    }
                });
            }
        });
    }
}
