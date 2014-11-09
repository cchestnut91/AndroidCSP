package csp.experiencepush.com.mycsp;
/**
 * Created by andrew on 9/29/14.
 */

import org.json.JSONArray;

import java.io.IOException;

public class PushRESTAPI {

    private network data;

    PushRESTAPI(){
        data = new network();

    }

    public JSONArray getAllListings()throws Exception{
        String getData = null;
        try {
            getData = data.sendGet("http://experiencepush.com/csp_portal/rest/?PUSH_ID=123&call=getAllListings");
        } catch(IOException ioe){

            System.out.println("I/O error occurred: "+ioe);
        }
        JSONArray JSON = new JSONArray(getData);

        return  JSON;
    }

    public JSONArray getUserFavorites(String uuid)throws Exception{
        String getData = null;
        try {
            getData = data.sendGet("http://experiencepush.com/csp_portal/rest/?PUSH_ID=123&call=getUserFavorites&uuid="+uuid);
        }catch(IOException ioe){
            System.out.println("I/O error occurred: "+ioe);
        }
        JSONArray JSON = new JSONArray(getData);

        return JSON;
    }

    public String addUserFavorite(String uuid, String favorite_id)throws Exception{
        String postData = null;
        try {
            postData = data.sendPost("http://experiencepush.com/csp_portal/rest/","PUSH_ID=123&uuid="+uuid+"&favorite_id="+favorite_id+"&call=addUserFavorite");
        }catch(IOException ioe){
            System.out.println("I/O error occurred: "+ioe);
        }
        return postData;
    }

    public String removeUserFavorite(String uuid, String favorite_id)throws  Exception{
        String postData = null;
        try{
            postData = data.sendPost("http://experiencepush.com/csp_portal/rest/","PUSH_ID=123&uuid="+uuid+"&favorite_id="+favorite_id+"&call=removeUserFavorite");
        }catch(IOException ioe){
            System.out.println("I/O error occurred: "+ioe);
        }
        return postData;
    }

    public String addNewAnonUser(String uuid)throws Exception{
        String postData = null;
        try{
            postData = data.sendPost("http://experiencepush.com/csp_portal/rest/index.php","call=addNewAnonUser&PUSH_ID=123&uuid="+uuid);
        }catch(IOException ioe){
            System.out.println("I/O error occurred: "+ioe);
        }
        return postData;
    }

    public JSONArray getAllBeacons()throws Exception {
        String getData = null;
        try {
            getData = data.sendGet("http://experiencepush.com/csp_portal/rest/?PUSH_ID=123&call=getAllBeacons");
        } catch(IOException ioe) {
            System.out.println("I/O Error occured: " + ioe);
        }
        return new JSONArray(getData);

    }

    public JSONArray getCampaignHasBeacon()throws Exception {
        String getData = null;
        try{
            getData = data.sendGet("http://experiencepush.com/csp_portal/rest/index.php?PUSH_ID=123&call=getCampaignHasBeacon");
        }catch(IOException ioe){
            System.out.println("I/O error occurred: "+ioe);
        }
        return new JSONArray(getData);
    }

    public String registerTriggeredBeaconAction(String campaignId, String clicked, String uuid)throws Exception {
        String postData = null;
        try{
            postData = data.sendPost("http://experiencepush.com/csp_portal/rest/index.php","PUSH_ID=123&campaign_id="+campaignId+"&clicked="+clicked+"&uuid="+uuid+"&call=registerTriggeredBeaconAction");
        }catch(IOException ioe){
            System.out.println("I/O error occurred: "+ioe);
        }
        return postData;
    }

    public String updateTriggeredBeaconAction(String action_id, String clicked) throws Exception{
        String postData = null;
        try{
            postData = data.sendPost("http://experiencepush.com/csp_portal/rest/index.php","PUSH_ID=123&action_id="+action_id+"&clicked="+clicked+"&call=updateTriggeredBeaconAction");
        }catch(IOException ioe){
            System.out.println("I/O error occurred: "+ioe);
        }
        return postData;
    }
    /*
    public static void main(String[] args) throws Exception{
        PushRESTAPI api = new PushRESTAPI();
        //System.out.println(api.getAllListings());
        //System.out.println(api.getUserFavorites("901292DF-C96A-4F19-AF3A-5A1893C41DF3"));
        //System.out.println(api.addUserFavorite("901292DF-C96A-4F19-AF3A-5A1893C41DF3","85087"));
        //System.out.println(api.removeUserFavorite("901292DF-C96A-4F19-AF3A-5A1893C41DF3","85087"));
        //System.out.println(api.addNewAnonUser("6E96D72B-850C-419A-B1AD-4FE08F40210E"));
        System.out.println(api.getCampaignHasBeacon());
    }
    */
}