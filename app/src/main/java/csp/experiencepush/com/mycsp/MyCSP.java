package csp.experiencepush.com.mycsp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.Region;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;


public class MyCSP extends Activity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    private int imgPos = 0;
    private static final String TAG = "MyActivity";
    private DownloadListingsTask task;
    private Location userLocation;
    public ArrayList<Listing> listings;
    private LocationClient mLocationClient;
    private ProgressDialog dialog;
    public AQuery aq;
    PushRESTAPI api = new PushRESTAPI();
    private String PREFS_NAME = "CSPPrefsFile";
    public SharedPreferences settings;
    Context context = this;
    public ArrayList<String[]> campaigns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_csp);

        settings = getSharedPreferences(PREFS_NAME, 0);
        final SharedPreferences.Editor editor = settings.edit();

        String UUID= settings.getString("userUUID", "-1");
        if (UUID.equals("-1")){
            Set<String> stringSet = new HashSet<String>();
            editor.putStringSet("Favorites", stringSet);
            UUID = java.util.UUID.randomUUID().toString();
            final String finalUUID = UUID;
            Thread thread = new Thread(new Runnable(){
                @Override
                public void run(){
                    try {
                        String resp = api.addNewAnonUser(finalUUID);
                        if (resp.equals("1")){
                            editor.putString("userUUID", finalUUID);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    editor.putBoolean("allowBeacons", true);

                    editor.commit();
                }
            });
            thread.start();
        }




        dialog = new ProgressDialog(this);

        this.aq = new AQuery(this);

        final Context ctx = this;

        int highlightColor = ctx.getResources().getColor(R.color.highlight_color_filter);
        PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(highlightColor, PorterDuff.Mode.SRC_ATOP);

        Paint redHighLight = new Paint();
        redHighLight.setColorFilter(colorFilter);
        redHighLight.setAlpha(190);

        final ImageSwitcher switcher = (ImageSwitcher)findViewById(R.id.image_switcher);
        final ImageView image_a =(ImageView)findViewById(R.id.main_image_view_a);
        final ImageView image_b = (ImageView)findViewById(R.id.main_image_view_b);
        image_a.setColorFilter(colorFilter);
        image_b.setColorFilter(colorFilter);

        mLocationClient = new LocationClient(this, this, this);
        task = new DownloadListingsTask(this);

        List<MainMenuListItem> itemList = new ArrayList<MainMenuListItem>();
        itemList.add(new MainMenuListItem("Locations Near Me", "location"));
        itemList.add(new MainMenuListItem("Advanced Search", "search"));
        itemList.add(new MainMenuListItem("My Favorites", "star"));
        itemList.add(new MainMenuListItem("Preferences", "profile"));
        itemList.add(new MainMenuListItem("Tenant Info", "lightbulb"));

        ListView itemListView = (ListView)findViewById(R.id.main_menu_list_view);
        itemListView.setAdapter(new MainMenuAdapter(ctx, R.layout.main_screen_list_item, itemList));
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (listings == null){
                    Toast.makeText(MyCSP.this, "Loading Listings. Please Wait", Toast.LENGTH_SHORT).show();
                } else {
                    if (i == 2){
                        Filter filter = new Filter();
                        filter.setFavorite(true);

                        ArrayList<Listing> filteredListings = filter.filterListings(listings.toArray(new Listing[listings.size()]), false, context);

                        Intent results = new Intent(ctx, DisplayListingResults.class);
                        results.putParcelableArrayListExtra("Listings", filteredListings);
                        startActivity(results);
                    } else if (i == 1){
                        Filter filter = new Filter(ctx);
// #Set user current location to filter
                        Intent advancedSearch = new Intent(ctx, ListingFilterSettings.class);
                        advancedSearch.putExtra("Source", "Search");
                        advancedSearch.putParcelableArrayListExtra("Listings", listings);
                        startActivity(advancedSearch);
                    } else if (i == 0){
                        if (userLocation == null){
// No Location Known
                        } else {
                            Filter filter = new Filter();
                            filter.userLocation = userLocation;
                            filter.setCheckLocation(true);

                            ArrayList<Listing> filteredListings = filter.filterListings(listings.toArray(new Listing[listings.size()]), false, context);

                            Intent results = new Intent(ctx, DisplayListingResults.class);
                            results.putParcelableArrayListExtra("Listings", filteredListings);
                            startActivity(results);
                        }
                    } else if (i == 3){
                        Intent preferences = new Intent(ctx, PreferenceActivity.class);
                        startActivity(preferences);
                    } else if (i == 4){
                        Intent info = new Intent(ctx, InfoActivity.class);
                        startActivity(info);
                    }
                }
            }
        });

        final int[] imgArray = { R.drawable.background, R.drawable.scroll_b, R.drawable.scroll_a, R.drawable.scroll_c };

        Log.v(TAG, "index: "+imgPos);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                MyCSP.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imgPos += 1;
                        int res = imgArray[imgPos % 4];
                        if (switcher.getDisplayedChild() == 0){
                            image_b.setImageResource(res);
                            switcher.showNext();
                        } else {
                            image_a.setImageResource(res);
                            switcher.showPrevious();
                        }
                    }
                });
            }
        }, 10000, 10000);


    }

    private void writeListingsToDir(){
        for (Listing list: listings){

            String address = list.getAddress();
            if (address != null){
                String filename = Integer.toString(list.getUnitID());
                ObjectOutput out = null;
                try {
                    File toFile;
                    toFile = new File(this.getDir("Listings", MODE_PRIVATE),filename);
                    out = new ObjectOutputStream(new FileOutputStream(toFile));
                    out.writeObject(list);
                    out.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onStart(){
        super.onStart();

        mLocationClient.connect();

        if (listings == null){
            this.dialog.setMessage("Loading Listings");
            this.dialog.show();
            boolean write = false;
            File listingDir = this.getDir("Listings", MODE_PRIVATE);
            String[] listingDirFiles = listingDir.list();
            if (listingDirFiles.length == 0){
                write = true;
            } else {
                File first = new File(listingDir, listingDirFiles[0]);
                Date modifified = new Date(first.lastModified());
                Date now = new Date();
                long difference = now.getTime() - modifified.getTime();
                if (difference >= 86400000){
                    write = true;
                }
            }
            //write = true;
            if (write){
                task.execute("http://experiencepush.com/csp_portal/rest/?PUSH_ID=123&call=getAllListings");
            } else {
                listings = new ArrayList<Listing>();
                for (String file: listingDirFiles){
                    FileInputStream fis = null;
                    try {
                        File readFile = new File(listingDir, file);
                        fis = new FileInputStream(readFile);
                        ObjectInputStream is = new ObjectInputStream(fis);
                        Listing toAdd = (Listing) is.readObject();
                        listings.add(toAdd);
                        is.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (StreamCorruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                try {
                    new LoadCampaignsTask(this).execute(this.listings);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        Intent intent = getIntent();
        Uri data = intent.getData();
        System.out.print(data);
        if (data != null){

            String query = data.getQueryParameter("Listings");
            if (query != null){
                String[] listingsFromURL = query.split(",");
                int[] units = new int[listingsFromURL.length];
                int cnt = 0;
                for (String unitString: listingsFromURL){
                    units[cnt] = Integer.parseInt(unitString);
                    cnt++;
                }
                String title;

                Filter filter = new Filter();
                filter.setUnitIDS(units);

                final ArrayList<Listing> filteredListings = filter.getSpecific(listings.toArray(new Listing[listings.size()]), context, false);

                if (filteredListings.size() > 0){
                    if (filteredListings.size() == 1){

                        Intent showDetails= new Intent(context, ListingDetailActivity.class);
                        showDetails.putExtra("Listing", (Parcelable) filteredListings.get(0));
                        startActivity(showDetails);

                    } else {
                        Intent results = new Intent(context, DisplayListingResults.class);
                        results.putParcelableArrayListExtra("Listings", filteredListings);
                        startActivity(results);
                    }
                }


            }
        }
    }

    @Override
    protected void onStop(){
        mLocationClient.disconnect();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_cs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
        userLocation = mLocationClient.getLastLocation();
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static class LoadCampaignsTask extends AsyncTask<ArrayList<Listing>, Void, Boolean>{

        Activity activity;
        JSONArray beaconData;
        Map<String, Region> filteredBeacons;
        public LoadCampaignsTask(Activity myActivity){
            this.activity= myActivity;
        }

        @Override
        protected Boolean doInBackground(ArrayList<Listing>... listingsIn) {

            Map<String, Region> beaconDictionary = new HashMap<String, Region>();
            try {
                beaconData = new PushRESTAPI().getAllBeacons();
                if (beaconData != null){
                    for (int i = 0; i < beaconData.length(); i++){
                        JSONArray beaconArray = null;
                        try {
                            beaconArray = (JSONArray)beaconData.get(i);
                            Region toAdd;
                            String unique = null;
                            String uuid = null;
                            unique = (String)beaconArray.get(0);
                            uuid = (String)beaconArray.get(2);
                            int major = (Integer)beaconArray.get(3);
                            int minor = (Integer)beaconArray.get(4);
                            toAdd = new Region(unique, Identifier.parse(uuid), Identifier.fromInt(major), Identifier.fromInt(minor));
                            beaconDictionary.put(unique, toAdd);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    System.out.println("Not Null");
                }
                if (beaconDictionary.size() > 0){
                    JSONArray campaignData = new PushRESTAPI().getCampaignHasBeacon();
                    ArrayList<String[]> campaigns = new ArrayList<String[]>();
                    if (campaignData != null){
                        for (int i = 0; i < campaignData.length(); i++){
                            JSONArray campaign = (JSONArray)campaignData.get(i);
                            // Campaign Format {campaignID, unitID, beaconID}
                            String[] newCampaign = {String.valueOf((Integer)campaign.get(0)), (String)campaign.get(1), (String)campaign.get(2)};
                            campaigns.add(newCampaign);
                        }
                    } else {
// Handle Campaign Failure
                    }
                    if (campaigns.size() > 0){
                        filteredBeacons = new HashMap<String, Region>();
                        Map<String, ArrayList<String>> beaconsToListings = new HashMap<String, ArrayList<String>>();

                        for(String[] campaign: campaigns){
                            filteredBeacons.put(campaign[2], beaconDictionary.get(campaign[2]));
                            ArrayList<String> newList;
                            if (beaconsToListings.containsKey(campaign[2])){
                                newList = beaconsToListings.get(campaign[2]);
                            } else {
                                newList = new ArrayList<String>();
                            }
                            newList.add(campaign[1]);
                            beaconsToListings.put(campaign[2], newList);
                        }
// Send Maps and do beacon stuff
                        ((PushListener)activity.getApplicationContext()).beaconsToListings = beaconsToListings;
                        ((PushListener)activity.getApplicationContext()).campaigns = campaigns;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }

        protected void onPostExecute(Boolean result){
            if (filteredBeacons != null){
                ((PushListener)activity.getApplicationContext()).listenForBeaconsWithInterval(filteredBeacons, 30);
            }
        }
    }

    public static class DownloadListingsTask extends AsyncTask<String, Void, JSONArray> {

        Context activity;
        JSONArray favorites;
        List<String> favsList;
        SharedPreferences prefs;

        public DownloadListingsTask(Context myActivity) {
            this.activity= myActivity;
            prefs = activity.getSharedPreferences("CSPPrefsFile", 0);
        }

        protected JSONArray doInBackground(String... urls){
            JSONArray jObject = null;

            String jsonString = downloadFileFromInternet(urls[0]);

            if (jsonString != null){
                try {
                    Map<String, String> listings;

                    jObject = new JSONArray(jsonString);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            try {
                String UUID = ((MyCSP)activity).settings.getString("userUUID", "-1");
                favorites = new PushRESTAPI().getUserFavorites(UUID);

                String[] JSONString = favorites.toString().replace("[\"", "").replace("\"]", "").split("\",\"");
                favsList = Arrays.asList(JSONString);

                SharedPreferences settings = activity.getSharedPreferences("CSPPrefsFile", 0);

                List<String> oldFavs = new ArrayList<String>(settings.getStringSet("Favorites", null));
                for (String oldFav: oldFavs){
                    if (!(favsList.contains(oldFav))){
                        favsList.add(oldFav);
                        new PushRESTAPI().addUserFavorite(settings.getString("userUUID", "-1"), oldFav);
                    }
                }
                SharedPreferences.Editor editor = settings.edit();
                editor.putStringSet("Favorites", new HashSet<String>(favsList));
                editor.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (jObject != null){
                ArrayList<Listing> allListings = new ArrayList<Listing>();

                for (int i = 0; i < jObject.length(); i++){
                    JSONObject jsonInfo = null;
                    try {
                        jsonInfo = jObject.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        Map<String, Object> info = toMap(jsonInfo);
                        info.put("Context", activity);
                        info.put("AQuery", ((MyCSP)activity).aq);
                        Listing list = new Listing(info);
                        if (list != null){
                            if (favsList.contains(String.valueOf(list.getUnitID()))){
                                list.favorite = true;
                            }
                            allListings.add(list);
                            String address = list.getAddress();
                            String filename = Integer.toString(list.getUnitID());
                            ObjectOutput out = null;
                            try {
                                File toFile;
                                toFile = new File(activity.getDir("Listings", MODE_PRIVATE),filename);
                                out = new ObjectOutputStream(new FileOutputStream(toFile));
                                out.writeObject(list);
                                out.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                ((MyCSP)(this.activity)).listings = allListings;
            }

            return jObject;
        }



        private String downloadFileFromInternet(String url)
        {
            if(url == null /*|| url.isEmpty() == true*/)
                new IllegalArgumentException("url is empty/null");
            StringBuilder sb = new StringBuilder();
            InputStream inStream = null;
            try
            {
                url = urlEncode(url);
                URL link = new URL(url);
                inStream = link.openStream();
                int i;
                int total = 0;
                byte[] buffer = new byte[8 * 1024];
                while((i=inStream.read(buffer)) != -1)
                {
                    if(total >= (1024 * 1024))
                    {
                        return "";
                    }
                    total += i;
                    sb.append(new String(buffer,0,i));
                }
            }catch(Exception e )
            {
                e.printStackTrace();
                return null;
            }catch(OutOfMemoryError e)
            {
                e.printStackTrace();
                return null;
            }
            return sb.toString();
        }

        private String urlEncode(String url)
        {
            if(url == null /*|| url.isEmpty() == true*/)
                return null;
            url = url.replace("[", "");
            url = url.replace("]","");
            url = url.replaceAll(" ","%20");
            return url;
        }

        protected void onPreExecute(){
        }

        protected void onPostExecute(JSONArray result){
            if (((MyCSP)activity).dialog.isShowing()) {
                ((MyCSP)activity).dialog.dismiss();
            }
            try {
                new LoadCampaignsTask((MyCSP) activity).execute((((MyCSP)activity).listings));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static Map<String, Object> toMap(JSONObject object) throws JSONException {
            Map<String, Object> map = new HashMap();
            Iterator keys = object.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                map.put(key, fromJson(object.get(key)));
            }
            return map;
        }

        private static Object fromJson(Object json) throws JSONException {
            if (json == JSONObject.NULL) {
                return null;
            } else if (json instanceof JSONObject) {
                return toMap((JSONObject) json);
            } else if (json instanceof JSONArray) {
                return toList((JSONArray) json);
            } else {
                return json;
            }
        }

        public static List toList(JSONArray array) throws JSONException {
            List list;
            list = new ArrayList();
            for (int i = 0; i < array.length(); i++) {
                list.add(fromJson(array.get(i)));
            }
            return list;
        }

    }
}
