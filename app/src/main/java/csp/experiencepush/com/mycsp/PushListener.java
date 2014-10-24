package csp.experiencepush.com.mycsp;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cchestnut on 10/20/14.
 */
public class PushListener extends Application implements BootstrapNotifier, RangeNotifier, BeaconConsumer {
// Class Properties
    public static final String TAG = ".MyCSP";
    Context appContext = this;

// Beacon Manager Properties
    public BeaconManager beaconManager;
    public RegionBootstrap regionBootstrap;
    public BackgroundPowerSaver backgroundPowerSaver;
    private boolean haveDetectedBeaconsSinceBoot = false;
    boolean managerReady = false;

// Campaign Monitoring Properties
    public ArrayList<String[]> campaigns = new ArrayList<String[]>();
    public Map<String, ArrayList<String>> beaconsToListings;
    public Map<String, Region> beaconRegions = new HashMap<String, Region>();
    public Map<String, Date> seenBeacons = new HashMap<String, Date>();
    public ArrayList<String> campaignIDs;

// Notifications Properties
    public ArrayList<Listing> listings = new ArrayList<Listing>();
    private int beaconInterval;
    ArrayList<Listing> filteredListings;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "App started up");

        beaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        /*
        mAllBeaconsRegion = new Region("all beacons", Identifier.parse("AAAAAAAA-BBBB-BBBB-CCCC-CCCCDDDDDDDD"), null, null);
        regionBootstrap = new RegionBootstrap(this, mAllBeaconsRegion);
        */

        beaconManager.setBackgroundScanPeriod(5000);
        beaconManager.setBackgroundBetweenScanPeriod(10000);

        beaconManager.bind(this);

    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(this);
        managerReady = true;
    }

    @Override
    public void didEnterRegion(Region region) {
        Log.d(TAG, "Got a didEnterRegion call");
        try {
            beaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void listenForBeacons(Map<String, Region> beacons) {
        ArrayList<Region> toRange = new ArrayList<Region>();
        for (Region beaconRegion : beacons.values()) {
            toRange.add(beaconRegion);
            beaconRegions.put(beaconRegion.getId1().toString(), beaconRegion);
        }

        regionBootstrap = new RegionBootstrap(this, toRange);
    }

    public void listenForBeaconsWithInterval(Map<String, Region> beacons, int seconds){
        beaconInterval = seconds;
        listenForBeacons(beacons);
    }

    public void stopListening(){
        for (Region region: beaconRegions.values()){
            try {
                beaconManager.stopMonitoringBeaconsInRegion(region);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        regionBootstrap = null;
    }

    public void stopListeningForBeaconsWithUUID(String uuid){
        if (beaconRegions.containsKey(uuid)){
            try {
                beaconManager.stopMonitoringBeaconsInRegion(beaconRegions.get(uuid));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            beaconRegions.remove(uuid);
        }
    }

    public void sendNotificationWithRegion(Region region){
        SharedPreferences prefs = this.getSharedPreferences("CSPPrefsFile", 0);
        if (prefs.getBoolean("allowBeacons", true)){
            if (shouldSendNotificationForRegion(region.getUniqueId())){
                addBeaconToSeen(region);

                ArrayList<String> listingsForBeacon = beaconsToListings.get(region.getUniqueId());

                if (listingsForBeacon != null){
                    int[] unitIDs = new int[listingsForBeacon.size()];
                    int cnt = 0;
                    for (String unitID: listingsForBeacon){
                        unitIDs[cnt] = Integer.parseInt(unitID);
                        cnt++;
                    }
                    String urlQueryString = "";
                    Filter initialFilter = new Filter(this);
                    initialFilter.setUnitIDS(unitIDs);
                    getListings(listingsForBeacon.toArray(new String[listingsForBeacon.size()]));

                    if (listings != null){
                        filteredListings = initialFilter.getSpecific(this.listings.toArray(new Listing[this.listings.size()]), this, false);
                        if (filteredListings.size() != 0){
                            for (Listing listing: filteredListings){
                                if (urlQueryString.equals("")){
                                    urlQueryString = String.valueOf(listing.getUnitID());
                                } else {
                                    urlQueryString = urlQueryString + "," + String.valueOf(listing.getUnitID());
                                }
                            }

                            String holdURL = "cspmgmt://?listings="+urlQueryString;
                            campaignIDs = new ArrayList<String>();
                            for (String[] campaign: this.campaigns){
                                // Campaign Format {campaignID, unitID, beaconID}
                                if (campaign[2].equals(region.getUniqueId())){
                                    campaignIDs.add(campaign[0]);
                                }
                            }
                            displayNearbyNotification(holdURL);
                        }
                    }
                }
            }



        }
    }

    public Map<String, String> parseQueryString(String queryString){
        Map<String, String> ret = new HashMap<String, String>();

        queryString = queryString.substring(queryString.indexOf("?") + 1);
        String[] pairs = queryString.split("&");
        for (String pair: pairs){
            String[] elements = pair.split("=");
            ret.put(elements[0], elements[1]);
        }

        return ret;
    }

    public void displayNearbyNotification(String url){
        Log.d(TAG, "Prepping Notification");

        String[] params = parseQueryString(url).get("listings").split(",");
        String alertBody = null;
        if (params.length == 1){
            alertBody = "There's a listing nearby you may be interested in";
        } else {
            alertBody = "There are " + params.length + " listings nearby you may be interested in";
        }

        Intent intent;
        if (filteredListings.size() == 1){
            intent = new Intent(this, ListingDetailActivity.class);
            intent.putExtra("Listing", (Parcelable) filteredListings.get(0));
        } else {
            intent = new Intent(this, DisplayListingResults.class);
            intent.putParcelableArrayListExtra("Listings", filteredListings);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification.Builder(this)
                .setContentTitle("Nearby Listings")
                .setContentText(alertBody)
                .setSmallIcon(R.drawable.ic_launcher).setContentIntent(pendingIntent)
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        manager.notify(0, notification);
    }

    private void getListings(String[] listingDirFiles){
        File listingDir = this.getDir("Listings", MODE_PRIVATE);
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
    }

    public boolean shouldSendNotificationForRegion(String uuid){
        if (seenBeacons.containsKey(uuid)){
            return Math.abs(seenBeacons.get(uuid).compareTo(new Date())) >= beaconInterval;
        }
        return true;
    }

    public void addBeaconToSeen(Region beacon){
        seenBeacons.put(beacon.getUniqueId(), new Date());
// Save to local
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> arg0, Region arg1){
        Log.d(TAG, "Did Range");
        if (arg0.size() > 0){
            sendNotificationWithRegion(arg1);
        }
    }

    @Override
    public void didExitRegion(Region region) {
        try {
            beaconManager.stopRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {

    }
}