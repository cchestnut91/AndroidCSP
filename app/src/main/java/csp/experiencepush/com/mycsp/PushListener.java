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
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cchestnut on 10/20/14.
 */
public class PushListener extends Application implements BootstrapNotifier, RangeNotifier, BeaconConsumer, MonitorNotifier {
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
    public Map<String, Date> seenBeacons;
    public ArrayList<String> campaignIDs;
    Map<String, Region> startWhenReady = null;

// Notifications Properties
    public ArrayList<Listing> listings = new ArrayList<Listing>();
    private int beaconInterval;
    ArrayList<Listing> filteredListings;

    Region mAllBeaconsRegion = new Region("all beacons", Identifier.parse("AAAAAAAA-BBBB-BBBB-CCCC-CCCCDDDDDDDD"), null, null);

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "App started up");

        seenBeacons = readBeaconsSeen();

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
        if (startWhenReady != null){
            listenForBeacons(startWhenReady);
        }
        beaconManager.setMonitorNotifier(this);
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
        if (managerReady) {
            ArrayList<Region> toRange = new ArrayList<Region>();
            for (Region beaconRegion : beacons.values()) {
                toRange.add(beaconRegion);
                beaconRegions.put(beaconRegion.getId1().toString(), beaconRegion);
                try {
                    beaconManager.startMonitoringBeaconsInRegion(beaconRegion);
                } catch (RemoteException e) {   }
            }
            //regionBootstrap = new RegionBootstrap(this, toRange);
        } else {
            startWhenReady = beacons;
        }
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
                .setTicker("Nearby Listings")
                .setSmallIcon(R.drawable.ic_launcher_small)
                .setContentIntent(pendingIntent)
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        manager.notify(0, notification);
    }

    private void writeBeaconsSeen(String key){
        File listingDir = this.getDir("SeenBeacons", MODE_PRIVATE);
        ObjectOutput out = null;
        try {
            File toWrite = new File(listingDir, key);
            out = new ObjectOutputStream(new FileOutputStream(toWrite));
            out.writeObject(seenBeacons.get(key));
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Date> readBeaconsSeen(){
        int priv = this.MODE_PRIVATE;
        File listingDir = getDir("SeenBeacons", 0);
        Map<String, Date> seen = new HashMap<String, Date>();
        String[] keys = listingDir.list();
        for (String key: keys){
            FileInputStream fis = null;
            try {
                File readFile = new File(listingDir, key);
                fis = new FileInputStream(readFile);
                ObjectInputStream is = new ObjectInputStream(fis);
                Date toAdd = (Date) is.readObject();
                seen.put(key, toAdd);
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
        return seen;
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
        boolean ret;
        if (seenBeacons.containsKey(uuid)){
            Date last = seenBeacons.get(uuid);
            Date now = new Date();
            long since = now.getTime() - last.getTime();
            ret = since >= beaconInterval * 1000;
        } else {
            ret = true;
        }
        return ret;
    }

    public void addBeaconToSeen(Region beacon){
        seenBeacons.put(beacon.getUniqueId(), new Date());
        writeBeaconsSeen(beacon.getUniqueId());
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