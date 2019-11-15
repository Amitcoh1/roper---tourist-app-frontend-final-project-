package org.ruppin.roper.bussines_owner_page.edit_bussineses_page;
//
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.ruppin.roper.Utils.Utils;
import org.ruppin.roper.bussines_owner_page.Models.BusinessBO;

import org.ruppin.roper.R;
import org.ruppin.roper.bussines_owner_page.Models.BusinessOwner;
import org.ruppin.roper.bussines_owner_page.Utils.JsonBusinessOwnerHolder;
import org.ruppin.roper.bussines_owner_page.add_business_page.AddBusinessToBO;
import org.ruppin.roper.tourist_page.Models.Categories;
import org.ruppin.roper.tourist_page.Utils.JsonCtgHolderApi;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditBusinessOwnesBusinesses extends AppCompatActivity
{
    TableLayout table;
    TableRow trEmpty,tr,tr2,tr3,tr4,tr5,tr6,tr7,tr8,tr9,tr10;
    private String IP_ADD;
    private Spinner bsnsTypeData;
    private JsonBusinessOwnerHolder jsonBusinessOwnerHolder;
    private JsonCtgHolderApi jsonCtgHolderApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IP_ADD = getResources().getString(R.string.IP);
        String userName = getIntent().getStringExtra("USERNAME").toString();
        String userPass = getIntent().getStringExtra("PASSWORD").toString();
        Retrofit retrofit  = Utils.retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"),userName,userPass);
        jsonCtgHolderApi = retrofit.create(JsonCtgHolderApi.class);
        Call call = jsonCtgHolderApi.getCtgrs("http://"+IP_ADD+":8080/WebService/webapi/category_service/categories");
        call.enqueue(new Callback<List<Categories>>() {
            @Override
            public void onResponse(Call<List<Categories>> call, Response<List<Categories>> response)
            {
                List<Categories> categories = response.body();
                List<String> ctgrsName= new ArrayList<String>();
                for(int i=0;i<categories.size();i++)
                    if(categories.get(i).getType().equals("place"))ctgrsName.add(categories.get(i).getDisplayName());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditBusinessOwnesBusinesses.this, android.R.layout.simple_spinner_dropdown_item, ctgrsName);

                setContentView(R.layout.activity_business_owner_show_businesses);
                String bsns = getIntent().getExtras().getString("BUSINESSES");
                JsonParser parser = new JsonParser();
                JsonElement mJson =  parser.parse(bsns);
                JsonArray jsonArray = mJson.getAsJsonArray();
                Gson gson = new Gson();
                BusinessBO[] listBO = gson.fromJson(jsonArray, BusinessBO[].class);
                table = (TableLayout)findViewById(R.id.ShowBSNSpage);
                table.setColumnStretchable(0,true);
                table.setColumnStretchable(1,true);
                if(listBO.length==0)
                {
                    TextView emptyPage = new TextView(getApplicationContext());
                    emptyPage.setGravity(Gravity.CENTER);
                    emptyPage.setText("No businesses to edit");
                    trEmpty = new TableRow(getApplicationContext());
                    trEmpty.addView(emptyPage);
                    table.addView(trEmpty);
                }
                int posBtnSave=5;
                ArrayList<Integer> pos = new ArrayList<Integer>();
                String userId = getIntent().getExtras().getString("ID");
                for (int i=0;i<listBO.length;i++) {
                    //123 t10
                    TextView bsnsToken = new TextView(getApplicationContext());
                    bsnsToken.setText("Token");
                    bsnsToken.setGravity(Gravity.CENTER);
                    tr10 = new TableRow(getApplicationContext());
                    tr10.addView(bsnsToken);
                    EditText bsnsTokenData = new EditText(getApplicationContext());
                    bsnsTokenData.setText(listBO[i].getMissionToken());
                    bsnsTokenData.setGravity(Gravity.CENTER);
                    bsnsTokenData.setEnabled(true);
                    tr10.addView(bsnsTokenData);
                    table.addView(tr10);
                    //12
                    TextView bsnsName = new TextView(getApplicationContext());
                    bsnsName.setText("Name");
                    bsnsName.setGravity(Gravity.CENTER);
                    tr = new TableRow(getApplicationContext());
                    tr.addView(bsnsName);
                    EditText bsnsNameData = new EditText(getApplicationContext());
                    bsnsNameData.setText(listBO[i].getName());
                    bsnsNameData.setGravity(Gravity.CENTER);
                    bsnsNameData.setEnabled(true);
                    tr.addView(bsnsNameData);
                    table.addView(tr);
                    //2
                    TextView bsnsAdd = new TextView(getApplicationContext());
                    bsnsAdd.setText("Address");
                    bsnsAdd.setGravity(Gravity.CENTER);
                    tr2 = new TableRow(getApplicationContext());
                    tr2.addView(bsnsAdd);
                    EditText bsnsAddData = new EditText(getApplicationContext());
                    bsnsAddData.setText(listBO[i].getAddress());
//                  LatLng latLng = getLocationFromAddress(getApplicationContext(),bsnsAddData.getText().toString());
                    bsnsAddData.setGravity(Gravity.CENTER);
                    bsnsAddData.setEnabled(true);
                    tr2.addView(bsnsAddData);
                    table.addView(tr2);
                    //3
                    TextView promot = new TextView(getApplicationContext());
                    promot.setText("Promot");
                    promot.setGravity(Gravity.CENTER);
                    tr3 = new TableRow(getApplicationContext());
                    tr3.addView(promot);
                    ToggleButton promotData = new ToggleButton(getApplicationContext());
                    if (listBO[i].getOnPromotion().equals("false"))
                        promotData.setChecked(false);
                    else
                        promotData.setChecked(true);
                    promotData.setEnabled(true);
                    promotData.setGravity(Gravity.CENTER);
                    tr3.addView(promotData);
                    table.addView(tr3);
                    //4
                    TextView bsnsType = new TextView(getApplicationContext());
                    bsnsType.setText("Type");
                    bsnsType.setGravity(Gravity.CENTER);
                    tr4 = new TableRow(getApplicationContext());
                    tr4.addView(bsnsType);
                    bsnsTypeData = new Spinner(getApplicationContext());
                    HashMap<String,Integer> selectionIndex = new HashMap<>();
                    bsnsTypeData.setAdapter(adapter);
                    for(int k=0;k<categories.size();k++)
                        selectionIndex.put(categories.get(k).getDisplayName().toLowerCase().toString(),k);
                    int s = selectionIndex.get(listBO[i].getType().toLowerCase().toString());
                    bsnsTypeData.setSelection(s);
                    bsnsTypeData.setGravity(Gravity.CENTER);
                    bsnsTypeData.setEnabled(true);
                    tr4.addView(bsnsTypeData);
                    table.addView(tr4);
                    s=0;
                    //5
                    TextView bsnsOnPromotionInfo = new TextView(getApplicationContext());
                    bsnsOnPromotionInfo.setText("Promotion Info");
                    bsnsOnPromotionInfo.setGravity(Gravity.CENTER);
                    tr5 = new TableRow(getApplicationContext());
                    tr5.addView(bsnsOnPromotionInfo);
                    EditText bsnsOnPromotionInfoData = new EditText(getApplicationContext());
                    bsnsOnPromotionInfoData.setText(listBO[i].getPromotionInfo());
                    bsnsOnPromotionInfoData.setGravity(Gravity.CENTER);
                    bsnsOnPromotionInfoData.setEnabled(true);
                    tr5.addView(bsnsOnPromotionInfoData);
                    table.addView(tr5);
                    //6
                    tr6 = new TableRow(getApplicationContext());
                    Button saveCngs = new Button(getApplicationContext());
                    TextView fillEmpty = new TextView(getApplicationContext());
                    tr6.addView(fillEmpty);
                    tr6.setTag(listBO[i].getId());
                    saveCngs.setText("SAVE BUSINESS");
                    saveCngs.setGravity(Gravity.CENTER_HORIZONTAL);
                    saveCngs.setTag(posBtnSave);
                    pos.add(Integer.parseInt(saveCngs.getTag().toString()));
                    tr6.addView(saveCngs);
                    tr6.setTag(table.getChildCount());
                    table.addView(tr6);
                    saveCngs.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onClick(View v) {
                            String userId = getIntent().getExtras().getString("ID");
                            Retrofit retrofit = Utils.retrofitInit(String.format("http://"+IP_ADD+":8080/WebService/webapi/"), userName,userPass);
                            jsonBusinessOwnerHolder = retrofit.create(JsonBusinessOwnerHolder.class);
                            //get number of rows
                            int numOfRows = table.getChildCount();
                            int numOfElements = listBO.length;
                            //
                            ArrayList<BusinessBO> bsnsBo = new ArrayList<>();
                            int i=listBO.length-1;
                            for(int j=Integer.parseInt(tr6.getTag().toString())-1;j>0;j--)
                            {
                                BusinessBO businessBO = new BusinessBO();
                                //promot info
                                TableRow promotInfo = (TableRow) table.getChildAt(j);
                                EditText promotinfoDataUpdt = (EditText) promotInfo.getChildAt(1);
                                if(promotinfoDataUpdt!=null)
                                    businessBO.setPromotionInfo(promotinfoDataUpdt.getText().toString());
                                else
                                    businessBO.setPromotionInfo("");
                                j--;
                                //type
                                TableRow type = (TableRow) table.getChildAt(j);
                                Spinner typeSelected =  (Spinner) type.getChildAt(1);
                                businessBO.setType(typeSelected.getSelectedItem().toString());
                                j--;
                                //promot
                                TableRow promot = (TableRow) table.getChildAt(j);
                                ToggleButton promotUpdt = (ToggleButton) promot.getChildAt(1);
                                if(promotUpdt.isChecked())
                                   businessBO.setOnPromotion("true");
                                else
                                   businessBO.setOnPromotion("false");
                                j--;
                                //address
                                TableRow add = (TableRow) table.getChildAt(j);
                                EditText addUpdt =  (EditText) add.getChildAt(1);
                                businessBO.setAddress(addUpdt.getText().toString());
                                j--;
                                //name
                                TableRow name = (TableRow) table.getChildAt(j);
                                EditText nameUpdt =  (EditText) name.getChildAt(1);
                                businessBO.setName(nameUpdt.getText().toString());
                                j=j-4;
                                //id
                                if(i>=0)
                                {
                                    businessBO.setId(listBO[i].getId());
                                    businessBO.setLatitude(listBO[i].getLatitude());
                                    businessBO.setLongitude(listBO[i].getLongitude());
                                    businessBO.setCreationDate(listBO[i].getCreationDate());
                                }
                                bsnsBo.add(businessBO);
                                i--;

                            }

                            BusinessOwner bo = new BusinessOwner(userId,bsnsBo);
                            Call call = jsonBusinessOwnerHolder.updateBOinfo(bo);
                            call.enqueue(new Callback<BusinessOwner>() {
                                @Override
                                public void onResponse(Call<BusinessOwner> call, Response<BusinessOwner> response) {
                                    Context context = getApplicationContext();
                                    CharSequence text = "info has updated..";
                                    int duration = Toast.LENGTH_SHORT;
                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();
                                }

                                @Override
                                public void onFailure(Call<BusinessOwner> call, Throwable t) {
                                    Log.e("ERROR", String.format("Error: %s", t.getMessage()));
                                }
                            });

                        }
                    });
                    TextView line2 = new TextView(getApplicationContext());
                    line2.setText("      ");
                    line2.setGravity(Gravity.CENTER);
                    tr7 = new TableRow(getApplicationContext());
                    tr7.addView(line2);
                    table.addView(tr7);
                    TextView line3 = new TextView(getApplicationContext());
                    line3.setText("      ");
                    line3.setGravity(Gravity.CENTER);
                    tr8 = new TableRow(getApplicationContext());
                    tr8.addView(line3);
                    table.addView(tr8);
                    TextView line4 = new TextView(getApplicationContext());
                    line4.setText("      ");
                    line4.setGravity(Gravity.CENTER);
                    tr9 = new TableRow(getApplicationContext());
                    tr9.addView(line4);
                    table.addView(tr9);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

    }


    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }
}


