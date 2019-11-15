package org.ruppin.roper.quest_page;

import org.json.JSONObject;
import org.ruppin.roper.bussines_owner_page.Models.BusinessBO;
import org.ruppin.roper.quest_page.Models.editquestModel;
import org.ruppin.roper.quest_page.Models.newQuestModel;
import org.ruppin.roper.tourist_page.Models.Mission;
import org.ruppin.roper.tourist_page.Models.MissionNoBS;
import org.ruppin.roper.tourist_page.Models.Quest;
import org.ruppin.roper.tourist_page.Models.Tourist;
import org.ruppin.roper.tourist_page.Models.questNoBS;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface JsonQuestPlaceHolder {
    @PUT
    Call<Quest> updateQuest(@Url String url,@Body Quest quest);
    @PUT
    Call<MissionNoBS> updateMission(@Url String url, @Body MissionNoBS missionNoBS);
    @POST
    Call<Quest> addQuest(@Url String url, @Body newQuestModel quest);
    @GET
    Call<ArrayList<Quest>> getAllQuests(@Url String url);
    @GET
    Call<Quest> getQuest(@Url String url);
    @GET
    Call<questNoBS> getQuestNoBS(@Url String url);
    @HTTP(method = "DELETE",hasBody = true)
    Call<Quest> removeQuest(@Url String url,@Body editquestModel id);
    @GET
    Call<Mission> getMission(@Url String url);
    @HTTP(method = "DELETE",hasBody = true)
    Call<MissionNoBS> removeMission(@Url String url,@Body MissionNoBS id);
    @PUT
    Call<MissionNoBS> addMissionToBsns(@Url String url, @Body MissionNoBS id);
    @HTTP(method = "DELETE",hasBody = true)
    Call<Quest> removeBsns(@Url String url,@Body MissionNoBS id);
    @PUT
    Call<MissionNoBS> finishMission(@Url String url, @Body Tourist tourist);
}
